package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import sooglejay.youtu.R;
import sooglejay.youtu.constant.ExtraConstants;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.event.BusEvent;
import sooglejay.youtu.fragment.GalleryFragment;
import sooglejay.youtu.utils.AsyncBitmapLoader;
import sooglejay.youtu.utils.CacheUtil;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.widgets.TitleBar;


public class GalleryActivity extends BaseActivity implements GalleryFragment.OnRectfChangeListener, GalleryFragment.Callback {
    private List<String> originUrls = new ArrayList<>();
    private String folderName;
    private int position;


    private ViewPager galleryViewPager;
    private int outerPosition=0;
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


    private ArrayList<GalleryFragment>fragments = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        originUrls = getIntent().getStringArrayListExtra(ExtraConstants.EXTRA_URLS);
        position = getIntent().getIntExtra(ExtraConstants.EXTRA_POSITION, 0);
        folderName = getIntent().getStringExtra(ExtraConstants.EXTRA_FOLDER_NAME);

        boolean isDetectFace  = PreferenceUtil.load(this, PreferenceConstant.SWITCH_DETECT_FACE,true);
        for(int i = 0 ; i< originUrls.size();i++)
        {
            GalleryFragment fragment = new GalleryFragment();
            fragment.init(originUrls.get(i),i,isDetectFace);
            fragments.add(fragment);
        }

        cacheUtil = new CacheUtil(this);
        asyncBitmapLoader = new AsyncBitmapLoader();

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub

        titleBar = (TitleBar) findViewById(R.id.title_bar);
        folderName = TextUtils.isEmpty(folderName) ? "" : folderName;
        titleBar.initTitleBarInfo((position + 1) + "/" + originUrls.size(), R.drawable.arrow_left, -1, folderName, "刷新");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                finish();
            }

            @Override
            public void onRightButtonClick(View v) {
              fragments.get(outerPosition).refreshViewInThread();
            }
        });

        galleryViewPager = (ViewPager) findViewById(R.id.gallery_pager);
        final GalleryAdapter galleryAdapter = new GalleryAdapter(getSupportFragmentManager());

        //读取文件结束才设置适配器
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                asyncBitmapLoader.setmDetectedFaceBitMapCache(cacheUtil.getDetectedObjectFromFile());
                asyncBitmapLoader.setmIdentifiedFaceBitMapCache(cacheUtil.getIdentifiedObjectFromFile());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                galleryViewPager.setAdapter(galleryAdapter);
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
            }
        }.execute();

    }

    @Override
    public void onDeleteImagefile(String path, int position) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            file.delete();
            try {
                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{path});
            } catch (Exception e) {

            } finally {
                EventBus.getDefault().post(new BusEvent(BusEvent.MSG_DELETE_IMAGE_FILE));
                if(originUrls.contains(path))
                {
                    Toast.makeText(this,"12121212",Toast.LENGTH_SHORT).show();
                    originUrls.remove(path);
                    if(position>=1) {
                        galleryViewPager.setAdapter(null);
                        final GalleryAdapter galleryAdapter = new GalleryAdapter(getSupportFragmentManager());
                        galleryViewPager.setAdapter(galleryAdapter);
                        galleryViewPager.setCurrentItem(position - 1);
                    }
                    else if(originUrls.size()>1){
                        galleryViewPager.setAdapter(null);
                        final GalleryAdapter galleryAdapter = new GalleryAdapter(getSupportFragmentManager());
                        galleryViewPager.setAdapter(galleryAdapter);
                        galleryViewPager.setCurrentItem(0);
                    }else {
                        finish();
                    }
                }else {
                    Toast.makeText(this,"34343434",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            try {
                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{path});
            } catch (Exception e) {
            } finally {
                EventBus.getDefault().post(new BusEvent(BusEvent.MSG_DELETE_IMAGE_FILE));
                if(originUrls.contains(path))
                {
                    originUrls.remove(path);
                    if(position>=1) {
                        galleryViewPager.setAdapter(null);
                        final GalleryAdapter galleryAdapter = new GalleryAdapter(getSupportFragmentManager());
                        galleryViewPager.setAdapter(galleryAdapter);
                        galleryViewPager.setCurrentItem(position - 1);
                    }
                    else if(originUrls.size()>1){
                        galleryViewPager.setAdapter(null);
                        final GalleryAdapter galleryAdapter = new GalleryAdapter(getSupportFragmentManager());
                        galleryViewPager.setAdapter(galleryAdapter);
                        galleryViewPager.setCurrentItem(0);
                    }else {
                        finish();
                    }
                }
            }
        }

    }

    @Override
    public void onTouchImageView() {

    }


    class GalleryAdapter extends FragmentPagerAdapter {
        public GalleryAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            outerPosition = position;
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
           return fragments.size();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (asyncBitmapLoader != null && cacheUtil != null) {
            Log.e("jwjw", "test start Gallery ");
            asyncBitmapLoader.setmDetectedFaceBitMapCache(cacheUtil.getDetectedObjectFromFile());
            asyncBitmapLoader.setmIdentifiedFaceBitMapCache(cacheUtil.getIdentifiedObjectFromFile());
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
            cacheUtil.saveDetectedObjectToFile(this, asyncBitmapLoader.getmDetectedFaceBitMapCache());
            cacheUtil.saveIdentifiedObjectToFile(this, asyncBitmapLoader.getmIdentifiedFaceBitMapCache());
        }
        ImageLoader.getInstance().clearMemoryCache();
    }
}