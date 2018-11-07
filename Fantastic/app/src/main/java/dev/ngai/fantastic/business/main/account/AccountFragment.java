package dev.ngai.fantastic.business.main.account;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import dev.ngai.fantastic.Appfantastic;
import dev.ngai.fantastic.BasicFragment;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.R;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.business.appabout.AppAboutsActivity;
import dev.ngai.fantastic.business.feedback.FeedBacksActivity;
import dev.ngai.fantastic.business.funabout.FunctionsAboutActivity;
import dev.ngai.fantastic.business.login.LoginActivity;
import dev.ngai.fantastic.business.main.MainContract;
import dev.ngai.fantastic.business.recharge.RechargesActivity;
import dev.ngai.fantastic.data.event.LoadingEvent;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;
import dev.ngai.fantastic.utils.AnimUtils;
import dev.ngai.fantastic.utils.NetworkUtil;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class AccountFragment extends BasicFragment implements MainContract.AccountView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.accountImg)
    ImageView accountImg;
    @BindView(R.id.accountName)
    TextView accountName;
    @BindView(R.id.accountDesc)
    TextView accountDesc;
    @BindView(R.id.accountQrCode)
    Button accountQrCode;
    @BindView(R.id.accountScore)
    TextView accountScore;
    @BindView(R.id.accountGoldCoin)
    TextView accountGoldCoin;
    @BindView(R.id.accountBalance)
    TextView accountBalance;
    @BindView(R.id.feedBack)
    TextView feedBack;
    @BindView(R.id.functionAbout)
    TextView functionAbout;
    @BindView(R.id.versionUpdate)
    TextView versionUpdate;
    @BindView(R.id.appAbout)
    TextView appAbout;
    @BindView(R.id.accountExit)
    TextView accountExit;
    @BindView(R.id.syncAccCollects)
    TextView syncAccCollects;

    private MainContract.AccountPresenter mPresenter;
    private final String TAG = "AccountFragment";

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AccountPresenter(this).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("我");

//        Glide.with(getActivity()).load(R.mipmap.muni).centerCrop().into(accountImg);
        if (Session.hasLogin()) {
            showUserInfo();
        } else {
            ((View) accountExit.getParent()).setVisibility(View.GONE);
        }

        accountQrCode.setOnClickListener(onClickListener);
        accountExit.setOnClickListener(onClickListener);
        versionUpdate.setOnClickListener(onClickListener);
        functionAbout.setOnClickListener(onClickListener);
        syncAccCollects.setOnClickListener(onClickListener);
        feedBack.setOnClickListener(onClickListener);
        appAbout.setOnClickListener(onClickListener);
        accountBalance.setOnClickListener(onClickListener);
        accountGoldCoin.setOnClickListener(onClickListener);
        accountScore.setOnClickListener(onClickListener);

    }

    private Badge collectBAdge;

    private void tipCollectSync() {
        boolean needSync = SharedPres.getBoolean(PrefsKey.UserPrivateNeedSync, false);
        Logc.d(TAG, "tipCollectSync = " + needSync
                + " ; getContext = " + (getContext() != null)
                + " ; syncAccCollects = " + (syncAccCollects != null)
                + " ; collectBAdge = " + (collectBAdge != null)
        );

        if (needSync) {
            hideCollectBadge();
            collectBAdge = new QBadgeView(getContext()).bindTarget(syncAccCollects)
                    .setBadgeGravity(Gravity.END | Gravity.CENTER)
                    .setBadgeText("S")
                    .setGravityOffset(48, 0, true);
        } else {
            hideCollectBadge();
        }
    }

    private void hideCollectBadge() {
        if (collectBAdge != null) {
            collectBAdge.hide(false);
            collectBAdge = null;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (notNetConnected()) {
                return;
            }

            if (v.equals(accountQrCode)) {
                if (Session.hasLogin()) {
                    showToggleAccount();
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            } else if (v.equals(accountExit)) {
                showAccountExitDialog();
            } else if (v.equals(versionUpdate)) {
                showUpdateAppDialog();
            } else if (v.equals(functionAbout)) {
                getActivity().startActivity(new Intent(getActivity(), FunctionsAboutActivity.class));
            } else if (v.equals(syncAccCollects)) {
                if (notLoginTip(v) && hasEmailVerified(v)) {
                    EventBus.getDefault().post(new LoadingEvent(true, "同步收藏列表", "请稍后..."));
                    mPresenter.onSyncAccCollects();
                }
            } else if (v.equals(feedBack)) {
                if (notLoginTip(v)) {
                    getActivity().startActivity(new Intent(getActivity(), FeedBacksActivity.class));
                }
            } else if (v.equals(appAbout)) {
                getActivity().startActivity(new Intent(getActivity(), AppAboutsActivity.class));
            } else if (v.equals(accountBalance)) {
                if (notLoginTip(v)) {
                    getActivity().startActivity(new Intent(getActivity(), RechargesActivity.class));
                }
            } else if (v.equals(accountGoldCoin)) {
                showGollCoinTip();
            } else if (v.equals(accountScore)) {
                showScoreTip();
            }
        }
    };

    private void showScoreTip() {
        new MaterialDialog.Builder(getActivity())
                .title("积分增益!")
                .content("通过点击浏览广告可以临时解锁图片，并获取得1积分！\n\n积分可用于直接兑换专辑图片!")
                .positiveText("了解情况 ！")
                .show();
    }

    private boolean notNetConnected() {
        if (!NetworkUtil.isNetworkConnected(getContext())) {
            new MaterialDialog.Builder(getActivity())
                    .title("网络提示!")
                    .content("当前网络未连接!")
                    .positiveText("了解情况 ！")
                    .show();
            return true;
        }
        return false;
    }

    private void showGollCoinTip() {
        new MaterialDialog.Builder(getActivity())
                .title("金币增益!")
                .content("每10分钟增益100金币！")
                .positiveText("了解情况 ！")
                .show();
    }

    private boolean hasEmailVerified(View v) {
        if (!Session.hasActivate()) {
            MaterialDialog.Builder dialog = new MaterialDialog.Builder(getActivity())
                    .title("账号激活!")
                    .content("1、同步收藏功能只针对激活用户！" +
                            "\n\n2、激活邮件已发送，请注意查收！" +
                            "\n\n3、若你已激活请退出应用重新登陆")
                    .positiveText("了解情况!");
            if (Session.hasLogin()) {
                dialog.negativeText("发送验证邮箱!")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mPresenter.requestEmailVerify();
                            }
                        });
            }
            dialog.show();
            return false;
        }
        return true;
    }

    private boolean notLoginTip(View v) {
        if (!Session.hasLogin()) {
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
            return false;
        }
        return true;
    }

    private void showToggleAccount() {
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(getActivity())
                .title("账号切换!")
                .content("当前账号正在增益金币!" +
                        "\n\n是否仍然切换?")
                .positiveText("是 ?")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });
        if (Session.hasLogin()) {
            dialog.negativeText("修改密码 ?")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mPresenter.requestResetPassword();
                        }
                    });
        }
        dialog.show();
    }


    private void showUpdateAppDialog() {
        mPresenter.updateApp();
    }

    private void showAccountExitDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("账号退出!")
                .content("1、退出当前账号，将不在增益金币!\n\n" +
                        "2、Fantastic将清空专辑收藏列表!")
                .positiveText("确定退出?")
                .negativeText("注销账号!")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Appfantastic.exit();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new MaterialDialog.Builder(getActivity())
                                .title(Html.fromHtml("<font color='#FF4081'>注销账号!</font>"))
                                .content("1、退出当前账号，将不在增益金币!\n\n" +
                                        "2、Fantastic将清空专辑收藏列表!")
                                .positiveText("确定注销?")
                                .negativeText("再想想!")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        hideCollectBadge();
                                        mPresenter.onAccountExit();
                                    }
                                })
                                .show();
                    }
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        collectBAdge = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.stop();
    }

    @Override
    public void setPresenter(MainContract.AccountPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void updateGoldCoin(int gold) {
        accountGoldCoin.setText(Html.fromHtml(gold + " 金币"));
    }

    public void updateBalance(int balance) {
        accountBalance.setText(Html.fromHtml(balance + " 余额"));
    }

    @Override
    public void showUserInfo() {
        Logc.d(TAG, "showUserInfo ！");
        if (Session.hasLogin()) {
            accountName.setText(Session.User.getUsername());
            updateBalance(Session.User.getBalance());
            updateGoldCoin(Session.User.getGoldCoin());
            updateScore(Session.User.getScore());
            accountDesc.setText("闲来无事,淡看云雨翻腾蓝空!");
            ((View) accountExit.getParent()).setVisibility(View.VISIBLE);
            accountExit.setText("退出当前账号");
        } else {
            accountName.setText("Fantastic");
            updateBalance(0);
            updateGoldCoin(0);
            updateScore(0);
            ((View) accountExit.getParent()).setVisibility(View.GONE);
            accountDesc.setText("Imaginative or fanciful");
        }

        tipCollectSync();
    }

    private void updateScore(int score) {
        accountScore.setText(Html.fromHtml(score + " 积分"));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void nothingAppUpdate(String content) {
        new MaterialDialog.Builder(getActivity())
                .title("版本更新!")
                .content(content)
                .positiveText("了解情况")
                .show();
    }

    @Override
    public void onTickIncreaseCoin(final int goldCoin) {
        Logc.d("onTickIncreaseCoin", "onTickIncreaseCoin ！");
        AnimUtils.rotationX(accountGoldCoin, 0f, 90f, 500);
        accountGoldCoin.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateGoldCoin(goldCoin);
                AnimUtils.rotationX(accountGoldCoin, -90f, 0f, 500);
            }
        }, 500);
    }

    @Override
    public void showTipVersionUpdate() {
        new QBadgeView(getContext())
                .bindTarget(versionUpdate)
                .setBadgeText("v " + Session.UpdateResponse.version)
                .setBadgeGravity(Gravity.END | Gravity.CENTER)
                .setGravityOffset(48, 0, true);
    }

    @Override
    public void showTipRequestEmailVerify(boolean b) {
        new MaterialDialog.Builder(getActivity())
                .title(Session.User.getEmail() + " !")
                .content("激活邮件发送" + (b ? "成功!" : "失败!\n\n请稍后重试!"))
                .positiveText("了解情况")
                .show();
    }

    @Override
    public void showTipResetPassword(boolean b) {
        new MaterialDialog.Builder(getActivity())
                .title(Session.User.getEmail() + " !")
                .content("密码重置邮件发送" + (b ? "成功!有效时间一个小时！" : "失败!\n\n请稍后重试!"))
                .positiveText("了解情况")
                .show();
    }

}
