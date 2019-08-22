package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/7/4 21:11
 * @des 医生列表父bean
 */
public class ReceiverBean implements Serializable {
    private static final long serialVersionUID = -4283326755396204951L;
    private ArrayList<ReceiverDoctorBean> friend;
    private ArrayList<ReceiverDoctorBean> other;

    public ArrayList<ReceiverDoctorBean> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<ReceiverDoctorBean> friend) {
        this.friend = friend;
    }

    public ArrayList<ReceiverDoctorBean> getOther() {
        return other;
    }

    public void setOther(ArrayList<ReceiverDoctorBean> other) {
        this.other = other;
    }
}
