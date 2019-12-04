package com.yht.yihuantong.ui.auth.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.DoctorAuthBean;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScalingUtils;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.adapter.AddImageAdapter;
import com.yht.yihuantong.ui.auth.PhotoInstanceActivity;
import com.yht.yihuantong.ui.auth.listener.OnAuthStepListener;
import com.yht.yihuantong.ui.dialog.listener.OnMediaItemClickListener;
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
 * @description 认证执照信息
 */
public class AuthLicenseFragment extends BaseFragment
        implements OnMediaItemClickListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_hint)
    JustifiedTextView tvHint;
    /**
     * 照片adapter
     */
    private AddImageAdapter addImageAdapter;
    /**
     * 上传数据mondle
     */
    private DoctorAuthBean doctorAuthBean;
    private String mCurrentPhotoPath;
    /**
     * 已上传图片
     */
    private ArrayList<NormImage> imagePaths;
    /**
     * 图片临时数据
     */
    private List<String> paths;
    private int currentUploadImgIndex = -1;
    private Handler dealImgHandler = new Handler(msg -> {
        //图片显示完开始上传图片
        uploadImage(new File(paths.get(currentUploadImgIndex)));
        return true;
    });

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_license;
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
        spannableString(getString(R.string.txt_upload_card_hint));
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
        } else {
            //占位图
            imagePaths = new ArrayList<>();
            imagePaths.add(new NormImage());
            addImageAdapter.setNewData(imagePaths);
        }
    }

    /**
     * 上传图片
     */
    private void uploadImage(File file) {
        ScalingUtils.resizePic(getContext(), file.getAbsolutePath());
        RequestUtils.uploadImgWaterMark(getContext(), loginBean.getToken(), file, false, this);
    }

    /**
     * 回填数据
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
            NormImage normImage = new NormImage();
            normImage.setImageUrl(doctorAuthBean.getCertFront());
            imagePaths.add(normImage);
        }
        if (!TextUtils.isEmpty(doctorAuthBean.getCertBack())) {
            NormImage normImage = new NormImage();
            normImage.setImageUrl(doctorAuthBean.getCertBack());
            imagePaths.add(normImage);
        }
        if (imagePaths.size() == 1) {
            imagePaths.add(new NormImage());
        }
        addImageAdapter.setNewData(imagePaths);
    }

    /**
     * 字符串处理
     */
    private void spannableString(String s) {
        SpannableString style = new SpannableString(s);
        //大小
        style.setSpan(new AbsoluteSizeSpan(BaseUtils.sp2px(getContext(), 16)), 4, 13,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //颜色
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),
                R.color.color_373d4d)), 4, 13,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //粗体
        style.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 4, 13,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvHint.setText(style);
    }

    /**
     * 判断 下一步按钮
     */
    private boolean initNextButton() {
        //等于2表示里面至少有一张真实图片(可能存在占位图)
        return imagePaths.size() == BaseData.BASE_TWO;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!TextUtils.isEmpty(imagePaths.get(position).getImagePath()) ||
                !TextUtils.isEmpty(imagePaths.get(position).getImageUrl())) {
            //查看大图
            Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
            ArrayList<NormImage> list = new ArrayList<>();
            for (NormImage image : imagePaths) {
                if (!TextUtils.isEmpty(image.getImagePath()) || !TextUtils.isEmpty(image.getImageUrl())) {
                    //显示水印图片,不显示本地
                    image.setImagePath("");
                    list.add(image);
                }
            }
            intent.putExtra(ImagePreviewActivity.INTENT_URLS, list);
            intent.putExtra(ImagePreviewActivity.INTENT_POSITION, position);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.anim_fade_in, R.anim.keep);
        } else {
            permissionHelper.request(new String[]{Permission.CAMERA, Permission.STORAGE_WRITE});
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == BaseData.BASE_ONE) {
            //设置占位图
            imagePaths.set(position, new NormImage());
        } else {
            //先移除
            imagePaths.remove(imagePaths.get(position));
            if (!TextUtils.isEmpty(imagePaths.get(position).getImagePath()) ||
                    !TextUtils.isEmpty(imagePaths.get(position).getImageUrl())) {
                //占位图
                imagePaths.add(new NormImage());
            }
        }
        addImageAdapter.setNewData(imagePaths);
    }

    @OnClick({R.id.tv_auth_license_last, R.id.tv_auth_license_submit, R.id.tv_instance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_license_last:
                if (onAuthStepListener != null) {
                    onAuthStepListener.onAuthTwo(BASE_ONE, null);
                }
                break;
            case R.id.tv_auth_license_submit:
                if (initNextButton() && onAuthStepListener != null) {
                    for (int i = 0; i < imagePaths.size(); i++) {
                        String url = imagePaths.get(i).getImageUrl();
                        if (i == BASE_ZERO) {
                            doctorAuthBean.setCertFront(url);
                        } else if (i == BASE_ONE) {
                            doctorAuthBean.setCertBack(url);
                        }
                    }
                    onAuthStepListener.onAuthTwo(BASE_TWO, doctorAuthBean);
                }
                break;
            case R.id.tv_instance:
                startActivity(new Intent(getContext(), PhotoInstanceActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onMediaItemClick(int position) {
        switch (position) {
            case 0:
                permissionHelper.request(new String[]{Permission.CAMERA, Permission.STORAGE_WRITE});
                break;
            case 1:
                permissionHelper.request(new String[]{Permission.STORAGE_WRITE});
                break;
            default:
                break;
        }
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        int num = 2;
        for (NormImage normImage : imagePaths) {
            if (!TextUtils.isEmpty(normImage.getImageUrl())) {
                num--;
            }
        }
        MatisseUtils.open(this, true, num,false);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.UPLOAD_FILE) {
            String url = (String) response.getData();
            if (imagePaths.size() == 0 || TextUtils.isEmpty(imagePaths.get(0).getImageUrl())) {
                imagePaths.clear();
                NormImage normImage = new NormImage();
                normImage.setImageUrl(url);
                imagePaths.add(normImage);
                //占位
                imagePaths.add(new NormImage());
            } else {
                imagePaths.get(1).setImageUrl(url);
            }
            addImageAdapter.setNewData(imagePaths);
            if (currentUploadImgIndex == 0) {
                currentUploadImgIndex = 1;
                dealImgHandler.sendEmptyMessage(0);
            } else if (currentUploadImgIndex == 1) {
                currentUploadImgIndex = -1;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK_IMG:
                paths = Matisse.obtainPathResult(data);
                if (1 == paths.size()) {
                    NormImage normImage = new NormImage();
                    normImage.setImagePath(paths.get(0));
                    imagePaths.set(imagePaths.size() - 1, normImage);
                    uploadImage(new File(paths.get(0)));
                } else {
                    imagePaths.clear();
                    currentUploadImgIndex = 0;
                    dealImgHandler.sendEmptyMessage(0);
                }
                break;
            case RC_PICK_CAMERA:
                NormImage normImage = new NormImage();
                normImage.setImagePath(mCurrentPhotoPath);
                imagePaths.set(imagePaths.size() - 1, normImage);
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
            if (isSamePermission(Permission.STORAGE_WRITE, ((String[]) permissionName)[0])) {
                openPhoto();
            } else if (isSamePermission(Permission.CAMERA, ((String[]) permissionName)[0])) {
                //                openCamera();
                openPhoto();
            }
        }
    }

    private OnAuthStepListener onAuthStepListener;

    public void setOnAuthStepListener(OnAuthStepListener onAuthStepListener) {
        this.onAuthStepListener = onAuthStepListener;
    }
}
