package dev.ngai.fantastic.business.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ngai.fantastic.Appfantastic;
import dev.ngai.fantastic.BaseActivity;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.business.login.LoginActivity;
import dev.ngai.fantastic.business.main.MainActivity;
import dev.ngai.fantastic.data.DiscoverTab;
import dev.ngai.fantastic.utils.AnimUtils;

public class SplashActivity extends BaseActivity implements SplashContract.View {

    private final String TAG = "SplashActivity";

    SplashContract.Presenter mPresenter;
    @BindView(R.id.toSetting)
    Button toSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        new SplashPresenter(this).start();

        toSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(mIntent);
            }
        });

        mUIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toSetting.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void notNetworkConnected() {
        AnimUtils.alphaView(toSetting, 0f, 1f, 500);
    }

    @Override
    public void startMainActivity(List<DiscoverTab> list) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putParcelableArrayListExtra(Constant.DISCOVER_TABS, (ArrayList<DiscoverTab>) list);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void startLoginActivity() {
//        Intent loginIntent = new Intent(Appfantastic.getContext(), LoginActivity.class);
//        Bundle mBundle = new Bundle();
//        mBundle.putString(Constant.USER_NAME, Session.User.getLoginUnique());
//        mBundle.putBoolean(Constant.TOOLBAR_NEEDCALLBACK,false);
//        loginIntent.putExtras(mBundle);
//        startActivityForResult(loginIntent, LOGIN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == LOGIN_REQUEST) {
//            mPresenter.loginRequestSuccess();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
