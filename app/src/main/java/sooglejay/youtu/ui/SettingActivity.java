package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.update.UmengUpdateAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import sooglejay.youtu.R;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.utils.CacheUtil;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.utils.ProgressDialogUtil;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.customswitch.SwitchButton;


public class SettingActivity extends BaseActivity {

    private TitleBar title_bar = null;


    private LinearLayout avatar_group;
    private LinearLayout signature_group;
    private LinearLayout clear_cache_group;
    private SwitchButton switch_detect_face;
    private SwitchButton switch_identify;

    private Activity activity;
    private CacheUtil cacheUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        activity = this;
        cacheUtil = new CacheUtil(this);


        avatar_group = (LinearLayout)findViewById(R.id.avatar_group);
        switch_detect_face = (SwitchButton)findViewById(R.id.switch_detect_face);
        switch_identify = (SwitchButton)findViewById(R.id.switch_identify);


        switch_identify.setChecked( PreferenceUtil.load(activity, PreferenceConstant.SWITCH_IDENTIFY, true));
        switch_detect_face.setChecked( PreferenceUtil.load(activity, PreferenceConstant.SWITCH_DETECT_FACE, true));
        signature_group = (LinearLayout)findViewById(R.id.signature_group);
        clear_cache_group = (LinearLayout)findViewById(R.id.clear_cache_group);
        title_bar = (TitleBar)findViewById(R.id.title_bar);
        title_bar.initTitleBarInfo("设置",R.drawable.arrow_left,-1,"","");
        title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {

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
                    PreferenceUtil.save(activity, PreferenceConstant.SWITCH_IDENTIFY,b);
            }
        });
        clear_cache_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(activity);
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
                        Toast.makeText(activity,"缓存已清除！",Toast.LENGTH_SHORT).show();
                    }
                }.execute();

            }
        });


    }
}
