package com.me.resume.service;

import java.io.File;

import com.me.resume.R;
import com.me.resume.comm.DownloadNewVersionTask;
import com.me.resume.ui.HomeActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 * apk更新服务
 * @author Administrator
 *
 */
public class UpdateService extends Service {

	// 通知栏
	private NotificationManager updateNotificationManager;
	private Notification updateNotification;

	private RemoteViews mRemoteViews;
	private int progressBarId;
	private int title;
	private int percent;
	private String desc;
	
	// 通知栏跳转Intent
	private PendingIntent updatePendingIntent;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DownloadNewVersionTask.DOWNLOAD_PROGRESS:
				// 设置RemoteView组件中进度条的进度值
				Bundle bundle = msg.getData();
				String fileName = bundle.getString("fileName");
				int progress = bundle.getInt("progress");
				mRemoteViews.setProgressBar(progressBarId, 100, progress, false);
				mRemoteViews.setTextViewText(title, desc);
				mRemoteViews.setTextViewText(percent, progress + "%");
				updateNotificationManager.notify(0, updateNotification);
				break;
			case DownloadNewVersionTask.DOWNLOAD_COMPLETE:
				File file = new File((String) msg.obj);
				installApk(file);
				stopSelf();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		};
	};
	
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String url = intent.getStringExtra("url");
		int icon = intent.getIntExtra("icon", -1);
		int layoutId = intent.getIntExtra("layoutId", -1);
		progressBarId = intent.getIntExtra("progressBarId", -1);
		title = intent.getIntExtra("title", -1);
		percent = intent.getIntExtra("percent", -1);
		desc = intent.getStringExtra("desc");
		mRemoteViews = new RemoteViews(getPackageName(), layoutId);

		updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 通知时间
		updateNotification.when = System.currentTimeMillis();
		// 设置图标
		updateNotification.icon = R.drawable.ic_launcher;
		// 通知特性
		updateNotification.flags = Notification.FLAG_AUTO_CANCEL;

		Intent mIntent = new Intent(this, HomeActivity.class);
		updatePendingIntent = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		updateNotification.contentView = mRemoteViews;
		updateNotification.contentIntent = updatePendingIntent;
		updateNotificationManager.notify(0, updateNotification);

		new DownloadNewVersionTask(handler).execute(url);

		return super.onStartCommand(intent, flags, startId);

	}
	
	/**
	 * 安装APK程序
	 * @param file 要安装的指定文件
	 */
	private void installApk(File file) {
		if (file.exists()) {
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			updateNotification.tickerText = "下载完成,点击安装";
			updateNotification.when = System.currentTimeMillis();
			updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
			updateNotification.defaults = Notification.DEFAULT_SOUND;// 铃声提醒
			mRemoteViews.setProgressBar(progressBarId, 100, 100, false);
			mRemoteViews.setTextViewText(title, "下载完成,点击安装");
			mRemoteViews.setTextViewText(percent, "100%");
			updatePendingIntent = PendingIntent.getActivity(this, 0, installIntent, 0);
			updateNotification.contentIntent = updatePendingIntent;
			updateNotificationManager.notify(0, updateNotification);
		}
	}


}
