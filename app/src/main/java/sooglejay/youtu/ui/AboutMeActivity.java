package sooglejay.youtu.ui;

import android.os.Bundle;
import android.view.View;

import sooglejay.youtu.R;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/12/6.
 */
public class AboutMeActivity extends BaseActivity {

    private TitleBar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        titleBar = (TitleBar)findViewById(R.id.title_bar);
        titleBar.initTitleBarInfo("关于作者",R.drawable.arrow_left,-1,"","");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {

            }
        });
    }
}
