package com.sms.code.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.app.AppContext;
import com.sms.code.dialog.LoadingDialog;
import com.sms.code.engine.ApiAgnet;
import com.sms.code.engine.DownloadImageTask;
import com.sms.code.utils.DisplayUtil;
import com.sms.code.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private LoadingDialog mLoadingDialog;
    private String mIndexToken;

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

    }

    @Override
    public void initData() {
        getIndexToken();
        freshVerifyIv();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DisplayUtil.closeKeybord(mOrderEt);
    }

    private void freshVerifyIv() {
        new DownloadImageTask(mVerifyIv).execute(imgUrl);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.recharge_verify_iv) {
            freshVerifyIv();
        } else if (v.getId() == R.id.recharge_submit_tv) {
            handleSubmit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialog();

    }

    private void handleSubmit() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        getLoginToken(mIndexToken);

    }

    private void hideDialog() {
        if (mLoadingDialog != null
                && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    private void getIndexToken() {
        Call<String> project = ApiAgnet.getApiService().index();
        project.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    return;
                }
                mIndexToken = parseToken(response.body());
                if (TextUtils.isEmpty(mIndexToken)) {
                    ToastUtils.showToastForShort(AppContext.getContext(), "操作失败，请重试");
                    hideDialog();
                } else {
//                    getLoginToken(indexToken);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtils.showToastForShort(AppContext.getContext(), "操作失败，请重试");
                hideDialog();
            }
        });
    }

    private String parseToken(String content) {
        String compile = ".*__token__.*([A-Za-z0-9]{32}).*";
        Pattern pattern = Pattern.compile(compile);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            if (matcher.groupCount() >= 1) {
                String result = matcher.group(1);
                return result;
            }
        }
        return "";
    }

    private void getLoginToken(String indexToken) {
        String verifyCode = mVerifyEt.getText().toString().trim();
        Call<String> project = ApiAgnet.getApiService().loginIndex("", "",
                verifyCode, indexToken);
        project.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    return;
                }
                Log.e("gao", response.body());
                if (isApiSuccess(response.body())) {
                    chongz();
                } else {
                    ToastUtils.showToastForShort(AppContext.getContext(), "验证码错误，请重试");
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtils.showToastForShort(AppContext.getContext(), "操作失败，请重试");
                hideDialog();
            }
        });


    }

    private boolean isApiSuccess(String content) {
        String compile = ".*(class=\"success).*";
        Pattern pattern = Pattern.compile(compile);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return true;
        }
        return false;
    }


    public void chongz() {
        Call<String> project = ApiAgnet.getApiService().chongz();
        project.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    return;
                }
                String indexToken = parseToken(response.body());
                if (TextUtils.isEmpty(indexToken)) {
                    ToastUtils.showToastForShort(AppContext.getContext(), "操作失败，请重试");
                    hideDialog();
                } else {
                    Log.e("gao", indexToken);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtils.showToastForShort(AppContext.getContext(), "操作失败，请重试");
                hideDialog();
            }
        });
    }
}
