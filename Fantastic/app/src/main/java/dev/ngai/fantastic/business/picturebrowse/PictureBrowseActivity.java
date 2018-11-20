package dev.ngai.fantastic.business.picturebrowse;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ngai.fantastic.BaseActivity;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.Imginfo;
import dev.ngai.fantastic.data.event.SwitchMainPageEvent;
import dev.ngai.fantastic.utils.AnimUtils;
import dev.ngai.fantastic.utils.ImginfoUtil;
import me.relex.circleindicator.CircleIndicator;

public class PictureBrowseActivity extends BaseActivity implements PictureBrowseContract.View {

    @BindView(R.id.background)
    View background;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.topLayout)
    View topLayout;
    @BindView(R.id.bottomLayout)
    View bottomLayout;
    @BindView(R.id.setWallpaper)
    View setWallpaper;


    private PictureBrowseContract.Presenter mPresenter;
    private boolean hasLock = true;
    private GestureDetector mGestureDetector;
    private int mGoldCoin;
    private int mBalance;
    private int mScore;
    private PictureBrowseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏
        setContentView(R.layout.activity_picture_browse);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT > 21)
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        initSimpleTouchEvent();
        new PictureBrowsePresenter(this, getIntent()).start();

        ArrayList<Imginfo> list = mPresenter.findPictureList(getIntent());
        int index = mPresenter.findPictureIndex(getIntent());

        if (list != null && !list.isEmpty()) {
            hasLock = !mPresenter.hasPayAlbum();
            mGoldCoin = mPresenter.payGoldCoin();
            mBalance = mPresenter.payBalance();
            mScore = mPresenter.payScore();
            mAdapter = new PictureBrowseAdapter(getSupportFragmentManager(), list);
            viewPager.setAdapter(mAdapter);
            indicator.setViewPager(viewPager);
        } else {
            finish();
        }

        viewPager.setCurrentItem(index, true);
        viewPager.addOnPageChangeListener(getPageChangeListener());
        text.setText(++index + "/" + mAdapter.getCount());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }

    private void initSimpleTouchEvent() {
        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onContextClick(MotionEvent e) {
                        return super.onContextClick(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        Logc.d("GestureDetector", "onSingleTapConfirmed = " + e.getAction());
                        if (topLayout.getAlpha() == 1) {
                            AnimUtils.alphaView(topLayout, 1f, 0f, 300);
                            AnimUtils.alphaView(bottomLayout, 1f, 0f, 300);
                        } else {
                            AnimUtils.alphaView(topLayout, 0f, 1f, 300);
                            AnimUtils.alphaView(bottomLayout, 0f, 1f, 300);
                        }
                        lockSetWallpaper();
                        return super.onSingleTapConfirmed(e);
                    }
                });
    }

    private void lockSetWallpaper() {
        if (hasLock && viewPager.getCurrentItem() > Constant.LockCount) {
            setWallpaper.setVisibility(View.GONE);
        } else {
            setWallpaper.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void setPresenter(PictureBrowseContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPayAlbumDialog(Discover discover) {

        if (Session.User == null) {
            Snackbar.make(findViewById(R.id.topLayout), "请先绑定账号!", 1000).show();
        } else {
            final int tGoldCoin = mGoldCoin;
            new MaterialDialog.Builder(this)
                    .title("兑换专辑!")
                    .content(getDesc(discover) + "!")
                    .positiveText(createGoldCoinTip())
                    .negativeText("使用积分(" + mScore + ")!")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showProgressDialog();
                            mPresenter.payAlbumByGoldCoin(tGoldCoin);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showProgressDialog();
                            mPresenter.payAlbumByScore(mScore);
//                            mPresenter.payAlbumByRMB(mBalance);
                        }
                    })
                    .show();
        }
    }

    private String getDesc(Discover discover) {
        if (TextUtils.isEmpty(discover.getDesc())) {
            return discover.getTitle();
        }
        return discover.getDesc();
    }

    private MaterialDialog mPressDialog;

    private void showProgressDialog() {
        mPressDialog = new MaterialDialog.Builder(this)
                .content("请稍后，正在为您处理...")
                .progress(true, 0)
                .show();
    }

    private void dissProgressDialog() {
        if (mPressDialog != null) {
            mPressDialog.dismiss();
            mPressDialog = null;
        }
    }


    @Override
    public void showAccessGoldCoin() {
        dissProgressDialog();
        TextView mTx = new TextView(this);
        mTx.setText(getResources().getString(R.string.about));

        new MaterialDialog.Builder(this)
                .title("如何获取金币!")
//                .content(getResources().getString(R.string.about))
                .customView(mTx, true)
                .positiveText("知道了!")
                .show();
    }

    @Override
    public void onPayAlbumResult(boolean hasPaySuccess) {
        dissProgressDialog();
        if (hasPaySuccess) {
            hasLock = !mPresenter.hasPayAlbum();
            Logc.d("PayAlbumResult", "onPayAlbumResult hasLock = " + hasLock);
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof PictureItemFragment) {
                    PictureItemFragment tPictureItemFragment = (PictureItemFragment) fragment;
                    tPictureItemFragment.lockChange(hasLock);
                }
            }
        } else {
            Snackbar.make(indicator, "服务器繁忙，请稍后重新兑换!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showScoreDeficiency() {
        dissProgressDialog();
        new MaterialDialog.Builder(this)
                .title("积分不足!")
                .content("1、点击广告，解锁当前图片。\n\n2、点击一次获取1积分!")
                .negativeText("知道了!")
//                .positiveText("前往充值!")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        finish();
//                        EventBus.getDefault().post(new SwitchMainPageEvent(2));
//                    }
//                })
                .show();
    }

    @Override
    public void showBalanceDeficiency() {

    }

    private String createGoldCoinTip() {
        if (mGoldCoin > Session.User.getGoldCoin()) {
            return "金币不足(" + mGoldCoin + ")!";
        }
        return "使用金币(" + mGoldCoin + ")!";
    }

    public ViewPager.OnPageChangeListener getPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                text.setText((position + 1) + "/" + mAdapter.getCount());
                lockSetWallpaper();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }


    protected class PictureBrowseAdapter extends FragmentStatePagerAdapter {
        ArrayList<Imginfo> pictureList;

        public PictureBrowseAdapter(FragmentManager fm, @NonNull ArrayList<Imginfo> list) {
            super(fm);
            this.pictureList = list;
        }

        @Override
        public PictureItemFragment getItem(int position) {
            Imginfo imginfo = pictureList.get(position);
            PictureItemFragment fragment = PictureItemFragment.newInstance(imginfo, hasLock, mGoldCoin);
            Bundle args = new Bundle();
            args.putInt(PictureItemFragment.GOLD_COIN, mGoldCoin);
            args.putParcelable(PictureItemFragment.IMGINFO, imginfo);
            args.putBoolean(PictureItemFragment.LOCK, position > Constant.LockCount && hasLock);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return pictureList.size();
        }

        public Imginfo getCurrImageUrl() {
            return pictureList.get(viewPager.getCurrentItem());
        }
    }

    public void pictureCancelBtn(View view) {
        finish();
    }


    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
    }

    public void pictureWallpaperBtn(View view) {
        Imginfo imginfo = ((PictureBrowseAdapter) viewPager.getAdapter()).getCurrImageUrl();
        setWallPaper(imginfo);
    }

    //设置壁纸
    public void setWallPaper(Imginfo imginfo) {
        final WallpaperManager mWallManager = WallpaperManager.getInstance(this);
        Glide.with(PictureBrowseActivity.this).load(ImginfoUtil.createGlideUrl(imginfo)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    mWallManager.setBitmap(resource);
                    new MaterialDialog.Builder(PictureBrowseActivity.this)
                            .title("提示!")
                            .content("赞! 壁纸设置成功!")
                            .negativeText("知道了!")
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                    new MaterialDialog.Builder(PictureBrowseActivity.this)
                            .title("提示!")
                            .content("杯具了，可能权限不够,壁纸设置失败! ")
                            .negativeText("知道了!")
                            .show();
                }
            }
        });

    }
}
