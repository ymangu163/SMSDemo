package com.sms.code.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.CrashlyticsCore;
import com.sms.code.BuildConfig;

import io.fabric.sdk.android.Fabric;

/**
 * File description
 *
 * @author gao
 * @date 2018/3/3
 */

public class AppContext extends Application {
    private static AppContext mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this,  new Answers(), crashlyticsKit);


    }

    public static AppContext getContext() {
        return mContext;
    }
}
