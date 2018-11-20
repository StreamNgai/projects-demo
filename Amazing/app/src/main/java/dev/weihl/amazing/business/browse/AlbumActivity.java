package dev.weihl.amazing.business.browse;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.weihl.amazing.Enum;
import dev.weihl.amazing.Logc;
import dev.weihl.amazing.R;
import dev.weihl.amazing.business.BaseActivity;
import dev.weihl.amazing.data.bean.Favorite;
import dev.weihl.amazing.data.bean.Imginfo;

public class AlbumActivity extends BaseActivity implements AlbumContract.View {

    AlbumContract.Presenter mPresenter;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.text)
    TextView mPagerText;
    @BindView(R.id.contentPanel)
    View mContentPanel;
    @BindView(R.id.favorite)
    FloatingActionButton mFavoriteBtn;

    ArrayList<Imginfo> mImgs;
    GestureDetector mDetector;
    float mTotalDistanceX;
    float mTotalDistanceY;
    boolean mNeedFinish = false;
    float mWindowRatio; // <=1;
    int mWindowHeight;
    float mWindowPressHeight;// 点击-点Y坐标距离底部的高度
    DetectorAction mDetectorAction;
    ImgsAdapter mImgsAdapter;
    Favorite mFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }
        getWindow().setFlags(flags, flags);
        Point point = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(point);
        mWindowHeight = point.y;
        Logc.d("WindowRatio", "WindowHeight = " + mWindowHeight);

        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);

        String imgsJson = getIntent().getStringExtra(Enum.IntentKey.Imgs);
        int discoverId = getIntent().getIntExtra(Enum.IntentKey.DiscoverId, -1);
        if (discoverId >= 0 && !TextUtils.isEmpty(imgsJson)) {
            newFavorite(discoverId, imgsJson);
            mImgs = new ArrayList<>();
            initViewPager();
            new AlbumPresenter(this).start();
            mPresenter.onParsingImgs(mFavorite.getImgs());
            onDetector();
            toggleFavoriteBtn();
        } else {
            finish();
        }

    }

    private void newFavorite(int discoverId, String imgsJson) {
        mFavorite = new Favorite();
        String title = getIntent().getStringExtra(Enum.IntentKey.Title);
        String desc = getIntent().getStringExtra(Enum.IntentKey.Desc);
        String tab = getIntent().getStringExtra(Enum.IntentKey.Tab);
        mFavorite.setDiscoverId(discoverId);
        mFavorite.setDesc(desc);
        mFavorite.setTab(tab);
        mFavorite.setTitle(title);
        mFavorite.setImgs(imgsJson);
    }

    private void initViewPager() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPagerText.setText(++position + "/" + mImgsAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mDetectorAction.isViewPagerAction()) {
            mDetector.onTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (mNeedFinish) {
                finish();
            } else {
                Logc.d("dispatchTouchEvent", "onKeyUp !");
                mTotalDistanceX = 0;
                mTotalDistanceY = 0;
                ImgFragment tImgFragment = mImgsAdapter.getCurrFragment();
                if (tImgFragment != null) {
                    tImgFragment.setXY(0, 0);
                    tImgFragment.onScaleXY(1f);
                }
                mContentPanel.setAlpha(1f);
            }
            mDetectorAction.reset();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void onDetector() {
        mDetectorAction = new DetectorAction();
        mDetectorAction.reset();
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
                mWindowPressHeight = mWindowHeight - e.getY();
                Logc.d("WindowRatio", "WindowPressHeight = " + mWindowPressHeight);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if ((velocityX >= 600 || velocityX <= -600)
                        && (e1.getY() - e2.getY() < -300)) {
                    mNeedFinish = true;
                }
                Logc.d("GestureDetector", "onFling !  velocityX = " + velocityX + " ; velocityY = " + velocityY + " ; e1.getY() - e2.getY() = " + (e1.getY() - e2.getY()));
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                if (mDetectorAction.isNeedReset()) {
                    mDetectorAction.onJudge(distanceX, distanceY);
                }

                if (!mDetectorAction.isViewPagerAction()) {
                    mTotalDistanceX = mTotalDistanceX + distanceX;
                    mTotalDistanceY = mTotalDistanceY + distanceY;
                    mWindowRatio = Math.abs(e2.getY() - e1.getY()) / mWindowPressHeight;
                    Logc.d("GestureDetector", "Math.abs(distanceY) = " + Math.abs(distanceY) + " ; WindowRatio = " + mWindowRatio);
                    if (mWindowRatio <= 1.0f) {
                        float tempRatio = 1f;
                        if (e2.getY() >= e1.getY()) {
                            tempRatio = 1.0f - mWindowRatio;
                            mNeedFinish = mWindowRatio > 0.8f;
                        }
                        mContentPanel.setAlpha(tempRatio);
                        ImgFragment tImgFragment = mImgsAdapter.getCurrFragment();
                        if (tImgFragment != null) {
                            tImgFragment.onScaleXY(tempRatio);
                            tImgFragment.setXY(-mTotalDistanceX, -mTotalDistanceY);
                        }

                        Logc.d("GestureDetector", "distanceX = " + distanceX
                                + " ; distanceY = " + distanceY
                                + " ; totalDistanceX = " + mTotalDistanceX
                                + " ; mTotalDistanceY = " + mTotalDistanceY
                                + " ; mWindowRatio = " + mWindowRatio
                        );
                    }
                }

                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    @Override
    public void setPresenter(AlbumContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public void onDisplayImgs(List<Imginfo> imginfos) {
        if (imginfos != null && !imginfos.isEmpty()) {
            mImgs.clear();
            mImgs.addAll(imginfos);
            mImgsAdapter = new ImgsAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mImgsAdapter);
            mPagerText.setText("1/" + mImgsAdapter.getCount());
        }
    }

    @OnClick(R.id.favorite)
    public void onViewClicked() {
        mPresenter.onSaveFavorite(mFavorite, new AlbumContract.FavoriteCallBack() {
            @Override
            public void onResult(Boolean rs) {
                toggleFavoriteBtn();
            }
        });
    }

    private void toggleFavoriteBtn() {
        mFavoriteBtn.setImageResource(mPresenter.isFavorite(mFavorite.discoverId) ? R.drawable.ic_favorite_press : R.drawable.ic_favorite);
    }

    class ImgsAdapter extends FragmentStatePagerAdapter {

        ImgFragment mImgFragment;

        ImgsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImgFragment.newInstance(mImgs.get(position));
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mImgFragment = (ImgFragment) object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {
            return mImgs.size();
        }

        ImgFragment getCurrFragment() {
            return mImgFragment;
        }
    }

    class DetectorAction {
        private boolean mNeedReset;
        private boolean mViewPagerAction;

        DetectorAction() {
        }

        void onJudge(float distanceX, float distanceY) {
            mViewPagerAction = Math.abs(distanceY / distanceX) < 1.0f;
            mNeedReset = false;
            Logc.d("DetectorAction", "ViewPagerAction : " + mViewPagerAction);
        }

        void reset() {
            mViewPagerAction = false;
            mNeedReset = true;
        }

        boolean isNeedReset() {
            return mNeedReset;
        }

        boolean isViewPagerAction() {
            return mViewPagerAction;
        }
    }

}
