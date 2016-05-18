package com.me.resume.comm;

import java.io.File;

import android.os.Environment;

import com.me.resume.utils.FileUtils;
import com.whjz.android.text.CommonText;

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
	 * app子包名
	 */
	public static final String PACKAGENAMECHILD = PACKAGENAME + ".ui.";
	
	/**
	 * MainActivity
	 */
	public static final String MAINACTIVITY = ".MainActivity";
    
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
	public static final int WE_MANAGER_REQUEST_CODE = 1100;
	public static final int JI_REQUEST_CODE = 2000;
	public static final int JI_REQUEST_CODE2 = 2002;
	public static final int BI_REQUEST_CODE = 3000;
	public static final int BI_REQUEST_CODE2 = 3002;
	
	public static final int ED_REQUEST_CODE = 4000;
	
	
	public static final String EDUCATION = "com.me.resume.education";
	
	// 本地db文件  
	public static final String DATABASE_FILENAME = "myresume.db"; // DB文件 
	
	// 获取存储位置地址
    public static final String DATABASE_PATH = File.separator + "data"  
            + Environment.getDataDirectory().getAbsolutePath() + File.separator  + PACKAGENAME;   
    
    // 用户头像名 apk下载地址
    public static String FILENAME="avatar.jpg";
    
    // apk下载路径
    public static String APKNAME="resume.apk";
    public static File APKPATH = new File(FileUtils.DOWNLOAD_APKPATH + File.separator + APKNAME); 
    
    // apk下载地址
    public static String APKURLPATH = CommonText.endPoint + "/apk/" + APKNAME;
    
}
