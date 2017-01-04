package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.comm.ResponseCode;
import com.me.resume.tools.UUIDGenerator;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: JobIntensionActivity 
* @Description: 求职意向 
* @date 2016/4/7 下午4:42:34 
*
 */
public class JobIntensionActivity extends BaseActivity implements OnClickListener{
	
	// 工作性质，工作点，职业,从事行业，薪资，状态
	private TextView info_exp_workingproperty,info_expworkplace,info_expworkcareer,info_expworkindustry,info_expmonthlysalary,info_workingstate;
	
	private String info_exp_workingpropertyStr,info_expworkplaceStr,info_expworkcareerStr; 
	private String info_expworkindustryStr,info_expmonthlysalaryStr,info_workingstateStr;
	
	private TextView helpInfo;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				int position = (int) msg.obj;
				if(whichTab == 1){
					info_exp_workingproperty.setText(mList.get(position));
				}else if(whichTab == 5){
					info_workingstate.setText(mList.get(position));
				}else if(whichTab == 4){
					info_expmonthlysalary.setText(mList.get(position));
				}
				break;
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (String) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.JOBINTENSION, 
							new String[]{"userId=?","bgcolor"}, 
							new String[]{uTokenId,checkColor},1);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						if (preferenceUtil.getPreferenceData(Constants.AUTOSYNC)) {
							actionAync();
						}
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
				actionAync();
				break;
			case OnTopMenu.MSG_MENU31:
				toastMsg(R.string.action_login_head);
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
		
		View v = View.inflate(self,R.layout.activity_jobintension_layout, null);
		bodyLayout.addView(v);
		
		findViews();
		
		initViews();
	}

	private void findViews(){
		left_icon.setOnClickListener(this);
		right_icon.setOnClickListener(this);
		
		helpInfo = findView(R.id.ji_helpInfo);
		
		info_exp_workingproperty = findView(R.id.info_exp_workingproperty);
		info_expworkplace = findView(R.id.info_expworkplace);
		info_expworkindustry = findView(R.id.info_expworkindustry);
		info_expmonthlysalary = findView(R.id.info_expmonthlysalary);
		info_workingstate = findView(R.id.info_workingstate);
		info_expworkcareer = findView(R.id.info_expworkcareer);
		
		info_exp_workingproperty.setOnClickListener(this);
		info_expworkplace.setOnClickListener(this);
		info_expworkindustry.setOnClickListener(this);
		info_expmonthlysalary.setOnClickListener(this);
		info_workingstate.setOnClickListener(this);
		info_expworkcareer.setOnClickListener(this);
	}
	
	private void initViews() {
		setTopTitle(R.string.resume_jobintension);
		setMsgHide();
		setRight2IconVisible(View.VISIBLE);
		setfabLayoutVisible(View.VISIBLE);
		setEditBtnVisible(View.GONE);
		
		setFeildValue();
	}

	/**
	 * 获取UI数据
	 * @return
	 */
	private boolean getJobIntensionData(){
		 queryWhere = "select * from " + CommonText.JOBINTENSION + " where userId = '" + uTokenId + "' order by id desc limit 1";
		 commMapArray = dbUtil.queryData(self, queryWhere);
         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	 tokenId = commMapArray.get("tokenId")[0];
        	 setAddBtnSrc(R.drawable.ic_btn_edit);
        	 return true;
         }else{
        	 setAddBtnSrc(R.drawable.ic_btn_add);
        	 return false;
         }
	}
	
	/**
	 * 设置字段值
	 */
	private void setFeildValue(){
		if(getJobIntensionData()){
			 info_exp_workingproperty.setText(commMapArray.get("expworkingproperty")[0]);
	    	 info_expworkplace.setText(commMapArray.get("expdworkplace")[0]);
	    	 info_expworkindustry.setText(commMapArray.get("expworkindustry")[0]);
	    	 info_expmonthlysalary.setText(commMapArray.get("expmonthlysalary")[0]);
	    	 info_workingstate.setText(commMapArray.get("workingstate")[0]);
	    	 info_expworkcareer.setText(commMapArray.get("expworkcareer")[0]);
		}
	}
	
	/**
	 * 执行同步操作
	 */
	private void actionAync(){
		if (!MyApplication.USERID.equals("0")) {
			if (CommUtil.isNetworkAvailable(self)) {
				helpInfo.setVisibility(View.GONE);
				set3Msg(R.string.action_syncing,Constants.DEFAULTIME);
				if (actionFlag == 0) {
					syncData(3);
				}else{
					syncData(1);
				}
			}else{
				set3Msg(R.string.check_network);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.save:
			if(judgeField()){
				if(getJobIntensionData()){
					actionFlag = 2;
					updResult = dbUtil.updateData(self, CommonText.JOBINTENSION, 
							new String[]{tokenId,"expworkingproperty","expdworkplace","expworkindustry","expworkcareer",
											  "expmonthlysalary","workingstate","updatetime"}, 
							new String[]{uTokenId,info_exp_workingpropertyStr,info_expworkplaceStr,info_expworkindustryStr,
											info_expworkcareerStr,info_expmonthlysalaryStr,info_workingstateStr,TimeUtils.getCurrentTimeInString()},3);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						if (preferenceUtil.getPreferenceData(Constants.AUTOSYNC)) {
							actionAync();
						}
					}else{
						toastMsg(R.string.action_update_fail);
					}
				}else{
					actionFlag = 1;
					ContentValues cValues = new ContentValues();
					cValues.put("tokenId", UUIDGenerator.getKUUID());
					cValues.put("userId", uTokenId);
		 			cValues.put("expworkingproperty", info_exp_workingpropertyStr);
		 			cValues.put("expdworkplace", info_expworkplaceStr);
		 			cValues.put("expworkindustry", info_expworkindustryStr);
		 			cValues.put("expworkcareer", info_expworkcareerStr);
		 			cValues.put("expmonthlysalary", info_expmonthlysalaryStr);
		 			cValues.put("workingstate", info_workingstateStr);
		 			cValues.put("bgcolor", checkColor);
		 			cValues.put("createtime", TimeUtils.getCurrentTimeInString());
		 			
		 			queryResult = dbUtil.insertData(self, CommonText.JOBINTENSION, cValues);
		 			if(queryResult){
		 				toastMsg(R.string.action_add_success);
		 				if(getJobIntensionData()){
		 					if (preferenceUtil.getPreferenceData(Constants.AUTOSYNC)) {
		 						actionAync();
		 					}
		 				}
		 			}
				}
			}
			break;
		case R.id.next:
			startChildActivity(Constants.EDUCATION,false);
			break;
		case R.id.info_exp_workingproperty:
			whichTab = 1;
			getValues(R.array.ba_expectedworkingproperty_values,info_exp_workingproperty,R.string.ji_info_expectedworkingproperty,mHandler);
			break;
		case R.id.info_expworkplace:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAMECHILD + Constants.ADDRESS, false, Constants.JI_REQUEST_CODE2);
			break;
		case R.id.info_expworkindustry:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAMECHILD + Constants.INDUSTRYTYPE, false, Constants.JI_REQUEST_CODE);
			break;
		case R.id.info_expworkcareer:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAMECHILD + Constants.PROFESSION, false, Constants.JI_REQUEST_CODE3);
			break;
		case R.id.info_expmonthlysalary:
			whichTab = 4;
			getValues(R.array.ji_qwyx_values,info_expmonthlysalary,R.string.ji_info_expectedmonthlysalary,mHandler);
			break;
		case R.id.info_workingstate:
			whichTab = 5;
			getValues(R.array.ji_jobstatue_values,info_workingstate,R.string.ji_info_workingstate,mHandler);
			break;
		case R.id.right_icon_more:
			DialogUtils.showTopMenuDialog(self, topLayout,2, mHandler);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * 获取界面字段值
	 */
	private void getFieldValue(){
		 info_exp_workingpropertyStr = getTextValue(info_exp_workingproperty);
		 info_expworkplaceStr = getTextValue(info_expworkplace);
		 info_expworkcareerStr = getTextValue(info_expworkcareer);
		 info_expworkindustryStr = getTextValue(info_expworkindustry);
		 info_expmonthlysalaryStr = getTextValue(info_expmonthlysalary);
		 info_workingstateStr = getTextValue(info_workingstate);
	}
	
	/**
	 * 判断字段值
	 */
	private boolean judgeField(){
		getFieldValue();
		if (!RegexUtil.checkNotNull(info_exp_workingpropertyStr)) {
			setMsg(R.string.ji_info_expectedworkingproperty);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_expworkplaceStr)) {
			setMsg(R.string.ji_info_expectedworkplace);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_expworkcareerStr)) {
			setMsg(R.string.ji_info_expectedworkcareer);
			return false;
		}
		
//		if (!RegexUtil.checkNotNull(info_expworkindustryStr)) {
//			setMsg(R.string.ji_info_expectedworkindustry);
//			return false;
//		}
		
//		if (!RegexUtil.checkNotNull(info_expmonthlysalaryStr)) {
//			setMsg(R.string.ji_info_expectedmonthlysalary);
//			return false;
//		}
		
//		if (!RegexUtil.checkNotNull(info_workingstateStr)) {
//			setMsg(R.string.ji_info_choose_workingstate);
//			return false;
//		}
		return true;
	}
	
	/**
	 * 
	 * @Description: 数据请求是否已同步
	 */
	private void syncData(final int style){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_tokenId");
		params.add("p_userId");
		values.add(tokenId);
		values.add(uTokenId);
		
		requestData("pro_get_jobintension", style, params, values, new HandlerData() {
			@Override
			public void error() {
				set3Msg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (style == 1) {
						tokenId = map.get("tokenId").get(0);
						if (map.get("userId").get(0).equals(uTokenId)) {
							syncRun(tokenId,3);
						}else{
							syncRun(tokenId,2);
						}
					}else{
						setDataFromServer(map);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				if (style == 1) {
					syncRun(tokenId,2);
				}else{
					if (style == 3 && judgeField()) {
						syncRun(tokenId,2);
					}
				}
			}
		});
	}
	
	/**
	 * 更新本地数据
	 * @param map
	 */
	private void setDataFromServer(Map<String, List<String>> map){
		queryWhere = "select * from " + CommonText.JOBINTENSION + " where userId = '" + uTokenId + "' limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			info_exp_workingpropertyStr = getServerKeyValue(map,"expworkingproperty");
			info_expworkplaceStr = getServerKeyValue(map,"expworkplace");
			info_expworkcareerStr = getServerKeyValue(map,"expworkcareer");; 
			info_expworkindustryStr = getServerKeyValue(map,"expworkindustry");
			info_expmonthlysalaryStr = getServerKeyValue(map,"expmonthlysalary");
			info_workingstateStr = getServerKeyValue(map,"workingstate");
			
			updResult = dbUtil.updateData(self, CommonText.JOBINTENSION, 
					new String[]{tokenId,"expworkingproperty","expdworkplace","expworkindustry","expworkcareer",
									  "expmonthlysalary","workingstate","updatetime"}, 
					new String[]{uTokenId,info_exp_workingpropertyStr,info_expworkplaceStr,info_expworkindustryStr,
									info_expworkcareerStr,info_expmonthlysalaryStr,info_workingstateStr,TimeUtils.getCurrentTimeInString()},3);
		}else{
			int size = map.get("userId").size();
			for (int i = 0; i < size; i++) {
				ContentValues cValues = new ContentValues();
				cValues.put("tokenId", map.get("tokenId").get(i));
				cValues.put("userId", map.get("userId").get(i));
				cValues.put("expworkingproperty", getServerKeyValue(map,"expworkingproperty"));
				cValues.put("expdworkplace", getServerKeyValue(map,"expworkplace"));
				cValues.put("expworkindustry", getServerKeyValue(map,"expworkindustry"));
				cValues.put("expworkcareer", getServerKeyValue(map,"expworkcareer"));
				cValues.put("expmonthlysalary", getServerKeyValue(map,"expmonthlysalary"));
				cValues.put("workingstate", getServerKeyValue(map,"workingstate"));
				cValues.put("bgcolor", getServerKeyValue(map,"bgcolor"));
				cValues.put("createtime", getServerKeyValue(map,"createtime"));
				cValues.put("updatetime", getServerKeyValue(map,"updatetime"));
				queryResult = dbUtil.insertData(self, CommonText.JOBINTENSION, cValues);
			}
		}
		
		if (updResult == 1 || queryResult) {
			set3Msg(R.string.action_sync_success);
			resetFeild(map);
		}else{
			set3Msg(R.string.action_sync_fail);
		}
	}
	
	/**
	 * 重新刷新UI
	 * @Title:JobIntensionActivity
	 * @param map
	 */
	private void resetFeild(Map<String, List<String>> map){
		info_exp_workingproperty.setText(getServerKeyValue(map,"expworkingproperty"));
   	 	info_expworkplace.setText(getServerKeyValue(map,"expworkplace"));
   	 	info_expworkindustry.setText(getServerKeyValue(map,"expworkindustry"));
   	 	info_expmonthlysalary.setText( getServerKeyValue(map,"expmonthlysalary"));
   	 	info_workingstate.setText(getServerKeyValue(map,"workingstate"));
   	 	info_expworkcareer.setText(getServerKeyValue(map,"expworkcareer"));
   	 	
   	 	getJobIntensionData();
	}
	
	/**
	 * 
	 * @Description: 同步数据
	 */
	private void syncRun(String tokenId,int style){ 
		if(judgeField()){
			List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			params.add("p_tokenId");
			params.add("p_userId");
			params.add("p_expworkingproperty");
			params.add("p_expdworkplace");
			params.add("p_expworkindustry");
			params.add("p_expworkcareer");
			params.add("p_expmonthlysalary");
			params.add("p_workingstate");
			params.add("p_bgcolor");
			
			values.add(tokenId);
			values.add(uTokenId);
			values.add(info_exp_workingpropertyStr);
			values.add(info_expworkplaceStr);
			values.add(info_expworkindustryStr);
			values.add(info_expworkcareerStr);
			values.add(info_expmonthlysalaryStr);
			values.add(info_workingstateStr);
			values.add(checkColor);
			
			requestData("pro_set_jobintension", style, params, values, new HandlerData() {
				@Override
				public void error() {
					
				}
				
				public void success(Map<String, List<String>> map) {
					try {
						if (map.get("msg").get(0).equals(ResponseCode.RESULT_OK)) {
							set3Msg(R.string.action_sync_success);
							preferenceUtil.setPreferenceData(Constants.SYNC_TIME, TimeUtils.getCurrentTimeString());
						}
					} catch (Exception e) {
						set3Msg(R.string.action_sync_fail);
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
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.JI_REQUEST_CODE) {
            if(resultCode == Constants.RESULT_CODE) {
                String result = data.getStringExtra(Constants.INDUSTRYTYPENAME);
                info_expworkindustry.setText(result);
            }
        }else if(requestCode == Constants.JI_REQUEST_CODE2){
        	if(resultCode == Constants.RESULT_CODE) {
                String city = data.getStringExtra(Constants.CITY);
                info_expworkplace.setText(city);
            }
        }else if(requestCode == Constants.JI_REQUEST_CODE3){
        	if(resultCode == Constants.RESULT_CODE) {
                String professionname = data.getStringExtra(Constants.PROFESSIONNAME);
                info_expworkcareer.setText(professionname);
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
