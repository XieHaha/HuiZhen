package com.yht.yihuantong.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.api.DirHelper;
import com.yht.frame.ui.AppManager;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;

import java.io.File;

import static com.yht.yihuantong.version.ConstantsVersionMode.UPDATE_MUST;

/**
 * @author dundun
 * 版本更新
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private ImageView ivCancel;
    private TextView tvTitle, tvContent, tvUpdate, tvProgress, tvExitApp;
    private RelativeLayout layoutProgress;
    private ProgressBar progressBar;
    /**
     * 版本更新说明
     */
    private String data, title;
    /**
     * 更新模式   （强制更新还是选择更新）
     */
    private int upDateMode;
    /**
     * 是否需要下载apk (检查本地是否有已下载好的最新的apk)
     */
    private boolean isDownNewAPK = true;

    public UpdateDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog_update);
        initWidget();
        initUpdateData();
    }

    private void initWidget() {
        layoutProgress = findViewById(R.id.layout_progress);
        progressBar = findViewById(R.id.custom_progressbar);
        ivCancel = findViewById(R.id.iv_cancel);
        tvExitApp = findViewById(R.id.tv_exit_app);
        tvProgress = findViewById(R.id.tv_progress);
        tvTitle = findViewById(R.id.title);
        tvContent = findViewById(R.id.tv_content);
        tvUpdate = findViewById(R.id.tv_update);
        ivCancel.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        tvExitApp.setOnClickListener(this);
    }

    private void initUpdateData() {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        tvContent.setText(data);
        if (upDateMode == UPDATE_MUST) {
            ivCancel.setVisibility(View.INVISIBLE);
        }
        if (isDownNewAPK) {
            tvUpdate.setText(R.string.txt_version_update);
        }
        else {
            tvUpdate.setText(R.string.txt_version_install);
        }
    }

    /**
     * 列表数据
     *
     * @param string
     */
    public UpdateDialog setData(String string) {
        this.data = string;
        return this;
    }

    /**
     * 更新模式    强制更新  选择更新
     *
     */
    public UpdateDialog setUpdateMode(int upDateMode) {
        this.upDateMode = upDateMode;
        return this;
    }

    /**
     * 标题
     */
    public UpdateDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 是否需要下载apk
     *
     * @param isDownNewAPK
     * @return
     */
    public UpdateDialog setIsDownNewAPK(boolean isDownNewAPK) {
        this.isDownNewAPK = isDownNewAPK;
        return this;
    }

    /**
     * 下载的进度值
     *
     * @return
     */
    public void setProgressValue(long total, long current) {
        if (total == current) {
            this.isDownNewAPK = false;
            tvUpdate.setVisibility(View.VISIBLE);
            layoutProgress.setVisibility(View.GONE);
            tvUpdate.setText(R.string.txt_version_install);
        }
        else {
            int progress = (int)(current / (float)total * 100);
            progressBar.setProgress(progress);
            tvProgress.setText(progress + "%");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                dismiss();
                break;
            case R.id.tv_update:
                if (isDownNewAPK && onEnterClickListener != null) {
                    if (upDateMode == UPDATE_MUST) {
                        onEnterClickListener.onEnter(true);
                        layoutProgress.setVisibility(View.VISIBLE);
                        tvUpdate.setVisibility(View.GONE);
                    }
                    else {
                        onEnterClickListener.onEnter(false);
                        dismiss();
                    }
                }
                else {
                    File file = new File(DirHelper.getPathFile(), "ZYC.apk");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(context, ZycApplication.getInstance().getPackageName() +
                                                                  ".fileprovider", file);
                    }
                    else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    context.startActivity(intent);
                }
                break;
            case R.id.tv_exit_app:
                dismiss();
                AppManager.getInstance().exit();
                break;
            default:
                break;
        }
    }

    private OnEnterClickListener onEnterClickListener;

    public interface OnEnterClickListener {
        void onEnter(boolean isMustUpdate);
    }

    public void setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
    }
}
