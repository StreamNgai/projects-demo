package dev.weihl.amazing.business.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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


public class MainActivity extends BaseActivity implements MainContract.View, GroupFragment.OnTabSelectListener {

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
    int mBottomBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new MainPresenter(this).start();
        onDetector();
        initViewPager();
        initTabLayout();
        mBottomBarHeight = mBottomBar.getMeasuredHeight();
        if (Logc.allowPrints()) {
            Logc.d(Tags.MainAction, "BottomBarHeight = " + mBottomBarHeight);
        }
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
                if (distanceY > 0) {
                    if (mBottomBar.getTag() == null) {
                        // 隐藏底部Bar
                        mBottomBar.setTag(true);
                        AnimUtil.alphaView(mBottomBar, 1.0f, 0, 500);
                    }
                } else {
                    if (mBottomBar.getTag() != null) {
                        // 显示
                        mBottomBar.setTag(null);
                        AnimUtil.alphaView(mBottomBar, 0, 1.0f, 500);
                    }
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    private void initTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        mTabLayout.setTabTextColors(Color.parseColor("#868585"), Color.parseColor("#117bf2"));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
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
                return DiscoverFragment.newInstance(Session.tabList.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return Session.tabList.get(position).getName();
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

}
