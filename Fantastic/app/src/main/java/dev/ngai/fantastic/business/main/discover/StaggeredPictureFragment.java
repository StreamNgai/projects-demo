//package dev.ngai.fantastic.business.main.discover;
//
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.GradientDrawable;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.BitmapRequestBuilder;
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import dev.ngai.fantastic.BasicFragment;
//import dev.ngai.fantastic.Constant;
//import dev.ngai.fantastic.Logc;
//import dev.ngai.fantastic.R;
//import dev.ngai.fantastic.sharedpres.SharedPres;
//import dev.ngai.fantastic.utils.ActivityUtils;
//import dev.ngai.fantastic.utils.GlideBlurTransform;
//
//import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
//
//public abstract class StaggeredPictureFragment<T> extends BasicFragment {
//
//    public static final String TAG = "StaggeredPicture";
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;
//
//    private List<T> mList;
//    private int recyclerViewwidth;
//    private long maxLastId = -1L;// 最大最后更新Id;
//    private Constant.Load loadMode;
//
//    protected int imgGridLayoutSpace = 20;
//    private int imgGridWidthHeight;
//
//    protected StaggeredPictureFragment() {
//        // Required empty public constructor
//    }
//
//    protected List<T> getDataList() {
//        return mList;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (mList == null) {
//            mList = new ArrayList<>();
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_staggered_picture, container, false);
//        ButterKnife.bind(this, view);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//
//        initSwipeRefreshLayout(view);
//        initRecyclerView(view);
//
//        int windowWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
//
//        recyclerViewwidth = windowWidth / 2;
//        if (mList.isEmpty()) {
//            loadInit();
//        }
//
//    }
//
//    protected void loadInit() {
//        // 加载本地数据库，初始化数据，如果为空，直接调用loadNew();
//        swipeRefreshLayout.setRefreshing(true);
//        loadMode = Constant.Load.Init;
//        maxLastId = SharedPres.getInt(getUnique() + "spf_max_loadId", 0);
//        Logc.d(TAG, "load.init ; maxLastId = " + maxLastId);
//        onLoadList(loadMode, maxLastId);
//    }
//
//    protected void loadNew() {
//        swipeRefreshLayout.setRefreshing(true);
//        loadMode = Constant.Load.New;
//        maxLastId = SharedPres.getInt(getUnique() + "spf_max_loadId", 0);
//        Logc.d(TAG, "load.new ; maxLastId = " + maxLastId);
//        onLoadList(loadMode, maxLastId);
//    }
//
//    protected void loadMore() {
//        loadMode = Constant.Load.More;
//        maxLastId = SharedPres.getInt(getUnique() + "spf_min_loadId", 0);
//        Logc.d(TAG, "load.more ; minLastId = " + maxLastId);
//        onLoadList(loadMode, maxLastId);
//    }
//
//    private void initRecyclerView(View view) {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        StaggeredImageAdapter StaggeredImageAdapter = new StaggeredImageAdapter();
//
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(StaggeredImageAdapter);
//        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
//                outRect.top = 32;
//            }
//        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == SCROLL_STATE_IDLE) {
//                    // newState 0 stopScroll , 1 Scrool but touching; 2 Scrool is autoScrool;
//                    if (mList != null && !mList.isEmpty() && !recyclerView.canScrollVertically(1)) {
//                        loadMore();
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
////                Logc.d(TAG, "RecyclerView . dx = " + dx + "  dy = " + dy);
//            }
//        });
//
//    }
//
//    private void initSwipeRefreshLayout(View view) {
//        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadNew();
//            }
//        });
//        swipeRefreshLayout.setDistanceToTriggerSync(100);
//    }
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    public abstract String getUnique();
//
//
//    protected void notifyDataChange(List<T> items) {
//        swipeRefreshLayout.setRefreshing(false);
//        if (items != null) {
//
//            if (loadMode == Constant.Load.More) { // 升序 返回 加入列表表尾
//                mList.addAll(items);
//            } else {// 降序 返回 ; 插入列表表头
//                mList.addAll(0, items);
//            }
//            if (mList.isEmpty()) return;
//            int tempMinLastId = getMinLastId();
//            int tempMaxLastId = getMaxLastId();
//            SharedPres.putInt(getUnique() + "spf_min_loadId", tempMinLastId);
//            SharedPres.putInt(getUnique() + "spf_max_loadId", tempMaxLastId);
//            Logc.d(TAG, "notifyDataChange . save ; tempMinLastId = " + tempMinLastId + " , tempMaxLastId = " + tempMaxLastId);
//
//            recyclerView.getAdapter().notifyDataSetChanged();
//        }
//    }
//
//    protected abstract int getMaxLastId();
//
//    protected abstract int getMinLastId();
//
//    abstract void onLoadList(Constant.Load loadMode, long lastId);
//
//
//    class StaggeredImageAdapter extends RecyclerView.Adapter<StaggeredImageAdapter.Holder> {
//
//
//        public StaggeredImageAdapter() {
//
//        }
//
//        @Override
//        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_staggeredimage, parent, false);
//            return new Holder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final Holder holder, int position) {
//            onBindView(holder, mList.get(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return (mList == null) ? 0 : mList.size();
//        }
//
//        class Holder extends RecyclerView.ViewHolder {
//
//            @BindView(R.id.imageView)
//            ImageView imageView;
//            @BindView(R.id.titleView)
//            TextView titleView;
//            @BindView(R.id.createTimeView)
//            TextView createTimeView;
//            @BindView(R.id.readNumView)
//            TextView readNumView;
//            @BindView(R.id.collectBtn)
//            TextView collectBtn;
//            @BindView(R.id.imgGridLayout)
//            GridLayout imgGridLayout;
//            @BindView(R.id.descView)
//            TextView descView;
//
//            View itemView;
//
//            public Holder(View view) {
//                super(view);
//                itemView = view;
//                ButterKnife.bind(this, itemView);
//
//                if (imgGridWidthHeight == 0) {
//                    int width = getActivity().getWindowManager().getDefaultDisplay().getWidth() - itemView.getPaddingLeft() * 2;
//                    imgGridWidthHeight = (width - imgGridLayoutSpace * 2) / 3;
//                    LinearLayout.LayoutParams imgGridLayoutParam = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    imgGridLayout.setLayoutParams(imgGridLayoutParam);
//                }
//            }
//
//            private ViewGroup.MarginLayoutParams getImgGridLayoutParams() {
//                return new ViewGroup.MarginLayoutParams(imgGridWidthHeight, imgGridWidthHeight);
//            }
//
//
//            public void updateGridLayoutImageView(List<String> imgs) {
//                if (imgs != null && !imgs.isEmpty()) {
//                    imgGridLayout.removeAllViews();
//                    int size = imgs.size() < 9 ? imgs.size() : 9;
//                    Logc.d("updateGridLayoutImageView", "size = " + size + " ; imgs.size() = " + imgs.size());
//                    for (int i = 0; i < size; i++) {
//                        ViewGroup.MarginLayoutParams gmp = getImgGridLayoutParams();
//                        gmp.topMargin = imgGridLayoutSpace;
//                        if (i < 3) gmp.topMargin = 0;
//                        if (i % 3 == 1) {
//                            gmp.leftMargin = imgGridLayoutSpace;
//                            gmp.rightMargin = imgGridLayoutSpace;
//                        }
//
//                        ImageView tImageView = new ImageView(getContext());
//                        tImageView.setBackgroundResource(R.color.colorBackground);
//                        tImageView.setLayoutParams(gmp);
//                        View itemView;
//                        if (imgs.size() > 9 && i == 8) {
//                            itemView = createShapeView(tImageView, gmp, imgs);
//                            imgGridLayout.addView(itemView);
//                        } else {
//                            itemView = tImageView;
//                            imgGridLayout.addView(tImageView);
//                        }
//
//                        BitmapRequestBuilder<String, Bitmap> glideLoad = Glide.with(getContext()).load(imgs.get(i)).asBitmap().centerCrop()
//                                .fitCenter()
//                                .centerCrop()
//                                .thumbnail(0.1f)
//                                .override(imgGridWidthHeight, imgGridWidthHeight);
//
//                        if (i > 4) {
//                            glideLoad.transform(new GlideBlurTransform(getContext(), 25, 4)).into(tImageView);
//                        } else {
//                            glideLoad.into(tImageView);
//                        }
//
//                        itemView.setTag(R.id.imgs, imgs);
//                        itemView.setTag(R.id.index, i);
//                        itemView.setOnClickListener(onClickListener);
//                    }
//                }
//            }
//
//            private View.OnClickListener onClickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        List<String> imgs = (List<String>) v.getTag(R.id.imgs);
//                        if (!imgs.isEmpty()) {
//                            int index = (int) v.getTag(R.id.index);
//                            ActivityUtils.startPictureBrowseActivity(getActivity(),imgs,index);
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//            };
//
//            private View createShapeView(ImageView tImageView, ViewGroup.MarginLayoutParams gmp, List<String> imgs) {
//                RelativeLayout relativeLayout = new RelativeLayout(getContext());
//                relativeLayout.setLayoutParams(gmp);
//                tImageView.setLayoutParams(getImgGridLayoutParams());
//                relativeLayout.addView(tImageView);
//
//                TextView textView = new TextView(getContext());
//                textView.setGravity(Gravity.CENTER);
//                textView.setLayoutParams(getImgGridLayoutParams());
//                textView.setText(String.valueOf(imgs.size()));
//                textView.setTextSize(26);
//                textView.setTextColor(getResources().getColor(android.R.color.white));
//                int mRadian = 0;
//                float[] radius = new float[]{mRadian, mRadian, mRadian, mRadian, mRadian, mRadian, mRadian, mRadian};
//                textView.setBackground(getShapeDrawable(radius, Color.parseColor("#6e000000")));
//                relativeLayout.addView(textView);
//                return relativeLayout;
//            }
//
//            private Drawable getShapeDrawable(float[] radius, int color) {
//                GradientDrawable drawable = new GradientDrawable();
//                drawable.setCornerRadii(radius);
//                drawable.setColor(color);
//                return drawable;
//            }
//        }
//    }
//
//    protected abstract void onBindView(StaggeredImageAdapter.Holder holder, T t);
//
//    protected void toast(String text) {
//        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
//    }
//
//    private void log(@NonNull Fragment fragment, String text) {
//        Log.d(fragment.getClass().getName(), text);
//    }
//
//
//}
