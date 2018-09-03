package com.sms.code.activity;

import android.widget.ExpandableListView;

import com.sms.code.R;
import com.sms.code.adapter.FaqAdapter;
import com.sms.code.bean.FaqEntry;
import com.sms.code.utils.AppUtil;
import com.sms.code.utils.GsonUtil;
import com.sms.code.utils.StatConstant;
import com.sms.code.utils.StatUtil;

import java.util.List;


/**
 * File description
 *
 * @author gao
 * @date 2018/3/5
 */

public class FAQActivity extends BaseActivity {
    private ExpandableListView mFaqListView;
    private FaqAdapter mFaqAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public void initViews() {
        mFaqListView = findViewById(R.id.help_expan_list);

        mFaqAdapter = new FaqAdapter(this);
        mFaqListView.setAdapter(mFaqAdapter);
    }

    @Override
    public void initData() {
        String faqJson = AppUtil.getJsonFromAssets(FAQActivity.this,"faq.json");
        List<FaqEntry> faqList = GsonUtil.gson2List(faqJson, FaqEntry.class);
        mFaqAdapter.setFaqList(faqList);
        StatUtil.onEvent(StatConstant.SMS_FAQ);
    }
}
