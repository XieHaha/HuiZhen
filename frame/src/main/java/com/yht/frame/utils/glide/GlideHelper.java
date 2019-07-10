package com.yht.frame.utils.glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yht.frame.R;

/**
 * @author dundun
 */
public final class GlideHelper {
    /**
     * 医生
     */
    private static final RequestOptions OPTIONS = new RequestOptions();
    /**
     * 图片
     */
    private static final RequestOptions OPTIONS_PIC = new RequestOptions();

    public static RequestOptions getOptions(int corner) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(corner);
        return OPTIONS.optionalTransform(roundedCorners)
                      .placeholder(R.mipmap.ic_default_header_r)
                      .error(R.mipmap.ic_default_header_r)
                      .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsPic(int corner) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(corner);
        return OPTIONS_PIC.optionalTransform(roundedCorners).priority(Priority.NORMAL);
    }
}
