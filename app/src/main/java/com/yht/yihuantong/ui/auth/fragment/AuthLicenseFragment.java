package com.yht.yihuantong.ui.auth.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.api.DirHelper;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.auth.listener.OnAuthStepListener;
import com.yht.yihuantong.ui.dialog.DownDialog;
import com.yht.yihuantong.ui.dialog.listener.OnMediaItemClickListener;
import com.yht.yihuantong.utils.glide.GlideHelper;
import com.yht.yihuantong.utils.glide.MatisseUtils;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 认证执照信息
 */
public class AuthLicenseFragment extends BaseFragment implements OnMediaItemClickListener {
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

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_license;
    }

    /**
     * 图片处理
     *
     * @param status
     */
    private void initImage(int type, boolean status) {
        switch (type) {
            case BASE_ONE:
                if (status) {
                    ivDeleteOne.setVisibility(View.VISIBLE);
                    //裁剪完成，上传图片
                    Glide.with(this)
                         .load(cutFileUriOne)
                         .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 4)))
                         .into(ivUploadOne);
                }
                else {
                    ivUploadOne.setImageDrawable(null);
                    ivDeleteOne.setVisibility(View.GONE);
                    cameraTempFileOne = null;
                    cutFileUriOne = null;
                    mCurrentPhotoPath = "";
                }
                break;
            case BASE_TWO:
                if (status) {
                    ivDeleteTwo.setVisibility(View.VISIBLE);
                    //裁剪完成，上传图片
                    Glide.with(this)
                         .load(cutFileUriTwo)
                         .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 4)))
                         .into(ivUploadTwo);
                }
                else {
                    ivUploadTwo.setImageDrawable(null);
                    ivDeleteTwo.setVisibility(View.GONE);
                    cameraTempFileTwo = null;
                    cutFileUriTwo = null;
                    mCurrentPhotoPath = "";
                }
                break;
            default:
                break;
        }
        initNextButton();
    }

    /**
     * 判断 下一步按钮
     */
    private void initNextButton() {
        if (cutFileUriOne == null || cutFileUriTwo == null) {
            tvAuthLicenseSubmit.setSelected(false);
        }
        else {
            tvAuthLicenseSubmit.setSelected(true);
        }
    }

    @OnClick({
            R.id.layout_upload_one, R.id.layout_upload_two, R.id.tv_auth_license_last, R.id.tv_auth_license_submit,
            R.id.iv_delete_one, R.id.iv_delete_two })
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
                    onAuthStepListener.onAuthTwo(BASE_ONE);
                }
                break;
            case R.id.tv_auth_license_submit:
                if (tvAuthLicenseSubmit.isSelected() && onAuthStepListener != null) {
                    onAuthStepListener.onAuthTwo(BASE_TWO);
                }
                break;
            case R.id.iv_delete_one:
                initImage(BASE_ONE, false);
                break;
            case R.id.iv_delete_two:
                initImage(BASE_TWO, false);
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
        startActivityForResult(getCutImageIntent(uri, cutUri), RC_CROP_IMG);
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
                initImage(type, true);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private OnAuthStepListener onAuthStepListener;

    public void setOnAuthStepListener(OnAuthStepListener onAuthStepListener) {
        this.onAuthStepListener = onAuthStepListener;
    }
}
