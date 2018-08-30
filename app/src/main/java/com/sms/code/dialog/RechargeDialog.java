package com.sms.code.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.activity.RechargeActivity;


/**
 * File description
 *
 * @author gao
 * @date 2018/7/16
 */

public class RechargeDialog extends AppCompatDialog implements View.OnClickListener {
    private TextView mOkBtn;
    private Context mContext;

    public RechargeDialog(Context context) {
        this(context, R.style.AppDilogTheme);
    }

    public RechargeDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.layout_recharge);

        this.mContext = context;
        mOkBtn = findViewById(R.id.recharge_ok);
        mOkBtn.setOnClickListener(this);

        setWindowParams();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.recharge_ok) {
            mContext.startActivity(new Intent(mContext, RechargeActivity.class));
            dismiss();
        }
    }
}
