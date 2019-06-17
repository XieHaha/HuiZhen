package com.zyc.doctor.ui.check.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.check.SelectCheckTypeActivity;
import com.zyc.doctor.ui.check.listener.OnCheckListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 确认提交
 */
public class SubmitFragment extends BaseFragment {
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.fullistview)
    FullListView fullistview;
    @BindView(R.id.layout_check_root)
    LinearLayout layoutCheckRoot;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.rb_self)
    RadioButton rbSelf;
    @BindView(R.id.rb_medicare)
    RadioButton rbMedicare;
    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.iv_upload_one)
    ImageView ivUploadOne;
    @BindView(R.id.iv_delete_one)
    ImageView ivDeleteOne;
    @BindView(R.id.layout_upload_one)
    RelativeLayout layoutUploadOne;
    @BindView(R.id.tv_submit_next)
    TextView tvSubmitNext;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_submit;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @OnClick({ R.id.layout_select_check_type, R.id.tv_delete_all, R.id.layout_upload_one, R.id.tv_submit_next })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_select_check_type:
                startActivity(new Intent(getContext(), SelectCheckTypeActivity.class));
                break;
            case R.id.tv_delete_all:
                break;
            case R.id.layout_upload_one:
                break;
            case R.id.tv_submit_next:
                if (checkListener != null) {
                    checkListener.onStepThree();
                }
                break;
            default:
                break;
        }
    }

    private OnCheckListener checkListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.checkListener = onCheckListener;
    }
}
