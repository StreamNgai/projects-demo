package dev.ngai.fantasticservice.activity.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.ngai.fantasticservice.R;
import dev.ngai.fantasticservice.TagFragment;

public class BasicFragment extends Fragment {

    private final String TAG = "BasicFragment";
    private int mIndex;
    private ViewPager mViewPager;

    public static BasicFragment newInstance(int position) {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        args.putInt("Index", position);
        fragment.setArguments(args);
        return fragment;
    }

    public BasicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TagFragment.onCreate(TAG + getIndex());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TagFragment.onAttach(TAG + getIndex() + " context");
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TagFragment.onAttach(TAG + getIndex() + " activity");
    }

    @Override
    public void onStart() {
        super.onStart();
        TagFragment.onStart(TAG + getIndex());
    }

    @Override
    public void onResume() {
        super.onResume();
        TagFragment.onResume(TAG + getIndex());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TagFragment.onDestroy(TAG + getIndex());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TagFragment.onDestroyView(TAG + getIndex());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TagFragment.onViewCreated(TAG + getIndex());

//        mViewPager = (ViewPager) getView().findViewById(R.id.viewPager);
//        if (getArguments() != null) {
//            if(getArguments().getInt("Index") == 0){
//                FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
//                    @Override
//                    public Fragment getItem(int position) {
//                        BasicFragment fragment = (BasicFragment) getActivity().getSupportFragmentManager().findFragmentByTag("Basic"+position);
//                        if(fragment == null){
//                            fragment = BasicFragment.newInstance(position+1000);
//                        }
//                        return fragment;
//                    }
//
//                    @Override
//                    public int getCount() {
//                        return 5;
//                    }
//                };
//                mViewPager.setAdapter(adapter);
//            }
//        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TagFragment.onActivityCreated(TAG + getIndex());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TagFragment.onSaveInstanceState(TAG + getIndex());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        TagFragment.onCreateView(TAG + getIndex());
        return inflater.inflate(R.layout.fragment_basic, container, false);
    }


    public String getIndex() {
        if (getArguments() != null) {
            return " " + getArguments().getInt("Index") + " ";
        }
        return " -1 ";
    }
}
