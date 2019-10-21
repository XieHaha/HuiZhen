package com.yht.yihuantong.ui.reservation.remote;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.api.DirHelper;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.data.bean.ReserveCheckBean;
import com.yht.frame.data.bean.ReserveCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScalingUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.dialog.SignatureDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;
import com.yht.yihuantong.ui.reservation.time.ConsultationTimeActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 确认提交
 */
public class RemoteSubmitFragment extends BaseFragment {
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.iv_upload_one)
    ImageView ivUploadOne;
    @BindView(R.id.iv_delete_one)
    ImageView ivDeleteOne;
    @BindView(R.id.layout_upload_one)
    RelativeLayout layoutUploadOne;
    @BindView(R.id.tv_submit_next)
    TextView tvSubmitNext;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.tv_camera)
    TextView tvCamera;
    @BindView(R.id.iv_signature)
    ImageView ivSignature;
    @BindView(R.id.layout_upload_two)
    RelativeLayout layoutUploadTwo;
    @BindView(R.id.tv_type_hint)
    TextView tvTypeHint;
    @BindView(R.id.view_signature)
    View viewSignature;
    @BindView(R.id.layout_signature)
    LinearLayout layoutSignature;
    @BindView(R.id.view_camera)
    View viewCamera;
    @BindView(R.id.layout_camera)
    LinearLayout layoutCamera;
    private File cameraTempFile;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    private ReserveCheckBean reserveCheckBean;
    /**
     * 拍照确认图片url
     */
    private String confirmImageUrl;
    /**
     * 图片
     */
    private ArrayList<NormImage> imagePaths = new ArrayList<>();
    /**
     * 根据检查项目选择医院
     */
    public static final int REQUEST_CODE_SELECT_HOSPITAL = 100;
    /**
     * 根据医院选择检查项目
     */
    public static final int REQUEST_CODE_SELECT_CHECK = 101;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_submit_remote;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(CommonData.KEY_PUBLIC, mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentPhotoPath = savedInstanceState.getString(CommonData.KEY_PUBLIC);
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        sureType(true);
    }

    public void setReserveCheckBean(ReserveCheckBean reserveCheckBean) {
        this.reserveCheckBean = reserveCheckBean;
    }

    /**
     * 上传图片
     */
    private void uploadImage(File file) {
        ScalingUtils.resizePic(getContext(), file.getAbsolutePath());
        RequestUtils.uploadImg(getContext(), loginBean.getToken(), file, this);
    }

    /**
     * 图片处理
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
     * 根据检查项目匹配医院回调
     */
    private void selectHospitalByCheckItem(Intent data) {
        tvSelect.setVisibility(View.GONE);
        initNextButton();
    }

    /**
     * 根据选择当前医院下的检查项目
     *
     * @param data
     */
    private void selectCheckItemByHospital(Intent data) {
        ArrayList<SelectCheckTypeBean> list = (ArrayList<SelectCheckTypeBean>)data.getSerializableExtra(
                CommonData.KEY_RESERVE_CHECK_TYPE_LIST);
    }

    /**
     * 全部删除已经选择的检查项目和医院
     */
    private void deleteAllSelectCheckType() {
        tvSelect.setVisibility(View.VISIBLE);
        initNextButton();
    }

    /**
     * next按钮可点击状态
     */
    private void initNextButton() {
        //需要添加判断检查项目是否为空
        if (!TextUtils.isEmpty(confirmImageUrl)) {
            tvSubmitNext.setSelected(true);
        }
        else {
            tvSubmitNext.setSelected(false);
        }
    }

    @OnClick({
            R.id.layout_select_check_type, R.id.layout_upload_one, R.id.iv_delete_one, R.id.tv_submit_next,
            R.id.layout_upload_two, R.id.layout_signature, R.id.layout_camera })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_select_check_type:
                intent = new Intent(getContext(), ConsultationTimeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_HOSPITAL);
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
                initImage(false);
                break;
            case R.id.tv_submit_next:
                submit();
                break;
            case R.id.layout_signature:
                sureType(true);
                break;
            case R.id.layout_camera:
                sureType(false);
                break;
            case R.id.layout_upload_two:
                new SignatureDialog(getContext()).setOnEnterClickListener(bitmap -> ivSignature.setImageBitmap(bitmap))
                                                 .show();
                break;
            default:
                break;
        }
    }

    /**
     * 提交方式  （签名 or 拍照）
     *
     * @param type true 为签名
     */
    private void sureType(boolean type) {
        if (type) {
            tvSignature.setSelected(true);
            tvSignature.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            viewSignature.setVisibility(View.VISIBLE);
            tvCamera.setSelected(false);
            tvCamera.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            viewCamera.setVisibility(View.INVISIBLE);
            tvTypeHint.setText(R.string.txt_signature_people_transfer_hint);
            layoutUploadOne.setVisibility(View.GONE);
            layoutUploadTwo.setVisibility(View.VISIBLE);
        }
        else {
            tvSignature.setSelected(false);
            tvSignature.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            viewSignature.setVisibility(View.INVISIBLE);
            tvCamera.setSelected(true);
            tvCamera.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            viewCamera.setVisibility(View.VISIBLE);
            tvTypeHint.setText(R.string.txt_camera_people_transfer_hint);
            layoutUploadOne.setVisibility(View.VISIBLE);
            layoutUploadTwo.setVisibility(View.GONE);
        }
    }

    /**
     * 提交数据
     */
    private void submit() {
        if (tvSubmitNext.isSelected() && checkListener != null) {
            reserveCheckBean.setConfirmPhoto(confirmImageUrl);
            ArrayList<ReserveCheckTypeBean> list = new ArrayList<>();
            //检查项列表
            reserveCheckBean.setCheckTrans(list);
            checkListener.onCheckStepThree(reserveCheckBean);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.UPLOAD_FILE) {
            confirmImageUrl = (String)response.getData();
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
            case REQUEST_CODE_SELECT_HOSPITAL:
                if (data != null) {
                    selectHospitalByCheckItem(data);
                }
                break;
            case REQUEST_CODE_SELECT_CHECK:
                if (data != null) { selectCheckItemByHospital(data); }
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
