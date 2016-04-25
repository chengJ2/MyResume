package com.me.resume.ui;

import com.me.resume.R;

import android.os.Bundle;
import android.view.View;

/**
 * 
* @Description: 项目经验
* @author chegnjian
* @date 2016/4/25 下午4:25:50 
*
 */
public class ProjectExperienceActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_projectexperience_layout, null);
		boayLayout.addView(v);
	}
}
