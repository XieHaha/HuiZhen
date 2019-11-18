package com.yht.yihuantong.ui.check;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.CheckTypeByDetailBean;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.gridview.AutoGridView;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.dialog.DownDialog;
import com.yht.yihuantong.ui.dialog.listener.OnMediaItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/5 17:19
 * @description 诊断意见详情
 */
public class DiagnosisDetailActivity extends BaseActivity
        implements AdapterView.OnItemClickListener, OnMediaItemClickListener {
    @BindView(R.id.auto_grid_view)
    AutoGridView autoGridView;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_advice)
    JustifiedTextView tvAdvice;
    @BindView(R.id.layout_report_list)
    LinearLayout layoutReportList;
    /**
     * 当前选中的报告index
     */
    private int curPosition = -1;
    /**
     * 数据源
     */
    private ArrayList<CheckTypeByDetailBean> checkTypeByDetailBeans;
    /**
     * 报告名
     */
    private ArrayList<String> reportNameList;
    private ArrayList<NormImage> imagePaths = new ArrayList<>();

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_diagnosis_detail;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            checkTypeByDetailBeans = (ArrayList<CheckTypeByDetailBean>)getIntent().getSerializableExtra(
                    CommonData.KEY_CHECK_REPORT_LIST);
            curPosition = getIntent().getIntExtra(CommonData.KEY_PUBLIC, -1);
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        bindData();
        initReportListDialog();
    }

    @Override
    public void initListener() {
        autoGridView.setOnItemClickListener(this);
    }

    /**
     * 显示报告菜单
     */
    private void initReportListDialog() {
        reportNameList = new ArrayList<>();
        for (CheckTypeByDetailBean bean : checkTypeByDetailBeans) {
            reportNameList.add(bean.getName());
        }
        if (reportNameList.size() > 1) {
            layoutReportList.setVisibility(View.VISIBLE);
        }
        else {
            layoutReportList.setVisibility(View.GONE);
        }
    }

    /**
     * 数据绑定
     */
    private void bindData() {
        imagePaths.clear();
        CheckTypeByDetailBean bean = checkTypeByDetailBeans.get(curPosition);
        tvAdvice.setText(bean.getSuggestionText());
        publicTitleBarTitle.setText(bean.getName());
        String reports = bean.getReport();
        if (!TextUtils.isEmpty(reports)) {
            autoGridView.setVisibility(View.VISIBLE);
            String[] data = reports.split(";");
            for (String url : data) {
                NormImage normImage = new NormImage();
                normImage.setImageUrl(url);
                imagePaths.add(normImage);
            }
            autoGridView.updateImg(imagePaths, false);
        }
        else {
            autoGridView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_report)
    public void onViewClicked() {
        new DownDialog(this).setData(reportNameList)
                            .setCurPosition(curPosition)
                            .setOnMediaItemClickListener(this)
                            .show();
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

    @Override
    public void onMediaItemClick(int position) {
        if (curPosition == position) { return; }
        curPosition = position;
        bindData();
    }
}
