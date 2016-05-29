package com.me.resume.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.tools.L;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_WX_ID, false);
    	
    	// 将该app注册到微信
    	api.registerApp(Constants.APP_WX_ID);
    	
    	api.handleIntent(getIntent(), this);
    	
    	SendAuth.Req req = new SendAuth.Req();
    	  
    	//授权读取用户信息  
    	req.scope = "snsapi_userinfo";

    	//自定义信息 
    	req.state = "wechat_sdk_demo_test";

    	//向微信发送请求
    	api.sendReq(req);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		handleIntent(intent);
		
        api.handleIntent(intent, this);
	}
	
	private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            //用户同意
        }
    }
	
	
	@Override
	public void onReq(BaseReq req) {
		// 微信发送请求到第三方应用时，会回调到该方法
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//			goToGetMsg();		
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}
	
	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;		
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
		
		StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);
		
		L.d("== wx onReq=="+msg.toString());
	}

	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		
	}
}
