package sooglejay.youtu.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.R;
import sooglejay.youtu.bean.FocusBean;

/**
 * Created by JammyQtheLab on 2015/12/4.
 */
public class MyFocusAdapter extends BaseAdapter {

    private List<FocusBean> datas = new ArrayList<>();
    private LayoutInflater mInflater;

    public void setIsShowSelectIndicator() {
        this.isShowSelectIndicator = !this.isShowSelectIndicator;
        notifyDataSetChanged();
    }

    private boolean isShowSelectIndicator = false;

    public MyFocusAdapter(List<FocusBean> datas, Activity activity) {
        this.datas = datas;
        this.activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
    }


    private Activity activity;

    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;

    /**
     * 重置每个Column的Size
     *
     * @param columnWidth
     */
    public void setItemSize(int columnWidth) {

        if (mItemSize == columnWidth) {
            return;
        }

        mItemSize = columnWidth;

        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public FocusBean getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        holde = new ViewHolde();
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
            holde.image = (ImageView) view.findViewById(R.id.image);
            holde.indicator = (ImageView) view.findViewById(R.id.checkmark);
            holde.mask = view.findViewById(R.id.mask);

            holde.onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.checkmark:
                            FocusBean bean = (FocusBean) view.getTag();
                            bean.setIsSelected(!bean.isSelected());
                            notifyDataSetChanged();
                            break;
                    }
                }
            };
            view.setTag(holde);
        } else {
            holde = (ViewHolde) view.getTag();
        }
        FocusBean bean = getItem(i);
        File imageFile = new File(bean.getImagePath());
        if (mItemSize > 0) {
            // 显示图片
            Picasso.with(activity)
                    .load(imageFile)
                    .placeholder(R.drawable.default_error)
                    .resize(mItemSize, mItemSize)
                    .centerCrop()
                    .into(holde.image);
        }
        if (isShowSelectIndicator) {
            holde.indicator.setVisibility(View.VISIBLE);
            holde.mask.setVisibility(View.VISIBLE);
        } else {
            holde.mask.setVisibility(View.GONE);
            holde.indicator.setVisibility(View.GONE);
        }

        if (!bean.isSelected()) {
            holde.indicator.setImageResource(R.drawable.btn_unselected);
        } else {
            holde.indicator.setImageResource(R.drawable.btn_selected);
        }

        holde.indicator.setTag(bean);
        holde.indicator.setClickable(true);
        holde.indicator.setOnClickListener(holde.onClickListener);

//
//        /** Fixed View Size */
//        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
//        if (lp.height != mItemSize) {
//            view.setLayoutParams(mItemLayoutParams);
//        }

        return view;
    }

    private ViewHolde holde;

    private class ViewHolde {
        private ImageView image;
        private ImageView indicator;
        private View mask;
        private View.OnClickListener onClickListener;
    }


}
