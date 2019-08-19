package com.yht.yihuantong.ui.patient;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/8/16 16:48
 * @des 标签编辑
 */
public class EditLabelActivity extends BaseActivity
        implements TextView.OnEditorActionListener, View.OnKeyListener, TagFlowLayout.OnTagClickListener {
    @BindView(R.id.selected_flow)
    FlowLayout selectedFlow;
    @BindView(R.id.all_flow)
    TagFlowLayout allFlow;
    @BindView(R.id.layout_all)
    LinearLayout layoutAll;
    private AppCompatEditText inputEditText;
    /**
     * 当前处于高亮状态的TextView
     */
    private TextView curHighLightTextView;
    /**
     * 当前高亮状态view的index
     */
    private int curHighLightPosition;
    /**
     * 高亮状态
     */
    private boolean curHighLight = false;
    /**
     * 上面的标签列表
     */
    private List<String> selectedLabelList = new ArrayList<>();
    /**
     * 所有标签列表
     */
    private List<String> allLabelList = new ArrayList<>();
    /**
     * 存放已有标签
     */
    final List<TextView> textViewLabels = new ArrayList<>();
    /**
     * 存放选中的
     */
    final Set<Integer> selectedPosition = new HashSet<>();
    /**
     * 标签适配器
     */
    private TagAdapter<String> tagAdapter;
    private LinearLayout.LayoutParams params;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_edit_label;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //设置边界
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                               ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(BaseUtils.dp2px(this, 6), BaseUtils.dp2px(this, 10), BaseUtils.dp2px(this, 6), 0);
        //添加标签输入框到layout中
        selectedFlow.addView(createInputLabel());
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //初始化适配器
        tagAdapter = new TagAdapter<String>(allLabelList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                return createNewLabel(s, allFlow, false);
            }
        };
        allFlow.setAdapter(tagAdapter);
        initTempData();
    }

    @Override
    public void initListener() {
        super.initListener();
        //重新显示光标
        inputEditText.setOnClickListener(v -> {
            inputEditText.setCursorVisible(true);
            //输入框重新获取焦点后，取消标签高亮状态
            setLabelHighLight(false);
        });
        //给候选标签添加监听
        allFlow.setOnTagClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initTempData() {
        //初始化上面标签
        ArrayList<String> list = new ArrayList<>();
        list.add("同事");
        list.add("亲人");
        list.add("同学");
        list.add("朋友");
        list.add("知己");
        //初始化下面标签列表
        allLabelList.addAll(list);
        allLabelList.add("异性朋友");
        allLabelList.add("高中同学");
        allLabelList.add("大学同学");
        allLabelList.add("社会朋友");
        for (int i = 0; i < list.size(); i++) {
            //初始化已有标签
            addLabel(list.get(i));
        }
    }

    /**
     * 更新所有标签选中状态
     */
    private void updateAllLabelSelectStatus() {
        selectedPosition.clear();
        //根据上面标签来判断下面的标签是否含有上面的标签
        for (int i = 0; i < selectedLabelList.size(); i++) {
            for (int j = 0; j < allLabelList.size(); j++) {
                if (selectedLabelList.get(i).equals(allLabelList.get(j))) {
                    //设为选中
                    selectedPosition.add(j);
                }
            }
        }
        tagAdapter.setSelectedList(selectedPosition);
        tagAdapter.notifyDataChanged();
    }

    /**
     * 已选标签的点击事件
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        TextView textView = (TextView)v;
        removeSelectedLabel(textViewLabels.indexOf(textView), textView);
    }

    /**
     * 候选标签点击事件
     */
    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        //输入框重新获取焦点后，取消标签高亮状态
        setLabelHighLight(false);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < textViewLabels.size(); i++) {
            list.add(textViewLabels.get(i).getText().toString());
        }
        //如果上面包含点击的标签就删除
        if (list.contains(allLabelList.get(position))) {
            for (int i = 0; i < list.size(); i++) {
                if (allLabelList.get(position).equals(list.get(i))) {
                    removeSelectedLabel(i, textViewLabels.get(i));
                    break;
                }
            }
        }
        else {
            addLabel(allLabelList.get(position));
        }
        return false;
    }

    /**
     * 监听回车键 确认加入标签
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String content = inputEditText.getText().toString();
            addLabel(content);
        }
        return true;
    }

    /**
     * 监听软键盘删除键
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        String s = inputEditText.getText().toString();
        if (textViewLabels.size() == 0 || !TextUtils.isEmpty(s)) { return false; }
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            curHighLightPosition = textViewLabels.size() - 1;
            curHighLightTextView = textViewLabels.get(curHighLightPosition);
            if (curHighLight) {
                //显示光标
                inputEditText.setCursorVisible(true);
                removeSelectedLabel(curHighLightPosition, curHighLightTextView);
            }
            else {
                //隐藏输入框光标
                inputEditText.setCursorVisible(false);
                setLabelHighLight(true);
            }
        }
        return false;
    }

    /**
     * 添加标签
     */
    private void addLabel(String content) {
        if (TextUtils.isEmpty(content)) { return; }
        if (!isExist(content)) {
            //添加标签
            final TextView textLabel = createNewLabel(content, null, true);
            textViewLabels.add(textLabel);
            selectedFlow.addView(textLabel);
            //添加点击事件，点击变成选中状态，选中状态下被点击则删除
            textLabel.setOnClickListener(this);
            //让输入框在最后一个位置上
            inputEditText.bringToFront();
            //清空编辑框
            inputEditText.setText("");
        }
        if (selectedLabelList.contains(content)) {
            //防止重复添加
            selectedLabelList.remove(content);
        }
        //重新将新的标签添加在最后（顺序重排）
        selectedLabelList.add(content);
        updateAllLabelSelectStatus();
    }

    /**
     * 判断标签是否已经存在
     */
    private boolean isExist(String content) {
        //判断是否重复
        for (TextView tag : textViewLabels) {
            String tempStr = tag.getText().toString();
            if (tempStr.equals(content)) {
                tag.bringToFront();
                //让输入框在最后一个位置上
                inputEditText.bringToFront();
                //清空编辑框
                inputEditText.setText("");
                return true;
            }
        }
        return false;
    }

    /**
     * 设置标签为高亮状态
     */
    private void setLabelHighLight(boolean highLight) {
        if (curHighLightTextView == null) { return; }
        curHighLight = highLight;
        if (highLight) {
            curHighLightTextView.setBackgroundResource(R.drawable.corner15_1491fc_bg);
            curHighLightTextView.setTextColor(ContextCompat.getColor(this, R.color.color_ffffff));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_delete_white);
            if (drawable != null) { drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); }
            curHighLightTextView.setCompoundDrawables(null, null, drawable, null);
        }
        else {
            curHighLightTextView.setBackgroundResource(R.drawable.corner28_stroke1_1491fc);
            curHighLightTextView.setTextColor(ContextCompat.getColor(this, R.color.color_1491fc));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_delete_blue);
            if (drawable != null) { drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); }
            curHighLightTextView.setCompoundDrawables(null, null, drawable, null);
        }
    }

    /**
     * 移除选中的标签
     */
    private void removeSelectedLabel(int position, TextView target) {
        curHighLight = false;
        selectedLabelList.remove(target.getText().toString());
        selectedFlow.removeView(target);
        textViewLabels.remove(position);
        updateAllLabelSelectStatus();
    }

    /**
     * 创建一个正常状态的标签
     */
    private TextView createNewLabel(String label, ViewGroup parent, boolean selected) {
        TextView textView = (TextView)getLayoutInflater().inflate(R.layout.item_text_label, parent, false);
        textView.setSelected(selected);
        textView.setLayoutParams(params);
        if (parent != null) {
            textView.setCompoundDrawables(null, null, null, null);
        }
        textView.setText(label);
        return textView;
    }

    /**
     * 创建输入标签
     */
    private AppCompatEditText createInputLabel() {
        inputEditText = (AppCompatEditText)getLayoutInflater().inflate(R.layout.item_edit_label, null, false);
        inputEditText.setOnEditorActionListener(this);
        inputEditText.setOnKeyListener(this);
        inputEditText.setLayoutParams(params);
        return inputEditText;
    }
}
