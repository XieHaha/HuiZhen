package com.yht.yihuantong.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.SingleTextAdapter;

import java.util.List;

/**
 * @author dundun
 * 版本更新
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private RecyclerView recyclerView;
    private ImageView ivCancel;
    private SingleTextAdapter singleTextAdapter;
    private List<String> data;

    public UpdateDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog_update);
        initWidget();
    }

    private void initWidget() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ivCancel = findViewById(R.id.iv_cancel);
        singleTextAdapter = new SingleTextAdapter(R.layout.item_text_single, data);
        recyclerView.setAdapter(singleTextAdapter);
        ivCancel.setOnClickListener(this);
    }

    /**
     * 列表数据
     *
     * @param data
     */
    public UpdateDialog setData(List<String> data) {
        this.data = data;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                break;
            case R.id.tv_update:
                break;
            default:
                break;
        }
        dismiss();
    }
}
