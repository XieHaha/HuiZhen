package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/12 14:36
 * @des 检查、转诊 验证
 */
public class ReservationValidateBean implements Serializable {
    private static final long serialVersionUID = -1872270555227088687L;
    private boolean jc;
    private boolean zz;

    public boolean isJc() {
        return jc;
    }

    public void setJc(boolean jc) {
        this.jc = jc;
    }

    public boolean isZz() {
        return zz;
    }

    public void setZz(boolean zz) {
        this.zz = zz;
    }
}
