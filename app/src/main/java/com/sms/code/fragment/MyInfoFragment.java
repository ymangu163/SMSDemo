package com.sms.code.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sms.code.R;


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
        return rootView;
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();

    }
}
