package com.me.resume.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;

/**
 * APP申明条款
 * @author Administrator
 *
 */
public class AboutAppActivity extends BaseActivity implements OnClickListener{

	private TextView contactus,declare;
	
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
		declare = findView(R.id.declare);
		
		StringBuffer sbStr = new StringBuffer();
		sbStr.append("微&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;信:");
		sbStr.append("<font color=\"red\">");
		sbStr.append("&#160;&#160;ppp_cj_qqq");
		sbStr.append("</font>");
		sbStr.append("<br/>");
		sbStr.append("QQ(Email):");
		sbStr.append("<font color=\"red\">");
		sbStr.append("&#160;&#160;1042838789");
		sbStr.append("</font>");
		sbStr.append("<br/>");
//		sbStr.append("邮&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;箱:");
//		sbStr.append("<font color=\"red\">");
//		sbStr.append("&#160;&#160;sandy_cj910@163.com");
//		sbStr.append("</font>");
//		sbStr.append("<br/>");
		contactus.setText(Html.fromHtml(sbStr.toString()));
		
		sbStr = new StringBuffer();
		sbStr.append(getStrValue(R.string.app_declare3));
		sbStr.append("<br/>");
		sbStr.append("<br/>");
		sbStr.append(getStrValue(R.string.app_declare4));
		sbStr.append("<br/>");
		sbStr.append("<br/>");
		sbStr.append("<font color=\"red\">");
		sbStr.append(getStrValue(R.string.app_declare5));
		sbStr.append("</font>");
		declare.setText(Html.fromHtml(sbStr.toString()));
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
}
