package com.yht.frame.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.R;

/**
 * @author 顿顿
 * @date 19/5/17 16:18
 * @des
 */
public class LoadViewHelper implements View.OnClickListener {
    /**
     * 暂无消息
     */
    public static final int NONE_MESSAGE = 1;
    /**
     * 暂无居民
     */
    public static final int NONE_PATIENT = NONE_MESSAGE + 1;
    /**
     * 功能升级
     */
    public static final int NONE_UPGRADE = NONE_PATIENT + 1;
    /**
     * 暂无记录
     */
    public static final int NONE_RECORDING = NONE_UPGRADE + 1;
    /**
     * 暂无标签
     */
    public static final int NONE_LABEL = NONE_RECORDING + 1;
    /**
     * 网络错误
     */
    public static final int NONE_NETWORK = NONE_LABEL + 1;
    private Context context;
    private ImageView hintImg;
    private TextView hintText, hintNext;
    private RelativeLayout rootLayout;

    public LoadViewHelper(Activity context) {
        initWidget(context);
        this.context = context.getBaseContext();
    }

    public LoadViewHelper(View view) {
        initWidget(view);
        this.context = view.getContext();
    }

    private void initWidget(Activity context) {
        hintImg = context.findViewById(R.id.public_hint_img);
        hintText = context.findViewById(R.id.public_hint_txt);
        hintNext = context.findViewById(R.id.public_hint_next);
        hintNext.setOnClickListener(this);
        rootLayout = context.findViewById(R.id.public_hint_layout);
        rootLayout.setVisibility(View.GONE);
    }

    private void initWidget(View view) {
        hintImg = view.findViewById(R.id.public_hint_img);
        hintText = view.findViewById(R.id.public_hint_txt);
        hintNext = view.findViewById(R.id.public_hint_next);
        hintNext.setOnClickListener(this);
        rootLayout = view.findViewById(R.id.public_hint_layout);
        rootLayout.setVisibility(View.GONE);
    }

    public void load(int type) {
        rootLayout.setVisibility(View.VISIBLE);
        switch (type) {
            case NONE_MESSAGE:
                hintImg.setImageResource(R.mipmap.pic_none_message);
                hintText.setText(R.string.txt_none_message);
                hintNext.setVisibility(View.GONE);
                rootLayout.setBackground(null);
                break;
            case NONE_PATIENT:
                hintImg.setImageResource(R.mipmap.pic_none_paient);
                hintText.setText(R.string.txt_none_patient);
                hintNext.setVisibility(View.GONE);
                rootLayout.setBackground(null);
                break;
            case NONE_UPGRADE:
                hintImg.setImageResource(R.mipmap.pic_upgrade);
                hintText.setText(R.string.txt_none_upgrade);
                hintNext.setVisibility(View.GONE);
                rootLayout.setBackgroundColor(Color.WHITE);
                break;
            case NONE_RECORDING:
                hintImg.setImageResource(R.mipmap.pic_none_record);
                hintText.setText(R.string.txt_none_recording);
                hintNext.setVisibility(View.GONE);
                rootLayout.setBackgroundColor(Color.WHITE);
                break;
            case NONE_LABEL:
                hintImg.setImageResource(R.mipmap.pic_none_label);
                hintText.setText(R.string.txt_no_label);
                hintNext.setVisibility(View.GONE);
                rootLayout.setBackgroundColor(Color.WHITE);
                break;
            case NONE_NETWORK:
            default:
                hintImg.setImageResource(R.mipmap.pic_network_error);
                hintText.setText(R.string.txt_none_network);
                hintNext.setVisibility(View.VISIBLE);
                hintNext.setText(R.string.txt_loading_again);
                rootLayout.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (onNextClickListener != null) {
            onNextClickListener.onNextClick();
        }
    }

    public interface OnNextClickListener {
        /**
         * 下一步
         */
        void onNextClick();
    }

    private OnNextClickListener onNextClickListener;

    public void setOnNextClickListener(OnNextClickListener onNextClickListener) {
        this.onNextClickListener = onNextClickListener;
    }
}
