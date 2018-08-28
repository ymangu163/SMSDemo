package com.sms.code.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gao on 2017/4/10.
 * FreeNet 的SharePreference
 */

public class CommonSharePref {
    private SharedPreferences sharedPreferences = null;
    private static CommonSharePref mSharePref;

    /************************定义 KEY*****************************/
    private final String FREENET_SHAREPREFERENCE = "common_sharepreference";

    private final String KEY_LOGIN_NAME = "key_login_name";
    private final String KEY_LOGIN_TOKEN = "key_login_token";


    /************************结束定义 KEY*****************************/

    public static CommonSharePref getInstance(Context context) {
        if (mSharePref == null) {
            synchronized(CommonSharePref.class) {
                if (mSharePref == null) {
                    mSharePref = new CommonSharePref(context);
                }
            }
        }
        return mSharePref;
    }
    private CommonSharePref(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(FREENET_SHAREPREFERENCE, Context.MODE_PRIVATE);
    }

    private  void putBoolean(String keyString, boolean value) {
        sharedPreferences.edit().putBoolean(keyString, value).apply();
    }
    private boolean getBoolean(String keyString) {
        return sharedPreferences.getBoolean(keyString, false);
    }

    private boolean getBoolean(String keyString, boolean defValue) {
        return sharedPreferences.getBoolean(keyString, defValue);
    }

    private  void putString(String keyString, String value) {
        sharedPreferences.edit().putString(keyString, value).apply();
    }
    private String getString(String keyString) {
        return sharedPreferences.getString(keyString, "");
    }

    private  void putInt(String keyString, int value) {
        sharedPreferences.edit().putInt(keyString, value).apply();
    }
    private int getInt(String keyString) {
        return sharedPreferences.getInt(keyString, -1);
    }

    private int getInt(String keyString, int defaultValue) {
        return sharedPreferences.getInt(keyString, defaultValue);
    }

    private  void putLong(String keyString, long value) {
        sharedPreferences.edit().putLong(keyString, value).apply();
    }
    private long getLong(String keyString) {
        return sharedPreferences.getLong(keyString, -1);
    }

    private long getLong(String keyString, long defValue) {
        return sharedPreferences.getLong(keyString, defValue);
    }
    /**----------------------------------------------------------------------------------------- **/
    public void setLoginName(String name) {
        putString(KEY_LOGIN_NAME, name);
    }

    public String getLoginName() {

       return getString(KEY_LOGIN_NAME);
    }

    public void setToken(String token) {
        putString(KEY_LOGIN_TOKEN, token);
    }

    public String getToken() {
        return getString(KEY_LOGIN_TOKEN);
    }
}
