package com.me.resume.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.tools.L;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: BaseInfoActivity 
* @Description: 个人基本资料
* @date 2016/3/29 下午3:36:12 
*
 */
public class BaseInfoActivity extends BaseActivity implements OnClickListener{
	
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
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				break;
			case 2:
				int position = (int) msg.obj;
				if (whichTab == 1) {
					info_maritalstatus.setText(mList.get(position));
					if (position == 0) {
						rg_maritalstatusStr = "0";
					}else if (position == 1) {
						rg_maritalstatusStr = "1";
					}else if (position == 2) {
						rg_maritalstatusStr = "2";
					}
				}else if(whichTab == 2){
					info_politicalstatus.setText(mList.get(position));
					if (position == 0) {
						rg_politicalstatusStr = "0";
					}else if (position == 1) {
						rg_politicalstatusStr = "1";
					}else if (position == 2) {
						rg_politicalstatusStr = "2";
					}else if (position == 3) {
						rg_politicalstatusStr = "3";
					}else if (position == 4) {
						rg_politicalstatusStr = "4";
					}
				}
				break;
			case 12:
				if (msg.obj != null) {
					info_brithday.setText((String)msg.obj);
				}
				break;
			case 13:
				if (msg.obj != null) {
					info_workyear.setText(((String)msg.obj)/*.substring(0, 7)*/);
				}
				break;
			case 100:
				initData();
				break;
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.BASEINFO, 
							new String[]{kId,"background"}, 
							new String[]{"1",String.valueOf(checkColor)});
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
					}else{
						toastMsg(R.string.action_update_fail);
					}
				}
				break;
			case OnTopMenu.MSG_MENU2:
				if (msg.obj != null) {
					setPreferenceData("edit_mode",(boolean) msg.obj);
				}
				break;
			case OnTopMenu.MSG_MENU3:
				set2Msg(R.string.action_syncing);
				syncData();
				break;
			case OnTopMenu.MSG_MENU31:
				toastMsg(R.string.action_login_head);
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
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_baseinfo_layout, null);
		boayLayout.addView(v);
		
		findViews();
		getChooseValue();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(100);
			}
		},100);
	}

	private void findViews(){
		setTitle(R.string.resume_baseinfo);
		setMsgHide();
		setRight2IconVisible(View.VISIBLE);
		
		setfabLayoutVisible(View.VISIBLE);
		setEditBtnVisible(View.GONE);
		
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
		radio_yes.setChecked(false);
		radio_no = findView(R.id.radio_no);
		radio_no.setChecked(true);
		
		info_brithday = findView(R.id.info_brithday);
		info_workyear = findView(R.id.info_workyear);
		info_hometown = findView(R.id.info_hometown);
		info_city = findView(R.id.info_city);
		
		info_brithday.setOnClickListener(this);
		info_workyear.setOnClickListener(this);
		
		info_hometown.setOnClickListener(this);
		info_city.setOnClickListener(this);
		
		info_maritalstatus.setOnClickListener(this);
		info_politicalstatus.setOnClickListener(this);
		
		info_realname.addTextChangedListener(this);
		info_phone.addTextChangedListener(this);
		info_email.addTextChangedListener(this);
		info_nationality.addTextChangedListener(this);
		info_license.addTextChangedListener(this);
	}
	
	private void initData() {
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			setAddBtnSrc(R.drawable.ic_btn_edit);
			
			kId = commMapArray.get("userId")[0];
			
			info_realname.setText(commMapArray.get("realname")[0]);
			info_phone.setText(commMapArray.get("phone")[0]);
			info_email.setText(commMapArray.get("email")[0]);
			info_nationality.setText(commMapArray.get("nationality")[0]);
			info_license.setText(commMapArray.get("license")[0]);
			
			String gender = commMapArray.get("gender")[0];
			if(gender.equals("0")){
				radioman.setChecked(true);
			}else{
				radiowoman.setChecked(true);
			}
			
			// 海外工作经验
			String workingabroad = commMapArray.get("workingabroad")[0];
			if(workingabroad.equals("0")){
				radio_yes.setChecked(true);
			}else{
				radio_no.setChecked(true);
			}
			
			info_brithday.setText(commMapArray.get("brithday")[0]);
			info_workyear.setText(commMapArray.get("joinworktime")[0]);
			info_hometown.setText(commMapArray.get("hometown")[0]);
			info_city.setText(commMapArray.get("city")[0]);
			String ismarry = commMapArray.get("ismarry")[0];
			if(ismarry.equals("1")){
				info_maritalstatus.setText(CommUtil.getStrValue(self, R.string.info_maritalstatus_2));
			}else if(ismarry.equals("2")){
				info_maritalstatus.setText(CommUtil.getStrValue(self, R.string.info_maritalstatus_3));
			}else{
				info_maritalstatus.setText(CommUtil.getStrValue(self, R.string.info_maritalstatus_1));
			}
			
			String politicalstatus = commMapArray.get("politicalstatus")[0];
			if(politicalstatus.equals("1")){
				info_politicalstatus.setText(CommUtil.getStrValue(self, R.string.info_politicalstatus_2));
			}else if(politicalstatus.equals("2")){
				info_politicalstatus.setText(CommUtil.getStrValue(self, R.string.info_politicalstatus_3));
			}else if(politicalstatus.equals("3")){
				info_politicalstatus.setText(CommUtil.getStrValue(self, R.string.info_politicalstatus_4));
			}else if(politicalstatus.equals("4")){
				info_politicalstatus.setText(CommUtil.getStrValue(self, R.string.info_politicalstatus_5));
			}else{
				info_politicalstatus.setText(CommUtil.getStrValue(self, R.string.info_politicalstatus_1));
			}
		}else{
			setAddBtnSrc(R.drawable.ic_btn_add);
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
	
	String info_realnameStr,info_phoneStr,info_emailStr,info_nationalityStr,info_licenseStr;
	
	String info_brithdayStr,info_workyearStr,info_hometownStr,info_cityStr;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			getFeildValue();
			
			if (!RegexUtil.checkNotNull(info_realnameStr)) {
				setMsg(R.string.info_name);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_brithdayStr)) {
				setMsg(R.string.info_brithday);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_workyearStr)) {
				setMsg(R.string.info_workyear);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_phoneStr)) {
				setMsg(R.string.info_contack);
				return;
			}
			
			if (!RegexUtil.isPhone(info_phoneStr)) {
				setMsg(R.string.reg_info_phone);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_emailStr)) {
				setMsg(R.string.info_email);
				return;
			}
			
			if (!RegexUtil.checkEmail(info_emailStr)) {
				set2Msg(R.string.reg_info_email);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_hometownStr)) {
				setMsg(R.string.info_hometown);
				return;
			}
		
			if (!RegexUtil.checkNotNull(info_cityStr)) {
				setMsg(R.string.info_city);
				return;
			}
			
			queryWhere = "select * from " + CommonText.BASEINFO + " where userId = 1";
			commMapArray = dbUtil.queryData(self, queryWhere);
			if (commMapArray!= null && commMapArray.get("userId").length > 0) {
				
				String edId = commMapArray.get("id")[0];
				updResult = dbUtil.updateData(self, CommonText.BASEINFO, 
						new String[]{edId,"realname","gender","brithday","joinworktime",
										  "phone","hometown","city","email","ismarry",
										  "nationality","license","workingabroad","politicalstatus"}, 
						new String[]{"1",info_realnameStr,rg_genderStr,info_brithdayStr,info_workyearStr,
										info_phoneStr,info_hometownStr,info_cityStr,info_emailStr,rg_maritalstatusStr,
										info_nationalityStr,info_licenseStr,rg_workingabroadStr,rg_politicalstatusStr});
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
				}else{
					toastMsg(R.string.action_update_fail);
				}
			}else{
				ContentValues cValues = new ContentValues();
				cValues.put("userId", "1");
				cValues.put("realname", info_realnameStr);
				cValues.put("gender", rg_genderStr);
				cValues.put("brithday", info_brithdayStr);
				cValues.put("joinworktime", info_workyearStr);
				cValues.put("phone", info_phoneStr);
				cValues.put("hometown", info_hometownStr);
				cValues.put("city", info_cityStr);
				cValues.put("email", info_emailStr);
				cValues.put("ismarry", rg_maritalstatusStr);
				cValues.put("nationality", info_nationalityStr);
				cValues.put("license", info_licenseStr);
				cValues.put("workingabroad", rg_workingabroadStr);
				cValues.put("politicalstatus", rg_politicalstatusStr);
				
				queryResult = dbUtil.insertData(self, 
						CommonText.BASEINFO, cValues);
				if (queryResult) {
					setAddBtnSrc(R.drawable.ic_btn_edit);
				}
			}
			break;
		case R.id.info_brithday:
			msg.setVisibility(View.GONE);
			DialogUtils.showTimeChooseDialog(self, info_brithday,R.string.info_brithday, 12,mHandler);
			break;
		case R.id.info_workyear:
			msg.setVisibility(View.GONE);
			DialogUtils.showTimeChooseDialog(self, info_workyear,R.string.info_workyear,13,mHandler);
			break;
		case R.id.info_hometown:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAME + ".ui.AddressActivity", false, Constants.BI_REQUEST_CODE2);
			break;
		case R.id.info_city:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAME + ".ui.AddressActivity", false, Constants.BI_REQUEST_CODE);
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
			goActivity(".ui.WorkExperienceActivity");
			break;
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.right_icon:
			goActivity(".MainActivity");
			break;
		case R.id.right_icon_more:
			DialogUtils.showTopMenuDialog(self, topLayout, mHandler);
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
	
	private void goActivity(String src){
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			startActivity(src,false);
		}else{
			set2Msg(R.string.action_baseinfo_null);
		}
	}
	
	/**
	 * 
	 * @Description: 获取界面字段值
	 * @author Comsys-WH1510032
	 */
	private void getFeildValue(){
		info_realnameStr = CommUtil.getEditTextValue(info_realname);
		info_phoneStr = CommUtil.getEditTextValue(info_phone);
		info_emailStr = CommUtil.getEditTextValue(info_email);
		info_nationalityStr = CommUtil.getEditTextValue(info_nationality);
		info_licenseStr = CommUtil.getEditTextValue(info_license);

		info_brithdayStr = CommUtil.getTextValue(info_brithday);
		info_workyearStr = CommUtil.getTextValue(info_workyear);
		info_hometownStr = CommUtil.getTextValue(info_hometown);
		info_cityStr = CommUtil.getTextValue(info_city);
	}
	
	/**
	 * 
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void syncData(){ 
		getFeildValue();
		
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_baId");
		params.add("p_userId");
		params.add("p_realname");
		params.add("p_gender");
		params.add("p_brithday");
		params.add("p_joinworktime");
		params.add("p_phone");
		params.add("p_hometown");
		params.add("p_city");
		params.add("p_email");
		params.add("p_ismarry");
		params.add("p_nationality");
		params.add("p_license");
		params.add("p_workingabroad");
		params.add("p_politicalstatus");
		params.add("p_background");
		
		values.add("1");
		values.add("3");
		values.add(info_realnameStr);
		values.add(rg_genderStr);
		values.add(info_brithdayStr);
		values.add(info_workyearStr);
		values.add(info_phoneStr);
		values.add(info_hometownStr);
		values.add(info_cityStr);
		values.add(info_emailStr);
		values.add(rg_maritalstatusStr);
		values.add(info_nationalityStr);
		values.add(info_licenseStr);
		values.add(rg_workingabroadStr);
		values.add(rg_politicalstatusStr);
		values.add(getCheckColor(checkColor));
		
		requestData("pro_baseinfo", 3, params, values, new HandlerData() {
			@Override
			public void error() {
				runOnUiThread(R.string.action_sync_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("msg").get(0).equals("200")) {
						runOnUiThread(R.string.action_sync_success);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.BI_REQUEST_CODE){
        	if(resultCode == Constants.RESULT_CODE) {
                String city = data.getStringExtra("city");
                info_city.setText(city);
            }
        }else if(requestCode == Constants.BI_REQUEST_CODE2){
        	if(resultCode == Constants.RESULT_CODE) {
                String city = data.getStringExtra("city");
                info_hometown.setText(city);
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
}
