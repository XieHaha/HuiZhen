package com.zyc.doctor.ui;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author 顿顿
 * @date 19/6/10 10:41
 * @des edit text
 */
public abstract class AbstractTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
