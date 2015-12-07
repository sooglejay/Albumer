package sooglejay.youtu.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import sooglejay.youtu.R;
import sooglejay.youtu.fragment.AlbumFragment;
import sooglejay.youtu.fragment.DetectFaceBeautyFragment;
import sooglejay.youtu.fragment.MeFragment;
import sooglejay.youtu.utils.ScreenUtils;
import sooglejay.youtu.widgets.TabBar;
import sooglejay.youtu.widgets.jazzyviewpager.JazzyViewPager;


public class MainActivity extends BaseActivity {
    private JazzyViewPager viewPager = null;
    private TabBar tabBar = null;
    private ViewPagerAdapter viewPagerAdapter = null;
    private int currentPos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
    }

    private void setUp() {
        tabBar = (TabBar) findViewById(R.id.home_bottomBar);
        initViewPager();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        viewPager = (JazzyViewPager) findViewById(R.id.view_pager);
        viewPager.removeAllViews();
        viewPager.setOffscreenPageLimit(10);

        viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("viewpager", "currentPosition:" + viewPager.getCurrentItem() + " position:" + position + "  positionOffset:" + positionOffset + " positionOffsetPixels" + positionOffsetPixels);
                tabBar.changeImageView(viewPager.getCurrentItem(), position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                tabBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //选中当前
        viewPager.setCurrentItem(currentPos);
        tabBar.selectTab(viewPager.getCurrentItem());
        tabBar.setOnTabClickListener(new TabBar.OnTabClickListener() {
            @Override
            public void onTabClick(int position) {
                viewPager.setCurrentItem(position, false);
            }
        });
    }


    private static final class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private JazzyViewPager mViewPagerInAdapter;
        public ViewPagerAdapter(Context context, FragmentManager fm, JazzyViewPager mViewPagerInAdapter) {
            super(fm);
            this.mViewPagerInAdapter = mViewPagerInAdapter;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Object obj = super.instantiateItem(container, position);
            mViewPagerInAdapter.setObjectForPosition(obj, position);
            return obj;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new DetectFaceBeautyFragment();
                    break;
                case 1:
                    fragment = new AlbumFragment();
                    break;
                case 2:
                    fragment = new MeFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长

    }
}
