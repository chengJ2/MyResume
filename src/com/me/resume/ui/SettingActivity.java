package com.me.resume.ui;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.service.DownloadService;
import com.me.resume.tools.DataCleanManager;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;

/**
 * 
* 设置界面 
* @date 2016/3/29 下午5:04:43 
*
 */
public class SettingActivity extends BaseActivity implements OnClickListener{

	private ToggleButton setting_start_cb,setting_auto_cb,setting_mode_cb,setting_sync_cb;
	
	private LinearLayout cacheLayout,feedbackLayout,shareLayout,aboutusLayout;
	
	private TextView cachesize;
	
	private LinearLayout llout020,llout021;
	private TextView effectsdurationfeild,effectsduration,animvaluefeild,animvalue;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				try {
					DataCleanManager.cleanDatabases(self);
					preferenceUtil.clearPreferenceData();
					preferenceUtil.setPreferenceData(Constants.FIRSTINSTALL,false);
					cachesize.setText("0KB");
					HomeActivity.userstatus = true;
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
					
					item_values = CommUtil.getArrayValue(self,R.array.jazzy_effects); 
					mList = Arrays.asList(item_values);
					animValueStr = mList.get(position);
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
		setting_sync_cb = findView(R.id.setting_sync_cb);
		
		cacheLayout = findView(R.id.cacheLayout);
		cachesize = findView(R.id.cachesize);
		feedbackLayout = findView(R.id.feedbackLayout);
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
		
		effectsduration.setText(getEffectdDuration(preferenceUtil.getPreferenceData(Constants.SET_SWITCHEFFDURATION,Constants.DEFAULTEFFECTTIME)));
		
		String animValueStr = preferenceUtil.getPreferenceData(Constants.SET_SWITCHANIM, Constants.EFFECT_STANDARD);
		item_values = CommUtil.getArrayValue(self,R.array.jazzy_effects); 
		int length = item_values.length;
		for (int i = 0; i < length; i++) {
			if (animValueStr.equals(item_values[i])) {
				item_values = CommUtil.getArrayValue(self,R.array.jazzy_effects_zh); 
				animvalue.setText(item_values[i]);
			}
		}
		
		cacheLayout.setOnClickListener(this);
		feedbackLayout.setOnClickListener(this);
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
		
		setting_sync_cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boolean onoff = false;
				if(preferenceUtil.getPreferenceData(Constants.AUTOSYNC)){
					onoff = false;
					setting_sync_cb.setChecked(false);
				}else{
					onoff = true;
					setting_sync_cb.setChecked(true);
				}
				preferenceUtil.setPreferenceData(Constants.AUTOSYNC, onoff); 
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
		boolean autosync = preferenceUtil.getPreferenceData(Constants.AUTOSYNC);
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
		if (autosync) {
			setting_sync_cb.setChecked(true);
		}else{
			setting_sync_cb.setChecked(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.feedbackLayout:
			startChildActivity(Constants.FEEDBACK, false);
			break;
		case R.id.cacheLayout:
			DialogUtils.showAlertDialog(self, 
					getStrValue(R.string.dialog_action_cache_alert),View.GONE,
					getStrValue(R.string.show_button_sure),mHandler);
			break;
		case R.id.shareLayout:
			Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("*/*");
            share.putExtra(Intent.EXTRA_SUBJECT, getStrValue(R.string.settings_item72));  
            share.putExtra(Intent.EXTRA_TEXT, getStrValue(R.string.settings_item72));  
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            startActivity(Intent.createChooser(share, 
            		getStrValue(R.string.settings_item71)));
			break;
		case R.id.aboutusLayout:
			startChildActivity(Constants.ABOUTAPP, false);
			break;
		case R.id.llout021:
			whichTab = 2;
			item_values = CommUtil.getArrayValue(self,R.array.jazzy_effects_zh); 
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
	 * 设置动画切换时段
	 * @param tv
	 * @param position
	 */
	private void setEffectsDuration(TextView tv,int position){
		int duration = 3 * 1000;
		item_values = CommUtil.getArrayValue(self,R.array.auto_show_effects_duration); 
		if(position == 0){
			duration = 3 * 1000;
		}else if(position == 1){
			duration = 5 * 1000;
		}else if(position == 1){
			duration = 7 * 1000;
		}else if(position == 1){
			duration = 9 * 1000;
		}else if(position == 1){
			duration = 11 * 1000;
		}else if(position == 1){
			duration = 15 * 1000;
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
		if(duration == 3 * 1000){
			position = 0;
		}else if(duration == 5 * 1000){
			position = 1;
		}else if(duration == 7 * 1000){
			position = 2;
		}else if(duration == 9 * 1000){
			position = 3;
		}else if(duration == 11 * 1000){
			position = 4;
		}else if(duration == 15 * 1000){
			position = 5;
		}
		return item_values[position];
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}
}
