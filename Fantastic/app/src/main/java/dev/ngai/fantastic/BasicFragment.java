package dev.ngai.fantastic;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BasicFragment extends Fragment {

    final String TAG = "BasicFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logc.d(TAG, "onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logc.d(TAG, "onAttach(Context context)");
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Logc.d(TAG, "onAttach(Activity activity)");
    }

    @Override
    public void onStart() {
        super.onStart();
        Logc.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logc.d(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logc.d(TAG, "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logc.d(TAG, "onDestroyView");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logc.d(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logc.d(TAG, "onActivityCreated");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Logc.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
