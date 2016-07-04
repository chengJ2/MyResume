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
    public static final String DIR_PATH = "IResume";
    
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
	public static final int ED_MANAGER_REQUEST_CODE = 1200;
	public static final int PE_MANAGER_REQUEST_CODE = 1300;
	public static final int JI_REQUEST_CODE = 2000;
	public static final int JI_REQUEST_CODE2 = 2002;
	public static final int JI_REQUEST_CODE3 = 2003;
	public static final int BI_REQUEST_CODE = 3000;
	public static final int BI_REQUEST_CODE2 = 3002;
	public static final int ED_REQUEST_CODE = 4000;
	public static final int EV_REQUEST_CODE = 5000;
	
	public static final String EDUCATION_SEND = "com.me.resume.education.send";
	public static final String EDUCATION_RECEIVE_ED = "com.me.resume.education";
	public static final String MANAGER_EDUCATION_RECEIVE_ED = "com.me.resume.education.manager";
	public static final String EDUCATION_RECEIVE_TR = "com.me.resume.training";
	public static final String MANAGER_EDUCATION_RECEIVE_TR = "com.me.resume.training.manager";
	
	public static final String EDUCATION_GET = "com.me.resume.fragement.education";
	public static final String TRAIN_GET = "com.me.resume.fragement.training";
	
	
	// 本地db文件  
	public static final String DATABASE_FILENAME = "JResume.db"; // DB文件 
	
	// 下载总路径
	public static final String DOWNLOAD =  "files";
	
	public static final String TEMP_PATH =  "temp"; // 临时文件
	public static final String APK_PATH =  "apk"; // apk
	public static final String COVER_PATH =  "cover"; // 封面
	public static final String IMAGE_PATH =  "images"; //  缩略图
	public static final String CACHE_PATH =  "cache"; //  缓存
	
	public static final String LOG_PATH =  "log"; //  日志
	public static final String LOG_NAME =  "crash.txt"; //  日志文件
	
	// 获取存储位置地址
    public static final String DATABASE_PATH = File.separator + "data"  
            + Environment.getDataDirectory().getAbsolutePath() 
            + File.separator  + PACKAGENAME + File.separator + "databases";   
    
    // 用户头像名 apk下载地址
    public static String FILENAME="avatar.jpg";
    
    // apk下载本地路径
    public static String APKNAME="resume.apk";
    public static File APKPATH = new File(FileUtils.DOWNLOAD_APKPATH + File.separator + APKNAME); 
    
    // apk下载地址
    public static String APKURLPATH = CommonText.ENDPOINT + File.separator + "apkFile" + File.separator + APKNAME;
    
    // 默认显示时间
    public static final int DEFAULTIME = 5000;
    
    public static final int DEFAULTEFFECTTIME = 3000;
    
    // 预览默认切换效果
    public static final String DEFAULEFFECT = "无";
    public static final String EFFECT_STANDARD = "Standard";
    public static final String EFFECT_RANDOM = "Random";
    
    // 腾讯bugly app_id  
 	public static final String APP_CRASH_ID = "900025676";
    
    // 微信ID
    public static final String APP_WX_ID = "";
    
    // QQ ID
    public static final String APP_QQ_ID = "1105433372";
    
    // SINA ID
    public static final String APP_SINA_ID = "3466604642";
    public static final String APP_SINA_SECRET = "4a9d24bd6e2d961e0195e488b014dec8";
    
    // UI activity
    public static final String ADDRESS = "AddressActivity";
    public static final String BASEINFO = "BaseInfoActivity";
    public static final String EDUCATION = "EducationActivity";
    public static final String EVALUATION = "EvaluationActivity";
    public static final String EVALUATIONMORE = "EvaluationMoreActivity";
    public static final String FEEDBACK = "FeedBackActivity";
    public static final String GUIDE = "GuideActivity";
    public static final String ABOUTAPP = "AboutAppActivity";
    public static final String HOME = "HomeActivity";
    public static final String IMAGEPAGER = "ImagePagerActivity";
    public static final String INDUSTRYTYPE = "IndustryTypeActivity";
    public static final String INFOMANAGER = "InfoManagerActivity";
    public static final String JOBINTENSION = "JobIntensionActivity";
    public static final String PROFESSION = "ProfessionActivity";// 职业
    public static final String MYCOLLECTION = "MyCollectionActivity";
    public static final String OTHERINFO = "OtherInfoActivity";
    public static final String MAJOR = "MajorActivity";// 专业
    public static final String PROJECTEXPERIENCE = "ProjectExperienceActivity";
    public static final String RESUMECOVERMORE = "ResumeCoverMoreActivity";
    public static final String RESUMESHAREMORE = "ResumeShareMoreActivity";
    public static final String RESUMETEMPLMORE = "ResumeTemplMoreActivity";
    public static final String SETTING = "SettingActivity";
    public static final String STARTUP = "StartupActivity";
    public static final String TODOSOME = "TodoActivity";
    public static final String TOPICVIEW = "TopicViewActivity";
    public static final String TOPICLISTDETAIL = "TopicListDetailActivity";
    public static final String USERCENTER = "UserCenterActivity";
    public static final String USERLOGIN = "UserLoginActivity";
    public static final String USERNEWPWD = "UserNewPwdActivity";
    public static final String USERREGPROTOCAL = "UserRegProtocalActivity";
    public static final String WORKEXPERIENCE = "WorkExperienceActivity";
    public static final String WORDS = "WordsActivity";
    
    public static final String TYPE = "type";
    public static final String TOPICID = "topicId";
    public static final String TOPICIDTYPE = "topicidtype";
    public static final String TOPICINFO = "topicinfo";
    public static final String TAB = "tab";
    public static final String TOKENID = "tokenId";
    
    public static final String EDITMODE = "editmode";
    public static final String NOTICESHOW = "noticeshow";
    public static final String CITY = "city";
    public static final String CHARACTER = "character";
    public static final String PROFESSIONNAME = "professionname";
    public static final String INDUSTRYTYPENAME = "industrytypename";
    public static final String MAJORNAME = "majorname";
    
    public static final String COVER = "cover";
    public static final String ISLOCAL = "islocal";
    
    public static final String SET_AUTOSHOW= "autoShow";
    public static final String SET_STARTVERYTIME= "startVerytime";
    public static final String SET_SWITCHANIM= "switchAnim";
    public static final String SET_SWITCHEFFDURATION= "switchEffDuration";
    
    public static final String FIRSTINSTALL= "firstInstall";
    
    public static final String SET_FEEDBACK= "feedback";
    public static final String MYWORDS= "mywords";
    
}
