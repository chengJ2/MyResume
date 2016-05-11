package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.whjz.android.text.CommonText;

/**
 * 
* @Description: 项目经验
* @author chegnjian
* @date 2016/4/25 下午4:25:50 
*
 */
public class ProjectExperienceActivity extends BaseActivity implements OnClickListener{

	private EditText info_projectname,info_workduties,input_workdesc;
	
	private TextView info_startworktime,info_endworktime;
	
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
					updResult = dbUtil.updateData(self, CommonText.WORKEXPERIENCE, 
							new String[]{uTokenId,"background"}, 
							new String[]{"1",String.valueOf(checkColor)},2);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
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
		// TODO Auto-generated method stub
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
	}
	
	private void initViews(){
		 queryWhere = "select * from " + CommonText.PROJECT_EXPERIENCE + " where userId = '"+ uTokenId +"' order by id desc limit 1";
		 commMapArray = dbUtil.queryData(self, queryWhere);
         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	 setEditBtnVisible(View.VISIBLE);
        	 
        	 uTokenId = commMapArray.get("id")[0];
        	 
        	 info_projectname.setText(commMapArray.get("projectname")[0]);
        	 info_startworktime.setText(commMapArray.get("worktimestart")[0]);
        	 info_endworktime.setText(commMapArray.get("worktimeend")[0]);
        	 info_workduties.setText(commMapArray.get("duties")[0]);
        	 input_workdesc.setText(commMapArray.get("prokectdesc")[0]);
         }else{
        	 setEditBtnVisible(View.GONE);
         }
	}
	
	private String info_projectnameStr,info_workdutiesStr,input_workdescStr;
	private String info_startworktimeStr,info_endworktimeStr;
	
	@Override
	public void onClick(View v) {
		info_projectnameStr = CommUtil.getEditTextValue(info_projectname);
		info_workdutiesStr = CommUtil.getEditTextValue(info_workduties);
		input_workdescStr = CommUtil.getEditTextValue(input_workdesc);
		info_startworktimeStr = CommUtil.getTextValue(info_startworktime);
		info_endworktimeStr = CommUtil.getTextValue(info_endworktime);
		
		switch (v.getId()) {
		case R.id.save:
			if (!RegexUtil.checkNotNull(info_projectnameStr)) {
				setMsg(R.string.pe_info_projectname);
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
			ContentValues cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("projectname", info_projectnameStr);
			cValues.put("worktimestart", info_startworktimeStr);
			cValues.put("worktimeend", info_endworktimeStr);
			cValues.put("duties", info_workdutiesStr);
			cValues.put("prokectdesc", input_workdescStr);
			
			queryResult = dbUtil.insertData(self, CommonText.PROJECT_EXPERIENCE, cValues);
			if(queryResult){
				toastMsg(R.string.action_add_success);
				setEditBtnVisible(View.VISIBLE);
			}
			
			break;
		case R.id.edit:
			updResult = dbUtil.updateData(self, CommonText.WORKEXPERIENCE, 
					new String[]{uTokenId,"projectname","worktimestart","worktimeend","duties","prokectdesc"}, 
					new String[]{"1",info_projectnameStr,info_startworktimeStr,info_endworktimeStr,info_workdutiesStr,input_workdescStr},2);
			if (updResult == 1) {
				toastMsg(R.string.action_update_success);
			}else{
				toastMsg(R.string.action_update_fail);
			}
			break;
		case R.id.next:
//			startActivity(".ui.EvaluationActivity", false);
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			startActivity(".MainActivity", false);
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
	}
}
