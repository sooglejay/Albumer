package sooglejay.youtu.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.R;
import sooglejay.youtu.adapter.MyLikeAdapter;
import sooglejay.youtu.bean.LikeBean;
import sooglejay.youtu.db.LikeDao;
import sooglejay.youtu.widgets.CircleButton;
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.imagepicker.bean.Image;
import sooglejay.youtu.widgets.imagepicker.utils.TimeUtils;

/**
 * Created by JammyQtheLab on 2015/12/3.
 */
public class MyLikeActivity extends BaseActivity {
    private TitleBar title_bar;
    private GridView grid;
    // 时间线
    private TextView mTimeLineText;
    private List<LikeBean> datas = new ArrayList<>();
    private LikeDao likeDao;

    private MyLikeAdapter adapter;

    private Activity activity;

    private FrameLayout layout_operation;
    private CircleButton iv_cancel_image;
    private CircleButton iv_delete_image;
    private Animation animation_enter;
    private Animation animation_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_like);
        activity = this;
        animation_enter = AnimationUtils.loadAnimation(activity,
                R.anim.enter_from_bottom_200);
        animation_exit = AnimationUtils.loadAnimation(activity,
                R.anim.exit_to_bottom_200);

        likeDao = new LikeDao(this);
        datas = likeDao.getAll();
        title_bar = (TitleBar) findViewById(R.id.title_bar);
        iv_cancel_image = (CircleButton) findViewById(R.id.iv_cancel_image);
        iv_delete_image = (CircleButton) findViewById(R.id.iv_delete_image);
        grid = (GridView) findViewById(R.id.grid);
        mTimeLineText = (TextView) findViewById(R.id.timeline_area);
        layout_operation = (FrameLayout) findViewById(R.id.layout_operation);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);

        adapter = new MyLikeAdapter(datas, this);
        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                final Picasso picasso = Picasso.with(activity);
                if (state == SCROLL_STATE_IDLE || state == SCROLL_STATE_TOUCH_SCROLL) {
                    picasso.resumeTag(activity);
                } else {
                    picasso.pauseTag(activity);
                }
                if (state == SCROLL_STATE_IDLE) {
                    // 停止滑动，日期指示器消失
                    mTimeLineText.setVisibility(View.GONE);
                } else if (state == SCROLL_STATE_FLING) {
                    mTimeLineText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mTimeLineText.getVisibility() == View.VISIBLE) {
                    int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                    LikeBean bean = (LikeBean) view.getAdapter().getItem(index);
                    if (bean != null) {
                        mTimeLineText.setText(TimeUtils.formatPhotoDate(bean.getImagePath()));
                    }
                }
            }
        });
        grid.setAdapter(adapter);
        grid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {
                final int width = grid.getWidth();
                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = width / desireSize;
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (width - columnSpace * (numCount - 1)) / numCount;
                adapter.setItemSize(columnWidth);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    grid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    grid.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> urls = new ArrayList<String>();
                for (LikeBean bean : datas) {
                    urls.add(bean.getImagePath());
                }
                GalleryActivity.startActivity(activity, "", i, urls);
            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setIsShowSelectIndicator();
                triangleBottomLayoutOperation(true);
                return true;
            }
        });
        layout_operation.setVisibility(View.GONE);
        iv_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triangleBottomLayoutOperation(false);
                for (int i = 0 ; i <datas.size();i++) {
                    if (datas.get(i).isSelected()) {
                        likeDao.deleteByName(datas.get(i).getImagePath());
                    }
                }
                datas.clear();
                datas.addAll(likeDao.getAll());
                adapter.setIsShowSelectIndicator();
//                new AsyncTask<Void, List<LikeBean>, List<LikeBean>>() {
//                    @Override
//                    protected void onPostExecute(List<LikeBean> aVoid) {
//                        super.onPostExecute(aVoid);
//                        datas.addAll(aVoid);
//                        adapter.setIsShowSelectIndicator(false);
//                    }
//
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                        datas.clear();
//                    }
//
//                    @Override
//                    protected List<LikeBean> doInBackground(Void... voids) {
//                        return likeDao.getAll();
//                    }
//                }.execute();

            }
        });
        iv_cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setIsShowSelectIndicator();
                triangleBottomLayoutOperation(false);
                for (LikeBean bean : datas) {
                    bean.setIsSelected(false);
                }
                adapter.notifyDataSetChanged();
            }
        });

        title_bar.initTitleBarInfo("我喜欢的", R.drawable.arrow_left, -1, "", "");
        title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {

            }
        });
    }

    private void triangleBottomLayoutOperation(boolean isShowSelectAll) {
        switch (layout_operation.getVisibility()) {
            case View.VISIBLE:
                animation_exit.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layout_operation.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                layout_operation.startAnimation(animation_exit);
                break;
            case View.GONE:
                animation_enter.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layout_operation.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                layout_operation.startAnimation(animation_enter);
                break;
        }

        if(isShowSelectAll)
        {

            title_bar.setOnTitleBarClickListener(null);
            title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
                @Override
                public void onLeftButtonClick(View v) {
                    finish();
                }

                @Override
                public void onRightButtonClick(View v) {
                    for (LikeBean bean : datas) {
                        bean.setIsSelected(true);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            title_bar.setRightTv("全选", -1);
        }else {
            title_bar.setOnTitleBarClickListener(null);
            title_bar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
                @Override
                public void onLeftButtonClick(View v) {
                    finish();
                }

                @Override
                public void onRightButtonClick(View v) {
                }
            });
            title_bar.setRightTv("", -1);
        }
    }
}
