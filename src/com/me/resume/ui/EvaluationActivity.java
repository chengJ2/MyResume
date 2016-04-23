package com.me.resume.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
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
	
	private CustomFAB save_edit,next;
	
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
		
		left_icon.setOnClickListener(this);
		right_icon.setOnClickListener(this);
		
		info_self_evaluation = findView(R.id.info_self_evaluation);
		info_career_goal = findView(R.id.info_career_goal);
		
		save_edit = findView(R.id.save_edit);
		save_edit.setOnClickListener(this);
		
		next = findView(R.id.next);
		next.setOnClickListener(this);
		
		queryWhere = "select * from " + CommonText.EVALUATION + " where userId = 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			info_self_evaluation.setText(commMapArray.get("selfevaluation")[0]);
			info_career_goal.setText(commMapArray.get("careergoal")[0]);
			save_edit.setImageResource(R.drawable.ic_btn_edit);
		}else{
			save_edit.setImageResource(R.drawable.ic_btn_add);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save_edit:
			queryWhere = "select * from " + CommonText.EVALUATION + " where userId = 1";
			commMapArray = dbUtil.queryData(self, queryWhere);
			String info_self_evaluationStr = CommUtil.getEditTextValue(info_self_evaluation);
			String info_career_goalStr = CommUtil.getEditTextValue(info_career_goal);
			if (commMapArray!= null && commMapArray.get("userId").length > 0) {
				String edId = commMapArray.get("id")[0];
				updResult = dbUtil.updateData(self, CommonText.EVALUATION, 
						new String[]{edId,"selfevaluation","careergoal","background"}, 
						new String[]{"1",info_self_evaluationStr,info_career_goalStr,String.valueOf(getCheckColor())});
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
				}
			}else{
				ContentValues cValues = new ContentValues();
				cValues.put("userId", "1");
				cValues.put("selfevaluation", info_self_evaluationStr);
				cValues.put("careergoal", info_career_goalStr);
				cValues.put("background", getCheckColor());
				cValues.put("createtime", TimeUtils.getCurrentTimeInString());
				queryResult = dbUtil.insertData(self, 
						CommonText.EVALUATION, cValues);
				if(queryResult){
					save_edit.setImageResource(R.drawable.ic_btn_edit);
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
		default:
			break;
		}
	}
}
