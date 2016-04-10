package com.me.resume.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.L;
import com.me.resume.views.CustomFAB;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: BaseInfoActivity 
* @Description: 个人基本资料
* @author Comsys-WH1510032 
* @date 2016/3/29 下午3:36:12 
*
 */
public class BaseInfoActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	// 姓名; 手机号; 电子邮箱; 护照
	private EditText info_realname,info_phone,info_email,info_nationality,info_license;
	
	// 性别;婚姻;海外工作经验;政治面貌
	private RadioGroup rg_gender,rg_maritalstatus,rg_workingabroad,rg_politicalstatus;
	
	// 性别
	private RadioButton radioman,radiowoman;
		
	// 婚姻
	private RadioButton radio_wh,radio_yh,radio_ly;
	
	// 海外工作经验
	
	// 出生日期; 工作时间; 家乡所在地; 现居住地
	private TextView info_brithday,info_workyear,info_hometown,info_city;
	
	String rg_genderStr = "";
	String rg_maritalstatusStr = "";
	String rg_workingabroadStr = "";
	String rg_politicalstatusStr = "";
	
	private CustomFAB save,next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baseinfo_layout);
		findViews();
		
		getChooseValue();
		
		initViews();
	}

	private void findViews(){
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		leftLable.setOnClickListener(this);
		rightLable = findView(R.id.right_lable);
		rightLable.setOnClickListener(this);
		
		info_realname = findView(R.id.info_realname);
		info_phone = findView(R.id.info_phone);
		info_email = findView(R.id.info_email);
		info_nationality = findView(R.id.info_nationality);
		info_license = findView(R.id.info_license);
		
		rg_gender = findView(R.id.rg_gender);
		rg_maritalstatus = findView(R.id.rg_maritalstatus);
		radio_wh = findView(R.id.radio_wh);
		radio_yh = findView(R.id.radio_yh);
		radio_ly = findView(R.id.radio_ly);
		
		rg_workingabroad = findView(R.id.rg_workingabroad);
		rg_politicalstatus = findView(R.id.rg_politicalstatus);
		
		info_brithday = findView(R.id.info_brithday);
		info_workyear = findView(R.id.info_workyear);
		info_hometown = findView(R.id.info_hometown);
		info_city = findView(R.id.info_city);
		
		save = findView(R.id.save);
		next = findView(R.id.next);
		
		save.setOnClickListener(this);
		next.setOnClickListener(this);
	}
	
	private void initViews() {
		toptext.setText(CommUtil.getStrValue(BaseInfoActivity.this, R.string.resume_baseinfo));
		rightLable.setText(CommUtil.getStrValue(BaseInfoActivity.this, R.string.review_resume));
		
		
	}

	private  void getChooseValue(){
		rg_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		rg_maritalstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (checkedId == radio_wh.getId()) {
							rg_maritalstatusStr = "1";
						}else if(checkedId == radio_yh.getId()){
							rg_maritalstatusStr = "2";
						}else if(checkedId == radio_ly.getId()){
							rg_maritalstatusStr = "3";
						}
					}
				});

		rg_workingabroad.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});

		rg_politicalstatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
//			setPreferenceData("info_realname",info_realname.getText().toString().trim());
			String info_realnameStr = CommUtil.getEditTextValue(info_realname);
			String info_phoneStr = CommUtil.getEditTextValue(info_phone);
			String info_emailStr = CommUtil.getEditTextValue(info_email);
			String info_nationalityStr = CommUtil.getEditTextValue(info_nationality);
			String info_licenseStr = CommUtil.getEditTextValue(info_license);
			
			String info_brithdayStr = CommUtil.getTextValue(info_brithday);
			String info_workyearStr = CommUtil.getTextValue(info_workyear);
			String info_hometownStr = CommUtil.getTextValue(info_hometown);
			String info_cityStr = CommUtil.getTextValue(info_city);
			
			ContentValues cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("realname", info_realnameStr);
			cValues.put("gender", "1");
			cValues.put("brithday", "1990-10-12");
			cValues.put("joinworktime", "2014-01-12");
			cValues.put("phone", info_phoneStr);
			cValues.put("hometown", "湖北黄石");
			cValues.put("areacity", "武汉");
			cValues.put("areaemail", info_emailStr);
			cValues.put("ismarry", rg_maritalstatusStr);
			cValues.put("nationality", info_nationalityStr);
			cValues.put("license", info_licenseStr);
			cValues.put("workingabroad", "0");
			cValues.put("politicalstatus", "3");
			cValues.put("avator", "/image/aa.jpg");
			
			boolean addBaseInfo = dbUtil.insertData(BaseInfoActivity.this, 
					CommonText.BASEINFO, cValues);
			L.d("==addBaseInfoActivity=="+addBaseInfo);
			
			break;
		case R.id.next:
			ActivityUtils.startActivity(BaseInfoActivity.this, MyApplication.PACKAGENAME+".ui.WorkExperienceActivity");
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(BaseInfoActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
		
	}
}
