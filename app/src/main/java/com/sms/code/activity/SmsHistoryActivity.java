package com.sms.code.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.adapter.SmsHistoryAdapter;
import com.sms.code.bean.MsgBean;
import com.sms.code.engine.DBManger;

import java.util.List;


/**
 * File description
 *
 * @author gao
 * @date 2018/3/6
 */

public class SmsHistoryActivity extends BaseActivity implements View.OnClickListener {

    private SmsHistoryAdapter mAdapter;
    private EditText mEditText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sms_history;
    }

    @Override
    public void initViews() {
        mEditText = findViewById(R.id.history_search_et);
        TextView searchTv = findViewById(R.id.history_search_tv);
        searchTv.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.history_recycler);
        mAdapter = new SmsHistoryAdapter<MsgBean>(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    public void initData() {
        DBManger.getInstance().handlesAsync(new Runnable() {
            @Override
            public void run() {
                List<MsgBean> msgBeanList = DBManger.getInstance().getSmsHistory();
                mAdapter.clearList();
                // 将本次查询的数据添加到bankCards中
                mAdapter.addList(msgBeanList);
                // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        String keys = mEditText.getText().toString();
        if (TextUtils.isEmpty(keys)) {
            return;
        }
        List<MsgBean> msgBeanList = DBManger.getInstance().queryKey(keys);
        mAdapter.clearList();
        // 将本次查询的数据添加到bankCards中
        mAdapter.addList(msgBeanList);
        mAdapter.notifyDataSetChanged();
    }
}
