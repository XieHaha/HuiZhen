package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.TimeUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.patient.ChatContainerActivity;

import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @description 最近添加的居民
 */
public class RecentPatientGroupAdapter extends BaseExpandableListAdapter implements BaseData {
    private Context mContext;
    private ArrayList<String> parentData = new ArrayList<>();
    private ArrayList<ArrayList<PatientBean>> childData = new ArrayList<>();

    public RecentPatientGroupAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setParentData(ArrayList<String> parentData) {
        this.parentData = parentData;
    }

    public void setChildData(ArrayList<ArrayList<PatientBean>> childData) {
        this.childData = childData;
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mContext.startActivity(intent);
    }

    @Override
    public int getGroupCount() {
        return parentData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        ParentHolder holder;
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(mContext).inflate(R.layout.item_recent_patient_group,
                            parent, false);
            holder = new ParentHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_title);
            holder.imageView = convertView.findViewById(R.id.iv_tab);
            convertView.setTag(holder);
        } else {
            holder = (ParentHolder) convertView.getTag();
        }
        initParentData(holder, isExpanded, groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recent_patient,
                    parent, false);
            holder = new ChildHolder();
            holder.tvName = convertView.findViewById(R.id.tv_patient_name);
            holder.tvTime = convertView.findViewById(R.id.tv_time);
            holder.ivHeader = convertView.findViewById(R.id.iv_patient_img);
            holder.ivCall = convertView.findViewById(R.id.iv_patient_call);
            convertView.setTag(holder);

        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        initChildData(convertView, holder, groupPosition, childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void initParentData(ParentHolder holder, boolean isExpanded, int groupPosition) {
        holder.tvTitle.setText(parentData.get(groupPosition));
        if (isExpanded) {
            holder.imageView.setSelected(true);
        } else {
            holder.imageView.setSelected(false);
        }
    }

    private void initChildData(View convertView, ChildHolder holder, int groupPosition,
                               int childPosition) {
        ArrayList<PatientBean> childList = childData.get(groupPosition);
        if (childList != null && childList.size() > 0) {
            PatientBean patientBean = childList.get(childPosition);
            holder.tvName.setText(patientBean.getName());
            holder.tvTime.setText(TimeUtil.getTimeString(BaseUtils.date2TimeStamp(patientBean.getAddTime(), BaseUtils.YYYY_MM_DD_HH_MM)));
            Glide.with(mContext)
                    .load(patientBean.getPhoto())
                    .apply(GlideHelper.getOptions(BaseUtils.dp2px(mContext, 4)))
                    .into(holder.ivHeader);
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, ChatContainerActivity.class);
                intent.putExtra(CommonData.KEY_CHAT_ID, patientBean.getCode());
                intent.putExtra(CommonData.KEY_CHAT_NAME, patientBean.getName());
                mContext.startActivity(intent);
            });
            holder.ivCall.setOnClickListener(v -> new HintDialog(mContext)
                    .setPhone(mContext.getString(R.string.txt_contact_patient_phone),
                            patientBean.getMobile())
                    .setOnEnterClickListener(() -> callPhone(patientBean.getMobile()))
                    .show());
        }
    }

    private class ParentHolder {
        private TextView tvTitle;
        private ImageView imageView;
    }

    private class ChildHolder {
        private TextView tvName, tvTime;
        private ImageView ivHeader, ivCall;
    }
}
