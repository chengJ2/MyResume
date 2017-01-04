package com.me.resume.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 
* @ClassName: CommLoadActivity 
* @Description: 统一数据加载界面
* @date 2017/1/4 上午10:15:58 
*
 */
public class CommLoadActivity extends BaseActivity implements OnClickListener{

	protected LinearLayout commbodyLayout;
	
	private TextView flag; // 数据加载中...
	
	// 网络连接失败...
	private RelativeLayout msgLayout;
	private TextView msgText;
	private Button reloadBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_common_load_layout);
		
		bodyLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_common_load_layout, null);
		bodyLayout.addView(v);
		
		findViews();
	}
	
	private void findViews(){
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		commbodyLayout = findView(R.id.commbodyLayout);
		
		flag = findView(R.id.flag);
		
		msgLayout = (RelativeLayout)findView(R.id.msgLayout);
		msgText  = (TextView)findView(R.id.msgText);
		reloadBtn = (Button) findView(R.id.reloadBtn);
		reloadBtn.setOnClickListener(this);
	}
	
	protected void setBodyShow() {
		commbodyLayout.setVisibility(View.VISIBLE);
	}
	
	protected void setBodyHide() {
		commbodyLayout.setVisibility(View.GONE);
	}
	
	protected void setFlagShow(int resId) {
		flag.setVisibility(View.VISIBLE);
		flag.setText(getString(resId));
	}
	
	protected void setFlagHide() {
		flag.setVisibility(View.GONE);
	}
	
	protected void setMsgLayoutShow(int resId) {
		msgLayout.setVisibility(View.VISIBLE);
		msgText.setText(getString(resId));
	}
	
	protected void setMsgLayoutHide() {
		msgLayout.setVisibility(View.GONE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
