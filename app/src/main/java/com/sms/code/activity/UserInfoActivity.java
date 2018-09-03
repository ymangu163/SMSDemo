package com.sms.code.activity;

import android.text.TextUtils;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.app.AppContext;
import com.sms.code.bean.UserInfo;
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
 * @date 2018/9/2
 */

public class UserInfoActivity extends BaseActivity {

    private TextView mLevelTv;
    private TextView mJifenTv;
    private TextView mMoneyTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initViews() {
        mLevelTv = findViewById(R.id.user_level_tv);
        mJifenTv = findViewById(R.id.user_jifen_tv);
        mMoneyTv = findViewById(R.id.user_money_tv);
    }

    @Override
    public void initData() {
        getUserInfo();
        StatUtil.onEvent(StatConstant.SMS_USER_INFO);
    }

    private void getUserInfo() {
        String token = CommonSharePref.getInstance(AppContext.getContext()).getToken();
        if (TextUtils.isEmpty(token)) {
            return;
        }
        Call<UserInfo> project = ApiAgnet.getApiService().userInfo(token);
        project.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.code() != 200) {
                    return;
                }
                UserInfo bean = response.body();
                if (bean == null) {
                    ToastUtils.showToastForShort(AppContext.getContext(), "请求失败，请重试");
                } else {
                    int level = bean.getDengji() % 10;
                    mLevelTv.setText("VIP" + level);
                    mJifenTv.setText(bean.getJifen());
                    mMoneyTv.setText(bean.getYue());

                }

            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                ToastUtils.showToastForShort(AppContext.getContext(), "请求失败，请重试");
            }
        });
    }
}
