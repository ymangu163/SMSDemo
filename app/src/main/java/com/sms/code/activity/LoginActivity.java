package com.sms.code.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.app.AppContext;
import com.sms.code.bean.TokenBean;
import com.sms.code.engine.ApiAgnet;
import com.sms.code.utils.CommonSharePref;
import com.sms.code.utils.StatConstant;
import com.sms.code.utils.StatUtil;
import com.sms.code.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * File description
 *
 * @author gao
 * @date 2018/8/7
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mNameEt;
    private EditText mPwdEt;
    private TextView mSubmitTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {
        mNameEt = findViewById(R.id.login_name_et);
        mPwdEt = findViewById(R.id.login_pwd_et);
        mSubmitTv = findViewById(R.id.login_submit_tv);

        findViewById(R.id.login_close_iv).setOnClickListener(this);
        mSubmitTv.setOnClickListener(this);

        String policyStr = getString(R.string.no_register);
        SpannableStringBuilder builder = new SpannableStringBuilder(policyStr);
        builder.setSpan(new TextClick(), policyStr.length() - 2, policyStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView policyTv = findViewById(R.id.login_register_tv);
        policyTv.setMovementMethod(LinkMovementMethod.getInstance());
        policyTv.setText(builder);

        mNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mSubmitTv.setEnabled(true);
                } else if (start == 0) {
                    mSubmitTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
        StatUtil.onEvent(StatConstant.SMS_LOGIN);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_close_iv) {
            finish();
        } else if (v.getId() == R.id.login_submit_tv) {
            login();
        }
    }

    private void login() {
        String name = mNameEt.getText().toString().trim();
        String pwdString = mPwdEt.getText().toString().trim();
        if (TextUtils.isEmpty(pwdString)) {
            ToastUtils.showToastForShort(this, R.string.input_pwd);
            return;
        }

        Call<TokenBean> login = ApiAgnet.getApiService().login(name, pwdString);
        login.enqueue(new Callback<TokenBean>() {
            @Override
            public void onResponse(Call<TokenBean> call, Response<TokenBean> response) {
                if (response.code() == 200) {
                    TokenBean bean = response.body();
                    CommonSharePref.getInstance(AppContext.getContext()).setToken(bean.getToken());
                }
                ToastUtils.showToastForShort(AppContext.getContext(), "登录成功");
                finish();
            }

            @Override
            public void onFailure(Call<TokenBean> call, Throwable t) {
                ToastUtils.showToastForShort(AppContext.getContext(), "登录失败，请重试");
            }
        });

    }

    private class TextClick extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            startActivity(new Intent(LoginActivity.this, WebRegisterActivity.class));
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.colorPrimary));
            ds.setUnderlineText(true);
        }
    }

}
