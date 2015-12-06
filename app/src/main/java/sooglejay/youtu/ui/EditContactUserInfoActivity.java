package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.setinfo.SetInfoResponseBean;
import sooglejay.youtu.api.setinfo.SetInfoUtil;
import sooglejay.youtu.bean.ContactBean;
import sooglejay.youtu.constant.ExtraConstants;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.db.ContactDao;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.CacheUtil;
import sooglejay.youtu.utils.GetGroupIdsUtil;
import sooglejay.youtu.utils.GetTagUtil;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.utils.ProgressDialogUtil;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/11/28.
 */
public class EditContactUserInfoActivity extends BaseActivity {
    private static final String EXTRA_BEAN = "face_bitmap";
    private static final int ACTION_CreateNewGroupActivity = 1000;
    private Activity activity;
    private ContactBean contactBean;//

    private ContactDao contactDao;

    private CacheUtil cacheUtil;

    public static void startActivity(Context context, ContactBean bean) {
        Intent intent = new Intent(context, EditContactUserInfoActivity.class);
        intent.putExtra(EXTRA_BEAN, bean);
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
        setContentView(R.layout.activity_edit_face_user_info);
        activity = this;
        contactBean = getIntent().getParcelableExtra(EXTRA_BEAN);
        contactDao = new ContactDao(this);
        cacheUtil = new CacheUtil(this);
        setUpView();
        setUpListener();
        doSomethng();
    }

    private void setUpView() {
        findViews();
        titleBar.initTitleBarInfo("编辑人脸联系人", R.drawable.arrow_left, -1, "", "确定");
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

                if (TextUtils.isEmpty(nameStr)) {
                    Toast.makeText(activity, "姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phoneStr)) {
                    Toast.makeText(activity, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }


                String newTag = GetTagUtil.getTag(nameStr, phoneStr, groupStrFromIntent);
                final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(activity);
                progressDialogUtil.show("正在提交人脸信息...");
                contactBean.setUser_name(nameStr);
                contactBean.setPhoneNumber(phoneStr);

                SetInfoUtil.setInfo(activity, NetWorkConstant.APP_ID, contactBean.getPerson_id(), nameStr, newTag, new NetCallback<SetInfoResponseBean>(activity) {
                    @Override
                    public void onFailure(RetrofitError error, String message) {
                        progressDialogUtil.hide();
                        Toast.makeText(activity, "请求超时,请确保网络良好再重试", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void success(SetInfoResponseBean setInfoResponseBean, Response response) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                contactDao.uodateBean(contactBean);
                                cacheUtil.clearIdentifyCache();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                progressDialogUtil.hide();
                                Toast.makeText(activity, "修改成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }.execute();
                    }
                });
            }
        });

    }

    private void setUpListener() {
        tv_group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewGroupActivity.startActivity(activity, ACTION_CreateNewGroupActivity);
            }
        });
    }

    private void doSomethng() {
        if (contactBean != null) {
            ImageLoader.getInstance().displayImage("file://" + contactBean.getImage_path(), ivAvatar, ImageUtils.getOptions());
            String tag = contactBean.getTag();
            groupStrFromIntent = GetTagUtil.getGroupIds(tag);//人脸识别后，检测到的该人脸属于的组id
            tv_group_name.setText(GetGroupIdsUtil.removeRegex(groupStrFromIntent));
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
                    tv_group_name.setText(GetGroupIdsUtil.removeRegex(groupStrFromIntent));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
