package com.me.resume.utils;

import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

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
	
	public static final int ID = 0;
	
	public NotificationUtil(Context context) {
		this.mContext = context;
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNofications = new HashMap<Integer,Notification>();
	}
	
	/**
	 * 显示通知
	 * @param url
	 */
	public void showNotication(){
		Notification mNotification = mNofications.get(ID);
		if(mNotification == null){
			
			mNotification = new Notification();
			
			mNotification.when = System.currentTimeMillis();
			mNotification.icon = R.drawable.notify_icon;
			mNotification.flags = Notification.FLAG_AUTO_CANCEL;

			Intent mIntent = new Intent(mContext, HomeActivity.class);
			PendingIntent updatePendingIntent = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mNotification.contentIntent = updatePendingIntent;
			
			RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.updateprogress);
			mNotification.contentView = mRemoteViews;
			mNotificationManager.notify(ID, mNotification);
			
			mNofications.put(ID, mNotification);
		}
	}
	
	/**
	 * 更新通知内容
	 * @param progress
	 */
	public void updateNotication(int id,int progress){
		Notification notification = mNofications.get(id);
		if (notification != null) {
			notification.contentView.setProgressBar(R.id.notificationProgress, 100, progress, false);
			notification.contentView.setTextViewText(R.id.notificationPercent, progress + "%");
			mNotificationManager.notify(id, notification);
		}
	}
	
	/**
	 * 更新通知内容
	 * @param progress
	 */
	public void finishNotication(int id,Intent installIntent){
		Notification notification = mNofications.get(id);
		if (notification != null) {
			
			notification.tickerText = "下载完成,点击安装";
			notification.when = System.currentTimeMillis();
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_SOUND;// 铃声提醒
			notification.contentView.setProgressBar(R.id.notificationProgress, 100, 100, false);
			notification.contentView.setTextViewText(R.id.notificationTitle, "下载完成,点击安装");
			notification.contentView.setTextViewText(R.id.notificationPercent, "100%");
			PendingIntent updatePendingIntent = PendingIntent.getActivity(mContext, 0, installIntent, 0);
			notification.contentIntent = updatePendingIntent;
			mNotificationManager.notify(0, notification);
			
		}
	}
	
	
	/**
	 * 取消通知
	 */
	public void cancleNotication(int id){
		mNotificationManager.cancel(id);
		mNofications.remove(id);
	}
	
}
