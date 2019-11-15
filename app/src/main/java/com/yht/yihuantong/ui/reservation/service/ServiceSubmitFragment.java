package com.yht.yihuantong.ui.reservation.service;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.api.DirHelper;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.data.bean.ReserveCheckBean;
import com.yht.frame.data.bean.ReserveCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeParentBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScalingUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.dialog.SignatureDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeParentSubmitAdapter;
import com.yht.yihuantong.ui.check.SelectCheckTypeActivity;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 预约检查 确认提交
 */
public class ServiceSubmitFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.rb_self)
    RadioButton rbSelf;
    @BindView(R.id.rb_medicare)
    RadioButton rbMedicare;
    @BindView(R.id.rb_ncms)
    RadioButton rbNcms;
    @BindView(R.id.iv_upload_one)
    ImageView ivUploadOne;
    @BindView(R.id.iv_delete_one)
    ImageView ivDeleteOne;
    @BindView(R.id.layout_upload_one)
    RelativeLayout layoutUploadOne;
    @BindView(R.id.layout_payment)
    RadioGroup layoutPayment;
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
    @BindView(R.id.layout_check)
    LinearLayout layoutCheck;
    @BindView(R.id.layout_signature)
    LinearLayout layoutSignature;
    @BindView(R.id.view_camera)
    View viewCamera;
    @BindView(R.id.layout_camera)
    LinearLayout layoutCamera;
    @BindView(R.id.service_recycler_view)
    RecyclerView serviceRecyclerView;
    private File cameraTempFile;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    private ReserveCheckBean reserveCheckBean;
    /**
     * 预约该服务入口传递的数据
     */
    private SelectCheckTypeParentBean parentBean;
    /**
     * 已选择检查项目适配器
     */
    private SelectCheckTypeParentSubmitAdapter shopAdapter;
    /**
     * 购物车
     */
    private ArrayList<SelectCheckTypeParentBean> checkTypeData = new ArrayList<>();
    /**
     * 拍照确认图片url   签名
     */
    private String confirmImageUrl, signatureImageUrl;
    /**
     * 缴费类型、转诊目的、转诊类型
     */
    private int payTypeId;
    /**
     * 患者确认方式
     */
    private int sureType;
    /**
     * 图片
     */
    private ArrayList<NormImage> imagePaths = new ArrayList<>();
    /**
     * 选择检查项目
     */
    public static final int REQUEST_CODE_SELECT_CHECK = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_submit_check;
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
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //预约服务数据 (预约该服务入口)
        if (parentBean != null) {
            checkTypeData.add(parentBean);
            if (checkTypeData.size() > 0) {
                layoutCheck.setVisibility(View.VISIBLE);
                tvSelect.setText(R.string.txt_add_service_goon);
            } else {
                layoutCheck.setVisibility(View.GONE);
                tvSelect.setText(R.string.txt_select_hint);
            }
        }
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //默认签名确认
        sureType = BASE_TWO;
        sureType();
        //默认自费
        payTypeId = rbSelf.getId();
    }

    @Override
    public void initListener() {
        super.initListener();
        layoutPayment.setOnCheckedChangeListener(this);
    }

    public void setReserveCheckBean(ReserveCheckBean reserveCheckBean) {
        this.reserveCheckBean = reserveCheckBean;
    }

    public void setParentBean(SelectCheckTypeParentBean parentBean) {
        this.parentBean = parentBean;
    }

    /**
     * 上传图片
     */
    private void uploadImage(File file) {
        ScalingUtils.resizePic(getContext(), file.getAbsolutePath());
        RequestUtils.uploadImg(getContext(), loginBean.getToken(), file, this);
    }

    /**
     * 检查项目列表
     */
    private void initAdapter() {
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        serviceRecyclerView.setNestedScrollingEnabled(false);
        shopAdapter =
                new SelectCheckTypeParentSubmitAdapter(R.layout.item_check_select_submit_root,
                        checkTypeData);
        serviceRecyclerView.setAdapter(shopAdapter);
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
        } else {
            imagePaths.clear();
            ivUploadOne.setImageDrawable(null);
            ivDeleteOne.setVisibility(View.GONE);
            cameraTempFile = null;
            mCurrentPhotoUri = null;
            confirmImageUrl = "";
            mCurrentPhotoPath = "";
        }
    }

    /**
     * 选择的检查项目
     */
    private void selectCheckItemByHospital(Intent data) {
        ArrayList<SelectCheckTypeParentBean> list =
                (ArrayList<SelectCheckTypeParentBean>) data.getSerializableExtra(
                        CommonData.KEY_RESERVE_CHECK_TYPE_LIST);
        checkTypeData.clear();
        checkTypeData.addAll(list);
        if (checkTypeData.size() > 0) {
            layoutCheck.setVisibility(View.VISIBLE);
            tvSelect.setText(R.string.txt_add_service_goon);
        } else {
            layoutCheck.setVisibility(View.GONE);
            tvSelect.setText(R.string.txt_select_hint);
        }
        shopAdapter.setNewData(checkTypeData);
    }

    /**
     * 重新选择
     */
    public void reselect() {
        //清除数据
        checkTypeData.clear();
        Intent intent = new Intent(getContext(), SelectCheckTypeActivity.class);
        intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true);
        intent.putExtra(CommonData.KEY_RESERVE_CHECK_TYPE_LIST, checkTypeData);
        startActivityForResult(intent, REQUEST_CODE_SELECT_CHECK);
    }

    /**
     * next按钮可点击状态
     */
    private boolean initNextButton() {
        //需要添加判断检查项目是否为空
        if (checkTypeData == null || checkTypeData.size() == 0) {
            ToastUtil.toast(getContext(), R.string.toast_select_service);
            return false;
        }
        boolean next = (!TextUtils.isEmpty(confirmImageUrl) && sureType == BASE_ONE) ||
                (sureType == BASE_TWO && !TextUtils.isEmpty(signatureImageUrl));
        if (!next) {
            ToastUtil.toast(getContext(), R.string.toast_select_sure_type);
            return false;
        }
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.layout_payment) {
            payTypeId = checkedId;
        }
    }

    @OnClick({
            R.id.layout_select_check_type, R.id.layout_upload_one, R.id.iv_delete_one,
            R.id.tv_submit_next,
            R.id.layout_upload_two, R.id.layout_signature, R.id.layout_camera})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_select_check_type:
                //选中该服务
                ArrayList<String> selectCodes = new ArrayList<>();
                for (SelectCheckTypeParentBean parentBean : checkTypeData) {
                    ArrayList<SelectCheckTypeBean> beans = parentBean.getProductPackageList();
                    for (SelectCheckTypeBean bean : beans) {
                        selectCodes.add(bean.getProjectCode());
                    }
                }
                ZycApplication.getInstance().setSelectCodes(selectCodes);
                intent = new Intent(getContext(), SelectCheckTypeActivity.class);
                intent.putExtra(CommonData.KEY_RESERVE_CHECK_TYPE_LIST, checkTypeData);
                startActivityForResult(intent, REQUEST_CODE_SELECT_CHECK);
                break;
            case R.id.layout_upload_one:
                if (TextUtils.isEmpty(confirmImageUrl)) {
                    permissionHelper.request(new String[]{Permission.CAMERA,
                            Permission.STORAGE_WRITE});
                } else {
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
                sureType = BASE_TWO;
                sureType();
                break;
            case R.id.layout_camera:
                sureType = BASE_ONE;
                sureType();
                break;
            case R.id.layout_upload_two:
                new SignatureDialog(getContext()).setOnEnterClickListener(bitmap -> {
                    sureType = BASE_TWO;
                    uploadImage(new File(DirHelper.getPathImage() + SignatureDialog.SIGNATURE_FILE_NAME));
                }).show();
                break;
            default:
                break;
        }
    }

    /**
     * 提交方式  （签名 or 拍照）
     */
    private void sureType() {
        switch (sureType) {
            case BASE_ONE:
                tvSignature.setSelected(false);
                tvSignature.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                viewSignature.setVisibility(View.INVISIBLE);
                tvCamera.setSelected(true);
                tvCamera.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                viewCamera.setVisibility(View.VISIBLE);
                tvTypeHint.setText(R.string.txt_camera_people_transfer_hint);
                layoutUploadOne.setVisibility(View.VISIBLE);
                layoutUploadTwo.setVisibility(View.GONE);
                break;
            case BASE_TWO:
                tvSignature.setSelected(true);
                tvSignature.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                viewSignature.setVisibility(View.VISIBLE);
                tvCamera.setSelected(false);
                tvCamera.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                viewCamera.setVisibility(View.INVISIBLE);
                tvTypeHint.setText(R.string.txt_signature_people_transfer_hint);
                layoutUploadOne.setVisibility(View.GONE);
                layoutUploadTwo.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 提交数据
     */
    private void submit() {
        if (initNextButton() && checkListener != null) {
            if (sureType == BASE_ONE) {
                reserveCheckBean.setConfirmPhoto(confirmImageUrl);
                reserveCheckBean.setConfirmType(String.valueOf(BASE_ONE));
            } else {
                reserveCheckBean.setConfirmPhoto(signatureImageUrl);
                reserveCheckBean.setConfirmType(String.valueOf(BASE_TWO));
            }
            ArrayList<ReserveCheckTypeBean> list = new ArrayList<>();
            for (SelectCheckTypeParentBean parentBean : checkTypeData) {
                ArrayList<SelectCheckTypeBean> beans = parentBean.getProductPackageList();
                for (SelectCheckTypeBean bean : beans) {
                    ReserveCheckTypeBean checkBean = new ReserveCheckTypeBean();
                    checkBean.setHospitalCode(parentBean.getHospitalCode());
                    checkBean.setProductCode(bean.getProjectCode());
                    checkBean.setPrice(bean.getPrice());
                    checkBean.setType(bean.getType());
                    list.add(checkBean);
                }
            }
            //检查项列表
            reserveCheckBean.setCheckTrans(list);
            //缴费类型
            if (payTypeId == rbSelf.getId()) {
                reserveCheckBean.setPayType(String.valueOf(BaseData.BASE_ZERO));
            } else if (payTypeId == rbMedicare.getId()) {
                reserveCheckBean.setPayType(String.valueOf(BaseData.BASE_ONE));
            } else {
                reserveCheckBean.setPayType(String.valueOf(BaseData.BASE_TWO));
            }
            checkListener.onCheckStepThree(reserveCheckBean);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (sureType == BASE_ONE) {
            confirmImageUrl = (String) response.getData();
            if (imagePaths.size() > 0) {
                imagePaths.get(0).setImageUrl(confirmImageUrl);
            }
            initImage(true);
        } else {
            signatureImageUrl = (String) response.getData();
            Glide.with(this).load(signatureImageUrl).apply(GlideHelper.getOptionsPic(0)).into(ivSignature);
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
            case REQUEST_CODE_SELECT_CHECK:
                if (data != null) {
                    selectCheckItemByHospital(data);
                }
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
            mCurrentPhotoUri = FileProvider.getUriForFile(getContext(),
                    ZycApplication.getInstance().getPackageName() +
                            ".fileprovider", cameraTempFile);
        } else {
            mCurrentPhotoUri = Uri.fromFile(cameraTempFile);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ResolveInfo> resInfoList = getContext().getPackageManager()
                    .queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getContext().grantUriPermission(packageName, mCurrentPhotoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
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
            if (isSamePermission(Permission.CAMERA, ((String[]) permissionName)[0])) {
                openCamera();
            }
        }
    }

    private OnCheckListener checkListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.checkListener = onCheckListener;
    }
}
