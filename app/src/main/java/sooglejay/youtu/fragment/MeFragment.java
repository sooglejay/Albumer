package sooglejay.youtu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.getgroupids.GetGroupIdsResponseBean;
import sooglejay.youtu.api.getgroupids.GetGroupIdsUtil;
import sooglejay.youtu.bean.GroupBean;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.constant.StringConstant;
import sooglejay.youtu.db.FocusDao;
import sooglejay.youtu.db.GroupNameDao;
import sooglejay.youtu.db.LikeDao;
import sooglejay.youtu.event.BusEvent;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.ui.MyContactsActivity;
import sooglejay.youtu.ui.MyFocusActivity;
import sooglejay.youtu.ui.MyLikeActivity;
import sooglejay.youtu.ui.SettingActivity;
import sooglejay.youtu.ui.SetIdentifyGroupIdActivity;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/12/2.
 */
public class MeFragment extends BaseFragment {

    private TitleBar titleBar;
    private RoundImageView my_avatar_image;
    private LinearLayout layout_choose_group_id;
    private Activity activity;
    private GroupNameDao groupNameDao;

    private TextView tv_signature;
    private TextView my_contacts_count_tv;
    private TextView my_focus_count_tv;
    private TextView my_like_count_tv;

    private LinearLayout my_contacts_group;
    private LinearLayout my_like_group;
    private LinearLayout my_focus_group;
    private LinearLayout my_setting_group;


    private LikeDao likeDao;
    private FocusDao focusDao;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (likeDao != null && focusDao != null) {
                int likeCount = 0;
                int focusCount = 0;
                likeCount = likeDao.getCount();
                focusCount = focusDao.getCount();
                my_like_count_tv.setText(likeCount + "");
                my_focus_count_tv.setText(focusCount + "");
            }
            String avatarStr = PreferenceUtil.load(getActivity(), PreferenceConstant.USER_AVATAR, "");
            if (my_avatar_image != null) {
                Log.e("qwqw", "qwqwqw9898");
                Log.e("qwqw", "avatarStr:" + avatarStr);
                ImageLoader.getInstance().displayImage("file://" + avatarStr, my_avatar_image, ImageUtils.getOptions());
            }
            Log.e("qwqw", "qwqwqw102");
            if (tv_signature != null) {
                Log.e("qwqw", "qwqwqw86");
                String signature = PreferenceUtil.load(getActivity(), PreferenceConstant.USER_SIGNATURE, StringConstant.default_signature);
                tv_signature.setText(signature);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = this.getActivity();

        likeDao = new LikeDao(getActivity());
        focusDao = new FocusDao(getActivity());


        groupNameDao = new GroupNameDao(getActivity());
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);

        my_avatar_image = (RoundImageView) view.findViewById(R.id.my_avatar_image);
        String avatarStr = PreferenceUtil.load(getActivity(), PreferenceConstant.USER_AVATAR, "");
        Log.e("qwqw", "干扰球");
        ImageLoader.getInstance().displayImage("file://" + avatarStr, my_avatar_image, ImageUtils.getOptions());

        layout_choose_group_id = (LinearLayout) view.findViewById(R.id.layout_choose_group_id);

        my_contacts_group = (LinearLayout) view.findViewById(R.id.my_contacts_group);
        my_like_group = (LinearLayout) view.findViewById(R.id.my_like_group);
        my_focus_group = (LinearLayout) view.findViewById(R.id.my_focus_group);
        my_setting_group = (LinearLayout) view.findViewById(R.id.my_setting_group);

        tv_signature = (TextView) view.findViewById(R.id.tv_signature);
        my_like_count_tv = (TextView) view.findViewById(R.id.my_like_count_tv);
        my_contacts_count_tv = (TextView) view.findViewById(R.id.my_contacts_count_tv);
        my_focus_count_tv = (TextView) view.findViewById(R.id.my_focus_count_tv);


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
        my_setting_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
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

    /**
     * EventBus 广播
     *
     * @param event
     */
    public void onEvent(BusEvent event) {
        switch (event.getMsg()) {

            case BusEvent.MSG_MODIFY_USER_INFO:
                String avatarStr = PreferenceUtil.load(getActivity(), PreferenceConstant.USER_AVATAR, "");
                if (my_avatar_image != null) {
                    Log.e("qwqw", "qwqwqw9898");
                    Log.e("qwqw", "avatarStr:" + avatarStr);
                    ImageLoader.getInstance().displayImage("file://" + avatarStr, my_avatar_image, ImageUtils.getOptions());
                }
                Log.e("qwqw", "qwqwqw102");
                if (tv_signature != null) {
                    Log.e("qwqw", "qwqwqw86");
                    tv_signature.setText(PreferenceUtil.load(getActivity(), PreferenceConstant.USER_SIGNATURE, StringConstant.default_signature));
                }
                break;
            default:
                break;
        }
    }
}
