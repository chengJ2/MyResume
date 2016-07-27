package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.service.DownloadService;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;

/**
 * 关于我们
 * @author Administrator
 *
 */
public class AboutAppActivity extends BaseActivity implements OnClickListener{

	private TextView version,contactus,declare;
	
	private boolean isupdate = false;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 100:
				Intent intentService = new Intent(self,DownloadService.class);
				intentService.putExtra("url", Constants.APKURLPATH);
				startService(intentService);
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.about_app_layout, null);
		boayLayout.addView(v);
		
		version = findView(R.id.version);
		contactus = findView(R.id.contactus);
		declare = findView(R.id.declare);
		
		version.setOnClickListener(this);
		contactus.setOnClickListener(this);
		
		setTopTitle(R.string.settings_item6);
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		showData();
	}
	
	/**
	 * 显示数据
	 */
	private void showData(){
		version.setText(getStrValue(R.string.app_name) + "   " 
							+ CommUtil.getVersionName(this));
		
		StringBuffer sbStr = new StringBuffer();
		if (!MyApplication.USERID.equals("0")) {
			sbStr.append(getStrValue(R.string.contactus_phone_show));
		}else{
			sbStr.append("<font color=\"red\">");
			sbStr.append(getStrValue(R.string.contactus_phone_hide));
			sbStr.append("</font>");
		}
		sbStr.append("<br/>");
		sbStr.append(getStrValue(R.string.contactus_weixin));
		sbStr.append(getStrValue(R.string.contactus_weixin_name));
		sbStr.append("<br/>");
		sbStr.append(getStrValue(R.string.contactus_qq));
		sbStr.append(getStrValue(R.string.contactus_qq_name) + getStrValue(R.string.contactus_qq_name_note));
		sbStr.append("<br/>");
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
		switch (v.getId()) {
		case R.id.version:
			checkNewVersin();
			break;
		case R.id.contactus:
			CommUtil.copy(self, getStrValue(R.string.contactus_qq_name));
			CommUtil.ToastMsg(self, R.string.contactus_copy_ok);
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 监测新版本
	 */
	private void checkNewVersin(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		requestData("pro_get_apkinfo", 1, params, values, new HandlerData() {		
			@Override
			public void success(Map<String, List<String>> map) {
				try {
					String apk_version = map.get("version").get(0); // 版本号
					String upd_content = map.get("updcontent").get(0); // 更新内容 
					isupdate = CommUtil.checkVersionIsUpdate(self,Integer.valueOf(apk_version));
					if (isupdate) {
						DialogUtils.showDialog(self, upd_content, mHandler);
					}else{
						toastMsg(R.string.settings_item31);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			public void error() {
				set3Msg(R.string.timeout_network);
			}

			@Override
			public void noData() {
				toastMsg(R.string.settings_item31);
			}
		});
	} 
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}
}
