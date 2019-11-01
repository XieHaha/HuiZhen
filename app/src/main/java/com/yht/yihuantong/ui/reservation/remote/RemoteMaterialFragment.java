package com.yht.yihuantong.ui.reservation.remote;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.data.bean.ReserveRemoteBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScalingUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.MultiLineEditText;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.gridview.AutoGridView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.remote.listener.OnRemoteListener;
import com.yht.yihuantong.ui.reservation.PastHistoryActivity;
import com.yht.yihuantong.ui.reservation.PastHistoryUtil;
import com.yht.yihuantong.utils.MatisseUtils;
import com.yht.yihuantong.utils.text.BankCardTextWatcher;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @description 完善资料
 */
public class RemoteMaterialFragment extends BaseFragment
        implements View.OnFocusChangeListener, AdapterView.OnItemClickListener, AutoGridView.OnDeleteClickListener {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.et_age)
    SuperEditText etAge;
    @BindView(R.id.tv_id_card)
    SuperEditText tvIdCard;
    @BindView(R.id.et_phone)
    SuperEditText etPhone;
    @BindView(R.id.et_diagnosis)
    MultiLineEditText etDiagnosis;
    @BindView(R.id.tv_diagnosis_num)
    TextView tvDiagnosisNum;
    @BindView(R.id.tv_material_next)
    TextView tvMaterialNext;
    @BindView(R.id.layout_past_medical_his)
    LinearLayout layoutPastMedicalHis;
    @BindView(R.id.layout_family_medical_his)
    LinearLayout layoutFamilyMedicalHis;
    @BindView(R.id.layout_allergies)
    LinearLayout layoutAllergies;
    @BindView(R.id.tv_past_medical)
    TextView tvPastMedical;
    @BindView(R.id.tv_family_medical)
    TextView tvFamilyMedical;
    @BindView(R.id.tv_allergies)
    TextView tvAllergies;
    @BindView(R.id.layout_past)
    RelativeLayout layoutPast;
    @BindView(R.id.et_description)
    MultiLineEditText etDescription;
    @BindView(R.id.tv_description_num)
    TextView tvDescriptionNum;
    @BindView(R.id.et_purpose)
    MultiLineEditText etPurpose;
    @BindView(R.id.tv_purpose_num)
    TextView tvPurposeNum;
    @BindView(R.id.auto_grid_view)
    AutoGridView autoGridView;
    /**
     * 既往史数据
     */
    private ArrayList<String> pastHistoryData;
    /**
     * 当前远程会诊数据
     */
    private ReserveRemoteBean reserveRemoteBean;
    /**
     * 编辑既往史
     */
    public static final int REQUEST_CODE_PAST_HISTORY = 100;
    /**
     * 基础信息
     */
    private int sex;
    private String age, phone, pastMedicalHis = "", familyMedicalHis = "", allergiesHis = "", diagnosisHis = "", description = "", purpose = "";
    /**
     * 二次编辑 是否清空所有已填数据
     */
    private boolean clearAll;
    /**
     * 已上传图片
     */
    private ArrayList<NormImage> imagePaths = new ArrayList<>();
    /**
     * 图片临时数据
     */
    private List<String> paths;
    private int currentUploadImgIndex = -1;
    private Handler dealImgHandler = new Handler(msg -> {
        //图片显示完开始上传图片
        showLoadingView();
        currentUploadImgIndex = 0;
        uploadImage(new File(paths.get(currentUploadImgIndex)));
        return true;
    });

    @Override
    public int getLayoutID() {
        return R.layout.fragment_remote_material;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPatientBaseData();
    }

    public ReserveRemoteBean getReserveRemoteBean() {
        return reserveRemoteBean;
    }

    public void setReserveRemoteBean(ReserveRemoteBean bean) {
        clearAllCheckData(bean);
        this.reserveRemoteBean = bean;
    }

    @Override
    public void initListener() {
        super.initListener();
        autoGridView.setOnItemClickListener(this);
        autoGridView.setOnDeleteClickListener(this);
        initEditListener();
    }

    /**
     * 上传图片
     */
    private void uploadImage(File file) {
        ScalingUtils.resizePic(getContext(), file.getAbsolutePath());
        RequestUtils.uploadImgWaterMark(getContext(), loginBean.getToken(), file, false, this);
    }

    /**
     * 姓名和身份证处理  老用户获取，新用户根据身份证计算
     */
    private void initPatientBaseData() {
        if (clearAll) {
            etPhone.setText("");
            etDiagnosis.setText("");
            etDescription.setText("");
            etPurpose.setText("");
            pastMedicalHis = "";
            familyMedicalHis = "";
            allergiesHis = "";
        }
        initCheckData();
    }

    private void initCheckData() {
        if (reserveRemoteBean != null) {
            BankCardTextWatcher.bind(tvIdCard, this);
            if (!TextUtils.isEmpty(reserveRemoteBean.getPatientCode())) {
                //老用户
                editStatus(false);
                age = String.valueOf(reserveRemoteBean.getPatientAge());
                sex = reserveRemoteBean.getPatientSex();
                phone = reserveRemoteBean.getPatientMobile();
                etPhone.setText(phone);
                pastMedicalHis = reserveRemoteBean.getPast();
                familyMedicalHis = reserveRemoteBean.getFamily();
                allergiesHis = reserveRemoteBean.getAllergy();
            }
            else {
                //新用户
                editStatus(true);
                age = BaseUtils.getAgeByCard(reserveRemoteBean.getPatientIdCard());
                sex = BaseUtils.getSexByCard(reserveRemoteBean.getPatientIdCard());
                reserveRemoteBean.setPatientSex(sex);
                if (!TextUtils.isEmpty(age)) {
                    reserveRemoteBean.setPatientAge(Integer.valueOf(age));
                }
                else {
                    reserveRemoteBean.setPatientAge(0);
                }
            }
            tvName.setText(reserveRemoteBean.getPatientName());
            tvIdCard.setText(reserveRemoteBean.getPatientIdCard());
            etAge.setText(age);
            if (sex == BaseData.BASE_MALE) {
                rbMale.setChecked(true);
            }
            else {
                rbFemale.setChecked(true);
            }
            //病情描述
            etDescription.setText(description = reserveRemoteBean.getDescIll());
            //初步诊断
            etDiagnosis.setText(diagnosisHis = reserveRemoteBean.getInitResult());
            //会珍目的
            etPurpose.setText(purpose = reserveRemoteBean.getDestination());
            //既往史
            pastHistoryData = new ArrayList<>();
            pastHistoryData.add(pastMedicalHis);
            pastHistoryData.add(familyMedicalHis);
            pastHistoryData.add(allergiesHis);
            initPastHistory();
            //病情描述
            initDescription();
            //诊断史
            initDiagnosis();
            //会珍目的
            initPurpose();
            initNextButton();
        }
    }

    /**
     * 涉及到数据回填逻辑，如果更改了居民，需要清空原有已填写数据
     */
    private void clearAllCheckData(ReserveRemoteBean bean) {
        if (reserveRemoteBean == null || bean == null) {
            clearAll = false;
        }
        else {
            clearAll = !reserveRemoteBean.getPatientName().equals(bean.getPatientName()) ||
                       !reserveRemoteBean.getPatientIdCard().equals(bean.getPatientIdCard());
        }
    }

    /**
     * @param mode 是否可以编辑基本信息
     */
    private void editStatus(boolean mode) {
        //老用户未绑定手机号可以修改
        if (!mode && !BaseData.BASE_STRING_ONE_TAG.equals(reserveRemoteBean.getIsBind())) {
            etPhone.setFocusable(true);
            etPhone.setFocusableInTouchMode(true);
        }
        else {
            etPhone.setFocusable(mode);
            etPhone.setFocusableInTouchMode(mode);
        }
        etAge.setFocusable(mode);
        etAge.setFocusableInTouchMode(mode);
        rbFemale.setClickable(mode);
        rbMale.setClickable(mode);
    }

    private void initEditListener() {
        etAge.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                age = s.toString().trim();
                initNextButton();
                if (!TextUtils.isEmpty(age)) {
                    reserveRemoteBean.setPatientAge(Integer.valueOf(age));
                }
                else {
                    reserveRemoteBean.setPatientAge(0);
                }
            }
        });
        etPhone.setOnFocusChangeListener(this);
        etPhone.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                phone = s.toString().trim();
                if (BaseUtils.isMobileNumber(phone)) {
                    reserveRemoteBean.setPatientMobile(phone);
                }
                //判断手机号和诊断史
                initNextButton();
            }
        });
        etDescription.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                description = s.toString().trim();
                initNextButton();
                initDescription();
                //病情描述
                reserveRemoteBean.setDescIll(description);
            }
        });
        etDiagnosis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                diagnosisHis = s.toString().trim();
                initNextButton();
                initDiagnosis();
                //初步诊断
                reserveRemoteBean.setInitResult(diagnosisHis);
            }
        });
        etPurpose.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                purpose = s.toString().trim();
                initNextButton();
                initPurpose();
                //会诊目的
                reserveRemoteBean.setDestination(purpose);
            }
        });
    }

    /**
     * 既往史
     */
    private void initPastHistory() {
        tvPastMedical.setText(PastHistoryUtil.getPastMedical(getContext(), pastHistoryData.get(0)));
        tvFamilyMedical.setText(PastHistoryUtil.getFamilyMedical(getContext(), pastHistoryData.get(1)));
        tvAllergies.setText(PastHistoryUtil.getAllergies(getContext(), pastHistoryData.get(2)));
    }

    /**
     * 诊断内容
     */
    private void initDescription() {
        tvDescriptionNum.setText(String.format(getString(R.string.txt_calc_num), description.length()));
        etDescription.setSelection(description.length());
    }

    /**
     * 诊断内容
     */
    private void initDiagnosis() {
        tvDiagnosisNum.setText(String.format(getString(R.string.txt_calc_num), diagnosisHis.length()));
        etDiagnosis.setSelection(diagnosisHis.length());
    }

    /**
     * 诊断内容
     */
    private void initPurpose() {
        tvPurposeNum.setText(String.format(getString(R.string.txt_calc_num), purpose.length()));
        etPurpose.setSelection(purpose.length());
    }

    /**
     * 上传的资料
     */
    private void initPatientResource() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < imagePaths.size(); i++) {
            NormImage image = imagePaths.get(i);
            builder.append(image.getImageUrl());
            if (imagePaths.size() - 1 > i) {
                builder.append(",");
            }
        }
        reserveRemoteBean.setPatientResource(builder.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (imagePaths.size() > position) {
            //查看大图
            Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.INTENT_URLS, imagePaths);
            intent.putExtra(ImagePreviewActivity.INTENT_POSITION, position);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.anim_fade_in, R.anim.keep);
        }
        else {
            permissionHelper.request(new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
        }
    }

    @Override
    public void onDeleteClick(int position) {
        imagePaths.remove(position);
        autoGridView.updateImg(imagePaths, true);
        initPatientResource();
        initNextButton();
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        MatisseUtils.open(this, true, BaseData.BASE_IMAGE_SIZE_MAX - imagePaths.size());
    }

    /**
     * next
     */
    private void initNextButton() {
        //判断手机号和诊断史
        if (BaseUtils.isMobileNumber(phone) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(diagnosisHis) &&
            !TextUtils.isEmpty(purpose) && !TextUtils.isEmpty(age)) {
            tvMaterialNext.setSelected(true);
        }
        else {
            tvMaterialNext.setSelected(false);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && etPhone != null) {
            if (!BaseUtils.isMobileNumber(phone)) {
                ToastUtil.toast(getContext(), R.string.toast_phone_error);
            }
        }
    }

    @OnClick({
            R.id.tv_material_next, R.id.layout_past })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_past:
                Intent intent = new Intent(getContext(), PastHistoryActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, pastHistoryData);
                startActivityForResult(intent, REQUEST_CODE_PAST_HISTORY);
                break;
            case R.id.tv_material_next:
                if (tvMaterialNext.isSelected() && onRemoteListener != null) {
                    onRemoteListener.onRemoteStepTwo(reserveRemoteBean);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case UPLOAD_FILE:
                String url = (String)response.getData();
                NormImage normImage = new NormImage();
                normImage.setImageUrl(url);
                imagePaths.add(normImage);
                autoGridView.updateImg(imagePaths, true);
                if (paths.size() - 1 > currentUploadImgIndex) {
                    currentUploadImgIndex++;
                    uploadImage(new File(paths.get(currentUploadImgIndex)));
                }
                else {
                    //上传完后赋值
                    initPatientResource();
                    closeLoadingView();
                }
                initNextButton();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PAST_HISTORY) {
            pastHistoryData = data.getStringArrayListExtra(CommonData.KEY_PUBLIC);
            reserveRemoteBean.setPast(pastHistoryData.get(0));
            reserveRemoteBean.setFamily(pastHistoryData.get(1));
            reserveRemoteBean.setAllergy(pastHistoryData.get(2));
            initNextButton();
        }
        else if (requestCode == RC_PICK_IMG) {
            paths = Matisse.obtainPathResult(data);
            if (paths != null && paths.size() > 0) {
                dealImgHandler.sendEmptyMessage(0);
            }
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0])) {
                openPhoto();
            }
        }
    }

    private OnRemoteListener onRemoteListener;

    public void setOnRemoteListener(OnRemoteListener onRemoteListener) {
        this.onRemoteListener = onRemoteListener;
    }
}
