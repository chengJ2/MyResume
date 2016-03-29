package com.me.resume;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 
* @ClassName: SettingActivity 
* @Description: App设置界面 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午5:04:43 
*
 */
public class SettingActivity extends BaseActivity {

	private RadioGroup radioButton;
	private RadioButton mRadio1, mRadio2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		radioButton = findView(R.id.radioGroup);
		mRadio1 = findView(R.id.radioyes);
		mRadio2 = findView(R.id.radiono);
		
		mRadio1.setChecked(true);
		mRadio2.setChecked(false);
		radioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == mRadio1.getId()) {
					setPreferenceData("firstInstall","1");
				}else{
					setPreferenceData("firstInstall","0");
				}
			}
		});
		
	}
}
