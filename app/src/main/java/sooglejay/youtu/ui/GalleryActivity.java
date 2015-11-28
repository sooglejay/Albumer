package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.R;
import sooglejay.youtu.constant.ExtraConstants;
import sooglejay.youtu.fragment.GalleryFragment;
import sooglejay.youtu.utils.AsyncBitmapLoader;
import sooglejay.youtu.utils.CacheUtil;
import sooglejay.youtu.widgets.TitleBar;


public class GalleryActivity extends BaseActivity implements GalleryFragment.OnRectfChangeListener {
    private List<String> originUrls = new ArrayList<>();
    private String folderName;
    private int position;
    private ViewPager galleryViewPager;
    private float width;
    private TitleBar titleBar;

    public CacheUtil cacheUtil;
    public AsyncBitmapLoader asyncBitmapLoader;


    public static void startActivity(Activity activity, String folderName, int position, ArrayList<String> urls) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_POSITION, position);
        intent.putExtra(ExtraConstants.EXTRA_URLS, urls);
        intent.putExtra(ExtraConstants.EXTRA_FOLDER_NAME, folderName);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        originUrls = getIntent().getStringArrayListExtra(ExtraConstants.EXTRA_URLS);
        position = getIntent().getIntExtra(ExtraConstants.EXTRA_POSITION, 0);
        folderName = getIntent().getStringExtra(ExtraConstants.EXTRA_FOLDER_NAME);

        cacheUtil = new CacheUtil(this);
        asyncBitmapLoader = new AsyncBitmapLoader();

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub

        titleBar = (TitleBar) findViewById(R.id.title_bar);
        folderName = TextUtils.isEmpty(folderName) ? "lalala" : folderName;
        titleBar.initTitleBarInfo((position + 1) + "/" + originUrls.size(), R.drawable.arrow_left, -1, folderName, "");
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

        galleryViewPager.setOffscreenPageLimit(1);
        galleryViewPager.setCurrentItem(position);
        galleryViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                if (originUrls != null) {
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


        //读取文件结束才设置适配器
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                asyncBitmapLoader.setmBitMapCache(cacheUtil.getObjectFromFile());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("jwjw","文件加载完毕");
                galleryViewPager.setAdapter(galleryAdapter);
            }
        }.execute();

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
            if (originUrls != null && originUrls.size() > position) {
                b.putString("url", originUrls.get(position));
                b.putInt("count", originUrls.size());
                b.putInt("position", position);
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
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (cacheUtil != null && asyncBitmapLoader != null) {
            cacheUtil.saveObjectToFile(this, asyncBitmapLoader.getmBitMapCache());
        }
//        ImageLoader.getInstance().clearMemoryCache();
    }
}