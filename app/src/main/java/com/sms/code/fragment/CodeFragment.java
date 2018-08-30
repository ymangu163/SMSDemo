package com.sms.code.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.activity.LoginActivity;
import com.sms.code.activity.RechargeActivity;
import com.sms.code.app.AppContext;
import com.sms.code.bean.ProjectBean;
import com.sms.code.dialog.RechargeDialog;
import com.sms.code.engine.ApiAgnet;
import com.sms.code.utils.CommonSharePref;
import com.sms.code.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File description
 *
 * @author gao
 * @date 2018/8/28
 */

public class CodeFragment extends Fragment implements View.OnClickListener {

    private final int REQUEST_CODE_SEARCH = 0x10;

    private View mRootView;
    private CheckBox mCheckBox;
    private TextView mNameTv;
    private TextView mPhoneTitleTv;
    private TextView mNumberTv;
    private TextView mPhoneCopyTv;
    private TextView mMsgTv;
    private TextView mVerifyTv;
    private TextView mGetVerifyTv;
    private EditText mPhoneEdit;
    private ProjectBean mProjectBean;
    private RechargeDialog mRechargeDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_code, container, false);

        initViews();

        return mRootView;
    }

    private void initViews() {
        mNameTv = mRootView.findViewById(R.id.code_name_tv);
        mCheckBox = mRootView.findViewById(R.id.code_phone_check);
        mPhoneTitleTv = mRootView.findViewById(R.id.code_phone_title);
        mNumberTv = mRootView.findViewById(R.id.code_number_tv);
        mPhoneCopyTv = mRootView.findViewById(R.id.code_copy_phone_tv);
        mMsgTv = mRootView.findViewById(R.id.code_msg_tv);
        mVerifyTv = mRootView.findViewById(R.id.code_verify_tv);
        mGetVerifyTv = mRootView.findViewById(R.id.code_get_verify_tv);
        mPhoneEdit = mRootView.findViewById(R.id.code_special_et);

        mRootView.findViewById(R.id.code_search_view).setOnClickListener(this);
        mRootView.findViewById(R.id.code_get_phone_tv).setOnClickListener(this);
        mRootView.findViewById(R.id.code_special_tv).setOnClickListener(this);
        mRootView.findViewById(R.id.code_copy_verify_tv).setOnClickListener(this);
        mRootView.findViewById(R.id.code_release_tv).setOnClickListener(this);
        mRootView.findViewById(R.id.code_black_tv).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.code_search_view) {
            String token = CommonSharePref.getInstance(AppContext.getContext()).getToken();
            if (TextUtils.isEmpty(token.trim())) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                startActivityForResult(new Intent(getActivity(), RechargeActivity.class), REQUEST_CODE_SEARCH);
            }
        } else if (vId == R.id.code_get_phone_tv) {
            getPhoneNumber();
        }
    }

    private void getPhoneNumber() {
        if (mProjectBean == null) {
            return;
        }
        Call<String> project = ApiAgnet.getApiService().getPhoneNumber(CommonSharePref.getInstance(AppContext.getContext()).getToken()
                , mProjectBean.getId(), 1);
        project.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    return;
                }
                String phoneNumber = response.body();
                handlePhoneNumber(phoneNumber);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtils.showToastForShort(AppContext.getContext(), "操作失败，请重试");
            }
        });

    }

    private void handlePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 11) {
            switch (Integer.valueOf(phoneNumber)) {
                case 10:
                    ToastUtils.showToastForShort(AppContext.getContext(), "获取手机号已达上限");
                    break;
                case 8:
                    ToastUtils.showToastForShort(AppContext.getContext(), "项目不存在");
                    break;
                case 62:
                    ToastUtils.showToastForShort(AppContext.getContext(), "专属项目与普通项目不兼容");
                    break;
                case 9:
                    ToastUtils.showToastForShort(AppContext.getContext(), "余额不足");
                    if (mRechargeDialog == null) {
                        mRechargeDialog = new RechargeDialog(getActivity());
                    }
                    if (!mRechargeDialog.isShowing()) {
                        mRechargeDialog.show();
                    }
                    break;
                case 7:
                    ToastUtils.showToastForShort(AppContext.getContext(), "无手机号该项目封6分钟才可查询");
                    break;
                case 19:
                    ToastUtils.showToastForShort(AppContext.getContext(), "条件太多无号码 ");
                    break;
                default:
                    break;
            }
        } else {
            mNumberTv.setText(phoneNumber);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEARCH) {
            if (resultCode == Activity.RESULT_OK) {
                Object object = data.getSerializableExtra("project");
                if (object != null && object instanceof ProjectBean) {
                    mProjectBean = (ProjectBean) object;
                    mNameTv.setText(mProjectBean.getName());
                }
            }
        }
    }
}
