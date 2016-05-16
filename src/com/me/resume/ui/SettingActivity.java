package com.me.resume.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.tools.DataCleanManager;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.PreferenceUtil;
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
	
	private LinearLayout llout021;
	private TextView animvalue;
	
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
				int position = (int) msg.obj;
				String animValueStr = mList.get(position);
				animvalue.setText(animValueStr);
				preferenceUtil.setPreferenceData("switchAnim", animValueStr);
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
		
		llout021 = findView(R.id.llout021); 
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
		
		cacheLayout.setOnClickListener(this);
		versionLayout.setOnClickListener(this);
		feedbackLayout.setOnClickListener(this);
		logoutLayout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		llout021.setOnClickListener(this);
	}
	
	private void switchBtnClick(){
		setting_start_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, boolean checkState) {
				// TODO Auto-generated method stub
				boolean onoff = false;
				if(!preferenceUtil.getPreferenceData("startVerytime")){
					onoff = true;
				}else{
					onoff = false;
				}
				preferenceUtil.setPreferenceData("startVerytime", onoff); 
			}
		});
		
		setting_auto_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, boolean checkState) {
				// TODO Auto-generated method stub
				boolean onoff = false;
				if(!preferenceUtil.getPreferenceData("autoShow")){
					onoff = true;
				}else{
					onoff = false;
				}
				preferenceUtil.setPreferenceData("autoShow", onoff); 
			}
		});
		
		setting_edit_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, boolean checkState) {
				// TODO Auto-generated method stub
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
	
	@Override
	protected void onResume() {
		super.onResume();
		boolean autoShow = preferenceUtil.getPreferenceData("autoShow");
		boolean startVerytime = preferenceUtil.getPreferenceData("startVerytime");
		boolean editmode = preferenceUtil.getPreferenceData("editmode");
		if (startVerytime) {
			setting_start_cb.setChecked(true);
		}else{
			setting_start_cb.setChecked(false);
		}
		if (autoShow ) {
			setting_auto_cb.setChecked(true);
		}else{
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
		case R.id.llout021:
			String[] item_text = CommUtil.getArrayValue(self,R.array.jazzy_effects); 
			mList = Arrays.asList(item_text);
			DialogUtils.showPopWindow(self, llout021, R.string.settings_item11, mList, mHandler);
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
//						preferenceUtil.clearPreferenceData();
						preferenceUtil.setPreferenceData("avator", "");
						preferenceUtil.setPreferenceData("useId","0");
						preferenceUtil.setPreferenceData("isregister", false);
						MyApplication.userId = "0";
						if(Constants.USERHEAD.exists()){
							Constants.USERHEAD.delete();
			        	}
						toastMsg(R.string.action_logout_success);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
