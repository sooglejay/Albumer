package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import sooglejay.youtu.R;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.constant.StringConstant;
import sooglejay.youtu.event.BusEvent;
import sooglejay.youtu.utils.CacheUtil;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.utils.ProgressDialogUtil;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.customswitch.SwitchButton;
import sooglejay.youtu.widgets.imagepicker.MultiImageSelectorActivity;


public class SettingActivity extends BaseActivity {

    private TitleBar title_bar = null;
    private TextView my_signature_count_tv;


    private LinearLayout avatar_group;
    private RoundImageView avatar_image;
    private LinearLayout signature_group;
    private LinearLayout clear_cache_group;
    private LinearLayout about_me_group;

    private LinearLayout layout_detect_face;
    private LinearLayout layout_identify;
    private LinearLayout layout_handle_dialog;

    private SwitchButton switch_detect_face;
    private SwitchButton switch_identify;
    private SwitchButton switch_handle_dialog;

    private Activity activity;
    private CacheUtil cacheUtil;



    private ArrayList<String> imageList = new ArrayList<>();
    private String resultPath;//图片最终位置
    @Override
    protected void onResume() {
        super.onResume();
        if (my_signature_count_tv != null)
        {
            my_signature_count_tv.setText(PreferenceUtil.load(this, PreferenceConstant.USER_SIGNATURE, StringConstant.default_signature));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        activity = this;
        cacheUtil = new CacheUtil(this);


        avatar_group = (LinearLayout) findViewById(R.id.avatar_group);
        avatar_image = (RoundImageView) findViewById(R.id.avatar_image);
        String avatarStr = PreferenceUtil.load(this,PreferenceConstant.USER_AVATAR,"");
        if(TextUtils.isEmpty(avatarStr))
        {
            avatar_image.setImageResource(R.drawable.test);
        }
        else {
            ImageLoader.getInstance().displayImage("file://" + avatarStr, avatar_image, ImageUtils.getOptions());
        }
        switch_detect_face = (SwitchButton) findViewById(R.id.switch_detect_face);
        switch_identify = (SwitchButton) findViewById(R.id.switch_identify);
        switch_handle_dialog = (SwitchButton) findViewById(R.id.switch_handle_dialog);

        layout_detect_face = (LinearLayout) findViewById(R.id.layout_detect_face);
        layout_identify = (LinearLayout) findViewById(R.id.layout_identify);
        layout_handle_dialog = (LinearLayout) findViewById(R.id.layout_handle_dialog);


        switch_identify.setChecked(PreferenceUtil.load(activity, PreferenceConstant.SWITCH_IDENTIFY, true));
        switch_detect_face.setChecked(PreferenceUtil.load(activity, PreferenceConstant.SWITCH_DETECT_FACE, true));
        switch_handle_dialog.setChecked(PreferenceUtil.load(activity, PreferenceConstant.SWITCH_DIALOG_PROGRESS_CANCELED_ON_TOUCH_OUTSIDE, true));

        signature_group = (LinearLayout) findViewById(R.id.signature_group);
        my_signature_count_tv = (TextView) findViewById(R.id.my_signature_count_tv);
        clear_cache_group = (LinearLayout) findViewById(R.id.clear_cache_group);
        about_me_group = (LinearLayout) findViewById(R.id.about_me_group);
        title_bar = (TitleBar) findViewById(R.id.title_bar);
        title_bar.initTitleBarInfo("设置", R.drawable.arrow_left, -1, "", "");
        title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {

            }
        });

        layout_detect_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch_detect_face.performClick();
            }
        });
        layout_identify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch_identify.performClick();
            }
        });

        layout_handle_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch_handle_dialog.performClick();
            }
        });

        switch_detect_face.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PreferenceUtil.save(activity, PreferenceConstant.SWITCH_DETECT_FACE, b);
            }
        });

        switch_identify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PreferenceUtil.save(activity, PreferenceConstant.SWITCH_IDENTIFY, b);
            }
        });

        switch_handle_dialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PreferenceUtil.save(activity, PreferenceConstant.SWITCH_DIALOG_PROGRESS_CANCELED_ON_TOUCH_OUTSIDE, b);
            }
        });



        clear_cache_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(activity);

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("提示").setMessage("清除缓存后，在浏览相册图片时，将重新对图片进行人脸检测和识别。这会产生大量用户流量，也会影响用户体验。\n您是否确定要这么做?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        cacheUtil.clearIdentifyCache();
                                        cacheUtil.clearDetectFaceCache();
                                        return null;
                                    }

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        progressDialogUtil.show("正在删除缓存文件...");

                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {

                                        super.onPostExecute(aVoid);
                                        progressDialogUtil.hide();
                                        Toast.makeText(activity, "缓存已清除！", Toast.LENGTH_SHORT).show();
                                    }
                                }.execute();


                            }
                        }).setNegativeButton("取消", null).create().show();


            }
        });


        avatar_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtils.startPickPhoto(activity, imageList, 1, false);
            }
        });


        signature_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, ModifyInfoActivity.class));
            }
        });

        about_me_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, AboutMeActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //若是从图库选择图
                case ImageUtils.REQUEST_CODE_PICK_IMAGE:
                    // 获取返回的图片列表
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    imageList.clear();
                    imageList.addAll(paths);
                    if (imageList.size() > 0) {
                        resultPath = ImageUtils.getImageFolderPath(activity)+File.separator+System.currentTimeMillis()+".jpg";
                        ImageUtils.cropImage(this, Uri.fromFile(new File(imageList.get(0))), resultPath, 1, 1);
                    }
                    break;

                //裁剪图片
                case ImageUtils.REQUEST_CODE_CROP_IMAGE:
                    //添加图片到list并且显示出来
                    //上传图片
                    if (!TextUtils.isEmpty(resultPath)) {
                        PreferenceUtil.save(activity,PreferenceConstant.USER_AVATAR,resultPath);
                        EventBus.getDefault().post(new BusEvent(BusEvent.MSG_MODIFY_USER_INFO));
                        ImageLoader.getInstance().displayImage("file://"+resultPath,avatar_image,ImageUtils.getOptions());
                    } else {
                        Toast.makeText(activity,"选择图片失败，请重试",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        } else {

        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
