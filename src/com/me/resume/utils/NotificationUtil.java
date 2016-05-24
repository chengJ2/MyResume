package com.me.resume.utils;

import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.me.resume.R;
import com.me.resume.ui.HomeActivity;

/**
 * 消息通知类
 * @author Administrator
 *
 */
public class NotificationUtil {
	
	private Context mContext = null;
	
	private NotificationManager mNotificationManager = null;
	
	private Map<Integer,Notification> mNofications = null;
	
	public NotificationUtil(Context context) {
		this.mContext = context;
		mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		mNofications = new HashMap<Integer,Notification>();
	}
	
	/**
	 * 显示通知
	 * @param url
	 */
	public void showNotication(String url){
		if(!mNofications.containsKey(url)){
			Notification notification = new Notification();
			// 滚动文字
			notification.tickerText = "开始下载";
			// 通知时间
			notification.when = System.currentTimeMillis();
			// 设置图标
			notification.icon = R.drawable.ic_launcher;
			// 通知特性
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			
			Intent intent = new Intent(mContext, HomeActivity.class);
			PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent,0, null);
			
			// 设置通知栏点击的操作
			notification.contentIntent = pIntent;
		}
	}
	
}
