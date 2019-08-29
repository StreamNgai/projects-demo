package dev.ngai.fantastic.business.splash;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import dev.ngai.fantastic.Appfantastic;
import dev.ngai.fantastic.Logc;
import dev.ngai.fantastic.Session;
import dev.ngai.fantastic.data.DiscoverTab;
import dev.ngai.fantastic.data.event.LoginEvent;
import dev.ngai.fantastic.sharedpres.PrefsKey;
import dev.ngai.fantastic.sharedpres.SharedPres;
import dev.ngai.fantastic.utils.NetworkUtil;

/**
 * @author Ngai
 * @since 2017/7/25
 * Des:
 */
public class SplashPresenter implements SplashContract.Presenter {

    private final String TAG = "SplashPresenter";
    private SplashContract.View mSplashView;

    public SplashPresenter(SplashContract.View view) {
        mSplashView = view;
        mSplashView.setPresenter(this);
    }

    @Override
    public void start() {
        EventBus.getDefault().register(this);

        if(isNetworkConnected()){
            Session.doAccountLogin();
        }
//        else {
//            bindDiscoverTab();
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        bindDiscoverTab();
    }

    private void bindDiscoverTab() {
        if (isNetworkConnected()) {
            BmobQuery<DiscoverTab> query = new BmobQuery<>();
            query.addWhereEqualTo("effective",true);
            query.findObjects(new FindListener<DiscoverTab>() {
                @Override
                public void done(List<DiscoverTab> list, BmobException e) {
                    String rs = "";
                    if (e == null) {
                        String jsonStr = new Gson().toJson(list);
                        SharedPres.putString(PrefsKey.DiscoverTabCache, jsonStr);
                        rs = jsonStr;
                        mSplashView.startMainActivity(list);
                    } else {
                        rs = e.getMessage();
                        mSplashView.notNetworkConnected();
                    }
                    Logc.d(TAG, "bindDiscoverTab.query.tab = " + rs);
                }
            });
        } else {
            String tabCache = SharedPres.getString(PrefsKey.DiscoverTabCache, "");
            if (!TextUtils.isEmpty(tabCache)) {
                Logc.d(TAG, " bindDiscoverTab read tabCache !"+tabCache);
                List<DiscoverTab> list = new Gson().fromJson(tabCache, new TypeToken<List<DiscoverTab>>() {
                }.getType());
                mSplashView.startMainActivity(list);
            }else {
                mSplashView.notNetworkConnected();
            }
        }
    }


    private boolean isNetworkConnected() {
        return NetworkUtil.isNetworkConnected(Appfantastic.getContext());
    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loginRequestSuccess() {
    }
}
