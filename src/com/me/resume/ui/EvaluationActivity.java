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

import com.me.resume.R;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.CustomFAB;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: EvaluationActivity 
* @Description: 自我评价 
* @author Comsys-WH1510032 
* @date 2016/3/30 下午5:23:13 
*
 */
public class EvaluationActivity extends BaseActivity implements OnClickListener{
	// 自我评价;职业目标
	private EditText info_self_evaluation,info_career_goal;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.EVALUATION, 
							new String[]{"userId=?","background"}, 
							new String[]{kId,String.valueOf(checkColor)},1);
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
		View v = View.inflate(self,R.layout.activity_evaluation_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.resume_evaluation);
		setMsgHide();
		setRight2IconVisible(View.VISIBLE);
		
		setfabLayoutVisible(View.VISIBLE);
		setEditBtnVisible(View.GONE);
		
		findViews();
		initData();
		
	}
	
	private void findViews(){
		info_self_evaluation = findView(R.id.info_self_evaluation);
		info_career_goal = findView(R.id.info_career_goal);
		
		info_self_evaluation.addTextChangedListener(this);
		info_career_goal.addTextChangedListener(this);
	}
	
	private void initData(){
		queryWhere = "select * from " + CommonText.EVALUATION + " where userId = 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			info_self_evaluation.setText(commMapArray.get("selfevaluation")[0]);
			info_career_goal.setText(commMapArray.get("careergoal")[0]);
			setAddBtnSrc(R.drawable.ic_btn_edit);
		}else{
			setAddBtnSrc(R.drawable.ic_btn_add);
		}
	}
	
	private String info_self_evaluationStr,info_career_goalStr;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			getFeildValue();
			queryWhere = "select * from " + CommonText.EVALUATION + " where userId = 1";
			commMapArray = dbUtil.queryData(self, queryWhere);
			if (commMapArray!= null && commMapArray.get("userId").length > 0) {
				String edId = commMapArray.get("id")[0];
				updResult = dbUtil.updateData(self, CommonText.EVALUATION, 
						new String[]{edId,"selfevaluation","careergoal"}, 
						new String[]{"1",info_self_evaluationStr,info_career_goalStr},2);
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
				}
			}else{
				ContentValues cValues = new ContentValues();
				cValues.put("userId", "1");
				cValues.put("selfevaluation", info_self_evaluationStr);
				cValues.put("careergoal", info_career_goalStr);
				cValues.put("createtime", TimeUtils.getCurrentTimeInString());
				queryResult = dbUtil.insertData(self, 
						CommonText.EVALUATION, cValues);
				if(queryResult){
					setAddBtnSrc(R.drawable.ic_btn_edit);
				}
			}
			break;
		case R.id.next:
			startActivity(".ui.JobIntensionActivity",false);
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			startActivity(".MainActivity",false);
			break;
		case R.id.right_icon_more:
			DialogUtils.showTopMenuDialog(self, topLayout, mHandler);
			break;
		default:
			break;
		}
	}
	
	private void getFeildValue(){
		info_self_evaluationStr = CommUtil.getEditTextValue(info_self_evaluation);
		info_career_goalStr = CommUtil.getEditTextValue(info_career_goal);
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
		params.add("p_evId");
		params.add("p_userId");
		params.add("p_selfevaluation");
		params.add("p_careergoal");
		params.add("p_bgcolor");
		
		values.add("0");
		values.add("1");
		values.add(info_self_evaluationStr);
		values.add(info_career_goalStr);
		values.add(getCheckColor(checkColor));
		
		
		requestData("pro_evaluation", 2, params, values, new HandlerData() {
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
