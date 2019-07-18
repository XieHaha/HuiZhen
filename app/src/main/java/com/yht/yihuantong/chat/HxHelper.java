package com.yht.yihuantong.chat;

import android.content.Context;
import android.text.TextUtils;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.UserInfoCallback;
import com.hyphenate.easeui.domain.EaseUser;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.PatientBean;
import com.yht.frame.http.listener.AbstractResponseAdapter;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.yihuantong.ZycApplication;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Map;

/**
 * @author dundun
 */
public class HxHelper {
    /**
     * 扩展消息-昵称
     */
    public static final String MSG_EXT_NICKNAME = "hx_nickname";
    /**
     * 扩展消息-头像
     */
    public static final String MSG_EXT_AVATAR = "hx_avatar";
    private static Resource resource;

    public synchronized static Resource getInstance() {
        if (resource == null) {
            resource = new Resource();
        }
        return resource;
    }

    public static class Resource {
        private Context context;
        /**
         * 所有的会话集合
         */
        private Map<String, EMConversation> mConvMap;

        private Resource() {
            if (null != resource) {
                throw new IllegalStateException("Can not instantiate singleton class.");
            }
        }

        /**
         * 初始化
         *
         * @param context context
         */
        public void init(Context context) {
            this.context = context;
        }

        public EaseUser getUser(String username, UserInfoCallback callback) {
            HuiZhenLog.i("ZYC", "code:" + username);
            if (TextUtils.isEmpty(username)) { return null; }
            EaseUser user = new EaseUser(username);
            List<PatientBean> list = DataSupport.where("code = ?", username).find(PatientBean.class);
            if (list != null && list.size() > 0) {
                PatientBean bean = list.get(0);
                user.setNickname(bean.getName());
                user.setAvatar(bean.getWxPhoto());
                callback.onSuccess(user);
                return user;
            }
            RequestUtils.getPatientDetailByPatientCode(context, username,
                                                       ZycApplication.getInstance().getLoginBean().getToken(),
                                                       new AbstractResponseAdapter<BaseResponse>() {
                                                           @Override
                                                           public void onResponseSuccess(Tasks task,
                                                                   BaseResponse response) {
                                                               PatientBean patientBean = (PatientBean)response.getData();
                                                               if (patientBean != null) {
                                                                   user.setNickname(patientBean.getName());
                                                                   user.setAvatar(patientBean.getWxPhoto());
                                                               }
                                                               patientBean.save();
                                                               callback.onSuccess(user);
                                                           }
                                                       });
            return user;
        }
    }

    /**
     * 配置项
     */
    public static class Opts {
        private boolean showChatTitle;

        public boolean isShowChatTitle() {
            return showChatTitle;
        }

        public void setShowChatTitle(boolean showChatTitle) {
            this.showChatTitle = showChatTitle;
        }
    }
}
