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
import sooglejay.youtu.bean.LikeBean;
import sooglejay.youtu.ui.BaseActivity;
import sooglejay.youtu.widgets.imagepicker.bean.Image;

/**
 * Created by JammyQtheLab on 2015/12/4.
 */
public class MyLikeAdapter extends BaseAdapter {

    public static final int VISIBLE_SELECTED = 100;
    public static final int VISIBLE_UNSELECTED = 101;
    public static final int GONE_UNSELECTED = 102;//没有 GONE_SELECTED  因为不需要

    private List<LikeBean>datas = new ArrayList<>();
    private LayoutInflater mInflater;

    public MyLikeAdapter(List<LikeBean> datas, Activity activity) {
        this.datas = datas;
        this.activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);

    }
    private boolean showSelectIndicator = false;

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
    public LikeBean getItem(int i) {
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
            view.setTag(holde);
        } else {
            holde = (ViewHolde) view.getTag();
        }
        LikeBean bean = getItem(i);
        File imageFile = new File(bean.getImagePath());
        if (mItemSize > 0) {
            // 显示图片
            Picasso.with(activity)
                    .load(imageFile)
                    .placeholder(R.drawable.default_error)
                            //.error(R.drawable.default_error)
                    .resize(mItemSize, mItemSize)
                    .centerCrop()
                    .into(holde.image);
        }
        switch (bean.getStatus())
        {
            case VISIBLE_SELECTED:
               holde.indicator.setImageResource(R.drawable.btn_selected);
                holde.mask.setVisibility(View.VISIBLE);
                break;
            case VISIBLE_UNSELECTED:
                holde.indicator.setImageResource(R.drawable.btn_unselected);
                holde.mask.setVisibility(View.GONE);
                break;
            case GONE_UNSELECTED:
                holde.indicator.setImageResource(R.drawable.btn_unselected);
                holde.mask.setVisibility(View.GONE);
                holde.indicator.setVisibility(View.GONE);
                break;
            default:
                holde.mask.setVisibility(View.GONE);
                holde.indicator.setVisibility(View.GONE);
                break;
        }


        /** Fixed View Size */
        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if (lp.height != mItemSize) {
            view.setLayoutParams(mItemLayoutParams);
        }
        return view;
    }

    private ViewHolde holde;
    private class ViewHolde {
        private ImageView image;
        private ImageView indicator;
        private View mask;
    }

}
