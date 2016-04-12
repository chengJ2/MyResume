package com.me.resume.ui;

import java.util.Map;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.L;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.CustomFAB;
import com.whjz.android.text.CommonText;
import com.whjz.android.util.common.CommonUtil;

/**
 * 
* @ClassName: EvaluationActivity 
* @Description: 自我评价 
* @author Comsys-WH1510032 
* @date 2016/3/30 下午5:23:13 
*
 */
public class EvaluationActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	// 自我评价;职业目标
	private EditText info_self_evaluation,info_career_goal;
	
	private CustomFAB save_edit,next;
	
	private Map<String, String[]> map ;
	private String evaluation = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation_layout);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		
		toptext.setText(CommUtil.getStrValue(EvaluationActivity.this, R.string.resume_evaluation));
		rightLable.setText(CommUtil.getStrValue(EvaluationActivity.this, R.string.review_resume));
		
		info_self_evaluation = findView(R.id.info_self_evaluation);
		info_career_goal = findView(R.id.info_career_goal);
		
		save_edit = findView(R.id.save_edit);
		save_edit.setOnClickListener(this);
		
		next = findView(R.id.next);
		next.setOnClickListener(this);
		
		evaluation = "select * from " + CommonText.WORKEXPERIENCE + " where userId = 1";
		map = dbUtil.queryData(EvaluationActivity.this, evaluation);
		if (map!= null && map.get("userId").length > 0) {
			save_edit.setImageResource(R.drawable.ic_btn_edit);
		}else{
			save_edit.setImageResource(R.drawable.ic_btn_add);
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save_edit:
			if (map!= null && map.get("userId").length > 0) {
				info_self_evaluation.setText(map.get("selfevaluation")[0]);
				info_career_goal.setText(map.get("careergoal")[0]);
			}else{
				String info_self_evaluationStr = CommUtil.getEditTextValue(info_self_evaluation);
				String info_career_goalStr = CommUtil.getEditTextValue(info_career_goal);
				ContentValues cValues = new ContentValues();
				cValues.put("userId", "1");
				cValues.put("selfevaluation", info_self_evaluationStr);
				cValues.put("careergoal", info_career_goalStr);
				cValues.put("createtime", TimeUtils.getCurrentTimeInString());
				
				boolean addEvalutaion = dbUtil.insertData(EvaluationActivity.this, 
						CommonText.EVALUATION, cValues);
				L.d("==addEvalutaionActivity=="+addEvalutaion);
			}
			break;
		case R.id.next:
			ActivityUtils.startActivity(EvaluationActivity.this, MyApplication.PACKAGENAME
					+ ".ui.EducationActivity");
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(EvaluationActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
	}
}
