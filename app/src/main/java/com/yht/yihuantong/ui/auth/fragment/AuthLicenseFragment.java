package com.yht.yihuantong.ui.auth.fragment;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.api.DirHelper;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.DoctorAuthBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.LogUtils;
import com.yht.frame.utils.ScalingUtils;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.adapter.AddImageAdapter;
import com.yht.yihuantong.ui.auth.listener.OnAuthStepListener;
import com.yht.yihuantong.ui.dialog.DownDialog;
import com.yht.yihuantong.ui.dialog.listener.OnMediaItemClickListener;
import com.yht.yihuantong.utils.ImageUrlUtil;
import com.yht.yihuantong.utils.MatisseUtils;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 认证执照信息
 */
public class AuthLicenseFragment extends BaseFragment
        implements OnMediaItemClickListener, BaseQuickAdapter.OnItemClickListener,
                   BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.tv_auth_license_last)
    TextView tvAuthLicenseLast;
    @BindView(R.id.tv_auth_license_submit)
    TextView tvAuthLicenseSubmit;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    /**
     * 照片adapter
     */
    private AddImageAdapter addImageAdapter;
    /**
     * 上传数据mondle
     */
    private DoctorAuthBean doctorAuthBean;
    /**
     * 裁剪前的uri
     */
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    private File frontFile, backFile;
    private ArrayList<String> imagePaths;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_license;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        addImageAdapter = new AddImageAdapter(R.layout.item_add_image, imagePaths);
        addImageAdapter.setOnItemClickListener(this);
        addImageAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(addImageAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (doctorAuthBean != null && !TextUtils.isEmpty(doctorAuthBean.getCertFront())) {
            initPage();
        }
        else {
            //占位图
            imagePaths = new ArrayList<>();
            imagePaths.add("");
            addImageAdapter.setNewData(imagePaths);
        }
    }

    /**
     * 上传图片
     *
     * @param file
     */
    private void uploadImage(File file) {
        LogUtils.i(TAG, "size:" + file.length());
        ScalingUtils.resizePic(getContext(), file.getAbsolutePath());
        LogUtils.i(TAG, "compress size:" + file.length());
        RequestUtils.uploadImg(getContext(), loginBean.getToken(), file, this);
    }

    /**
     * 回填数据
     *
     * @param doctorInfoBean
     */
    public void setDoctorAuthBean(DoctorAuthBean doctorInfoBean) {
        this.doctorAuthBean = doctorInfoBean;
    }

    /**
     * 数据回填
     */
    private void initPage() {
        imagePaths = new ArrayList<>();
        if (!TextUtils.isEmpty(doctorAuthBean.getCertFront())) {
            imagePaths.add(ImageUrlUtil.append(doctorAuthBean.getCertFront()));
        }
        if (!TextUtils.isEmpty(doctorAuthBean.getCertBack())) {
            imagePaths.add(ImageUrlUtil.append(doctorAuthBean.getCertBack()));
        }
        if (imagePaths.size() == 1) {
            imagePaths.add("");
        }
        addImageAdapter.setNewData(imagePaths);
        initNextButton();
    }

    /**
     * 判断 下一步按钮
     */
    private void initNextButton() {
        //等于2表示里面至少有一张真实图片(可能存在占位图)
        if (imagePaths.size() == BaseData.BASE_TWO) {
            tvAuthLicenseSubmit.setSelected(true);
        }
        else {
            tvAuthLicenseSubmit.setSelected(false);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!TextUtils.isEmpty(imagePaths.get(position))) {
            //查看大图
            //            Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
            //            intent.putExtra(ImagePreviewActivity.INTENT_URLS, imagePaths);
            //            startActivity(intent);
            //            getActivity().overridePendingTransition(R.anim.anim_fade_in, R.anim.keep);
        }
        else {
            new DownDialog(getContext()).setData(data).setOnMediaItemClickListener(this).show();
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == BaseData.BASE_ONE) {
            //设置占位图
            imagePaths.set(position, "");
        }
        else {
            //先移除
            imagePaths.remove(imagePaths.get(position));
            if (!TextUtils.isEmpty(imagePaths.get(position))) {
                //占位图
                imagePaths.add("");
            }
        }
        addImageAdapter.setNewData(imagePaths);
        initNextButton();
    }

    @OnClick({ R.id.tv_auth_license_last, R.id.tv_auth_license_submit })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_license_last:
                if (onAuthStepListener != null) {
                    onAuthStepListener.onAuthTwo(BASE_ONE, null);
                }
                break;
            case R.id.tv_auth_license_submit:
                if (tvAuthLicenseSubmit.isSelected() && onAuthStepListener != null) {
                    onAuthStepListener.onAuthTwo(BASE_TWO, doctorAuthBean);
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

    File tempFile;

    /**
     * 打开相机
     */
    private void openCamera() {
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
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
        startActivityForResult(intent, RC_PICK_CAMERA);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.UPLOAD_FILE) {
            if (imagePaths.size() == BaseData.BASE_ONE) {
                //占位
                imagePaths.add("");
            }
            addImageAdapter.setNewData(imagePaths);
            initNextButton();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK_IMG:
                List<String> paths = Matisse.obtainPathResult(data);
                if (null != paths && 0 != paths.size()) {
                    imagePaths.set(imagePaths.size() - 1, paths.get(0));
                    uploadImage(new File(paths.get(0)));
                }
                break;
            case RC_PICK_CAMERA:
                imagePaths.set(imagePaths.size() - 1, mCurrentPhotoPath);
                uploadImage(new File(mCurrentPhotoPath));
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
