package com.me.resume.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.utils.CommUtil;

/**
 * 
* @ClassName: EvaluationActivity 
* @Description: 自我评价 
* @author Comsys-WH1510032 
* @date 2016/3/30 下午5:23:13 
*
 */
public class EvaluationActivity extends BaseActivity {

	private TextView toptext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation_layout);
		
		toptext = findView(R.id.top_text);
		
		toptext.setText(CommUtil.getStrValue(EvaluationActivity.this, R.string.resume_evaluation));
	}
}
