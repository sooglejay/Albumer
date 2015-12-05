package sooglejay.youtu.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import de.greenrobot.event.EventBus;
import sooglejay.youtu.R;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.constant.StringConstant;
import sooglejay.youtu.event.BusEvent;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/12/5.
 */
public class ModifyInfoActivity extends BaseActivity {

    private TitleBar titleBar;
    private EditText et_signature;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        activity = this;
        titleBar = (TitleBar)findViewById(R.id.title_bar);
        et_signature = (EditText)findViewById(R.id.et_signature);

        String s = PreferenceUtil.load(this,PreferenceConstant.USER_SIGNATURE, StringConstant.default_signature);
        et_signature.setText(s);

        titleBar.initTitleBarInfo("编辑签名",R.drawable.arrow_left,-1,"","保存");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {
                String signatureStr = et_signature.getText().toString();
                PreferenceUtil.save(activity, PreferenceConstant.USER_SIGNATURE,signatureStr);
                EventBus.getDefault().post(new BusEvent(BusEvent.MSG_MODIFY_USER_INFO));
                finish();
            }
        });
    }
}
