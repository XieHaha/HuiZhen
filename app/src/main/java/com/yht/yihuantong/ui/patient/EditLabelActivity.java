package com.yht.yihuantong.ui.patient;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yht.frame.api.notify.NotifyChangeListenerManager;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.LabelBean;
import com.yht.frame.data.bean.LabelSetBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.SearchLabelAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/8/16 16:48
 * @des 标签编辑
 */
public class EditLabelActivity extends BaseActivity
        implements TextView.OnEditorActionListener, View.OnKeyListener, TagFlowLayout.OnTagClickListener,
                   AdapterView.OnItemClickListener {
    @BindView(R.id.selected_flow)
    FlowLayout selectedFlow;
    @BindView(R.id.all_flow)
    TagFlowLayout allFlow;
    @BindView(R.id.layout_all)
    LinearLayout layoutAll;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.public_title_bar_back)
    ImageView publicTitleBarBack;
    /**
     * 搜索候选
     */
    private SearchLabelAdapter searchLabelAdapter;
    /**
     * 当前患者
     */
    private String patientCode;
    /**
     * 标签集合
     */
    private LabelSetBean labelSetBean;
    /**
     * 标签输入框
     */
    private EditText inputEditText;
    /**
     * 当前处于高亮状态的TextView
     */
    private TextView curHighLightTextView;
    /**
     * 高亮状态
     */
    private boolean curHighLight = false;
    /**
     * 已有标签初始值（判断本次编辑是否修改）
     */
    private String initString;
    /**
     * 上面的标签列表
     */
    private List<String> selectedLabelList = new ArrayList<>();
    /**
     * 所有标签列表
     */
    private List<String> allLabelList = new ArrayList<>();
    /**
     * 搜索结果标签
     */
    private List<String> searchLabels = new ArrayList<>();
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
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.txt_save);
        if (getIntent() != null) {
            patientCode = getIntent().getStringExtra(CommonData.KEY_PATIENT_CODE);
        }
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
        searchLabelAdapter = new SearchLabelAdapter(this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(searchLabelAdapter);
        getAllLabel();
    }

    @Override
    public void initListener() {
        super.initListener();
        publicTitleBarBack.setOnClickListener(this);
        //重新显示光标
        inputEditText.setOnClickListener(v -> {
            inputEditText.setCursorVisible(true);
            //输入框重新获取焦点后，取消标签高亮状态
            setLabelHighLight(false);
        });
        inputEditText.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                HuiZhenLog.i(TAG, "value:" + s.toString());
                searchLabel(s.toString());
            }
        });
        //给候选标签添加监听
        allFlow.setOnTagClickListener(this);
    }

    /**
     * 获取所有标签
     */
    private void getAllLabel() {
        RequestUtils.getLabel(this, loginBean.getToken(), patientCode, this);
    }

    /**
     * 保存所有标签
     */
    private void savePatientLabel() {
        RequestUtils.savePatientLabel(this, loginBean.getToken(), patientCode, selectedLabelList, this);
    }

    /**
     * 初始化数据
     */
    private void initLabelData() {
        //初始化下面标签列表
        if (labelSetBean != null) {
            ArrayList<LabelBean> allLabels = labelSetBean.getDoctorTag();
            for (LabelBean labelTag : allLabels) {
                allLabelList.add(labelTag.getTagName());
            }
            ArrayList<LabelBean> list = labelSetBean.getPatientTag();
            if (list != null) {
                StringBuilder builder = new StringBuilder();
                for (LabelBean bean : list) {
                    builder.append(bean.getTagName());
                    //保存已有标签初始值
                    initString = builder.toString();
                    //添加标签
                    addLabel(bean.getTagName());
                }
            }
        }
        if (allLabelList != null && allLabelList.size() > 0) {
            layoutAll.setVisibility(View.VISIBLE);
        }
        else {
            layoutAll.setVisibility(View.GONE);
        }
        tagAdapter.notifyDataChanged();
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
     * 患者搜索
     */
    private void searchLabel(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            searchLabels.clear();
            for (String string : allLabelList) {
                if (string.contains(tag)) {
                    searchLabels.add(string);
                }
            }
            initSearchList(searchLabels, tag);
        }
        else {
            initSearchList(null, "");
        }
    }

    /**
     * 搜索结果处理
     */
    private void initSearchList(List<String> list, String tag) {
        if (list != null && list.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            layoutAll.setVisibility(View.GONE);
            searchLabelAdapter.setSearchKey(tag);
            searchLabelAdapter.setList(list);
        }
        else {
            layoutAll.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.public_title_bar_more)
    public void onViewClicked() {
        if (publicTitleBarMore.isSelected()) {
            savePatientLabel();
        }
    }

    /**
     * 已选标签的点击事件
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_label:
                TextView textView = (TextView)v;
                removeSelectedLabel(textViewLabels.indexOf(textView), textView);
                break;
            case R.id.public_title_bar_back:
                onFinish();
                break;
            default:
                break;
        }
    }

    /**
     * 搜索候选列表
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addLabel(searchLabels.get(position));
        hideSoftInputFromWindow();
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
            //判断患者标签是否达到上限  患者最多拥有10个标签
            if (selectedLabelList.size() >= BaseData.BASE_PATIENT_LABEL_NUM) {
                ToastUtil.toast(this, R.string.txt_patient_label_over);
                updateAllLabelSelectStatus();
            }
            else {
                addLabel(allLabelList.get(position));
            }
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
            curHighLightTextView = textViewLabels.get(textViewLabels.size() - 1);
            if (curHighLight) {
                //显示光标
                inputEditText.setCursorVisible(true);
                removeSelectedLabel(textViewLabels.size() - 1, curHighLightTextView);
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
            //医生总标签数最多50个
            if (!allLabelList.contains(content)) {
                int allLabelNum = selectedLabelList.size() - selectedPosition.size() + allLabelList.size();
                if (allLabelNum >= BaseData.BASE_DOCTOR_LABEL_NUM) {
                    ToastUtil.toast(this, R.string.txt_doctor_label_over);
                    return;
                }
            }
            //患者最多拥有10个标签
            if (selectedLabelList.size() >= BaseData.BASE_PATIENT_LABEL_NUM) {
                ToastUtil.toast(this, R.string.txt_patient_label_over);
                return;
            }
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
        initNextButton();
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
        initNextButton();
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
    private EditText createInputLabel() {
        inputEditText = (EditText)getLayoutInflater().inflate(R.layout.item_edit_label, null, false);
        inputEditText.setOnEditorActionListener(this);
        inputEditText.setOnKeyListener(this);
        inputEditText.setLayoutParams(params);
        return inputEditText;
    }

    private void onFinish() {
        if (publicTitleBarMore.isSelected()) {
            new HintDialog(this).setContentString(R.string.txt_save_edit)
                                .setCancleBtnTxt(R.string.txt_not_save)
                                .setOnCancelClickListener(this::finish)
                                .setEnterBtnTxt(R.string.txt_save)
                                .setEnterSelect(true)
                                .setOnEnterClickListener(this::savePatientLabel)
                                .show();
            return;
        }
        hideSoftInputFromWindow();
        finish();
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }

    /**
     * 是否保存
     */
    private void initNextButton() {
        StringBuilder builder = new StringBuilder();
        for (String string : selectedLabelList) {
            builder.append(string);
        }
        if (TextUtils.equals(builder.toString(), initString)) {
            publicTitleBarMore.setSelected(false);
        }
        else {
            publicTitleBarMore.setSelected(true);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_LABEL:
                labelSetBean = (LabelSetBean)response.getData();
                initLabelData();
                break;
            case SAVE_PATIENT_LABEL:
                //标签保存成功后 刷新患者列表
                NotifyChangeListenerManager.getInstance().notifyPatientListChanged("");
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }
}
