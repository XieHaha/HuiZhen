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
import android.view.LayoutInflater;
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
import com.yht.frame.data.bean.DepartInfoBean;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.data.bean.RemoteDepartBean;
import com.yht.frame.data.bean.ReserveRemoteBean;
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
import com.yht.yihuantong.ui.remote.listener.OnRemoteListener;
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
    @BindView(R.id.layout_depart)
    LinearLayout layoutDepart;
    @BindView(R.id.layout_signature)
    LinearLayout layoutSignature;
    @BindView(R.id.view_camera)
    View viewCamera;
    @BindView(R.id.layout_camera)
    LinearLayout layoutCamera;
    @BindView(R.id.tv_select_hint)
    TextView tvSelectHint;
    @BindView(R.id.tv_depart_select)
    TextView tvDepartSelect;
    private File cameraTempFile;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    private ReserveRemoteBean reserveRemoteBean;
    /**
     * 患者确认方式
     */
    private int sureType;
    /**
     * 预约时间
     */
    private String date, startHour, endHour;
    /**
     * 拍照确认图片url
     */
    private String confirmImageUrl, signatureImageUrl;
    /**
     * 图片
     */
    private ArrayList<NormImage> imagePaths = new ArrayList<>();
    /**
     * 已选科室
     */
    private ArrayList<RemoteDepartBean> remoteDepartBeans = new ArrayList<>();
    /**
     * 已选科室 id
     */
    private ArrayList<Integer> remoteDepartId = new ArrayList<>();
    /**
     * 二次编辑 是否清空所有已填数据
     */
    private boolean clearAll;
    /**
     * 选择远程会诊时间
     */
    public static final int REQUEST_CODE_SELECT_REMOTE_HOUR = 100;
    /**
     * 选择远程科室
     */
    public static final int REQUEST_CODE_SELECT_DEPART = 101;

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
    public void onResume() {
        super.onResume();
        initPageData();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //默认签名确认
        sureType = BASE_TWO;
        sureType();
    }

    public void setReserveRemoteBean(ReserveRemoteBean bean) {
        clearAll(bean);
        this.reserveRemoteBean = bean;
    }

    /**
     * 页面逻辑
     */
    private void initPageData() {
        if (clearAll) {
            tvSelect.setText("");
            tvSelectHint.setVisibility(View.INVISIBLE);
            tvDepartSelect.setText(R.string.txt_select_depart_hint1);
            tvDepartSelect.setSelected(false);
            //移除所有已添加子VIEW
            layoutDepart.removeAllViews();
            remoteDepartBeans.clear();
        }
        else {
            //数据回填
            initRemoteData();
        }
    }

    /**
     * 已有数据回填（订单数据）
     */
    private void initRemoteData() {
        if (reserveRemoteBean != null) {
            remoteDepartBeans.clear();
            remoteDepartId.clear();
            long start = BaseUtils.date2TimeStamp(reserveRemoteBean.getStartAt(), BaseUtils.YYYY_MM_DD_HH_MM);
            long end = BaseUtils.date2TimeStamp(reserveRemoteBean.getEndAt(), BaseUtils.YYYY_MM_DD_HH_MM);
            tvSelect.setText(timeFormat(start, end));
            ArrayList<DepartInfoBean> hosDeptInfo = reserveRemoteBean.getHosDeptInfo();
            if (hosDeptInfo != null) {
                for (DepartInfoBean info : hosDeptInfo) {
                    RemoteDepartBean bean = new RemoteDepartBean();
                    bean.setDepartmentId(info.getHospitalDepartmentId());
                    bean.setDepartmentName(info.getHospitalDepartmentName());
                    bean.setHospitalCode(info.getHospitalCode());
                    bean.setHospitalName(info.getHospitalName());
                    remoteDepartBeans.add(bean);
                    remoteDepartId.add(info.getHospitalDepartmentId());
                }
            }
            selectDepartItemByHospital();
        }
    }

    /**
     * 根据时间戳返回时间范围
     */
    public String timeFormat(long start, long end) {
        if (start == 0 || end == 0) {
            return "";
        }
        date = BaseUtils.formatDate(start, BaseUtils.YYYY_MM_DD);
        startHour = BaseUtils.formatDate(start, BaseUtils.HH_MM);
        endHour = BaseUtils.formatDate(end, BaseUtils.HH_MM);
        return date + " " + startHour + "-" + endHour;
    }

    /**
     * 涉及到数据回填逻辑，如果更改了居民，需要清空原有已填写数据
     */
    private void clearAll(ReserveRemoteBean bean) {
        if (reserveRemoteBean == null || bean == null) {
            clearAll = false;
        }
        else {
            clearAll = !reserveRemoteBean.getPatientName().equals(bean.getPatientName()) ||
                       !reserveRemoteBean.getPatientIdCard().equals(bean.getPatientIdCard());
        }
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
     * 选择时间回调
     */
    private void selectReservationTime() {
        tvSelect.setText(String.format(getString(R.string.txt_date_joiner), date, startHour, endHour));
        initNextButton();
    }

    /**
     * 科室数据处理
     */
    private void selectDepartItemByHospital() {
        //移除所有已添加子VIEW
        layoutDepart.removeAllViews();
        if (remoteDepartBeans != null && remoteDepartBeans.size() > 0) {
            for (int i = 0; i < remoteDepartBeans.size(); i++) {
                RemoteDepartBean bean = remoteDepartBeans.get(i);
                TextView textView = (TextView)LayoutInflater.from(getContext())
                                                            .inflate(R.layout.item_remote_depart_simple, null);
                textView.setText(String.format(getString(R.string.txt_joiner), bean.getDepartmentName(),
                                               bean.getHospitalName()));
                layoutDepart.addView(textView);
            }
            tvSelectHint.setVisibility(View.VISIBLE);
            tvDepartSelect.setText(R.string.txt_add);
            tvDepartSelect.setSelected(true);
        }
        else {
            tvSelectHint.setVisibility(View.INVISIBLE);
            tvDepartSelect.setText(R.string.txt_select_depart_hint1);
            tvDepartSelect.setSelected(false);
        }
        initNextButton();
    }

    /**
     * 重新选择
     * 1、重新选择时间 2、或者科室
     */
    public void reselect(int type) {
        Intent intent;
        if (type == BASE_ONE) {
            intent = new Intent(getContext(), ConsultationTimeActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_REMOTE_HOUR);
        }
        else {
            //清除已选的科室信息
            remoteDepartId.clear();
            remoteDepartBeans.clear();
            layoutDepart.removeAllViews();
            reserveRemoteBean.setHosDeptInfo(null);
            intent = new Intent(getContext(), SelectRemoteDepartActivity.class);
            intent.putExtra(CommonData.KEY_REMOTE_DATE, date);
            intent.putExtra(CommonData.KEY_REMOTE_START_HOUR, startHour);
            intent.putExtra(CommonData.KEY_REMOTE_END_HOUR, endHour);
            intent.putExtra(CommonData.KEY_REMOTE_DEPART_LIST_ID, remoteDepartId);
            startActivityForResult(intent, REQUEST_CODE_SELECT_DEPART);
        }
    }

    /**
     * next按钮可点击状态
     */
    private void initNextButton() {
        boolean next = (!TextUtils.isEmpty(confirmImageUrl) && sureType == BASE_ONE) ||
                       (sureType == BASE_TWO && !TextUtils.isEmpty(signatureImageUrl));
        if (next && !TextUtils.isEmpty(date) && remoteDepartBeans.size() > 0) {
            tvSubmitNext.setSelected(true);
        }
        else {
            tvSubmitNext.setSelected(false);
        }
    }

    @OnClick({
            R.id.layout_select_check_type, R.id.layout_hospital_depart, R.id.layout_upload_one, R.id.iv_delete_one,
            R.id.tv_submit_next, R.id.layout_upload_two, R.id.layout_signature, R.id.layout_camera })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_select_check_type:
                intent = new Intent(getContext(), ConsultationTimeActivity.class);
                intent.putExtra(CommonData.KEY_REMOTE_DATE, date);
                intent.putExtra(CommonData.KEY_REMOTE_START_HOUR, startHour);
                intent.putExtra(CommonData.KEY_REMOTE_END_HOUR, endHour);
                startActivityForResult(intent, REQUEST_CODE_SELECT_REMOTE_HOUR);
                break;
            case R.id.layout_hospital_depart:
                if (!TextUtils.isEmpty(date)) {
                    intent = new Intent(getContext(), SelectRemoteDepartActivity.class);
                    intent.putExtra(CommonData.KEY_REMOTE_DATE, date);
                    intent.putExtra(CommonData.KEY_REMOTE_START_HOUR, startHour);
                    intent.putExtra(CommonData.KEY_REMOTE_END_HOUR, endHour);
                    intent.putExtra(CommonData.KEY_REMOTE_DEPART_LIST_ID, remoteDepartId);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_DEPART);
                }
                else {
                    ToastUtil.toast(getContext(), R.string.txt_hour_empty_hint);
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
        initNextButton();
    }

    /**
     * 提交数据
     */
    private void submit() {
        if (tvSubmitNext.isSelected() && onRemoteListener != null) {
            if (sureType == BASE_ONE) {
                reserveRemoteBean.setConfirmFile(confirmImageUrl);
                reserveRemoteBean.setConfirmType(String.valueOf(BASE_ONE));
            }
            else {
                reserveRemoteBean.setConfirmFile(signatureImageUrl);
                reserveRemoteBean.setConfirmType(String.valueOf(BASE_TWO));
            }
            onRemoteListener.onRemoteStepThree(reserveRemoteBean);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.UPLOAD_FILE) {
            if (sureType == BASE_ONE) {
                confirmImageUrl = (String)response.getData();
                if (imagePaths.size() > 0) {
                    imagePaths.get(0).setImageUrl(confirmImageUrl);
                }
                initImage(true);
            }
            else {
                signatureImageUrl = (String)response.getData();
                Glide.with(this).load(signatureImageUrl).apply(GlideHelper.getOptionsPic(0)).into(ivSignature);
                initNextButton();
            }
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
            case REQUEST_CODE_SELECT_REMOTE_HOUR:
                if (data != null) {
                    //如果重新选择时间  需要清除已选科室
                    remoteDepartBeans.clear();
                    remoteDepartId.clear();
                    layoutDepart.removeAllViews();
                    reserveRemoteBean.setHosDeptInfo(null);
                    //选择的时间
                    date = data.getStringExtra(CommonData.KEY_REMOTE_DATE);
                    startHour = data.getStringExtra(CommonData.KEY_REMOTE_START_HOUR);
                    endHour = data.getStringExtra(CommonData.KEY_REMOTE_END_HOUR);
                    reserveRemoteBean.setStartAt(date + " " + startHour);
                    reserveRemoteBean.setEndAt(date + " " + endHour);
                    selectReservationTime();
                }
                break;
            case REQUEST_CODE_SELECT_DEPART:
                if (data != null) {
                    String date = data.getStringExtra(CommonData.KEY_REMOTE_DATE);
                    String startHour = data.getStringExtra(CommonData.KEY_REMOTE_START_HOUR);
                    String endHour = data.getStringExtra(CommonData.KEY_REMOTE_END_HOUR);
                    if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(startHour) && !TextUtils.isEmpty(endHour)) {
                        this.date = date;
                        this.startHour = startHour;
                        this.endHour = endHour;
                        reserveRemoteBean.setStartAt(date + " " + startHour);
                        reserveRemoteBean.setEndAt(date + " " + endHour);
                    }
                    remoteDepartBeans = (ArrayList<RemoteDepartBean>)data.getSerializableExtra(
                            CommonData.KEY_REMOTE_DEPART_LIST);
                    remoteDepartId = data.getIntegerArrayListExtra(CommonData.KEY_REMOTE_DEPART_LIST_ID);
                    ArrayList<DepartInfoBean> list = new ArrayList<>();
                    for (RemoteDepartBean bean : remoteDepartBeans) {
                        DepartInfoBean departInfoBean = new DepartInfoBean();
                        departInfoBean.setHospitalDepartmentId(bean.getDepartmentId());
                        departInfoBean.setHospitalDepartmentName(bean.getDepartmentName());
                        departInfoBean.setHospitalCode(bean.getHospitalCode());
                        departInfoBean.setHospitalName(bean.getHospitalName());
                        list.add(departInfoBean);
                    }
                    reserveRemoteBean.setHosDeptInfo(list);
                    selectDepartItemByHospital();
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
        mCurrentPhotoPath = cameraTempFile.getAbsolutePath();
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

    private OnRemoteListener onRemoteListener;

    public void setOnRemoteListener(OnRemoteListener onRemoteListener) {
        this.onRemoteListener = onRemoteListener;
    }
}
