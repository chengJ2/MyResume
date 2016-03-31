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

/**
 * 
* @ClassName: EvaluationActivity 
* @Description: 自我评价 
* @author Comsys-WH1510032 
* @date 2016/3/30 下午5:23:13 
*
 */
public class EvaluationActivity extends BaseActivity implements OnClickListener{

	private TextView toptext,leftLable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation_layout);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		toptext.setText(CommUtil.getStrValue(EvaluationActivity.this, R.string.resume_evaluation));
		leftLable.setText(CommUtil.getStrValue(EvaluationActivity.this, R.string.review_resume));
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveandgo:
			break;
		case R.id.left_lable:
			ActivityUtils.startActivity(EvaluationActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
	}
}
