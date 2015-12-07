package sooglejay.youtu.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.R;
import sooglejay.youtu.adapter.SetIdentifyGroupIdAdapter;
import sooglejay.youtu.bean.GroupBean;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.db.GroupNameDao;
import sooglejay.youtu.utils.CacheUtil;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/12/2.
 */
public class SetIdentifyGroupIdActivity extends BaseActivity {
    private SwipeRefreshLayout swipeLayout;
    private TitleBar title_bar;
    private ListView list_view;
    private SetIdentifyGroupIdAdapter adapter;
    private ArrayList<GroupBean>datas = new ArrayList<>();
    private GroupNameDao groupNameDao ;
    private Activity activity;
    private CacheUtil cacheUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_identify_group_id);
        groupNameDao = new GroupNameDao(this);
        cacheUtil = new CacheUtil(this);
        activity = this;
        title_bar = (TitleBar)findViewById(R.id.title_bar);
        title_bar.initTitleBarInfo("选择匹配群组",R.drawable.arrow_left,-1,"","确定");
        title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {


                for(GroupBean bean :datas) {
                    groupNameDao.updateGroupNameBean(bean);
                }

                for(GroupBean bean :datas)
                {
                    if(bean.isUsedForIdentify())
                    {
                        String name = bean.getName();
                        PreferenceUtil.save(activity, PreferenceConstant.IDENTIFY_GROUP_NAME, name);
                        cacheUtil.clearIdentifyCache();
                        break;
                    }
                }
                finish();
            }
        });
        list_view = (ListView)findViewById(R.id.list_view);
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeLayout.setColorSchemeResources(R.color.base_color);
        adapter = new SetIdentifyGroupIdAdapter(this,datas,groupNameDao);
        list_view.setAdapter(adapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        refresh();
    }

    private void refresh() {
        new AsyncTask<Void, List<GroupBean>, List<GroupBean>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(List<GroupBean> beanList) {
                super.onPostExecute(beanList);
                if (beanList != null) {
                    datas.clear();
                    datas.addAll(beanList);

                }

                String groupNameStr = PreferenceUtil.load(SetIdentifyGroupIdActivity.this, PreferenceConstant.IDENTIFY_GROUP_NAME,"1");

                for(int i = 0 ;i<datas.size();i++)
                {
                    if(datas.get(i).isUsedForIdentify())
                    {
                        break;
                    }
                    else if(i==datas.size()-1)
                    {//如果在最后一个都没有 符合条件的，就在此循环，使用xml存储的字符串来匹配
                        for(int k = 0;k<datas.size();k++)
                        {
                            if(datas.get(k).getName().equals(groupNameStr))
                            {
                                datas.get(k).setIsUsedForIdentify(true);
                                break;
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }

            @Override
            protected List<GroupBean> doInBackground(Void... voids) {
                return groupNameDao.getAll();
            }
        }.execute();
    }


}
