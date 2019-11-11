package com.yht.frame.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.Set;

/**
 * 新添加的方法对SharedPreference的使用做了建议的封装，对外公布出put，getObject，remove，clear等等方法；
 *
 * @author liushubao
 * Created by admin on 2017/3/6.
 */
public class SharePreferenceUtil {
    private Context mContext;
    private SharedPreferences sp;
    private Editor spEditor;
    /**
     * 保存在手机里面的默认文件名
     */
    private static final String FILE_NAME = "ZYC";
    /**
     * 保存在手机里面的默认文件名(退出登录不清除的数据)
     */
    private static final String FILE_NAME_OTHER = "ZYC_OTHER";

    public SharePreferenceUtil(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /**
     * 存  不清除的数据
     */
    public void putAlwaysString(String spKey, String spValue) {
        spEditor = mContext.getSharedPreferences(FILE_NAME_OTHER, 0).edit();
        spEditor.putString(spKey, spValue);
        spEditor.apply();
    }

    /**
     * 取 不清除的数据
     */
    public String getAlwaysString(String spKey) {
        sp = mContext.getSharedPreferences(FILE_NAME_OTHER, 0);
        String value = "";
        if (sp != null) {
            value = sp.getString(spKey, "");
        }
        return value;
    }

    /**
     * 存  不清除的数据
     */
    public void putAlwaysLong(String spKey, long spValue) {
        spEditor = mContext.getSharedPreferences(FILE_NAME_OTHER, 0).edit();
        spEditor.putLong(spKey, spValue);
        spEditor.apply();
    }

    /**
     * 取 不清除的数据
     */
    public long getAlwaysLong(String spKey) {
        sp = mContext.getSharedPreferences(FILE_NAME_OTHER, 0);
        //默认1 为有通知权限
        long value = 1;
        if (sp != null) {
            value = sp.getLong(spKey, 1);
        }
        return value;
    }

    /**
     * 存  不清除的数据
     */
    public void putAlwaysBoolean(String spKey, boolean spValue) {
        spEditor = mContext.getSharedPreferences(FILE_NAME_OTHER, 0).edit();
        spEditor.putBoolean(spKey, spValue);
        spEditor.apply();
    }

    /**
     * 取 不清除的数据
     */
    public boolean getAlwaysBoolean(String spKey) {
        sp = mContext.getSharedPreferences(FILE_NAME_OTHER, 0);
        //默认1 为有通知权限
        boolean value = false;
        if (sp != null) {
            value = sp.getBoolean(spKey, false);
        }
        return value;
    }

    /**
     * 存
     */
    public void putString(String spKey, String spValue) {
        spEditor = mContext.getSharedPreferences(FILE_NAME, 0).edit();
        spEditor.putString(spKey, spValue);
        spEditor.apply();
    }

    /**
     * 取
     */
    public String getString(String spKey) {
        sp = mContext.getSharedPreferences(FILE_NAME, 0);
        String value = "";
        if (sp != null) {
            value = sp.getString(spKey, "");
        }
        return value;
    }

    /**
     * 存
     */
    public void putStringSet(String spKey, Set<String> spValue) {
        spEditor = mContext.getSharedPreferences(FILE_NAME, 0).edit();
        spEditor.putStringSet(spKey, spValue);
        spEditor.apply();
    }

    /**
     * 取
     */
    public Set<String> getStringSet(String spKey) {
        sp = mContext.getSharedPreferences(FILE_NAME, 0);
        Set<String> value = null;
        if (sp != null) {
            value = sp.getStringSet(spKey, null);
        }
        return value;
    }

    /**
     * 存
     */
    public void putInt(String spKey, int spValue) {
        spEditor = mContext.getSharedPreferences(FILE_NAME, 0).edit();
        spEditor.putInt(spKey, spValue);
        spEditor.apply();
    }

    /**
     * 取
     */
    public int getInt(String spKey) {
        sp = mContext.getSharedPreferences(FILE_NAME, 0);
        int value = -1;
        if (sp != null) {
            value = sp.getInt(spKey, -1);
        }
        return value;
    }

    /**
     * 存
     */
    public void putBoolean(String spKey, boolean spValue) {
        spEditor = mContext.getSharedPreferences(FILE_NAME, 0).edit();
        spEditor.putBoolean(spKey, spValue);
        spEditor.apply();
    }

    /**
     * 取
     */
    public boolean getBoolean(String spKey) {
        sp = mContext.getSharedPreferences(FILE_NAME, 0);
        boolean value = false;
        if (sp != null) {
            value = sp.getBoolean(spKey, false);
        }
        return value;
    }

    /**
     * 删
     */
    public void clear(String spKey) {
        sp = mContext.getSharedPreferences(FILE_NAME, 0);
        if (sp != null) {
            sp.edit().remove(spKey).apply();
        }
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    @SuppressWarnings("unchecked")
    public static void putObject(Context context, String key, Object object) {
        if (object != null) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            Editor editor = sp.edit();
            if (object instanceof String) {
                editor.putString(key, (String)object);
            }
            else if (object instanceof Integer) {
                editor.putInt(key, (Integer)object);
            }
            else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean)object);
            }
            else if (object instanceof Float) {
                editor.putFloat(key, (Float)object);
            }
            else if (object instanceof Long) {
                editor.putLong(key, (Long)object);
            }
            else if (object instanceof Set<?>) {
                editor.putStringSet(key, (Set<String>)object);
            }
            else {
                editor.putString(key, new Gson().toJson(object));
            }
            editor.apply();
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object getObject(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String)defaultObject);
        }
        else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long)defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, "");
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear().apply();
    }

    /**
     * 查询某个key是否已经存在
     */
    public boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }
}
