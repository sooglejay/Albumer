package sooglejay.youtu.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.adapter.MyContactsListAdapter;
import sooglejay.youtu.api.getgroupids.GetGroupIdsResponseBean;
import sooglejay.youtu.api.getgroupids.GetGroupIdsUtil;
import sooglejay.youtu.api.getinfo.GetInfoReponseBean;
import sooglejay.youtu.api.getinfo.GetInfoUtil;
import sooglejay.youtu.api.getpersonids.GetPersonIdsResponseBean;
import sooglejay.youtu.api.getpersonids.GetPersonIdsUtil;
import sooglejay.youtu.bean.ContactBean;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.db.ContactDao;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/12/4.
 */
public class MyContactsActivity extends BaseActivity {
    private TitleBar title_bar;
    private ListView list_view;
    private MyContactsListAdapter adapter;
    private ArrayList<ContactBean>datas = new ArrayList<>();


    private ArrayList<String>groupidsList = new ArrayList<>();
    private ArrayList<String>personidsList = new ArrayList<>();
    private ContactDao contactDao ;

    private boolean free = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);
        title_bar = (TitleBar)findViewById(R.id.title_bar);
        list_view = (ListView)findViewById(R.id.list_view);
        contactDao = new ContactDao(this);
        datas.addAll(contactDao.getAll());
        adapter = new MyContactsListAdapter(datas,this);
        list_view.setAdapter(adapter);


        title_bar.initTitleBarInfo("我的联系人", R.drawable.arrow_left, -1, "", "");
        title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {

            }
        });

    }
//
//    private void getAllGroupIds() {
//        GetGroupIdsUtil.getGroupIds(this, NetWorkConstant.APP_ID, new NetCallback<GetGroupIdsResponseBean>(this) {
//            @Override
//            public void onFailure(RetrofitError error, String message) {
//
//            }
//
//            @Override
//            public void success(GetGroupIdsResponseBean getGroupIdsResponseBean, Response response) {
//                if (getGroupIdsResponseBean.getGroup_ids() != null) {
//                    groupidsList.clear();
//                    groupidsList.addAll(getGroupIdsResponseBean.getGroup_ids());
//                }
//                personidsList.clear();
//                for (int i = 0; i < groupidsList.size(); i++) {
//                    getAllPersonIds(groupidsList.get(i));
//                }
//            }
//        });
//    }
//
//    private void getAllPersonIds(String group_id)
//    {
//        GetPersonIdsUtil.getPersonIds(this, NetWorkConstant.APP_ID, group_id, new NetCallback<GetPersonIdsResponseBean>(this) {
//            @Override
//            public void onFailure(RetrofitError error, String message) {
//
//            }
//
//            @Override
//            public void success(GetPersonIdsResponseBean getPersonIdsResponseBean, Response response) {
//                personidsList.addAll(getPersonIdsResponseBean.getPerson_ids());
//
//                for (int i = 0; i < getPersonIdsResponseBean.getPerson_ids().size(); i++) {
//
//                }
//
//            }
//        });
//    }
//
//    private void getInfo(String person_ids)
//    {
//        GetInfoUtil.getinfo(this, NetWorkConstant.APP_ID, person_ids, new NetCallback<GetInfoReponseBean>() {
//            @Override
//            public void onFailure(RetrofitError error, String message) {
//
//            }
//
//            @Override
//            public void success(GetInfoReponseBean getInfoReponseBean, Response response) {
//
//                ContactBean bean = new ContactBean();
//                bean.setImage_path();
//            }
//        });
//    }
//

}
