package com.zyc.doctor.ui.auth.fragment;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.api.DirHelper;
import com.yht.frame.permission.OnPermissionCallback;
import com.yht.frame.permission.Permission;
import com.yht.frame.permission.PermissionHelper;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.zhihu.matisse.Matisse;
import com.zyc.doctor.R;
import com.zyc.doctor.ZycApplication;
import com.zyc.doctor.ui.auth.listener.OnStepListener;
import com.zyc.doctor.ui.dialog.DownDialog;
import com.zyc.doctor.ui.dialog.listener.OnMediaItemClickListener;
import com.zyc.doctor.utils.glide.GlideHelper;
import com.zyc.doctor.utils.glide.MatisseUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 认证执照信息
 */
public class AuthLicenseFragment extends BaseFragment implements OnMediaItemClickListener, OnPermissionCallback {
    @BindView(R.id.iv_upload_one)
    ImageView ivUploadOne;
    @BindView(R.id.iv_delete_one)
    ImageView ivDeleteOne;
    @BindView(R.id.layout_upload_one)
    RelativeLayout layoutUploadOne;
    @BindView(R.id.iv_upload_two)
    ImageView ivUploadTwo;
    @BindView(R.id.iv_delete_two)
    ImageView ivDeleteTwo;
    @BindView(R.id.layout_upload_two)
    RelativeLayout layoutUploadTwo;
    @BindView(R.id.tv_auth_license_last)
    TextView tvAuthLicenseLast;
    @BindView(R.id.tv_auth_license_submit)
    TextView tvAuthLicenseSubmit;
    private Uri cutFileUriOne, cutFileUriTwo;
    private File cameraTempFileOne, cameraTempFileTwo;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    /**
     * 区分两处证件
     */
    private int type = -1;
    /**
     * 动态权限
     */
    private PermissionHelper permissionHelper;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_license;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        permissionHelper = PermissionHelper.getInstance(getActivity());
    }

    @OnClick({ R.id.layout_upload_one, R.id.layout_upload_two, R.id.tv_auth_license_last, R.id.tv_auth_license_submit })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_upload_one:
                type = BASE_ONE;
                new DownDialog(getContext()).setData(data).setOnMediaItemClickListener(this).show();
                break;
            case R.id.layout_upload_two:
                type = BASE_TWO;
                new DownDialog(getContext()).setData(data).setOnMediaItemClickListener(this).show();
                break;
            case R.id.tv_auth_license_last:
                if (onAuthStepListener != null) {
                    onAuthStepListener.onStepTwo(BASE_ONE);
                }
                break;
            case R.id.tv_auth_license_submit:
                if (onAuthStepListener != null) {
                    onAuthStepListener.onStepTwo(BASE_TWO);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onMediaItemClick(int position) {
        switch (position) {
            case 0:
                permissionHelper.request(new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
                break;
            case 1:
                permissionHelper.request(new String[] { Permission.STORAGE_WRITE });
                break;
            default:
                break;
        }
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        MatisseUtils.open(this, false);
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        File tempFile;
        tempFile = new File(DirHelper.getPathImage(), System.currentTimeMillis() + ".jpg");
        if (tempFile != null) {
            mCurrentPhotoPath = tempFile.getAbsolutePath();
        }
        //选择拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mCurrentPhotoUri = FileProvider.getUriForFile(getContext(), ZycApplication.getInstance().getPackageName() +
                                                                        ".fileprovider", tempFile);
        }
        else {
            mCurrentPhotoUri = Uri.fromFile(tempFile);
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
        if (type == BASE_ONE) {
            cameraTempFileOne = tempFile;
        }
        else {
            cameraTempFileTwo = tempFile;
        }
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(intent, RC_PICK_CAMERA);
    }

    /**
     * 图片裁剪
     */
    private void startCutImg(Uri uri, Uri cutUri) {
        cutFileUriOne = cutUri;
        startActivityForResult(getCutimgIntent(uri, cutUri), RC_CROP_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK_IMG:
                List<Uri> uris = Matisse.obtainResult(data);
                List<String> paths = Matisse.obtainPathResult(data);
                if (null != paths && 0 != paths.size()) {
                    if (type == BASE_ONE) {
                        cameraTempFileOne = new File(paths.get(0));
                        String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                        File file = new File(DirHelper.getPathCache(), fileName);
                        cutFileUriOne = Uri.fromFile(file);
                        startCutImg(uris.get(0), cutFileUriOne);
                    }
                    else {
                        cameraTempFileTwo = new File(paths.get(0));
                        String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                        File file = new File(DirHelper.getPathCache(), fileName);
                        cutFileUriTwo = Uri.fromFile(file);
                        startCutImg(uris.get(0), cutFileUriTwo);
                    }
                }
                break;
            case RC_PICK_CAMERA:
                if (type == BASE_ONE) {
                    cameraTempFileOne = new File(mCurrentPhotoPath);
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    File file = new File(DirHelper.getPathCache(), fileName);
                    cutFileUriOne = Uri.fromFile(file);
                    startCutImg(mCurrentPhotoUri, cutFileUriOne);
                }
                else {
                    cameraTempFileTwo = new File(mCurrentPhotoPath);
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    File file = new File(DirHelper.getPathCache(), fileName);
                    cutFileUriTwo = Uri.fromFile(file);
                    startCutImg(mCurrentPhotoUri, cutFileUriTwo);
                }
                break;
            case RC_CROP_IMG:
                //裁剪完成，上传图片
                if (type == BASE_ONE) {
                    Glide.with(this)
                         .load(cutFileUriOne)
                         .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 2)))
                         .into(ivUploadOne);
                }
                else {
                    Glide.with(this)
                         .load(cutFileUriTwo)
                         .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 2)))
                         .into(ivUploadTwo);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isSamePermission(String o, String n) {
        if (TextUtils.isEmpty(o) || TextUtils.isEmpty(n)) {
            return false;
        }
        if (o.equals(n)) {
            return true;
        }
        return false;
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
        for (String permission : permissionName) {
            if (Permission.STORAGE_WRITE.equals(permission)) {
                ToastUtil.toast(getContext(), R.string.dialog_no_storage_permission_tip);
                break;
            }
            if (Permission.CAMERA.equals(permission)) {
                ToastUtil.toast(getContext(), R.string.dialog_no_camera_permission_tip);
                break;
            }
        }
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        onNoPermissionNeeded(permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        permissionHelper.requestAfterExplanation(permissionName);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        HintDialog dialog = new HintDialog(getContext());
        if (isSamePermission(Permission.STORAGE_WRITE, permissionName)) {
            dialog.setContentString(getString(R.string.dialog_no_storage_permission_tip));
        }
        else if (isSamePermission(Permission.CAMERA, permissionName)) {
            dialog.setContentString(getString(R.string.dialog_no_camera_permission_tip));
        }
        dialog.show();
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.STORAGE_WRITE, ((String[])permissionName)[0])) {
                openPhoto();
            }
            else if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0])) {
                openCamera();
            }
        }
    }

    private OnStepListener onAuthStepListener;

    public void setOnAuthStepListener(OnStepListener onAuthStepListener) {
        this.onAuthStepListener = onAuthStepListener;
    }
}
