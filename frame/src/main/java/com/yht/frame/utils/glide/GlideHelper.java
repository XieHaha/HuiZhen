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
    /**
     * 图片 大图
     */
    private static final RequestOptions OPTIONS_PIC_BIG = new RequestOptions();

    public static RequestOptions getOptions(int corner) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(corner);
        return OPTIONS.optionalTransform(roundedCorners)
                .placeholder(R.mipmap.ic_default_header_r)
                .error(R.mipmap.ic_default_header_r)
                .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsPic(int corner) {
        return OPTIONS_PIC.transform(new CenterCropRoundCornerTransform(corner)).priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsPic() {
        return OPTIONS_PIC.priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsPicBig() {
        return OPTIONS_PIC_BIG.placeholder(R.mipmap.icon_loading_img)
                .error(R.mipmap.icon_load_faild_img)
                .priority(Priority.NORMAL);
    }
}
