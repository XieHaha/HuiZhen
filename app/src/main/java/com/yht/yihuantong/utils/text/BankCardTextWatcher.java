package com.yht.yihuantong.utils.text;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.yht.yihuantong.ui.reservation.remote.RemoteIdentifyFragment;
import com.yht.yihuantong.ui.reservation.service.ServiceIdentifyFragment;
import com.yht.yihuantong.ui.reservation.transfer.TransferIdentifyFragment;

/**
 * @author 顿顿
 * @date 19/6/14 15:07
 * @des 身份证空格
 */
public class BankCardTextWatcher implements TextWatcher {
    private Fragment fragment;
    /**
     * default max length = 18+2个空格
     */
    private static final int DEFAULT_MAX_LENGTH = 18 + 2;
    /**
     * max input length
     */
    private int maxLength = DEFAULT_MAX_LENGTH;
    private int beforeTextLength = 0;
    private boolean isChanged = false;
    /**
     * space count
     */
    private int space = 0;
    private StringBuffer buffer = new StringBuffer();
    private EditText editText;

    public static void bind(EditText editText, Fragment fragment) {
        new BankCardTextWatcher(editText, DEFAULT_MAX_LENGTH, fragment);
    }

    public static void bind(EditText editText, int maxLength) {
        new BankCardTextWatcher(editText, maxLength, null);
    }

    public BankCardTextWatcher(EditText editText, int maxLength, Fragment fragment) {
        this.editText = editText;
        this.maxLength = maxLength;
        this.fragment = fragment;
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeTextLength = s.length();
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
        space = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                space++;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (fragment != null) {
            if (fragment instanceof ServiceIdentifyFragment) {
                ((ServiceIdentifyFragment) fragment).onCardTextChanged(s, start, before, count);
            }
            if (fragment instanceof TransferIdentifyFragment) {
                ((TransferIdentifyFragment) fragment).onCardTextChanged(s, start, before, count);
            }
            if (fragment instanceof RemoteIdentifyFragment) {
                ((RemoteIdentifyFragment) fragment).onCardTextChanged(s, start, before, count);
            }
        }
        int length = s.length();
        buffer.append(s.toString());
        if (length == beforeTextLength || length <= 5 || isChanged) {
            isChanged = false;
            return;
        }
        isChanged = true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isChanged) {
            int selectionIndex = editText.getSelectionEnd();
            //total char length
            int index = 0;
            while (index < buffer.length()) {
                if (buffer.charAt(index) == ' ') {
                    buffer.deleteCharAt(index);
                } else {
                    index++;
                }
            }
            //total space count
            index = 0;
            int totalSpace = 0;
            while (index < buffer.length()) {
                if ((index == 6 || index == 15)) {
                    buffer.insert(index, ' ');
                    totalSpace++;
                }
                index++;
            }
            //selection index
            if (totalSpace > space) {
                selectionIndex += (totalSpace - space);
            }
            char[] tempChar = new char[buffer.length()];
            buffer.getChars(0, buffer.length(), tempChar, 0);
            String str = buffer.toString();
            if (selectionIndex > str.length()) {
                selectionIndex = str.length();
            } else if (selectionIndex < 0) {
                selectionIndex = 0;
            }
            editText.setText(str);
            Editable text = editText.getText();
            //set selection
            Selection.setSelection(text, selectionIndex < maxLength ? selectionIndex : maxLength);
            isChanged = false;
        }
    }
}
