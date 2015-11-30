package sooglejay.youtu.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sooglejay.youtu.R;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.utils.GetTagUtil;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.widgets.RoundImageView;

/**
 * Created by JammyQtheLab on 2015/11/30.
 */
public class ChooseFaceAdapter extends BaseAdapter {
    private ArrayList<IdentifyItem> identifyItems;

    public ChooseFaceAdapter(Context context, ArrayList<IdentifyItem> identifyItems) {
        this.context = context;
        this.identifyItems = identifyItems;
    }

    private Context context;

    @Override
    public int getCount() {
        return identifyItems.size();
    }

    @Override
    public IdentifyItem getItem(int i) {
        return identifyItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        holder = new ViewHolder();
        if (view == null) {
            view = View.inflate(context, R.layout.item_choose_face, null);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.tvConfidence = (TextView) view.findViewById(R.id.tv_confidence);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        IdentifyItem identifyItem = getItem(i);
        String tag = identifyItem.getTag();
        if (!TextUtils.isEmpty(tag)) {
            holder.tvName.setText(GetTagUtil.getName(tag));
        }
        holder.tvConfidence.setText(identifyItem.getConfidence() + "");
        return view;
    }

    private ViewHolder holder;

    private class ViewHolder {
        private TextView tvName;
        private TextView tvConfidence;
    }
}
