package com.me.resume.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.L;
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
public class SettingActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext;
	
	private ImageView left_icon,right_icon;
	
	private RadioGroup radioGroup_show;
	private RadioButton radio_left, radio_right,radionv_middle;
	
	private SwitchButton setting_start_cb,setting_auto_cb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		radioGroup_show = findView(R.id.radioGroup_show);
		radio_left = findView(R.id.radio_left);
		radio_right = findView(R.id.radio_right);
		radionv_middle = findView(R.id.radionv_middle);
		
		setting_start_cb = findView(R.id.setting_start_cb);
		
		setting_auto_cb = findView(R.id.setting_auto_cb);
		
		toptext = findView(R.id.top_text);
		left_icon = findView(R.id.left_lable);
		right_icon = findView(R.id.right_icon);
		left_icon.setOnClickListener(this);
		toptext.setText(CommUtil.getStrValue(self, R.string.action_settings));
		right_icon.setVisibility(View.INVISIBLE);
		
		radio_left.setChecked(true);
		radio_right.setChecked(false);
		radionv_middle.setChecked(false);
		
		radioGroup_show.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

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
		});
		
		setting_start_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, boolean checkState) {
				// TODO Auto-generated method stub
				int onoff = 0;
				if(startVerytime == 0){
					onoff = 1;
				}else{
					onoff = 0;
				}
				L.d("===onoff===="+onoff);
				setPreferenceData("startVerytime", onoff); 
			}
		});
		
		setting_auto_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, boolean checkState) {
				// TODO Auto-generated method stub
				int onoff = 0;
				if(autoShow == 0){
					onoff = 1;
				}else{
					onoff = 0;
				}
				L.d("===onoff===="+onoff);
				setPreferenceData("autoShow", onoff); 
			}
		});
	}

	private int autoShow = 0,startVerytime = 0;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		autoShow = getPreferenceData("autoShow", 0);
		startVerytime = getPreferenceData("startVerytime", 0);
		
		L.d("===autoShow===="+autoShow + "  startVerytime:"+startVerytime);
		
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
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		default:
			break;
		}
		
	}
}
