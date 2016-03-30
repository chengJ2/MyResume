package com.me.resume.ui;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.utils.L;

/**
 * 
* @ClassName: SettingActivity 
* @Description: App设置界面 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午5:04:43 
*
 */
public class SettingActivity extends BaseActivity {

	private RadioGroup radioGroup;
	private RadioButton mRadio1, mRadio2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		radioGroup = findView(R.id.radioGroup);
		mRadio1 = findView(R.id.radioyes);
		mRadio2 = findView(R.id.radiono);
		
		mRadio1.setChecked(true);
		mRadio2.setChecked(false);
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
		
	}
}
