package com.sms.code.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.activity.AboutUsActivity;
import com.sms.code.activity.FAQActivity;
import com.sms.code.activity.LoginActivity;
import com.sms.code.activity.UserInfoActivity;
import com.sms.code.app.AppContext;
import com.sms.code.utils.AppUtil;
import com.sms.code.utils.CommonSharePref;
import com.sms.code.utils.StatConstant;
import com.sms.code.utils.StatUtil;
import com.sms.code.utils.ToastUtils;


/**
 * File description
 *
 * @author gao
 * @date 2018/3/5
 */

public class MyInfoFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        TextView redPacketTv = rootView.findViewById(R.id.my_red_packet);
        rootView.findViewById(R.id.my_help_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_about_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_mine_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_share_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_msg_history).setOnClickListener(this);
        rootView.findViewById(R.id.my_logout_tv).setOnClickListener(this);
        redPacketTv.setOnClickListener(this);

        if (AppUtil.isAppInstalled(AppContext.getContext(), "com.eg.android.AlipayGphone")) {
            redPacketTv.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.my_mine_tv) {
            String token = CommonSharePref.getInstance(AppContext.getContext()).getToken();
            if (TextUtils.isEmpty(token)) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return;
            }
            startActivity(new Intent(getActivity(), UserInfoActivity.class));
        } else if (vId == R.id.my_help_tv) {
            startActivity(new Intent(getActivity(), FAQActivity.class));
        } else if (vId == R.id.my_msg_history) {
            ToastUtils.showToastForShort(getContext(), "敬请期待");
        } else if (vId == R.id.my_logout_tv) {
            CommonSharePref.getInstance(getContext()).setToken("");
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else if (vId == R.id.my_about_tv) {
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
        } else if (vId == R.id.my_red_packet) {
            handleRedPacket();
        }

    }

    private void handleRedPacket() {

        String INTENT_URL_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
                "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s" +
                "%3Dweb-other&_t=1472443966571#Intent;" +
                "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";

        startIntentUrl(getContext(), INTENT_URL_FORMAT.replace("{urlCode}", "c1x05828dbb5uwtrasbia19"));
    }

    public void startIntentUrl(Context context, String intentFullUrl) {
        StatUtil.onEvent(StatConstant.SMS_RED_PACKET);
        try {
            Intent intent = Intent.parseUri(
                    intentFullUrl,
                    Intent.URI_INTENT_SCHEME);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
