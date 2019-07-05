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
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.DoctorInfoBean;
import com.yht.frame.http.retrofit.RequestUtils;
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
    /**
     * 上传数据mondle
     */
    private DoctorInfoBean doctorAuthBean;
    /**
     * 裁剪后的uri
     */
    private Uri cutFileUriFront, cutFileUriBack;
    /**
     * 裁剪前的uri
     */
    private Uri mCurrentPhotoUri;
    /**
     * 裁剪后的file文件
     */
    private File cutFileFront, cutFileBack;
    /**
     * 上传图片后的url
     */
    private String fileFrontUrl, fileBackUrl;
    /**
     * 区分两处证件
     */
    private int type = -1;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_license;
    }

    /**
     * 上传图片
     *
     * @param file
     */
    private void uploadImage(File file) {
        RequestUtils.uploadImg(getContext(), loginBean.getToken(), file, this);
    }

    public void setDoctorAuthBean(DoctorInfoBean doctorAuthBean) {
        this.doctorAuthBean = doctorAuthBean;
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
                         .load(cutFileUriFront)
                         .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 4)))
                         .into(ivUploadOne);
                }
                else {
                    ivUploadOne.setImageDrawable(null);
                    ivDeleteOne.setVisibility(View.GONE);
                    cutFileFront = null;
                    cutFileUriFront = null;
                }
                break;
            case BASE_TWO:
                if (status) {
                    ivDeleteTwo.setVisibility(View.VISIBLE);
                    //裁剪完成，上传图片
                    Glide.with(this)
                         .load(cutFileUriBack)
                         .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 4)))
                         .into(ivUploadTwo);
                }
                else {
                    ivUploadTwo.setImageDrawable(null);
                    ivDeleteTwo.setVisibility(View.GONE);
                    cutFileBack = null;
                    cutFileUriBack = null;
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
        if (cutFileUriFront == null || cutFileUriBack == null) {
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
                    onAuthStepListener.onAuthTwo(BASE_ONE, null);
                }
                break;
            case R.id.tv_auth_license_submit:
                if (tvAuthLicenseSubmit.isSelected() && onAuthStepListener != null) {
                    doctorAuthBean.setCertFront(fileFrontUrl);
                    doctorAuthBean.setCertBack(fileBackUrl);
                    onAuthStepListener.onAuthTwo(BASE_TWO, doctorAuthBean);
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
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(intent, RC_PICK_CAMERA);
    }

    /**
     * 图片裁剪
     */
    private void startCutImg(Uri uri, Uri cutUri) {
        cutFileUriFront = cutUri;
        startActivityForResult(getCutImageIntent(uri, cutUri), RC_CROP_IMG);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.UPLOAD_FILE) {
            if (type == BASE_ONE) {
                fileFrontUrl = (String)response.getData();
            }
            else {
                fileBackUrl = (String)response.getData();
            }
            initImage(type, true);
        }
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
                        String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                        cutFileFront = new File(DirHelper.getPathCache(), fileName);
                        cutFileUriFront = Uri.fromFile(cutFileFront);
                        startCutImg(uris.get(0), cutFileUriFront);
                    }
                    else {
                        String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                        cutFileBack = new File(DirHelper.getPathCache(), fileName);
                        cutFileUriBack = Uri.fromFile(cutFileBack);
                        startCutImg(uris.get(0), cutFileUriBack);
                    }
                }
                break;
            case RC_PICK_CAMERA:
                if (type == BASE_ONE) {
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    cutFileFront = new File(DirHelper.getPathCache(), fileName);
                    cutFileUriFront = Uri.fromFile(cutFileFront);
                    startCutImg(mCurrentPhotoUri, cutFileUriFront);
                }
                else {
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    cutFileBack = new File(DirHelper.getPathCache(), fileName);
                    cutFileUriBack = Uri.fromFile(cutFileBack);
                    startCutImg(mCurrentPhotoUri, cutFileUriBack);
                }
                break;
            case RC_CROP_IMG:
                if (type == BASE_ONE) {
                    uploadImage(cutFileFront);
                }
                else {
                    uploadImage(cutFileBack);
                }
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
