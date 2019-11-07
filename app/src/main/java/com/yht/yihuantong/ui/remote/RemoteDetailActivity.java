package com.yht.yihuantong.ui.remote;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.FileBean;
import com.yht.frame.data.bean.RemoteDetailBean;
import com.yht.frame.data.bean.RemoteInvitedBean;
import com.yht.frame.data.type.InvitedPartyStatus;
import com.yht.frame.data.type.RemoteOrderStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.frame.widgets.view.CenterImageSpan;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.reservation.remote.ReservationRemoteActivity;
import com.yht.yihuantong.utils.FileUrlUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 远程会诊详情
 */
public class RemoteDetailActivity extends BaseActivity implements RemoteOrderStatus, InvitedPartyStatus {
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
    @BindView(R.id.layout_annex_root)
    LinearLayout layoutAnnexRoot;
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
     * 订单状态
     */
    private int orderStatus;
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
    private ArrayList<FileBean> fileBeans = new ArrayList<>();
    /**
     * 会诊受邀方
     */
    private ArrayList<RemoteInvitedBean> remoteInvitedBeans = new ArrayList<>();
    /**
     * 受邀方状态
     */
    private Bitmap bitmapReceived, bitmapWait, bitmapRefused;

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
        initBitmap();
        invitedAdapter = new InvitedAdapter();
        fullListView.setAdapter(invitedAdapter);
    }

    @Override
    public void fillNetWorkData() {
        getRemoteDetail();
    }

    /**
     * 获取会诊详情
     */
    private void getRemoteDetail() {
        RequestUtils.getRemoteDetail(this, loginBean.getToken(), orderNo, this);
    }

    /**
     * 检查项状态图
     */
    private void initBitmap() {
        bitmapReceived = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_label_accepted);
        bitmapWait = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_label_accepting);
        bitmapRefused = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_label_refuesd);
    }

    /**
     * 基础数据
     */
    private void bindData() {
        if (remoteDetailBean == null) { return; }
        fileBeans = remoteDetailBean.getPatientResourceList();
        bindAnnexData();
        //患者基础信息
        Glide.with(this)
             .load(FileUrlUtil.addTokenToUrl(remoteDetailBean.getPatientPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivPatientImg);
        tvPatientName.setText(remoteDetailBean.getPatientName());
        tvPatientSex.setText(remoteDetailBean.getSex() == BaseData.BASE_ONE
                             ? getString(R.string.txt_sex_male)
                             : getString(R.string.txt_sex_female));
        tvPatientAge.setText(String.valueOf(remoteDetailBean.getPatientAge()));
        tvInitiateTime.setText(BaseUtils.timeFormat(remoteDetailBean.getStartAt(), remoteDetailBean.getEndAt()));
        tvInitiateDepart.setText(remoteDetailBean.getSourceHospitalDepartmentName());
        tvInitiateHospital.setText(remoteDetailBean.getSourceHospitalName());
        tvPastMedical.setText(remoteDetailBean.getPastHistory());
        tvFamilyMedical.setText(remoteDetailBean.getFamilyHistory());
        tvAllergies.setText(remoteDetailBean.getAllergyHistory());
        tvConditionDescription.setText(remoteDetailBean.getDescIll());
        tvCheckDiagnosisTop.setText(remoteDetailBean.getInitResult());
        tvConsultationPurpose.setText(remoteDetailBean.getDestination());
        orderStatus = remoteDetailBean.getStatus();
        switch (orderStatus) {
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
        //受邀方数据
        remoteInvitedBeans = remoteDetailBean.getInvitationList();
        invitedAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(fullListView, remoteInvitedBeans.size());
    }

    /**
     * 附件资料
     */
    private void bindAnnexData() {
        if (fileBeans != null && fileBeans.size() > 0) {
            layoutAnnexRoot.setVisibility(View.VISIBLE);
            addView();
        }
        else {
            layoutAnnexRoot.setVisibility(View.GONE);
        }
    }

    private void addView() {
        for (int i = 0; i < fileBeans.size(); i++) {
            FileBean bean = fileBeans.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_check_report, null);
            TextView textView = view.findViewById(R.id.tv_check_report_name);
            textView.setText(bean.getName());
            layoutAnnex.addView(view);
        }
    }

    @OnClick(R.id.tv_again_apply)
    public void onViewClicked() {
        Intent intent = new Intent(this, ReservationRemoteActivity.class);
        intent.putExtra(CommonData.KEY_REMOTE_ORDER_BEAN, remoteDetailBean);
        startActivity(intent);
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
                convertView = getLayoutInflater().inflate(R.layout.item_remote_invited, parent, false);
                holder.tvHospitalAndDepart = convertView.findViewById(R.id.tv_depart_hospital);
                //拒绝原因
                holder.tvRefuseReason = convertView.findViewById(R.id.tv_reason);
                holder.layoutRefuseReason = convertView.findViewById(R.id.layout_refuse_reason);
                //会诊意见
                holder.tvDoctorName = convertView.findViewById(R.id.tv_doctor_name);
                holder.tvAdviceBtn = convertView.findViewById(R.id.tv_advice_btn);
                holder.tvResult = convertView.findViewById(R.id.tv_result);
                holder.layoutAdvice = convertView.findViewById(R.id.layout_advice);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            RemoteInvitedBean remoteBean = remoteInvitedBeans.get(position);
            bindInvitedData(holder, remoteBean);
            return convertView;
        }
    }

    /**
     * 受邀方数据
     */
    private void bindInvitedData(ViewHolder holder, RemoteInvitedBean remoteBean) {
        holder.tvDoctorName.setText(remoteBean.getDoctorName());
        holder.tvHospitalAndDepart.setText(
                String.format(getString(R.string.txt_joiner), remoteBean.getHospitalDepartmentName(),
                              remoteBean.getHospitalName() + " "));
        //订单状态为待审核、被拒绝 不显示受邀方状态
        if (orderStatus == REMOTE_ORDER_STATUS_REVIEW_REFUSE || orderStatus == REMOTE_ORDER_STATUS_UNDER_REVIEW) {
            holder.layoutRefuseReason.setVisibility(View.GONE);
            holder.layoutAdvice.setVisibility(View.GONE);
        }
        else {
            holder.tvHospitalAndDepart.append(appendImage(remoteBean.getStatus(),
                                                          String.format(getString(R.string.txt_joiner),
                                                                        remoteBean.getHospitalDepartmentName(),
                                                                        remoteBean.getHospitalName() + " ")));
            switch (remoteBean.getStatus()) {
                case INVITED_PARTY_STATUS_RECEIVED:
                case INVITED_PARTY_STATUS_WAIT:
                    holder.layoutRefuseReason.setVisibility(View.GONE);
                    //显示会诊意见
                    if (remoteBean.isResultStatus()) {
                        holder.layoutAdvice.setVisibility(View.VISIBLE);
                        holder.tvDoctorName.setText(remoteBean.getDoctorName());
                        if (TextUtils.isEmpty(remoteBean.getResult())) {
                            holder.tvResult.setText(R.string.txt_consultation_advice_none);
                        }
                        else { holder.tvResult.setText(remoteBean.getResult()); }
                        holder.tvAdviceBtn.setOnClickListener(v -> {
                            holder.tvAdviceBtn.setSelected(!holder.tvAdviceBtn.isSelected());
                            if (holder.tvAdviceBtn.isSelected()) {
                                holder.tvResult.setVisibility(View.VISIBLE);
                            }
                            else {
                                holder.tvResult.setVisibility(View.GONE);
                            }
                            setListViewHeightBasedOnChildren(fullListView, remoteInvitedBeans.size());
                        });
                    }
                    else {
                        holder.layoutAdvice.setVisibility(View.GONE);
                    }
                    break;
                case INVITED_PARTY_STATUS_REFUSED:
                    //显示拒绝原因
                    holder.layoutRefuseReason.setVisibility(View.VISIBLE);
                    holder.layoutAdvice.setVisibility(View.GONE);
                    holder.tvRefuseReason.setText(remoteBean.getRejectReason());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 添加状态图
     */
    private SpannableString appendImage(int status, String showText) {
        CenterImageSpan imgSpan;
        switch (status) {
            case INVITED_PARTY_STATUS_RECEIVED:
                imgSpan = new CenterImageSpan(this, bitmapReceived);
                break;
            case INVITED_PARTY_STATUS_WAIT:
                imgSpan = new CenterImageSpan(this, bitmapWait);
                break;
            case INVITED_PARTY_STATUS_REFUSED:
                imgSpan = new CenterImageSpan(this, bitmapRefused);
                break;
            default:
                imgSpan = new CenterImageSpan(this, bitmapReceived);
                break;
        }
        SpannableString spanString = new SpannableString(showText);
        spanString.setSpan(imgSpan, 0, showText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    private class ViewHolder {
        private TextView tvDoctorName, tvHospitalAndDepart, tvRefuseReason, tvAdviceBtn;
        private JustifiedTextView tvResult;
        private LinearLayout layoutRefuseReason;
        private RelativeLayout layoutAdvice;
    }

    /**
     * 设置高度
     */
    private void setListViewHeightBasedOnChildren(ListView listView, int count) {
        if (listView == null || invitedAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View listItem = invitedAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            HuiZhenLog.i(TAG, "childHeight:" + listItem.getMeasuredHeight());
            totalHeight += listItem.getMeasuredHeight();
        }
        HuiZhenLog.i(TAG, "height:" + totalHeight);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (count - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapReceived != null) {
            bitmapReceived.recycle();
            bitmapReceived = null;
        }
        if (bitmapWait != null) {
            bitmapWait.recycle();
            bitmapWait = null;
        }
        if (bitmapRefused != null) {
            bitmapRefused.recycle();
            bitmapRefused = null;
        }
    }
}
