package com.sms.code.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.activity.LoginActivity;
import com.sms.code.activity.SearchActivity;
import com.sms.code.app.AppContext;
import com.sms.code.bean.MsgBean;
import com.sms.code.bean.ProjectBean;
import com.sms.code.dialog.RechargeDialog;
import com.sms.code.engine.ApiAgnet;
import com.sms.code.utils.CommonSharePref;
import com.sms.code.utils.GsonUtil;
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
 * @date 2018/8/28
 */

public class CodeFragment extends Fragment implements View.OnClickListener {

    private final int REQUEST_CODE_SEARCH = 0x10;
    private final int MSG_QUERY_VERFITY_CODE = 011;

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
    private long mGetMsgStartTime;
    private int mGetMsgTimes;

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
        mPhoneCopyTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.code_search_view) {
            String token = CommonSharePref.getInstance(AppContext.getContext()).getToken();
            if (TextUtils.isEmpty(token.trim())) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                startActivityForResult(new Intent(getActivity(), SearchActivity.class), REQUEST_CODE_SEARCH);
            }
        } else if (vId == R.id.code_get_phone_tv) {
            getPhoneNumber();
//            parseMsgCode("");
        } else if (vId == R.id.code_copy_phone_tv) {
            String phoneStr = mNumberTv.getText().toString().trim();
            if (TextUtils.isEmpty(phoneStr)) {
                return;
            }
            copy2Clipboard(phoneStr);
        } else if (vId == R.id.code_release_tv) {
            releasePhone();
        } else if (vId == R.id.code_copy_verify_tv) {
            String phoneStr = mVerifyTv.getText().toString().trim();
            if (TextUtils.isEmpty(phoneStr)) {
                return;
            }
            copy2Clipboard(phoneStr);
        } else if (vId == R.id.code_black_tv) {
            blackList();
        }
    }

    private void parseDemo() {
        String content = " <input type=\"hidden\" name=\"__token__\" value=\"ffdf18fe922416e490f5c03ff8ef767f\" />";
        String compile = ".*__token__.*([A-Za-z0-9]{32}).*";
        Pattern pattern = Pattern.compile(compile);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            if (matcher.groupCount() >= 1) {
                String result = matcher.group(1);
                Log.e("gao", result);
            }
        }
    }


    private void getPhoneNumber() {
        if (mProjectBean == null) {
            ToastUtils.showToastForShort(AppContext.getContext(), "请先搜索项目");
            return;
        }
        int haoduan = 1;
        if (!mCheckBox.isChecked()) {
            haoduan = 0;
        }

        Call<String> project = ApiAgnet.getApiService().getPhoneNumber(CommonSharePref.getInstance(AppContext.getContext()).getToken()
                , mProjectBean.getId(), haoduan);
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
            handleErrorCode(Integer.valueOf(phoneNumber));
        } else {
            phoneNumber = phoneNumber.replace("\"", "");
            mNumberTv.setText(phoneNumber);
            mHandler.sendEmptyMessageDelayed(MSG_QUERY_VERFITY_CODE, 10000);
            mMsgTv.setText(R.string.wait_msg_desc);
        }
    }

    /**
     * 处理错误码
     *
     * @param errorCode
     */
    private void handleErrorCode(int errorCode) {
        switch (errorCode) {
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
    }

    private void copy2Clipboard(String value) {
        Context context = AppContext.getContext();
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("url", value);
        // 将ClipData内容放到系统剪贴板里。
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
            ToastUtils.showToastForShort(context, context.getString(R.string.string_copied));
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


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_QUERY_VERFITY_CODE:
                    queryMsg();
                    break;
                default:
                    break;
            }


            return false;
        }
    });

    private void queryMsg() {
        if (mGetMsgStartTime < 10) {
            mGetMsgStartTime = System.currentTimeMillis();
        }

        String phoneStr = mNumberTv.getText().toString().trim();
        Call<String> project = ApiAgnet.getApiService().queryMsg(CommonSharePref.getInstance(AppContext.getContext()).getToken()
                , mProjectBean.getId(), phoneStr);
        project.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    return;
                }
                String msgResponse = response.body();
                haneleVerifyCode(msgResponse);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ToastUtils.showToastForShort(AppContext.getContext(), "操作失败，请重试");
                mGetMsgStartTime = 0;
                mGetMsgTimes = 0;
            }
        });


    }

    private void haneleVerifyCode(String response) {
        mMsgTv.setText(response);
        mGetMsgTimes++;
        if (response.length() <= 3) {
            if (System.currentTimeMillis() - mGetMsgStartTime > 90 * 1000) {
                mMsgTv.setText("获取失败！请更换号码重试！");
                return;
            }
            mHandler.sendEmptyMessageDelayed(MSG_QUERY_VERFITY_CODE, 2000);
            mMsgTv.setText("查询验证码 " + mGetMsgTimes + " 次");

        } else {
            MsgBean msgBean = GsonUtil.gson2Bean(response, MsgBean.class);
            if (msgBean == null) {
                return;
            }
            mMsgTv.setText(msgBean.getDuanx());
            mGetMsgStartTime = 0;
            mGetMsgTimes = 0;

            parseMsgCode(msgBean.getDuanx());
        }
    }

    private void parseMsgCode(String msg) {
        String content = "【态链坊】您的验证码是7339。如非本人操作，请忽略本短信";
        String compile = ".*([0-9]{4,8}).*";
        Pattern pattern = Pattern.compile(compile);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            if (matcher.groupCount() >= 1) {
                String result = matcher.group(1);
                mVerifyTv.setText(result);
            }
        }

    }

    private void releasePhone() {
        mHandler.removeMessages(MSG_QUERY_VERFITY_CODE);
        Call<String> project = ApiAgnet.getApiService().releasePhone(CommonSharePref.getInstance(AppContext.getContext()).getToken());
        project.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    return;
                }
                mGetMsgStartTime = 0;
                mGetMsgTimes = 0;
                mNumberTv.setText("");
                mVerifyTv.setText(R.string.wait_for_msg);
                mMsgTv.setText("");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mGetMsgStartTime = 0;
                mGetMsgTimes = 0;
            }
        });
    }

    private void blackList() {
        if (mProjectBean == null) {
            return;
        }
        String phoneStr = mNumberTv.getText().toString().trim();
        if (TextUtils.isEmpty(phoneStr)) {
            return;
        }
        mHandler.removeMessages(MSG_QUERY_VERFITY_CODE);
        Call<String> project = ApiAgnet.getApiService().blacklist(CommonSharePref.getInstance(AppContext.getContext()).getToken()
                , mProjectBean.getId(), phoneStr);
        project.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() != 200) {
                    return;
                }
                mGetMsgStartTime = 0;
                mGetMsgTimes = 0;
                mNumberTv.setText("");
                mVerifyTv.setText(R.string.wait_for_msg);
                mMsgTv.setText("");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mGetMsgStartTime = 0;
                mGetMsgTimes = 0;
            }
        });
    }
}
