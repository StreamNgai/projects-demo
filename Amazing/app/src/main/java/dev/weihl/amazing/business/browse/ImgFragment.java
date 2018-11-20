package dev.weihl.amazing.business.browse;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.weihl.amazing.Logc;
import dev.weihl.amazing.R;
import dev.weihl.amazing.Tags;
import dev.weihl.amazing.data.bean.Imginfo;
import dev.weihl.amazing.util.ImginfoUtil;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

/**
 */
public class ImgFragment extends Fragment {

    Imginfo mImgInfo;
    @BindView(R.id.photoview)
    PhotoView mPhotoview;
    Unbinder unbinder;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    public ImgFragment() {
        // Required empty public constructor
    }

    public static ImgFragment newInstance(Imginfo imginfo) {
        ImgFragment fragment = new ImgFragment();
        Bundle args = new Bundle();
        args.putParcelable("Img", imginfo);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgInfo = getArguments().getParcelable("Img");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_img, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Glide.with(getActivity())
                .load(ImginfoUtil.createGlideUrl(mImgInfo))
                .apply(fitCenterTransform())
                .thumbnail(0.1f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(mPhotoview);

        mPhotoview.setOnSingleFlingListener(new PhotoViewAttacher.OnSingleFlingListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        mPhotoview.setOnScaleChangeListener(new PhotoViewAttacher.OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                if(Logc.allowPrints()){
                    Logc.d(Tags.Album,"scaleFactor = "+scaleFactor);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onScaleXY(float scaleRatio) {
        mPhotoview.setScaleX(scaleRatio);
        mPhotoview.setScaleY(scaleRatio);
    }

    public void setXY(float x, float y) {
        mPhotoview.setX(x);
        mPhotoview.setY(y);
    }
}
