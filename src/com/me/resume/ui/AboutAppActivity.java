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
		sbStr.append("微信号:");
		sbStr.append("<font color=\"red\">");
		sbStr.append("ppp_cj_qqq");
		sbStr.append("</font>");
		sbStr.append("<br/>");
		sbStr.append("QQ(Email):");
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
		
		sbStr = new StringBuffer();
		sbStr.append("本App上的大部分内容来源网络,包括文字,图片等,提供的内容将服务使用者用于个人学习、研究或欣赏,以及其他非商业性或非盈利性用途.");
		sbStr.append("<br/>");
		sbStr.append("若内容侵犯了原作者的权利或原作者不愿意在本App刊登内容,请及时通知并联系我们,予以删除。");
		declare.setText(Html.fromHtml(sbStr.toString()));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}
}
