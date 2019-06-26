package com.zyc.doctor.ui.reservation.fragment;

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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.api.DirHelper;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.view.ExpandableLayout;
import com.zyc.doctor.R;
import com.zyc.doctor.ZycApplication;
import com.zyc.doctor.ui.check.listener.OnCheckListener;
import com.zyc.doctor.utils.glide.GlideHelper;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 预约转诊 确认提交
 */
public class SubmitTransferFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
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
     * 根据检查项目选择医院
     */
    public static final int REQUEST_CODE_SELECT_HOSPITAL = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_submit_transfer;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    public void initListener() {
        super.initListener();
        groupTransferType.setOnCheckedChangeListener(this);
        groupTransferPurpose.setOnCheckedChangeListener(this);
        groupPayment.setOnCheckedChangeListener(this);
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
            ivUploadOne.setImageDrawable(null);
            ivDeleteOne.setVisibility(View.GONE);
            cameraTempFile = null;
            mCurrentPhotoUri = null;
            mCurrentPhotoPath = "";
        }
    }

    /**
     * 根据检查项目匹配医院回调
     *
     * @param data
     */
    private void selectHospitalByCheckItem(Intent data) {
        tvSelect.setVisibility(View.GONE);
    }

    /**
     * 全部删除已经选择的检查项目和医院
     */
    private void deleteAllSelectCheckType() {
        layoutOther.collapse();
        tvSelect.setVisibility(View.VISIBLE);
    }

    @OnClick({
            R.id.layout_select_receiving_doctor, R.id.iv_receiving_doctor_delete, R.id.iv_delete_one,
            R.id.layout_upload_one, R.id.tv_submit_next })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_select_receiving_doctor:
                if (tvSelect.getVisibility() == View.VISIBLE) {
                    //                    intent = new Intent(getContext(), SelectCheckTypeActivity.class);
                    //                    startActivityForResult(intent, REQUEST_CODE_SELECT_HOSPITAL);
                    layoutReceivingDoctor.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.layout_upload_one:
                permissionHelper.request(new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
                break;
            case R.id.iv_delete_one:
                initImage(false);
                break;
            case R.id.tv_submit_next:
                if (checkListener != null) {
                    checkListener.onStepThree();
                }
                break;
            case R.id.iv_receiving_doctor_delete:
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
                break;
            case R.id.group_transfer_purpose:
                if (checkedId == rbOther.getId()) {
                    layoutOther.expand();
                }
                else {
                    layoutOther.collapse();
                }
                break;
            case R.id.group_payment:
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
        switch (requestCode) {
            case RC_PICK_CAMERA:
                cameraTempFile = new File(mCurrentPhotoPath);
                initImage(true);
                break;
            case REQUEST_CODE_SELECT_HOSPITAL:
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

    private OnCheckListener checkListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.checkListener = onCheckListener;
    }
}
