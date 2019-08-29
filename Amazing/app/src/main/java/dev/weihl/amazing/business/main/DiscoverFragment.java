package dev.weihl.amazing.business.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.weihl.amazing.Enum;
import dev.weihl.amazing.Logc;
import dev.weihl.amazing.R;
import dev.weihl.amazing.Tags;
import dev.weihl.amazing.business.browse.AlbumActivity;
import dev.weihl.amazing.data.bean.Discover;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.data.bean.Imginfo;
import dev.weihl.amazing.practical.SpacesItemDecoration;
import dev.weihl.amazing.sharedpres.PrefsKey;
import dev.weihl.amazing.sharedpres.SharedPres;
import dev.weihl.amazing.util.DensityUtil;
import dev.weihl.amazing.util.ImginfoUtil;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.overrideOf;

public class DiscoverFragment extends Fragment implements MainContract.DiscoverView<DiscoverPresenter> {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    Unbinder unbinder;

    int mPosition;
    DiscoverTab mTab;
    DiscoverAdapter mAdapter;
    MainContract.DiscoverPresenter mPresenter;
    CallBack mCallBack;

    public interface CallBack {
        void onScrolled(RecyclerView recyclerView, int dx, int dy);

        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onDetachDiscoverFragment(int position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public DiscoverFragment() {
        super();
    }

    public static DiscoverFragment newInstance(DiscoverTab tab, int position) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putParcelable("Tab", tab);
        args.putInt("Position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTab = getArguments().getParcelable("Tab");
            mPosition = getArguments().getInt("Position");
        }
        new DiscoverPresenter(this).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Logc.d("DiscoverFragment", "onViewCreated");
        initRecyclerView();
        initRefreshLayout();
        int maxDiscoverId = getMaxDiscoverId(mTab);
        mPresenter.loadDiscoverList(mTab, maxDiscoverId, maxDiscoverId == 1);
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        // 使item停止滑动后，不变化位置
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DiscoverAdapter();
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new SpacesItemDecoration(16));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && !recyclerView.canScrollVertically(1)) { // 停止滑动
                    loadLocal();
                }
                if (mCallBack != null) {
                    mCallBack.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mCallBack != null) {
                    mCallBack.onScrolled(recyclerView, dx, dy);
                }
            }

        });
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRemote();
            }
        });
        mSwipeRefreshLayout.setProgressViewEndTarget(true, 300);
    }

    private void loadRemote() {
        if (mTab != null) {
            int maxDiscoverId = getMaxDiscoverId(mTab);
            mPresenter.loadDiscoverList(mTab, maxDiscoverId, true);
            if (Logc.allowPrints()) {
                Logc.d(Tags.MainAction, "loadRemote  MaxDiscoverId = " + maxDiscoverId + " ; load remote " + mTab.toString());
            }
        }
    }

    private void loadLocal() {
        if (mTab != null) {
            int minDiscoverId = mAdapter.getMinDiscoverId();
            mPresenter.loadDiscoverList(mTab, minDiscoverId - 1, false);
            if (Logc.allowPrints()) {
                Logc.d(Tags.MainAction, "loadLocal  MinDiscoverId = " + minDiscoverId + " ; load remote " + mTab.toString());
            }
        }
    }

    private boolean isNonNullList(List list) {
        return list != null && !list.isEmpty();
    }

    private boolean isNonNull(DiscoverTab tab) {
        return tab != null;
    }


    private int getMaxDiscoverId(DiscoverTab tab) {
        int maxId = 0;
        if (tab != null && !TextUtils.isEmpty(tab.getTab())) {
            maxId = SharedPres.getInt(tab.getTab() + PrefsKey.MaxId, 1);
            if (Logc.allowPrints()) {
                Logc.d(Tags.MainAction, "getMaxDiscoverId ! maxId = " + maxId + " ; tab = " + tab.toString());
            }
        }
        return maxId;
    }

    private void setMaxDiscoverId(DiscoverTab tab, int maxId) {
        if (Logc.allowPrints()) {
            Logc.d(Tags.MainAction, "setMaxDiscoverId ! maxId = " + maxId + " ; tab = " + tab.toString());
        }
        SharedPres.putInt(tab.getTab() + PrefsKey.MaxId, maxId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBack) {
            mCallBack = (CallBack) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mCallBack != null){
            mCallBack.onDetachDiscoverFragment(mPosition);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setPresenter(DiscoverPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void refreshDiscoverList(DiscoverTab tab, boolean remote, List<Discover> discoverList) {
        if (Logc.allowPrints()) {
            Logc.d(Tags.MainAction, "refreshDiscoverList ; discoverList is null ? " + !isNonNullList(discoverList));
        }
        if (isNonNullList(discoverList)) {
            if (mTab.getTab().equals(tab.getTab())) {
                int maxIdRs = getMaxDiscoverId(mTab);
                int maxId = discoverList.get(0).id;
                if (maxIdRs < maxId) {
                    setMaxDiscoverId(mTab, maxId);
                }
                if (remote) {
                    mAdapter.addFirstDiscoverList(discoverList);
                } else {
                    mAdapter.addLastDiscoverList(discoverList);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {

        List<Discover> mDiscoverList;
        SparseArray<RelativeLayout.LayoutParams> mItemLayoutParams;
        int mRecyItemWidth = -1;
        int mBarHeight;

        DiscoverAdapter() {
            mBarHeight = DensityUtil.dpToPx(getActivity(), 29);
            mDiscoverList = new ArrayList<>();
            mItemLayoutParams = new SparseArray<>();
        }

        void addFirstDiscoverList(List<Discover> list) {
            mDiscoverList.addAll(0, list);
        }

        void addLastDiscoverList(List<Discover> list) {
            mDiscoverList.addAll(list);
        }

        List<Discover> getDiscoverList() {
            return mDiscoverList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disconver_list, null));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Discover discover = mDiscoverList.get(position);
            if (discover != null) {
                RelativeLayout.LayoutParams itemRlp = mItemLayoutParams.get(position);
                if (itemRlp == null) {
                    itemRlp = getLayoutParams();
                    mItemLayoutParams.put(position, itemRlp);
                }
                holder.itemView.setLayoutParams(itemRlp);
                holder.itemImg.setLayoutParams(itemRlp);
                if (position < 2) {
                    RelativeLayout.LayoutParams itemRlp_ = new RelativeLayout.LayoutParams(itemRlp.width, itemRlp.height);
                    itemRlp_.height = itemRlp_.height + mBarHeight;
                    itemRlp_.topMargin = mBarHeight;
                    holder.itemView.setLayoutParams(itemRlp_);
                }

                holder.itemTx.setText(discover.getTitle());
                Imginfo imginfo = ImginfoUtil.parsingThumb(discover.thumb);
                Glide.with(getActivity())
                        .load(ImginfoUtil.createGlideUrl(imginfo))
                        .apply(centerCropTransform())
                        .apply(overrideOf(itemRlp.width, itemRlp.height))
                        .into(holder.itemImg);

                holder.itemView.setTag(R.id.obj, discover);
                holder.itemView.setOnClickListener(onClickListener);
            }
        }

        private RelativeLayout.LayoutParams getLayoutParams() {

            if (mRecyItemWidth == -1) {
                Point mPoint = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getRealSize(mPoint);
                mRecyItemWidth = (mPoint.x) / 2;
            }

            RelativeLayout.LayoutParams vlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            vlp.width = mRecyItemWidth;
            vlp.height = (int) (350 + Math.random() * 400);
            Logc.d("DiscoverAdapter", "height = " + vlp.height + " ; width = " + vlp.width);
            return vlp;
        }

        @Override
        public int getItemCount() {
            return mDiscoverList != null ? mDiscoverList.size() : 0;
        }

        int getMinDiscoverId() {
            if (isNonNullList(mDiscoverList)) {
                return mDiscoverList.get(mDiscoverList.size() - 1).id;
            }
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView itemTx;
            ImageView itemImg;

            ViewHolder(View itemView) {
                super(itemView);
                itemTx = (TextView) itemView.findViewById(R.id.itemTx);
                itemImg = (ImageView) itemView.findViewById(R.id.itemImg);
            }
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Discover discover = (Discover) v.getTag(R.id.obj);
                if (discover != null) {
                    Intent intent = new Intent(getActivity(), AlbumActivity.class);
                    intent.putExtra(Enum.IntentKey.Imgs, discover.getDetails());
                    intent.putExtra(Enum.IntentKey.DiscoverId, discover.getId());
                    intent.putExtra(Enum.IntentKey.Title, discover.getTitle());
                    intent.putExtra(Enum.IntentKey.Desc, discover.getDesc());
                    intent.putExtra(Enum.IntentKey.Tab, discover.getTab());
                    getActivity().startActivity(intent);
                }
            }
        };
    }
}
