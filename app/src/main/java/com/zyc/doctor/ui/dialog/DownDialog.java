package com.zyc.doctor.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.SingleTextAdapter;
import com.zyc.doctor.ui.dialog.listener.OnMediaItemClickListener;
import com.zyc.doctor.ui.dialog.listener.OnTitleItemClickListener;

import java.util.List;

/**
 * @author dundun
 */
public class DownDialog extends Dialog implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    private Context context;
    private RecyclerView recyclerView;
    private TextView tvCancel;
    private List<String> data;
    private SingleTextAdapter singleTextAdapter;
    private OnMediaItemClickListener onMediaItemClickListener;
    private OnTitleItemClickListener onTitleItemClickListener;

    public DownDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog_down);
        initWidget();
        init();
    }

    private void initWidget() {
        recyclerView = findViewById(R.id.view_down_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        tvCancel = findViewById(R.id.tv_single_txt);
        singleTextAdapter = new SingleTextAdapter(R.layout.item_text_single, data);
        recyclerView.setAdapter(singleTextAdapter);
        setCanceledOnTouchOutside(true);
        singleTextAdapter.setOnItemClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void init() {
        setCancelable(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = window.getAttributes();
        //设置dialog的宽度为当前手机屏幕的宽度
        p.width = d.getWidth();
        window.setAttributes(p);
    }

    /**
     * 列表数据
     *
     * @param data
     */
    public void setData(List<String> data) {
        this.data = data;
    }

    /**
     * 列表点击回调
     *
     * @param onMediaItemClickListener
     */
    public void setOnMediaItemClickListener(OnMediaItemClickListener onMediaItemClickListener) {
        this.onMediaItemClickListener = onMediaItemClickListener;
    }

    public void setOnTitleItemClickListener(OnTitleItemClickListener onTitleItemClickListener) {
        this.onTitleItemClickListener = onTitleItemClickListener;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (onMediaItemClickListener != null) {
            onMediaItemClickListener.onMediaItemClick(position);
        }
        if (onTitleItemClickListener != null) {
            onTitleItemClickListener.onTitleItemClick(position);
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
