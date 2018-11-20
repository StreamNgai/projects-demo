package dev.weihl.recyclerview;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MainActivity extends Activity {


    RecyclerView mRecyclerView;
    ArrayList<String> mList;
    int mItemWidth;
    int mSpace = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3885251949,2872570565&fm=27&gp=0.jpg");
        }

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new StaggeredGridAdapter());
        mRecyclerView.addItemDecoration(new ItemDecor());
        layoutManager.setGapStrategy(2);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(size);
        mItemWidth = (size.x - mSpace * 3) / 2;
    }

    class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.ViewHolder> {

        @Override
        public StaggeredGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null));
        }

        @Override
        public void onBindViewHolder(StaggeredGridAdapter.ViewHolder holder, int position) {

            ViewGroup.LayoutParams vlp = holder.mItemImg.getLayoutParams();
            if (vlp == null) {
                vlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            vlp.width = mItemWidth;
            vlp.height = (int) (300 + Math.random() * 400);
            holder.mItemImg.setLayoutParams(vlp);
            Log.d("StaggeredGrid", "position = " + position + " ; height = " + vlp.height + " ; width = " + vlp.width);

            Glide.with(MainActivity.this).load(mList.get(position))
//                    .apply(centerCropTransform())
//                    .apply(fitCenterTransform())
                    .apply(bitmapTransform(new GlideRoundTransform(MainActivity.this, 16, 0)))
                    .into(holder.mItemImg);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView mItemImg;

             ViewHolder(View itemView) {
                super(itemView);
                mItemImg = (ImageView) itemView.findViewById(R.id.itemImg);
            }
        }
    }

    class ItemDecor extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(mSpace, mSpace, mSpace, mSpace);
        }
    }
}
