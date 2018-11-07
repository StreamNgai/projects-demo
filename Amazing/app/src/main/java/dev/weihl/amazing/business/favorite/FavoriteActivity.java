package dev.weihl.amazing.business.favorite;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.weihl.amazing.Logc;
import dev.weihl.amazing.R;
import dev.weihl.amazing.business.BaseActivity;
import dev.weihl.amazing.data.bean.Favorite;
import dev.weihl.amazing.data.bean.Imginfo;
import dev.weihl.amazing.util.ImginfoUtil;

public class FavoriteActivity extends BaseActivity implements FavoriteContract.View {

    @BindView(R.id.close)
    ImageButton mClose;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    FavoriteContract.Presenter mPresenter;
    List<Favorite> mFavoriteList;
    int imgGridLayoutSpace = 20;
    int imgGridWidthHeight;
    String TAG = "FavoriteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        mFavoriteList = new ArrayList<>();
        new FavoritePresenter(this).start();

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onRefreshFavorites(List<Favorite> favoriteList) {
        if (favoriteList != null && !favoriteList.isEmpty()) {
            mFavoriteList.clear();
            mFavoriteList.addAll(favoriteList);
            mRecyclerView.setAdapter(new FavoriteAdapter());
        }
    }

    @Override
    public void setPresenter(FavoriteContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public BaseActivity getActivity() {
        return this;
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

    class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        FavoriteAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            Favorite favorite = (Favorite) getItemObj(position);
            onBindViewDiscover((Holder) holder, position, favorite);
        }

        private void onBindViewDiscover(Holder holder, int position, Favorite favorite) {
            holder.titleView.setText(favorite.getTitle());
            if (TextUtils.isEmpty(favorite.getDesc())) {
                holder.descView.setVisibility(View.GONE);
            } else {
                holder.descView.setText(favorite.getDesc());
            }
            holder.createTimeView.setText(getCreateTime());
            holder.readNumView.setText("阅读 " + getReadNumRandom() + "k");

            List<Imginfo> imgs = ImginfoUtil.parsingDetails(favorite.getImgs());
            holder.updateGridLayoutImageView(favorite.getId(), imgs);
            holder.setPosition(position);

            Imginfo imginfo = imgs.get(0);
            Glide.with(FavoriteActivity.this).load(ImginfoUtil.createGlideUrl(imginfo))
                    .apply(RequestOptions.fitCenterTransform())
                    .thumbnail(0.1f)
                    .into(holder.imageView);
        }

        Object getItemObj(int position) {
            return mFavoriteList.get(position);
        }

        @Override
        public int getItemCount() {
            return (mFavoriteList == null) ? 0 : mFavoriteList.size();
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
            @BindView(R.id.imgGridLayout)
            GridLayout imgGridLayout;
            @BindView(R.id.descView)
            TextView descView;

            View itemView;
            private int mPosition;
            private int mGridCount = 9;

            Holder(View view) {
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


            void updateGridLayoutImageView(Long discoverId, List<Imginfo> imgs) {
                if (imgs != null && !imgs.isEmpty()) {
                    imgGridLayout.removeAllViews();
                    int size = imgs.size() < mGridCount ? imgs.size() : mGridCount;
                    if (Logc.allowPrints()) {
                        Logc.d("updateGridLayoutImageView", "size = " + size + " ; imgs.size() = " + imgs.size());
                    }
                    for (int i = 0; i < size; i++) {
                        GridLayout.LayoutParams gmp = getImgGridLayoutParams();
                        gmp.topMargin = imgGridLayoutSpace;
                        if (i < 3) gmp.topMargin = 0;
                        if (i % 3 == 1) {
                            gmp.leftMargin = imgGridLayoutSpace;
                            gmp.rightMargin = imgGridLayoutSpace;
                        }

                        ImageView tImageView = new ImageView(getActivity());
                        tImageView.setBackgroundResource(R.color.colorBackground);
                        tImageView.setLayoutParams(gmp);
                        View itemView;

                        if (imgs.size() > 9 && i == 8) {
                            itemView = createShapeView(tImageView, gmp, imgs);
                            imgGridLayout.addView(itemView);
                        } else {
                            itemView = tImageView;
                            imgGridLayout.addView(tImageView);
                        }

                        Imginfo imginfo = imgs.get(i);
                        GlideUrl glideUrl = ImginfoUtil.createGlideUrl(imginfo);
                        Glide.with(getActivity()).load(glideUrl)
                                .apply(RequestOptions.fitCenterTransform())
                                .apply(RequestOptions.centerCropTransform())
                                .apply(RequestOptions.overrideOf(imgGridWidthHeight, imgGridWidthHeight))
                                .thumbnail(0.1f).into(tImageView);

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
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            private View createShapeView(ImageView tImageView, ViewGroup.MarginLayoutParams gmp, List<Imginfo> imgs) {
                RelativeLayout relativeLayout = new RelativeLayout(getActivity());
                relativeLayout.setLayoutParams(gmp);
                tImageView.setLayoutParams(getImgGridLayoutParams());
                relativeLayout.addView(tImageView);

                TextView textView = new TextView(getActivity());
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

            void setPosition(int position) {
                this.mPosition = position;
            }
        }
    }

}
