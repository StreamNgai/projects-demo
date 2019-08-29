package dev.ngai.fantastic.business.main.collect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.ngai.fantastic.BasicFragment;
import dev.ngai.fantastic.BuildConfig;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.business.main.MainContract;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;

public class CollectFragment extends BasicFragment implements MainContract.CollectView {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;

    MainContract.CollectPresenter mPresenter;
    DachshundTabLayout mCollectTabLayout;
    SparseArray<CollectTabFragment> mTabFragments;
    List<Object> mResultList;

    public CollectFragment() {
        mTabFragments = new SparseArray<>();
        mResultList = new ArrayList<>();
    }

    public static CollectFragment newInstance() {
        CollectFragment fragment = new CollectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new CollectPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mResultList.isEmpty()) {
            mPresenter.loadCollectData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        unbinder = ButterKnife.bind(this, view);
        mPresenter.start();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        CollectTabFragment collectTabFragment0 = CollectTabFragment.newInstance(true);
        collectTabFragment0.setCollectPresenter(mPresenter);
        mTabFragments.put(0, collectTabFragment0);
        CollectTabFragment collectTabFragment1 = CollectTabFragment.newInstance(false);
        collectTabFragment1.setCollectPresenter(mPresenter);
        mTabFragments.put(1, collectTabFragment1);

        initViewPager();
        initTabLayout();
    }

    private void initViewPager() {
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                return mTabFragments.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return (position == 0) ? "未兑换" : "已兑换";
            }
        });
    }

    private void initTabLayout() {
        mCollectTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mCollectTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void displayCollectDatas(List<Discover> collectsNotPay, List<Discover> collectsHasPay) {

        CollectTabFragment tabFragment0 = mTabFragments.get(0);
        tabFragment0.onRefresh(collectsNotPay);
        CollectTabFragment tabFragment1 = mTabFragments.get(1);
        tabFragment1.onRefresh(collectsHasPay);

        mResultList.addAll(collectsNotPay);
        mResultList.addAll(collectsHasPay);
    }

    @Override
    public void onDataNotAvailable() {
        CollectTabFragment tabFragment0 = mTabFragments.get(0);
        tabFragment0.onRefresh(null);
        CollectTabFragment tabFragment1 = mTabFragments.get(1);
        tabFragment1.onRefresh(null);
        mResultList.clear();
    }

    @Override
    public void showSkipPictureDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("下载成功！")
                .content("现已归类到[Fantastic]文件夹! \n\n是否前往相册/文件管理查找并浏览?")
                .positiveText("前往")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showSkipPicture();
                    }
                })
                .show();
    }

    private void showSkipPicture() {
        File pictureFolder = Environment.getExternalStorageDirectory();
        File picDir = new File(pictureFolder, "Fantastic/");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", picDir);
        Logc.d("showSkipPicture", "photoURI = " + photoURI.getPath());
        intent.setDataAndType(photoURI, "*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void setPresenter(MainContract.CollectPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void setCollectTabLayout(DachshundTabLayout collectTabLayout) {
        this.mCollectTabLayout = collectTabLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
