package com.me.resume.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.utils.CommUtil;

/**
 * 
* @ClassName: WorkExperienceActivity 
* @Description: 工作经历 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午3:39:01 
*
 */
public class WorkExperienceActivity extends BaseActivity {

	private TextView toptext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workexperience_layout);
		
		toptext = findView(R.id.top_text);
		
		toptext.setText(CommUtil.getStrValue(WorkExperienceActivity.this, R.string.resume_workexperience));
	}
}
