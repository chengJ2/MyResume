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

import com.me.resume.tools.L;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.RegexUtil;

/**
 * 下载Apk文件
 * @author Administrator
 *
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {

	public static final int DOWNLOAD_PROGRESS = 10000;
	public static final int DOWNLOAD_COMPLETE = 10001;
	
	private Handler handler;
	/**
	 * 下载的类型
	 * 1:首页 cover
	 * 2:apk
	 */
	private int style = 1;

	public DownloadTask(Handler handler,int style){
		this.handler = handler;
		this.style = style;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String filePath = downloadFile(params[0]);
		return filePath;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (RegexUtil.checkNotNull(result)) {
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
			
			File tempDir = null;
			if (style == 1) {
				tempDir = FileUtils.createCoverDownloadDir();// 简历封面下载路径
			}else{
				tempDir = FileUtils.createDownloadDir();// 安装包文件的临时路径
			}
			
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
			L.e("下载文件异常：" + e.getLocalizedMessage());
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
				L.e(e.getMessage());
				FileUtils.deleteFile(tempFile);
			}
		}
		return filePath;
	}

}
