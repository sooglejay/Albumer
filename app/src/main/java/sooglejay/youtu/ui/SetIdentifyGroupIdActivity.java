package sooglejay.youtu.ui;

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
import sooglejay.youtu.db.GroupNameDao;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_identify_group_id);
        groupNameDao = new GroupNameDao(this);
        title_bar = (TitleBar)findViewById(R.id.title_bar);
        title_bar.initTitleBarInfo("选择匹配群组",R.drawable.arrow_left,-1,"","");
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
        if(swipeLayout.isRefreshing())
        {
            return;
        }
        new AsyncTask<Void, List<GroupBean>, List<GroupBean>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                swipeLayout.setRefreshing(true);
            }

            @Override
            protected void onPostExecute(List<GroupBean> beanList) {
                super.onPostExecute(beanList);
                if (beanList != null) {
                    datas.clear();
                    datas.addAll(beanList);
                    adapter.notifyDataSetChanged();
                }
                swipeLayout.setRefreshing(false);
            }

            @Override
            protected List<GroupBean> doInBackground(Void... voids) {
                return groupNameDao.getAll();
            }
        }.execute();
    }


}
