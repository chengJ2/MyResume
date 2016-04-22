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
	public static final int JI_REQUEST_CODE2 = 2002;
	public static final int BI_REQUEST_CODE = 3000;
	public static final int BI_REQUEST_CODE2 = 3002;
	
	public static final int ED_REQUEST_CODE = 4000;
	
	public static final String EDCATION = "com.me.resume.education";
	
	/**
	 * 本地db文件  
	 */
	public static final String DATABASE_FILENAME = "myresume.db"; // 这个是DB文件名字  
    public static final String DATABASE_PATH = "/data"  
            + Environment.getDataDirectory().getAbsolutePath() + "/"  
            + PACKAGENAME; // 获取存储位置地址  
    
    // 请求超时
 	public static final int EXECUTE_TIMEOUT = -0X2000;
 	
 	// 请求网络异常
 	public static final int EXECUTE_NETERROR = -0X1000;
 	
 	// 加载数据成功
 	public static final int LOAD_DATA_SUCCESS = 0X1000;
 	
 	// 加载数据失败
 	public static final int LOAD_DATA_ERROR = 0X8000;
 	
 	// 加载暂无数据
 	public static final int LOAD_NO_DATA = -0X8000;
}
