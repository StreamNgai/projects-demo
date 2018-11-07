package dev.ngai.fantastic.business.login;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ngai.fantastic.BaseActivity;
import dev.ngai.fantastic.Constant;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.business.login.fragment.LoginFragment;
import dev.ngai.fantastic.business.login.fragment.RegisteredFragment;
import dev.ngai.fantastic.utils.ActivityUtils;

/**
 * @author Weihl
 * @since 2017/7/4
 * Des:
 */
public class LoginActivity extends BaseActivity implements LoginFragment.LoginFragmentListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.contentPanel)
    FrameLayout contentPanel;

    private LoginContract.LoginPresenter mPresenter;
    private LoginFragment loginFragment;
    private RegisteredFragment registeredFragment;
    private boolean mNeedCallBack = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        String userName = analyParm();

        if (mNeedCallBack) {
            toolbar.setNavigationIcon(R.drawable.ic_aleft);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        loginFragment = LoginFragment.newInstance(userName);
        mPresenter = new LoginPresenter(loginFragment);
        mPresenter.start();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginFragment, R.id.contentPanel);

    }

    private String analyParm() {
        String userName = null;
        Bundle tBundle = getIntent().getExtras();
        if (tBundle != null) {
            userName = tBundle.getString(Constant.USER_NAME, "");
            mNeedCallBack = tBundle.getBoolean(Constant.TOOLBAR_NEEDCALLBACK, true);
        }
        return userName;
    }

    @Override
    public void registeredAccount() {
        if (registeredFragment == null) {
            contentPanel.removeAllViews();
            registeredFragment = RegisteredFragment.newInstance();
            mPresenter.bindRegisteredView(registeredFragment);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), registeredFragment, R.id.contentPanel);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }

    public void registeredBtn(View view) {
        registeredAccount();
    }

    public void forgetPswBtn(View view) {
        registeredAccount();
    }
}
