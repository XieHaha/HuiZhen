package com.yht.yihuantong.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.NotifyMessageBean;
import com.yht.frame.utils.TimeUtil;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 消息通知
 */
public class NotifyMessageAdapter extends BaseMultiItemQuickAdapter<NotifyMessageBean, BaseViewHolder> {
    public NotifyMessageAdapter(List<NotifyMessageBean> data) {
        super(data);
        addItemType(NotifyMessageBean.REPORT, R.layout.item_notify_message_report);
        addItemType(NotifyMessageBean.CURRENCY, R.layout.item_notify_message_currency);
    }

    @Override
    protected void convert(BaseViewHolder helper, NotifyMessageBean item) {
        switch (helper.getItemViewType()) {
            case NotifyMessageBean.REPORT:
                helper.setText(R.id.tv_time_bar, TimeUtil.getTimeString(Long.valueOf(item.getTime())));
                break;
            case NotifyMessageBean.CURRENCY:
                helper.setText(R.id.tv_time_bar, TimeUtil.getTimeString(Long.valueOf(item.getTime())));
                break;
            default:
                break;
        }
    }
}
