package com.yht.yihuantong.ui.remote;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.FileBean;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.data.bean.RemoteDetailBean;
import com.yht.frame.data.bean.RemoteInvitedBean;
import com.yht.frame.data.bean.ReservationValidateBean;
import com.yht.frame.data.type.InvitedPartyStatus;
import com.yht.frame.data.type.RemoteOrderStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.MimeUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.adapter.RemoteDetailInviteAdapter;
import com.yht.yihuantong.ui.reservation.ReservationDisableActivity;
import com.yht.yihuantong.ui.reservation.remote.ReservationRemoteActivity;
import com.yht.yihuantong.utils.FileUrlUtil;
import com.yht.yihuantong.utils.FileUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 远程会诊详情
 */
public class RemoteDetailActivity extends BaseActivity implements RemoteOrderStatus,
        InvitedPartyStatus {
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
    RecyclerView recyclerView;
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
    private RemoteDetailInviteAdapter invitedAdapter;
    /**
     * 附件资料
     */
    private ArrayList<FileBean> fileBeans = new ArrayList<>();
    /**
     * 附件资料中的图片
     */
    private ArrayList<NormImage> normImages = new ArrayList<>();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        invitedAdapter = new RemoteDetailInviteAdapter(R.layout.item_remote_invited,
                remoteInvitedBeans);
        invitedAdapter.setBitmap(bitmapWait, bitmapReceived, bitmapRefused);
        recyclerView.setAdapter(invitedAdapter);
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
     * 权限
     */
    private void getValidateHospitalList() {
        RequestUtils.getValidateHospitalList(this, loginBean.getToken(), true, this);
    }

    /**
     * 检查项状态图
     */
    private void initBitmap() {
        bitmapReceived = BitmapFactory.decodeResource(getApplication().getResources(),
                R.mipmap.ic_label_accepted);
        bitmapWait = BitmapFactory.decodeResource(getApplication().getResources(),
                R.mipmap.ic_label_accepting);
        bitmapRefused = BitmapFactory.decodeResource(getApplication().getResources(),
                R.mipmap.ic_label_refuesd);
    }

    /**
     * 基础数据
     */
    private void bindData() {
        if (remoteDetailBean == null) {
            return;
        }
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
        tvInitiateTime.setText(BaseUtils.timeFormat(remoteDetailBean.getStartAt(),
                remoteDetailBean.getEndAt()));
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
                if (TextUtils.isEmpty(remoteDetailBean.getRejectReason())) {
                    layoutCloseReason.setVisibility(View.GONE);
                } else {
                    layoutCloseReason.setVisibility(View.VISIBLE);
                    tvCloseReason.setText(remoteDetailBean.getRejectReason());
                }
                layoutAgainApply.setVisibility(View.GONE);
                ivStatus.setImageResource(R.mipmap.ic_status_closed);
                break;
            case REMOTE_ORDER_STATUS_UNDER_REVIEW:
                layoutCloseReason.setVisibility(View.GONE);
                layoutAgainApply.setVisibility(View.GONE);
                ivStatus.setImageResource(R.mipmap.ic_status_wait_review);
                break;
            case REMOTE_ORDER_STATUS_REVIEW_REFUSE:
                if (TextUtils.isEmpty(remoteDetailBean.getRejectReason())) {
                    layoutCloseReason.setVisibility(View.GONE);
                } else {
                    layoutCloseReason.setVisibility(View.VISIBLE);
                    tvCloseReason.setText(remoteDetailBean.getRejectReason());
                }
                layoutAgainApply.setVisibility(View.VISIBLE);
                ivStatus.setImageResource(R.mipmap.ic_status_pass_not);
                break;
            default:
                break;
        }
        //受邀方数据
        dealRemoteInvited();
    }


    /**
     * 重新排序受邀方  已接受靠前、已拒绝靠后
     */
    private void dealRemoteInvited() {
        ArrayList<RemoteInvitedBean> list = remoteDetailBean.getInvitationList();
        ArrayList<RemoteInvitedBean> completedList = new ArrayList<>();
        ArrayList<RemoteInvitedBean> refusedList = new ArrayList<>();
        ArrayList<RemoteInvitedBean> otherList = new ArrayList<>();
        for (RemoteInvitedBean bean : list) {
            switch (bean.getStatus()) {
                case INVITED_PARTY_STATUS_RECEIVED:
                    completedList.add(bean);
                    break;
                case INVITED_PARTY_STATUS_WAIT:
                    otherList.add(bean);
                    break;
                case INVITED_PARTY_STATUS_REFUSED:
                    refusedList.add(bean);
                    break;
                default:
                    break;
            }
        }
        remoteInvitedBeans.addAll(completedList);
        remoteInvitedBeans.addAll(otherList);
        remoteInvitedBeans.addAll(refusedList);
        invitedAdapter.setOrderStatus(remoteDetailBean.getStatus());
        invitedAdapter.setNewData(remoteInvitedBeans);
        invitedAdapter.notifyDataSetChanged();
    }

    /**
     * 附件资料
     */
    private void bindAnnexData() {
        if (fileBeans != null && fileBeans.size() > 0) {
            layoutAnnexRoot.setVisibility(View.VISIBLE);
            addView();
        } else {
            layoutAnnexRoot.setVisibility(View.GONE);
        }
    }

    private void addView() {
        for (int i = 0; i < fileBeans.size(); i++) {
            FileBean bean = fileBeans.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_check_report, null);
            TextView textView = view.findViewById(R.id.tv_check_report_name);
            textView.setText(bean.getName());
            //获取文件格式
            String type = MimeUtils.getMime(FileUtils.getFileExtNoPoint(bean.getFileUrl()));
            if (BaseData.BASE_IMAGE_TYPE.contains(type)) {
                //如果为图片格式
                view.setTag(true);
                NormImage normImage = new NormImage();
                normImage.setImageUrl(bean.getFileUrl());
                normImages.add(normImage);
            } else {
                view.setTag(false);
            }
            view.setOnClickListener(v -> {
                boolean image = (boolean) v.getTag();
                if (image) {
                    //查看大图
                    Intent intent = new Intent(RemoteDetailActivity.this,
                            ImagePreviewActivity.class);
                    intent.putExtra(ImagePreviewActivity.INTENT_URLS, normImages);
                    //                    intent.putExtra(ImagePreviewActivity.INTENT_POSITION,
                    //                    position);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.keep);
                } else {
                    ToastUtil.toast(RemoteDetailActivity.this, R.string.txt_open_file_error);
                }
            });
            layoutAnnex.addView(view);
        }
    }

    @OnClick(R.id.tv_again_apply)
    public void onViewClicked() {
        getValidateHospitalList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_REMOTE_DETAIL:
                remoteDetailBean = (RemoteDetailBean) response.getData();
                bindData();
                break;
            case GET_VALIDATE_HOSPITAL_LIST:
                Intent intent;
                ReservationValidateBean bean = (ReservationValidateBean) response.getData();
                if (bean != null && bean.isRemote()) {
                    intent = new Intent(this, ReservationRemoteActivity.class);
                    intent.putExtra(CommonData.KEY_REMOTE_ORDER_BEAN, remoteDetailBean);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ReservationDisableActivity.class);
                    intent.putExtra(CommonData.KEY_RESERVATION_TYPE, BASE_TWO);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
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
