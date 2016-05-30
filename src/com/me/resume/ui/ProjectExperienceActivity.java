package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.tools.UUIDGenerator;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.whjz.android.text.CommonText;

/**
 * @Description: 项目经验
 * @date 2016/4/25 下午4:25:50 
 */
public class ProjectExperienceActivity extends BaseActivity implements OnClickListener{

	private EditText info_projectname,info_workduties,input_workdesc;
	
	private TextView info_startworktime,info_endworktime;
	
	private String info_projectnameStr,info_workdutiesStr,input_workdescStr;
	private String info_startworktimeStr,info_endworktimeStr;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				if (msg.obj != null) {
					info_startworktime.setText((String)msg.obj);
				}
				break;
			case 12:
				if (msg.obj != null) {
					info_endworktime.setText((String)msg.obj);
				}
				break;
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.PROJECT_EXPERIENCE, 
							new String[]{"userId=?","bgcolor"}, 
							new String[]{uTokenId,getCheckColor(checkColor)},1);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						actionAync();
					}else{
						toastMsg(R.string.action_update_fail);
					}
				}
				break;
			case OnTopMenu.MSG_MENU2:
				if (msg.obj != null) {
					preferenceUtil.setPreferenceData(Constants.EDITMODE,(boolean) msg.obj);
				}
				break;
			case OnTopMenu.MSG_MENU3:
				if (judgeField()) {
					actionAync();
				}
				break;
			case OnTopMenu.MSG_MENU31:
				toastMsg(R.string.action_login_head);
				break;
			case OnTopMenu.MSG_MENU32:
				ActivityUtils.startActivityForResult(self, 
						Constants.PACKAGENAMECHILD + Constants.INFOMANAGER, 
						Constants.TYPE,CommonText.PROJECT_EXPERIENCE,
						Constants.WE_MANAGER_REQUEST_CODE);
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_projectexperience_layout, null);
		boayLayout.addView(v);
		
		findViews();
		initViews();
	}
	
	private void findViews(){
		setTopTitle(R.string.resume_workexperience);
		setMsgHide();
		setRight2IconVisible(View.VISIBLE);
		setfabLayoutVisible(View.VISIBLE);
		setEditBtnVisible(View.GONE);
		
		info_projectname = findView(R.id.info_projectname);
		info_workduties = findView(R.id.info_workduties);
		input_workdesc = findView(R.id.input_workdesc);
		
		info_startworktime = findView(R.id.info_startworktime);
		info_endworktime = findView(R.id.info_endworktime);
		
		info_startworktime.setOnClickListener(this);
		info_endworktime.setOnClickListener(this);
	}
	
	/**
	 * 初始化ui
	 */
	private void initViews(){
		if (getPEData()) {
			 setFieldValue();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean getPEData(){
		 queryWhere = "select * from " + CommonText.PROJECT_EXPERIENCE + " where userId = '"+ uTokenId +"' order by id desc limit 1";
		 commMapArray = dbUtil.queryData(self, queryWhere);
         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	 seNextBtnSrc(R.drawable.ic_btn_edit);
        	 tokenId = commMapArray.get("tokenId")[0];
        	 return true;
         }else{
        	 setNextBtnHide();
        	 return false;
         }
	}
	
	/**
	 * 
	 * @Description: 执行同步操作
	 */
	private void actionAync(){
		if (!MyApplication.USERID.equals("0")) {
			if (CommUtil.isNetworkAvailable(self)) {
				set3Msg(R.string.action_syncing,Constants.DEFAULTIME);
				syncData();
			} else {
				set3Msg(R.string.check_network);
			}
		} else {
			set3Msg(R.string.action_login_head);
		}
	}
	
	/**
	 * 判断字段值
	 */
	private boolean judgeField(){
		if (!RegexUtil.checkNotNull(info_projectnameStr)) {
			setMsg(R.string.pe_info_projectname);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_startworktimeStr)) {
			setMsg(R.string.we_info_start_worktime);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_endworktimeStr)) {
			setMsg(R.string.we_info_end_worktime);
			return false;
		}
		
		if (TimeUtils.compareDate(info_startworktimeStr, info_endworktimeStr) <= 0) {
			set3Msg(R.string.we_info_compare_worktime);
			return false;
		}
		return true;
	}
	
	/**
	 * 获得字段值
	 */
	private void getFieldValue(){
		info_projectnameStr = CommUtil.getEditTextValue(info_projectname);
		info_workdutiesStr = CommUtil.getEditTextValue(info_workduties);
		input_workdescStr = CommUtil.getEditTextValue(input_workdesc);
		info_startworktimeStr = CommUtil.getTextValue(info_startworktime);
		info_endworktimeStr = CommUtil.getTextValue(info_endworktime);
	}
	
	/**
	 * 设置字段值
	 */
	private void setFieldValue(){
		info_projectname.setText(commMapArray.get("projectname")[0]);
   	 	info_startworktime.setText(commMapArray.get("worktimestart")[0]);
   	 	info_endworktime.setText(commMapArray.get("worktimeend")[0]);
   	 	info_workduties.setText(commMapArray.get("duties")[0]);
   	 	input_workdesc.setText(commMapArray.get("prokectdesc")[0]);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.save:
			getFieldValue();
			if (judgeField()) {
				preferenceUtil.setPreferenceData(UserInfoCode.RESUMEUPDTIME, TimeUtils.getCurrentTimeString());
				ContentValues cValues = new ContentValues();
				cValues.put("tokenId", UUIDGenerator.getKUUID());
				cValues.put("userId", uTokenId);
				cValues.put("projectname", info_projectnameStr);
				cValues.put("worktimestart", info_startworktimeStr);
				cValues.put("worktimeend", info_endworktimeStr);
				cValues.put("duties", info_workdutiesStr);
				cValues.put("prokectdesc", input_workdescStr);
				cValues.put("createtime", TimeUtils.getCurrentTimeInString());
				queryResult = dbUtil.insertData(self, CommonText.PROJECT_EXPERIENCE, cValues);
				if(queryResult){
					toastMsg(R.string.action_add_success);
					seNextBtnSrc(R.drawable.ic_btn_edit);
					if (getPEData()) {
						actionAync();
					}
				}
			}
			break;
		case R.id.edit:
			break;
		case R.id.next:
			getFieldValue();
			if (judgeField()) {
				if (getPEData()) {
					updResult = dbUtil.updateData(self, CommonText.PROJECT_EXPERIENCE, 
							new String[]{tokenId,"projectname","worktimestart","worktimeend","duties","prokectdesc","updatetime"}, 
							new String[]{uTokenId,info_projectnameStr,info_startworktimeStr,info_endworktimeStr,info_workdutiesStr,input_workdescStr,TimeUtils.getCurrentTimeInString()},3);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						actionAync();
					}else{
						toastMsg(R.string.action_update_fail);
					}
				}
			}
			break;
		case R.id.info_startworktime:
			DialogUtils.showTimeChooseDialog(self, info_startworktime,R.string.we_info_start_worktime, 11,mHandler);
			break;
		case R.id.info_endworktime:
			DialogUtils.showTimeChooseDialog(self, info_endworktime,R.string.we_info_end_worktime, 12,mHandler);
			break;
		case R.id.right_icon_more:
			DialogUtils.showTopMenuDialog(self, topLayout,1, mHandler);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title:WorkExperienceActivity
	 * @Description: 同步数据
	 */
	private void syncData(){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_tokenId");
		params.add("p_userId");
		values.add(tokenId);
		values.add(uTokenId);
		
		requestData("pro_get_projectexpericnce", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				syncRun(tokenId,2);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					String p_tokenId = map.get("tokenId").get(0);
					if (map.get("userId").get(0).equals(uTokenId)) {
						syncRun(p_tokenId,3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 
	 * @Title:WorkExperienceActivity
	 * @Description: 同步数据
	 */
	private void syncRun(String tokenId,int style){ 
		getFieldValue();
		
		if(judgeField()){
			List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			params.add("p_tokenId");
			params.add("p_userId");
			params.add("p_projectname");
			params.add("p_worktimestart");
			params.add("p_worktimeend");
			params.add("p_duties");
			params.add("p_prokectdesc");
			params.add("p_bgcolor");
			
			values.add(tokenId);
			values.add(uTokenId);
			values.add(info_projectnameStr);
			values.add(info_startworktimeStr);
			values.add(info_endworktimeStr);
			values.add(info_workdutiesStr);
			values.add(input_workdescStr);
			values.add(getCheckColor(checkColor));
			
			requestData("pro_set_projectexpericnce", style, params, values, new HandlerData() {
				@Override
				public void error() {
					runOnUiThread(R.string.action_sync_fail);
				}
				
				public void success(Map<String, List<String>> map) {
					try {
						if (map.get("msg").get(0).equals(ResponseCode.RESULT_OK)) {
							runOnUiThread(R.string.action_sync_success);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
		}
	}
}
