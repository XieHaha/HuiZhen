package com.yht.yihuantong.ui.x5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yht.frame.api.DirHelper;
import com.yht.frame.api.FileTransferServer;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.CheckTypeByDetailBean;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.data.type.SuggestionTypeStatus;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.MimeUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.PercentDialog;
import com.yht.frame.widgets.gridview.AutoGridView;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.dialog.DownDialog;
import com.yht.yihuantong.ui.dialog.listener.OnMediaItemClickListener;
import com.yht.yihuantong.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * describe：文件阅读类
 *
 * @author dundun 2019年3月5日17:33:04
 */
public class FileDisplayActivity extends BaseActivity implements OnMediaItemClickListener, SuggestionTypeStatus, AdapterView.OnItemClickListener {
    @BindView(R.id.public_title_bar_title)
    TextView tvTitle;
    @BindView(R.id.documentReaderView)
    FileReaderView fileReaderView;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.layout_report_list)
    LinearLayout layoutReportList;
    @BindView(R.id.auto_grid_view)
    AutoGridView autoGridView;
    @BindView(R.id.tv_advice)
    JustifiedTextView tvAdvice;
    @BindView(R.id.layout_diagnosis_detail)
    RelativeLayout layoutDiagnosisDetail;
    /**
     * 报告列表
     */
    private ArrayList<CheckTypeByDetailBean> reportList;
    /**
     * 报告名
     */
    private ArrayList<String> reportNameList;
    /**
     * 下载进度
     */
    private PercentDialog percentDialog;
    private ArrayList<NormImage> imagePaths = new ArrayList<>();
    /**
     * 文件总大小
     */
    private long fileSize;
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
        initPercentView();
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
        distinguishFile();
    }

    @Override
    public void initListener() {
        autoGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (imagePaths.size() > position) {
            //查看大图
            Intent intent = new Intent(this, ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.INTENT_URLS, imagePaths);
            intent.putExtra(ImagePreviewActivity.INTENT_POSITION, position);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_fade_in, R.anim.keep);
        }
        else {
            permissionHelper.request(new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
        }
    }

    /**
     * 下载进度条
     */
    private void initPercentView() {
        percentDialog = new PercentDialog(this);
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
    private void distinguishFile() {
        CheckTypeByDetailBean bean = reportList.get(curPosition);
        tvTitle.setText(bean.getName());
        String url = bean.getReport();
        if(bean.getSuggestionType() == SUGGESTION_TYPE_DOCTOR)
        {
            webView.setVisibility(View.GONE);
            fileReaderView.setVisibility(View.GONE);
            layoutDiagnosisDetail.setVisibility(View.VISIBLE);
            //医生录入
            imagePaths.clear();
            tvAdvice.setText(bean.getSuggestionText());
            String reports = bean.getReport();
            if (!TextUtils.isEmpty(reports)) {
                autoGridView.setVisibility(View.VISIBLE);
                String[] data = reports.split(";");
                for (String string : data) {
                    NormImage normImage = new NormImage();
                    normImage.setImageUrl(string);
                    imagePaths.add(normImage);
                }
                autoGridView.updateImg(imagePaths, false);
            }
            else {
                autoGridView.setVisibility(View.GONE);
            }

        }else {
            //科室录入
            layoutDiagnosisDetail.setVisibility(View.GONE);
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
                File file = new File(filePath);
                if ( file.exists()) {
                    openFile(filePath);
                }
                else {
                    //本地文件不存在
                    downReportFile(url, fileName);
                }
            }
        }

    }

    private void openFile(String filePath) {
        //每次打开一个文件需要初始化文件阅读器，否则无法阅读下一个文件（一直处于加载中）
        fileReaderView.setNewTbsReaderView();
        webView.setVisibility(View.GONE);
        fileReaderView.setVisibility(View.VISIBLE);
        fileReaderView.show(filePath);
    }

    @Override
    public void onMediaItemClick(int position) {
        if (curPosition == position) { return; }
        curPosition = position;
        distinguishFile();
    }

    private void downReportFile(String url, String fileName) {
        FileTransferServer.getInstance(this)
                          .downloadFile(1, loginBean.getToken(), url, DirHelper.getPathFile(), fileName,
                                        new DownloadListener() {
                                            @Override
                                            public void onDownloadError(int what, Exception exception) {
                                                ToastUtil.toast(FileDisplayActivity.this, "加载失败");
                                            }

                                            @Override
                                            public void onStart(int what, boolean isResume, long rangeSize,
                                                    Headers responseHeaders, long allCount) {
                                                fileSize = allCount;
                                                percentDialog.show();
                                            }

                                            @Override
                                            public void onProgress(int what, int progress, long fileCount, long speed) {
                                                percentDialog.setProgressValue(fileSize, fileCount);
                                            }

                                            @Override
                                            public void onFinish(int what, String filePath) {
                                                percentDialog.dismiss();
                                                openFile(filePath);
                                            }

                                            @Override
                                            public void onCancel(int what) {
                                            }
                                        });
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
