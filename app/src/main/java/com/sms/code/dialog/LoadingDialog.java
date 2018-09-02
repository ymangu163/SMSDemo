package com.sms.code.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sms.code.R;


/**
 * File description
 *
 * @author gao
 * @date 2018/7/16
 */

public class LoadingDialog extends AppCompatDialog {

    private TextView mMsgTv;

    public LoadingDialog(Context context) {
        this(context, R.style.AppDilogTheme);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.layout_loading);

        mMsgTv = findViewById(R.id.loading_title);
        setWindowParams();
    }

    public void setMessage(String msg) {
        mMsgTv.setText(msg);
    }

    //设置Dialog 全屏显示
    private void setWindowParams() {
        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

}
