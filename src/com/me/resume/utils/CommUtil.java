package com.me.resume.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.me.resume.tools.L;
import com.whjz.android.text.CommonText;

public class CommUtil {

	private static Toast toast;

	/**
	 * Toast
	 * 
	 * @param context
	 * @param str
	 */
	public static void ToastMsg(Context context, String str) {
		toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * 
	 * @Title:Utils
	 * @Description: 消息提示
	 * @author Comsys-WH1510032
	 * @return 返回类型
	 * @param context
	 * @param resId
	 */
	public static void ToastMsg(Context context, int resId) {
		toast = Toast.makeText(context, getStrValue(context, resId),
				Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * 
	 * @Title:WiVehicleUtil
	 * @Description: 取消toast显示
	 * @author Comsys-WH1510032
	 */
	public static void cancel() {
		if (toast != null) {
			toast.cancel();
		}
	}

	/**
	 * 将字符串转int
	 * 
	 * @param value
	 * @return
	 */
	public static int parseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 将dip转换为px
	 * 
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px转换为dip
	 * 
	 * @param dipValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 
	 * @Title:CommUtil
	 * @Description: 判断文本是否为空
	 * @author Comsys-WH1510032
	 * @return 返回类型
	 * @param textView
	 * @return
	 */
	public static boolean textIsNull(TextView textView) {
		String value = textView.getText().toString().trim();
		if (value.equals("") || value == null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @Title:CommUtil
	 * @Description: 获取文本值
	 * @author Comsys-WH1510032
	 * @return 返回类型
	 * @param textView
	 * @return
	 */
	public static String getTextValue(TextView textView) {
		String value = textView.getText().toString().trim();
		if (!value.equals("") && value != null) {
			return value;
		}
		return "";
	}

	/**
	 * 
	 * @Title:CommUtil
	 * @Description: 判断编辑框是否为空
	 * @author Comsys-WH1510032
	 * @return 返回类型
	 * @param textView
	 * @return
	 */
	public static boolean EditTextIsNull(EditText editText) {
		String value = editText.getText().toString().trim();
		if (value.equals("") || value == null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @Title:CommUtil
	 * @Description: 获取文本值
	 * @author Comsys-WH1510032
	 * @return 返回类型
	 * @param textView
	 * @return
	 */
	public static String getEditTextValue(EditText editText) {
		String value = editText.getText().toString().trim();
		if (!value.equals("") && value != null) {
			return value;
		}
		return "";
	}

	
	/**
	 * 获取资源文件
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static String getStrValue(Context context, int id) {
		return context.getResources().getString(id);
	}

	/**
	 * 获取资源文件
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static int getIntValue(Context context, int id) {
		return context.getResources().getColor(id);
	}
	
	/**
	 * 获取资源文件
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static float getFloatValue(Context context, int id) {
		return context.getResources().getDimension(id);
	}

	/**
	 * 获取资源文件
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static Drawable getDrawable(Context context, int id) {
		return context.getResources().getDrawable(id);
	}

	/**
	 * 获取资源文件
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static int getColorValue(Context context, int id) {
		return context.getResources().getColor(id);
	}

	/**
	 * 获取Array资源文件
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static String[] getArrayValue(Context context, int id) {
		return context.getResources().getStringArray(id);
	}

	/**
	 * 检测当前网络连接的类型<br/>
	 * 注意：需要添加权限&lt;uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE"/&gt;
	 * 
	 * @param context
	 * @return 返回0代表GPRS网络;返回1,代表WIFI网络;返回-1代表网络不可用
	 */
	public static int getNetworkType(Context context) {
		int code = -1;
		try {
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (null != connManager) {
				State state = connManager.getNetworkInfo(
						ConnectivityManager.TYPE_WIFI).getState();
				if (State.CONNECTED == state) {
					code = ConnectivityManager.TYPE_WIFI;
				} else {
					state = connManager.getNetworkInfo(
							ConnectivityManager.TYPE_MOBILE).getState();
					if (State.CONNECTED == state) {
						code = ConnectivityManager.TYPE_MOBILE;
					}
				}
			}
		} catch (Exception e) {
			Log.e("NetworkInfo", "Exception", e);
		}
		return code;
	}
	
	public static boolean isNetworkAvailable(Context mContext) {
		// TODO Auto-generated method stub
		ConnectivityManager connectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {

		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断wifi连接状态
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean isWifiAvailable(Context ctx) {
		ConnectivityManager conMan = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (State.CONNECTED == wifi) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取应用版本号(用于比对更新)
	 */
	public static int getVersionCode(Context context) {
		int verCode = 1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			L.e(e);
		}
		return verCode;
	}

	/**
	 * 获取应用版本名
	 */
	public static String getVersionName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			L.e(e);
		}
		return verName;
	}
	
	/**
     * MD5加密
     * @param str 要加密的密码
     * @return MD5加密的密码
     */
    public static String getMD5(String str) {
		StringBuffer hexString = new StringBuffer();
		if (str != null && str.trim().length() != 0) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(str.getBytes());
				byte[] hash = md.digest();
				for (int i = 0; i < hash.length; i++) {
					if ((0xff & hash[i]) < 0x10) {
						hexString.append("0"
								+ Integer.toHexString((0xFF & hash[i])));
					} else {
						hexString.append(Integer.toHexString(0xFF & hash[i]));
					}
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return hexString.toString();
	}
    
	/**
	 * 隐藏软键盘
	 */
	public static void hideKeyboard(Activity activity) {
		InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activity.getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * 隐藏软键盘
	 */
	public static void hideSoftInput(Activity activity,EditText editText){
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
	    if (isOpen) {
	    	imm.hideSoftInputFromWindow(editText.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	    
	}
	
	public static String getHttpLink(String link){
		if(!"".equals(link) || link != null){
			if(!link.contains("http://")){
				link = CommonText.endPoint + "/" + link;
				if(link.contains("\\")){
					return link.replace("\\", "/");
				}else{
					return link;
				}
			}else{
				return link;
			}
		}else{
			return "";
		}
	}
	
	 /**
     * 生成随机密码
     * @param passLenth 生成的密码长度
     * @return 随机密码
     */
     public static String getPass(int passLenth) {

        StringBuffer buffer = new StringBuffer(
          "0123456789abcdefghijklmnopqrstuvwxyz");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < passLenth; i++) {
             //生成指定范围类的随机数0—字符串长度(包括0、不包括字符串长度)
             sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
     }
     
     
     public static String getRandomNo(int passLenth) {

         StringBuffer buffer = new StringBuffer(
           "123456789");
         StringBuffer sb = new StringBuffer();
         Random r = new Random();
         int range = buffer.length();
         for (int i = 0; i < passLenth; i++) {
              //生成指定范围类的随机数0—字符串长度(包括0、不包括字符串长度)
              sb.append(buffer.charAt(r.nextInt(range)));
         }
         return sb.toString();
      }
}
