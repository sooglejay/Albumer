package sooglejay.youtu.ui;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.R;
import sooglejay.youtu.constant.ExtraConstants;
import sooglejay.youtu.fragment.GalleryFragment;
import sooglejay.youtu.widgets.TitleBar;


public class GalleryActivity extends BaseActivity implements GalleryFragment.OnRectfChangeListener {

    private ArrayList<String> urls = new ArrayList<>();
    private List<String> originUrls = new ArrayList<>();
    private int position;
    private ViewPager galleryViewPager;
    private float width;
    private TitleBar titleBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        urls = getIntent().getStringArrayListExtra(ExtraConstants.EXTRA_URLS);
        position = getIntent().getIntExtra(ExtraConstants.EXTRA_POSITION, 0);
        initView();

    }
    private void initView() {
        // TODO Auto-generated method stub

        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.initTitleBarInfo((position + 1) + "/" + originUrls.size(), R.drawable.arrow_left, -1, "", "");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {

            }
        });

        galleryViewPager = (ViewPager) findViewById(R.id.gallery_pager);
        final GalleryAdapter galleryAdapter = new GalleryAdapter(getSupportFragmentManager());
        galleryViewPager.setAdapter(galleryAdapter);
        galleryViewPager.setCurrentItem(position);
        galleryViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                if(originUrls!=null)
                {
                    titleBar.updateTitle((position + 1) + "/" + originUrls.size());
                }
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {
                // TODO Auto-generated method stub

            }
        });
    }


    class GalleryAdapter extends FragmentPagerAdapter {
        public GalleryAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            GalleryFragment fragment = new GalleryFragment();
            Bundle b = new Bundle();
            b.putInt("position", position);
            if (originUrls != null && originUrls.size() > position) {
                b.putString("url", originUrls.get(position));
                b.putInt("count", originUrls.size());
            }
            fragment.setArguments(b);
            return fragment;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (originUrls != null)
                return originUrls.size();
            else return 0;

        }


    }


    @Override
    public void onRectfChanged(RectF rectF) {
        // TODO Auto-generated method stub
//        if(rectF.left >= 0 || rectF.right <= width){
////            if(!galleryViewPager.isScroll()){
////                galleryViewPager.setScrollable(false);
////            }
//        }
//        else {
////            if(galleryViewPager.isScroll()){
////                galleryViewPager.setScrollable(true);
////            }
//
//        }
    }

}