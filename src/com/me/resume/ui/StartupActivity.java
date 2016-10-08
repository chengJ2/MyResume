package com.me.resume.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.tools.SystemBarTintManager;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.PreferenceUtil;

/**
 * 启动界面
 * @author Administrator
 *
 */
public class StartupActivity extends Activity {

	private LinearLayout startup;
	private TextView version;
	
	protected PreferenceUtil preferenceUtil;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.startup_layout);
		MyApplication.getApplication().addActivity(this);
		startup = (LinearLayout)findViewById(R.id.startup);
		version = (TextView)findViewById(R.id.version);
		version.setText(CommUtil.getVersionName(this));
        AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);
        animation.setDuration(1800);
        startup.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			public void onAnimationRepeat(Animation animation) {
			}
			
			public void onAnimationEnd(Animation animation) {
				ActivityUtils.startActivity(StartupActivity.this, 
						Constants.PACKAGENAMECHILD + Constants.HOME,true);
			}
        });
	}
	
	@Override  
	public boolean dispatchKeyEvent(KeyEvent event) { 
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK  
	            && event.getRepeatCount() == 0) { 
	    	MyApplication.getApplication().exitAll();
			return true;
	    }  
	    return super.dispatchKeyEvent(event);  
	}  
	
	@Override
	protected void onResume() {
		super.onResume();
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
}
