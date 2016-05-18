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

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.model.UUIDGenerator;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: EvaluationActivity 
* @Description: 自我评价 
* @date 2016/3/30 下午5:23:13 
*
 */
public class EvaluationActivity extends BaseActivity implements OnClickListener{
	
	// 自我评价;职业目标
	private EditText info_self_evaluation,info_career_goal;
	
	private String info_self_evaluationStr,info_career_goalStr;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.EVALUATION, 
							new String[]{"userId=?","background"}, 
							new String[]{uTokenId,String.valueOf(checkColor)},1);
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
		View v = View.inflate(self,R.layout.activity_evaluation_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.resume_evaluation);
		setMsgHide();
		setRight2IconVisible(View.VISIBLE);
		
		setfabLayoutVisible(View.VISIBLE);
		setEditBtnVisible(View.GONE);
		
		findViews();
		getEvData();
		initViews();
		
	}
	
	private void findViews(){
		info_self_evaluation = findView(R.id.info_self_evaluation);
		info_career_goal = findView(R.id.info_career_goal);
		
		info_self_evaluation.addTextChangedListener(this);
		info_career_goal.addTextChangedListener(this);
	}
	
	private boolean getEvData(){
		queryWhere = "select * from " + CommonText.EVALUATION + " where userId = '" + uTokenId + "' order by id desc limit 1";
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
	
	private void initViews(){
		if(getEvData()){
			info_self_evaluation.setText(commMapArray.get("selfevaluation")[0]);
			info_career_goal.setText(commMapArray.get("careergoal")[0]);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.save:
			getFeildValue();
			if(judgeFeild()){
				if (getEvData()) {
					actionFlag = 2;
					updResult = dbUtil.updateData(self, CommonText.EVALUATION,
							new String[] { tokenId, "selfevaluation","careergoal","updatetime" }, 
							new String[] { uTokenId,info_self_evaluationStr,info_career_goalStr,TimeUtils.getCurrentTimeInString() }, 3);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						actionAync();
					}
				} else {
					actionFlag = 1;
					ContentValues cValues = new ContentValues();
					cValues.put("tokenId", UUIDGenerator.getKUUID());
					cValues.put("userId", uTokenId);
					cValues.put("selfevaluation", info_self_evaluationStr);
					cValues.put("careergoal", info_career_goalStr);
					cValues.put("createtime",TimeUtils.getCurrentTimeInString());
					queryResult = dbUtil.insertData(self,CommonText.EVALUATION, cValues);
					if (queryResult) {
						if (getEvData()) {
							actionAync();
						}
					}
				}
			}
			break;
		case R.id.next:
			startActivity(".ui.JobIntensionActivity",false);
			break;
		case R.id.right_icon_more:
			DialogUtils.showTopMenuDialog(self, topLayout,0, mHandler);
			break;
		default:
			break;
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
	
	
	/**
	 * 
	 * @Title:EvaluationActivity
	 * @Description: 获取字段值
	 */
	private void getFeildValue(){
		info_self_evaluationStr = CommUtil.getEditTextValue(info_self_evaluation);
		info_career_goalStr = CommUtil.getEditTextValue(info_career_goal);
	}
	
	/**
	 * 
	 * @Title:EvaluationActivity
	 * @Description: 字段输入限制
	 */
	private boolean judgeFeild(){
		if (!RegexUtil.checkNotNull(info_self_evaluationStr)) {
			setMsg(R.string.ev_info_self_evaluation);
			return false;
		}
		
		if (!RegexUtil.checkStringLength(info_self_evaluationStr, 1000)) {
			set3Msg(R.string.ev_info_self_evaluation_length);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_career_goalStr)) {
			setMsg(R.string.ev_info_career_goal);
			return false;
		}
		
		if (!RegexUtil.checkStringLength(info_career_goalStr, 1000)) {
			set3Msg(R.string.ev_info_career_goal_length);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void syncData(final int style){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_tokenId");
		params.add("p_userId");
		values.add(tokenId);
		values.add(uTokenId);
		
		requestData("pro_get_evaluation", style, params, values, new HandlerData() {
			@Override
			public void error() {
				if (style == 1) {
					syncRun("0",2);
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
							syncRun("0",2);
						}	
					}else{
						// 更新本地数据
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
		queryWhere = "select * from " + CommonText.EVALUATION + " where userId = '" + uTokenId + "' limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			info_self_evaluationStr = map.get("selfevaluation").get(0);;
			info_career_goalStr = map.get("careergoal").get(0);
			
			updResult = dbUtil.updateData(self, CommonText.EVALUATION,
					new String[] { tokenId, "selfevaluation","careergoal","updatetime" }, 
					new String[] { uTokenId,info_self_evaluationStr,info_career_goalStr,TimeUtils.getCurrentTimeInString() }, 3);
		}else{
			int size = map.get("userId").size();
			for (int i = 0; i < size; i++) {
				ContentValues cValues = new ContentValues();
				cValues.put("tokenId", map.get("tokenId").get(i));
				cValues.put("userId", map.get("userId").get(i));
				cValues.put("selfevaluation", map.get("selfevaluation").get(i));
				cValues.put("careergoal", map.get("careergoal").get(i));
				cValues.put("createtime", map.get("createtime").get(i));
				cValues.put("updatetime", map.get("updatetime").get(i));
				queryResult = dbUtil.insertData(self, CommonText.EVALUATION, cValues);
			}
		}
		
		if (updResult == 1 || queryResult) {
			set3Msg(R.string.action_sync_success);
			initViews();
		}
	}
	
	
	/**
	 * 
	 * @Title:WorkExperienceActivity
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void syncRun(String weId,int style){ 
		getFeildValue();
		
		if(judgeFeild()){
			List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			params.add("p_tokenId");
			params.add("p_userId");
			params.add("p_selfevaluation");
			params.add("p_careergoal");
			params.add("p_bgcolor");
			
			values.add(tokenId);
			values.add(uTokenId);
			values.add(info_self_evaluationStr);
			values.add(info_career_goalStr);
			values.add(getCheckColor(checkColor));
			
			requestData("pro_set_evaluation", 2, params, values, new HandlerData() {
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
						runOnUiThread(R.string.action_sync_fail);
					}
				}
			});
		}
	}
}
