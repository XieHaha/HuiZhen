package com.yht.yihuantong.ui.x5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import butterknife.BindView;

/**
 * describe：文件阅读类
 *
 * @author dundun 2019年3月5日17:33:04
 */
public class FileDisplayActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView tvTitle;
    @BindView(R.id.documentReaderView)
    FileReaderView fileReaderView;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_file_display;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            tvTitle.setText(getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING));
            String url = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            fileReaderView.show(url);
        }
    }

    /**
     * 显示
     *
     * @param context
     * @param url
     * @param fileName
     */
    public static void show(Context context, String url, String fileName) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, url);
        intent.putExtra(CommonData.KEY_PUBLIC_STRING, fileName);
        context.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fileReaderView != null) {
            fileReaderView.stop();
        }
    }
}
