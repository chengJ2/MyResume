package com.me.resume.service;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.me.resume.comm.DownloadTask;
import com.me.resume.utils.NotificationUtil;

/**
 * apk更新服务
 * @author Administrator
 *
 */
public class DownloadService extends Service {

	public static final String ACTION_HONE = "action_home";
	public static final String ACTION_APKUPDATE = "action_apkupdate";

	public NotificationUtil notificationUtil = null;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DownloadTask.DOWNLOAD_PROGRESS:
				Bundle bundle = msg.getData();
				int progress = bundle.getInt("progress");
				notificationUtil.updateNotication(NotificationUtil.ID, progress);
				break;
			case DownloadTask.DOWNLOAD_COMPLETE:
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
		
		if (notificationUtil == null) {
			notificationUtil = new NotificationUtil(this);
			notificationUtil.showNotication();
		}
		
		new DownloadTask(handler,2).execute(url);
		
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
			notificationUtil.finishNotication(NotificationUtil.ID, installIntent);
		}
	}

}
