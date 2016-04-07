package com.me.resume.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.views.CustomFAB;

/**
 * 
* @ClassName: JobIntensionActivity 
* @Description: 求职意向 
* @author Comsys-WH1510032 
* @date 2016/4/7 下午4:42:34 
*
 */
public class JobIntensionActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private CustomFAB saveGo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobintension_layout);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		
		toptext.setText(CommUtil.getStrValue(JobIntensionActivity.this, R.string.resume_jobintension));
		rightLable.setText(CommUtil.getStrValue(JobIntensionActivity.this, R.string.review_resume));
		
		saveGo = findView(R.id.saveGo);
		saveGo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.saveGo:
			ActivityUtils.startActivity(JobIntensionActivity.this, MyApplication.PACKAGENAME
					+ ".ui.OtherInfoActivity");
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(JobIntensionActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
	}
}
