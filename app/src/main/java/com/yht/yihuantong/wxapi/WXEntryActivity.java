package com.yht.yihuantong.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.utils.ToastUtil;
import com.zyc.doctor.ZycApplication;
import com.zyc.doctor.ui.login.LoginOptionsActivity;

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
            iwxapi = WXAPIFactory.createWXAPI(this, BaseData.WECHAT_ID, false);
            iwxapi.registerApp(BaseData.WECHAT_ID);
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
                    Intent intent = new Intent(this, LoginOptionsActivity.class);
                    intent.putExtra(CommonData.KEY_PUBLIC, code);
                    startActivity(intent);
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