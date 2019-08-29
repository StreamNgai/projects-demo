package dev.weihl.amazing.business.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import dev.weihl.amazing.R;
import dev.weihl.amazing.business.BaseActivity;

public class RegisterActivity extends BaseActivity implements AccountContract.View {

    @BindView(R.id.email)
    AppCompatEditText mEmailEdit;
    @BindView(R.id.password)
    AppCompatEditText mPasswordEdit;
    @BindView(R.id.nextStep)
    Button mRegisterBtn;
    @BindView(R.id.progressBar)
    RelativeLayout mProgressBar;
    @BindView(R.id.accName)
    AppCompatEditText mAccNameEdit;

    AccountContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        new AccountPresenter(this).start();
    }

    @OnClick({R.id.close, R.id.nextStep})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.nextStep:
                mProgressBar.setVisibility(View.VISIBLE);
                String email = mEmailEdit.getText().toString();
                String password = mPasswordEdit.getText().toString();
                String accName = mAccNameEdit.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEmailEdit.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    mPasswordEdit.requestFocus();
                } else if (TextUtils.isEmpty(accName)) {
                    mAccNameEdit.requestFocus();
                } else {
                    mPresenter.register(accName,email, password);
                }
                break;
        }
    }

    @Override
    public void loginResult(BmobUser user) {

    }

    @Override
    public void registerResult(boolean b, String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(errorMessage)) {
            new MaterialDialog.Builder(this)
                    .title("注册失败 !")
                    .content(errorMessage)
                    .positiveText("了解情况")
                    .show();
        } else {
            Intent intent = new Intent();
            intent.putExtra("Result", true);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void retrievePasswordResult(boolean b) {

    }

    @Override
    public void setPresenter(AccountContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }
}
