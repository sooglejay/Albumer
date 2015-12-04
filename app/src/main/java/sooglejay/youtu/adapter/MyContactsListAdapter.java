package sooglejay.youtu.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import sooglejay.youtu.R;
import sooglejay.youtu.bean.ContactBean;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.widgets.imagepicker.bean.Image;

/**
 * Created by JammyQtheLab on 2015/12/4.
 */
public class MyContactsListAdapter extends BaseAdapter {
    private ArrayList<ContactBean> datas = new ArrayList<>();
    private Activity activity;

    public MyContactsListAdapter(ArrayList<ContactBean> datas, Activity activity) {
        this.datas = datas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ContactBean getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        holder = new ViewHolder();
        if (view == null)
        {
            view = View.inflate(activity, R.layout.list_item_my_contact,null);
            holder.iv_avatar = (ImageView)view.findViewById(R.id.iv_avatar);
            holder.tv_user_name = (TextView)view.findViewById(R.id.tv_user_name);
            holder.tv_phone_number = (TextView)view.findViewById(R.id.tv_phone_number);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        ContactBean bean = getItem(i);
        ImageLoader.getInstance().displayImage("file://"+bean.getImage_path(),holder.iv_avatar, ImageUtils.getOptions());
        holder.tv_phone_number.setText(bean.getPhoneNumber()+"");
        holder.tv_user_name.setText(bean.getUser_name()+"");
        return view;
    }

    private ViewHolder holder;
    private class ViewHolder
    {
        private ImageView iv_avatar;
        private TextView tv_user_name;
        private TextView tv_phone_number;
    }

}
