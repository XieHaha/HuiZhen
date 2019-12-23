package com.yht.yihuantong.ui.auth.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yht.frame.api.DirHelper;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.DoctorAuthBean;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.data.bean.HospitalDepartChildBean;
import com.yht.frame.data.bean.HospitalTitleBean;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.data.type.DataDictionary;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.auth.SelectDepartActivity;
import com.yht.yihuantong.ui.auth.SelectHospitalByAuthActivity;
import com.yht.yihuantong.ui.auth.listener.OnAuthStepListener;
import com.yht.yihuantong.ui.dialog.DownDialog;
import com.yht.yihuantong.ui.dialog.listener.OnMediaItemClickListener;
import com.yht.yihuantong.ui.dialog.listener.OnTitleItemClickListener;
import com.yht.yihuantong.utils.FileUrlUtil;
import com.yht.yihuantong.utils.MatisseUtils;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @description 认证基础信息
 */
public class AuthBaseFragment extends BaseFragment implements OnMediaItemClickListener,
        OnTitleItemClickListener, RadioGroup.OnCheckedChangeListener {
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
    private DoctorAuthBean doctorAuthBean;
    /**
     * 当前选中科室
     */
    private HospitalDepartChildBean curDepart;
    private Uri cutFileUri;
    private File cutFile;
    private List<String> titleData = new ArrayList<>();
    ArrayList<NormImage> imagePaths = new ArrayList<>();
    /**
     * 已上传的头像URL
     */
    private String headerImageUrl;
    /**
     * 当前选中的科室position 一二级科室
     */
    private int positionOne = -1, positionTwo = -1;
    /**
     * 医院选择
     */
    public static final int REQUEST_CODE_HOSPITAL = 100;
    /**
     * 科室选择
     */
    public static final int REQUEST_CODE_DEPART = 200;
    /**
     * 头像更新
     */
    public static final int REQUEST_NEW_HEADER_IMAGE = 300;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_base;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        tvAuthBaseNext.setSelected(true);
        getDoctorTitle();
        //不为空代表已经提交过认证信息
        if (doctorAuthBean != null) {
            initPage();
        } else {
            doctorAuthBean = new DoctorAuthBean();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etAuthBaseName.setFilters(new InputFilter[]{emojiFilter});
        etAuthBaseName.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                String name = s.toString().trim();
                doctorAuthBean.setDoctorName(name);
                int mTextMaxlenght = 0;
                Editable editable = etAuthBaseName.getText();
                //得到最初字段的长度大小,用于光标位置的判断
                int selEndIndex = Selection.getSelectionEnd(editable);
                // 取出每个字符进行判断,如果是字母数字和标点符号则为一个字符加1,
                //如果是汉字则为两个字符
                for (int i = 0; i < name.length(); i++) {
                    char charAt = name.charAt(i);
                    //32-122包含了空格,大小写字母,数字和一些常用的符号,
                    //如果在这个范围内则算一个字符,
                    //如果不在这个范围比如是汉字的话就是两个字符
                    if (charAt >= 32 && charAt <= 122) {
                        mTextMaxlenght++;
                    } else {
                        mTextMaxlenght += 2;
                    }
                    // 当最大字符大于10时,进行字段的截取,并进行提示字段的大小
                    if (mTextMaxlenght > BaseData.BASE_NICK_NAME_LENGTH) {
                        // 截取最大的字段
                        String newStr = name.substring(0, i);
                        etAuthBaseName.setText(newStr);
                        // 得到新字段的长度值
                        editable = etAuthBaseName.getText();
                        int newLen = editable.length();
                        if (selEndIndex > newLen) {
                            selEndIndex = editable.length();
                        }
                        // 设置新光标所在的位置
                        Selection.setSelection(editable, selEndIndex);
                    }
                }
            }
        });
        rgSex.setOnCheckedChangeListener(this);
    }

    /**
     * 数据回填
     */
    private void initPage() {
        etAuthBaseName.setText(doctorAuthBean.getDoctorName());
        tvAuthBaseHospital.setText(doctorAuthBean.getLastApplyHospitalName());
        tvAuthBaseHospital.setSelected(true);
        tvAuthBaseDepart.setText(doctorAuthBean.getLastApplyDepartmentName());
        tvAuthBaseDepart.setSelected(true);
        tvAuthBaseTitle.setText(doctorAuthBean.getJobTitle());
        tvAuthBaseTitle.setSelected(true);
        if (doctorAuthBean.getDoctorSex() != BaseData.BASE_ZERO) {
            if (doctorAuthBean.getDoctorSex() == BaseData.BASE_MALE) {
                rbMale.setChecked(true);
            } else {
                rbFemale.setChecked(true);
            }
        }
        headerImageUrl = doctorAuthBean.getDoctorPhoto();
        imagePaths.clear();
        NormImage normImage = new NormImage();
        normImage.setImageUrl(headerImageUrl);
        imagePaths.add(normImage);
        Glide.with(this).load(FileUrlUtil.addTokenToUrl(headerImageUrl)).apply(GlideHelper.getOptions(BaseUtils.dp2px(getContext(), 4))).into(ivAuthBaseImg);
    }

    /**
     * 上传图片
     */
    private void uploadImage(File file) {
        RequestUtils.uploadImg(getContext(), loginBean.getToken(), file, this);
    }

    /**
     * 获取职称
     */
    private void getDoctorTitle() {
        RequestUtils.getDataByType(getContext(), loginBean.getToken(),
                DataDictionary.DATA_JOB_TITLE, this);
    }

    /**
     * 科室数据
     */
    private void initDepartData(boolean status) {
        if (status) {
            doctorAuthBean.setLastApplyDepartmentId(curDepart.getDepartmentId());
            tvAuthBaseDepart.setText(curDepart.getDepartmentName());
            tvAuthBaseDepart.setSelected(true);
        } else {
            doctorAuthBean.setLastApplyDepartmentId(0);
            tvAuthBaseDepart.setText(R.string.txt_select_hint);
            tvAuthBaseDepart.setSelected(false);
        }
    }

    /**
     * 判断
     */
    private boolean initNextButton() {
        if (TextUtils.isEmpty(doctorAuthBean.getDoctorPhoto())) {
            ToastUtil.toast(getContext(), R.string.txt_header_image_hint);
            return false;
        }
        if (TextUtils.isEmpty(doctorAuthBean.getDoctorName())) {
            ToastUtil.toast(getContext(), R.string.txt_input_name_hint);
            return false;
        }
        if (doctorAuthBean.getDoctorSex() == 0) {
            ToastUtil.toast(getContext(), R.string.txt_sex_hint);
            return false;
        }
        if (TextUtils.isEmpty(doctorAuthBean.getLastApplyHospitalName())) {
            ToastUtil.toast(getContext(), R.string.txt_hospital_hint);
            return false;
        }
        if (doctorAuthBean.getLastApplyDepartmentId() == 0) {
            ToastUtil.toast(getContext(), R.string.txt_depart_hint);
            return false;
        }
        if (TextUtils.isEmpty(doctorAuthBean.getJobTitle())) {
            ToastUtil.toast(getContext(), R.string.txt_title_hint);
            return false;
        }
        return true;
    }

    public void setDoctorAuthBean(DoctorAuthBean doctorAuthBean) {
        this.doctorAuthBean = doctorAuthBean;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == rbMale.getId()) {
            doctorAuthBean.setDoctorSex(BaseData.BASE_MALE);
        } else {
            doctorAuthBean.setDoctorSex(BaseData.BASE_FEMALE);
        }
    }

    @OnClick({R.id.layout_upload_img, R.id.layout_base_hospital, R.id.layout_base_depart,
            R.id.layout_base_title, R.id.tv_auth_base_next})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_upload_img:
                if (TextUtils.isEmpty(headerImageUrl)) {
                    permissionHelper.request(new String[]{Permission.CAMERA,
                            Permission.STORAGE_WRITE});

                } else {
                    //查看大图
                    intent = new Intent(getContext(), ImagePreviewActivity.class);
                    intent.putExtra(ImagePreviewActivity.INTENT_URLS, imagePaths);
                    intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true);
                    startActivityForResult(intent, REQUEST_NEW_HEADER_IMAGE);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.anim_fade_in, R.anim.keep);
                }
                break;
            case R.id.layout_base_hospital:
                intent = new Intent(getContext(), SelectHospitalByAuthActivity.class);
                startActivityForResult(intent, REQUEST_CODE_HOSPITAL);
                break;
            case R.id.layout_base_depart:
                if (!TextUtils.isEmpty(doctorAuthBean.getLastApplyHospitalName())) {
                    intent = new Intent(getContext(), SelectDepartActivity.class);
                    intent.putExtra(CommonData.KEY_HOSPITAL_CODE,
                            doctorAuthBean.getLastApplyHospitalCode());
                    intent.putExtra(CommonData.KEY_DEPART_POSITION, positionOne);
                    intent.putExtra(CommonData.KEY_DEPART_CHILD_POSITION, positionTwo);
                    startActivityForResult(intent, REQUEST_CODE_DEPART);
                } else {
                    ToastUtil.toast(getContext(), R.string.txt_select_hospital);
                }
                break;
            case R.id.layout_base_title:
                new DownDialog(getContext()).setData(titleData).setCurPosition(titleData.indexOf(doctorAuthBean.getJobTitle())).setOnTitleItemClickListener(this).show();
                break;
            case R.id.tv_auth_base_next:
                if (initNextButton() && onAuthStepListener != null) {
                    onAuthStepListener.onAuthOne(doctorAuthBean);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 相机 相册回调
     */
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
     * 职称
     */
    @Override
    public void onTitleItemClick(int position) {
        doctorAuthBean.setJobTitle(titleData.get(position));
        tvAuthBaseTitle.setText(titleData.get(position));
        tvAuthBaseTitle.setSelected(true);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case UPLOAD_FILE:
                headerImageUrl = (String) response.getData();
                imageUploadSuccess();
                break;
            case DATA_JOB_TITLE:
                ArrayList<HospitalTitleBean> list =
                        (ArrayList<HospitalTitleBean>) response.getData();
                titleData = new ArrayList<>();
                if (list != null && list.size() > 0) {
                    for (HospitalTitleBean bean : list) {
                        titleData.add(bean.getName());
                    }
                }
                break;
            default:
                break;
        }
    }

    private void imageUploadSuccess() {
        //图片上传成功
        Glide.with(this).load(headerImageUrl).apply(GlideHelper.getOptions(BaseUtils.dp2px(getContext(), 4))).into(ivAuthBaseImg);
        imagePaths.clear();
        NormImage normImage = new NormImage();
        normImage.setImageUrl(headerImageUrl);
        imagePaths.add(normImage);
        doctorAuthBean.setDoctorPhoto(headerImageUrl);
    }


    /**
     * 打开图片库
     */
    private void openPhoto() {
        MatisseUtils.open(this, true, 1, true);
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
                headerImageCallBack(data);
                break;
            case RC_CROP_IMG:
                uploadImage(cutFile);
                break;
            case REQUEST_NEW_HEADER_IMAGE:
                headerImageUrl = data.getStringExtra(CommonData.KEY_PUBLIC_STRING);
                imageUploadSuccess();
                break;
            case REQUEST_CODE_HOSPITAL:
                HospitalBean bean =
                        (HospitalBean) data.getSerializableExtra(CommonData.KEY_HOSPITAL_BEAN);
                doctorAuthBean.setLastApplyHospitalName(bean.getHospitalName());
                doctorAuthBean.setLastApplyHospitalCode(bean.getHospitalCode());
                tvAuthBaseHospital.setText(bean.getHospitalName());
                tvAuthBaseHospital.setSelected(true);
                //重新选择医院后  需初始化科室
                initDepartData(false);
                break;
            //科室选择
            case REQUEST_CODE_DEPART:
                curDepart =
                        (HospitalDepartChildBean) data.getSerializableExtra(CommonData.KEY_DEPART_BEAN);
                positionOne = data.getIntExtra(CommonData.KEY_DEPART_POSITION, -1);
                positionTwo = data.getIntExtra(CommonData.KEY_DEPART_CHILD_POSITION, -1);
                initDepartData(true);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void headerImageCallBack(Intent data) {
        List<Uri> uris = Matisse.obtainResult(data);
        List<String> paths = Matisse.obtainPathResult(data);
        if (null != paths && 0 != paths.size()) {
            String fileName = "corp" + System.currentTimeMillis() + ".jpg";
            cutFile = new File(DirHelper.getPathCache(), fileName);
            startCutImg(uris.get(0), Uri.fromFile(cutFile));
        }
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

    private InputFilter emojiFilter = new InputFilter() {
        private String filterImoji = "[^a-zA-Z ·.\u4E00-\u9FA5]";
        Pattern emoji = Pattern.compile(filterImoji,
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast(getContext(), R.string.txt_not_support_input);
                return "";
            }
            return null;
        }
    };
    private OnAuthStepListener onAuthStepListener;

    public void setOnAuthStepListener(OnAuthStepListener onAuthStepListener) {
        this.onAuthStepListener = onAuthStepListener;
    }
}
