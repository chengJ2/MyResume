package com.me.resume.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Base64Util {

	public static String getbyteString(Context context, String path) {
		InputStream isa = null;
		String uploadBuffera = "";

		try {
			 isa = new FileInputStream(path);// 获取文件输入流
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024*4];
			int count = 0;
			while ((count = isa.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			uploadBuffera = Base64.encodeToString(baos.toByteArray(),
					Base64.DEFAULT); // 进行Base64编码
			isa.close();

		} catch (Exception e) {
			return "";
		}

		return uploadBuffera;
	}

	public static String getPath() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(
				Calendar.getInstance().getTime()).trim()
				+ ".jpg";
	}

	/*public static String getbyteStr(String path, boolean islocal) {
		byte[] localStream = null;
		if (islocal) {
			localStream = getLocalStream(path);
		} else {
			localStream = getURLStream(path);
		}

		return new String(Base64.encode(localStream, Base64.DEFAULT)); // 进行Base64编码
	}*/

	public static Bitmap getBitmap(Context context, String str) {
		if (str != null && str.length() > 0) {
			byte[] decode = Base64.decode(str, Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(decode, 0, decode.length);
		}
		return null;
	}

	/*
	 * 从数据流中获得数据
	 */
	/*public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024*4];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();

	}*/

	/*public static byte[] getURLStream(String imageUrl) {
		HttpGet httpRequest = new HttpGet(imageUrl);
		HttpClient httpclient = new DefaultHttpClient();
		byte[] data = null;
		InputStream is = null;
		try {
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得相关信息 取得HttpEntiy
				HttpEntity httpEntity = httpResponse.getEntity();
				// 获得一个输入流
				is = httpEntity.getContent();
				data = readInputStream(is);
				is.close();
			}

		} catch (FileNotFoundException err) {
			err.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}*/

	
	private static byte[] getLocalStream(String path) {
		InputStream isa = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024*4];
			isa = new FileInputStream(path);// 获取文件输入流
			int count = 0;
			while ((count = isa.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}

			isa.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
}
