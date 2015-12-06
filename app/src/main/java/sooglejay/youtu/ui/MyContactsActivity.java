package sooglejay.youtu.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.adapter.MyContactsListAdapter;
import sooglejay.youtu.api.delperson.DelPersonResponseBean;
import sooglejay.youtu.api.delperson.DelPersonUtil;
import sooglejay.youtu.api.getgroupids.GetGroupIdsResponseBean;
import sooglejay.youtu.api.getgroupids.GetGroupIdsUtil;
import sooglejay.youtu.api.getinfo.GetInfoReponseBean;
import sooglejay.youtu.api.getinfo.GetInfoUtil;
import sooglejay.youtu.api.getpersonids.GetPersonIdsResponseBean;
import sooglejay.youtu.api.getpersonids.GetPersonIdsUtil;
import sooglejay.youtu.bean.ContactBean;
import sooglejay.youtu.bean.FocusBean;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.db.ContactDao;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.widgets.CircleButton;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/12/4.
 */
public class MyContactsActivity extends BaseActivity {
    private TitleBar title_bar;
    private SwipeRefreshLayout swipeLayout;
    private ListView list_view;
    private MyContactsListAdapter adapter;
    private ArrayList<ContactBean> datas = new ArrayList<>();


    private ArrayList<String> groupidsList = new ArrayList<>();
    private ArrayList<String> personidsList = new ArrayList<>();
    private ContactDao contactDao;


    private FrameLayout layout_operation;
    private CircleButton iv_cancel_image;
    private CircleButton iv_delete_image;
    private Animation animation_enter;
    private Animation animation_exit;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);
        activity = this;
        animation_enter = AnimationUtils.loadAnimation(this,
                R.anim.enter_from_bottom_200);
        animation_exit = AnimationUtils.loadAnimation(this,
                R.anim.exit_to_bottom_200);

        title_bar = (TitleBar) findViewById(R.id.title_bar);
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeLayout.setColorSchemeResources(R.color.base_color);
        list_view = (ListView) findViewById(R.id.list_view);
        layout_operation = (FrameLayout) findViewById(R.id.layout_operation);
        iv_cancel_image = (CircleButton) findViewById(R.id.iv_cancel_image);
        iv_delete_image = (CircleButton) findViewById(R.id.iv_delete_image);

        adapter = new MyContactsListAdapter(datas, this);
        list_view.setAdapter(adapter);
        contactDao = new ContactDao(this);
        refresh();


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setIsShowSelectIndicator();
                adapter.notifyDataSetChanged();
                triangleBottomLayoutOperation(true);
                return true;
            }
        });
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditContactUserInfoActivity.startActivity(activity,datas.get(i));
            }
        });


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


        iv_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triangleBottomLayoutOperation(false);
                final int[] error_count = {0};
                final int[] success_count = {0};
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).isSelected()) {

                        final int finalI = i;
                        DelPersonUtil.delPerson(activity, NetWorkConstant.APP_ID, datas.get(i).getPerson_id(), new NetCallback<DelPersonResponseBean>(activity) {
                            @Override
                            public void onFailure(RetrofitError error, String message) {
                                error_count[0]++;
                            }

                            @Override
                            public void success(DelPersonResponseBean delPersonResponseBean, Response response) {
                                success_count[0]++;
                                contactDao.deleteByName(datas.get(finalI).getImage_path());
                            }
                        });
                    }
                }
                if(error_count[0]>0) {
                    if (success_count[0] > 0) {
                        Toast.makeText(activity, "网络状态不好，只成功删除了" + success_count[0] + "个", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity, "请求超时,请检查网络设置后重试！", Toast.LENGTH_SHORT).show();
                    }
                }else  if (success_count[0] > 0){
                    Toast.makeText(activity, "成功删除人脸联系人" + success_count[0] + "个", Toast.LENGTH_SHORT).show();
                }

                datas.clear();
                datas.addAll(contactDao.getAll());
                adapter.setIsShowSelectIndicator();
                adapter.notifyDataSetChanged();

//                new AsyncTask<Void, List<FocusBean>, List<FocusBean>>() {
//                    @Override
//                    protected void onPostExecute(List<FocusBean> aVoid) {
//                        super.onPostExecute(aVoid);
//                        datas.addAll(aVoid);
//                        adapter.setIsShowSelectIndicator(false);
//                    }
//
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                        datas.clear();
//                    }
//
//                    @Override
//                    protected List<FocusBean> doInBackground(Void... voids) {
//                        return focusDao.getAll();
//                    }
//                }.execute();
            }
        });
        iv_cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triangleBottomLayoutOperation(false);
                adapter.setIsShowSelectIndicator();
                for (ContactBean bean : datas) {
                    bean.setIsSelected(false);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void refresh() {
        //先读取文件结束才设置
        new AsyncTask<Void, List<ContactBean>, List<ContactBean>>() {
            @Override
            protected List<ContactBean> doInBackground(Void... voids) {
                return contactDao.getAll();
            }

            @Override
            protected void onPostExecute(List<ContactBean> aVoid) {
                super.onPostExecute(aVoid);
                if(aVoid!=null)
                {
                    datas.clear();
                    datas.addAll(aVoid);
                    adapter.notifyDataSetChanged();

                }
                swipeLayout.setRefreshing(false);
            }
        }.execute();
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
    private void triangleBottomLayoutOperation(boolean isShowSelectAll) {
        switch (layout_operation.getVisibility()) {
            case View.VISIBLE:
                animation_exit.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layout_operation.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                layout_operation.startAnimation(animation_exit);
                break;
            case View.GONE:
                animation_enter.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layout_operation.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                layout_operation.startAnimation(animation_enter);
                break;
        }

        if (isShowSelectAll) {
            title_bar.setOnTitleBarClickListener(null);
            title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
                @Override
                public void onLeftButtonClick(View v) {
                    finish();
                }

                @Override
                public void onRightButtonClick(View v) {
                    for (ContactBean bean : datas) {
                        bean.setIsSelected(true);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            title_bar.setRightTv("全选", -1);
        } else {

            title_bar.setOnTitleBarClickListener(null);
            title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
                @Override
                public void onLeftButtonClick(View v) {
                    finish();
                }

                @Override
                public void onRightButtonClick(View v) {
                }
            });
            title_bar.setRightTv("", -1);
        }
    }


}
