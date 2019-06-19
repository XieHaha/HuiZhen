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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.api.DirHelper;
import com.yht.frame.data.CommonData;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zhihu.matisse.Matisse;
import com.zyc.doctor.R;
import com.zyc.doctor.ZycApplication;
import com.zyc.doctor.ui.auth.SelectDepartActivity;
import com.zyc.doctor.ui.auth.SelectHospitalActivity;
import com.zyc.doctor.ui.auth.listener.OnStepListener;
import com.zyc.doctor.ui.dialog.DownDialog;
import com.zyc.doctor.ui.dialog.listener.OnMediaItemClickListener;
import com.zyc.doctor.ui.dialog.listener.OnTitleItemClickListener;
import com.zyc.doctor.utils.glide.GlideHelper;
import com.zyc.doctor.utils.glide.MatisseUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 认证基础信息
 */
public class AuthBaseFragment extends BaseFragment implements OnMediaItemClickListener, OnTitleItemClickListener {
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
    @BindView(R.id.iv_auth_base_img)
    ImageView ivAuthBaseImg;
    private String name, hospital, depart, title;
    private Uri cutFileUri;
    private File cameraTempFile;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    private List<String> titleDatas;
    /**
     * 医院选择
     */
    public static final int REQUEST_CODE_HOSPITAL = 100;
    /**
     * 科室选择
     */
    public static final int REQUEST_CODE_DEPART = REQUEST_CODE_HOSPITAL + 1;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_base;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        titleDatas = new ArrayList<String>() {
            {
                add(getString(R.string.txt_title_chief_physician));
                add(getString(R.string.txt_title_deputy_chief_physician));
                add(getString(R.string.txt_title_attending_physician));
                add(getString(R.string.txt_title_hospitalization__physician));
            }
        };
    }

    @OnClick({
            R.id.layout_upload_img, R.id.layout_base_hospital, R.id.layout_base_depart, R.id.layout_base_title,
            R.id.tv_auth_base_next })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_upload_img:
                new DownDialog(getContext()).setData(data).setOnMediaItemClickListener(this).show();
                break;
            case R.id.layout_base_hospital:
                intent = new Intent(getContext(), SelectHospitalActivity.class);
                startActivityForResult(intent, REQUEST_CODE_HOSPITAL);
                break;
            case R.id.layout_base_depart:
                intent = new Intent(getContext(), SelectDepartActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DEPART);
                break;
            case R.id.layout_base_title:
                new DownDialog(getContext()).setData(titleDatas)
                                            .setCurPosition(titleDatas.indexOf(title))
                                            .setOnTitleItemClickListener(this)
                                            .show();
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
     * 相机 相册回调
     *
     * @param position
     */
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
     * 职称
     *
     * @param position
     */
    @Override
    public void onTitleItemClick(int position) {
        title = titleDatas.get(position);
        tvAuthBaseTitle.setText(title);
        tvAuthBaseTitle.setSelected(true);
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
        //        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(intent, RC_PICK_CAMERA);
    }

    /**
     * 图片裁剪
     */
    private void startCutImg(Uri uri, Uri cutUri) {
        cutFileUri = cutUri;
        startActivityForResult(getCutimgIntent(uri, cutFileUri), RC_CROP_IMG);
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
                    cameraTempFile = new File(paths.get(0));
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    File file = new File(DirHelper.getPathCache(), fileName);
                    startCutImg(uris.get(0), Uri.fromFile(file));
                }
                break;
            case RC_PICK_CAMERA:
                cameraTempFile = new File(mCurrentPhotoPath);
                String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                File file = new File(DirHelper.getPathCache(), fileName);
                startCutImg(mCurrentPhotoUri, Uri.fromFile(file));
                break;
            case RC_CROP_IMG:
                //裁剪完成，上传图片
                Glide.with(this)
                     .load(cutFileUri)
                     .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 4)))
                     .into(ivAuthBaseImg);
                break;
            //医院选择
            case REQUEST_CODE_HOSPITAL:
                hospital = data.getStringExtra(CommonData.KEY_HOSPITAL_NAME);
                tvAuthBaseHospital.setText(hospital);
                tvAuthBaseHospital.setSelected(true);
                break;
            //科室选择
            case REQUEST_CODE_DEPART:
                depart = data.getStringExtra(CommonData.KEY_DEPART_NAME);
                tvAuthBaseDepart.setText(depart);
                tvAuthBaseDepart.setSelected(true);
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

    private OnStepListener onAuthStepListener;

    public void setOnAuthStepListener(OnStepListener onAuthStepListener) {
        this.onAuthStepListener = onAuthStepListener;
    }
}
