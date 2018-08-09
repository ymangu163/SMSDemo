package com.sms.code.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sms.code.utils.DisplayUtil;


/**
 * File description
 *
 * @author gao
 * @date 2018/2/25
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        if (layoutId > 0) {
            setContentView(layoutId);
        }
        // 系统 6.0 以上 状态栏白底黑字的实现方法
        if (Build.VERSION.SDK_INT >= 23) {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        DisplayUtil.MIUISetStatusBarLightMode(this.getWindow(), true);
        DisplayUtil.FlymeSetStatusBarLightMode(this.getWindow(), true);

        initViews();
        initData();
    }

    public abstract int getLayoutId();

    public abstract void initViews();

    public abstract void initData();
}
