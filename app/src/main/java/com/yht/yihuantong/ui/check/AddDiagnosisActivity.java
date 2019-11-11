package com.yht.yihuantong.ui.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.ScalingUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.EditTextLayout;
import com.yht.frame.widgets.gridview.AutoGridView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.utils.MatisseUtils;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yht.frame.ui.BaseFragment.RC_PICK_IMG;

/**
 * @author 顿顿
 * @date 19/6/5 17:19
 * @description 填写诊断意见
 */
public class AddDiagnosisActivity extends BaseActivity
        implements AdapterView.OnItemClickListener, AutoGridView.OnDeleteClickListener {
    @BindView(R.id.edit_layout)
    EditTextLayout editLayout;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.public_title_bar_back)
    ImageView publicTitleBarBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_calc_num)
    TextView tvCalcNum;
    @BindView(R.id.auto_grid_view)
    AutoGridView autoGridView;
    /**
     * 诊断意见
     */
    private String diagnosisAdvice;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 检查项id
     */
    private int checkTranId;
    /**
     * 已上传图片
     */
    private ArrayList<NormImage> imagePaths = new ArrayList<>();
    /**
     * 图片临时数据
     */
    private List<String> paths;
    private ArrayList<File> files = new ArrayList<>();
    private int currentUploadImgIndex = -1;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_add_diagnosis;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            String name = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
            checkTranId = getIntent().getIntExtra(CommonData.KEY_CHECK_TYPE_ID, 0);
            tvName.setText(name);
        }
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarBack.setOnClickListener(this);
        autoGridView.setShowNum(true, 10);
        autoGridView.updateImg(imagePaths, true);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        publicTitleBarMore.setText(R.string.txt_save);
        editLayout.getEditText().setHint(R.string.txt_add_diagnosis_hint);
        tvCalcNum.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        autoGridView.setOnItemClickListener(this);
        autoGridView.setOnDeleteClickListener(this);
        editLayout.getEditText().addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                diagnosisAdvice = s.toString();
                tvCalcNum.setText(String.format(getString(R.string.txt_calc_num), s.toString().trim().length()));
                initNextButton();
            }
        });
    }

    /**
     * 保存回执报告
     */
    private void doctorReport() {
        RequestUtils.doctorReport(this, loginBean.getToken(), checkTranId, orderNo, diagnosisAdvice, files, this);
    }

    /**
     * 上传图片
     */
    private void uploadImage(File file) {
        ScalingUtils.resizePic(this, file.getAbsolutePath());
        RequestUtils.uploadImgWaterMark(this, loginBean.getToken(), file, false, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.public_title_bar_back) {
            onFinish();
        }
    }

    @OnClick({ R.id.public_title_bar_more })
    public void onViewClicked() {
        if (publicTitleBarMore.isSelected()) { save(); }
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
    public void onDeleteClick(int position) {
        files.remove(position);
        imagePaths.remove(position);
        autoGridView.updateImg(imagePaths, true);
        initNextButton();
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        MatisseUtils.open(this, true, BaseData.BASE_IMAGE_SIZE_MAX - imagePaths.size());
    }

    private void initNextButton() {
        if (!TextUtils.isEmpty(diagnosisAdvice) || files.size() > 0) {
            publicTitleBarMore.setSelected(true);
        }
        else {
            publicTitleBarMore.setSelected(false);
        }
    }

    /**
     * 保存
     */
    private void save() {
        hideSoftInputFromWindow();
        doctorReport();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case UPLOAD_FILE:
                String url = (String)response.getData();
                NormImage normImage = new NormImage();
                normImage.setImageUrl(url);
                imagePaths.add(normImage);
                autoGridView.updateImg(imagePaths, true);
                if (paths.size() - 1 > currentUploadImgIndex) {
                    currentUploadImgIndex++;
                    uploadImage(new File(paths.get(currentUploadImgIndex)));
                }
                else {
                    closeLoadingView();
                }
                initNextButton();
                break;
            case DOCTOR_REPORT:
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == RC_PICK_IMG) {
            paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0) {
                for (String path : paths) {
                    files.add(new File(path));
                    NormImage normImage = new NormImage();
                    normImage.setImageUrl(path);
                    imagePaths.add(normImage);
                }
                autoGridView.updateImg(imagePaths, true);
                initNextButton();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0])) {
                openPhoto();
            }
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }

    private void onFinish() {
        if (publicTitleBarMore.isSelected()) {
            new HintDialog(this).setContentString(R.string.txt_save_edit)
                                .setCancleBtnTxt(R.string.txt_not_save)
                                .setOnCancelClickListener(this::finish)
                                .setEnterBtnTxt(R.string.txt_save)
                                .setEnterSelect(true)
                                .setDeleteVisible(View.VISIBLE)
                                .setOnEnterClickListener(this::save)
                                .show();
            return;
        }
        hideSoftInputFromWindow();
        finish();
    }
}
