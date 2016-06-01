package com.me.resume.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;

/**
 * 意见反馈
 * @author Administrator
 *
 */
public class AboutAppActivity extends BaseActivity implements OnClickListener{

	private TextView contactus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boayLayout.removeAllViews();
		
		View v = View.inflate(self,R.layout.about_app_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.settings_item6);
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		contactus = findView(R.id.contactus);
		StringBuffer sbStr = new StringBuffer();
		sbStr.append("微信号:");
		sbStr.append("<font color=\"red\">");
		sbStr.append("ppp_cj_qqq");
		sbStr.append("</font>");
		sbStr.append("<br/>");
		sbStr.append("QQ:");
		sbStr.append("<font color=\"red\">");
		sbStr.append("1042838789");
		sbStr.append("</font>");
		sbStr.append("<br/>");
		sbStr.append("邮箱:");
		sbStr.append("<font color=\"red\">");
		sbStr.append("sandy_cj910@163.com");
		sbStr.append("</font>");
		sbStr.append("<br/>");
		contactus.setText(Html.fromHtml(sbStr.toString()));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}
}
