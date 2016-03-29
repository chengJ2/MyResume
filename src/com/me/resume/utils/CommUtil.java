package com.me.resume.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

public class CommUtil {

	private static Toast toast;
	 /**
	 * Toast
	 * @param context
	 * @param str
	 */
	public static void ToastMsg(Context context,String str){
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
	public static void ToastMsg(Context context,int resId){
		toast = Toast.makeText(context, getStrValue(context,resId), Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/**
	 * 
	 * @Title:WiVehicleUtil
	 * @Description: 取消toast显示
	 * @author Comsys-WH1510032
	 */
	 public static void cancel() {  
		 if (toast!=null) {
			 toast.cancel();  
		}
	 }  
	 
	 /**
    * 将字符串转int
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
	 * 获取资源文件
	 * @param context
	 * @param id
	 * @return
	 */
	public static String getStrValue(Context context,int id){
		return context.getResources().getString(id);
	}
	
	/**
	 * 获取资源文件
	 * @param context
	 * @param id
	 * @return
	 */
	public static Drawable getDrawable(Context context,int id){
		return context.getResources().getDrawable(id);
	}
	
	/**
	 * 获取资源文件
	 * @param context
	 * @param id
	 * @return
	 */
	public static int getColorValue(Context context,int id){
		return context.getResources().getColor(id);
	}
	
	/**
	 * 获取Array资源文件
	 * @param context
	 * @param id
	 * @return
	 */
	public static String[] getArrayValue(Context context,int id){
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
  			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  			if (null != connManager) {
  				State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
  				if (State.CONNECTED == state) {
  					code = ConnectivityManager.TYPE_WIFI;
  				} else {
  					state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
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
   public  static int getVersionCode(Context context){
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
   public  static String getVersionName(Context context){
       String verName = "";
       try {
       	verName = context.getPackageManager().getPackageInfo(
           		context.getPackageName(), 0).versionName;
       } catch (NameNotFoundException e) {
           L.e(e);
       }
       return verName;
   }
}
