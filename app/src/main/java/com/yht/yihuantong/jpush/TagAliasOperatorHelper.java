package com.yht.yihuantong.jpush;

import android.content.Context;
import android.util.SparseArray;

import com.yht.frame.utils.HuiZhenLog;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * 处理tagalias相关的逻辑
 * /**
 * *6001   无效的设置，tag/alias 不应参数都为 null
 * *6002   设置超时    建议重试
 * *6003   alias 字符串不合法    有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
 * *6004   alias超长。最多 40个字节    中文 UTF-8 是 3 个字节
 * *6005   某一个 tag 字符串不合法  有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
 * *6006   某一个 tag 超长。一个 tag 最多 40个字节  中文 UTF-8 是 3 个字节
 * *6007   tags 数量超出限制。最多 100个 这是一台设备的限制。一个应用全局的标签数量无限制。
 * *6008   tag/alias 超出总长度限制。总长度最多 1K 字节
 * *6011   10s内设置tag或alias大于3次 短时间内操作过于频繁
 *
 * @author dundun
 */
public class TagAliasOperatorHelper {
    private static final String TAG = "ZYC-PUSH";
    public static int sequence = 1;
    /**
     * 增加
     */
    public static final int ACTION_ADD = 1;
    /**
     * 覆盖
     */
    public static final int ACTION_SET = 2;
    /**
     * 删除部分
     */
    public static final int ACTION_DELETE = 3;
    /**
     * 删除所有
     */
    public static final int ACTION_CLEAN = 4;
    /**
     * 查询
     */
    public static final int ACTION_GET = 5;
    public static final int ACTION_CHECK = 6;
    public static final int DELAY_SEND_ACTION = 1;
    public static final int DELAY_SET_MOBILE_NUMBER_ACTION = 2;
    public Context context;
    public static TagAliasOperatorHelper mInstance;

    private TagAliasOperatorHelper() {
    }

    public static TagAliasOperatorHelper getInstance() {
        if (mInstance == null) {
            synchronized (TagAliasOperatorHelper.class) {
                if (mInstance == null) {
                    mInstance = new TagAliasOperatorHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
    }

    private SparseArray<Object> setActionCache = new SparseArray<Object>();

    public Object get(int sequence) {
        return setActionCache.get(sequence);
    }

    public Object remove(int sequence) {
        return setActionCache.get(sequence);
    }

    private void put(int sequence, Object tagAliasBean) {
        setActionCache.put(sequence, tagAliasBean);
    }

    /**
     * 处理设置tag
     */
    public void handleAction(Context context, int sequence, TagAliasBean tagAliasBean) {
        init(context);
        if (tagAliasBean == null) {
            HuiZhenLog.w(TAG, "tagAliasBean was null");
            return;
        }
        put(sequence, tagAliasBean);
        if (tagAliasBean.isAliasAction) {
            switch (tagAliasBean.action) {
                case ACTION_GET:
                    JPushInterface.getAlias(context, sequence);
                    break;
                case ACTION_DELETE:
                    JPushInterface.deleteAlias(context, sequence);
                    break;
                case ACTION_SET:
                    JPushInterface.setAlias(context, sequence, tagAliasBean.alias);
                    break;
                default:
                    HuiZhenLog.w(TAG, "unsupport alias action type");
                    return;
            }
        } else {
            switch (tagAliasBean.action) {
                case ACTION_ADD:
                    JPushInterface.addTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_SET:
                    JPushInterface.setTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_DELETE:
                    JPushInterface.deleteTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_CHECK:
                    //一次只能check一个tag
                    String tag = (String) tagAliasBean.tags.toArray()[0];
                    JPushInterface.checkTagBindState(context, sequence, tag);
                    break;
                case ACTION_GET:
                    JPushInterface.getAllTags(context, sequence);
                    break;
                case ACTION_CLEAN:
                    JPushInterface.cleanTags(context, sequence);
                    break;
                default:
                    HuiZhenLog.w(TAG, "unsupport tag action type");
                    return;
            }
        }
    }

    public static class TagAliasBean {
        public int action;
        public Set<String> tags;
        public String alias;
        public boolean isAliasAction;

        @Override
        public String toString() {
            return "TagAliasBean{" + "action=" + action + ", tags=" + tags + ", alias='" + alias + '\'' +
                    ", isAliasAction=" + isAliasAction + '}';
        }
    }
}
