package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.adapter.SetGroupIdAdapter;
import sooglejay.youtu.api.getgroupids.GetGroupIdsResponseBean;
import sooglejay.youtu.bean.GroupBean;
import sooglejay.youtu.constant.ExtraConstants;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.db.GroupNameDao;
import sooglejay.youtu.fragment.DialogFragmentCreater;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetGroupIdsUtil;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/12/1.
 */
public class CreateNewGroupActivity extends BaseActivity {


    private ListView list_view;
    private TitleBar titleBar;
    private SetGroupIdAdapter adapter;
    private ArrayList<GroupBean> datas = new ArrayList<>();
    private Activity activity;

    private String groupListStrFromIntent = "";

    private LinearLayout layout_create_new_group;

    private DialogFragmentCreater dialogFragmentCreater;

    private GroupNameDao groupNameDao;

    public static void startActivity(Activity activity ,int requestCode) {
        Intent intent = new Intent(activity, CreateNewGroupActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        activity = this;
        dialogFragmentCreater = new DialogFragmentCreater();
        groupNameDao = new GroupNameDao(this);
        setUpView();
        setUpListener();
        doSomething();

    }


    private void setUpView() {
        list_view = (ListView) findViewById(R.id.list_view);
        adapter = new SetGroupIdAdapter(this, datas, groupNameDao);
        list_view.setAdapter(adapter);
        layout_create_new_group = (LinearLayout) findViewById(R.id.layout_create_new_group);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.initTitleBarInfo("选择群组", R.drawable.arrow_left, -1, "", "确定");

    }

    private void setUpListener() {

        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {
                String groupIdsForResult = "";
                if (datas.size() > 0) {
                    for (GroupBean bean : datas) {
                        if (bean.isSelected()) {
                            groupIdsForResult += bean.getName() + GetGroupIdsUtil.reg;
                        }
                    }
                }

                if (!TextUtils.isEmpty(groupIdsForResult)) {
                    Intent data = new Intent();
                    data.putExtra(ExtraConstants.EXTRA_CREATE_NEW_GROUP, groupIdsForResult);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        layout_create_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragmentCreater.initDialogFragment(activity, getSupportFragmentManager());
                dialogFragmentCreater.setOnCreateNewGroupCallBack(new DialogFragmentCreater.OnCreateNewGroupCallBack() {
                    @Override
                    public void onClick(View view, EditText editText) {
                        final String groupName = editText.getText().toString();
                        switch (view.getId()) {
                            case R.id.tv_cancel:
                                break;
                            case R.id.tv_confirm:
                                if (TextUtils.isEmpty(groupName)) {
                                    Toast.makeText(activity, "请输入群组名称", Toast.LENGTH_SHORT).show();
                                } else {
                                    GroupBean bean = new GroupBean();
                                    bean.setIsSelected(false);
                                    bean.setName(groupName);
                                    groupNameDao.add(bean);
                                    datas.add(bean);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(activity, "新建群组成功！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                });
                dialogFragmentCreater.showDialog(activity, DialogFragmentCreater.DIALOG_showCreateNewGroupDialog);

            }
        });
    }

    private void doSomething() {
        List<GroupBean> groupBeanList = groupNameDao.getAll();
        if (groupBeanList != null && groupBeanList.size() > 0)//如果数据库有，就从数据库取数据
        {
            new AsyncTask<Void, List<GroupBean>, List<GroupBean>>() {
                @Override
                protected void onPostExecute(List<GroupBean> beanList) {
                    super.onPostExecute(beanList);
                    if (beanList != null) {
                        datas.addAll(beanList);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                protected List<GroupBean> doInBackground(Void... voids) {
                    return groupNameDao.getAll();
                }
            }.execute();
        } else {
            sooglejay.youtu.api.getgroupids.GetGroupIdsUtil.getGroupIds(this, NetWorkConstant.APP_ID, new NetCallback<GetGroupIdsResponseBean>(this) {
                @Override
                public void onFailure(RetrofitError error, String message) {
                    Toast.makeText(activity,"请求超时,请确保网络良好再重试",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void success(GetGroupIdsResponseBean getGroupIdsResponseBean, Response response) {
                    if (getGroupIdsResponseBean != null) {
                        List<String> stringArrayList = getGroupIdsResponseBean.getGroup_ids();
                        groupNameDao.deleteAll();
                        for (String name : stringArrayList) {
                            GroupBean groupBean = new GroupBean();
                            groupBean.setIsSelected(false);
                            groupBean.setName(name);
                            datas.add(groupBean);
                            groupNameDao.add(groupBean);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
