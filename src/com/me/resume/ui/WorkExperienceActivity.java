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
* @ClassName: WorkExperienceActivity 
* @Description: 工作经历 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午3:39:01 
*
 */
public class WorkExperienceActivity extends BaseActivity implements OnClickListener{
	
	private TextView info_companynature,info_companyscale,info_industryclassification,
		info_startworktime,info_endworktime,info_expectedsalary;
	
	private EditText info_companyname,info_jobtitle,info_workdescdetail;
	
	private String info_companynameStr,info_jobtitleStr,info_workdescdetailStr;
	
	private String info_companynatureStr,info_companyscaleStr;
	
	private String info_industryclassificationStr,info_startworktimeStr,info_endworktimeStr,
		info_expectedsalaryStr;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				break;
			case 2:
				int position = (int) msg.obj;
				if(whichTab == 1){
					info_industryclassification.setText(mList.get(position));
				}else if(whichTab == 2){
					info_expectedsalary.setText(mList.get(position));
				}else if(whichTab == 3){
					info_companyscale.setText(mList.get(position));
				}else if(whichTab == 4){
					info_companynature.setText(mList.get(position));
				}
				break;
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
					updResult = dbUtil.updateData(self, CommonText.WORKEXPERIENCE, 
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
				if (msg.obj != null) {
					set2Msg(R.string.action_syncing);
					syncData();
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
		View v = View.inflate(self,R.layout.activity_workexperience_layout, null);
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
		
		info_companyname = findView(R.id.info_companyname);
		info_companynature = findView(R.id.info_companynature);
		info_companyscale = findView(R.id.info_companyscale);
		info_industryclassification = findView(R.id.info_industryclassification);
		info_jobtitle = findView(R.id.info_jobtitle);
		info_startworktime = findView(R.id.info_startworktime);
		info_endworktime = findView(R.id.info_endworktime);
		info_expectedsalary = findView(R.id.info_expectedsalary);
		info_workdescdetail = findView(R.id.info_workdescdetail);
		
		info_companynature.setOnClickListener(this);
		info_companyscale.setOnClickListener(this);
		info_industryclassification.setOnClickListener(this);
		info_expectedsalary.setOnClickListener(this);
		info_startworktime.setOnClickListener(this);
		info_endworktime.setOnClickListener(this);
		
		info_companyname.addTextChangedListener(this);
		info_jobtitle.addTextChangedListener(this);
		info_workdescdetail.addTextChangedListener(this);
	}
	
	private void initViews(){
		 queryWhere = "select * from " + CommonText.WORKEXPERIENCE + " where userId = 1 order by id desc limit 1";
		 commMapArray = dbUtil.queryData(self, queryWhere);
         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	 setEditBtnVisible(View.VISIBLE);
        	 
        	 kId = commMapArray.get("id")[0];
        	 
        	 info_industryclassification.setText(commMapArray.get("industryclassification")[0]);
        	 info_startworktime.setText(commMapArray.get("worktimestart")[0]);
        	 info_endworktime.setText(commMapArray.get("worktimeend")[0]);
        	 info_expectedsalary.setText(commMapArray.get("expectedsalary")[0]);
        		
        	 info_companyname.setText(commMapArray.get("companyname")[0]);
        	 info_jobtitle.setText(commMapArray.get("jobtitle")[0]);
             info_workdescdetail.setText(commMapArray.get("workdesc")[0]);
             
             info_companynature.setText(commMapArray.get("companynature")[0]);
     		 info_companyscale.setText(commMapArray.get("companyscale")[0]);
         }else{
        	 setEditBtnVisible(View.GONE);
         }
	}
	
	
	@Override
	public void onClick(View v) {
		info_companynameStr = CommUtil.getEditTextValue(info_companyname);
		info_jobtitleStr = CommUtil.getEditTextValue(info_jobtitle);
		info_workdescdetailStr = CommUtil.getEditTextValue(info_workdescdetail);

		info_companynatureStr = CommUtil.getTextValue(info_companynature);
		info_companyscaleStr = CommUtil.getTextValue(info_companyscale);

		info_industryclassificationStr = CommUtil
				.getTextValue(info_industryclassification);
		info_startworktimeStr = CommUtil.getTextValue(info_startworktime);
		info_endworktimeStr = CommUtil.getTextValue(info_endworktime);
		info_expectedsalaryStr = CommUtil.getTextValue(info_expectedsalary);
		
		switch (v.getId()) {
		case R.id.save:
			if (!RegexUtil.checkNotNull(info_companynameStr)) {
				setMsg(R.string.we_info_companyname);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_industryclassificationStr)) {
				setMsg(R.string.we_info_industryclassification);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_jobtitleStr)) {
				setMsg(R.string.we_info_jobtitle);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_startworktimeStr)) {
				setMsg(R.string.we_info_start_worktime);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_endworktimeStr)) {
				setMsg(R.string.we_info_end_worktime);
				return;
			}
			
			if (TimeUtils.compareDate(info_startworktimeStr, info_endworktimeStr) <= 0) {
				set2Msg(R.string.we_info_compare_worktime);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_expectedsalaryStr)) {
				setMsg(R.string.we_info_jobsalary);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_workdescdetailStr)) {
				setMsg(R.string.we_info_workdesc);
				return;
			}
			
			ContentValues cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("companyname", info_companynameStr);
			cValues.put("companynature", info_companynatureStr);
			cValues.put("companyscale", info_companyscaleStr);
			cValues.put("industryclassification", info_industryclassificationStr);
			cValues.put("jobtitle", info_jobtitleStr);
			cValues.put("worktimeStart", info_startworktimeStr);
			cValues.put("worktimeEnd", info_endworktimeStr);
			cValues.put("expectedsalary", info_expectedsalaryStr);
			cValues.put("workdesc", info_workdescdetailStr);
			cValues.put("background", getCheckColor());
			cValues.put("createtime", TimeUtils.getCurrentTimeInString());
			queryResult = dbUtil.insertData(self, CommonText.WORKEXPERIENCE, cValues);
			if(queryResult){
				toastMsg(R.string.action_add_success);
				setEditBtnVisible(View.VISIBLE);
			}
			break;
		case R.id.edit:
			updResult = dbUtil.updateData(self, CommonText.WORKEXPERIENCE, 
					new String[]{kId,"companyname","companynature","companyscale","industryclassification",
									  "jobtitle","worktimestart","worktimeend","expectedsalary","workdesc"}, 
					new String[]{"1",info_companynameStr,info_companynatureStr,info_companyscaleStr,info_industryclassificationStr,
								info_jobtitleStr,info_startworktimeStr,info_endworktimeStr,info_expectedsalaryStr,info_workdescdetailStr});
			if (updResult == 1) {
				toastMsg(R.string.action_update_success);
			}else{
				toastMsg(R.string.action_update_fail);
			}
			break;
		case R.id.next:
			startActivity(".ui.EvaluationActivity", false);
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			startActivity(".MainActivity", false);
			break;
		case R.id.info_companynature:
			whichTab = 4;
			getValues(R.array.oi_companynature_values,info_companynature,R.string.we_info_companynature);
			break;
		case R.id.info_companyscale:
			whichTab = 3;
			getValues(R.array.we_companyscale_values,info_industryclassification,R.string.we_info_companyscale);
			break;
		case R.id.info_industryclassification:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAME + ".ui.IndustryTypeActivity", false, Constants.WE_REQUEST_CODE);
			break;
		case R.id.info_expectedsalary:
			whichTab = 2;
			getValues(R.array.we_qwyx_values,info_expectedsalary,R.string.we_info_jobsalary);
			break;
		case R.id.info_startworktime:
			DialogUtils.showTimeChooseDialog(self, info_startworktime,R.string.we_info_start_worktime, 11,mHandler);
			break;
		case R.id.info_endworktime:
			DialogUtils.showTimeChooseDialog(self, info_endworktime,R.string.we_info_end_worktime, 12,mHandler);
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
	
	/**
	 * 
	 * @Title:WorkExperienceActivity
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void syncData(){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		params.add("p_weId");
		params.add("p_userId");
		params.add("p_companyname");
		params.add("p_industryclassification");
		params.add("p_jobtitle");
		params.add("p_worktimestart");
		params.add("p_worktimeend");
		params.add("p_expectedsalary");
		params.add("p_workdesc");
		params.add("p_companynature");
		params.add("p_companyscale");
		params.add("p_background");
		
		values.add("0");
		values.add("1");
		values.add(info_companynameStr);
		values.add(info_industryclassificationStr);
		values.add(info_jobtitleStr);
		values.add(info_startworktimeStr);
		values.add(info_endworktimeStr);
		values.add(info_expectedsalaryStr);
		values.add(info_workdescdetailStr);
		values.add(info_companynatureStr);
		values.add(info_companyscaleStr);
		values.add(getCheckColor());
		
		requestData("pro_workexpericnce", 2, params, values, new HandlerData() {
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
		L.d("onActivityResult"+"requestCode="+requestCode+" resultCode="+resultCode + " data:"+data);
        if(requestCode == Constants.WE_REQUEST_CODE) {
            if(resultCode == Constants.RESULT_CODE) {
                String result = data.getStringExtra("name");
                info_industryclassification.setText(result);
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
		
	}
}
