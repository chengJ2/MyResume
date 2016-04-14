package com.me.resume.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
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
import com.me.resume.utils.DialogUtils;
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

	private BaseInfoActivity self;
	
	private TextView toptext,leftLable,rightLable;
	
	// 姓名; 手机号; 电子邮箱; 护照
	private EditText info_realname,info_phone,info_email,info_nationality,info_license;
	
	// 性别;海外工作经验;
	private RadioGroup rg_gender,rg_workingabroad;
	
	// 性别
	private RadioButton radioman,radiowoman;
	
	// 海外工作经验
	private RadioButton radio_yes,radio_no;
		
	// 出生日期; 工作时间; 家乡所在地; 现居住地;婚姻;政治面貌
	private TextView info_brithday,info_workyear,info_hometown,info_city,info_maritalstatus,info_politicalstatus;
	
	String rg_genderStr = "0";
	String rg_maritalstatusStr = "0";
	String rg_workingabroadStr = "0";
	String rg_politicalstatusStr = "0";
	
	private CustomFAB save_edit,next;
	
	private List<String> mList = null;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				DialogUtils.dismissPopwindow();
				break;
			case 2:
				int position = (int) msg.obj;
				if (whichTab == 1) {
					info_maritalstatus.setText(mList.get(position));
					if (position == 0) {
						rg_maritalstatusStr = "1";
					}else if (position == 1) {
						rg_maritalstatusStr = "2";
					}else if (position == 2) {
						rg_maritalstatusStr = "3";
					}
				}else if(whichTab == 2){
					info_politicalstatus.setText(mList.get(position));
					if (position == 0) {
						rg_politicalstatusStr = "1";
					}else if (position == 1) {
						rg_politicalstatusStr = "2";
					}else if (position == 2) {
						rg_politicalstatusStr = "3";
					}else if (position == 3) {
						rg_politicalstatusStr = "4";
					}else if (position == 4) {
						rg_politicalstatusStr = "5";
					}
				}
				DialogUtils.dismissPopwindow();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baseinfo_layout);
		self = BaseInfoActivity.this;
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
		radioman = findView(R.id.radioman);
		radiowoman  = findView(R.id.radiowoman);
		
		info_maritalstatus = findView(R.id.info_maritalstatus);
		info_politicalstatus = findView(R.id.info_politicalstatus);
		
		rg_workingabroad = findView(R.id.rg_workingabroad);
		radio_yes  = findView(R.id.radio_yes);
		radio_no = findView(R.id.radio_no);
		
		info_brithday = findView(R.id.info_brithday);
		info_workyear = findView(R.id.info_workyear);
		info_hometown = findView(R.id.info_hometown);
		info_city = findView(R.id.info_city);
		
		save_edit = findView(R.id.save_edit);
		next = findView(R.id.next);
		
		save_edit.setOnClickListener(this);
		next.setOnClickListener(this);
		
		info_maritalstatus.setOnClickListener(this);
		info_politicalstatus.setOnClickListener(this);
	}
	
	private void initViews() {
		toptext.setText(CommUtil.getStrValue(self, R.string.resume_baseinfo));
		rightLable.setText(CommUtil.getStrValue(self, R.string.review_resume));
		
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			save_edit.setImageResource(R.drawable.ic_btn_edit);
		}else{
			save_edit.setImageResource(R.drawable.ic_btn_add);
		}
		
	}

	private  void getChooseValue(){
		rg_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == radioman.getId()) {
					rg_genderStr = "0";
				}else if (checkedId == radiowoman.getId()){
					rg_genderStr = "1";
				}
			}
		});
		
		rg_workingabroad.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == radio_yes.getId()) {
					rg_workingabroadStr = "1";
				}else if (checkedId == radio_no.getId()){
					rg_workingabroadStr = "0";
				}
			}
		});

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save_edit:
//			if (CommUtil.textIsNull(info_brithday)) {
//				return;
//			}
			
//			if (CommUtil.textIsNull(info_workyear)) {
//				return;
//			}
			
//			if (CommUtil.textIsNull(info_hometown)) {
//				return;
//			}
			
//			if (CommUtil.textIsNull(info_city)) {
//				return;
//			}
			
			if (CommUtil.EditTextIsNull(info_realname)) {
				return;
			}
			
			if (CommUtil.EditTextIsNull(info_phone)) {
				return;
			}
			
			if (CommUtil.EditTextIsNull(info_email)) {
				return;
			}
			
			String info_realnameStr = CommUtil.getEditTextValue(info_realname);
			String info_phoneStr = CommUtil.getEditTextValue(info_phone);
			String info_emailStr = CommUtil.getEditTextValue(info_email);
			String info_nationalityStr = CommUtil.getEditTextValue(info_nationality);
			String info_licenseStr = CommUtil.getEditTextValue(info_license);
			
			String info_brithdayStr = CommUtil.getTextValue(info_brithday);
			String info_workyearStr = CommUtil.getTextValue(info_workyear);
			String info_hometownStr = CommUtil.getTextValue(info_hometown);
			String info_cityStr = CommUtil.getTextValue(info_city);
			
			queryWhere = "select * from " + CommonText.BASEINFO + " where userId = 1";
			commMapArray = dbUtil.queryData(self, queryWhere);
			if (commMapArray!= null && commMapArray.get("userId").length > 0) {
				
				String edId = commMapArray.get("_id")[0];
				int upd = dbUtil.updateData(self, CommonText.EVALUATION, 
						new String[]{edId,"realname","gender"}, 
						new String[]{"1",info_realnameStr,rg_genderStr});
				if (upd == 1) {
					CommUtil.ToastMsg(self, R.string.action_update_success);
				}
			}else{
				ContentValues cValues = new ContentValues();
				cValues.put("userId", "1");
				cValues.put("realname", info_realnameStr);
				cValues.put("gender", rg_genderStr);
				cValues.put("brithday", "1990-10-12");
				cValues.put("joinworktime", "2014-01-12");
				cValues.put("phone", info_phoneStr);
				cValues.put("hometown", "湖北黄石");
				cValues.put("city", "武汉");
				cValues.put("email", info_emailStr);
				cValues.put("ismarry", rg_maritalstatusStr);
				cValues.put("nationality", info_nationalityStr);
				cValues.put("license", info_licenseStr);
				cValues.put("workingabroad", rg_workingabroadStr);
				cValues.put("politicalstatus", rg_politicalstatusStr);
				cValues.put("avator", "/image/aa.jpg");
				
				boolean addBaseInfo = dbUtil.insertData(self, 
						CommonText.BASEINFO, cValues);
				if (addBaseInfo) {
					save_edit.setImageResource(R.drawable.ic_btn_edit);
				}
			}
			break;
		case R.id.info_maritalstatus:
			whichTab = 1;
			getValues(R.array.ba_maritalstatus_values,info_maritalstatus,R.string.info_maritalstatus);
			break;
		case R.id.info_politicalstatus:
			whichTab = 2;
			getValues(R.array.ba_politicalstatus_values,info_politicalstatus,R.string.info_politicalstatus);
			break;
		case R.id.next:
			// TODO
			ActivityUtils.startActivity(self, MyApplication.PACKAGENAME+".ui.WorkExperienceActivity");
			break;
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(self, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
	}
	
	private void getValues(int array,View parent,int resId) {
		String[] item_text = CommUtil.getArrayValue(self,array); 
		mList = Arrays.asList(item_text);
		DialogUtils.showPopWindow(self, parent, resId, mList, mHandler);
	}
}
