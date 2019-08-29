package dev.ngai.fantastic.business.samples;

import android.os.Bundle;

import dev.ngai.fantastic.R;
import dev.ngai.fantastic.business.main.MainActivity;
import dev.ngai.fantastic.business.main.discover.DiscoverFragment;
import dev.ngai.fantastic.utils.ActivityUtils;

public class DiscoverActivity extends MainActivity {

    private static final String TAG = "DiscoverActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), DiscoverFragment.newInstance(null),R.id.activity_discover);


//        DiscoverRemoteDataSource.queryQingChunData(120,5);

    }
}
