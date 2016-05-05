package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
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

	private RadioGroup radioGroup_show;
	private RadioButton radio_left, radio_right,radionv_middle;
	
	private SwitchButton setting_start_cb,setting_auto_cb,setting_edit_cb;
	
	private LinearLayout versionLayout,feedbackLayout,logoutLayout,shareLayout;
	
	private TextView version;
	
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
/*		radioGroup_show = findView(R.id.radioGroup_show);
		radio_left = findView(R.id.radio_left);
		radio_right = findView(R.id.radio_right);
		radionv_middle = findView(R.id.radionv_middle);*/
		
		setting_start_cb = findView(R.id.setting_start_cb);
		setting_auto_cb = findView(R.id.setting_auto_cb);
		setting_edit_cb = findView(R.id.setting_edit_cb);
		
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
		
		version.setText(CommUtil.getVersionName(self));
		
		radio_left.setChecked(true);
		radio_right.setChecked(false);
		radionv_middle.setChecked(false);
		
		versionLayout.setOnClickListener(this);
		feedbackLayout.setOnClickListener(this);
		logoutLayout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
	}
	
	private void switchBtnClick(){
		/*radioGroup_show.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == radio_left.getId()) {
				setPreferenceData("we_show_nav",1);
			}else if (checkedId == radio_right.getId()){
				setPreferenceData("we_show_nav",2);
			}else if (checkedId == radionv_middle.getId()){
				setPreferenceData("we_show_nav",3);
			}
		}
	});*/
	
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
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.versionLayout:
			CommUtil.ToastMsg(self, "暂无新版本");
			break;
		case R.id.feedbackLayout:
			startActivity(".ui.FeedBackActivity", false);
			break;
		case R.id.logoutLayout:
			if (MyApplication.userId != 0) {
				logoutSend();
			}else{
				toastMsg(R.string.action_no_login);
			}
			break;
		case R.id.shareLayout:
			// TODO
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
	private void logoutSend(){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_userId");
		values.add(kId);
		
		requestData("pro_login_out", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				runOnUiThread(R.string.action_logout_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("msg").get(0).equals("200")) {
						setPreferenceData("useId",0);
						
						MyApplication.userId = getPreferenceData("useId", 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
