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

import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.OnTopMenu;
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
* @author Comsys-WH1510032 
* @date 2016/4/7 下午4:42:34 
*
 */
public class JobIntensionActivity extends BaseActivity implements OnClickListener{
	// 工作性质，工作点，职业,从事行业，薪资，状态
	private TextView info_exp_workingproperty,info_expworkplace,info_expworkcareer,info_expworkindustry,info_expmonthlysalary,info_workingstate;
	
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
		
		 queryWhere = "select * from " + CommonText.JOBINTENSION + " where userId = 1 limit 1";
		 commMapArray = dbUtil.queryData(self, queryWhere);
         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	 setAddBtnSrc(R.drawable.ic_btn_edit);
        	 info_exp_workingproperty.setText(commMapArray.get("expworkingproperty")[0]);
        	 info_expworkplace.setText(commMapArray.get("expdworkplace")[0]);
        	 info_expworkindustry.setText(commMapArray.get("expworkindustry")[0]);
        	 info_expmonthlysalary.setText(commMapArray.get("expmonthlysalary")[0]);
        	 info_workingstate.setText(commMapArray.get("workingstate")[0]);
        	 info_expworkcareer.setText(commMapArray.get("expworkcareer")[0]);
         }else{
        	 setAddBtnSrc(R.drawable.ic_btn_add);
         }
	}

	private String info_exp_workingpropertyStr,info_expworkplaceStr,info_expworkcareerStr; 
	private String info_expworkindustryStr,info_expmonthlysalaryStr,info_workingstateStr;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			getFeildValue();
			
			if (!RegexUtil.checkNotNull(info_exp_workingpropertyStr)) {
				setMsg(R.string.ji_info_expectedworkingproperty);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_expworkplaceStr)) {
				setMsg(R.string.ji_info_expectedworkplace);
				return;
			}
			
//			if (!RegexUtil.checkNotNull(info_expworkcareerStr)) {
//				msg.setText(CommUtil.getStrValue(self, R.string.ji_info_expectedworkcareer) + fieldNull);
//				msg.setVisibility(View.VISIBLE);
//				return;
//			}
			
			if (!RegexUtil.checkNotNull(info_expworkindustryStr)) {
				setMsg(R.string.ji_info_expectedworkindustry);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_expmonthlysalaryStr)) {
				setMsg(R.string.ji_info_expectedmonthlysalary);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_workingstateStr)) {
				setMsg(R.string.ji_info_choose_workingstate);
				return;
			}
			
			 queryWhere = "select * from " + CommonText.JOBINTENSION + " where userId = 1 limit 1";
			 commMapArray = dbUtil.queryData(self, queryWhere);
	         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
	        	String edId = commMapArray.get("id")[0];
				updResult = dbUtil.updateData(self, CommonText.JOBINTENSION, 
						new String[]{edId,"expworkingproperty","expdworkplace","expworkindustry","expworkcareer",
										  "expmonthlysalary","workingstate"}, 
						new String[]{"1",info_exp_workingpropertyStr,info_expworkplaceStr,info_expworkindustryStr,
										info_expworkcareerStr,info_expmonthlysalaryStr,info_workingstateStr});
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
				}else{
					toastMsg(R.string.action_update_fail);
				}
	         }else{
	        	ContentValues cValues = new ContentValues();
	 			cValues.put("userId", "1");
	 			cValues.put("expworkingproperty", info_exp_workingpropertyStr);
	 			cValues.put("expdworkplace", info_expworkplaceStr);
	 			cValues.put("expworkindustry", info_expworkindustryStr);
	 			cValues.put("expworkcareer", info_expworkcareerStr);
	 			cValues.put("expmonthlysalary", info_expmonthlysalaryStr);
	 			cValues.put("workingstate", info_workingstateStr);
	 			cValues.put("createtime", TimeUtils.getCurrentTimeInString());
	 			
	 			queryResult = dbUtil.insertData(self, CommonText.JOBINTENSION, cValues);
	 			if(queryResult){
	 				toastMsg(R.string.action_add_success);
	 				setAddBtnSrc(R.drawable.ic_btn_edit);
	 			}
	         }
			break;
		case R.id.next:
			startActivity(".ui.EducationActivity",false);
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			startActivity(".MainActivity",false);
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
					Constants.PACKAGENAME + ".ui.AddressActivity", false, Constants.JI_REQUEST_CODE2);
			break;
		case R.id.info_expworkindustry:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAME + ".ui.IndustryTypeActivity", false, Constants.JI_REQUEST_CODE);
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
			DialogUtils.showTopMenuDialog(self, topLayout, mHandler);
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
	
	/**
	 * 
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void syncData(){ 
		getFeildValue();
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		params.add("p_joId");
		params.add("p_userId");
		params.add("p_expworkingproperty");
		params.add("p_expdworkplace");
		params.add("p_expworkindustry");
		params.add("p_expworkcareer");
		params.add("p_expmonthlysalary");
		params.add("p_workingstate");
		params.add("p_bgcolor");
		
		values.add("0");
		values.add("1");
		values.add(info_exp_workingpropertyStr);
		values.add(info_expworkplaceStr);
		values.add(info_expworkindustryStr);
		values.add(info_expworkcareerStr);
		values.add(info_expmonthlysalaryStr);
		values.add(info_workingstateStr);
		values.add(getCheckColor(checkColor));
		
		requestData("pro_jobintension", 2, params, values, new HandlerData() {
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
