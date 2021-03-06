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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.sms.code.BuildConfig;
import com.sms.code.R;
import com.sms.code.activity.LoginActivity;
import com.sms.code.activity.SearchActivity;
import com.sms.code.app.AppContext;
import com.sms.code.bean.MsgBean;
import com.sms.code.bean.ProjectBean;
import com.sms.code.dialog.RechargeDialog;
import com.sms.code.engine.ApiAgnet;
import com.sms.code.engine.DBManger;
import com.sms.code.utils.AppUtil;
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
    private final int MSG_QUERY_VERFITY_CODE = 0x11;

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
        mGetVerifyTv.setOnClickListener(this);
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
            if (mPhoneEdit.getVisibility() == View.VISIBLE) {
                hideSpecialPhone();
            } else {
                getPhoneNumber();
            }
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
            if (BuildConfig.DEBUG) {
                getMd5();
            } else {
                blackList();
            }
        } else if (vId == R.id.code_special_tv) {
            showSpecialPhone();
        } else if (vId == R.id.code_get_verify_tv) {
            queryMsg();
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
        if (mProjectBean == null) {
            return;
        }

        if (mGetMsgStartTime < 10) {
            mGetMsgStartTime = System.currentTimeMillis();
        }

        String phoneStr = getPhoneStr();
        if (TextUtils.isEmpty(phoneStr)) {
            ToastUtils.showToastForShort(AppContext.getContext(), "手机号不能为空");
            return;
        }
        Call<String> project = ApiAgnet.getApiService().queryMsg(CommonSharePref.getInstance(AppContext.getContext()).getToken()
                , mProjectBean.getId(), phoneStr, 50801);
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

    private String getPhoneStr() {
        String phoneStr = mNumberTv.getText().toString().trim();
        if (mPhoneEdit.getVisibility() == View.VISIBLE) {
            phoneStr = mPhoneEdit.getText().toString().trim();
        }
        return phoneStr;
    }

    private void haneleVerifyCode(String response) {
        mMsgTv.setText(response);
        mGetMsgTimes++;
        if (response.length() <= 3) {
            int errorCode = 0;
            try {
                errorCode = Integer.valueOf(response);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (System.currentTimeMillis() - mGetMsgStartTime > 90 * 1000) {
                mMsgTv.setText("获取失败！请更换号码重试！");
                return;
            } else if (errorCode == 18) {
                mMsgTv.setText("传入手机号错误或以释放 ");
                return;
            }
            mHandler.sendEmptyMessageDelayed(MSG_QUERY_VERFITY_CODE, 5000);
            mMsgTv.setText("查询验证码 " + mGetMsgTimes + " 次");

        } else {
            MsgBean msgBean = GsonUtil.gson2Bean(response, MsgBean.class);
            if (msgBean == null) {
                return;
            }
            msgBean.setTime(System.currentTimeMillis());
            msgBean.setPhoneNumber(getPhoneStr());
            mMsgTv.setText(msgBean.getDuanx());
            mGetMsgStartTime = 0;
            mGetMsgTimes = 0;

            String yzm = getyzm(msgBean.getDuanx());
            mVerifyTv.setText(yzm);
            DBManger.getInstance().saveSmsTb(msgBean);
        }
    }

    private String getyzm(String msg) {
        Pattern p = Pattern.compile("(?<![0-9])([0-9]{4,6})(?![0-9])");

        Matcher m = p.matcher(msg);
        if (m.find()) {
            return m.group(0);
        }
        return "";
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
                mMsgTv.setText(R.string.wait_for_msg);
                mVerifyTv.setText("");
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
            ToastUtils.showToastForShort(AppContext.getContext(), "请先搜索项目");
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
                mMsgTv.setText(R.string.wait_for_msg);
                mVerifyTv.setText("");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mGetMsgStartTime = 0;
                mGetMsgTimes = 0;
            }
        });
    }

    private void showSpecialPhone() {
        mPhoneTitleTv.setVisibility(View.INVISIBLE);
        mNumberTv.setVisibility(View.INVISIBLE);
        mPhoneCopyTv.setVisibility(View.INVISIBLE);
        mPhoneEdit.setVisibility(View.VISIBLE);
        mGetVerifyTv.setVisibility(View.VISIBLE);
        mPhoneEdit.setText("");
    }

    private void hideSpecialPhone() {
        mPhoneTitleTv.setVisibility(View.VISIBLE);
        mNumberTv.setVisibility(View.VISIBLE);
        mPhoneCopyTv.setVisibility(View.VISIBLE);
        mPhoneEdit.setVisibility(View.INVISIBLE);
        mGetVerifyTv.setVisibility(View.INVISIBLE);
    }

    private void getMd5() {
        String code = mPhoneEdit.getText().toString().trim();
        String result = AppUtil.getMd5(code + 36);
        if (TextUtils.isEmpty(result)) {
            return;
        }
        String subResult = result.substring(10, 16);
        mPhoneEdit.setText(subResult);
    }
}
