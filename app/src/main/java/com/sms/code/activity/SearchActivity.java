package com.sms.code.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.app.AppContext;
import com.sms.code.bean.ProjectBean;
import com.sms.code.engine.ApiAgnet;
import com.sms.code.utils.CommonSharePref;
import com.sms.code.utils.DisplayUtil;
import com.sms.code.utils.ToastUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * File description
 *
 * @author gao
 * @date 2018/8/28
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private EditText mSearchEt;
    private TagFlowLayout mFlowLayout;
    private List<String> mHistoryList;
    private List<TextView> mReultTvList;
    private TextView mResultTitle;
    private List<ProjectBean> mProjectBeans;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initViews() {
        mSearchEt = findViewById(R.id.search_et);
        DisplayUtil.openKeybord(mSearchEt);

        mFlowLayout = findViewById(R.id.search_flow_layout);
        mResultTitle = findViewById(R.id.search_result_title);
        TextView resultTv1 = findViewById(R.id.search_result_1);
        TextView resultTv2 = findViewById(R.id.search_result_2);
        TextView resultTv3 = findViewById(R.id.search_result_3);
        mReultTvList = new ArrayList<>();
        mReultTvList.add(resultTv1);
        mReultTvList.add(resultTv2);
        mReultTvList.add(resultTv3);

        resultTv1.setOnClickListener(this);
        resultTv2.setOnClickListener(this);
        resultTv3.setOnClickListener(this);

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                    return false;
                }
                String name = mSearchEt.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showToastForShort(AppContext.getContext(), "请输入要搜索的项目");
                    return false;
                }
                reset();
                queryProject(name);
                return true;
            }
        });

    }

    @Override
    public void initData() {
        mHistoryList = new ArrayList<>();
        mHistoryList.add("火牛");
        mHistoryList.add("哈哈");
        mHistoryList.add("芝麻");
        mHistoryList.add("火牛");
        mFlowLayout.setAdapter(new TagAdapter<String>(mHistoryList) {
            @Override
            public View getView(FlowLayout parent, int position, String item) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.layout_flow_item,
                        mFlowLayout, false);
                tv.setText(item);
                return tv;
            }
        });
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ToastUtils.showToastForShort(AppContext.getContext(), mHistoryList.get(position));
                return true;
            }
        });
    }

    private void queryProject(String name) {
        Call<List<ProjectBean>> project = ApiAgnet.getApiService().queryProject(CommonSharePref.getInstance(this).getToken(), name);
        project.enqueue(new Callback<List<ProjectBean>>() {
            @Override
            public void onResponse(Call<List<ProjectBean>> call, Response<List<ProjectBean>> response) {
                if (response.code() != 200) {
                    return;
                }
                mProjectBeans = response.body();
                showResultBtn(mProjectBeans);
            }

            @Override
            public void onFailure(Call<List<ProjectBean>> call, Throwable t) {
                ToastUtils.showToastForShort(AppContext.getContext(), "操作失败，请重试");
            }
        });

    }

    private void showResultBtn(List<ProjectBean> list) {
        int sum = list.size();
        if (sum > 3) {
            sum = 3;
        }
        mResultTitle.setVisibility(View.VISIBLE);
        mResultTitle.setText(getString(R.string.search_result, sum));
        for (int index = 0; index < sum; index++) {
            mReultTvList.get(index).setVisibility(View.VISIBLE);
            mReultTvList.get(index).setText(list.get(index).getName());
        }
    }

    private void reset() {
        mResultTitle.setVisibility(View.GONE);
        for (int index = 0; index < 3; index++) {
            mReultTvList.get(index).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DisplayUtil.closeKeybord(mSearchEt);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        ProjectBean projectBean = null;
        if (vId == R.id.search_result_1) {
            projectBean = mProjectBeans.get(0);
        } else if (vId == R.id.search_result_2) {
            projectBean = mProjectBeans.get(1);
        } else if (vId == R.id.search_result_3) {
            projectBean = mProjectBeans.get(2);
        }
        Intent intent = new Intent();
        intent.putExtra("project", projectBean);
        setResult(RESULT_OK, intent);
        finish();
    }
}
