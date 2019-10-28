package com.yht.frame.data.type;

/**
 * @author 顿顿
 * @date 19/10/28 18:34
 * @description 受邀方状态
 */
public interface InvitedPartyStatus {
    /**
     * 已接收
     */
    int INVITED_PARTY_STATUS_RECEIVED = 1;
    /**
     * 待接受
     */
    int INVITED_PARTY_STATUS_WAIT = 2;
    /**
     * 已拒绝
     */
    int INVITED_PARTY_STATUS_REFUSED = 3;
}
