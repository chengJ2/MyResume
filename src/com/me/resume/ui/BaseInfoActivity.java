package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;
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
	
	private RelativeLayout expandLayoyut;
	private LinearLayout uselessLayoyut;
	private ImageView arrow_updown;
	
	private String rg_genderStr = "0";
	private String rg_maritalstatusStr = "0";
	private String rg_workingabroadStr = "0";
	private String rg_politicalstatusStr = "0";
	
	private String info_realnameStr,info_phoneStr,info_emailStr,info_nationalityStr,info_licenseStr;
	private String info_brithdayStr,info_workyearStr,info_hometownStr,info_cityStr;
	
	private boolean isShow = false;
	
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
					String year = (String)msg.obj;
					year = year.substring(0, year.lastIndexOf("-"));
					info_workyear.setText(year);
				}
				break;
			case 100:
				initData();
				break;
			case OnTopMenu.MSG_MENU1:
				if (commMapArray != null) {
					if (msg.obj != null) {
						checkColor = (String) msg.obj;
						updResult = dbUtil.updateData(self, CommonText.BASEINFO, 
								new String[]{"userId=?","bgcolor"}, 
								new String[]{uTokenId,checkColor},1);
						if (updResult == 1) {
							toastMsg(R.string.action_update_success);
							actionAync();
						}else{
							set3Msg(R.string.action_update_bgcolor_fail);
						}
					}
				}else{
					set3Msg(R.string.action_baseinfo_null);
				}
					
				break;
			case OnTopMenu.MSG_MENU2:
				if (msg.obj != null) {
					preferenceUtil.setPreferenceData(Constants.EDITMODE,(boolean) msg.obj);
				}
				break;
			case OnTopMenu.MSG_MENU3:
				actionAync();
				break;
			case OnTopMenu.MSG_MENU31:
				set3Msg(R.string.action_login_head);
				break;
			case OnTopMenu.MSG_MENU33:
				set3Msg(R.string.check_network);
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bodyLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_baseinfo_layout, null);
		bodyLayout.addView(v);
		
		findViews();
		
		getChooseValue();
		
		getBaseInfo();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
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
		
		if (RegexUtil.checkNotNull(preferenceUtil.getPreferenceData(UserInfoCode.REALNAME, ""))) {
			info_realname.setEnabled(false);
		}else{
			info_realname.setEnabled(true);
		}
		
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
		
		expandLayoyut = findView(R.id.expandLayoyut);
		uselessLayoyut = findView(R.id.uselessLayoyut);
		uselessLayoyut.setVisibility(View.GONE);
		arrow_updown = findView(R.id.arrow_updown);
		
		radio_yes  = findView(R.id.radio_yes);
		radio_yes.setChecked(false);
		radio_no = findView(R.id.radio_no);
		radio_no.setChecked(true);
		
		info_brithday = findView(R.id.info_brithday);
		info_workyear = findView(R.id.info_workyear);
		info_hometown = findView(R.id.info_hometown);
		info_city = findView(R.id.info_city);
		
		radioman.setChecked(true);
		info_maritalstatus.setText(getStrValue(R.string.info_maritalstatus_1));
		info_politicalstatus.setText(getStrValue(R.string.info_politicalstatus_1));
		
		info_brithday.setOnClickListener(this);
		info_workyear.setOnClickListener(this);
		
		info_hometown.setOnClickListener(this);
		info_city.setOnClickListener(this);
		
		info_maritalstatus.setOnClickListener(this);
		info_politicalstatus.setOnClickListener(this);
		
		expandLayoyut.setOnClickListener(this);
		arrow_updown.setOnClickListener(this);
		
		info_realname.addTextChangedListener(this);
		info_phone.addTextChangedListener(this);
		info_email.addTextChangedListener(this);
		info_nationality.addTextChangedListener(this);
		info_license.addTextChangedListener(this);
	}
	
	/**
	 * 初始化基本信息
	 * @return
	 */
	private boolean getBaseInfo(){
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = '" + uTokenId + "' order by id desc limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			localHasData = true;
			setAddBtnSrc(R.drawable.ic_btn_edit);
			return true;
		}
		return false;
	}
	
	/**
	 * 显示数据
	 */
	private void initData() {
		if (localHasData) {
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
			if(workingabroad.equals("1")){
				radio_yes.setChecked(true);
			}else{
				radio_no.setChecked(true);
			}
			
			info_brithday.setText(commMapArray.get("brithday")[0]);
			info_workyear.setText(commMapArray.get("joinworktime")[0]);
			info_hometown.setText(commMapArray.get("hometown")[0]);
			info_city.setText(commMapArray.get("city")[0]);
			
			String ismarry = commMapArray.get("ismarry")[0];
			setArrayValue(R.array.ba_maritalstatus_values,info_maritalstatus,CommUtil.parseInt(ismarry));
			
			String politicalstatus = commMapArray.get("politicalstatus")[0];
			setArrayValue(R.array.ba_politicalstatus_values,info_politicalstatus,CommUtil.parseInt(politicalstatus));
		}
		
	}

	/**
	 * 获取radiobutton值
	 */
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
		super.onClick(v);
		msg.setVisibility(View.GONE);
		switch (v.getId()) {
		case R.id.save:
			if(judgeFeild()){
				syncLocalData();
			}
			break;
		case R.id.info_brithday:
			DialogUtils.showTimeChooseDialog(self, info_brithday,R.string.info_brithday, 12,mHandler);
			break;
		case R.id.info_workyear:
			DialogUtils.showTimeChooseDialog(self, info_workyear,R.string.info_workyear,13,mHandler);
			break;
		case R.id.info_hometown:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAMECHILD + Constants.ADDRESS, false, Constants.BI_REQUEST_CODE2);
			break;
		case R.id.info_city:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAMECHILD + Constants.ADDRESS, false, Constants.BI_REQUEST_CODE);
			break;
		case R.id.info_maritalstatus:
			whichTab = 1;
			getValues(R.array.ba_maritalstatus_values,info_maritalstatus,R.string.info_maritalstatus,mHandler);
			break;
		case R.id.info_politicalstatus:
			whichTab = 2;
			getValues(R.array.ba_politicalstatus_values,info_politicalstatus,R.string.info_politicalstatus,mHandler);
			break;
		case R.id.next:
			if (RegexUtil.checkNotNull(preferenceUtil.getPreferenceData(UserInfoCode.REALNAME,""))) {
				goActivity(Constants.WORKEXPERIENCE);
			}else{
				set3Msg(R.string.action_baseinfo_null);
			}
			break;
		case R.id.right_icon_more:
			DialogUtils.showTopMenuDialog(self, topLayout,0,mHandler);
			break;
		case R.id.expandLayoyut:
		case R.id.arrow_updown:
			isShow = !isShow;
			if (isShow) {
				arrow_updown.setImageResource(R.drawable.icon_arrow_up);
				uselessLayoyut.setVisibility(View.VISIBLE);
			}else{
				arrow_updown.setImageResource(R.drawable.icon_arrow_down);
				uselessLayoyut.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 执行同步操作
	 */
	private void actionAync(){
		if (judgeFeild()) {
			if (!MyApplication.USERID.equals("0")) {
				if (CommUtil.isNetworkAvailable(self)) {
					set3Msg(R.string.action_syncing,Constants.DEFAULTIME);
					syncData();
				}else{
					set3Msg(R.string.check_network);
				}
			}
		}
	}
	
	/**
	 * 同步本地库数据
	 */
	private void syncLocalData(){
		//preferenceUtil.setPreferenceData(UserInfoCode.RESUMEUPDTIME, TimeUtils.getCurrentTimeString());
		updResult = dbUtil.updateData(self, CommonText.BASEINFO, 
				new String[]{"userId=?",
				"realname","gender","brithday","joinworktime",
				"phone","hometown","city","email","ismarry",
				"nationality","license","workingabroad","politicalstatus",
				"updatetime"}, 
				new String[]{uTokenId,
				info_realnameStr,rg_genderStr,info_brithdayStr,info_workyearStr,
				info_phoneStr,info_hometownStr,info_cityStr,info_emailStr,rg_maritalstatusStr,
				info_nationalityStr,info_licenseStr,rg_workingabroadStr,rg_politicalstatusStr,
				TimeUtils.getCurrentTimeInString()},1);
		if (updResult == 1) {
			preferenceUtil.setPreferenceData(UserInfoCode.REALNAME,info_realnameStr.trim().toString());
			toastMsg(R.string.action_update_success);
			if (!MyApplication.USERID.equals("0")) {
				if (CommUtil.isNetworkAvailable(self)) {
					if (preferenceUtil.getPreferenceData(Constants.AUTOSYNC)) {
						set3Msg(R.string.action_syncing,Constants.DEFAULTIME);
						syncData();
					}
				}else{
					set3Msg(R.string.check_network);
				}
			}
		}else{
			set3Msg(R.string.action_update_fail);
		}
	}

	/**
	 * 界面字段判断
	 */
	private boolean judgeFeild() {
		getFeildValue();
		if (!RegexUtil.checkNotNull(info_realnameStr)) {
			setMsg(R.string.info_name);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_brithdayStr)) {
			setMsg(R.string.info_brithday);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_workyearStr)) {
			setMsg(R.string.info_workyear);
			return false;
		}
		
		/*if (!RegexUtil.checkNotNull(info_phoneStr)) {
			setMsg(R.string.info_contack);
			return false;
		}*/
		
		if (RegexUtil.checkNotNull(info_phoneStr) && !RegexUtil.isPhone(info_phoneStr)) {
			set3Msg(R.string.reg_info_phone);
			return false;
		}
		
		/*if (!RegexUtil.checkNotNull(info_cityStr)) {
			setMsg(R.string.info_city);
			return false;
		}*/
		
		/*if (!RegexUtil.checkNotNull(info_emailStr)) {
			setMsg(R.string.info_email);
			return false;
		}*/
		
		if (RegexUtil.checkNotNull(info_emailStr) && !RegexUtil.checkEmail(info_emailStr)) {
			set3Msg(R.string.reg_info_email);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 设置弹出窗数据
	 * @param array
	 * @param parent
	 * @param resId
	 */
	private void setArrayValue(int array,TextView tv,int value){
		String[] item_text = CommUtil.getArrayValue(self,array); 
		tv.setText(item_text[value]);
	}
	
	/**
	 * 确认是否填写基本信息
	 * @param src
	 */
	private void goActivity(String src){
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = '" + uTokenId + "' order by id desc limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null) {
			String realname = commMapArray.get("realname")[0];
			if (RegexUtil.checkNotNull(realname)) {
				startChildActivity(src,false);
			}else{
				set3Msg(R.string.action_baseinfo_null);
			}
		}else{
			set3Msg(R.string.action_baseinfo_null);
		}
	}
	
	/**
	 * @Description: 获取界面字段值
	 */
	private void getFeildValue(){
		info_realnameStr = getEditTextValue(info_realname);
		info_phoneStr = getEditTextValue(info_phone);
		info_emailStr = getEditTextValue(info_email);
		info_nationalityStr = getEditTextValue(info_nationality);
		info_licenseStr = getEditTextValue(info_license);

		info_brithdayStr = getTextValue(info_brithday);
		info_workyearStr = getTextValue(info_workyear);
		info_hometownStr = getTextValue(info_hometown);
		info_cityStr = getTextValue(info_city);
	}
	
	/**
	 * 
	 * @Description: 同步数据(判断库是否存在记录)
	 */
	private void syncData(){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_userId");
		values.add(uTokenId);
		
		requestData("pro_get_baseinfo", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				set3Msg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("userId").get(0).equals(uTokenId)) {
						syncRun(3);
					}else{
						syncRun(2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				syncRun(2);
			}
		});
	}
	
	/**
	 * 执行同步数据请求
	 * @param style 2：add 3.update
	 */
	private void syncRun(int style){
		if(judgeFeild()){
			List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			
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
			params.add("p_bgcolor");
			
			values.add(uTokenId);
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
			values.add(checkColor);
			
			requestData("pro_set_baseinfo", style, params, values, new HandlerData() {
				@Override
				public void error() {
					set3Msg(R.string.timeout_network);
				}
				
				public void success(Map<String, List<String>> map) {
					try {
						if (map.get(ResponseCode.MSG).get(0).equals(ResponseCode.RESULT_OK)) {
							set3Msg(R.string.action_sync_success);
							preferenceUtil.setPreferenceData(Constants.SYNC_TIME, TimeUtils.getCurrentTimeString());
						}
					} catch (Exception e) {
						set3Msg(R.string.action_sync_fail);
						e.printStackTrace();
					}
				}

				@Override
				public void noData() {
					set3Msg(R.string.action_sync_fail);
				}
			});
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.BI_REQUEST_CODE){
        	if(resultCode == Constants.RESULT_CODE) {
                String city = data.getStringExtra(Constants.CITY);
                info_city.setText(city);
            }
        }else if(requestCode == Constants.BI_REQUEST_CODE2){
        	if(resultCode == Constants.RESULT_CODE) {
                String city = data.getStringExtra(Constants.CITY);
                info_hometown.setText(city);
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
}
