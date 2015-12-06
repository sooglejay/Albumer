package sooglejay.youtu.test;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.delperson.DelPersonResponseBean;
import sooglejay.youtu.api.delperson.DelPersonUtil;
import sooglejay.youtu.api.getgroupids.GetGroupIdsResponseBean;
import sooglejay.youtu.api.getgroupids.GetGroupIdsUtil;
import sooglejay.youtu.api.getpersonids.GetPersonIdsResponseBean;
import sooglejay.youtu.api.getpersonids.GetPersonIdsUtil;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.ui.BaseActivity;

/**
 * Created by JammyQtheLab on 2015/12/5.
 */
public class ApiTestActivity extends BaseActivity {
    private ArrayList<String> groupidsList = new ArrayList<>();
    private ArrayList<String> personidsList = new ArrayList<>();
    private TextView tv_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaa_test_api);
        tv_test = (TextView)findViewById(R.id.tv_test);
        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

}
