package dev.weihl.amazing.business.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import dev.weihl.amazing.MainApplication;
import dev.weihl.amazing.R;
import dev.weihl.amazing.business.BaseActivity;

public class LoginActivity extends BaseActivity implements AccountContract.View {

    @BindView(R.id.email)
    AppCompatEditText mEmailEdit;
    @BindView(R.id.password)
    AppCompatEditText mPasswordEdit;
    @BindView(R.id.progressBar)
    RelativeLayout mProgressBar;
    AccountContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        new AccountPresenter(this).start();
    }

    @OnClick({R.id.close, R.id.login, R.id.forgetPsw, R.id.userRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.login:
                mProgressBar.setVisibility(View.VISIBLE);
                String email = mEmailEdit.getText().toString();
                String password = mPasswordEdit.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEmailEdit.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    mPasswordEdit.requestFocus();
                } else {
                    mPresenter.login(email, password);
                }
                break;
            case R.id.forgetPsw:
                startActivityForResult(new Intent(this, RetrievePasswordActivity.class), 1000);
                break;
            case R.id.userRegister:
                startActivityForResult(new Intent(this, RegisterActivity.class), 2000);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            finish();
        }
    }

    @Override
    public void setPresenter(AccountContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public void loginResult(BmobUser user) {
        mProgressBar.setVisibility(View.GONE);
        if (user != null) {
            finish();
        } else {
            MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(MainApplication.getTopActivity())
                    .title("登录失败 !")
                    .content("账号或密码错误 !")
                    .positiveText("了 解 !");
        }
    }

    @Override
    public void registerResult(boolean b, String errorMessage) {

    }

    @Override
    public void retrievePasswordResult(boolean b) {

    }
}
