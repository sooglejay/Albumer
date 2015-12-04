package sooglejay.youtu.ui;

import android.annotation.TargetApi;
import android.app.Activity;
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
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.imagepicker.bean.Image;
import sooglejay.youtu.widgets.imagepicker.utils.TimeUtils;

/**
 * Created by JammyQtheLab on 2015/12/3.
 */
public class MyLikeActivity extends BaseActivity {
    private TitleBar title_bar;
    private GridView grid;
    private int mGridWidth, mGridHeight;
    // 时间线
    private TextView mTimeLineText;
    private List<LikeBean> datas = new ArrayList<>();
    private LikeDao likeDao;

    private MyLikeAdapter adapter;

    private Activity activity;

    private FrameLayout layout_operation;
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
                    Image image = (Image) view.getAdapter().getItem(index);
                    if (image != null) {
                        mTimeLineText.setText(TimeUtils.formatPhotoDate(image.path));
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
                final int height = grid.getHeight();
                mGridWidth = width;
                mGridHeight = height;
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
                for (LikeBean likeBean : datas) {
                    urls.add(likeBean.getImagePath());
                }
                GalleryActivity.startActivity(activity, "", i, urls);
            }
        });
        grid.setLongClickable(true);
        grid.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for (LikeBean bean : datas) {
                    bean.setStatus(MyLikeAdapter.VISIBLE_UNSELECTED);
                }
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
                adapter.notifyDataSetChanged();
                return true;
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
}