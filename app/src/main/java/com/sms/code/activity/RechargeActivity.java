package com.sms.code.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.engine.DownloadImageTask;
import com.sms.code.utils.DisplayUtil;

/**
 * File description
 *
 * @author gao
 * @date 2018/8/30
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private EditText mOrderEt;
    private EditText mVerifyEt;
    private ImageView mVerifyIv;
    private String imgUrl = "http://www.66yzm.com/captcha.html";
    private DownloadImageTask mImageTask;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initViews() {
        mOrderEt = findViewById(R.id.recharge_order_et);
        mVerifyEt = findViewById(R.id.recharge_verify_et);
        mVerifyIv = findViewById(R.id.recharge_verify_iv);
        TextView submitTv = findViewById(R.id.recharge_submit_tv);
        submitTv.setOnClickListener(this);
        mVerifyIv.setOnClickListener(this);
        mImageTask = new DownloadImageTask(mVerifyIv);
        freshVerifyIv();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        DisplayUtil.closeKeybord(mOrderEt);
    }

    private void freshVerifyIv() {
        mImageTask.execute(imgUrl);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.recharge_verify_iv) {
            freshVerifyIv();
        } else if (v.getId() == R.id.recharge_submit_tv) {


        }
    }
}
