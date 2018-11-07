package dev.ngai.fantastic.business.main.collect;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.business.main.MainContract;
import dev.ngai.fantastic.data.Discover;
import dev.ngai.fantastic.data.Imginfo;
import dev.ngai.fantastic.data.event.LoadingEvent;
import dev.ngai.fantastic.utils.ActivityUtils;
import dev.ngai.fantastic.utils.AnimUtils;
import dev.ngai.fantastic.utils.ImginfoUtil;

public class CollectTabFragment extends Fragment {
    static private final String HasPayAlbum = "HasPayAlbum";

    @BindView(R.id.collectEmptyLayout)
    LinearLayout collectEmptyLayout;
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.deleteActionLayout)
    View deleteActionLayout;
    @BindView(R.id.cancel_action)
    View cancelAction;
    @BindView(R.id.delete_action)
    View deleteAction;
    @BindView(R.id.download_action)
    View downloadAction;


    boolean notPayAlbum;
    List<Discover> mCollectList;
    Unbinder unbinder;
    boolean mDoDeleteAction;
    List<Integer> mSelectPositions;
    MainContract.CollectPresenter mPresenter;

    public CollectTabFragment() {
        // Required empty public constructor
        mCollectList = new ArrayList<>();
        mSelectPositions = new ArrayList<>();
    }

    public static CollectTabFragment newInstance(boolean hasPayAlbum) {
        CollectTabFragment fragment = new CollectTabFragment();
        Bundle args = new Bundle();
        args.putBoolean(HasPayAlbum, hasPayAlbum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notPayAlbum = getArguments().getBoolean(HasPayAlbum);
        }
    }

    public void onRefresh(List<Discover> list) {
        if (list != null && !list.isEmpty()) {
            mCollectList.clear();
            mCollectList.addAll(list);
            recyclerView.getAdapter().notifyDataSetChanged();
            collectEmptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            collectEmptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_tab, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        titleView.setText(notPayAlbum ? R.string.collect_not_pay : R.string.collect_has_pay);
        initRecyclerView();

        collectEmptyLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        cancelAction.setOnClickListener(deleteActionListener);
        deleteAction.setOnClickListener(deleteActionListener);
        downloadAction.setOnClickListener(deleteActionListener);
        downloadAction.setVisibility(notPayAlbum ? View.GONE : View.VISIBLE);
    }

    View.OnClickListener deleteActionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cancel_action) {
            } else if (v.getId() == R.id.delete_action) {
                List<Discover> deleteDiscovers = new ArrayList<>();
                for (Integer position : mSelectPositions) {
                    deleteDiscovers.add(mCollectList.get(position));
                }
                for (Discover deleteDis : deleteDiscovers) {
                    mCollectList.remove(deleteDis);
                }
                mPresenter.doDeleteCollects(notPayAlbum, mCollectList, deleteDiscovers);
            } else if (v.equals(downloadAction)) {
                List<Discover> downloadDiscovers = new ArrayList<>();
                for (Integer position : mSelectPositions) {
                    downloadDiscovers.add(mCollectList.get(position));
                }
                mPresenter.doDownloadCollects(downloadDiscovers);
            }
            doCancelAction();
        }
    };

    private void doCancelAction() {
        mDoDeleteAction = false;
        mSelectPositions.clear();
        deleteActionLayout.setVisibility(View.GONE);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        int rWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        RelativeLayout.LayoutParams recyRlp = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
        //new RelativeLayout.LayoutParams(rWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        recyRlp.width = rWidth;
        recyclerView.setLayoutParams(recyRlp);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 15;
                outRect.left = 10;
                outRect.right = 10;
//                outRect.bottom = Constant.COLLECT_ITEM_SPACE;

            }
        });
        recyclerView.setAdapter(new CollectAdapter());
    }

    public void setCollectPresenter(MainContract.CollectPresenter collectPresenter) {
        this.mPresenter = collectPresenter;
    }

    class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectHolder> {


        @Override
        public CollectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_collect, null);
            return new CollectHolder(view);
        }

        @Override
        public void onBindViewHolder(CollectHolder holder, int position) {

            Discover discover = mCollectList.get(position);

            String thumbWH = discover.getThumbWh();
            String[] weightHeight = thumbWH.split(Constant.Separated);

            int tWidth = Integer.valueOf(weightHeight[0]);
            int tHeight = Integer.valueOf(weightHeight[1]);
            RelativeLayout.LayoutParams itemRlp = new RelativeLayout.LayoutParams(tWidth, tHeight);
            holder.imageView.setLayoutParams(itemRlp);
            holder.itemView.setLayoutParams(itemRlp);

            List<Imginfo> imgs = mPresenter.parsingDetails(discover.details);
            holder.itemView.setTag(R.id.imgs, imgs);
            holder.itemView.setTag(R.id.obj_id, discover.getId());

            Glide.with(getContext()).load(ImginfoUtil.createGlideUrl(imgs.get(0))).asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop().thumbnail(0.1f)
                    .override(tWidth, tHeight)
                    .into(holder.imageView);
            holder.checkBox.setTag(R.id.index, position);
            holder.checkBox.setVisibility(mDoDeleteAction ? View.VISIBLE : View.GONE);
            holder.checkBox.setChecked(mSelectPositions.contains(position));
            holder.checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
            holder.itemView.setOnClickListener(onClickListener);
            holder.itemView.setLongClickable(true);
            holder.itemView.setOnLongClickListener(onLongClickListener);
            holder.itemView.setTag(R.id.index, position);

        }

        CheckBox.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int) buttonView.getTag(R.id.index);
                if (isChecked) {
                    mSelectPositions.add(position);
                } else {
                    mSelectPositions.remove((Object) position);
                }
            }
        };

        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDoDeleteAction = true;
                int position = (int) v.getTag(R.id.index);
                mSelectPositions.add(position);
                recyclerView.getAdapter().notifyDataSetChanged();
                deleteActionLayout.setVisibility(View.VISIBLE);
                AnimUtils.shakeView(deleteActionLayout);

                return true;
            }
        };

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Imginfo> imgs = (List<Imginfo>) v.getTag(R.id.imgs);
                    int objId = (int) v.getTag(R.id.obj_id);
                    if (!imgs.isEmpty()) {
                        ActivityUtils.startPictureBrowseActivity(getActivity(), objId, imgs, 0, -1, "Collect");
                    }
                } catch (Exception e) {
                }
            }
        };

        @Override
        public int getItemCount() {
            return mCollectList.size();
        }

        class CollectHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.imageView)
            ImageView imageView;

            @BindView(R.id.checkBox)
            CheckBox checkBox;


            View itemView;

            public CollectHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
