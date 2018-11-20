package dev.ngai.fantastic.business.login.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.ngai.fantastic.BasicFragment;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.business.login.LoginContract;

public class LoginFragment extends BasicFragment implements LoginContract.LoginView {

    @BindView(R.id.login_progress)
    ProgressBar loginProgress;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;
    @BindView(R.id.email_sign_in_button)
    Button emailSignInButton;
    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;
    Unbinder unbinder;
    @BindView(R.id.login_form)
    ScrollView loginForm;
    @BindView(R.id.forgetPswBtn)
    Button forgetPswBtn;


    private LoginContract.LoginPresenter mPresenter;
    private LoginFragmentListener mListener;
    private String mUserName ;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String userName) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString("UserName",userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mUserName = getArguments().getString("UserName","");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        loginProgress.setVisibility(View.GONE);

        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailSignInButton.getTag() != null && "ForgotPwd".equals(emailSignInButton.getTag())) {
                    String emailInput = email.getText().toString();
                    if (!mPresenter.legalEmail(emailInput)) {
                        emailLayout.setError("邮箱不合法 !");
                    } else {
                        loginProgress.setVisibility(View.VISIBLE);
                        loginForm.setVisibility(View.GONE);
                        mPresenter.resetPasswordByEmail(emailInput);
                    }
                } else {
                    String passwordInput = password.getText().toString();
                    String emailInput = email.getText().toString();
                    if (TextUtils.isEmpty(passwordInput)) {
                        passwordLayout.setError("密码不合法 !");
                    } else if (TextUtils.isEmpty(emailInput)) {
                        emailLayout.setError("请输入登录邮箱 !");
                    } else if (!mPresenter.legalEmail(emailInput)) {
                        emailLayout.setError("邮箱不合法 !");
                    } else {
                        loginForm.setVisibility(View.GONE);
                        loginProgress.setVisibility(View.VISIBLE);
                        mPresenter.login(emailInput, passwordInput);
                    }
                }
            }
        });

        forgetPswBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPswBtn.setVisibility(View.GONE);
                passwordLayout.setVisibility(View.GONE);
                emailSignInButton.setTag("ForgotPwd");
                emailSignInButton.setText("发送");
            }
        });

        if(!TextUtils.isEmpty(mUserName)){
            email.setText(mUserName);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentListener) {
            mListener = (LoginFragmentListener) context;
        }
    }


    @Override
    public void loginResult(boolean result) {
        if(getActivity().isDestroyed()) return;

        if (!result) {
            loginForm.setVisibility(View.VISIBLE);
            loginProgress.setVisibility(View.GONE);
            password.requestFocus();
            passwordLayout.setError("邮箱或密码不对!");
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void resetPasswordByEmailResult(boolean b) {
        loginForm.setVisibility(View.VISIBLE);
        loginProgress.setVisibility(View.GONE);
        if (b) {
            new MaterialDialog.Builder(getActivity())
                    .title("重置密码提醒!")
                    .content("重置密码请求成功，请尽快到[" + email.getText().toString() + "]邮箱进行密码重置操作")
                    .positiveText("了解情况")
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            getActivity().finish();
                        }
                    }).show();
        } else {
            emailLayout.setError("重置密码失败!请重新输入邮箱再操作!");
        }
    }

    @Override
    public void setPresenter(LoginContract.LoginPresenter presenter) {
        this.mPresenter = presenter;
    }

    public interface LoginFragmentListener {
        void registeredAccount();
    }
}
