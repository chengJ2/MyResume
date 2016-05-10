package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.tools.DataCleanManager;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.views.SwitchButton;
import com.me.resume.views.SwitchButton.OnChangedListener;

/**
 * 
* @ClassName: SettingActivity 
* @Description: App设置界面 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午5:04:43 
*
 */
public class SettingActivity extends BaseActivity implements OnClickListener{

	private SwitchButton setting_start_cb,setting_auto_cb,setting_edit_cb;
	
	private LinearLayout cacheLayout,versionLayout,feedbackLayout,logoutLayout,shareLayout;
	
	private TextView cachesize,version;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// TODO
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
		
		View v = View.inflate(self,R.layout.activity_setting, null);
		boayLayout.addView(v);
	
		findViews();
		initViews();
		switchBtnClick();
		
	}
	
	private void findViews(){
		setting_start_cb = findView(R.id.setting_start_cb);
		setting_auto_cb = findView(R.id.setting_auto_cb);
		setting_edit_cb = findView(R.id.setting_edit_cb);
		
		cacheLayout = findView(R.id.cacheLayout);
		cachesize = findView(R.id.cachesize);
		versionLayout = findView(R.id.versionLayout);
		feedbackLayout = findView(R.id.feedbackLayout);
		version = findView(R.id.viewsion);
		logoutLayout = findView(R.id.logoutLayout);
		shareLayout = findView(R.id.shareLayout);
		
	}
	
	private void initViews(){
		setTopTitle(R.string.action_settings);
		
		setMsgHide();
		
		setRightIconVisible(View.INVISIBLE);
		
		setRight2IconVisible(View.GONE);
		
		setfabLayoutVisible(View.GONE);
		
		try {
			cachesize.setText(DataCleanManager.getSQlDataCacheSize(self));
		} catch (Exception e) {
			e.printStackTrace();
		}
		version.setText(CommUtil.getVersionName(self));
		
		cacheLayout.setOnClickListener(this);
		versionLayout.setOnClickListener(this);
		feedbackLayout.setOnClickListener(this);
		logoutLayout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
	}
	
	private void switchBtnClick(){
		setting_start_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, boolean checkState) {
				// TODO Auto-generated method stub
				int onoff = 0;
				if(getPreferenceData("startVerytime", 0) == 0){
					onoff = 1;
				}else{
					onoff = 0;
				}
				setPreferenceData("startVerytime", onoff); 
			}
		});
		
		setting_auto_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, boolean checkState) {
				// TODO Auto-generated method stub
				int onoff = 0;
				if(getPreferenceData("autoShow", 0) == 0){
					onoff = 1;
				}else{
					onoff = 0;
				}
				setPreferenceData("autoShow", onoff); 
			}
		});
		
		setting_edit_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, boolean checkState) {
				// TODO Auto-generated method stub
				int onoff = 0;
				if(getPreferenceData("editmode", 0) == 0){
					onoff = 1;
				}else{
					onoff = 0;
				}
				setPreferenceData("editmode", onoff); 
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int autoShow = getPreferenceData("autoShow", 0);
		int startVerytime = getPreferenceData("startVerytime", 0);
		int editmode = getPreferenceData("editmode", 0);
		if (startVerytime == 0) {
			setting_start_cb.setChecked(false);
		}else{
			setting_start_cb.setChecked(true);
		}
		if (autoShow == 0) {
			setting_auto_cb.setChecked(false);
		}else{
			setting_auto_cb.setChecked(true);
		}
		if (editmode == 0) {
			setting_edit_cb.setChecked(false);
		}else{
			setting_edit_cb.setChecked(true);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.left_lable:
//			self.scrollToFinishActivity();
//			break;
		case R.id.versionLayout:
			CommUtil.ToastMsg(self, "暂无新版本");
			break;
		case R.id.feedbackLayout:
			startChildActivity("FeedBackActivity", false);
			break;
		case R.id.logoutLayout:
			if (!MyApplication.userId.equals("0")) {
				actionLogout();
			}else{
				toastMsg(R.string.action_no_login);
			}
			break;
		case R.id.cacheLayout:
			DialogUtils.showAlertDialog(self, CommUtil.getStrValue(self,
					R.string.dialog_action_cache_alert),View.GONE, mHandler);
			break;
		case R.id.shareLayout:
			// TODO
			Intent share = new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_STREAM,Constants.APKURLPATH);
            share.setType("*/*");
            startActivity(Intent.createChooser(share, 
            		CommUtil.getStrValue(self, R.string.settings_item71)));
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Description: 注销用户
	 * @author Comsys-WH1510032
	 */
	private void actionLogout(){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_userId");
		values.add(uTokenId);
		
		requestData("pro_login_out", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				toastMsg(R.string.action_logout_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("msg").get(0).equals(ResponseCode.RESULT_OK)) {
//						setPreferenceData("uid","0");
						setPreferenceData("useId","0");
//						uTokenId = "0";
						MyApplication.userId = "0";
						toastMsg(R.string.action_logout_success);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
