package com.yht.yihuantong.ui.remote;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.RemoteAdviceBean;
import com.yht.frame.data.bean.RemoteDetailBean;
import com.yht.frame.data.type.ConsultationStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.FileUrlUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 远程会诊详情
 */
public class RemoteDetailActivity extends BaseActivity implements ConsultationStatus {
    @BindView(R.id.iv_patient_img)
    ImageView ivPatientImg;
    @BindView(R.id.tv_patient_name)
    TextView tvPatientName;
    @BindView(R.id.tv_patient_age)
    TextView tvPatientAge;
    @BindView(R.id.tv_patient_sex)
    TextView tvPatientSex;
    @BindView(R.id.iv_check_status)
    ImageView ivCheckStatus;
    @BindView(R.id.tv_initiate_time)
    TextView tvInitiateTime;
    @BindView(R.id.tv_initiate_doctor)
    TextView tvInitiateDoctor;
    @BindView(R.id.tv_initiate_depart)
    TextView tvInitiateDepart;
    @BindView(R.id.tv_initiate_hospital)
    TextView tvInitiateHospital;
    @BindView(R.id.tv_condition_description)
    TextView tvConditionDescription;
    @BindView(R.id.tv_check_diagnosis_top)
    TextView tvCheckDiagnosisTop;
    @BindView(R.id.tv_consultation_purpose)
    TextView tvConsultationPurpose;
    @BindView(R.id.tv_consultation_status)
    TextView tvConsultationStatus;
    @BindView(R.id.tv_consultation_time)
    TextView tvConsultationTime;
    @BindView(R.id.list_view)
    FullListView listView;
    @BindView(R.id.layout_advice)
    LinearLayout layoutAdvice;
    /**
     * id
     */
    private String orderNo;
    /**
     * 详情数据
     */
    private RemoteDetailBean remoteDetailBean;
    private AdviceAdapter adviceAdapter;
    /**
     * 会诊意见
     */
    private ArrayList<RemoteAdviceBean> adviceList = new ArrayList<>();

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_remote_detail;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
        adviceAdapter = new AdviceAdapter();
        listView.setAdapter(adviceAdapter);
    }

    @Override
    public void fillNetWorkData() {
        getRemoteDetail();
    }

    private void getRemoteDetail() {
        RequestUtils.getRemoteDetail(this, loginBean.getToken(), orderNo, this);
    }

    private void bindData() {
        if (remoteDetailBean == null) { return; }
        bindAdviceData();
        Glide.with(this)
             .load(FileUrlUtil.addTokenToUrl(remoteDetailBean.getPatientPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivPatientImg);
        tvPatientName.setText(remoteDetailBean.getPatientName());
        tvPatientSex.setText(remoteDetailBean.getSex() == BaseData.BASE_ONE
                             ? getString(R.string.txt_sex_male)
                             : getString(R.string.txt_sex_female));
        tvPatientAge.setText(String.valueOf(remoteDetailBean.getPatientAge()));
        tvInitiateTime.setText(BaseUtils.formatDate(remoteDetailBean.getStartAt(), BaseUtils.YYYY_MM_DD_HH_MM_SS));
        tvInitiateDoctor.setText(remoteDetailBean.getSourceDoctorName());
        tvInitiateDepart.setText(remoteDetailBean.getSourceHospitalDepartmentName());
        tvInitiateHospital.setText(remoteDetailBean.getSourceHospitalName());
        tvConditionDescription.setText(remoteDetailBean.getDescIll());
        tvCheckDiagnosisTop.setText(remoteDetailBean.getInitResult());
        tvConsultationPurpose.setText(remoteDetailBean.getDestination());
        if (!TextUtils.isEmpty(remoteDetailBean.getTimeLength())) {
            tvConsultationTime.setText(
                    String.format(getString(R.string.txt_remote_duration), remoteDetailBean.getTimeLength()));
        }
        int status = remoteDetailBean.getStatus();
        switch (status) {
            case CONSULTATION_NONE:
            case CONSULTATION_ING:
                tvConsultationStatus.setText(R.string.txt_status_incomplete);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_incomplete);
                break;
            case CONSULTATION_ALL_REJECT:
                tvConsultationStatus.setText(R.string.txt_status_reject);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_rejected);
                break;
            case CONSULTATION_COMPLETE:
                tvConsultationStatus.setText(R.string.txt_status_complete);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_complete);
                break;
            case CONSULTATION_INTERRUPT:
            case CONSULTATION_CLOSE:
            case CONSULTATION_CLOSE_BY_TIMEOUT:
            case CONSULTATION_CLOSE_BY_INTERRUPT:
            case CONSULTATION_CLOSE_ALL_REJECT:
                tvConsultationStatus.setText(R.string.txt_status_cancel);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_cancel);
                break;
            default:
                break;
        }
    }

    /**
     * 会诊意见数据处理
     */
    private void bindAdviceData() {
        ArrayList<RemoteAdviceBean> list = remoteDetailBean.getInvitationList();
        if (list != null && list.size() > 0) {
            for (RemoteAdviceBean bean : list) {
                if (bean.isResultStatus()) {
                    adviceList.add(bean);
                }
            }
            if (adviceList.size() > 0) {
                layoutAdvice.setVisibility(View.VISIBLE);
                adviceAdapter.notifyDataSetChanged();
            }
            else {
                layoutAdvice.setVisibility(View.GONE);
            }
        }
        else {
            layoutAdvice.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_REMOTE_DETAIL) {
            remoteDetailBean = (RemoteDetailBean)response.getData();
            bindData();
        }
    }

    private class AdviceAdapter extends BaseAdapter {
        private ViewHolder holder;

        @Override
        public int getCount() {
            return adviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return adviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_remote_advice, parent, false);
                holder.headerImg = convertView.findViewById(R.id.iv_head_img);
                holder.tvName = convertView.findViewById(R.id.tv_name);
                holder.tvHospitalAndDepart = convertView.findViewById(R.id.tv_hospital_and_depart);
                holder.tvResult = convertView.findViewById(R.id.tv_result);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            RemoteAdviceBean remoteBean = adviceList.get(position);
            Glide.with(RemoteDetailActivity.this)
                 .load(FileUrlUtil.addTokenToUrl(remoteBean.getDoctorPhoto()))
                 .apply(GlideHelper.getOptions(BaseUtils.dp2px(RemoteDetailActivity.this, 4)))
                 .into(holder.headerImg);
            holder.tvName.setText(remoteBean.getDoctorName());
            holder.tvHospitalAndDepart.setText(
                    String.format(getString(R.string.txt_space_two), remoteBean.getHospitalName(),
                                  remoteBean.getHospitalDepartmentName()));
            holder.tvResult.setText(remoteBean.getResult());
            return convertView;
        }
    }

    private class ViewHolder {
        private ImageView headerImg;
        private TextView tvName, tvHospitalAndDepart;
        private JustifiedTextView tvResult;
    }
}
