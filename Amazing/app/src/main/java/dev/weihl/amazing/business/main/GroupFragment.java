package dev.weihl.amazing.business.main;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.weihl.amazing.R;
import dev.weihl.amazing.Session;
import dev.weihl.amazing.data.bean.DiscoverTab;
import dev.weihl.amazing.practical.GridSpacingItemDecoration;

/**
 *
 */
public class GroupFragment extends DialogFragment {
    private static final String CURR_TAB = "curr_tab";
    @BindView(R.id.close)
    Button mClose;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    private DiscoverTab mSelectTab;
    private OnTabSelectListener mListener;

    public GroupFragment() {
        // Required empty public constructor
    }

    public static GroupFragment newInstance() {
//        GroupFragment fragment = new GroupFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(CURR_TAB, tab);
//        fragment.setArguments(args);
//        return fragment;
        return new GroupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mCurrTab = getArguments().getParcelable(CURR_TAB);
//        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.GroupDialog);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.close})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.close:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (hasTabList()) {
            mRecyclerView.setAdapter(new GroupAdapter());
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(5, 16, true));
        }
    }

    private boolean hasTabList() {
        return Session.tabList != null && !Session.tabList.isEmpty();
    }

    public void setSelectTab(DiscoverTab selectTab) {
        this.mSelectTab = selectTab;
    }

    class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list, null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DiscoverTab tab = Session.tabList.get(position);
            if (tab != null && !TextUtils.isEmpty(tab.name)) {
                holder.mItemBtn.setText(tab.name);
                holder.mItemBtn.setTag(tab);
                holder.mItemBtn.setOnClickListener(onClickListener);

                boolean isSel = mSelectTab != null && mSelectTab.getTab().equals(tab.getTab());
                holder.mItemBtn.setBackgroundResource(isSel ? R.drawable.item_list_sel_bg : R.drawable.item_list_bg);
            }
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onTabResult((DiscoverTab) v.getTag());
                }
            }
        };

        @Override
        public int getItemCount() {
            return hasTabList() ? Session.tabList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.itemTx)
            TextView mItemBtn;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTabResult(mSelectTab);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTabSelectListener) {
            mListener = (OnTabSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTabSelectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnTabSelectListener {
        void onTabResult(DiscoverTab tab);
    }
}
