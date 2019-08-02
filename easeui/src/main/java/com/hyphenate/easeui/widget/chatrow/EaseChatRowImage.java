package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.utils.EaseImageUtils;
import com.yht.frame.utils.BaseUtils;

import java.io.File;

public class EaseChatRowImage extends EaseChatRowFile {
    protected ImageView imageView;
    private EMImageMessageBody imgBody;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE
                         ? R.layout.ease_row_received_picture
                         : R.layout.ease_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView)findViewById(R.id.percentage);
        imageView = (ImageView)findViewById(R.id.image);
    }

    @Override
    protected void onSetUpView() {
        imgBody = (EMImageMessageBody)message.getBody();
        // received messages
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            return;
        }
        String filePath = imgBody.getLocalUrl();
        String thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
        showImageView(thumbPath, filePath, message);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        if (msg.direct() == EMMessage.Direct.SEND) {
            if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                super.onViewUpdate(msg);
            }
            else {
                if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED) {
                    progressBar.setVisibility(View.INVISIBLE);
                    percentageView.setVisibility(View.INVISIBLE);
                    imageView.setImageResource(R.drawable.ease_default_image);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    percentageView.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.ease_default_image);
                    String thumbPath = imgBody.thumbnailLocalPath();
                    if (!new File(thumbPath).exists()) {
                        // to make it compatible with thumbnail received in previous version
                        thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
                    }
                    showImageView(thumbPath, imgBody.getLocalUrl(), message);
                }
            }
            return;
        }
        // received messages
        if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
            imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
            if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                imageView.setImageResource(R.drawable.ease_default_image);
            }
            else {
                progressBar.setVisibility(View.INVISIBLE);
                percentageView.setVisibility(View.INVISIBLE);
                imageView.setImageResource(R.drawable.ease_default_image);
            }
        }
        else if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED) {
            if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                progressBar.setVisibility(View.VISIBLE);
                percentageView.setVisibility(View.VISIBLE);
            }
            else {
                progressBar.setVisibility(View.INVISIBLE);
                percentageView.setVisibility(View.INVISIBLE);
            }
        }
        else {
            progressBar.setVisibility(View.GONE);
            percentageView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.ease_default_image);
            String thumbPath = imgBody.thumbnailLocalPath();
            if (!new File(thumbPath).exists()) {
                // to make it compatible with thumbnail received in previous version
                thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
            }
            showImageView(thumbPath, imgBody.getLocalUrl(), message);
        }
    }

    /**
     * load image into image view
     */
    private void showImageView(final String thumbernailPath, final String localFullSizePath, final EMMessage message) {
        // first check if the thumbnail image already loaded into cache s
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            calcPictureSize(bitmap);
        }
        else {
            imageView.setImageResource(R.drawable.ease_default_image);
            DownLoadImageTask downLoadImageTask = new DownLoadImageTask();
            downLoadImageTask.execute(new String[] { thumbernailPath, localFullSizePath });
        }
    }

    class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        String s = "";

        @Override
        protected Bitmap doInBackground(String... params) {
            s = params[0];
            File file = new File(params[0]);
            if (file.exists()) {
                return EaseImageUtils.decodeScaleImage(params[0], 160, 160);
            }
            else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 160, 160);
            }
            else {
                if (message.direct() == EMMessage.Direct.SEND) {
                    if (params[1] != null && new File(params[1]).exists()) {
                        return EaseImageUtils.decodeScaleImage(params[2], 160, 160);
                    }
                    else {
                        return null;
                    }
                }
                else {
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(Bitmap resource) {
            if (resource != null) {
                calcPictureSize(resource);
                EaseImageCache.getInstance().put(s, resource);
            }
        }
    }

    /**
     * 计算图片大小
     *
     * @param resource
     */
    private void calcPictureSize(Bitmap resource) {
        //  根据图片尺寸重新计算ImageView的大小
        final ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        //图片实际宽高
        int width = resource.getWidth();
        int height = resource.getHeight();
        //图片显示最大及最小限制值
        int maxValue = BaseUtils.dp2px(getContext(), 140);
        int minValue = BaseUtils.dp2px(getContext(), 80);
        //根据图片宽高比例 处理ImageView宽高
        if (width > height) {
            float scale = (float)maxValue / width;
            layoutParams.width = maxValue;
            layoutParams.height = height * scale > minValue ? (int)(height * scale) : minValue;
        }
        else {
            float scale = (float)maxValue / height;
            layoutParams.width = width * scale > minValue ? (int)(width * scale) : minValue;
            layoutParams.height = maxValue;
        }
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(resource);
    }
}
