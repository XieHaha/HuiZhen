package com.yht.yihuantong.ui.remote;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.yht.frame.data.bean.RemoteInvitedBean;
import com.yht.frame.data.bean.RemoteDetailBean;
import com.yht.frame.data.type.RemoteOrderStatus;
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
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 远程会诊详情
 */
public class RemoteDetailActivity extends BaseActivity implements RemoteOrderStatus {
    @BindView(R.id.iv_patient_img)
    ImageView ivPatientImg;
    @BindView(R.id.tv_patient_name)
    TextView tvPatientName;
    @BindView(R.id.tv_patient_age)
    TextView tvPatientAge;
    @BindView(R.id.tv_patient_sex)
    TextView tvPatientSex;
    @BindView(R.id.iv_check_status)
    ImageView ivStatus;
    @BindView(R.id.tv_initiate_time)
    TextView tvInitiateTime;
    @BindView(R.id.tv_initiate_depart)
    TextView tvInitiateDepart;
    @BindView(R.id.tv_initiate_hospital)
    TextView tvInitiateHospital;
    @BindView(R.id.full_list_view)
    FullListView fullListView;
    @BindView(R.id.tv_past_medical)
    JustifiedTextView tvPastMedical;
    @BindView(R.id.tv_family_medical)
    JustifiedTextView tvFamilyMedical;
    @BindView(R.id.tv_allergies)
    JustifiedTextView tvAllergies;
    @BindView(R.id.tv_condition_description)
    TextView tvConditionDescription;
    @BindView(R.id.tv_check_diagnosis_top)
    TextView tvCheckDiagnosisTop;
    @BindView(R.id.tv_consultation_purpose)
    TextView tvConsultationPurpose;
    @BindView(R.id.layout_annex)
    LinearLayout layoutAnnex;
    @BindView(R.id.tv_close_reason)
    TextView tvCloseReason;
    @BindView(R.id.layout_close_reason)
    LinearLayout layoutCloseReason;
    @BindView(R.id.layout_again_apply)
    LinearLayout layoutAgainApply;
    /**
     * id
     */
    private String orderNo;
    /**
     * 详情数据
     */
    private RemoteDetailBean remoteDetailBean;
    /**
     * 受邀方
     */
    private InvitedAdapter invitedAdapter;
    /**
     * 会诊受邀方
     */
    private ArrayList<RemoteInvitedBean> remoteInvitedBeans = new ArrayList<>();

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
        invitedAdapter = new InvitedAdapter();
        fullListView.setAdapter(invitedAdapter);
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
        bindInvitedData();
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
        tvInitiateDepart.setText(remoteDetailBean.getSourceHospitalDepartmentName());
        tvInitiateHospital.setText(remoteDetailBean.getSourceHospitalName());
        //        tvPastMedical.setText(remoteDetailBean.getPa());
        //        tvFamilyMedical.setText(remoteDetailBean.getFamilyHistory());
        //        tvAllergies.setText(remoteDetailBean.getAllergyHistory());
        tvConditionDescription.setText(remoteDetailBean.getDescIll());
        tvCheckDiagnosisTop.setText(remoteDetailBean.getInitResult());
        tvConsultationPurpose.setText(remoteDetailBean.getDestination());
        int status = remoteDetailBean.getStatus();
        switch (status) {
            case REMOTE_ORDER_STATUS_NONE:
                layoutCloseReason.setVisibility(View.GONE);
                layoutAgainApply.setVisibility(View.GONE);
                ivStatus.setImageResource(R.mipmap.ic_status_not_start);
                break;
            case REMOTE_ORDER_STATUS_IN_CONSULTATION:
            case REMOTE_ORDER_STATUS_INTERRUPT:
                layoutCloseReason.setVisibility(View.GONE);
                layoutAgainApply.setVisibility(View.GONE);
                ivStatus.setImageResource(R.mipmap.ic_status_in_consultation);
                break;
            case REMOTE_ORDER_STATUS_ALL_REFUSE:
                layoutCloseReason.setVisibility(View.GONE);
                layoutAgainApply.setVisibility(View.GONE);
                ivStatus.setImageResource(R.mipmap.ic_status_all_refuse);
                break;
            case REMOTE_ORDER_STATUS_COMPLETE:
                layoutCloseReason.setVisibility(View.GONE);
                layoutAgainApply.setVisibility(View.GONE);
                ivStatus.setImageResource(R.mipmap.ic_status_complete);
                break;
            case REMOTE_ORDER_STATUS_CLOSED:
            case REMOTE_ORDER_STATUS_TIMEOUT_CLOSE:
            case REMOTE_ORDER_STATUS_INTERRRUPT_CLOSE:
            case REMOTE_ORDER_STATUS_ALL_REFUSE_CLOSE:
                layoutCloseReason.setVisibility(View.VISIBLE);
                tvCloseReason.setText(remoteDetailBean.getRejectReason());
                layoutAgainApply.setVisibility(View.GONE);
                ivStatus.setImageResource(R.mipmap.ic_status_closed);
                break;
            case REMOTE_ORDER_STATUS_UNDER_REVIEW:
                layoutCloseReason.setVisibility(View.GONE);
                layoutAgainApply.setVisibility(View.GONE);
                ivStatus.setImageResource(R.mipmap.ic_status_wait_review);
                break;
            case REMOTE_ORDER_STATUS_REVIEW_REFUSE:
                layoutAgainApply.setVisibility(View.VISIBLE);
                layoutCloseReason.setVisibility(View.VISIBLE);
                tvCloseReason.setText(remoteDetailBean.getRejectReason());
                ivStatus.setImageResource(R.mipmap.ic_status_pass_not);
                break;
            default:
                break;
        }
    }

    /**
     * 绑定受邀方数据
     */
    private void bindInvitedData() {
        ArrayList<RemoteInvitedBean> list = remoteDetailBean.getInvitationList();
        if (list != null && list.size() > 0) {
            for (RemoteInvitedBean bean : list) {
                if (bean.isResultStatus()) {
                    remoteInvitedBeans.add(bean);
                }
            }
        }
    }

    @OnClick(R.id.tv_again_apply)
    public void onViewClicked() {

    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_REMOTE_DETAIL) {
            remoteDetailBean = (RemoteDetailBean)response.getData();
            bindData();
        }
    }

    private class InvitedAdapter extends BaseAdapter {
        private ViewHolder holder;

        @Override
        public int getCount() {
            return remoteInvitedBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return remoteInvitedBeans.get(position);
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
            RemoteInvitedBean remoteBean = remoteInvitedBeans.get(position);
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
