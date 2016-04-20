package com.me.resume.comm;

import android.os.Environment;

/**
 * App 常量
 * @author Administrator
 *
 */
public class Constants {

	public static final String PACKAGENAME = "com.me.resume";
	
	 /** 保存的总路径 */
    public static final String DIR_PATH = "MyResume";
    /** 错误日志存放目录*/
    public static final String LOG_PATH = "log/";
    
    /**
     * 错误日志文件名称
     */
    public static final String LOG_NAME = "crash.txt";
    
    /**
     * Logcat文件名称
     */
    public static final String LOG_CAT = "logcat.txt";

    /** 截图存放目录*/
    public static final String IMAGE_PATH =  "images/";
   
    /** 缩略图缓存存放目录*/
    public static final String IMAGECACHE_PATH = IMAGE_PATH + "cache/";
    /** 视频存放目录*/
    public static final String VIDEO_PATH = "videos/";
	
	/**
	 * 本地配置缓存文件
	 */
	public static final String CONFIG = "config";
	
	/**
	 * 开发模式
	 */
	public static final boolean DEVELOPER_MODE = true;
	
	/**
	 * Activity调整请求码
	 */
	public static final int RESULT_CODE = 1001;
	
	public static final int WE_REQUEST_CODE = 1000;
	public static final int JI_REQUEST_CODE = 2000;
	
	/**
	 * 本地db文件  
	 */
	public static final String DATABASE_FILENAME = "myresume.db"; // 这个是DB文件名字  
    public static final String DATABASE_PATH = "/data"  
            + Environment.getDataDirectory().getAbsolutePath() + "/"  
            + PACKAGENAME; // 获取存储位置地址  
}
