package com.sms.code.views;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sms.code.R;
import com.sms.code.utils.ToastUtils;

/**
 * File description
 *
 * @author gao
 * @date 2018/6/24
 */

public class ActionView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private TextView mTitleTv;
    private ImageView mBackIv;
    private ImageView mPacketIv;
    private String mTilte = "";

    public ActionView(Context context) {
        this(context, null);
    }

    public ActionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        layoutInflater.inflate(R.layout.layout_action_view, this, true);
        setBackgroundColor(getResources().getColor(R.color.white));

        mTitleTv = findViewById(R.id.action_title_tv);
        mBackIv = findViewById(R.id.action_back_iv);
        mPacketIv = findViewById(R.id.action_right_iv);
        mBackIv.setOnClickListener(this);
        mPacketIv.setOnClickListener(this);

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ActionView);
        mTilte = typedArray.getString(R.styleable.ActionView_title);
        int iconId = typedArray.getResourceId(R.styleable.ActionView_icon, R.drawable.ic_red_packet);
        boolean iconVisible = typedArray.getBoolean(R.styleable.ActionView_icon_visible, true);
        boolean backVisible = typedArray.getBoolean(R.styleable.ActionView_back_visible, true);
        typedArray.recycle();
        mTitleTv.setText(mTilte);
        mPacketIv.setImageResource(iconId);
        if (!iconVisible) {
            mPacketIv.setVisibility(View.GONE);
        }
        if (!backVisible) {
            mBackIv.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.action_back_iv) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        } else if (vId == R.id.action_right_iv) {
            handleRedPacket();
        }

    }

    private void handleRedPacket() {

        String INTENT_URL_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
                "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s" +
                "%3Dweb-other&_t=1472443966571#Intent;" +
                "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";

        startIntentUrl(mContext, INTENT_URL_FORMAT.replace("{urlCode}", "c1x05828dbb5uwtrasbia19"));
    }

    /**
     * 打开 Intent Scheme Url
     *
     * @param context       Parent Activity
     * @param intentFullUrl Intent 跳转地址
     * @return 是否成功调用
     */
    public void startIntentUrl(Context context, String intentFullUrl) {
        try {
            Intent intent = Intent.parseUri(
                    intentFullUrl,
                    Intent.URI_INTENT_SCHEME);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copy2Clipboard(String value) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("url", value);
        // 将ClipData内容放到系统剪贴板里。
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
            ToastUtils.showToastForShort(mContext, mContext.getString(R.string.string_copied));
        }
    }


}
