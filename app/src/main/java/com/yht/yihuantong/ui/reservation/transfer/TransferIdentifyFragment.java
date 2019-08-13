package com.yht.yihuantong.ui.reservation.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yht.frame.api.LitePalHelper;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.ReserveTransferBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.SearchPatientAdapter;
import com.yht.yihuantong.ui.transfer.listener.OnTransferListener;
import com.yht.yihuantong.utils.text.BankCardTextWatcher;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 身份确认
 */
public class TransferIdentifyFragment extends BaseFragment
        implements View.OnFocusChangeListener, AdapterView.OnItemClickListener {
    @BindView(R.id.et_patient_name)
    SuperEditText etPatientName;
    @BindView(R.id.et_patient_id_card)
    SuperEditText etPatientIdCard;
    @BindView(R.id.tv_identify_next)
    TextView tvIdentifyNext;
    @BindView(R.id.list_view)
    ListView listView;
    private String name, idCard;
    /**
     * 搜索候选
     */
    private SearchPatientAdapter searchPatientAdapter;
    /**
     * 当前患者
     */
    private PatientBean patientBean;
    /**
     * 搜索结果
     */
    private List<PatientBean> searchPatients = new ArrayList<>();
    /**
     * 当前预约转诊信息
     */
    private ReserveTransferBean reverseTransferBean;
    /**
     * 扫码
     */
    private static final int REQUEST_CODE_SCAN = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_identify;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        BankCardTextWatcher.bind(etPatientIdCard, this);
        searchPatientAdapter = new SearchPatientAdapter(getContext());
        listView.setOnItemClickListener(this);
        listView.setAdapter(searchPatientAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        etPatientIdCard.setOnFocusChangeListener(this);
        etPatientName.setFilters(new InputFilter[] { emojiFilter });
        etPatientName.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString().trim();
                if (!TextUtils.isEmpty(idCard) && !TextUtils.isEmpty(s)) {
                    tvIdentifyNext.setSelected(true);
                }
                else {
                    tvIdentifyNext.setSelected(false);
                }
                int mTextMaxlenght = 0;
                Editable editable = etPatientName.getText();
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
                    }
                    else {
                        mTextMaxlenght += 2;
                    }
                    // 当最大字符大于10时,进行字段的截取,并进行提示字段的大小
                    if (mTextMaxlenght > BaseData.BASE_NICK_NAME_LENGTH) {
                        // 截取最大的字段
                        String newStr = name.substring(0, i);
                        etPatientName.setText(newStr);
                        // 得到新字段的长度值
                        editable = etPatientName.getText();
                        int newLen = editable.length();
                        if (selEndIndex > newLen) {
                            selEndIndex = editable.length();
                        }
                        // 设置新光标所在的位置
                        Selection.setSelection(editable, selEndIndex);
                    }
                }
                searchPatient(etPatientName.getText().toString().trim());
                initNextButton();
            }
        });
    }

    public void setReverseTransferBean(ReserveTransferBean reverseTransferBean) {
        this.reverseTransferBean = reverseTransferBean;
    }

    /**
     * 患者验证
     */
    private void verifyPatient() {
        RequestUtils.verifyPatient(getContext(), loginBean.getToken(), idCard, this);
    }

    /**
     * 查询患者是否存在未完成的转诊单
     */
    private void getPatientExistTransfer() {
        RequestUtils.getPatientExistTransfer(getContext(), loginBean.getToken(), patientBean.getCode(), this);
    }

    /**
     * 患者搜索
     */
    private void searchPatient(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            searchPatients = LitePalHelper.findPatients(tag);
            initSearchList(searchPatients, tag);
        }
        else {
            initSearchList(null, "");
        }
    }

    private void initSearchList(List<PatientBean> list, String tag) {
        if (list != null && list.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            searchPatientAdapter.setSearchKey(tag);
            searchPatientAdapter.setList(list);
        }
        else {
            searchPatientAdapter.setSearchKey(tag);
            listView.setVisibility(View.GONE);
        }
    }

    @OnClick({ R.id.tv_identify_next, R.id.layout_scan })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_identify_next:
                if (tvIdentifyNext.isSelected()) {
                    //已经校验过  不在校验
                    if (reverseTransferBean != null && idCard.equals(reverseTransferBean.getPatientIdCardNo())) {
                        if (onTransferListener != null) {
                            onTransferListener.onTransferStepOne(reverseTransferBean);
                        }
                    }
                    else {
                        verifyPatient();
                    }
                }
                break;
            case R.id.layout_scan:
                permissionHelper.request(new String[] { Permission.CAMERA });
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus && etPatientIdCard != null) {
            idCard = etPatientIdCard.getText().toString().replace(" ", "");
            if (!TextUtils.isEmpty(idCard) && !BaseUtils.isCardNum(idCard)) {
                ToastUtil.toast(getContext(), R.string.toast_id_card_error);
            }
            initNextButton();
        }
    }

    /**
     * 身份证输入框监听
     */
    public void onCardTextChanged(CharSequence s, int start, int before, int count) {
        idCard = s.toString().replace(" ", "");
        initNextButton();
    }

    /**
     * 搜索、扫码数据回填
     */
    private void resultData(PatientBean bean) {
        etPatientName.setText(name = bean.getName());
        etPatientName.setSelection(name.length());
        etPatientIdCard.setText(idCard = bean.getIdCard());
        initNextButton();
    }

    /**
     * 开启扫一扫
     */
    private void openScan() {
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.keep, R.anim.keep);
    }

    private void initNextButton() {
        if (!TextUtils.isEmpty(name) && BaseUtils.isCardNum(idCard)) {
            tvIdentifyNext.setSelected(true);
        }
        else {
            tvIdentifyNext.setSelected(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        resultData(searchPatients.get(position));
        searchPatient("");
        hideSoftInputFromWindow(getContext(), etPatientName);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case VERIFY_PATIENT:
                patientBean = (PatientBean)response.getData();
                //新用户
                if (patientBean == null) {
                    if (onTransferListener != null) {
                        reverseTransferBean = new ReserveTransferBean();
                        reverseTransferBean.setPatientName(name);
                        reverseTransferBean.setPatientIdCardNo(idCard);
                        onTransferListener.onTransferStepOne(reverseTransferBean);
                    }
                }
                else {
                    if (!name.equals(patientBean.getName())) {
                        ToastUtil.toast(getContext(), R.string.txt_identity_information_error);
                    }
                    else {
                        //校验患者是否有存在的待处理转诊单
                        getPatientExistTransfer();
                    }
                }
                break;
            case GET_PATIENT_EXIST_TRANSFER:
                boolean exist = (boolean)response.getData();
                if (exist) {
                    ToastUtil.toast(getContext(), R.string.txt_patient_exist_transfer);
                }
                else {
                    if (onTransferListener != null) {
                        transferData();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                ToastUtil.toast(getContext(), content);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            openScan();
        }
    }

    /**
     * 转诊数据
     */
    private void transferData() {
        reverseTransferBean = new ReserveTransferBean();
        reverseTransferBean.setPatientName(name);
        reverseTransferBean.setPatientIdCardNo(idCard);
        reverseTransferBean.setIsBind(patientBean.getIsBind());
        reverseTransferBean.setPatientCode(patientBean.getCode());
        reverseTransferBean.setPatientMobile(patientBean.getMobile());
        reverseTransferBean.setPatientAge(patientBean.getAge());
        reverseTransferBean.setSex(patientBean.getSex());
        reverseTransferBean.setPastHistory(patientBean.getPast());
        reverseTransferBean.setFamilyHistory(patientBean.getFamily());
        reverseTransferBean.setAllergyHistory(patientBean.getAllergy());
        onTransferListener.onTransferStepOne(reverseTransferBean);
    }

    InputFilter emojiFilter = new InputFilter() {
        private String filterImoji = "[^a-zA-Z ·.\u4E00-\u9FA5]";
        Pattern emoji = Pattern.compile(filterImoji, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast(getContext(), "不支持输入");
                return "";
            }
            return null;
        }
    };
    private OnTransferListener onTransferListener;

    public void setOnTransferListener(OnTransferListener onTransferListener) {
        this.onTransferListener = onTransferListener;
    }
}
