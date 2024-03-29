package com.yht.yihuantong.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.yht.frame.api.DirHelper;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.imagepreview.transformer.ImageTransformer;
import com.yht.frame.widgets.imagepreview.utils.NavigatorPageIndex;
import com.yht.frame.widgets.imagepreview.view.ImageLoadingView;
import com.yht.frame.widgets.imagepreview.view.ImagePreviewView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.FileUrlUtil;
import com.yht.yihuantong.utils.MatisseUtils;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dundun
 */
public class ImagePreviewActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener {
    public static final String INTENT_URLS = "intent_urls";
    public static final String INTENT_POSITION = "intent_position";
    /**
     * 图片对象
     */
    private ArrayList<NormImage> urls;
    private ArrayList<ImagePreviewView> imgPreViews = new ArrayList<>();
    private RelativeLayout layoutTitle;
    private ImageView ivBack;
    private TextView tvUpdate;
    private TouchImageAdapter touchImageAdapter;
    /**
     * 图片加载动画view
     */
    private ImageLoadingView mLoadingView;
    /**
     * 页面游标
     */
    private NavigatorPageIndex indicator;
    /**
     * 当前page 游标
     */
    private int currentIndex;
    /**
     * 是否显示title
     */
    private boolean showTitle;
    /**
     * 图片加载  开始
     */
    private static final int LOAD_START = 0;
    /**
     * 图片加载  失败
     */
    private static final int LOAD_ERROR = 100;
    /**
     * 图片加载  成功
     */
    private static final int LOAD_SUCCESS = 200;
    /**
     * 图片加载  取消
     */
    private static final int LOAD_CANCEL = 300;
    /**
     * 选择图片
     */
    public static final int RC_PICK_IMG = 0x0001;
    /**
     * 图片  裁剪
     */
    public static final int RC_CROP_IMG = RC_PICK_IMG + 1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_START:
                    mLoadingView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_ERROR:
                    mLoadingView.setVisibility(View.GONE);
                    ToastUtil.toast(ImagePreviewActivity.this, R.string.toast_load_image_error);
                    break;
                case LOAD_SUCCESS:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case LOAD_CANCEL:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    public int getLayoutID() {
        return R.layout.activity_image_view;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Intent intent = getIntent();
        if (intent != null) {
            urls = (ArrayList<NormImage>) intent.getSerializableExtra(INTENT_URLS);
            currentIndex = intent.getIntExtra(INTENT_POSITION, 0);
            showTitle = intent.getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }
        if (showTitle) {
            layoutTitle = findViewById(R.id.layout_title);
            ivBack = findViewById(R.id.public_title_bar_back);
            tvUpdate = findViewById(R.id.public_title_bar_more);
            layoutTitle.setVisibility(View.VISIBLE);
            ivBack.setOnClickListener(this);
            tvUpdate.setOnClickListener(this);
        }
        mLoadingView = findViewById(R.id.act_image_view_loading);
        //图片游标
        indicator = findViewById(R.id.act_image_view_page_indicator);
        indicator.initPageIndex(urls.size());
        //当只有一张图，不显示图片游标
        if (urls.size() == 1) {
            indicator.setVisibility(View.GONE);
        }
        ViewPager imgViewPager = findViewById(R.id.act_image_view_viewpager);
        //滑动效果
        imgViewPager.setPageTransformer(true, new ImageTransformer());
        imgViewPager.addOnPageChangeListener(this);
        imgViewPager.setAdapter(touchImageAdapter = new TouchImageAdapter());
        imgViewPager.setCurrentItem(currentIndex);
        bindImages();
    }

    private void bindImages() {
        if (imgPreViews.size() > 0) {
            imgPreViews.clear();
        }
        for (int i = 0; i < urls.size(); i++) {
            imgPreViews.add(i, new ImagePreviewView(this, mHandler));
        }
        touchImageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        indicator.changePageIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_title_bar_back:
                onBackPressed();
                break;
            case R.id.public_title_bar_more:
                MatisseUtils.open(this, true, 1, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK_IMG:
                headerImageCallBack(data);
                break;
            case RC_CROP_IMG:
                uploadImage(cutFile);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File cutFile;
    private String headerImageUrl;

    /**
     * 上传图片
     */
    private void uploadImage(File file) {
        RequestUtils.uploadImg(this, loginBean.getToken(), file, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.UPLOAD_FILE) {
            headerImageUrl = (String) response.getData();
            NormImage normImage = new NormImage();
            normImage.setImageUrl(headerImageUrl);
            urls.clear();
            urls.add(normImage);
            bindImages();
        }
    }

    private void headerImageCallBack(Intent data) {
        List<Uri> uris = Matisse.obtainResult(data);
        List<String> paths = Matisse.obtainPathResult(data);
        if (null != paths && 0 != paths.size()) {
            String fileName = "corp" + System.currentTimeMillis() + ".jpg";
            cutFile = new File(DirHelper.getPathCache(), fileName);
            startCutImg(uris.get(0), Uri.fromFile(cutFile));
        }
    }

    /**
     * 图片裁剪
     */
    private void startCutImg(Uri uri, Uri cutUri) {
        startActivityForResult(getCutImageIntent(uri, cutUri), RC_CROP_IMG);
    }

    /**
     * 裁剪图片
     *
     * @param originUri  裁剪前
     * @param cutFileUri 裁剪后
     */
    public Intent getCutImageIntent(Uri originUri, Uri cutFileUri) {
        //系统裁剪
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 在Android N中，为了安全起见，您必须获得“写入或读取Uri文件”的权限。如果您希望系统照片裁剪您的“uri文件”，那么您 必须允许系统照片。
        intent.setDataAndType(originUri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        if (Build.BRAND.toUpperCase().contains(BaseData.BASE_HONOR_NAME) ||
                Build.BRAND.toUpperCase().contains(BaseData.BASE_HUAWEI_NAME)) {
            //华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cutFileUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        return intent;
    }


    class TouchImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return urls.size();
        }

        @NonNull
        @Override
        public View instantiateItem(@NonNull final ViewGroup container, final int position) {
            ImagePreviewView currentPreviewView = imgPreViews.get(position);
            currentPreviewView.loadingImageAsync(urls.get(position).getImagePath(),
                    FileUrlUtil.addTokenToUrl(urls.get(position).getImageUrl()));
            container.addView(currentPreviewView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            return currentPreviewView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position,
                                @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(headerImageUrl)) {
            Intent intent = new Intent();
            intent.putExtra(CommonData.KEY_PUBLIC_STRING, headerImageUrl);
            setResult(RESULT_OK, intent);
        }
        finish();
        overridePendingTransition(R.anim.keep, R.anim.fade_out);
    }
}
