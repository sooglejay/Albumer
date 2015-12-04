package sooglejay.youtu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.adapter.MyFocusAdapter;
import sooglejay.youtu.api.getgroupids.GetGroupIdsResponseBean;
import sooglejay.youtu.api.getgroupids.GetGroupIdsUtil;
import sooglejay.youtu.bean.GroupBean;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.db.FocusDao;
import sooglejay.youtu.db.GroupNameDao;
import sooglejay.youtu.db.LikeDao;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.ui.MyContactsActivity;
import sooglejay.youtu.ui.MyFocusActivity;
import sooglejay.youtu.ui.MyLikeActivity;
import sooglejay.youtu.ui.SetIdentifyGroupIdActivity;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/12/2.
 */
public class MeFragment extends BaseFragment {

    private TitleBar titleBar;
    private LinearLayout layout_choose_group_id;
    private Activity activity;
    private GroupNameDao groupNameDao;

    private TextView my_contacts_count_tv;
    private TextView my_focus_count_tv;
    private TextView my_like_count_tv;

    private LinearLayout my_contacts_group;
    private LinearLayout my_like_group;
    private LinearLayout my_focus_group;


    private LikeDao likeDao;
    private FocusDao focusDao;

    private int likeCount = 0 ;
    private int focusCount = 0 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = this.getActivity();

        likeDao = new LikeDao(getActivity());
        focusDao = new FocusDao(getActivity());

        likeCount = likeDao.getCount();
        focusCount = focusDao.getCount();

        groupNameDao = new GroupNameDao(getActivity());
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        layout_choose_group_id = (LinearLayout) view.findViewById(R.id.layout_choose_group_id);

        my_contacts_group = (LinearLayout) view.findViewById(R.id.my_contacts_group);
        my_like_group = (LinearLayout) view.findViewById(R.id.my_like_group);
        my_focus_group = (LinearLayout) view.findViewById(R.id.my_focus_group);

        my_like_count_tv = (TextView) view.findViewById(R.id.my_like_count_tv);
        my_contacts_count_tv = (TextView) view.findViewById(R.id.my_contacts_count_tv);
        my_focus_count_tv = (TextView) view.findViewById(R.id.my_focus_count_tv);


        my_like_count_tv.setText(likeCount+"");
        my_focus_count_tv.setText(focusCount + "");



        titleBar.initTitleBarInfo("我的", -1, -1, "", "");


        //choose the default group for identify
        layout_choose_group_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, SetIdentifyGroupIdActivity.class));
            }
        });
        my_contacts_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), MyContactsActivity.class));

            }
        });
        my_like_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), MyLikeActivity.class));
            }
        });

        my_focus_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), MyFocusActivity.class));
            }
        });

        GetGroupIdsUtil.getGroupIds(getActivity(), NetWorkConstant.APP_ID, new NetCallback<GetGroupIdsResponseBean>(getActivity()) {
            @Override
            public void onFailure(RetrofitError error, String message) {
                Toast.makeText(activity, "请求超时,请确保网络良好再重试", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void success(GetGroupIdsResponseBean getGroupIdsResponseBean, Response response) {
                if (getGroupIdsResponseBean != null) {
                    groupNameDao.deleteAll();
                    List<String> stringArrayList = getGroupIdsResponseBean.getGroup_ids();
                    for (String name : stringArrayList) {
                        GroupBean groupBean = new GroupBean();
                        groupBean.setIsSelected(false);
                        groupBean.setName(name);
                        groupNameDao.add(groupBean);
                    }
                }
            }
        });


    }
}
