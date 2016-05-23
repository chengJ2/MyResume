package com.me.resume.ui;

import android.os.Bundle;
import android.view.View;

import com.me.resume.BaseActivity;
import com.me.resume.R;

/**
 * 
* @ClassName: EvaluationMoreActivity 
* @Description: 自我评价更多详情
* @author Comsys-WH1510032 
* @date 2016/5/23 下午1:22:03 
*
 */
public class EvaluationMoreActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_evalution_more_layout, null);
		boayLayout.addView(v);*/
		
		findView();
		
		initView();
	}

	
	private void findView() {
		// TODO Auto-generated method stub
		
	}
	
	private void initView() {
		setTopTitle(R.string.ev_info_character);
		setMsgHide();
		setRightIcon(R.drawable.icon_sync);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
	}

}
