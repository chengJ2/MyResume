package com.me.resume;

import java.util.Stack;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.me.resume.tools.SystemBarTintManager;
import com.me.resume.utils.Constants;
import com.me.resume.utils.L;

/**
 * 
* @ClassName: BaseActivity 
* @Description: 基类 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午2:13:23 
*
 */
public class BaseActivity extends Activity {

	// activity访问栈
    private static Stack<BaseActivity> mLocalStack = new Stack<BaseActivity>();
    protected BaseActivity mContext;
    
    protected SharedPreferences sp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (L.DEBUG) {
    		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
    		.detectDiskReads()
    		.detectDiskWrites()
    		.detectNetwork()   // or .detectAll() for all detectable problems
    		.penaltyLog()
    		.build());
    		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
    		.detectLeakedSqlLiteObjects()
    		.detectLeakedClosableObjects()
    		.penaltyLog()
    		.penaltyDeath()
    		.build());
    	}
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE);
        MyApplication.getApplication().addActivity(this);
        
        mLocalStack.add(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	// 初始化语言环境
//    	LanguageSettings.getInstance().initLang(this);
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.top_bar);
    }
    
    @TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
    
    /**
     * 
     * @Title:BaseActivity
     * @Description: Find View By Id
     * @author Comsys-WH1510032
     * @return View  
     * @param viewID
     */
    public <T extends View> T findView(int viewID){
    	return (T)findViewById(viewID);
    }
    
    protected void switchLang(String newLang){
    	setPreferenceData("LANGUAGE",newLang);
        // finish app内存中的所有activity
    	while (0 != mLocalStack.size()) {
            mLocalStack.pop().finish();
        }
        // 跳转到app首页
//    	WiApplication.getApplication().exitAll();
//        ActivityUtils.startActivity(BaseActivity.this, WiApplication.PACKAGENAME+".ui.WiVehicleMainActivity");
    }

    public void setPreferenceData(String key, String value){
    	sp.edit().putString(key, value).commit();
    }
    
    public String getPreferenceData(String str,String def){
    	return sp.getString(str, def);
    }
    
    public void setPreferenceData(String key, int value){
    	sp.edit().putInt(key, value).commit();
    }
    
    public int getPreferenceData(String str,int def){
    	return sp.getInt(str, def);
    }
    
    @Override
    protected void onDestroy() {
        // 出栈
    	mLocalStack.remove(this);
        super.onDestroy();
    }
    
}
