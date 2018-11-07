package dev.ngai.fantastic.business.picturebrowse;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.data.Imginfo;
import dev.ngai.fantastic.data.event.PayAlbumEvent;
import dev.ngai.fantastic.utils.ImginfoUtil;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class PictureItemFragment extends Fragment {


    public static final String IMGINFO = "imginfo";
    public static final String LOCK = "lock";
    public static final String GOLD_COIN = "goldCoin";
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.payBtn)
    TextView payBtn;
    @BindView(R.id.payLayout)
    LinearLayout payLayout;
    private Imginfo mImginfo;
    private boolean mLock;
    private int mGoldCoin;

    public PictureItemFragment() {
        // Required empty public constructor
    }

    public static PictureItemFragment newInstance(Imginfo imginfo, boolean lock, int goldCoin) {
        PictureItemFragment fragment = new PictureItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(IMGINFO, imginfo);
        args.putBoolean(LOCK, lock);
        args.putInt(GOLD_COIN, goldCoin);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImginfo = getArguments().getParcelable(IMGINFO);
            mLock = getArguments().getBoolean(LOCK);
            mGoldCoin = getArguments().getInt(GOLD_COIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payBtn.setText(mGoldCoin + " 金币");
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v,"请先绑定账号",1000).show();
                EventBus.getDefault().post(new PayAlbumEvent(PayAlbumEvent.Type.Show));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        start();
    }

    private void start() {
        if (!getActivity().isDestroyed()) {
            Logc.d("PictureItemFragment", "start !");
            if (!TextUtils.isEmpty(mImginfo.url)) {
                BitmapRequestBuilder<GlideUrl, Bitmap> glideLoad = Glide.with(getActivity()).load(ImginfoUtil.createGlideUrl(mImginfo)).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(new RequestListener<GlideUrl, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, GlideUrl model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, GlideUrl model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).thumbnail(0.3f);

                payLayout.setVisibility(mLock ? View.VISIBLE : View.GONE);
                if (mLock) {
                    glideLoad.transform(new BlurTransformation(getContext(), 25, 4));
                }
                glideLoad.into(imageView);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.clear(imageView);
    }


    public void lockChange(boolean hasLock) {
        mLock = hasLock;
        start();
    }
}



