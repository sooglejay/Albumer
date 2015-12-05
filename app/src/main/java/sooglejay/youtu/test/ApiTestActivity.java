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
                delAllPersonsByGroupIds();
            }
        });
    }

    private void delPersons(List<String> person_ids)
    {
        for (String person_id :person_ids) {
            DelPersonUtil.delPerson(this, NetWorkConstant.APP_ID, person_id, new NetCallback<DelPersonResponseBean>(this) {
                @Override
                public void onFailure(RetrofitError error, String message) {

                }

                @Override
                public void success(DelPersonResponseBean delPersonResponseBean, Response response) {

                }
            });
        }
    }


    private void delAllPersonsByGroupIds() {
        GetGroupIdsUtil.getGroupIds(this, NetWorkConstant.APP_ID, new NetCallback<GetGroupIdsResponseBean>(this) {
            @Override
            public void onFailure(RetrofitError error, String message) {

            }

            @Override
            public void success(GetGroupIdsResponseBean getGroupIdsResponseBean, Response response) {
                //获取到所有的组ids
                if (getGroupIdsResponseBean.getGroup_ids() != null) {
                    groupidsList.clear();
                    groupidsList.addAll(getGroupIdsResponseBean.getGroup_ids());
                }
                personidsList.clear();

                //根据每个组id 去获取所有用户的信息
                for (int i = 0; i < groupidsList.size(); i++) {
                    getAllPersonIds(groupidsList.get(i));
                }
            }
        });
    }

    private void getAllPersonIds(String group_id)
    {
        GetPersonIdsUtil.getPersonIds(this, NetWorkConstant.APP_ID, group_id, new NetCallback<GetPersonIdsResponseBean>(this) {
            @Override
            public void onFailure(RetrofitError error, String message) {

            }

            @Override
            public void success(GetPersonIdsResponseBean getPersonIdsResponseBean, Response response) {
                //获取了一个组用户的信息之后就删除这个组的用户
                personidsList.addAll(getPersonIdsResponseBean.getPerson_ids());
                delPersons(getPersonIdsResponseBean.getPerson_ids());
            }
        });
    }


}
