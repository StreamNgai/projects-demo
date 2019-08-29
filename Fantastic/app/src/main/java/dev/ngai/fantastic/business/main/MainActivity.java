package dev.ngai.fantastic.business.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ngai.fantastic.BaseActivity;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.business.main.account.AccountFragment;
import dev.ngai.fantastic.business.main.collect.CollectFragment;
import dev.ngai.fantastic.business.main.discover.DiscoverFragment;
import dev.ngai.fantastic.data.DiscoverTab;
import dev.ngai.fantastic.manager.PermissionManager;
import dev.ngai.fantastic.receiver.NetworkReceiver;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;
import dev.ngai.fantastic.utils.AnimUtils;
import dev.ngai.widget.BottomNavigationBar;

public class MainActivity extends BaseActivity implements MainContract.MainView {


    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.navigationTabBar)
    BottomNavigationBar navigationTabBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<DiscoverTab> mDiscoverTabs;
    private CollectFragment mCollectFragment;
    private AccountFragment mAccountFragment;
    private DiscoverFragment mDiscoverFragment;
    private DachshundTabLayout mCollectTabLayout;
    private DachshundTabLayout mDiscoverTabLayout;
    private LinearLayout mDiscoverBarLayout;
    private LinearLayout mCollectBarLayout;
    private LinearLayout mAccountBarLayout;
    private MainContract.MainPresenter mMainPresenter;

    private Chronometer mChronometer;// 当前打开应用使用时长

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Session.initDefaultDisplay(this);

        PermissionManager.checkAndRequestPermission(this);

        mDiscoverTabs = getIntent().getParcelableArrayListExtra(Constant.DISCOVER_TABS);
        if (mDiscoverTabs == null) finish();

        initTabLayout();
        initViewPager();
        initNavBar();
//        startService(new Intent(this, FantasticService.class));
//        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
//                StaggeredPictureFragment.newInstance(StaggeredPictureFragment.Type.GaommQingchun),
//                contentFrame.getId());
//        UpdateAgentUtil.update(this);
        new MainPresenter(this).start();
        NetworkReceiver.registerN(this);


//        GouloveTask tGouloveTask = new GouloveTask(new TaskScheduler.BaseTask.CallBack<String>() {
//            @Override
//            public void onResult(String obj) {
//            }
//        });
//        TaskScheduler.execute(tGouloveTask);

    }

    private void initTabLayout() {
        RelativeLayout barGroupLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.design_main_bar_layout, null);
        mDiscoverBarLayout = (LinearLayout) barGroupLayout.findViewById(R.id.discover_bar_layout);
        mAccountBarLayout = (LinearLayout) barGroupLayout.findViewById(R.id.account_bar_layout);
        mCollectBarLayout = (LinearLayout) barGroupLayout.findViewById(R.id.collect_bar_layout);
        mAccountBarLayout.setAlpha(0);
        mCollectBarLayout.setAlpha(0);
        toolbar.addView(barGroupLayout);
        mChronometer = (Chronometer) mAccountBarLayout.findViewById(R.id.chronometer);
        mChronometer.start();
        mChronometer.setOnChronometerTickListener(getChronometerTickListener());

        mDiscoverTabLayout = (DachshundTabLayout) mDiscoverBarLayout.findViewById(R.id.discover_tab_layout);
        mCollectTabLayout = (DachshundTabLayout) mCollectBarLayout.findViewById(R.id.collect_tab_layout);
    }

    public Chronometer.OnChronometerTickListener getChronometerTickListener() {
        return new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                mMainPresenter.onChronometerTick(chronometer);
            }
        };
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                Logc.d("MainAdapter", "getItem position = " + position);
                switch (position) {
                    case 0:
                        if (mDiscoverFragment == null)
                            mDiscoverFragment = DiscoverFragment.newInstance(mDiscoverTabs);
                        mDiscoverFragment.setDiscoverTabLayout(mDiscoverTabLayout);
                        return mDiscoverFragment;
                    case 1:
                        if (mCollectFragment == null)
                            mCollectFragment = CollectFragment.newInstance();
                        mCollectFragment.setCollectTabLayout(mCollectTabLayout);
                        return mCollectFragment;
                    case 2:
                        if (mAccountFragment == null)
                            mAccountFragment = AccountFragment.newInstance();

                        return mAccountFragment;
                    default:
                        break;
                }
                return null;
            }

        });

    }

    private void initNavBar() {
        navigationTabBar.addTab(navigationTabBar.newTab("discover")
                .setIcon(getResources().getDrawable(R.drawable.ic_discover),
                        getResources().getDrawable(R.drawable.ic_discover_focus))
                .setTitle("发现", Color.parseColor("#6D6D6D"), getResources().getColor(R.color.colorPrimary)));
        navigationTabBar.addTab(navigationTabBar.newTab("collect")
                .setIcon(getResources().getDrawable(R.drawable.ic_collect),
                        getResources().getDrawable(R.drawable.ic_collect_focus))
                .setTitle("收藏", Color.parseColor("#6D6D6D"), getResources().getColor(R.color.colorPrimary)));
        navigationTabBar.addTab(navigationTabBar.newTab("me")
                .setIcon(getResources().getDrawable(R.drawable.ic_me),
                        getResources().getDrawable(R.drawable.ic_me_focus))
                .setTitle("我", Color.parseColor("#6D6D6D"), getResources().getColor(R.color.colorPrimary)));
        navigationTabBar.setupWithViewPager(viewPager, 0);

        navigationTabBar.setNaviBarOnClickListener(new BottomNavigationBar.NaviBarOnClickListener() {
            @Override
            public void onClick(int position, BottomNavigationBar.Tab tab) {
                mDiscoverTabLayout.setAlpha(0);
                mCollectBarLayout.setAlpha(0);
                mAccountBarLayout.setAlpha(0);

                mDiscoverTabLayout.setVisibility(View.GONE);
                mCollectBarLayout.setVisibility(View.GONE);
                mAccountBarLayout.setVisibility(View.GONE);
                switch (position) {
                    case 0:
                        mDiscoverTabLayout.setAlpha(1);
                        mDiscoverTabLayout.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mCollectBarLayout.setAlpha(1);
                        mCollectBarLayout.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mAccountBarLayout.setAlpha(1);
                        mAccountBarLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        navigationTabBar.setNaviBarTabAlphaListener(new BottomNavigationBar.NaviBarTabAlphaListener() {
            @Override
            public void onCurrTab(int position, float alpha) {
                doAlphaTooBarLayout(position, alpha);
            }

            @Override
            public void onNextTab(int position, float alpha) {
                doAlphaTooBarLayout(position, alpha);
            }
        });
    }

    private void doAlphaTooBarLayout(int position, float alpha) {
        switch (position) {
            case 0:
                mDiscoverTabLayout.setAlpha(alpha);
                mDiscoverTabLayout.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);
                break;
            case 1:
                mCollectBarLayout.setAlpha(alpha);
                mCollectBarLayout.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);
                break;
            case 2:
                mAccountBarLayout.setAlpha(alpha);
                mAccountBarLayout.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void setPresenter(MainContract.MainPresenter presenter) {
        this.mMainPresenter = presenter;
    }

    @Override
    public void notWifiNetworkTip() {
        new MaterialDialog.Builder(this)
                .title("Fantastic 提示!")
                .content("当前网络非Wifi，请用户注意使用!因为Fantastic为图片浏览居多，对流量需求大!")
                .positiveText("了解情况")
                .negativeText("了解(并不再提示)!")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        SharedPres.putBoolean(PrefsKey.NotWifiNetworkTip, true);
                    }
                })
                .show();
    }

    private MaterialDialog mLoadingDialog;

    @Override
    public void showLoadingDialog(boolean hasShow, String title, String content) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new MaterialDialog.Builder(this)
                    .title(title)
                    .content(content)
                    .progress(true, 0).show();
        }
        if (hasShow) {
            mLoadingDialog.setTitle(title);
            mLoadingDialog.setContent(content);
            mLoadingDialog.show();
        } else {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onSwitchMainPage(int position) {

        if (position >= 0 && position < viewPager.getAdapter().getCount()) {
            mDiscoverTabLayout.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            mCollectBarLayout.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
            mAccountBarLayout.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            viewPager.setCurrentItem(position, false);

            if (position == 2) {
                View balanceView = findViewById(R.id.accountBalance);
                if (balanceView != null) {
                    AnimUtils.shakeView(balanceView);
                }
            }
        }
    }

//    private Badge mAccountBadge;

    @Override
    public void showEmailNotVerifiedBadge() {
//        if (mAccountBadge == null) {
//            String txt = null;
//            if (!NetworkUtil.isNetworkConnected(this)) {
//                txt = "未联网";
//            } else if (Session.User == null) {
//            } else if (!Session.hasActivate()) {
//                txt = "待激活";
//            }
//            if (!TextUtils.isEmpty(txt)) {
//                mAccountBadge = new QBadgeView(this)
//                        .bindTarget(navigationTabBar)
//                        .setBadgeText(txt)
//                        .setBadgeGravity(Gravity.END | Gravity.TOP)
//                        .setGravityOffset(16, 0, true);
//            }
//        } else if (Session.User.getEmailVerified() && mAccountBadge != null) {
//            mAccountBadge.hide(false);
//            mAccountBadge = null;
//        }
    }
}
