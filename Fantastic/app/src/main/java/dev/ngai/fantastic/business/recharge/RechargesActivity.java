package dev.ngai.fantastic.business.recharge;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import dev.ngai.fantastic.BaseActivity;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.User;
import dev.ngai.fantastic.data.event.RefreshAccountEvent;
import dev.ngai.fantastic.utils.AnimUtils;

public class RechargesActivity extends BaseActivity {


    @BindView(R.id.balanceEdit)
    EditText balanceEdit;
    @BindView(R.id.balanceEditLayout)
    TextInputLayout balanceEditLayout;
    @BindView(R.id.goldCoinTxt)
    TextView goldCoinTxt;
    @BindView(R.id.exchangeCommit)
    Button exchangeCommit;
    @BindView(R.id.curGoldCoinTxt)
    TextView curGoldCoinTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharges);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("充值兑换");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        exchangeCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String balanceStr = String.valueOf(balanceEdit.getText());
                if (!TextUtils.isEmpty(balanceStr)) {
                    showLoadingDialog(true, "请稍后!", "正在确认中...");
                    int exchangeNumber = Integer.valueOf(balanceStr);
                    updateUser(exchangeNumber);
                } else {
                    AnimUtils.shakeView(balanceEdit);
                }
            }
        });

        balanceEditLayout.setHint("可兑换余额：" + Session.User.getBalance());
        balanceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("RechargeActivity", "beforeTextChanged " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("RechargeActivity", "onTextChanged " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Character.isDigit(s.charAt(0))
                            && Integer.valueOf(s.toString()) > 0) {
                        final int number = Integer.valueOf(s.toString());
                        if (number <= Session.User.getBalance()) {
                            mUIHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    goldCoinTxt.setText(String.valueOf(number * 500));
                                }
                            });
                            Log.d("RechargeActivity", "count " + number);
                        } else {
                            int numberTxt = Integer.valueOf(goldCoinTxt.getText().toString()) / 500;
                            s.clear();
                            if (numberTxt <= 0) {
                                s.append("");
                            } else {
                                s.append(String.valueOf(numberTxt));
                            }
                            mUIHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    balanceEditLayout.setError("兑换必须小于当前余额!");
                                }
                            });
                        }
                        Log.d("RechargeActivity", "afterTextChanged " + number);
                    } else {
                        s.clear();
                    }
                } catch (Exception e) {

                }
            }
        });

        curGoldCoinTxt.setText("当前金币：" + Session.User.getGoldCoin());
    }


    private void updateUser(final int exchangeNumber) {
        Session.User.setBalance(Session.User.getBalance() - exchangeNumber);
        Session.User.setGoldCoin(Session.User.getGoldCoin() + (exchangeNumber * 500));
        User newUser = Session.getUpdateUser();
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Logc.d("RechargeActivity", "更新用户信息成功");
                    goldCoinTxt.setText("0");
                    balanceEdit.setText("");
                    balanceEditLayout.setHint("可兑换余额：" + Session.User.getBalance());
                    curGoldCoinTxt.setText("当前金币：" + Session.User.getGoldCoin());
                } else {
                    Session.User.setBalance(Session.User.getBalance() + exchangeNumber);
                    Session.User.setGoldCoin(Session.User.getGoldCoin() - (exchangeNumber * 500));
                    Logc.d("RechargeActivity", "更新用户信息失败:" + e.getMessage());
                }
                showLoadingDialog(false, "", "");
                EventBus.getDefault().post(new RefreshAccountEvent());
            }
        });
    }

    private MaterialDialog mLoadingDialog;

    public void showLoadingDialog(boolean hasShow, String title, String content) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new MaterialDialog.Builder(this)
                    .title(title)
                    .content(content)
                    .progress(true, 0).show();
        }
        if (hasShow) {
            mLoadingDialog.setTitle(title);
            mLoadingDialog.setContent(content);
            mLoadingDialog.show();
        } else {
            mLoadingDialog.dismiss();
        }
    }
}
