package com.zyc.doctor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.yht.frame.data.BaseData;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.zyc.doctor.R;

import butterknife.BindView;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 工作室
 */
public class WorkerFragment extends BaseFragment implements CustomAdapt, LoadViewHelper.OnNextClickListener {
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    int type = 1;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_worker;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        LoadViewHelper helper = new LoadViewHelper(view);
        helper.setOnNextClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(getContext(), "中间显示");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type > 5) {
                    type = 1;
                }
                helper.load(type);
                type++;
            }
        });
    }

    private void showDialog() {
        HintDialog hintDialog = new HintDialog(getContext());
        hintDialog.show();
    }

    @Override
    public void onNextClick() {
        ToastUtil.toast(getContext(), "next");
    }

    /*************************屏幕适配*/
    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return BaseData.BASE_DEVICE_DEFAULT_WIDTH;
    }
}
