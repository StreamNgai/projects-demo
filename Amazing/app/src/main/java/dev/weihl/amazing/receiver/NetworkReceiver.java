package dev.weihl.amazing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import org.greenrobot.eventbus.EventBus;

import dev.weihl.amazing.Logc;
import dev.weihl.amazing.Tags;
import dev.weihl.amazing.business.BaseActivity;
import dev.weihl.amazing.data.event.NetworkEvent;


/**
 * Des:
 * Created by Weihl
 * 2017/7/19
 */
public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = Tags.NetworkReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Logc.allowPrints()) {
            Logc.d(TAG, "action = " + action);
        }
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            analyConnect(context);
        }
    }

    private static void analyConnect(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    if (Logc.allowPrints()) {
                        Logc.d(TAG, "Network is Wifi !");
                    }
                    break;
                case ConnectivityManager.TYPE_ETHERNET:
                    if (Logc.allowPrints()) {
                        Logc.d(TAG, "Network is ethernet !");
                    }
                    break;

            }
            EventBus.getDefault().post(new NetworkEvent(networkInfo.getType()));
        }
    }

    public static void registerN(final BaseActivity baseActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) baseActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        analyConnect(baseActivity);
                    }
                });
            }
        }
    }
}
