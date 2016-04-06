package com.me.resume.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.views.CustomFAB;

/**
 * 
* @ClassName: BaseInfoActivity 
* @Description: 个人基本资料
* @author Comsys-WH1510032 
* @date 2016/3/29 下午3:36:12 
*
 */
public class BaseInfoActivity extends SwipeBackActivity implements OnClickListener{

	private CustomFAB saveInfo,saveInfoAndGo;
	
	private TextView toptext,leftLable,rightLable;
	
	private EditText info_realname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baseinfo_layout);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		leftLable.setOnClickListener(this);
		rightLable = findView(R.id.right_lable);
		rightLable.setOnClickListener(this);
		toptext.setText(CommUtil.getStrValue(BaseInfoActivity.this, R.string.resume_baseinfo));
		rightLable.setText(CommUtil.getStrValue(BaseInfoActivity.this, R.string.review_resume));
		
		saveInfo = findView(R.id.save);
		saveInfoAndGo = findView(R.id.saveandgo);
		
		info_realname = findView(R.id.info_realname);
		
		saveInfo.setOnClickListener(this);
		saveInfoAndGo.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			setPreferenceData("info_realname",info_realname.getText().toString().trim());
			break;
		case R.id.saveandgo:
			ActivityUtils.startActivity(BaseInfoActivity.this, MyApplication.PACKAGENAME+".ui.WorkExperienceActivity");
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(BaseInfoActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
		
	}
}
