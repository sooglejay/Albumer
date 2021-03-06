package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.api.newperson.NewPersonResponseBean;
import sooglejay.youtu.api.newperson.NewPersonUtil;
import sooglejay.youtu.bean.ContactBean;
import sooglejay.youtu.constant.ExtraConstants;
import sooglejay.youtu.constant.IntConstant;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.db.ContactDao;
import sooglejay.youtu.event.BusEvent;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.CacheUtil;
import sooglejay.youtu.utils.GetGroupIdsUtil;
import sooglejay.youtu.utils.GetTagUtil;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.utils.ProgressDialogUtil;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.youtu.sign.Base64Util;

/**
 * Created by JammyQtheLab on 2015/11/28.
 */
public class AddNewPersonActivity extends BaseActivity {
    private static final String EXTRA_FACE_IMAGE_PATH = "face_bitmap";
    private static final String EXTRA_FACE_IDENTIFY_DATAS = "identifyItems";
    private static final int ACTION_CreateNewGroupActivity = 1000;
    private Activity activity;
    private String imageFilePath;//图片文件的地址
    private ArrayList<IdentifyItem> identifyItems;//人脸识别 置信度 top5列表
    private HashMap<String, ArrayList<IdentifyItem>> mIdentifiedFaceBitMapCache = null;
    private CacheUtil cacheUtil;

    private ContactDao contactDao;

    public static void startActivity(Context context, String imageFilePath, ArrayList<IdentifyItem> identifyItems) {
        Intent intent = new Intent(context, AddNewPersonActivity.class);
        intent.putExtra(EXTRA_FACE_IMAGE_PATH, imageFilePath);
        intent.putParcelableArrayListExtra(EXTRA_FACE_IDENTIFY_DATAS, identifyItems);
        context.startActivity(intent);
    }

    private TitleBar titleBar;
    private RoundImageView ivAvatar;
    private EditText etName;
    private String groupStrFromIntent;
    private LinearLayout layout_choose_group_id;
    private TextView tv_group_name;
    private EditText etPhoneNumber;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2015-11-30 22:59:55 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        ivAvatar = (RoundImageView) findViewById(R.id.ivAvatar);
        etName = (EditText) findViewById(R.id.et_name);
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        layout_choose_group_id = (LinearLayout) findViewById(R.id.layout_choose_group_id);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_person_activity);

        activity = this;
        contactDao = new ContactDao(this);
        cacheUtil = new CacheUtil(this);
        mIdentifiedFaceBitMapCache = cacheUtil.getIdentifiedObjectFromFile();
        setUpView();
        setUpListener();
        doSomethng();
    }

    private void setUpView() {
        findViews();
        titleBar.initTitleBarInfo("添加人脸联系人", R.drawable.arrow_left, -1, "", "确定");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {
                if (TextUtils.isEmpty(groupStrFromIntent)) {
                    Toast.makeText(activity, "群组名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String phoneStr = etPhoneNumber.getText().toString();
                final String nameStr = etName.getText().toString();
                final String person_id = System.currentTimeMillis() + "";
                final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(activity);
                new AsyncTask<String, Bitmap, Bitmap>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialogUtil.show("正在添加人脸");
                    }

                    @Override
                    protected Bitmap doInBackground(String... params) {
                        Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                        Bitmap bitmap = ImageUtils.getResizedBitmap(tempBitmap, IntConstant.IMAGE_SIZE, IntConstant.IMAGE_SIZE);
                        return bitmap;
                    }

                    @Override
                    protected void onPostExecute(final Bitmap bitmap) {
                        if (bitmap != null) {
                            final String tag = GetTagUtil.getTag(nameStr, phoneStr, groupStrFromIntent);
                            ArrayList<String> arrayList = GetGroupIdsUtil.getGroupIdArrayList(groupStrFromIntent);
                            if (arrayList.size() > 0) {
                                PreferenceUtil.save(activity, PreferenceConstant.IDENTIFY_GROUP_NAME, arrayList.get(0));
                            }
                            NewPersonUtil.newPerson(activity, NetWorkConstant.APP_ID, arrayList, person_id, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), nameStr, tag, new NetCallback<NewPersonResponseBean>(activity) {
                                @Override
                                public void onFailure(RetrofitError error, String message) {
                                    progressDialogUtil.hide();
                                    Toast.makeText(activity, "请求超时,请确保网络良好再重试", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void success(NewPersonResponseBean newPersonResponseBean, Response response) {
                                    progressDialogUtil.hide();
                                    ContactBean bean = new ContactBean();
                                    bean.setUser_name(nameStr);
                                    bean.setPhoneNumber(phoneStr);
                                    bean.setTag(tag);
                                    bean.setImage_path(imageFilePath);
                                    bean.setPerson_id(person_id);
                                    contactDao.add(bean);
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            EventBus.getDefault().post(new BusEvent(BusEvent.MSG_REFRESH));
                                            Toast.makeText(activity, "添加成功！", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            cacheUtil.clearIdentifyCache();
                                            return null;
                                        }
                                    }.execute();

                                }
                            });

                        } else {
                            progressDialogUtil.hide();
                        }
                    }
                }.execute(imageFilePath);
            }
        });
    }

    private void setUpListener() {
        layout_choose_group_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewGroupActivity.startActivity(activity, ACTION_CreateNewGroupActivity);
            }
        });
    }

    private void doSomethng() {
        imageFilePath = getIntent().getStringExtra(EXTRA_FACE_IMAGE_PATH);
        identifyItems = getIntent().getParcelableArrayListExtra(EXTRA_FACE_IDENTIFY_DATAS);
        if (imageFilePath != null) {
            ImageLoader.getInstance().displayImage("file://" + imageFilePath, ivAvatar, ImageUtils.getOptions());
        }
        tv_group_name.setText("群组名称");


        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void value) {
                super.onPostExecute(value);
                if (etName != null) {
                    etName.requestFocus();
                    InputMethodManager imm = (InputMethodManager) etName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                }
            }
        }.execute(400);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_CreateNewGroupActivity:
                    groupStrFromIntent = data.getStringExtra(ExtraConstants.EXTRA_CREATE_NEW_GROUP);
                    tv_group_name.setText(groupStrFromIntent.replaceAll(GetGroupIdsUtil.reg, " "));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
