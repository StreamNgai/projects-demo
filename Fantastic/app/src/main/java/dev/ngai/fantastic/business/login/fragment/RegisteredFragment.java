package dev.ngai.fantastic.business.login.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.ngai.fantastic.BasicFragment;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.business.login.LoginContract;

public class RegisteredFragment extends BasicFragment implements LoginContract.RegisteredView {

    @BindView(R.id.login_progress)
    ProgressBar loginProgress;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.nameLayout)
    TextInputLayout nameLayout;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.email_sign_in_button)
    Button emailSignInButton;
    @BindView(R.id.registeredEmailLayout)
    LinearLayout registeredEmailLayout;
    @BindView(R.id.registerErrorTip)
    TextView registerErrorTip;
    @BindView(R.id.register_error_btn)
    Button registerErrorBtn;
    @BindView(R.id.registerErrorLayout)
    LinearLayout registerErrorLayout;
    Unbinder unbinder;
    private LoginContract.LoginPresenter mPresenter;

    public RegisteredFragment() {
        // Required empty public constructor
    }

    public static RegisteredFragment newInstance() {
        RegisteredFragment fragment = new RegisteredFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registered, container, false);
        unbinder = ButterKnife.bind(this, view);

        emailSignInButton.setOnClickListener(keyOnClickListener);

        registeredEmailLayout.setVisibility(View.VISIBLE);
        registerErrorLayout.setVisibility(View.GONE);
        registerErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                password.setText("");
                email.setText("");
                registeredEmailLayout.setVisibility(View.VISIBLE);
                registerErrorLayout.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private View.OnClickListener keyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String nameA = name.getText().toString();
            String pswA = password.getText().toString();
            String emailA = email.getText().toString();
            if (TextUtils.isEmpty(nameA)) {
                nameLayout.setError("请输入用户名!");
            } else if (TextUtils.isEmpty(pswA)) {
                passwordLayout.setError("请输入正确密码!");
            } else if (TextUtils.isEmpty(emailA)) {
                emailLayout.setError("请输入正确邮箱!");
            } else if (!mPresenter.legalEmail(emailA)) {
                emailLayout.setError("邮箱不合法!");
            } else {
                loginProgress.setVisibility(View.VISIBLE);
                registeredEmailLayout.setVisibility(View.INVISIBLE);
                mPresenter.registerAccount(email.getText().toString(),
                        true, name.getText().toString(),
                        password.getText().toString());
                emailSignInButton.setVisibility(View.INVISIBLE);
            }

        }
    };

    @Override
    public void setPresenter(LoginContract.LoginPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showVerifyEmailCodeLayout() {

    }

    @Override
    public void registerResult(boolean result, String error) {
        if (result) {
            // 注册成功
            getActivity().finish();
        } else {
            loginProgress.setVisibility(View.GONE);
            registeredEmailLayout.setVisibility(View.VISIBLE);
            emailSignInButton.setVisibility(View.VISIBLE);
            registerErrorTip.setText("注册失败原因:" + error);
            registeredEmailLayout.setVisibility(View.GONE);
            registerErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
