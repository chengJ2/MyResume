package com.me.resume.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.PreferenceUtil;
import com.me.resume.views.ScrollLayoutView;
import com.me.resume.views.ScrollLayoutView.OnViewChangeListener;

/**
 * App第一次安装向导界面
 */
public class GuideActivity extends Activity implements
		OnViewChangeListener, OnClickListener {

	private LinearLayout pointLayout;
	private ScrollLayoutView scrollLayout;
	private TextView skip;
	private Button startBtn;
	private int count;
	private ImageView[] imgs;
	private int currentItem;
	
	protected PreferenceUtil preferenceUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.guide_layout);
		
		if(preferenceUtil == null)
			preferenceUtil = new PreferenceUtil(this);
		
		findViews();
		
	}

	private void findViews() {
		pointLayout = (LinearLayout) findViewById(R.id.pointLayout);
		scrollLayout = (ScrollLayoutView) findViewById(R.id.scrollLayout);
		skip = (TextView) findViewById(R.id.skip);
		startBtn = (Button) findViewById(R.id.startBtn);
		count = scrollLayout.getChildCount();
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		scrollLayout.setOnViewChangeLintener(this);
		skip.setOnClickListener(this);
		startBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.skip:
		case R.id.startBtn:
			preferenceUtil.setPreferenceData(Constants.FIRSTINSTALL,false);
			ActivityUtils.startActivity(GuideActivity.this, Constants.PACKAGENAMECHILD + Constants.HOME,true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onViewChange(int postion) {
		if (postion < 0 || postion > count - 1 || currentItem == postion) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[postion].setEnabled(false);
		currentItem = postion;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

}
