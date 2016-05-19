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
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.model.UUIDGenerator;
import com.me.resume.tools.L;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
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
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				DialogUtils.dismissPopwindow();
				break;
			case 2:
				int position = (int) msg.obj;
				if(whichTab == 1){
					info_exp_workingproperty.setText(mList.get(position));
				}else if(whichTab == 5){
					info_workingstate.setText(mList.get(position));
				}else if(whichTab == 4){
					info_expmonthlysalary.setText(mList.get(position));
				}
				DialogUtils.dismissPopwindow();
				break;
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.JOBINTENSION, 
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
					preferenceUtil.setPreferenceData("edit_mode",(boolean) msg.obj);
				}
				break;
			case OnTopMenu.MSG_MENU3:
				if (actionFlag == 0) {
					syncData(3);
				}else{
					actionAync();
				}
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
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		
		View v = View.inflate(self,R.layout.activity_jobintension_layout, null);
		boayLayout.addView(v);
		
		findViews();
		
		initViews();
	}

	private void findViews(){
		left_icon.setOnClickListener(this);
		right_icon.setOnClickListener(this);
		
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
	}
	
	private void initViews() {
		setTopTitle(R.string.resume_jobintension);
		setMsgHide();
		setRight2IconVisible(View.VISIBLE);
		
		setfabLayoutVisible(View.VISIBLE);
		setEditBtnVisible(View.GONE);
		
		setFeildValue();
	}

	
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
	 * 
	 * @Title:EvaluationActivity
	 * @Description: 执行同步操作
	 */
	private void actionAync(){
		if (!MyApplication.USERID.equals("0")) {
			if (CommUtil.isNetworkAvailable(self)) {
				set2Msg(R.string.action_syncing);
				syncData(1);
			} else {
				set3Msg(R.string.check_network);
			}
		} else {
			set3Msg(R.string.action_login_head);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.save:
			getFeildValue();
			if(judgeFeild()){
				if(getJobIntensionData()){
					actionFlag = 2;
					updResult = dbUtil.updateData(self, CommonText.JOBINTENSION, 
							new String[]{tokenId,"expworkingproperty","expdworkplace","expworkindustry","expworkcareer",
											  "expmonthlysalary","workingstate","updatetime"}, 
							new String[]{uTokenId,info_exp_workingpropertyStr,info_expworkplaceStr,info_expworkindustryStr,
											info_expworkcareerStr,info_expmonthlysalaryStr,info_workingstateStr,TimeUtils.getCurrentTimeInString()},3);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						actionAync();
					}else{
						toastMsg(R.string.action_update_fail);
					}
				}else{
					actionFlag = 3;
					ContentValues cValues = new ContentValues();
					cValues.put("tokenId", UUIDGenerator.getKUUID());
					cValues.put("userId", uTokenId);
		 			cValues.put("expworkingproperty", info_exp_workingpropertyStr);
		 			cValues.put("expdworkplace", info_expworkplaceStr);
		 			cValues.put("expworkindustry", info_expworkindustryStr);
		 			cValues.put("expworkcareer", info_expworkcareerStr);
		 			cValues.put("expmonthlysalary", info_expmonthlysalaryStr);
		 			cValues.put("workingstate", info_workingstateStr);
		 			cValues.put("bgcolor", getCheckColor(checkColor));
		 			cValues.put("createtime", TimeUtils.getCurrentTimeInString());
		 			
		 			queryResult = dbUtil.insertData(self, CommonText.JOBINTENSION, cValues);
		 			if(queryResult){
		 				toastMsg(R.string.action_add_success);
		 				if(getJobIntensionData()){
		 					actionAync();
		 				}
		 			}
				}
			}
			break;
		case R.id.next:
			startChildActivity("EducationActivity",false);
			break;
		case R.id.info_exp_workingproperty:
			whichTab = 1;
			item_values = CommUtil.getArrayValue(self,R.array.ji_workingproperty_values); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, info_exp_workingproperty, 
					R.string.ji_info_expectedworkingproperty, mList, 
					mHandler);
			break;
		case R.id.info_expworkplace:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAMECHILD + "AddressActivity", false, Constants.JI_REQUEST_CODE2);
			break;
		case R.id.info_expworkindustry:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAMECHILD + "IndustryTypeActivity", false, Constants.JI_REQUEST_CODE);
			break;
		case R.id.info_expmonthlysalary:
			whichTab = 4;
			item_values = CommUtil.getArrayValue(self,R.array.we_qwyx_values); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, info_expmonthlysalary, 
					R.string.ji_info_expectedmonthlysalary, mList, 
					mHandler);
			break;
		case R.id.info_workingstate:
			whichTab = 5;
			item_values = CommUtil.getArrayValue(self,R.array.ji_jobstatue_values); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, info_workingstate, 
					R.string.ji_info_workingstate, mList, 
					mHandler);
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
	 * @Description: 获取界面字段值
	 * @author Comsys-WH1510032
	 */
	private void getFeildValue(){
		 info_exp_workingpropertyStr = CommUtil.getTextValue(info_exp_workingproperty);
		 info_expworkplaceStr = CommUtil.getTextValue(info_expworkplace);
		 info_expworkcareerStr = CommUtil.getTextValue(info_expworkcareer);
		 info_expworkindustryStr = CommUtil.getTextValue(info_expworkindustry);
		 info_expmonthlysalaryStr = CommUtil.getTextValue(info_expmonthlysalary);
		 info_workingstateStr = CommUtil.getTextValue(info_workingstate);
	}
	
	private boolean judgeFeild(){
		if (!RegexUtil.checkNotNull(info_exp_workingpropertyStr)) {
			setMsg(R.string.ji_info_expectedworkingproperty);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_expworkplaceStr)) {
			setMsg(R.string.ji_info_expectedworkplace);
			return false;
		}
		
//		if (!RegexUtil.checkNotNull(info_expworkcareerStr)) {
//			msg.setText(CommUtil.getStrValue(self, R.string.ji_info_expectedworkcareer) + fieldNull);
//			msg.setVisibility(View.VISIBLE);
//			return false;
//		}
		
		if (!RegexUtil.checkNotNull(info_expworkindustryStr)) {
			setMsg(R.string.ji_info_expectedworkindustry);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_expmonthlysalaryStr)) {
			setMsg(R.string.ji_info_expectedmonthlysalary);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_workingstateStr)) {
			setMsg(R.string.ji_info_choose_workingstate);
			return false;
		}
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
				if (style == 1) {
					syncRun(tokenId,2);
				}else{
					runOnUiThread(R.string.action_sync_success);
				}
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
			info_exp_workingpropertyStr= map.get("expworkingproperty").get(0);
			info_expworkplaceStr= map.get("expdworkplace").get(0);
			info_expworkcareerStr= map.get("expworkcareer").get(0);; 
			info_expworkindustryStr= map.get("expworkindustry").get(0);
			info_expmonthlysalaryStr= map.get("expmonthlysalary").get(0);
			info_workingstateStr= map.get("workingstate").get(0);
			
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
				cValues.put("expworkingproperty", map.get("expworkingproperty").get(i));
				cValues.put("expdworkplace", map.get("expdworkplace").get(i));
				cValues.put("expworkindustry", map.get("expworkindustry").get(i));
				cValues.put("expworkcareer", map.get("expworkcareer").get(i));
				cValues.put("expmonthlysalary", map.get("expmonthlysalary").get(i));
				cValues.put("workingstate", map.get("workingstate").get(i));
				cValues.put("bgcolor", map.get("bgcolor").get(i));
				cValues.put("createtime", map.get("createtime").get(i));
				cValues.put("updatetime", map.get("updatetime").get(i));
				queryResult = dbUtil.insertData(self, CommonText.EVALUATION, cValues);
			}
		}
		
		if (updResult == 1 || queryResult) {
			set3Msg(R.string.action_sync_success);
			setFeildValue();
		}
	}
	
	
	/**
	 * 
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void syncRun(String tokenId,int style){ 
		getFeildValue();
		
		if(judgeFeild()){
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
			values.add(getCheckColor(checkColor));
			
			requestData("pro_set_jobintension", style, params, values, new HandlerData() {
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
						runOnUiThread(R.string.action_sync_fail);
					}
				}
			});
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d("onActivityResult"+"requestCode="+requestCode+" resultCode="+resultCode + " data:"+data);
        if(requestCode == Constants.JI_REQUEST_CODE) {
            if(resultCode == Constants.RESULT_CODE) {
                String result = data.getStringExtra("name");
                info_expworkindustry.setText(result);
            }
        }else if(requestCode == Constants.JI_REQUEST_CODE2){
        	if(resultCode == Constants.RESULT_CODE) {
                String city = data.getStringExtra("city");
                info_expworkplace.setText(city);
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	
}
