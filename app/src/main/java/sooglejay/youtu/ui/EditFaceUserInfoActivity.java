package sooglejay.youtu.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import sooglejay.youtu.R;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/11/28.
 */
public class EditFaceUserInfoActivity extends BaseActivity {
    private TitleBar titleBar;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_face_user_info);
        activity = this;
        titleBar = (TitleBar)findViewById(R.id.title_bar);
        titleBar.initTitleBarInfo("编辑",R.drawable.arrow_left,-1,"","");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                activity.finish();
            }

            @Override
            public void onRightButtonClick(View v) {

            }
        });
    }
}
