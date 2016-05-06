package com.me.resume.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.me.resume.utils.FileUtils;

/**
 * 文件缓存类
 * 
 * @author ChengJian
 * 
 */
public class FileCache {
	
	private Context context;
	private File cacheDir;

	public FileCache(Context context) {
		this.context = context;
		createCacheDir();
	}

	public void createCacheDir() {
		// 如果有SD卡则在SD卡中建一个cacheDirName的目录存放缓存的图片
		if (FileUtils.isSDCardExist()) {
			cacheDir = new File(FileUtils.BASE_IMAGE_CACHE);
		} else {
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	/**
	 * 获取缓存的图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap get(String url) {
		File file = new File(cacheDir, getFileName(url));
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
			if (bitmap == null) {
				file.delete();
			} else {
				return bitmap;
			}
		}
		return null;
	}

	/**
	 * 保存图片到SDCard
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void saveImageToSDCard(String url, Bitmap bitmap) {
		if (bitmap == null) {// 需要保存的是一个空值
			return;
		}
		File file = new File(cacheDir, getFileName(url));
		FileOutputStream fos = null;
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 70, fos);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将url的hashCode作为缓存的文件名
	 * 
	 * @param url
	 *            图片的url下载地址
	 * @return 返回文件名
	 */
	public String getFileName(String url) {
		return String.valueOf(url.hashCode());
	}

	/**
	 * 
	 * @return 缓存文件保存路径
	 */
	public String getSaveFilePath() {
		return FileUtils.BASE_IMAGE_CACHE;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public File getFile(String url) {
		String str = getSaveFilePath() + getFileName(url);
		File f = new File(str);
		return f;
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			file.delete();
		}
	}

}
