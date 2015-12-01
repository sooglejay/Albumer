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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.api.newperson.NewPersonResponseBean;
import sooglejay.youtu.api.newperson.NewPersonUtil;
import sooglejay.youtu.constant.ExtraConstants;
import sooglejay.youtu.constant.IntConstant;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetGroupIdsUtil;
import sooglejay.youtu.utils.GetTagUtil;
import sooglejay.youtu.utils.ImageUtils;
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
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_person_activity);
        activity = this;
        setUpView();
        setUpListener();
        doSomethng();
    }

    private void setUpView()
    {
        findViews();
        titleBar.initTitleBarInfo("添加人脸联系人", R.drawable.arrow_left, -1, "", "确定");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                activity.finish();
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
                new AsyncTask<String, Bitmap, Bitmap>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
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
                            NewPersonUtil.newPerson(activity, NetWorkConstant.APP_ID, GetGroupIdsUtil.getGroupIdArrayList(groupStrFromIntent), person_id, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), nameStr, GetTagUtil.getTag(nameStr, phoneStr,groupStrFromIntent), new NetCallback<NewPersonResponseBean>(activity) {
                                @Override
                                public void onFailure(RetrofitError error, String message) {

                                }

                                @Override
                                public void success(NewPersonResponseBean newPersonResponseBean, Response response) {

                                }
                            });

                        }
                    }
                }.execute(imageFilePath);
            }
        });

    }
    private void setUpListener()
    {
        tv_group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewGroupActivity.startActivity(activity, groupStrFromIntent, ACTION_CreateNewGroupActivity);
            }
        });
    }

    private void doSomethng() {
        imageFilePath = getIntent().getStringExtra(EXTRA_FACE_IMAGE_PATH);
        identifyItems = getIntent().getParcelableArrayListExtra(EXTRA_FACE_IDENTIFY_DATAS);
        if (imageFilePath != null) {
            ImageLoader.getInstance().displayImage("file://" + imageFilePath, ivAvatar, ImageUtils.getOptions());
        }
        if (identifyItems != null && identifyItems.size() > 0) {

            String tag = identifyItems.get(0).getTag();
            groupStrFromIntent = GetTagUtil.getGroupIds(tag);//人脸识别后，检测到的该人脸属于的组id
            tv_group_name.setText(groupStrFromIntent.replaceAll(GetGroupIdsUtil.reg," "));
            etName.setText(GetTagUtil.getName(tag));
            etPhoneNumber.setText(GetTagUtil.getPhoneNumber(tag));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_CreateNewGroupActivity:
                    groupStrFromIntent = data.getStringExtra(ExtraConstants.EXTRA_CREATE_NEW_GROUP);
                    tv_group_name.setText(groupStrFromIntent.replaceAll(GetGroupIdsUtil.reg," "));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
