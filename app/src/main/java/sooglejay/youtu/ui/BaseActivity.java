package sooglejay.youtu.ui;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import de.greenrobot.event.EventBus;
import sooglejay.youtu.utils.ScreenUtils;

/**
 *  activity 基类
 */
public class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ScreenUtils.setTranslucentStatus(this);
        //初始化eventBus
        EventBus.getDefault().register(this);

    }

    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onEvent(Object object){
    }

}
