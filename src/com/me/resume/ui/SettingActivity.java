package com.me.resume.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.CommUtil;

/**
 * 
* @ClassName: SettingActivity 
* @Description: App设置界面 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午5:04:43 
*
 */
public class SettingActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private RadioGroup radioGroup;
	private RadioButton mRadio1, mRadio2;
	
	private RadioGroup radioGroup_show;
	private RadioButton radio_left, radio_right,radionv_middle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		radioGroup = findView(R.id.radioGroup);
		mRadio1 = findView(R.id.radioyes);
		mRadio2 = findView(R.id.radiono);
		
		radioGroup_show = findView(R.id.radioGroup_show);
		radio_left = findView(R.id.radio_left);
		radio_right = findView(R.id.radio_right);
		radionv_middle = findView(R.id.radionv_middle);
		
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		toptext.setText(CommUtil.getStrValue(SettingActivity.this, R.string.action_settings));
		rightLable.setVisibility(View.INVISIBLE);
		
		mRadio1.setChecked(true);
		mRadio2.setChecked(false);
		
		radio_left.setChecked(true);
		radio_right.setChecked(false);
		radionv_middle.setChecked(false);
		
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == mRadio1.getId()) {
					setPreferenceData("firstInstall",1);
				}else{
					setPreferenceData("firstInstall",0);
				}
			}
		});
		
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			SettingActivity.this.finish();
			break;
		default:
			break;
		}
		
	}
}
