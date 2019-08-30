package com.yht.frame.widgets.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.yht.frame.R;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 顿顿
 * @date 19/7/9 12:14
 * @description
 */
public class MultiLineEditText extends EditText {
    private int maxLength = 500;
    private InputFilter[] emojiFilters;
    private Context context;

    public MultiLineEditText(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public MultiLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public MultiLineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiLineEditText, 0, 0);
        int length = a.getInt(R.styleable.MultiLineEditText_maxLength, maxLength);
        emojiFilters = new InputFilter[] {
                emojiFilter, new InputFilter.LengthFilter(length) };
        setFilters(emojiFilters);
    }

    InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast(context, R.string.txt_not_support_input);
                return "";
            }
            return null;
        }

        Pattern emoji = Pattern.compile(BaseUtils.filterImoji, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    };

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
        if (inputConnection != null) {
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        }
        return inputConnection;
    }
}
