package com.yht.frame.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.R;
import com.yht.frame.widgets.view.CircularProgress;

/**
 * @author dundun
 */
public class PercentDialog extends Dialog {
    private RelativeLayout layoutPercent;
    private CircularProgress circularProgress;
    private TextView tvPercent;

    public PercentDialog(Context context) {
        super(context, R.style.percent_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog_percent);
        init();
    }

    private void init() {
        layoutPercent = findViewById(R.id.layout_circular_progress);
        circularProgress = findViewById(R.id.circular_progress);
        tvPercent = findViewById(R.id.tv_percent);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    /**
     * 下载的进度值
     *
     * @return
     */
    public void setProgressValue(long total, long current) {
        if (total == current) {
        }
        else {
            tvPercent.setText((int)(current / (float)total * 100) + "%");
        }
    }
}
