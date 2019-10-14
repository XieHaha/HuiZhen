package com.yht.yihuantong.ui.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScalingUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.EditTextLayout;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.ImagePreviewActivity;
import com.yht.yihuantong.ui.adapter.AddImageAdapter;
import com.yht.yihuantong.utils.MatisseUtils;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yht.frame.ui.BaseFragment.RC_PICK_IMG;

/**
 * @author 顿顿
 * @date 19/6/5 17:19
 * @description 填写诊断意见
 */
public class AddDiagnosisActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.edit_layout)
    EditTextLayout editLayout;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.public_title_bar_back)
    ImageView publicTitleBarBack;
    @BindView(R.id.tv_calc_num)
    TextView tvCalcNum;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    /**
     * 照片adapter
     */
    private AddImageAdapter addImageAdapter;
    /**
     * 已上传图片
     */
    private ArrayList<NormImage> imagePaths;
    /**
     * 图片临时数据
     */
    private List<String> paths;
    private int currentUploadImgIndex = -1;
    private Handler dealImgHandler = new Handler(msg -> {
        //图片显示完开始上传图片
        uploadImage(new File(paths.get(currentUploadImgIndex)));
        return true;
    });

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_add_diagnosis;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarBack.setOnClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        addImageAdapter = new AddImageAdapter(R.layout.item_add_image_two, imagePaths);
        addImageAdapter.setOnItemClickListener(this);
        addImageAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(addImageAdapter);
        //占位图
        imagePaths = new ArrayList<>();
        imagePaths.add(new NormImage());
        addImageAdapter.setNewData(imagePaths);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        publicTitleBarMore.setText(R.string.txt_save);
        editLayout.getEditText().setHint(R.string.txt_add_diagnosis_hint);
        tvCalcNum.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        editLayout.getEditText().addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (TextUtils.isEmpty(s.toString())) {
                    publicTitleBarMore.setSelected(false);
                }
                else {
                    publicTitleBarMore.setSelected(true);
                }
            }
        });
    }

    /**
     * 更新个人简介
     */
    private void updateIntroduction(String value) {
        RequestUtils.updateIntroduce(this, loginBean.getToken(), value, this);
    }

    /**
     * 上传图片
     *
     * @param file
     */
    private void uploadImage(File file) {
        ScalingUtils.resizePic(this, file.getAbsolutePath());
        RequestUtils.uploadImgWaterMark(this, loginBean.getToken(), file, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.public_title_bar_back) {
            onFinish();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!TextUtils.isEmpty(imagePaths.get(position).getImagePath()) ||
            !TextUtils.isEmpty(imagePaths.get(position).getImageUrl())) {
            //查看大图
            Intent intent = new Intent(this, ImagePreviewActivity.class);
            ArrayList<NormImage> list = new ArrayList<>();
            for (NormImage image : imagePaths) {
                if (!TextUtils.isEmpty(image.getImagePath()) || !TextUtils.isEmpty(image.getImageUrl())) {
                    //显示水印图片,不显示本地
                    image.setImagePath("");
                    list.add(image);
                }
            }
            intent.putExtra(ImagePreviewActivity.INTENT_URLS, list);
            intent.putExtra(ImagePreviewActivity.INTENT_POSITION, position);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_fade_in, R.anim.keep);
        }
        else {
            permissionHelper.request(new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == BaseData.BASE_ONE) {
            //设置占位图
            imagePaths.set(position, new NormImage());
        }
        else {
            //先移除
            imagePaths.remove(imagePaths.get(position));
            if (!TextUtils.isEmpty(imagePaths.get(position).getImagePath()) ||
                !TextUtils.isEmpty(imagePaths.get(position).getImageUrl())) {
                //占位图
                imagePaths.add(new NormImage());
            }
        }
        addImageAdapter.setNewData(imagePaths);
    }

    @OnClick({ R.id.public_title_bar_more })
    public void onViewClicked() {
        if (publicTitleBarMore.isSelected()) { save(); }
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        int num = 9;
        for (NormImage normImage : imagePaths) {
            if (!TextUtils.isEmpty(normImage.getImageUrl())) {
                num--;
            }
        }
        MatisseUtils.open(this, true, num);
    }

    /**
     * 保存
     */
    private void save() {
        hideSoftInputFromWindow();
        String inputValue = editLayout.getEditText().getText().toString().trim();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.UPLOAD_FILE) {
            String url = (String)response.getData();
            if (imagePaths.size() == 0 || TextUtils.isEmpty(imagePaths.get(0).getImageUrl())) {
                imagePaths.clear();
                NormImage normImage = new NormImage();
                normImage.setImageUrl(url);
                imagePaths.add(normImage);
            }
            else {
                imagePaths.get(1).setImageUrl(url);
            }
            addImageAdapter.setNewData(imagePaths);
            if (currentUploadImgIndex == 0) {
                currentUploadImgIndex = 1;
                dealImgHandler.sendEmptyMessage(0);
            }
            else if (currentUploadImgIndex == 1) {
                currentUploadImgIndex = -1;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == RC_PICK_IMG) {
            paths = Matisse.obtainPathResult(data);
            if (1 == paths.size()) {
                NormImage normImage = new NormImage();
                normImage.setImagePath(paths.get(0));
                imagePaths.set(imagePaths.size() - 1, normImage);
                uploadImage(new File(paths.get(0)));
            }
            else {
                imagePaths.clear();
                currentUploadImgIndex = 0;
                dealImgHandler.sendEmptyMessage(0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0])) {
                openPhoto();
            }
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }

    private void onFinish() {
        if (publicTitleBarMore.isSelected()) {
            new HintDialog(this).setContentString(R.string.txt_save_edit)
                                .setCancleBtnTxt(R.string.txt_not_save)
                                .setOnCancelClickListener(this::finish)
                                .setEnterBtnTxt(R.string.txt_save)
                                .setEnterSelect(true)
                                .setDeleteVisible(View.VISIBLE)
                                .setOnEnterClickListener(this::save)
                                .show();
            return;
        }
        hideSoftInputFromWindow();
        finish();
    }

    private InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast(AddDiagnosisActivity.this, R.string.txt_not_support_input);
                return "";
            }
            return null;
        }

        Pattern emoji = Pattern.compile(BaseUtils.filterImoji, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    };
}
