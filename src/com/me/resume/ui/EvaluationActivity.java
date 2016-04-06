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
* @ClassName: EvaluationActivity 
* @Description: 自我评价 
* @author Comsys-WH1510032 
* @date 2016/3/30 下午5:23:13 
*
 */
public class EvaluationActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private CustomFAB saveGo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation_layout);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		
		toptext.setText(CommUtil.getStrValue(EvaluationActivity.this, R.string.resume_evaluation));
		rightLable.setText(CommUtil.getStrValue(EvaluationActivity.this, R.string.review_resume));
		
		saveGo = findView(R.id.saveGo);
		saveGo.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveGo:
			ActivityUtils.startActivity(EvaluationActivity.this, MyApplication.PACKAGENAME
					+ ".ui.EducationActivity");
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(EvaluationActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
	}
}
