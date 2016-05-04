package com.me.resume.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.utils.ActivityUtils;

/**
 * 启动界面
 * @author Administrator
 *
 */
public class StartupActivity extends Activity {

	private LinearLayout startup;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.startup_layout);
		MyApplication.getApplication().addActivity(this);
		
		startup = (LinearLayout)findViewById(R.id.startup);
	    
        AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
        animation.setDuration(3000);
        startup.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			public void onAnimationRepeat(Animation animation) {
			}
			
			public void onAnimationEnd(Animation animation) {
				ActivityUtils.startActivity(StartupActivity.this, 
						Constants.PACKAGENAME + ".MainActivity", false, true);
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
	}
}
