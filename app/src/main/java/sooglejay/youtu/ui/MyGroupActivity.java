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
import sooglejay.youtu.adapter.MyGroupListAdapter;
import sooglejay.youtu.adapter.SetIdentifyGroupIdAdapter;
import sooglejay.youtu.bean.GroupBean;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.db.GroupNameDao;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/12/5.
 */
public class MyGroupActivity extends BaseActivity {
    private SwipeRefreshLayout swipeLayout;
    private TitleBar title_bar;
    private ListView list_view;
    private MyGroupListAdapter adapter;
    private ArrayList<GroupBean> datas = new ArrayList<>();
    private GroupNameDao groupNameDao ;

    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);
        activity = this;

        groupNameDao = new GroupNameDao(this);
        title_bar = (TitleBar)findViewById(R.id.title_bar);
        title_bar.initTitleBarInfo("我的群组",R.drawable.arrow_left,-1,"","");
        title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {

            }
        });
        list_view = (ListView)findViewById(R.id.list_view);
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeLayout.setColorSchemeResources(R.color.base_color);
        adapter = new MyGroupListAdapter(this,datas,groupNameDao);
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
