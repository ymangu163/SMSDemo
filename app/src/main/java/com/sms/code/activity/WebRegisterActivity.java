package com.sms.code.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sms.code.R;
import com.sms.code.utils.ToastUtils;

/**
 * File description
 *
 * @author gao
 * @date 2018/8/7
 */

public class WebRegisterActivity extends BaseActivity {

    private WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initViews() {
        mWebView = findViewById(R.id.register_webview);

        WebSettings settings = mWebView.getSettings();

        mWebView.requestFocus();
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    @Override
    public void initData() {
        String myUrl = "http://www.66yzm.com/index/register/register?refer_id=50801";
        mWebView.loadUrl(myUrl);
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("/login")) {
                    finish();
                    ToastUtils.showToastForShort(WebRegisterActivity.this, "注册成功！");
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }
}
