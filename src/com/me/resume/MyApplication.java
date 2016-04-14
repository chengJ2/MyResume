package com.me.resume;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.util.DisplayMetrics;

import com.me.resume.utils.CrashHandler;
import com.tencent.bugly.crashreport.CrashReport;

public class MyApplication extends Application {

	 /** */
    private List<Activity> mActivityList = new LinkedList<Activity>();
    
    /** WiApplication对象 */
    private static MyApplication application;
    
    public static final String PACKAGENAME = "com.me.resume";
    
    public static int cposition = 0;
    /**
    * 
    */
   private int displayWitdh;

   /**
    * 
    */
   private int displayHeight;
   /**
    * 
    */
   private float displayDensity;
   
   /**
    * 用户ID
    */
   public static int userId = 0;
   
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		setApplication(this);
        init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
//		LanguageSettings.getInstance().initLang(this);
		CrashReport.initCrashReport(getApplicationContext(), "900025676", false);
		CrashHandler catchExcep = new CrashHandler(this);
    	Thread.setDefaultUncaughtExceptionHandler(catchExcep);
	}

	/**
     * 设置WiApplication对象
     * 
     * @param app WiApplication对象
     */
    private void setApplication(MyApplication app) {
        application = app;
    }

    /**
     * 获取WiApplication对象
     * 
     * @return application
     */
    public static MyApplication getApplication() {
        return application;
    }
    
    /**
     * @param activity the activity
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
    
    /**
     * @param mContext Context
     */
    public void initDisplay(Activity mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayWitdh = dm.widthPixels;
        displayHeight = dm.heightPixels;
        displayDensity = dm.density;
    }
    
    /**
     * @return the displayWitdh
     */
    public int getDisplayWitdh() {
        return displayWitdh;
    }

    /**
     * @return the displayHeight
     */
    public int getDisplayHeight() {
        return displayHeight;
    }

    /**
     * @return the displayDensity
     */
    public float getDisplayDensity() {
        return displayDensity;
    }
}
