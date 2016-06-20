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
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.BaseActivity;
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
	
	private String info_self_evaluationStr,info_career_goalStr,info_characterStr;
	
	private TextView info_character;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.EVALUATION, 
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
				set3Msg(R.string.action_syncing,Constants.DEFAULTIME);
				if (actionFlag == 0) {
					syncData(3);
				}else{
					actionAync();
				}
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
		getCharacter();
		setFeildValue();
	}
	
	private void findViews(){
		info_self_evaluation = findView(R.id.info_self_evaluation);
		info_career_goal = findView(R.id.info_career_goal);
		info_character = findView(R.id.info_character);
		
		info_self_evaluation.addTextChangedListener(this);
		info_career_goal.addTextChangedListener(this);
		info_character.setOnClickListener(this);
	}
	
	/**
	 * 获得自我简评数据
	 */
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
	
	/**
	 * 获得性格标签数据
	 */
	private void getCharacter() {
		queryWhere = "select * from " + CommonText.CHARACTER + " where userId = '"+ uTokenId+"'";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null) {
			int count = commMapArray.get("userId").length;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < count; i++) {
				sb.append(commMapArray.get("character")[i]).append(";");
			}
			
			info_character.setText(CommUtil.getStringLable(sb.toString()));
		}
	}
	
	private void setFeildValue(){
		if(getEvData()){
			info_self_evaluation.setText(commMapArray.get("selfevaluation")[0]);
			info_career_goal.setText(commMapArray.get("careergoal")[0]);
			info_character.setText(commMapArray.get("character")[0]);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.save:
			getFeildValue();
			if(judgeField()){
				if (getEvData()) {
					actionFlag = 2;
					updResult = dbUtil.updateData(self, CommonText.EVALUATION,
							new String[] { tokenId, "selfevaluation","careergoal","character","updatetime" }, 
							new String[] { uTokenId,info_self_evaluationStr,info_career_goalStr,info_characterStr,TimeUtils.getCurrentTimeInString() }, 3);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						actionAync();
					}
				} else {
					actionFlag = 1;
					ContentValues cValues = new ContentValues();
					tokenId = UUIDGenerator.getKUUID();
					cValues.put("tokenId", tokenId);
					cValues.put("userId", uTokenId);
					cValues.put("selfevaluation", info_self_evaluationStr);
					cValues.put("bgcolor", getCheckColor(checkColor));
					cValues.put("careergoal", info_career_goalStr);
					cValues.put("character", info_characterStr);
					cValues.put("createtime",TimeUtils.getCurrentTimeInString());
					queryResult = dbUtil.insertData(self,CommonText.EVALUATION, cValues);
					if (queryResult) {
						toastMsg(R.string.action_add_success);
						actionAync();
					}
				}
			}
			break;
		case R.id.next:
			startChildActivity(Constants.JOBINTENSION,false);
			break;
		case R.id.right_icon_more:
			DialogUtils.showTopMenuDialog(self, topLayout,0, mHandler);
			break;
		case R.id.info_character:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAMECHILD + Constants.EVALUATIONMORE, false, Constants.EV_REQUEST_CODE);
			break;
		default:
			break;
		}
	}
	
	/**
	 * @Description: 执行同步操作
	 */
	private void actionAync(){
		syncData(1);
	}
	
	
	/**
	 * 
	 * @Title:EvaluationActivity
	 * @Description: 获取字段值
	 */
	private void getFeildValue(){
		info_self_evaluationStr = getEditTextValue(info_self_evaluation);
		info_career_goalStr = getEditTextValue(info_career_goal);
		info_characterStr = getTextValue(info_character);
	}
	
	/**
	 * 
	 * @Title:EvaluationActivity
	 * @Description: 字段输入限制
	 */
	private boolean judgeField(){
		if (!RegexUtil.checkNotNull(info_self_evaluationStr)) {
			setMsg(R.string.ev_info_self_evaluation);
			return false;
		}
		
		if (!RegexUtil.checkStringLength(info_self_evaluationStr, 1000)) {
			set3Msg(R.string.ev_info_self_evaluation_length);
			return false;
		}
		
		/*if (!RegexUtil.checkNotNull(info_career_goalStr)) {
			setMsg(R.string.ev_info_career_goal);
			return false;
		}*/
		
		if (!RegexUtil.checkStringLength(info_career_goalStr, 1000)) {
			set3Msg(R.string.ev_info_career_goal_length);
			return false;
		}
		return true;
	}
	
	/**
	 * @Description: 同步数据
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
						// 更新本地数据
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
					set3Msg(R.string.action_sync_success);
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
			info_self_evaluationStr = getServerKeyValue(map,"selfevaluation",0);
			info_career_goalStr = getServerKeyValue(map,"careergoal",0);
			info_characterStr = getServerKeyValue(map,"character",0);
			
			updResult = dbUtil.updateData(self, CommonText.EVALUATION,
					new String[] { tokenId, "selfevaluation","careergoal","character","updatetime" }, 
					new String[] { uTokenId,info_self_evaluationStr,info_career_goalStr,info_characterStr,TimeUtils.getCurrentTimeInString() }, 3);
		}else{
			int size = map.get("userId").size();
			for (int i = 0; i < size; i++) {
				ContentValues cValues = new ContentValues();
				cValues.put("tokenId", map.get("tokenId").get(i));
				cValues.put("userId", map.get("userId").get(i));
				cValues.put("selfevaluation", getServerKeyValue(map,"selfevaluation",i));
				cValues.put("careergoal", getServerKeyValue(map,"careergoal",i));
				cValues.put("character",getServerKeyValue(map,"character",i));
				cValues.put("bgcolor", getServerKeyValue(map,"bgcolor",i));
				cValues.put("createtime",getServerKeyValue(map,"createtime",i));
				cValues.put("updatetime", getServerKeyValue(map,"updatetime",i));
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
	 * @Title:WorkExperienceActivity
	 * @Description: 同步数据
	 */
	private void syncRun(String tokenId,int style){ 
		getFeildValue();
		
		if(judgeField()){
			List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			params.add("p_tokenId");
			params.add("p_userId");
			params.add("p_selfevaluation");
			params.add("p_careergoal");
			params.add("p_character");
			params.add("p_bgcolor");
			
			values.add(tokenId);
			values.add(uTokenId);
			values.add(info_self_evaluationStr);
			values.add(info_career_goalStr);
			values.add(info_characterStr);
			values.add(getCheckColor(checkColor));
			
			requestData("pro_set_evaluation", style, params, values, new HandlerData() {
				@Override
				public void error() {
					set3Msg(R.string.timeout_network);
				}
				
				public void success(Map<String, List<String>> map) {
					try {
						if (map.get(ResponseCode.MSG).get(0).equals(ResponseCode.RESULT_OK)) {
							set3Msg(R.string.action_sync_success);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Constants.EV_REQUEST_CODE){
        	if(resultCode == Constants.RESULT_CODE) {
                String character = data.getStringExtra(Constants.CHARACTER);
                info_character.setText(CommUtil.getStringLable(character));
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
}
