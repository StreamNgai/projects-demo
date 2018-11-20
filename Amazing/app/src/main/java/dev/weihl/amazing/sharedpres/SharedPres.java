package dev.weihl.amazing.sharedpres;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import dev.weihl.amazing.MainApplication;

/**
 * Created by Ngai on 2016/11/18.
 * <p>
 * 记录必要属性<br />
 */

public class SharedPres {

    /// get_X
    static public Set<String> getStringSet(String key, Set<String> defValues) {
        return getStringSet(PrefsName.COMMON, key, defValues);
    }

    static public Set<String> getStringSet(String name, String key, Set<String> defValues) {
        return getInstance(name).getStringSet(key, defValues);
    }

    static public boolean getBoolean(String key, boolean defValues) {
        return getBoolean(PrefsName.COMMON, key, defValues);
    }

    static public boolean getBoolean(String name, String key, boolean defValues) {
        return getInstance(name).getBoolean(key, defValues);
    }

    static public float getFloat(String key, float defValues) {
        return getFloat(PrefsName.COMMON, key, defValues);
    }

    static public float getFloat(String name, String key, float defValues) {
        return getInstance(name).getFloat(key, defValues);
    }

    static public int getInt(String key, int defValues) {
        return getInt(PrefsName.COMMON, key, defValues);
    }

    static public int getInt(String name, String key, int defValues) {
        return getInstance(name).getInt(key, defValues);
    }

    static public long getLong(String key, long defValues) {
        return getLong(PrefsName.COMMON, key, defValues);
    }

    static public long getLong(String name, String key, long defValues) {
        return getInstance(name).getLong(key, defValues);
    }

    static public String getString(String key, String defValues) {
        return getString(PrefsName.COMMON, key, defValues);
    }

    static public String getString(String name, String key, String defValues) {
        return getInstance(name).getString(key, defValues);
    }

    /// put_X
    static public boolean putStringSet(String key, Set<String> value) {
        return putStringSet(PrefsName.COMMON, key, value);
    }

    static public boolean putStringSet(String name, String key, Set<String> value) {
        getInstance(name).edit().putStringSet(key, value).commit();
        return true;
    }

    static public boolean putBoolean(String key, boolean value) {
        return putBoolean(PrefsName.COMMON, key, value);
    }

    static public boolean putBoolean(String name, String key, boolean value) {
        getInstance(name).edit().putBoolean(key, value).commit();
        return true;
    }

    static public boolean putFloat(String key, float value) {
        return putFloat(PrefsName.COMMON, key, value);
    }

    static public boolean putFloat(String name, String key, float value) {
        getInstance(name).edit().putFloat(key, value).commit();
        return true;
    }

    static public boolean putInt(String key, int value) {
        return putInt(PrefsName.COMMON, key, value);
    }

    static public boolean putInt(String name, String key, int value) {
        getInstance(name).edit().putInt(key, value).commit();
        return true;
    }

    static public boolean putLong(String key, long value) {
        return putLong(PrefsName.COMMON, key, value);
    }

    static public boolean putLong(String name, String key, long value) {
        getInstance(name).edit().putLong(key, value).commit();
        return true;
    }

    static public boolean putString(String key, String value) {
        return putString(PrefsName.COMMON, key, value);
    }

    static public boolean putString(String name, String key, String value) {
        getInstance(name).edit().putString(key, value).commit();
        return true;
    }

    //// Instance
    static public SharedPreferences getInstance(String name) {
        return MainApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public SharedPres() {
        throw new RuntimeException("Do not allow the instance");
    }

}
