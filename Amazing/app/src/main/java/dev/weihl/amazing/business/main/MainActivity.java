package dev.weihl.amazing.business.main;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.weihl.amazing.Logc;
import dev.weihl.amazing.R;
import dev.weihl.amazing.Session;
import dev.weihl.amazing.Tags;
import dev.weihl.amazing.business.BaseActivity;
import dev.weihl.amazing.business.account.LoginActivity;
import dev.weihl.amazing.business.favorite.FavoriteActivity;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.util.AnimUtil;
import dev.weihl.amazing.util.DensityUtil;
import dev.weihl.amazing.util.LayoutUtil;


public class MainActivity extends BaseActivity
        implements MainContract.View, GroupFragment.OnTabSelectListener, DiscoverFragment.CallBack {

    @BindView(R.id.bar)
    RelativeLayout mBar;
    @BindView(R.id.favorite)
    Button mFavorite;
    @BindView(R.id.account)
    Button mAccount;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.bottomBar)
    LinearLayout mBottomBar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    final int LOGIN_REQUESTCODE = 1000;
    MainContract.Presenter mPresenter;
    GestureDetector mDetector;
    int tempBarHeight = -1;
    int maxBarHeight = -1; // 最大高度
    int minBarHeight = -1; // 最小高度
    SparseIntArray mFragmentBarHeights = new SparseIntArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new MainPresenter(this).start();
        onDetector();
        initViewPager();
        initTabLayout();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void onDetector() {
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (Logc.allowPrints()) {
                    Logc.d(Tags.MainAction, "distanceX = " + distanceX + " ; distanceY = " + distanceY);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    private void onAnimBar(int distanceY) {
        if (tempBarHeight == -1) {
            minBarHeight = tempBarHeight = DensityUtil.dpToPx(getActivity(), 29);
            maxBarHeight = DensityUtil.dpToPx(getActivity(), 56);
        }
        tempBarHeight += distanceY;
        if (tempBarHeight < minBarHeight) {
            tempBarHeight = minBarHeight;
        }
        if (tempBarHeight > maxBarHeight) {
            tempBarHeight = maxBarHeight;
        }
        mFragmentBarHeights.put(mViewPager.getCurrentItem(), tempBarHeight);
        mUIHandler.post(onAnimBarRunnable);
    }

    private Runnable onAnimBarRunnable = new Runnable() {
        @Override
        public void run() {
            LayoutUtil.setViewWidthHeight(mBar, mBar.getWidth(), tempBarHeight);
            animBottomBar();
        }
    };

    private void animBottomBar() {
        mUIHandler.removeCallbacks(animBottomBarRunnable);
        mUIHandler.post(animBottomBarRunnable);
    }

    private Runnable animBottomBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (tempBarHeight == minBarHeight) {
                if (mBottomBar.getVisibility() == View.GONE) {
                    AnimUtil.translateView(mBottomBar, mBottomBar.getX(), 0,
                            0, 0, 300, true, null);
                    mBottomBar.setVisibility(View.VISIBLE);
                }
            } else if (tempBarHeight == maxBarHeight) {
                if (mBottomBar.getVisibility() == View.VISIBLE) {
                    AnimUtil.translateView(mBottomBar, 0, mBottomBar.getX(),
                            0, 0, 300, true, null);
                    mBottomBar.setVisibility(View.GONE);
                }
            }
        }
    };

    private boolean mBarShrinking = false;
    private boolean mMagnify = false;

    private void onValueAnimBar() {
        int fBarHeight = mFragmentBarHeights.get(mViewPager.getCurrentItem());
        if (fBarHeight < minBarHeight) {
            fBarHeight = minBarHeight;
            mFragmentBarHeights.put(mViewPager.getCurrentItem(), fBarHeight);
        }
        if (fBarHeight > maxBarHeight) {
            fBarHeight = maxBarHeight;
            mFragmentBarHeights.put(mViewPager.getCurrentItem(), fBarHeight);
        }
        if (tempBarHeight > fBarHeight) {
            if (!mBarShrinking) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(tempBarHeight, fBarHeight);
                valueAnimator.setDuration(500);
                final int finalFBarHeight = fBarHeight;
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tempBarHeight = (int) animation.getAnimatedValue();
                        LayoutUtil.setViewWidthHeight(mBar, mBar.getWidth(), tempBarHeight);
                        mBarShrinking = !(tempBarHeight == finalFBarHeight);
                        animBottomBar();
                    }
                });
                valueAnimator.start();
            }
        } else {
            if (!mMagnify) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(tempBarHeight, fBarHeight);
                valueAnimator.setDuration(500);
                final int finalFBarHeight1 = fBarHeight;
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tempBarHeight = (int) animation.getAnimatedValue();
                        LayoutUtil.setViewWidthHeight(mBar, mBar.getWidth(), tempBarHeight);
                        mMagnify = !(tempBarHeight == finalFBarHeight1);
                        animBottomBar();
                    }
                });
                valueAnimator.start();
            }
        }
    }

    private void initTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        mTabLayout.setTabTextColors(Color.parseColor("#8BFFFFFF"), Color.parseColor("#FFFFFF"));
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }

            @Override
            public int getCount() {
                return Session.tabList.size();
            }

            @Override
            public Fragment getItem(int position) {
                Logc.d("MainAdapter", "getItem position = " + position);
                return DiscoverFragment.newInstance(Session.tabList.get(position), position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return Session.tabList.get(position).getName();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onValueAnimBar();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private boolean isNonNullList(List list) {
        return list != null && !list.isEmpty();
    }

    @OnClick({R.id.account, R.id.favorite})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.account:
                startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_REQUESTCODE);
                break;
            case R.id.favorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOGIN_REQUESTCODE:
                    // do something
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    protected void onHandleMessage(Message msg) {

    }

    @Override
    public void onTabResult(DiscoverTab tab) {
        if (Logc.allowPrints()) {
            Logc.d(Tags.GroupTab, tab == null ? "tab is null !" : tab.toString());
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (Logc.allowPrints()) {
            Logc.d(Tags.Discover, "onAnimBar dy = " + dy);
        }
        onAnimBar(dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onDetachDiscoverFragment(int position) {
        if (Logc.allowPrints()) {
            Logc.d(Tags.Discover, "onAttachDiscoverFragment position = " + position);
        }
    }
}
