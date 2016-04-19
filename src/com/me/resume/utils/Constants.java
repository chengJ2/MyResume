package com.me.resume.utils;

public class Constants {

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
	
	public static final boolean DEVELOPER_MODE = true;
	
	public static final int RESULT_CODE = 1001;
	
	public static final int WE_REQUEST_CODE = 1000;
	public static final int JI_REQUEST_CODE = 2000;
}
