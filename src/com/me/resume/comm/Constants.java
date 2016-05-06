package com.me.resume.comm;

import java.io.File;

import com.me.resume.utils.FileUtils;

import android.os.Environment;

/**
 * App 常量
 * @author Administrator
 *
 */
public class Constants {

	/** 保存的总路径 */
    public static final String DIR_PATH = "MyResume";
    
    /**
	 * app包名
	 */
	public static final String PACKAGENAME = "com.me.resume";
    
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
	public static final String DATABASE_FILENAME = "myresume.db"; // DB文件 
    public static final String DATABASE_PATH = "/data"  
            + Environment.getDataDirectory().getAbsolutePath() + "/"  
            + PACKAGENAME; // 获取存储位置地址  
    
    public static String fileName="Avatar.jpg";
    
    public static File userhead = new File(FileUtils.BASE_PATH + File.separator + fileName); 
    
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
