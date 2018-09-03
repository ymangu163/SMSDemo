package com.sms.code.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.app.AppContext;
import com.sms.code.bean.Upgrade;
import com.sms.code.utils.AppUtil;
import com.sms.code.utils.CommonSharePref;
import com.sms.code.utils.StatConstant;
import com.sms.code.utils.StatUtil;
import com.sms.code.utils.ToastUtils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


/**
 * File description
 *
 * @author gao
 * @date 2018/3/6
 */

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initViews() {
        TextView versionTv = findViewById(R.id.about_us_version_tv);
        String version = "V " + AppUtil.getVersionName(this);
        versionTv.setText(version);

        findViewById(R.id.textViewVersion).setOnClickListener(this);
        findViewById(R.id.privacy_tv).setOnClickListener(this);
    }

    @Override
    public void initData() {
        StatUtil.onEvent(StatConstant.SMS_ABOUT);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.privacy_tv) {
            startActivity(new Intent(AboutUsActivity.this, PrivacyPolicyActivity.class));
        } else if (vId == R.id.textViewVersion) {
            checkAppUpdate();
        }

    }

    private void checkAppUpdate() {
        BmobQuery<Upgrade> query = new BmobQuery<Upgrade>();
        query.getObject("cFho222P", new QueryListener<Upgrade>() {

            @Override
            public void done(Upgrade bean, BmobException e) {
                if (e != null) {
                    ToastUtils.showToastForShort(AppContext.getContext(), "已经是最新版本.");
                    return;
                }
                int currVerionCode = AppUtil.getVersionCode(AppContext.getContext());
                if (bean.getVersioncode() > currVerionCode) {
                    Uri uri = Uri.parse(bean.getDownload());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                CommonSharePref.getInstance(AppContext.getContext()).setUpgradeTime(System.currentTimeMillis());
            }
        });
    }

}
