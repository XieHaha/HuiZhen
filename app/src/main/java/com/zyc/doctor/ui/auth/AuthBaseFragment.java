package com.zyc.doctor.ui.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.permission.OnPermissionCallback;
import com.yht.frame.permission.Permission;
import com.yht.frame.permission.PermissionHelper;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.MatisseUtils;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.auth.listener.OnAuthStepListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 认证基础信息
 */
public class AuthBaseFragment extends BaseFragment implements OnPermissionCallback {
    @BindView(R.id.layout_upload_img)
    RelativeLayout layoutUploadImg;
    @BindView(R.id.et_auth_base_name)
    SuperEditText etAuthBaseName;
    @BindView(R.id.tv_auth_base_sex)
    TextView tvAuthBaseSex;
    @BindView(R.id.tv_auth_base_hospital)
    TextView tvAuthBaseHospital;
    @BindView(R.id.layout_base_hospital)
    RelativeLayout layoutBaseHospital;
    @BindView(R.id.tv_auth_base_depart)
    TextView tvAuthBaseDepart;
    @BindView(R.id.layout_base_depart)
    RelativeLayout layoutBaseDepart;
    @BindView(R.id.tv_auth_base_title)
    TextView tvAuthBaseTitle;
    @BindView(R.id.layout_base_title)
    RelativeLayout layoutBaseTitle;
    @BindView(R.id.tv_auth_base_next)
    TextView tvAuthBaseNext;
    /**
     * 动态权限
     */
    private PermissionHelper permissionHelper;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_base;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        permissionHelper = PermissionHelper.getInstance(getActivity());
    }

    @OnClick({
            R.id.layout_upload_img, R.id.layout_base_hospital, R.id.layout_base_depart, R.id.layout_base_title,
            R.id.tv_auth_base_next })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_upload_img:
                //动态申请权限
                permissionHelper.request(new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
                break;
            case R.id.layout_base_hospital:
                break;
            case R.id.layout_base_depart:
                break;
            case R.id.layout_base_title:
                break;
            case R.id.tv_auth_base_next:
                if (onAuthStepListener != null) {
                    onAuthStepListener.onStepOne();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        MatisseUtils.open(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        onNoPermissionNeeded(permissionName);
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        if (permissionName == null) {
            return;
        }
        ToastUtil.toast(getContext(), R.string.dialog_no_camera_permission_tip);
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        onNoPermissionNeeded(permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        permissionHelper.requestAfterExplanation(Permission.CAMERA);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        HintDialog dialog = new HintDialog(getContext());
        dialog.setContentString(getString(R.string.dialog_no_camera_permission_tip));
        dialog.show();
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        openPhoto();
    }

    private OnAuthStepListener onAuthStepListener;

    public void setOnAuthStepListener(OnAuthStepListener onAuthStepListener) {
        this.onAuthStepListener = onAuthStepListener;
    }
}
