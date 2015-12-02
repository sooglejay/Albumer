package sooglejay.youtu.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sooglejay.youtu.R;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.bean.GroupBean;
import sooglejay.youtu.db.GroupNameDao;
import sooglejay.youtu.utils.GetTagUtil;

/**
 * Created by JammyQtheLab on 2015/11/30.
 */
public class SetGroupIdAdapter extends BaseAdapter {
    private ArrayList<GroupBean> groupNameDatas;
    private GroupNameDao groupNameDao;

    public SetGroupIdAdapter(Context context, ArrayList<GroupBean> groupNameDatas, GroupNameDao groupNameDao) {
        this.context = context;
        this.groupNameDatas = groupNameDatas;
        this.groupNameDao = groupNameDao;
    }

    private Context context;

    @Override
    public int getCount() {
        return groupNameDatas.size();
    }

    @Override
    public GroupBean getItem(int i) {
        return groupNameDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        holder = new ViewHolder();
        if (view == null) {
            view = View.inflate(context, R.layout.item_group_name, null);
            holder.item = (LinearLayout) view.findViewById(R.id.item);
            holder.tvName = (TextView) view.findViewById(R.id.tv_group_name);
            holder.iv_selected = (ImageView) view.findViewById(R.id.iv_selected);
            holder.listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Object object = view.getTag();
                    if (object instanceof GroupBean) {
                        GroupBean bean = (GroupBean) object;
                        switch (view.getId()) {
                            case R.id.item:
                                bean.setIsSelected(!bean.isSelected());
                                groupNameDao.uodateGroupNameBean(bean);

                                break;
                            default:
                                break;
                        }
                        notifyDataSetChanged();
                    }
                }
            };
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GroupBean bean = getItem(i);
        if (bean.isSelected()) {
            holder.iv_selected.setImageResource(R.drawable.icon_choose_selected);
        } else {
            holder.iv_selected.setImageResource(R.drawable.icon_choose);
        }
        holder.tvName.setText(bean.getName());
        holder.item.setTag(bean);
        holder.item.setOnClickListener(holder.listener);
        return view;
    }

    private ViewHolder holder;

    private class ViewHolder {
        private LinearLayout item;
        private TextView tvName;
        private ImageView iv_selected;
        private View.OnClickListener listener;
    }
}
