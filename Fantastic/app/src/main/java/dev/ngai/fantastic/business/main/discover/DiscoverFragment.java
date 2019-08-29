package dev.ngai.fantastic.business.main.discover;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kekstudio.dachshundtablayout.DachshundTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ngai.fantastic.BasicFragment;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.business.main.MainContract;
import dev.ngai.fantastic.data.DiscoverTab;


public class DiscoverFragment extends BasicFragment implements MainContract.DiscoverView {

    //    @BindView(R.id.tab_layout)
    DachshundTabLayout mDiscoverTabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private final String TAG = "DiscoverFragment";
    private MainContract.DiscoverPresenter mPresenter;
    private ArrayList<DiscoverTab> mDiscoverTabs;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance(ArrayList<DiscoverTab> discoverTabs) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constant.DISCOVER_TABS, discoverTabs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDiscoverTabs = getArguments().getParcelableArrayList(Constant.DISCOVER_TABS);
            Logc.d(TAG, "DiscoverTabs = " + mDiscoverTabs.toString());
        }

        new DiscoverPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
        initTabLayout();
    }


    private void initTabLayout() {
        mDiscoverTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mDiscoverTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViewPager() {

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return mDiscoverTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                DiscoverTabFragment discoverFragment = DiscoverTabFragment.newInstance(mDiscoverTabs.get(position));
                mPresenter.bindDiscoverViewByTab(mDiscoverTabs.get(position), discoverFragment);
                return discoverFragment;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mDiscoverTabs.get(position).name;
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.stop();
    }

    @Override
    public void setPresenter(MainContract.DiscoverPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void setDiscoverTabLayout(DachshundTabLayout discoverTabLayout) {
        this.mDiscoverTabLayout = discoverTabLayout;
    }
}
