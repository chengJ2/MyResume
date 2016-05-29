package com.me.resume.thirdparty;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.model.SinaInfo;
import com.me.resume.tools.L;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * sina 登录
 * @author Administrator
 *
 */
public class SinaLogin {

	public static final String REDIRECT_URL = "http://www.sina.com";
	
	public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
	
    private AuthInfo mAuthInfo;
    
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    
    private Activity context;
    
    private SinaInfo sina = null;
    
    public SinaLogin(Activity context){
    	this.context = context;
    }
    
    /**
	 * qq登录授权
	 */
	public void sinaRegistor() {
		sina = new SinaInfo();
		if (!mAccessToken.isSessionValid()) {
			mAuthInfo = new AuthInfo(context, Constants.APP_SINA_ID, REDIRECT_URL,SCOPE);
	        mSsoHandler = new SsoHandler(context, mAuthInfo);
	        mSsoHandler.authorizeClientSso(new AuthListener());
		}else{
			
		}
		
	}
    
	// 重写Activity#onActivityResult方法
	public void onSinaActivityResult(int requestCode, int resultCode, Intent data){
		if (mSsoHandler != null) {
	        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }

	}
    
    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息 
            String  phoneNum =  mAccessToken.getPhoneNum();
            
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);
                
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(context, mAccessToken);
                Toast.makeText(context, 
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = context.getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(context, 
                   R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(context, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                L.d(response);
                JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					sina = new SinaInfo();
					sina.setId(jsonObject.optString("id", ""));
					sina.setIdstr(jsonObject.optString("idstr", ""));
					sina.setScreen_name(jsonObject.optString("screen_name", ""));
					sina.setName(jsonObject.optString("name", ""));
					sina.setProvince(jsonObject.optInt("province", -1));
					sina.setCity(jsonObject.optInt("city", -1));
					sina.setAvatar_large(jsonObject.optString("avatar_large",""));
					
					handler.sendMessage(handler.obtainMessage(1));
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            L.e(e.getMessage());
            Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    };
    
    /**
     * 显示当前 Token 信息。
     * 
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = context.getString(R.string.weibosdk_demo_token_to_string_format_1);
        L.d("====updateTokenView==1===" +String.format(format, mAccessToken.getToken(), date) );
        
        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = context.getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        L.d("====updateTokenView==2===" +message);
    }
    
    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    public class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }     

        @Override
        public void onWeiboException(WeiboException e) {
            
        }
    }
    
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				new PatformInfoAsyncTask(context, "qq", sina).execute();
				break;
			default:
				break;
			}
		};
	};
}
