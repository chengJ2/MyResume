package com.me.resume.thirdparty;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.me.resume.model.QQInfo;
import com.me.resume.tools.L;
import com.tencent.open.utils.HttpUtils.HttpStatusException;
import com.tencent.open.utils.HttpUtils.NetworkUnavailableException;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ授权请求
 */
public class QQLogin {
	private static final String SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,add_share";
	private Context context;
	private QQInfo qq;
	private Tencent mTencent;
//	private NotifyData notify;

	public QQLogin(Context context, Tencent mTencent) {
		this.context = context;
		this.mTencent = mTencent;
	}
	
	/*public QQLogin(Context context, Tencent mTencent, NotifyData notify) {
		this.context = context;
		this.mTencent = mTencent;
		this.notify = notify;
	}*/

	/**
	 * qq登录授权
	 */
	public void qqRegistor() {
		qq = new QQInfo();
		if (!mTencent.isSessionValid()) {
			mTencent.login((Activity) context, SCOPE, new BaseUiListener());
		} else {
			mTencent.logout(context);
		}
	}

	/**
	 * 调用SDK已经封装好的接口时，
	 * 例如：登录、快速支付登录、应用分享、应用邀请等接口，需传入该回调的实例
	 * @author Administrator
	 *
	 */
	private class BaseUiListener implements IUiListener {
		
		@Override
		public void onComplete(Object response) {
			Log.i("QQ", "onComplete:");
			if (null == response) {
				Toast.makeText(context, "返回为空,登录失败", Toast.LENGTH_SHORT).show();
	            return;
	        }
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				Toast.makeText(context, "返回为空,登录失败", Toast.LENGTH_SHORT).show();
				return;
			}

			qq = new QQInfo();
			doComplete(jsonResponse);
		}

		protected void doComplete(JSONObject values) {
			TencentQQToken token = new TencentQQToken();
			try {
				qq.setOpenId(values.getString("openid"));
				token.setOpenid(values.getString("openid"));
				token.setAccess_token(values.getString("access_token"));
				token.setExpires_in(values.getString("expires_in"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			QQTokenKeeper.writeAccessToken(context, token);
		}

		@Override
		public void onError(UiError e) {
			Toast.makeText(context, e.errorMessage, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(context, "取消操作", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 使用requestAsync、request等通用方法调用sdk未封装的接口时，
	 * 	例如上传图片、查看相册等，需传入该回调的实例。
	 * @author Administrator
	 *
	 */
	public class BaseApiListener implements IRequestListener {
		private String mScope = "all";
		private Boolean mNeedReAuth = false;

		public BaseApiListener(String scope, boolean needReAuth) {
			mScope = scope;
			mNeedReAuth = needReAuth;
		}

		@Override
		public void onComplete(JSONObject response) {
			try {
				L.d("===BaseApiListener(onComplete)====" + response.toString());
				int ret = response.getInt("ret");
				if (ret == 100030) {
					if (mNeedReAuth) {
						Runnable r = new Runnable() {
							public void run() {
								mTencent.reAuth((Activity) context, mScope, new BaseUiListener());
							}
						};
						((Activity) context).runOnUiThread(r);
					}
				}
				qq.setUserName(response.getString("nickname"));
				qq.setSex(response.getString("gender"));
				qq.setPhotoPath(response.getString("figureurl_qq_2"));
				qq.setAddress(response.getString("province") + response.getString("city"));

				handler.sendMessage(handler.obtainMessage(1));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onConnectTimeoutException(ConnectTimeoutException arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onHttpStatusException(HttpStatusException arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onIOException(IOException arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onJSONException(JSONException arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onMalformedURLException(MalformedURLException arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSocketTimeoutException(SocketTimeoutException arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onUnknowException(Exception arg0) {
			// TODO Auto-generated method stub
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				new PatformInfoAsyncTask(context, "qq", qq).execute();
				break;
			default:
				break;
			}
		};
	};
}
