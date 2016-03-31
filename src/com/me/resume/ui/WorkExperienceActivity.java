package com.me.resume.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.views.CustomFAB;

/**
 * 
* @ClassName: WorkExperienceActivity 
* @Description: 工作经历 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午3:39:01 
*
 */
public class WorkExperienceActivity extends BaseActivity implements OnClickListener{

	private TextView toptext,leftLable;
	
	private CustomFAB saveandgo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workexperience_layout);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		leftLable.setText(CommUtil.getStrValue(WorkExperienceActivity.this, R.string.review_resume));
		toptext.setText(CommUtil.getStrValue(WorkExperienceActivity.this, R.string.resume_workexperience));
		
		saveandgo = findView(R.id.saveandgo);
		
		saveandgo.setOnClickListener(this);
		leftLable.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveandgo:
			ActivityUtils.startActivity(WorkExperienceActivity.this, MyApplication.PACKAGENAME+".ui.EvaluationActivity");
			break;
		case R.id.left_lable:
			ActivityUtils.startActivity(WorkExperienceActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
		
	}
}
