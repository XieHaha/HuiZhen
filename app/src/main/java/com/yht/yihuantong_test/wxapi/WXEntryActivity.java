package com.yht.yihuantong_test.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.utils.SharePreferenceUtil;
import com.yht.frame.utils.ToastUtil;
import com.yht.yihuantong.ZycApplication;

/**
 * @author dundun
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = ZycApplication.iwxapi;
        if (iwxapi == null) {
            if (ZycApplication.getInstance().debugMode) {
                iwxapi = WXAPIFactory.createWXAPI(this, BaseData.WE_CHAT_ID_TEST, false);
                iwxapi.registerApp(BaseData.WE_CHAT_ID_TEST);
            }
            else {
                iwxapi = WXAPIFactory.createWXAPI(this, BaseData.WE_CHAT_ID, false);
                iwxapi.registerApp(BaseData.WE_CHAT_ID);
            }
            ZycApplication.iwxapi = iwxapi;
        }
        boolean handleIntent = iwxapi.handleIntent(getIntent(), this);
        if (!handleIntent) {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        this.iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (baseResp instanceof SendAuth.Resp) {
                    String code = ((SendAuth.Resp)baseResp).code;
                    new SharePreferenceUtil(this).putString(CommonData.KEY_WECHAT_LOGIN_SUCCESS_BEAN, code);
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastUtil.toast(this, "拒绝");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtil.toast(this, "取消");
                finish();
                break;
            default:
                finish();
                break;
        }
    }
}