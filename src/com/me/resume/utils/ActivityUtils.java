package com.me.resume.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.me.resume.R;

public class ActivityUtils {

	/**
	 * @描述：内部之间的跳转 <br>
	 * @param src
	 *            当前activity
	 * @param obj
	 *            目标activity字符串
	 * @param finish
	 *            是否关闭
	 */
	
	public static void startActivity(Activity src) {
		src.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		((Activity) src).finish();
	}
	
	public static void startActivity(Activity src, String obj) {
		try {
			Class className = Class.forName(obj);
			Intent intent = new Intent(src, className);
			src.startActivity(intent);
			src.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void startActivity(Activity src, String obj, boolean finish) {
		try {
			Class className = Class.forName(obj);
			Intent intent = new Intent(src, className);
			src.startActivity(intent);
			src.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			if (finish) {
				((Activity) src).finish();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void startActivity(Activity src, String obj, boolean isAnim, boolean finish) {
		try {
			Class className = Class.forName(obj);
			Intent intent = new Intent(src, className);
			src.startActivity(intent);
			if (isAnim) {
				src.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
			if (finish) {
				((Activity) src).finish();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @描述：内部之间的跳转 <br>
	 * @param src
	 *            当前activity
	 * @param obj
	 *            目标activity字符串
	 * @param data
	 *            数据
	 */
	public static void startActivityPro(Activity src, String obj,String key, String data) {
		try {
			Class className = Class.forName(obj);
			Intent intent = new Intent(src, className);
			intent.putExtra(key, data);
			src.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void startActivityPro(Activity src, String obj,String key, String data, boolean isAnim, boolean finish) {
		try {
			Class className = Class.forName(obj);
			Intent intent = new Intent(src, className);
			intent.putExtra(key, data);
			src.startActivity(intent);
			if (isAnim) {
				src.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
			if (finish) {
				((Activity) src).finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @描述：内部之间的跳转，有返回值 <br>
	 * @param src
	 *            当前activity
	 * @param obj
	 *            目标activity字符串
	 * @param finish
	 *            是否关闭
	 * @param bundle
	 *            传递时附带的参数
	 * @param requestCode
	 *            如果值大于0，则会在退出调用界面后返回该值到调用该方法的界面中 If >= 0, this code will be
	 *            returned in onActivityResult() when the activity exits.
	 */
	public static void startActivityForResult(Activity src, String obj,
			boolean finish, Bundle bundle, int requestCode) {
		try {
			Class className = Class.forName(obj);
			Intent intent = new Intent(src, className);
			intent.putExtras(bundle);
			src.startActivityForResult(intent, requestCode);
			if (finish) {
				((Activity) src).finish();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @描述：内部之间的跳转，没有返回值 <br>
	 * @param src
	 *            当前activity
	 * @param obj
	 *            目标activity字符串
	 * @param bundle
	 *            传递时附带的参数
	 * @param finish
	 *            是否关闭
	 */
	public static void startActivity(Activity src, String obj, Bundle bundle,
			boolean isAnim,boolean finish) {
		try {
			Class className = Class.forName(obj);
			Intent intent = new Intent(src, className);
			intent.putExtras(bundle);
			src.startActivity(intent);
			if (isAnim) {
				src.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
			if (finish) {
				((Activity) src).finish();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void finishActivity(Activity src, String obj){
		try {
			Class className = Class.forName(obj);
			Intent intent = new Intent(src, className);
			src.startActivity(intent);
			src.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			((Activity) src).finish();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
