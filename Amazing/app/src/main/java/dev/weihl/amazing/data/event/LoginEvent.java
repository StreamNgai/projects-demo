package dev.weihl.amazing.data.event;

/**
 * Des:
 * Created by Weihl
 * 2017/9/16
 */
public class LoginEvent {

    public boolean mLoginResult;

    public LoginEvent(boolean result) {
        this.mLoginResult = result;
    }
}
