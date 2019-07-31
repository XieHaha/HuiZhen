package com.yht.yihuantong.ui.reservation.transfer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.api.DirHelper;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.DoctorInfoBean;
import com.yht.frame.data.bean.ReserveTransferBean;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScalingUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.view.ExpandableLayout;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.transfer.SelectReceivingDoctorActivity;
import com.yht.yihuantong.ui.transfer.listener.OnTransferListener;
import com.yht.yihuantong.utils.FileUrlUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 预约转诊 确认提交
 */
public class TransferSubmitFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rb_up)
    RadioButton rbUp;
    @BindView(R.id.rb_down)
    RadioButton rbDown;
    @BindView(R.id.rb_family_require)
    RadioButton rbFamilyRequire;
    @BindView(R.id.rb_rehabilitation)
    RadioButton rbRehabilitation;
    @BindView(R.id.rb_other)
    RadioButton rbOther;
    @BindView(R.id.et_other)
    EditText etOther;
    @BindView(R.id.tv_notice_num)
    TextView tvNoticeNum;
    @BindView(R.id.layout_other)
    ExpandableLayout layoutOther;
    @BindView(R.id.rb_self)
    RadioButton rbSelf;
    @BindView(R.id.rb_ncms)
    RadioButton rbNcms;
    @BindView(R.id.rb_medicare)
    RadioButton rbMedicare;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.iv_receiving_doctor)
    ImageView ivReceivingDoctor;
    @BindView(R.id.tv_receiving_doctor_name)
    TextView tvReceivingDoctorName;
    @BindView(R.id.tv_receiving_doctor_title)
    TextView tvReceivingDoctorTitle;
    @BindView(R.id.tv_receiving_doctor_hospital_depart)
    TextView tvReceivingDoctorHospitalDepart;
    @BindView(R.id.iv_receiving_doctor_call)
    ImageView ivReceivingDoctorDelete;
    @BindView(R.id.layout_receiving_doctor)
    RelativeLayout layoutReceivingDoctor;
    @BindView(R.id.iv_upload_one)
    ImageView ivUploadOne;
    @BindView(R.id.iv_delete_one)
    ImageView ivDeleteOne;
    @BindView(R.id.layout_upload_one)
    RelativeLayout layoutUploadOne;
    @BindView(R.id.tv_submit_next)
    TextView tvSubmitNext;
    @BindView(R.id.group_transfer_type)
    RadioGroup groupTransferType;
    @BindView(R.id.group_transfer_purpose)
    RadioGroup groupTransferPurpose;
    @BindView(R.id.group_payment)
    RadioGroup groupPayment;
    private File cameraTempFile;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    /**
     * 当前预约转诊数据
     */
    private ReserveTransferBean reverseTransferBean;
    private DoctorInfoBean curReceiveDoctor;
    /**
     * 拍照确认图片url
     */
    private String confirmImageUrl;
    /**
     * 转诊目的其他原因
     */
    private String otherString;
    /**
     * 缴费类型、转诊目的、转诊类型
     */
    private int payTypeId, transferPurposeId, transferTypeId;
    /**
     * 图片
     */
    private ArrayList<NormImage> imagePaths = new ArrayList<>();
    /**
     * 二次编辑 是否清空所有已填数据
     */
    private boolean clearAll;
    /**
     * 选择接诊医生
     */
    public static final int REQUEST_CODE_SELECT_DOCTOR = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_submit_transfer;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPageData();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //默认自费
        payTypeId = rbSelf.getId();
        //默认家属要求
        transferPurposeId = rbFamilyRequire.getId();
        //默认上转
        transferTypeId = rbUp.getId();
    }

    @Override
    public void initListener() {
        super.initListener();
        groupTransferType.setOnCheckedChangeListener(this);
        groupTransferPurpose.setOnCheckedChangeListener(this);
        groupPayment.setOnCheckedChangeListener(this);
        etOther.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                otherString = s.toString();
                tvNoticeNum.setText(String.format(getString(R.string.txt_calc_num_thirty), otherString.length()));
                if (reverseTransferBean != null) {
                    reverseTransferBean.setTransferTarget(otherString);
                }
                initNextButton();
            }
        });
    }

    public void setReverseTransferBean(ReserveTransferBean bean) {
        clearAll(bean);
        this.reverseTransferBean = bean;
    }

    /**
     * 点击返回回传数据
     *
     * @return
     */
    public ReserveTransferBean getReverseTransferBean() {
        return reverseTransferBean;
    }

    /**
     * 上传图片
     *
     * @param file
     */
    private void uploadImage(File file) {
        ScalingUtils.resizePic(getContext(), file.getAbsolutePath());
        RequestUtils.uploadImg(getContext(), loginBean.getToken(), file, this);
    }

    /**
     * 页面逻辑
     */
    private void initPageData() {
        if (clearAll) {
            rbUp.setChecked(true);
            rbFamilyRequire.setChecked(true);
            rbSelf.setChecked(true);
        }
        else {
            //数据回填
            initTransferData();
        }
    }

    private void initTransferData() {
        if (reverseTransferBean != null) {
            //转诊类型
            if (reverseTransferBean.getTransferType() == BASE_ZERO) {
                transferTypeId = rbUp.getId();
                rbUp.setChecked(true);
            }
            else if (reverseTransferBean.getTransferType() == BASE_ONE) {
                transferTypeId = rbDown.getId();
                rbDown.setChecked(true);
            }
            else {
                reverseTransferBean.setTransferType(BASE_ZERO);
            }
            //缴费类型
            if (reverseTransferBean.getPayType() == BASE_ZERO) {
                payTypeId = rbSelf.getId();
                rbSelf.setChecked(true);
            }
            else if (reverseTransferBean.getPayType() == BASE_ONE) {
                payTypeId = rbMedicare.getId();
                rbMedicare.setChecked(true);
            }
            else if (reverseTransferBean.getPayType() == BASE_TWO) {
                payTypeId = rbNcms.getId();
                rbNcms.setChecked(true);
            }
            else {
                reverseTransferBean.setPayType(BASE_ZERO);
            }
            //转诊目的
            if (!TextUtils.isEmpty(reverseTransferBean.getTransferTarget())) {
                if (getString(R.string.txt_family_require).equals(reverseTransferBean.getTransferTarget())) {
                    transferPurposeId = rbFamilyRequire.getId();
                    rbFamilyRequire.setChecked(true);
                }
                else if (getString(R.string.txt_rehabilitation).equals(reverseTransferBean.getTransferTarget())) {
                    rbRehabilitation.setChecked(true);
                    transferPurposeId = rbRehabilitation.getId();
                }
                else {
                    transferPurposeId = rbOther.getId();
                    rbOther.setChecked(true);
                    layoutOther.expand();
                    otherString = reverseTransferBean.getTransferTarget();
                    etOther.setText(otherString);
                }
            }
            else {
                reverseTransferBean.setTransferTarget(getString(R.string.txt_family_require));
            }
            //接诊医生
            if (!TextUtils.isEmpty(reverseTransferBean.getReceiveDoctorCode())) {
                curReceiveDoctor = new DoctorInfoBean();
                curReceiveDoctor.setDoctorCode(reverseTransferBean.getReceiveDoctorCode());
                curReceiveDoctor.setDoctorName(reverseTransferBean.getReceiveDoctorName());
                curReceiveDoctor.setPhoto(reverseTransferBean.getReceiveDoctorPhoto());
                curReceiveDoctor.setJobTitle(reverseTransferBean.getReceiveDoctorJobTitle());
                curReceiveDoctor.setHospitalName(reverseTransferBean.getReceiveDoctorHospital());
                curReceiveDoctor.setDepartmentName(reverseTransferBean.getReceiveDoctorDepart());
                initReceiveDoctor(false);
            }
            if (!TextUtils.isEmpty(reverseTransferBean.getConfirmPhoto())) {
                confirmImageUrl = reverseTransferBean.getConfirmPhoto();
                ivDeleteOne.setVisibility(View.VISIBLE);
                //裁剪完成，上传图片
                Glide.with(this)
                     .load(FileUrlUtil.addTokenToUrl(confirmImageUrl))
                     .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 4)))
                     .into(ivUploadOne);
            }
            initNextButton();
        }
    }

    /**
     * 涉及到数据回填逻辑，如果更改了患者，需要清空原有已填写数据
     */
    private void clearAll(ReserveTransferBean bean) {
        if (reverseTransferBean == null || bean == null) {
            clearAll = false;
        }
        else {
            if (reverseTransferBean.getPatientName().equals(bean.getPatientName()) &&
                reverseTransferBean.getPatientIdCardNo().equals(bean.getPatientIdCardNo())) {
                //都相等 说明未改变用户
                clearAll = false;
            }
            else {
                //有不相等的 说明患者已经更改，需要清除原有已填写数据
                clearAll = true;
            }
        }
    }

    /**
     * 图片处理
     *
     * @param status
     */
    private void initImage(boolean status) {
        if (status) {
            ivDeleteOne.setVisibility(View.VISIBLE);
            //裁剪完成，上传图片
            Glide.with(this)
                 .load(mCurrentPhotoPath)
                 .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 4)))
                 .into(ivUploadOne);
        }
        else {
            imagePaths.clear();
            ivUploadOne.setImageDrawable(null);
            ivDeleteOne.setVisibility(View.GONE);
            cameraTempFile = null;
            mCurrentPhotoUri = null;
            confirmImageUrl = "";
            mCurrentPhotoPath = "";
        }
        initNextButton();
    }

    /**
     * 接诊医生回调
     */
    private void initReceiveDoctor(boolean refresh) {
        layoutReceivingDoctor.setVisibility(View.VISIBLE);
        tvSelect.setVisibility(View.GONE);
        if (curReceiveDoctor != null) {
            Glide.with(this)
                 .load(curReceiveDoctor.getPhoto())
                 .apply(GlideHelper.getOptions(BaseUtils.dp2px(getContext(), 4)))
                 .into(ivReceivingDoctor);
            tvReceivingDoctorName.setText(curReceiveDoctor.getDoctorName());
            tvReceivingDoctorTitle.setText(curReceiveDoctor.getJobTitle());
            tvReceivingDoctorHospitalDepart.setText(
                    curReceiveDoctor.getHospitalName() + "  " + curReceiveDoctor.getDepartmentName());
            ivReceivingDoctorDelete.setImageResource(R.mipmap.ic_delete);
        }
        if (refresh) { initNextButton(); }
    }

    /**
     * 全部删除已经选择的检查项目和医院
     */
    private void deleteAllSelectCheckType() {
        curReceiveDoctor = null;
        tvSelect.setVisibility(View.VISIBLE);
        layoutReceivingDoctor.setVisibility(View.GONE);
        initNextButton();
    }

    /**
     * next
     */
    private void initNextButton() {
        boolean other = rbOther.getId() != transferPurposeId || !TextUtils.isEmpty(otherString);
        if (curReceiveDoctor != null && !TextUtils.isEmpty(confirmImageUrl) && other) {
            tvSubmitNext.setSelected(true);
        }
        else {
            tvSubmitNext.setSelected(false);
        }
    }

    @OnClick({
            R.id.layout_select_receiving_doctor, R.id.iv_receiving_doctor_call, R.id.iv_delete_one,
            R.id.layout_upload_one, R.id.tv_submit_next })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_select_receiving_doctor:
                if (tvSelect.getVisibility() == View.VISIBLE) {
                    intent = new Intent(getContext(), SelectReceivingDoctorActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_DOCTOR);
                }
                break;
            case R.id.layout_upload_one:
                if (TextUtils.isEmpty(confirmImageUrl)) {
                    permissionHelper.request(new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
                }
                else {
                    //查看大图
                    intent = new Intent(getContext(), ImagePreviewActivity.class);
                    intent.putExtra(ImagePreviewActivity.INTENT_URLS, imagePaths);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_fade_in, R.anim.keep);
                }
                break;
            case R.id.iv_delete_one:
                reverseTransferBean.setConfirmPhoto("");
                initImage(false);
                break;
            case R.id.tv_submit_next:
                if (tvSubmitNext.isSelected() && onTransferListener != null) {
                    onTransferListener.onTransferStepThree(reverseTransferBean);
                }
                break;
            case R.id.iv_receiving_doctor_call:
                reverseTransferBean.setReceiveDoctorCode("");
                deleteAllSelectCheckType();
                break;
            default:
                break;
        }
    }

    /**
     * RadioGroup选择
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.group_transfer_type:
                transferTypeId = checkedId;
                //转诊类型
                reverseTransferBean.setTransferType(transferTypeId == rbUp.getId() ? BASE_ZERO : BASE_ONE);
                break;
            case R.id.group_transfer_purpose:
                transferPurposeId = checkedId;
                if (checkedId == rbOther.getId()) {
                    layoutOther.expand();
                }
                else {
                    layoutOther.collapse();
                }
                //转诊目的
                if (transferPurposeId == rbFamilyRequire.getId()) {
                    reverseTransferBean.setTransferTarget(getString(R.string.txt_family_require));
                }
                else if (transferPurposeId == rbRehabilitation.getId()) {
                    reverseTransferBean.setTransferTarget(getString(R.string.txt_rehabilitation));
                }
                initNextButton();
                break;
            case R.id.group_payment:
                payTypeId = checkedId;
                //缴费类型
                if (payTypeId == rbSelf.getId()) {
                    reverseTransferBean.setPayType(BASE_ZERO);
                }
                else if (payTypeId == rbMedicare.getId()) {
                    reverseTransferBean.setPayType(BASE_ONE);
                }
                else {
                    reverseTransferBean.setPayType(BASE_TWO);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.UPLOAD_FILE) {
            confirmImageUrl = (String)response.getData();
            reverseTransferBean.setConfirmPhoto(confirmImageUrl);
            if (imagePaths.size() > 0) {
                imagePaths.get(0).setImageUrl(confirmImageUrl);
            }
            initImage(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK_CAMERA:
                imagePaths.clear();
                NormImage normImage = new NormImage();
                normImage.setImagePath(mCurrentPhotoPath);
                imagePaths.add(normImage);
                cameraTempFile = new File(mCurrentPhotoPath);
                uploadImage(cameraTempFile);
                break;
            case REQUEST_CODE_SELECT_DOCTOR:
                if (data != null) {
                    curReceiveDoctor = (DoctorInfoBean)data.getSerializableExtra(CommonData.KEY_DOCTOR_BEAN);
                }
                reverseTransferBean.setReceiveDoctorCode(curReceiveDoctor.getDoctorCode());
                reverseTransferBean.setReceiveDoctorName(curReceiveDoctor.getDoctorName());
                reverseTransferBean.setReceiveDoctorPhoto(curReceiveDoctor.getPhoto());
                reverseTransferBean.setReceiveDoctorJobTitle(curReceiveDoctor.getJobTitle());
                reverseTransferBean.setReceiveDoctorHospital(curReceiveDoctor.getHospitalName());
                reverseTransferBean.setReceiveDoctorDepart(curReceiveDoctor.getDepartmentName());
                initReceiveDoctor(true);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        cameraTempFile = new File(DirHelper.getPathImage(), System.currentTimeMillis() + ".jpg");
        if (cameraTempFile != null) {
            mCurrentPhotoPath = cameraTempFile.getAbsolutePath();
        }
        //选择拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mCurrentPhotoUri = FileProvider.getUriForFile(getContext(), ZycApplication.getInstance().getPackageName() +
                                                                        ".fileprovider", cameraTempFile);
        }
        else {
            mCurrentPhotoUri = Uri.fromFile(cameraTempFile);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ResolveInfo> resInfoList = getContext().getPackageManager()
                                                        .queryIntentActivities(intent,
                                                                               PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getContext().grantUriPermission(packageName, mCurrentPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                                               Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
        startActivityForResult(intent, RC_PICK_CAMERA);
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0])) {
                openCamera();
            }
        }
    }

    private OnTransferListener onTransferListener;

    public void setOnTransferListener(OnTransferListener onTransferListener) {
        this.onTransferListener = onTransferListener;
    }
}
