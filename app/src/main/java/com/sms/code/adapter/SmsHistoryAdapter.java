package com.sms.code.adapter;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.bean.MsgBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * File description
 *
 * @author gao
 * @date 2018/12/24
 */
public class SmsHistoryAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int TYPE_CONTENT = 0;//正常内容
    private final static int TYPE_FOOTER = 1;//下拉刷新
    public final static int REQUEST_LOADMORE = 2;//加载更多
    private List<T> mListData = new ArrayList<>();
    private Context mContext;
    public boolean mIsLoadMore;
    private DateFormat mDateFormat = new SimpleDateFormat("HH:mm yyyy/MM/dd", Locale.CHINESE);
    private Date mDate = new Date();

    public SmsHistoryAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_main_footer, parent, false);
            return new FootViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_sms_item, parent, false);
            return new SmsViewHoler(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SmsViewHoler) {
            SmsViewHoler viewHolder = (SmsViewHoler) holder;
            T tBean = mListData.get(position);
            if (tBean instanceof MsgBean) {
                MsgBean smsBean = (MsgBean) tBean;
                viewHolder.setBodyTv(smsBean.getDuanx());
                viewHolder.setPhoneTv(mContext.getString(R.string.history_phone, smsBean.getPhoneNumber()));
                mDate.setTime(smsBean.getTime());
                viewHolder.setTimeTv(mDateFormat.format(mDate));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && mIsLoadMore) {
            return TYPE_FOOTER;
        }
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        if (mIsLoadMore) {
            return mListData.size() + 1;
        }
        return mListData.size();
    }

    public void setListData(List<T> data) {
        mListData.clear();
        mListData.addAll(data);
    }

    public void addItemData(T item) {
        mListData.add(item);
    }

    public void addList(List<T> data) {
        mListData.addAll(data);
    }

    public void clearList() {
        mListData.clear();
    }

    public void startLoad() {
        mIsLoadMore = true;
        notifyDataSetChanged();
    }

    public void finishLoad() {
        mIsLoadMore = false;
        notifyDataSetChanged();
    }

    private static class SmsViewHoler extends RecyclerView.ViewHolder {

        private final TextView mPhoneTv;
        private final TextView mTimeTv;
        private final TextView mBodyTv;

        public SmsViewHoler(View itemView) {
            super(itemView);
            mPhoneTv = itemView.findViewById(R.id.sms_phone_tv);
            mTimeTv = itemView.findViewById(R.id.sms_time_tv);
            mBodyTv = itemView.findViewById(R.id.sms_body_tv);
        }

        public void setPhoneTv(String phoneTv) {
            mPhoneTv.setText(phoneTv);
        }

        public void setTimeTv(String time) {
            mTimeTv.setText(time);
        }

        public void setBodyTv(String body) {
            mBodyTv.setText(body);
        }

    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        ContentLoadingProgressBar contentLoadingProgressBar;

        public FootViewHolder(View itemView) {
            super(itemView);
            contentLoadingProgressBar = itemView.findViewById(R.id.pb_progress);
        }
    }
}
