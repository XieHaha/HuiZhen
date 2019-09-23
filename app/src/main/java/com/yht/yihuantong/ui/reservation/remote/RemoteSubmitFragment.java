package com.yht.yihuantong.ui.reservation.remote;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScalingUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.adapter.CheckTypeListViewAdapter;
import com.yht.yihuantong.ui.check.SelectCheckTypeActivity;
import com.yht.yihuantong.ui.check.SelectCheckTypeByHospitalActivity;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des  确认提交
 */
public class RemoteSubmitFragment extends BaseFragment
        implements CheckTypeListViewAdapter.OnDeleteClickListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.full_listview)
    FullListView fullListView;
    @BindView(R.id.layout_check_root)
    LinearLayout layoutCheckRoot;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
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
    @BindView(R.id.tv_submit_next)
    TextView tvSubmitNext;
    @BindView(R.id.tv_hospital_name)
    TextView tvHospitalName;
    @BindView(R.id.layout_pregnancy)
    RadioGroup layoutPregnancy;
    @BindView(R.id.layout_payment)
    RadioGroup layoutPayment;
    private File cameraTempFile;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    private ReserveCheckBean reserveCheckBean;
    /**
     * 已选择检查项目适配器
     */
    private CheckTypeListViewAdapter checkTypeListviewAdapter;
    /**
     * 检查项目数据
     */
    private List<SelectCheckTypeBean> checkTypeData = new ArrayList<>();
    /**
     * 拍照确认图片url
     */
    private String confirmImageUrl;
    /**
     * 缴费类型、转诊目的、转诊类型
     */
    private int payTypeId, pregnancyId;
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
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //默认自费
        payTypeId = rbSelf.getId();
        //默认备孕
        //        pregnancyId = rbYes.getId();
    }

    @Override
    public void initListener() {
        super.initListener();
        //        layoutPregnancy.setOnCheckedChangeListener(this);
        layoutPayment.setOnCheckedChangeListener(this);
    }

    public void setReserveCheckBean(ReserveCheckBean reserveCheckBean) {
        this.reserveCheckBean = reserveCheckBean;
    }

    /**
     * 上传图片
     *
     * @param file
     */
    private void uploadImage(File file) {
        ScalingUtils.resizePic(getContext(), file.getAbsolutePath());
        RequestUtils.uploadImg(getContext(), loginBean.getToken(), file, this);
    }

    /**
     * 检查项目列表
     */
    private void initFullListView() {
        checkTypeListviewAdapter = new CheckTypeListViewAdapter(getContext());
        checkTypeListviewAdapter.setData(checkTypeData);
        checkTypeListviewAdapter.setOnDeleteClickListener(this);
        fullListView.setAdapter(checkTypeListviewAdapter);
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
        SelectCheckTypeBean bean = (SelectCheckTypeBean)data.getSerializableExtra(
                CommonData.KEY_RESERVE_CHECK_TYPE_BEAN);
        checkTypeData.clear();
        checkTypeData.add(bean);
        tvSelect.setVisibility(View.GONE);
        layoutCheckRoot.setVisibility(View.VISIBLE);
        tvHospitalName.setText(bean.getHospitalName());
        initFullListView();
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
        checkTypeData.addAll(list);
        checkTypeListviewAdapter.setData(checkTypeData);
        checkTypeListviewAdapter.notifyDataSetChanged();
    }

    /**
     * 全部删除已经选择的检查项目和医院
     */
    private void deleteAllSelectCheckType() {
        checkTypeData.clear();
        tvSelect.setVisibility(View.VISIBLE);
        layoutCheckRoot.setVisibility(View.GONE);
        initNextButton();
    }

    /**
     * next按钮可点击状态
     */
    private void initNextButton() {
        //需要添加判断检查项目是否为空
        if (checkTypeData.size() > 0 && !TextUtils.isEmpty(confirmImageUrl)) {
            tvSubmitNext.setSelected(true);
        }
        else {
            tvSubmitNext.setSelected(false);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.layout_pregnancy:
                pregnancyId = checkedId;
                break;
            case R.id.layout_payment:
                payTypeId = checkedId;
                break;
            default:
                break;
        }
    }

    @OnClick({
            R.id.layout_select_check_type, R.id.tv_delete_all, R.id.layout_upload_one, R.id.iv_delete_one,
            R.id.tv_submit_next, R.id.layout_add_hospital_check })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_select_check_type:
                if (tvSelect.getVisibility() == View.VISIBLE) {
                    intent = new Intent(getContext(), SelectCheckTypeActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_HOSPITAL);
                }
                break;
            case R.id.tv_delete_all:
                deleteAllSelectCheckType();
                break;
            case R.id.layout_add_hospital_check:
                intent = new Intent(getContext(), SelectCheckTypeByHospitalActivity.class);
                intent.putExtra(CommonData.KEY_TITLE, checkTypeData.get(0).getHospitalName());
                intent.putExtra(CommonData.KEY_HOSPITAL_CODE, checkTypeData.get(0).getHospitalCode());
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < checkTypeData.size(); i++) {
                    SelectCheckTypeBean bean = checkTypeData.get(i);
                    builder.append(bean.getProjectCode());
                    if (checkTypeData.size() - 1 != i) {
                        builder.append(",");
                    }
                }
                intent.putExtra(CommonData.KEY_PUBLIC, builder.toString());
                startActivityForResult(intent, REQUEST_CODE_SELECT_CHECK);
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
                if (tvSubmitNext.isSelected() && checkListener != null) {
                    reserveCheckBean.setConfirmPhoto(confirmImageUrl);
                    ArrayList<ReserveCheckTypeBean> list = new ArrayList<>();
                    for (SelectCheckTypeBean bean : checkTypeData) {
                        ReserveCheckTypeBean checkBean = new ReserveCheckTypeBean();
                        checkBean.setHospitalCode(bean.getHospitalCode());
                        checkBean.setProductCode(bean.getProjectCode());
                        checkBean.setPrice(bean.getPrice());
                        list.add(checkBean);
                    }
                    //检查项列表
                    reserveCheckBean.setCheckTrans(list);
                    //                    //是否备孕
                    //                    if (pregnancyId == rbYes.getId()) {
                    //                        reserveCheckBean.setIsPregnancy(BaseData.BASE_ONE);
                    //                    }
                    //                    else {
                    //                        reserveCheckBean.setIsPregnancy(BaseData.BASE_ZERO);
                    //                    }
                    //缴费类型
                    if (payTypeId == rbSelf.getId()) {
                        reserveCheckBean.setPayType(String.valueOf(BaseData.BASE_ZERO));
                    }
                    else if (payTypeId == rbMedicare.getId()) {
                        reserveCheckBean.setPayType(String.valueOf(BaseData.BASE_ONE));
                    }
                    else {
                        reserveCheckBean.setPayType(String.valueOf(BaseData.BASE_TWO));
                    }
                    checkListener.onCheckStepThree(reserveCheckBean);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 删除已选择检查项目
     *
     * @param position
     */
    @Override
    public void onDelete(int position) {
        checkTypeData.remove(position);
        checkTypeListviewAdapter.setData(checkTypeData);
        checkTypeListviewAdapter.notifyDataSetChanged();
        //不存在检查项 删除已选医院
        if (checkTypeData.size() == 0) {
            deleteAllSelectCheckType();
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
