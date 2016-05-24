package com.me.resume.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.me.resume.utils.FileUtils;

/**
 * 下载Apk文件
 * @author Administrator
 *
 */
public class DownloadNewVersionTask extends AsyncTask<String, Integer, String> {

	private static final String TAG = "DownloadNewVersionTask";
	
	public static final int DOWNLOAD_PROGRESS = 0;
	public static final int DOWNLOAD_COMPLETE = 1;
	
	private Handler handler;

	public DownloadNewVersionTask(Handler handler){
		this.handler = handler;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String filePath = downloadFile(params[0]);
		return filePath;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if ("".equals(result) || result != null) {
			handler.sendMessage(handler.obtainMessage(DOWNLOAD_COMPLETE, result));
		}

		super.onPostExecute(result);
	}
	
	/**
	 * 下载APK
	 * @param strUrl
	 * @return
	 */
	private String downloadFile(String strUrl) {
		String filePath = "";
		File tempFile = null;// 创建一个临时文件
		HttpURLConnection conn = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String fileName = FileUtils.getFileName(strUrl);
		try {
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(10000);
			conn.connect();
			int contentLength = conn.getContentLength();
			
			File tempDir = FileUtils.createDownloadDir();
			// 安装包文件的临时路径
			tempFile = new File(tempDir, fileName);
			if (tempFile.exists()) {
				tempFile.delete();
			}
			bis = new BufferedInputStream(conn.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(tempFile));
			byte[] buffer = new byte[1024];
			int len = 0; // 每次读取的字节数
			int count = 0; // 下载文件的字节数
			int downloadCount = 0;
			
			// 下载完成便停止下载,或者网络中断便停止下载
			while ((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
				count += len;
				// 当前下载进量(下载量)
				int progress = count * 100 / contentLength;
				if (downloadCount == 0 || progress - 10 > downloadCount) {
					downloadCount += 10;
					// 发送更新进度消息
					Message msg = handler.obtainMessage(DOWNLOAD_PROGRESS);
					Bundle data = new Bundle();
					data.putString("fileName", fileName);
					data.putInt("progress", progress);
					msg.setData(data);
					handler.sendMessage(msg);
				}
			}
			bos.flush();
			filePath = tempFile.getAbsolutePath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			FileUtils.deleteFile(tempFile);
		} catch (IOException e) {
			Log.e(TAG, "下载文件异常：" + e.getLocalizedMessage());
			FileUtils.deleteFile(tempFile);
		} finally {
			try {
				if (null != bis) {
					bis.close();
					bis = null;
				}
				if (null != bos) {
					bos.close();
					bos = null;
				}
				if (null != conn) {
					conn.disconnect();
					conn = null;
				}
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				FileUtils.deleteFile(tempFile);
			}
		}
		return filePath;
	}

}
