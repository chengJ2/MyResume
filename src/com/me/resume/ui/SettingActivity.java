package com.me.resume.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.tools.DataCleanManager;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.FileUtils;
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

	private ToggleButton setting_start_cb,setting_auto_cb;
	private SwitchButton setting_edit_cb;
	
	private LinearLayout cacheLayout,versionLayout,feedbackLayout,logoutLayout,shareLayout;
	
	private TextView cachesize,version;
	
	private LinearLayout llout020,llout021;
	private TextView effectsdurationfeild,effectsduration,animvaluefeild,animvalue;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					DataCleanManager.cleanDatabases(self);
					preferenceUtil.clearPreferenceData();
					cachesize.setText("0KB");
					
					if (!preferenceUtil.getPreferenceData("useId","0").equals("0")) {
						preferenceUtil.setPreferenceData("useId","0");
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
					preferenceUtil.setPreferenceData("switchAnim", animValueStr);
				}
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
		
		llout020 = findView(R.id.llout020);
		effectsdurationfeild = findView(R.id.effectsdurationfeild);
		effectsduration = findView(R.id.effectsduration);
		llout021 = findView(R.id.llout021); 
		animvaluefeild = findView(R.id.animvaluefeild);
		animvalue = findView(R.id.animvalue);
		
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
		
		effectsduration.setText(getEffectdDuration(preferenceUtil.getPreferenceData("switchEffDuration",5*1000)));
		animvalue.setText(preferenceUtil.getPreferenceData("switchAnim", "Standard"));
		
		cacheLayout.setOnClickListener(this);
		versionLayout.setOnClickListener(this);
		feedbackLayout.setOnClickListener(this);
		logoutLayout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		llout020.setOnClickListener(this);
		llout021.setOnClickListener(this);
	}
	
	private void switchBtnClick(){
		setting_start_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean onoff = false;
				if(!preferenceUtil.getPreferenceData("startVerytime")){
					onoff = true;
				}else{
					onoff = false;
				}
				preferenceUtil.setPreferenceData("startVerytime", onoff); 
			}
		});
		
		setting_auto_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean onoff = false;
				if(!preferenceUtil.getPreferenceData("autoShow")){
					onoff = true;
					setEffectDuration(true,R.color.black);
				}else{
					onoff = false;
					setEffectDuration(false,R.color.grey_70);
				}
				preferenceUtil.setPreferenceData("autoShow", onoff); 
			}
		});
		
		setting_edit_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, boolean checkState) {
				boolean onoff = false;
				if(!preferenceUtil.getPreferenceData("editmode")){
					onoff = true;
				}else{
					onoff = false;
				}
				preferenceUtil.setPreferenceData("editmode", onoff); 
			}
		});
	}
	
	/**
	 * 设置效果切换及时段item项
	 */
	private void setEffectDuration(boolean enabled,int color){
		llout020.setEnabled(enabled);
		llout021.setEnabled(enabled);
		effectsdurationfeild.setTextColor(CommUtil.getColorValue(self, color));
		effectsduration.setTextColor(CommUtil.getColorValue(self, color));
		animvaluefeild.setTextColor(CommUtil.getColorValue(self, color));
		animvalue.setTextColor(CommUtil.getColorValue(self, color));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		boolean startVerytime = preferenceUtil.getPreferenceData("startVerytime");
		boolean autoShow = preferenceUtil.getPreferenceData("autoShow");
		boolean editmode = preferenceUtil.getPreferenceData("editmode");
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
			setting_edit_cb.setChecked(true);
		}else{
			setting_edit_cb.setChecked(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.versionLayout:
			CommUtil.ToastMsg(self, "暂无新版本");
			break;
		case R.id.feedbackLayout:
			startChildActivity("FeedBackActivity", false);
			break;
		case R.id.logoutLayout:
			if (!MyApplication.USERID.equals("0")) {
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
						preferenceUtil.setPreferenceData("avator", "");
						preferenceUtil.setPreferenceData("useId","0");
						preferenceUtil.setPreferenceData("isregister", false);
						MyApplication.USERID = "0";
						if(FileUtils.existsFile(MyApplication.USERAVATORPATH)){
							new File(MyApplication.USERAVATORPATH).delete();
			        	}
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
		preferenceUtil.setPreferenceData("switchEffDuration", duration);
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
