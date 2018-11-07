package dev.ngai.fantastic.business.main.discover;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ngai.fantastic.BasicFragment;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.business.main.MainContract;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.DiscoverTab;
import dev.ngai.fantastic.data.Imginfo;
import dev.ngai.fantastic.sharedpres.SharedPres;
import dev.ngai.fantastic.utils.ActivityUtils;
import dev.ngai.fantastic.utils.DiscoverUtil;
import dev.ngai.fantastic.utils.GlideBlurTransform;
import dev.ngai.fantastic.utils.ImginfoUtil;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class DiscoverTabFragment extends BasicFragment implements MainContract.DiscoverTabView {

    public static final String TAG = "DiscoverTabFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private MainContract.DiscoverPresenter mPresenter;
    private static final String KEY_DISCOVER_TAB = "discover_tab";
    private DiscoverTab mTab;
    private List<Object> mList;
    private long maxLastId = -1L;// 最大最后更新Id;
    private Constant.Load loadMode;
    protected int imgGridLayoutSpace = 20;
    private int imgGridWidthHeight;

    public DiscoverTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mTab = getArguments().getParcelable(KEY_DISCOVER_TAB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover_tab, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initSwipeRefreshLayout(view);
        initRecyclerView(view);
        if (mList.isEmpty()) {
            loadInit();
        }
    }

    protected void loadInit() {
        // 加载本地数据库，初始化数据，如果为空，直接调用loadNew();
        swipeRefreshLayout.setRefreshing(true);
        loadMode = Constant.Load.Init;
        maxLastId = SharedPres.getInt(getCategory() + "spf_max_loadId", 0);
        Logc.d(TAG, "load.init ; maxLastId = " + maxLastId);
        onLoadList(loadMode, maxLastId);
    }

    protected void loadNew() {
        swipeRefreshLayout.setRefreshing(true);
        loadMode = Constant.Load.New;
        maxLastId = SharedPres.getInt(getCategory() + "spf_max_loadId", 0);
        Logc.d(TAG, "load.new ; maxLastId = " + maxLastId);
        onLoadList(loadMode, maxLastId);
    }

    protected void loadMore() {
        loadMode = Constant.Load.More;
        maxLastId = SharedPres.getInt(getCategory() + "spf_min_loadId", 0);
        Logc.d(TAG, "load.more ; minLastId = " + maxLastId);
        onLoadList(loadMode, maxLastId);
    }

    private void initRecyclerView(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        StaggeredImageAdapter mStaggeredImageAdapter = new StaggeredImageAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mStaggeredImageAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 8;
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == SCROLL_STATE_IDLE) {
                    // newState 0 stopScroll , 1 Scrool but touching; 2 Scrool is autoScrool;
                    if (mList != null && !mList.isEmpty() && !recyclerView.canScrollVertically(1)) {
                        loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Logc.d(TAG, "RecyclerView . dx = " + dx + "  dy = " + dy);
            }
        });

    }

    private void initSwipeRefreshLayout(View view) {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNew();
            }
        });
        swipeRefreshLayout.setDistanceToTriggerSync(100);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public String getCategory() {
        return mTab.tab;
    }


    protected void notifyDataChange(List<Discover> items) {
        swipeRefreshLayout.setRefreshing(false);
        if (items != null) {
            if (loadMode == Constant.Load.More || loadMode == Constant.Load.Init) { // 升序 返回 加入列表表尾
                mList.addAll(items);
            } else {// 降序 返回 ; 插入列表表头 ; 并增加load 信息
                if (!mList.isEmpty()) {
                    mList.add(0, new LoadInfo(getLoadDate()));
                }

                mList.addAll(0, items);
            }

            int tempMinLastId = ((Discover) mList.get(mList.size() - 1)).id;
            int tempMaxLastId = ((Discover) mList.get(0)).id;
            SharedPres.putInt(getCategory() + "spf_min_loadId", tempMinLastId);
            SharedPres.putInt(getCategory() + "spf_max_loadId", tempMaxLastId);
            Logc.d(TAG, "notifyDataChange . save ; tempMinLastId = " + tempMinLastId + " , tempMaxLastId = " + tempMaxLastId);

            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void onLoadList(Constant.Load loadMode, long lastId) {
        mPresenter.loadDiscoverData(mTab, loadMode, lastId);
    }

    private View.OnClickListener collectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final Discover discover = (Discover) v.getTag(R.id.obj);

            if (!TextUtils.isEmpty(discover.getThumbWh())) {
                DiscoverUtil.displayWh(discover);
                Logc.d(TAG, mTab.name + ".id = " + discover.getId() + " ; thumbWh = " + discover.thumbWh);
                mPresenter.updateDiscover(discover);
                mPresenter.onCollect(discover);
                hasTagCollect((TextView) v, true);
            }

        }
    };

    private void hasTagCollect(TextView collectBtn, boolean isHas) {
        if (isHas) {
            collectBtn.setText("已收藏");
            collectBtn.setTextColor(getResources().getColor(android.R.color.darker_gray));
        } else {
            collectBtn.setText(" 收藏 ");
            collectBtn.setTextColor(Color.parseColor("#FF8200"));
        }
        collectBtn.setClickable(!isHas);
    }

    public String getLoadDate() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public String getReadNumRandom() {
        return new DecimalFormat("#.##").format(new Random().nextDouble() * 20);
    }

    public String getCreateTime() {
        return new SimpleDateFormat("MM-dd HH:ss").format(new Date());
    }

    @Override
    public void displayDiscoverDatas(ArrayList<Discover> datas) {
        notifyDataChange(datas);
    }

    @Override
    public void onDataNotAvailable() {
        notifyDataChange(null);
    }

    @Override
    public void onRefresh(int itemPosition) {

        if (isVisible()) {
            StaggeredImageAdapter tStaggeredImageAdapter = (StaggeredImageAdapter) recyclerView.getAdapter();
            tStaggeredImageAdapter.notifyItemChanged(itemPosition);
        }

        if (itemPosition == -1) {
            StaggeredImageAdapter tStaggeredImageAdapter = (StaggeredImageAdapter) recyclerView.getAdapter();
            tStaggeredImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setPresenter(MainContract.DiscoverPresenter presenter) {
        this.mPresenter = presenter;
    }

    public static DiscoverTabFragment newInstance(DiscoverTab discoverTab) {
        DiscoverTabFragment fragment = new DiscoverTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DISCOVER_TAB, discoverTab);
        fragment.setArguments(bundle);
        return fragment;
    }

    class LoadInfo {
        String date;

        LoadInfo(String date) {
            this.date = date;
        }
    }

    class StaggeredImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int LOADINFO = 0;
        private final int DISCOVER = 1;

        StaggeredImageAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {
                case LOADINFO:
                    View itemViewLoadInfo = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_discovertab_loadinfo, parent, false);
                    return new HolderLoadInfo(itemViewLoadInfo);
                case DISCOVER:
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_discovertab, parent, false);
                    return new Holder(itemView);
                default:
                    break;
            }

            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case LOADINFO:
                    LoadInfo loadInfo = (LoadInfo) getItemObj(position);
                    onBindViewLoadInfo((HolderLoadInfo) holder, position, loadInfo);
                    break;
                case DISCOVER:
                    Discover discover = (Discover) getItemObj(position);
                    onBindViewDiscover((Holder) holder, position, discover);
                    break;
                default:
                    break;
            }
        }

        private void onBindViewLoadInfo(HolderLoadInfo holder, int position, LoadInfo loadInfo) {
            holder.createTimeView.setText("Fantastic 记录您于[ " + loadInfo.date + " ]前看到这里");
        }

        private void onBindViewDiscover(Holder holder, int position, Discover discover) {
            holder.titleView.setText(discover.getTitle());
            if (TextUtils.isEmpty(discover.getDesc())) {
                holder.descView.setVisibility(View.GONE);
            } else {
                holder.descView.setText(discover.getDesc());
            }
            holder.createTimeView.setText(getCreateTime());
            holder.readNumView.setText("阅读 " + getReadNumRandom() + "k");

            holder.collectBtn.setTag(R.id.obj, discover);
            holder.collectBtn.setOnClickListener(collectClickListener);
            boolean isHasTagCollect = mPresenter.hasCollect(discover);//Constant.TAG_COLLECT.equals(discover.getTag());
            hasTagCollect(holder.collectBtn, isHasTagCollect);

//            List<Imginfo> imgs = new Gson().fromJson(discover.getDetails(), new TypeToken<ArrayList<Imginfo>>() {
//            }.getType());
            List<Imginfo> imgs = mPresenter.parsingDetails(discover.getDetails());
            holder.collectBtn.setTag(R.id.imgs, imgs.get(0));
            holder.updateGridLayoutImageView(discover.getId(), imgs);
            holder.setPosition(position);

            Imginfo imginfo = ImginfoUtil.parsingThumb(discover.thumb);
            Glide.with(getContext()).load(ImginfoUtil.createGlideUrl(imginfo))
                    .asBitmap().fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .thumbnail(0.1f)
                    .override(48, 48).placeholder(R.mipmap.muni)
//                .transform(new GlideCircleTransform(getContext()))
                    .into(holder.imageView);
        }

        Object getItemObj(int position) {
            return mList.get(position);
        }

        @Override
        public int getItemViewType(int position) {
            return mList.get(position) instanceof LoadInfo ? LOADINFO : DISCOVER;
        }

        @Override
        public int getItemCount() {
            return (mList == null) ? 0 : mList.size();
        }

        class HolderLoadInfo extends RecyclerView.ViewHolder {
            @BindView(R.id.createTimeView)
            TextView createTimeView;
            View itemView;

            HolderLoadInfo(View itemView) {
                super(itemView);
                this.itemView = itemView;
                ButterKnife.bind(this, itemView);
            }
        }


        class Holder extends RecyclerView.ViewHolder {

            @BindView(R.id.imageView)
            ImageView imageView;
            @BindView(R.id.titleView)
            TextView titleView;
            @BindView(R.id.createTimeView)
            TextView createTimeView;
            @BindView(R.id.readNumView)
            TextView readNumView;
            @BindView(R.id.collectBtn)
            TextView collectBtn;
            @BindView(R.id.imgGridLayout)
            GridLayout imgGridLayout;
            @BindView(R.id.descView)
            TextView descView;

            View itemView;
            private int mPosition;
            private int mGridCount = 9;

            public Holder(View view) {
                super(view);
                itemView = view;
                ButterKnife.bind(this, itemView);

                if (imgGridWidthHeight == 0) {
                    imgGridLayoutSpace = itemView.getPaddingLeft();
                    int width = getActivity().getWindowManager().getDefaultDisplay().getWidth() - itemView.getPaddingLeft() * 2;
                    imgGridWidthHeight = (width - imgGridLayoutSpace * 2) / 3;
                    LinearLayout.LayoutParams imgGridLayoutParam = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                    imgGridLayout.setLayoutParams(imgGridLayoutParam);
                }
            }

            private GridLayout.LayoutParams getImgGridLayoutParams() {
                GridLayout.LayoutParams glp = new GridLayout.LayoutParams();
                glp.width = imgGridWidthHeight;
                glp.height = imgGridWidthHeight;
                return glp;
            }


            void updateGridLayoutImageView(int discoverId, List<Imginfo> imgs) {
                if (imgs != null && !imgs.isEmpty()) {
                    imgGridLayout.removeAllViews();
                    int size = imgs.size() < mGridCount ? imgs.size() : mGridCount;
                    Logc.d("updateGridLayoutImageView", "size = " + size + " ; imgs.size() = " + imgs.size());
                    for (int i = 0; i < size; i++) {
                        GridLayout.LayoutParams gmp = getImgGridLayoutParams();
                        gmp.topMargin = imgGridLayoutSpace;
                        if (i < 3) gmp.topMargin = 0;
                        if (i % 3 == 1) {
                            gmp.leftMargin = imgGridLayoutSpace;
                            gmp.rightMargin = imgGridLayoutSpace;
                        }

                        ImageView tImageView = new ImageView(getContext());
                        tImageView.setBackgroundResource(R.color.colorBackground);
                        tImageView.setLayoutParams(gmp);
                        View itemView;

                        boolean hasNotPay = !mPresenter.hasPayAlbum(discoverId);
                        Logc.d(TAG, "hasNotPay = " + hasNotPay + " / discoverId = " + discoverId);
                        if (imgs.size() > 9 && i == 8) {
                            itemView = createShapeView(tImageView, gmp, imgs);
                            imgGridLayout.addView(itemView);
                        } else {
                            itemView = tImageView;
                            imgGridLayout.addView(tImageView);
                        }

                        Imginfo imginfo = imgs.get(i);
                        GlideUrl glideUrl = ImginfoUtil.createGlideUrl(imginfo);
                        BitmapRequestBuilder<GlideUrl, Bitmap> glideLoad = Glide.with(getContext()).load(glideUrl).asBitmap().centerCrop()
                                .fitCenter()
                                .centerCrop()
                                .thumbnail(0.1f)
                                .override(imgGridWidthHeight, imgGridWidthHeight);

                        if (hasNotPay && i > Constant.LockCount) {
                            glideLoad.transform(new GlideBlurTransform(getContext(), 25, 4)).into(tImageView);
                        } else {
                            glideLoad.into(tImageView);
                        }

                        itemView.setTag(R.id.imgs, imgs);
                        itemView.setTag(R.id.index, i);
                        itemView.setTag(R.id.obj_id, discoverId);
                        itemView.setOnClickListener(onClickListener);
                    }
                }
            }

            private View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        List<Imginfo> imgs = (List<Imginfo>) v.getTag(R.id.imgs);
                        int objId = (int) v.getTag(R.id.obj_id);
                        if (!imgs.isEmpty()) {
                            int index = (int) v.getTag(R.id.index);
                            ActivityUtils.startPictureBrowseActivity(getActivity(), objId, imgs, index, mPosition, mTab.tab);
                        }
                    } catch (Exception e) {
                    }
                }
            };

            private View createShapeView(ImageView tImageView, ViewGroup.MarginLayoutParams gmp, List<Imginfo> imgs) {
                RelativeLayout relativeLayout = new RelativeLayout(getContext());
                relativeLayout.setLayoutParams(gmp);
                tImageView.setLayoutParams(getImgGridLayoutParams());
                relativeLayout.addView(tImageView);

                TextView textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(getImgGridLayoutParams());
                textView.setText(String.valueOf(imgs.size()));
                textView.setTextSize(26);
                textView.setTextColor(getResources().getColor(android.R.color.white));
                int mRadian = 0;
                float[] radius = new float[]{mRadian, mRadian, mRadian, mRadian, mRadian, mRadian, mRadian, mRadian};
                textView.setBackground(getShapeDrawable(radius, Color.parseColor("#6e000000")));
                relativeLayout.addView(textView);
                return relativeLayout;
            }

            private Drawable getShapeDrawable(float[] radius, int color) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadii(radius);
                drawable.setColor(color);
                return drawable;
            }

            public void setPosition(int position) {
                this.mPosition = position;
            }
        }
    }

    protected void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }


}
