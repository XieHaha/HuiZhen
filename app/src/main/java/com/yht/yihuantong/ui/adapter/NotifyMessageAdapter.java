package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.bean.NotifyMessageBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.TimeUtil;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 消息通知
 */
public class NotifyMessageAdapter extends BaseQuickAdapter<NotifyMessageBean, BaseViewHolder> {
    /**
     * 全部已读状态（本地）
     */
    private boolean updateAll = false;

    public NotifyMessageAdapter(int layoutResId, @Nullable List<NotifyMessageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NotifyMessageBean item) {
        helper.setText(R.id.tv_title, item.getTitle())
              .setText(R.id.tv_content, item.getContent())
              .setText(R.id.tv_time_bar, TimeUtil.getTimeString(
                      BaseUtils.date2TimeStamp(item.getCreateAt(), BaseUtils.YYYY_MM_DD_HH_MM_SS)));
        if (item.getState() == BaseData.BASE_ZERO && !updateAll) {
            helper.setVisible(R.id.iv_new_message, true);
        }
        else {
            helper.setVisible(R.id.iv_new_message, false);
        }
    }

    public void setUpdateAll(boolean updateAll) {
        this.updateAll = updateAll;
    }
}
