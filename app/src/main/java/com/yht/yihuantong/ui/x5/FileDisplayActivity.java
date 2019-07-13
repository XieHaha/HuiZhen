package com.yht.yihuantong.ui.x5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.yht.frame.api.DirHelper;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.base.CheckTypeByDetailBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.MimeUtils;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.dialog.DownDialog;
import com.yht.yihuantong.ui.dialog.listener.OnMediaItemClickListener;
import com.yht.yihuantong.utils.FileUrlUtil;
import com.yht.yihuantong.utils.FileUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * describe：文件阅读类
 *
 * @author dundun 2019年3月5日17:33:04
 */
public class FileDisplayActivity extends BaseActivity implements OnMediaItemClickListener {
    @BindView(R.id.public_title_bar_title)
    TextView tvTitle;
    @BindView(R.id.documentReaderView)
    FileReaderView fileReaderView;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.layout_report_list)
    LinearLayout layoutReportList;
    /**
     * 报告列表
     */
    private ArrayList<CheckTypeByDetailBean> reportList;
    /**
     * 报告名
     */
    private ArrayList<String> reportNameList;
    /**
     * 当前选中的报告index
     */
    private int curPosition = -1;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_file_display;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initWebViewSetting();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            reportList = (ArrayList<CheckTypeByDetailBean>)getIntent().getSerializableExtra(
                    CommonData.KEY_CHECK_REPORT_LIST);
            curPosition = getIntent().getIntExtra(CommonData.KEY_PUBLIC, -1);
        }
        initReportListDialog();
        openFile();
    }

    private void initWebViewSetting() {
        //指定的垂直滚动条有叠加样式
        webView.setVerticalScrollbarOverlay(true);
        WebSettings settings = webView.getSettings();
        //设定支持viewport
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        //设定支持缩放
        settings.setSupportZoom(true);
    }

    /**
     * 显示报告菜单
     */
    private void initReportListDialog() {
        reportNameList = new ArrayList<>();
        for (CheckTypeByDetailBean bean : reportList) {
            reportNameList.add(bean.getName());
        }
        if (reportNameList.size() > 1) {
            layoutReportList.setVisibility(View.VISIBLE);
        }
        else {
            layoutReportList.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_report)
    public void onViewClicked() {
        new DownDialog(this).setData(reportNameList)
                            .setCurPosition(curPosition)
                            .setOnMediaItemClickListener(this)
                            .show();
    }

    /**
     * 判断文件格式，选择不同方式打开
     */
    private void openFile() {
        CheckTypeByDetailBean bean = reportList.get(curPosition);
        tvTitle.setText(bean.getName());
        String url = FileUrlUtil.append(bean.getReport());
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        String filePath = DirHelper.getPathFile() + "/" + fileName;
        //获取文件格式
        String type = MimeUtils.getMime(FileUtils.getFileExtNoPoint(filePath));
        if (BaseData.BASE_IMAGE_TYPE.contains(type)) {
            //如果为图片格式 直接加载网络url
            webView.setVisibility(View.VISIBLE);
            fileReaderView.setVisibility(View.GONE);
            webView.loadUrl(url);
        }
        else {
            //每次打开一个文件需要初始化文件阅读器，否则无法阅读下一个文件（一直处于加载中）
            fileReaderView.setNewTbsReaderView();
            webView.setVisibility(View.GONE);
            fileReaderView.setVisibility(View.VISIBLE);
            fileReaderView.show(filePath);
        }
    }

    @Override
    public void onMediaItemClick(int position) {
        if (curPosition == position) { return; }
        curPosition = position;
        openFile();
    }

    /**
     * 显示
     *
     * @param context
     * @param list    文件列表
     */
    public static void show(Context context, ArrayList<CheckTypeByDetailBean> list, int position) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        intent.putExtra(CommonData.KEY_CHECK_REPORT_LIST, list);
        intent.putExtra(CommonData.KEY_PUBLIC, position);
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
