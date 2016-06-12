package com.me.resume.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.service.DownloadService;
import com.me.resume.tools.DataCleanManager;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;

/**
 * 
* @ClassName: SettingActivity 
* @Description: App设置界面 
* @date 2016/3/29 下午5:04:43 
*
 */
public class SettingActivity extends BaseActivity implements OnClickListener{

	private ToggleButton setting_start_cb,setting_auto_cb,setting_mode_cb;
	
	private LinearLayout cacheLayout,versionLayout,feedbackLayout,logoutLayout,shareLayout,aboutusLayout;
	
	private TextView cachesize,version;
	
	private LinearLayout llout020,llout021;
	private TextView effectsdurationfeild,effectsduration,animvaluefeild,animvalue;
	
	private boolean isupdate = false;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					DataCleanManager.cleanDatabases(self);
					preferenceUtil.clearPreferenceData();
					cachesize.setText("0KB");
					
					if (!preferenceUtil.getPreferenceData(UserInfoCode.USEID,"0").equals("0")) {
						preferenceUtil.setPreferenceData(UserInfoCode.USEID,"0");
						if(CommUtil.isNetworkAvailable(self)){
							actionLogout();
						}
					}
					toastMsg(R.string.action_clearcache_success);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				if (whichTab == 1) {
					int position = (int) msg.obj;
					setEffectsDuration(effectsduration,position);
				}else{
					int position = (int) msg.obj;
					String animValueStr = mList.get(position);
					animvalue.setText(animValueStr);
					preferenceUtil.setPreferenceData(Constants.SET_SWITCHANIM, animValueStr);
				}
				break;
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
		
		View v = View.inflate(self,R.layout.activity_setting, null);
		boayLayout.addView(v);
	
		findViews();
		initViews();
		switchBtnClick();
	}

	private void findViews(){
		setting_start_cb = findView(R.id.setting_start_cb);
		setting_auto_cb = findView(R.id.setting_auto_cb);
		setting_mode_cb = findView(R.id.setting_mode_cb);
		
		cacheLayout = findView(R.id.cacheLayout);
		cachesize = findView(R.id.cachesize);
		versionLayout = findView(R.id.versionLayout);
		feedbackLayout = findView(R.id.feedbackLayout);
		version = findView(R.id.viewsion);
		logoutLayout = findView(R.id.logoutLayout);
		shareLayout = findView(R.id.shareLayout);
		aboutusLayout = findView(R.id.aboutusLayout);
		
		llout020 = findView(R.id.llout020);
		effectsdurationfeild = findView(R.id.effectsdurationfeild);
		effectsduration = findView(R.id.effectsduration);
		llout021 = findView(R.id.llout021); 
		animvaluefeild = findView(R.id.animvaluefeild);
		animvalue = findView(R.id.animvalue);
	}
	
	/**
	 * 初始化View
	 */
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
		
		effectsduration.setText(getEffectdDuration(preferenceUtil.getPreferenceData(Constants.SET_SWITCHEFFDURATION,Constants.DEFAULTIME)));
		animvalue.setText(preferenceUtil.getPreferenceData(Constants.SET_SWITCHANIM, "Standard"));
		
		cacheLayout.setOnClickListener(this);
		versionLayout.setOnClickListener(this);
		feedbackLayout.setOnClickListener(this);
		logoutLayout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		aboutusLayout.setOnClickListener(this);
		llout020.setOnClickListener(this);
		llout021.setOnClickListener(this);
	}
	
	private void switchBtnClick(){
		setting_start_cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boolean onoff = false;
				if(preferenceUtil.getPreferenceData(Constants.SET_STARTVERYTIME)){
					onoff = false;
					setting_start_cb.setChecked(false);
				}else{
					onoff = true;
					setting_start_cb.setChecked(true);
				}
				preferenceUtil.setPreferenceData(Constants.SET_STARTVERYTIME, onoff); 
			}
		});
		
		setting_auto_cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boolean onoff = false;
				if(preferenceUtil.getPreferenceData(Constants.SET_AUTOSHOW)){
					onoff = false;
					setting_auto_cb.setChecked(false);
					setEffectDuration(false,R.color.grey_70);
				}else{
					onoff = true;
					setting_auto_cb.setChecked(true);
					setEffectDuration(true,R.color.black);
				}
				preferenceUtil.setPreferenceData(Constants.SET_AUTOSHOW, onoff); 
			}
		});
		setting_mode_cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boolean onoff = false;
				if(preferenceUtil.getPreferenceData(Constants.EDITMODE)){
					onoff = false;
					setting_mode_cb.setChecked(false);
				}else{
					onoff = true;
					setting_mode_cb.setChecked(true);
				}
				preferenceUtil.setPreferenceData(Constants.EDITMODE, onoff); 
			}
		});
		
	}
	
	/**
	 * 设置效果切换及时段item项
	 */
	private void setEffectDuration(boolean enabled,int color){
		llout020.setEnabled(enabled);
		llout021.setEnabled(enabled);
		effectsdurationfeild.setTextColor(getColorValue(color));
		effectsduration.setTextColor(getColorValue(color));
		animvaluefeild.setTextColor(getColorValue(color));
		animvalue.setTextColor(getColorValue(color));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		boolean startVerytime = preferenceUtil.getPreferenceData(Constants.SET_STARTVERYTIME);
		boolean autoShow = preferenceUtil.getPreferenceData(Constants.SET_AUTOSHOW);
		boolean editmode = preferenceUtil.getPreferenceData(Constants.EDITMODE);
		if (startVerytime) {
			setting_start_cb.setChecked(true);
		}else{
			setting_start_cb.setChecked(false);
		}
		if (autoShow) {
			setEffectDuration(true,R.color.black);
			setting_auto_cb.setChecked(true);
		}else{
			setEffectDuration(false,R.color.grey_70);
			setting_auto_cb.setChecked(false);
		}
		if (editmode) {
			setting_mode_cb.setChecked(true);
		}else{
			setting_mode_cb.setChecked(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.versionLayout:
			checkNewVersin();
			break;
		case R.id.feedbackLayout:
			startChildActivity(Constants.FEEDBACK, false);
			break;
		case R.id.logoutLayout:
			if (!MyApplication.USERID.equals("0")) {
				actionLogout();
			}else{
				toastMsg(R.string.action_no_login);
			}
			break;
		case R.id.cacheLayout:
			DialogUtils.showAlertDialog(self, getStrValue(
					R.string.dialog_action_cache_alert),View.GONE, mHandler);
			break;
		case R.id.shareLayout:
			Intent share = new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_STREAM,Constants.APKURLPATH);
            share.setType("*/*");
            startActivity(Intent.createChooser(share, 
            		getStrValue(R.string.settings_item71)));
			break;
		case R.id.aboutusLayout:
			startChildActivity(Constants.ABOUTAPP, false);
			break;
		case R.id.llout021:
			whichTab = 2;
			item_values = CommUtil.getArrayValue(self,R.array.jazzy_effects); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, llout021, R.string.settings_item11, mList, mHandler);
			break;
		case R.id.llout020:
			whichTab = 1;
			item_values = CommUtil.getArrayValue(self,R.array.auto_show_effects_duration); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, llout020, R.string.settings_item20, mList, mHandler);
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
				toastMsg(R.string.settings_item31);
			}
		});
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
		
		requestData("pro_user_loginout", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				toastMsg(R.string.action_logout_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("msg").get(0).equals(ResponseCode.RESULT_OK)) {
						preferenceUtil.setPreferenceData(UserInfoCode.AVATOR, "");
						preferenceUtil.setPreferenceData(UserInfoCode.USEID,"0");
						preferenceUtil.setPreferenceData(UserInfoCode.ISREGISTER, false);
						MyApplication.USERID = "0";
//						if(FileUtils.existsFile(MyApplication.USERAVATORPATH)){
//							new File(MyApplication.USERAVATORPATH).delete();
//			        	}
						toastMsg(R.string.action_logout_success);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 设置动画切换时段
	 * @param tv
	 * @param position
	 */
	private void setEffectsDuration(TextView tv,int position){
		int duration = 5 * 1000;
		item_values = CommUtil.getArrayValue(self,R.array.auto_show_effects_duration); 
		if(position == 0){
			duration = 5 * 1000;
		}else if(position == 1){
			duration = 10 * 1000;
		}else if(position == 1){
			duration = 15 * 1000;
		}else if(position == 1){
			duration = 30 * 1000;
		}else if(position == 1){
			duration = 45 * 1000;
		}else if(position == 1){
			duration = 60 * 1000;
		}
		tv.setText(item_values[position]);
		preferenceUtil.setPreferenceData(Constants.SET_SWITCHEFFDURATION, duration);
	}
	
	/**
	 * 设置动画切换时段
	 * @param tv
	 * @param position
	 */
	private String getEffectdDuration(int duration){
		int position = 0;
		item_values = CommUtil.getArrayValue(self,R.array.auto_show_effects_duration);
		if(duration == 5 * 1000){
			position = 0;
		}else if(duration == 10 * 1000){
			position = 1;
		}else if(duration == 15 * 1000){
			position = 2;
		}else if(duration == 30 * 1000){
			position = 3;
		}else if(duration == 45 * 1000){
			position = 4;
		}else if(duration == 06 * 1000){
			position = 5;
		}
		return item_values[position];
	}
}
