package dev.weihl.amazing.business.account;

import android.content.DialogInterface;
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

public class RetrievePasswordActivity extends BaseActivity implements AccountContract.View {

    @BindView(R.id.email)
    AppCompatEditText mEmailEdit;
    @BindView(R.id.nextStep)
    Button mNextStepBtn;
    AccountContract.Presenter mPresenter;
    @BindView(R.id.progressBar)
    RelativeLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpsw);
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
                if (TextUtils.isEmpty(email)) {
                    mEmailEdit.requestFocus();
                } else {
                    mPresenter.retrievePassword(email);
                }
                break;
        }
    }

    @Override
    public void loginResult(BmobUser user) {

    }

    @Override
    public void registerResult(boolean b, String errorMessage) {

    }

    @Override
    public void retrievePasswordResult(final boolean result) {
        mProgressBar.setVisibility(View.GONE);
        new MaterialDialog.Builder(this)
                .title("重置密码 !")
                .content((result ? "成功" : "失败") + " ! \n重置操作连接已发送对应邮箱,请及时修改!")
                .positiveText("了解情况")
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (result) {
                            finish();
                        }
                    }
                })
                .show();
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
