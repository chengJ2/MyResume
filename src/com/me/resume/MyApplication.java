package com.me.resume;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;

import com.me.resume.tools.CrashHandler;
import com.me.resume.tools.DbManager;
import com.me.resume.tools.L;

/**
 * 
* @ClassName: MyApplication 
* @Description: 全局Application
* @author Comsys-WH1510032 
* @date 2016/4/18 下午4:17:22 
*
 */
public class MyApplication extends Application {
	 /** */
    private List<Activity> mActivityList = new LinkedList<Activity>();
    
    /** WiApplication对象 */
    private static MyApplication application;
    
    public static SQLiteDatabase database = null;
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
    * 判断用户是否注册登录成功
    */
   public static String USERID = "0";
   
   /**
    * 用户名
    */
   public static String USERNAME = "";
   
   /**
    * 用户名头像
    */
   public static String USERAVATORPATH = "";
   
   /**
    * 栏目id
    */
   public static String KID = "0";
   
	@Override
	public void onCreate() {
		super.onCreate();
		setApplication(this);
        init();
	}
	
	private void init() {
//		LanguageSettings.getInstance().initLang(this);
		
		database = DbManager.openDatabase(this);
		
		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
		
//		CrashReport.initCrashReport(getApplicationContext(), "900025676", true);
    	
//    	FontsOverride.setDefaultFont(this, "SERIF", "fonts/FZY1JW.ttf");
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
        int densityDpi = dm.densityDpi;
        L.d("宽度（PX）:"+displayWitdh +" 高度（PX）:"+displayHeight + " 密度:" + displayDensity + " 密度DPI:" + densityDpi);
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
