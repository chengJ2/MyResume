package com.me.resume;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;

import com.me.resume.comm.Constants;
import com.me.resume.tools.CrashHandler;
import com.me.resume.tools.DbManager;
import com.me.resume.tools.L;
import com.me.resume.utils.PreferenceUtil;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 
 * @Description: 全局Application
 * @date 2016/4/18 下午4:17:22
 * 
 */
public class MyApplication extends Application {

	private List<Activity> mActivityList = new LinkedList<Activity>();

	/** WiApplication对象 */
	private static MyApplication application;

	public static SQLiteDatabase database = null;

	/**
	 * 判断用户是否注册登录成功
	 */
	public static String USERID = "0";
	public static String USERNAME = "";
	public static String USERAVATORPATH = "";
	
	private PreferenceUtil preferenceUtil;

	@Override
	public void onCreate() {
		super.onCreate();
		setApplication(this);
		init();
	}

	private void init() {
		database = DbManager.openDatabase(this);
		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
		
		if(preferenceUtil == null)
			preferenceUtil = new PreferenceUtil(this);
		
		if (preferenceUtil.getPreferenceData(Constants.SET_FEEDBACK)) {
			CrashReport.initCrashReport(getApplicationContext(), Constants.APP_CRASH_ID, true);
		}
		
		// LanguageSettings.getInstance().initLang(this);
		// FontsOverride.setDefaultFont(this, "serif", "fonts/FZLT.TTF");
	}

	/**
	 * 设置WiApplication对象
	 * @param app
	 *            WiApplication对象
	 */
	private void setApplication(MyApplication app) {
		application = app;
	}

	/**
	 * 获取WiApplication对象
	 * @return application
	 */
	public static MyApplication getApplication() {
		return application;
	}

	/**
	 * @param activity
	 *            the activity
	 */
	public void addActivity(Activity activity) {
		if (!mActivityList.contains(activity)) {
			mActivityList.add(activity);
		}
	}

	/**
	 * 关闭全部Activity
	 */
	public void exitAll() {
		for (Activity a : mActivityList) {
			a.finish();
		}
		System.exit(0);
	}
}
