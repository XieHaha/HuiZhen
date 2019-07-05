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
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import com.yht.frame.data.base.DoctorInfoBean;
import com.yht.frame.data.base.HospitalBean;
import com.yht.frame.data.base.HospitalDepartChildBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.auth.SelectDepartActivity;
import com.yht.yihuantong.ui.auth.SelectHospitalByAuthActivity;
import com.yht.yihuantong.ui.auth.listener.OnAuthStepListener;
import com.yht.yihuantong.ui.dialog.DownDialog;
import com.yht.yihuantong.ui.dialog.listener.OnMediaItemClickListener;
import com.yht.yihuantong.ui.dialog.listener.OnTitleItemClickListener;
import com.yht.yihuantong.utils.glide.GlideHelper;
import com.yht.yihuantong.utils.glide.ImageUrlUtil;
import com.yht.yihuantong.utils.glide.MatisseUtils;
import com.zhihu.matisse.Matisse;

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
public class AuthBaseFragment extends BaseFragment
        implements OnMediaItemClickListener, OnTitleItemClickListener, RadioGroup.OnCheckedChangeListener {
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
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    /**
     * 上传数据mondle
     */
    private DoctorInfoBean doctorInfoBean;
    /**
     * 当前选中的医院
     */
    private HospitalBean curHospital;
    private HospitalDepartChildBean curDepart;
    private Uri cutFileUri;
    private File cameraTempFile, cutFile;
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
    public static final int REQUEST_CODE_DEPART = 200;

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
        //不为空代表已经提交过认证信息
        if (doctorInfoBean != null) {
            initPage();
        }
        else {
            doctorInfoBean = new DoctorInfoBean();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etAuthBaseName.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                doctorInfoBean.setDoctorName(s.toString());
                initNextButton();
            }
        });
        rgSex.setOnCheckedChangeListener(this);
    }

    /**
     * 数据回填
     */
    private void initPage() {
        etAuthBaseName.setText(doctorInfoBean.getDoctorName());
        tvAuthBaseHospital.setText(doctorInfoBean.getDirectHospitalName());
        tvAuthBaseHospital.setSelected(true);
        tvAuthBaseDepart.setText(doctorInfoBean.getDirectDepartmentName());
        tvAuthBaseDepart.setSelected(true);
        tvAuthBaseTitle.setText(doctorInfoBean.getJobTitle());
        tvAuthBaseTitle.setSelected(true);
        if (doctorInfoBean.getDoctorSex() == BaseData.BASE_ONE) {
            rbMale.setChecked(true);
        }
        else {
            rbFemale.setChecked(true);
        }
        Glide.with(this)
             .load(ImageUrlUtil.append(doctorInfoBean.getDoctorPhoto()))
             .apply(GlideHelper.getOptionsP(BaseUtils.dp2px(getContext(), 4)))
             .into(ivAuthBaseImg);
        initNextButton();
    }

    /**
     * 上传图片
     *
     * @param file
     */
    private void uploadImage(File file) {
        RequestUtils.uploadImg(getContext(), loginBean.getToken(), file, this);
    }

    /**
     * 判断
     */
    private void initNextButton() {
        if (TextUtils.isEmpty(doctorInfoBean.getDoctorPhoto()) || TextUtils.isEmpty(doctorInfoBean.getDoctorName()) ||
            TextUtils.isEmpty(doctorInfoBean.getDirectHospitalName()) ||
            TextUtils.isEmpty(doctorInfoBean.getJobTitle()) || doctorInfoBean.getDoctorSex() == 0 ||
            doctorInfoBean.getDirectDepartmentId() == 0) {
            tvAuthBaseNext.setSelected(false);
        }
        else {
            tvAuthBaseNext.setSelected(true);
        }
    }

    public void setDoctorInfoBean(DoctorInfoBean doctorInfoBean) {
        this.doctorInfoBean = doctorInfoBean;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == rbMale.getId()) {
            doctorInfoBean.setDoctorSex(BaseData.BASE_MALE);
        }
        else {
            doctorInfoBean.setDoctorSex(BaseData.BASE_FEMALE);
        }
        initNextButton();
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
                intent = new Intent(getContext(), SelectHospitalByAuthActivity.class);
                startActivityForResult(intent, REQUEST_CODE_HOSPITAL);
                break;
            case R.id.layout_base_depart:
                if (curHospital != null) {
                    intent = new Intent(getContext(), SelectDepartActivity.class);
                    intent.putExtra(CommonData.KEY_HOSPITAL_CODE, curHospital.getHospitalCode());
                    startActivityForResult(intent, REQUEST_CODE_DEPART);
                }
                else {
                    ToastUtil.toast(getContext(), R.string.txt_select_hospital);
                }
                break;
            case R.id.layout_base_title:
                new DownDialog(getContext()).setData(titleDatas)
                                            .setCurPosition(titleDatas.indexOf(doctorInfoBean.getJobTitle()))
                                            .setOnTitleItemClickListener(this)
                                            .show();
                break;
            case R.id.tv_auth_base_next:
                if (tvAuthBaseNext.isSelected() && onAuthStepListener != null) {
                    onAuthStepListener.onAuthOne(doctorInfoBean);
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
        doctorInfoBean.setJobTitle(titleDatas.get(position));
        tvAuthBaseTitle.setText(titleDatas.get(position));
        tvAuthBaseTitle.setSelected(true);
        initNextButton();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        //图片上传成功
        if (task == Tasks.UPLOAD_FILE) {
            Glide.with(this)
                 .load(cutFileUri)
                 .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(getContext(), 4)))
                 .into(ivAuthBaseImg);
            doctorInfoBean.setDoctorPhoto((String)response.getData());
            initNextButton();
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

    /**
     * 图片裁剪
     */
    private void startCutImg(Uri uri, Uri cutUri) {
        cutFileUri = cutUri;
        startActivityForResult(getCutImageIntent(uri, cutFileUri), RC_CROP_IMG);
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
                    cutFile = new File(DirHelper.getPathCache(), fileName);
                    startCutImg(uris.get(0), Uri.fromFile(cutFile));
                }
                break;
            case RC_PICK_CAMERA:
                cameraTempFile = new File(mCurrentPhotoPath);
                String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                cutFile = new File(DirHelper.getPathCache(), fileName);
                startCutImg(mCurrentPhotoUri, Uri.fromFile(cutFile));
                break;
            case RC_CROP_IMG:
                uploadImage(cutFile);
                break;
            //医院选择
            case REQUEST_CODE_HOSPITAL:
                curHospital = (HospitalBean)data.getSerializableExtra(CommonData.KEY_HOSPITAL_BEAN);
                doctorInfoBean.setDirectHospitalName(curHospital.getHospitalName());
                tvAuthBaseHospital.setText(curHospital.getHospitalName());
                tvAuthBaseHospital.setSelected(true);
                initNextButton();
                break;
            //科室选择
            case REQUEST_CODE_DEPART:
                curDepart = (HospitalDepartChildBean)data.getSerializableExtra(CommonData.KEY_DEPART_BEAN);
                doctorInfoBean.setDirectDepartmentId(curDepart.getDepartmentId());
                tvAuthBaseDepart.setText(curDepart.getDepartmentName());
                tvAuthBaseDepart.setSelected(true);
                initNextButton();
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