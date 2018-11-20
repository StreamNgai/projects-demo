package dev.ngai.fantastic.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import cn.bmob.v3.BmobUser;


@Entity
public class User extends BmobUser {

    private String oauthPwd; // password
    private String loginUnique; // email
    private int goldCoin;
    private int balance;
    private int score;
    private String privateId;// UserPrivate objectId;

    @Generated(hash = 783218570)
    public User(String oauthPwd, String loginUnique, int goldCoin, int balance,
            int score, String privateId) {
        this.oauthPwd = oauthPwd;
        this.loginUnique = loginUnique;
        this.goldCoin = goldCoin;
        this.balance = balance;
        this.score = score;
        this.privateId = privateId;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getOauthPwd() {
        return this.oauthPwd;
    }

    public void setOauthPwd(String oauthPwd) {
        this.oauthPwd = oauthPwd;
    }

    public String getLoginUnique() {
        return this.loginUnique;
    }

    public void setLoginUnique(String loginUnique) {
        this.loginUnique = loginUnique;
    }

    public int getGoldCoin() {
        return this.goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "balance=" + balance +
                ", oauthPwd='" + oauthPwd + '\'' +
                ", loginUnique='" + loginUnique + '\'' +
                ", goldCoin=" + goldCoin +
                '}';
    }

    public String getPrivateId() {
        return this.privateId;
    }

    public void setPrivateId(String privateId) {
        this.privateId = privateId;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
