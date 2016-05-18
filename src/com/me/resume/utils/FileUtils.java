package com.me.resume.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.me.resume.MyApplication;
import com.me.resume.comm.Constants;

/**
 * 本地文件管理
 * @author Administrator
 *
 */
public class FileUtils {
	
	public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();// SDCard路径
	public static final String BASE_PATH = SD_PATH + File.separator + Constants.DIR_PATH; // 根目录
	
	public static final String TEMPDIR = BASE_PATH + File.separator + "temp";
	
	public static final String IMAGE_PATH =  "images";
	 /** 缩略图缓存存放目录*/
	public static final String BASE_IMAGE_CACHE = TEMPDIR + File.separator + IMAGE_PATH + File.separator + "cache" + File.separator;
	
	 /** 错误日志存*/
    public static final String LOG_PATH = "log";
    public static final String LOG_NAME = BASE_PATH + File.separator + LOG_PATH + File.separator + "crash.txt";
    
    /**
     * APk下载目录
     */
    public static final String DOWNLOAD_APKPATH = TEMPDIR + File.separator + "download" + File.separator;
    
	/**
     * 判断SDcard是否挂起
     * @return
     */
    public static boolean isSDCardExist() {
		boolean flag = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			flag = true;
		}
		return flag;
	}
    
    /**
	 * 获取缓存文件夹目录 如果不存在创建 否则则创建文件夹
	 * 
	 * @return filePath
	 */
	public static String isExistsFilePath(Context context) {
		String filePath = BASE_PATH + File.separator +  MyApplication.USERNAME;
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return filePath;
	}
	
	/**
	 * 获取缓存文件 如果不存在创建 否则则创建文件夹
	 * 
	 * @return filePath
	 */
	public static boolean existsFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}
    
	/**
	 * 获取文件的文件名
	 * @param url	文件的下载地址
	 * @return		文件名
	 */
	public static String getFileName(String url) {
		String fileName = url.substring(url.lastIndexOf("/") + 1, url.length()).toLowerCase();
		return fileName;
	}

	/**
	 * 获取文件的扩展名
	 * @param url 文件的下载地址
	 * @return 文件扩展名
	 */
	public static String getFileExtension(String url) {
		String fileExtension = url.substring(url.lastIndexOf("."), url.length()).toLowerCase();
		return fileExtension;
	}

	/**
	 * 创建临时下载根目录
	 * @param dirName 指定的目录名
	 * @return 返回创建的临时下载目录
	 */
	public static File createDownloadDir() {
		File updateDir = new File(Environment.getExternalStorageDirectory(), DOWNLOAD_APKPATH);
		if (!updateDir.exists()) {
			updateDir.mkdirs();
		}
		return updateDir;
	}

	/**
	 * 删除文件
	 * @param file 指定要删除的文件
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(File file) {
		boolean flag = false;
		if (file != null && file.exists()) {
			file.delete();
			flag = true;// 删除成功
		}
		return flag;
	}
}
