package com.yht.yihuantong.ui.reservation.fragment;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.api.DirHelper;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.adapter.CheckTypeListviewAdapter;
import com.yht.yihuantong.ui.check.SelectCheckTypeActivity;
import com.yht.yihuantong.ui.check.SelectCheckTypeByHospitalActivity;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;
import com.yht.yihuantong.utils.glide.GlideHelper;

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
public class SubmitCheckFragment extends BaseFragment implements CheckTypeListviewAdapter.OnDeleteClickListener {
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
    private File cameraTempFile;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;
    /**
     * 已选择检查项目适配器
     */
    private CheckTypeListviewAdapter checkTypeListviewAdapter;
    /**
     * 检查项目数据
     */
    private List<String> checkTypeData;
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
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    /**
     * 检查项目列表
     */
    private void initFullListView() {
        checkTypeListviewAdapter = new CheckTypeListviewAdapter(getContext());
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
            ivUploadOne.setImageDrawable(null);
            ivDeleteOne.setVisibility(View.GONE);
            cameraTempFile = null;
            mCurrentPhotoUri = null;
            mCurrentPhotoPath = "";
        }
        initNextButton();
    }

    /**
     * 根据检查项目匹配医院回调
     *
     * @param data
     */
    private void selectHospitalByCheckItem(Intent data) {
        tvSelect.setVisibility(View.GONE);
        layoutCheckRoot.setVisibility(View.VISIBLE);
        tvHospitalName.setText("医院");
        checkTypeData = new ArrayList<>();
        checkTypeData.add("测试数据");
        initFullListView();
    }

    /**
     * 根据选择当前医院下的检查项目
     *
     * @param data
     */
    private void selectCheckItemByHospital(Intent data) {
        checkTypeData.add("yige");
        checkTypeData.add("22222");
        checkTypeData.add("3333");
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
    }

    /**
     * next按钮可点击状态
     */
    private void initNextButton() {
        //需要添加判断检查项目是否为空
        if (cameraTempFile == null) {
            tvSubmitNext.setSelected(false);
        }
        else {
            tvSubmitNext.setSelected(true);
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
                startActivityForResult(intent, REQUEST_CODE_SELECT_CHECK);
                break;
            case R.id.layout_upload_one:
                permissionHelper.request(new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
                break;
            case R.id.iv_delete_one:
                initImage(false);
                break;
            case R.id.tv_submit_next:
                if (checkListener != null) {
                    checkListener.onStepThree();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK_CAMERA:
                cameraTempFile = new File(mCurrentPhotoPath);
                initImage(true);
                break;
            case REQUEST_CODE_SELECT_HOSPITAL:
                selectHospitalByCheckItem(data);
                break;
            case REQUEST_CODE_SELECT_CHECK:
                selectCheckItemByHospital(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
