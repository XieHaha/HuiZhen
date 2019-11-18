package com.yht.frame.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.R;
import com.yht.frame.data.bean.DepartInfoBean;
import com.yht.frame.widgets.dialog.adapter.ErrorDepartAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dundun
 * 列表
 */
public class ErrorDepartListDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private View view;
    private ImageView ivCancel;
    private TextView tvContent, tvLeft, tvRight;
    private RecyclerView recyclerView;
    private ErrorDepartAdapter dialogListAdapter;
    /**
     * 数据源
     */
    private List<DepartInfoBean> data = new ArrayList<>();
    private String rightString = "继续提交", leftString = "重新选择", contentString;
    private boolean rightSelect, leftSelect;
    /**
     * 隐藏右按钮
     */
    private boolean hideRight;
    private boolean isShow;

    public ErrorDepartListDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog_list);
        initWidget();
    }

    private void initWidget() {
        ivCancel = findViewById(R.id.iv_cancel);
        tvContent = findViewById(R.id.tv_content);
        tvLeft = findViewById(R.id.dialog_simple_left);
        tvRight = findViewById(R.id.dialog_simple_right);
        view = findViewById(R.id.view_line);
        recyclerView = findViewById(R.id.recycler_view);
        dialogListAdapter = new ErrorDepartAdapter(R.layout.item_dialog_list, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(dialogListAdapter);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
    }

    public ErrorDepartListDialog setData(List<DepartInfoBean> data) {
        this.data = data;
        return this;
    }

    public ErrorDepartListDialog setHideRight(boolean hideRight) {
        this.hideRight = hideRight;
        return this;
    }

    public ErrorDepartListDialog setContentString(String contentString) {
        this.contentString = contentString;
        return this;
    }

    public ErrorDepartListDialog setLeftSelect(boolean leftSelect) {
        this.leftSelect = leftSelect;
        return this;
    }

    public ErrorDepartListDialog setLeftString(String leftString) {
        this.leftString = leftString;
        return this;
    }

    public ErrorDepartListDialog setRightString(String rightString) {
        this.rightString = rightString;
        return this;
    }

    public ErrorDepartListDialog setRightSelect(boolean rightSelect) {
        this.rightSelect = rightSelect;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == tvLeft) {
            if (onNextClickListener != null) {
                onNextClickListener.onLeftClick();
            }
        }
        else if (v == tvRight) {
            if (onNextClickListener != null) {
                onNextClickListener.onRightClick();
            }
        }
        dismiss();
    }

    @Override
    public void show() {
        if (!isShow) {
            super.show();
            tvContent.setText(contentString);
            if (data == null) {
                leftString = context.getString(R.string.txt_select_time_again);
            }
            else {
                leftString = context.getString(R.string.txt_select_depart_again);
            }
            tvLeft.setText(leftString);
            if (hideRight) {
                tvRight.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                tvLeft.setSelected(true);
            }
            else {
                tvRight.setText(rightString);
                tvRight.setSelected(true);
                tvLeft.setSelected(false);
            }
            dialogListAdapter.setNewData(data);
            isShow = true;
        }
    }

    private OnNextClickListener onNextClickListener;

    public interface OnNextClickListener {
        void onLeftClick();

        void onRightClick();
    }

    public ErrorDepartListDialog setOnNextClickListener(OnNextClickListener onNextClickListener) {
        this.onNextClickListener = onNextClickListener;
        return this;
    }
}
