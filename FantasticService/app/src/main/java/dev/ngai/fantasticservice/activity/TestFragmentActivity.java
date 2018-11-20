package dev.ngai.fantasticservice.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import dev.ngai.fantasticservice.R;
import dev.ngai.fantasticservice.TagFragment;
import dev.ngai.fantasticservice.activity.fragment.BasicFragment;

public class TestFragmentActivity extends AppCompatActivity {

    private final String TAG = "TestFragmentActivity";
    private FrameLayout mContentPanel;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        TagFragment.onCreate(TAG);
        mContentPanel = (FrameLayout) findViewById(R.id.contentPanel);

        addFragment();
//
//        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               addFragment();
//            }
//        });

//        mViewPager = (ViewPager) findViewById(R.id.viewPager);
//
////        FragmentPagerAdapter 缓存内存数据，但不可见时销毁View；（onDestroyView -> onCreateView -> onViewCreated -> ）
////        FragmentStatePagerAdapter 不可见时，整个Fragment销毁；
//        FragmentStatePagerAdapter  adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                BasicFragment fragment = (BasicFragment) getSupportFragmentManager().findFragmentByTag("Basic"+position);
//                if(fragment == null){
//                    fragment = BasicFragment.newInstance(position);
//                }
//                return fragment;
//            }
//
//            @Override
//            public int getCount() {
//                return 5;
//            }
//        };
//        mViewPager.setAdapter(adapter);

    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        BasicFragment fragment = (BasicFragment) fragmentManager.findFragmentByTag("Basic");
        if (fragment == null) {
            fragment = BasicFragment.newInstance(0);
        }
        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (transaction != null && !fragment.isAdded()) {
                transaction.add(R.id.contentPanel, fragment, "Basic");
            } else {
                transaction.remove(fragment);
            }
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        TagFragment.onStart(TAG);
    }

    @Override
    protected void onStop() {
        super.onStop();
        TagFragment.onStop(TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TagFragment.onDestroy(TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TagFragment.onPause(TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TagFragment.onResume(TAG);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TagFragment.onSaveInstanceState(TAG);
    }
}
