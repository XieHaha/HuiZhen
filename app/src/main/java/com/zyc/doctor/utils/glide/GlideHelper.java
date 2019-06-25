package com.zyc.doctor.utils.glide;

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
    public static final RequestOptions OPTIONS = new RequestOptions();
    /**
     * 患者
     */
    public static final RequestOptions OPTIONS_P = new RequestOptions();
    /**
     * 图片
     */
    public static final RequestOptions OPTIONS_PIC = new RequestOptions();
    /**
     * 图片
     */
    public static final RequestOptions OPTIONS_HOSPITAL_PIC = new RequestOptions();

    public static RequestOptions getOptions() {
        return OPTIONS.centerCrop()
                      .placeholder(R.mipmap.ic_launcher_round)
                      .error(R.mipmap.ic_launcher_round)
                      .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsP() {
        return OPTIONS_P.centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round)
                        .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsPic(int corner) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(corner);
        return OPTIONS_PIC.optionalTransform(roundedCorners)
                          .placeholder(R.mipmap.ic_launcher_round)
                          .error(R.mipmap.ic_launcher_round)
                          .priority(Priority.NORMAL);
    }

    public static RequestOptions getOptionsHospitalPic() {
        return OPTIONS_HOSPITAL_PIC.centerCrop()
                                   .placeholder(R.mipmap.ic_launcher_round)
                                   .error(R.mipmap.ic_launcher_round)
                                   .priority(Priority.NORMAL);
    }
}
