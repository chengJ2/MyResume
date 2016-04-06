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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		radioGroup = findView(R.id.radioGroup);
		mRadio1 = findView(R.id.radioyes);
		mRadio2 = findView(R.id.radiono);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		toptext.setText(CommUtil.getStrValue(SettingActivity.this, R.string.action_settings));
		rightLable.setVisibility(View.INVISIBLE);
		
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
