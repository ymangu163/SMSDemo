package com.sms.code.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sms.code.R;
import com.sms.code.activity.AboutUsActivity;
import com.sms.code.activity.FAQActivity;
import com.sms.code.activity.LoginActivity;
import com.sms.code.activity.UserInfoActivity;
import com.sms.code.app.AppContext;
import com.sms.code.utils.CommonSharePref;
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

        rootView.findViewById(R.id.my_help_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_about_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_mine_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_share_tv).setOnClickListener(this);
        rootView.findViewById(R.id.my_msg_history).setOnClickListener(this);
        rootView.findViewById(R.id.my_logout_tv).setOnClickListener(this);
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
        }

    }


}
