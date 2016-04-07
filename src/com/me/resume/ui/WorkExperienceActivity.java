package com.me.resume.ui;

import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.L;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.CustomFAB;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: WorkExperienceActivity 
* @Description: 工作经历 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午3:39:01 
*
 */
public class WorkExperienceActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private CustomFAB save,saveandgo;
	
	private String[] item_text = null;
	
	private TextView info_industryclassification,info_worktime,info_expectedsalary;
	
	private EditText info_companyname,info_jobtitle,info_workdescdetail;
	
	private List<String> mList = null;
	
	private int tab = 1;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				DialogUtils.dismissPopwindow();
				break;
			case 2:
				int position = (int) msg.obj;
				if(tab == 1){
					info_industryclassification.setText(mList.get(position));
				}else if(tab == 2){
					info_expectedsalary.setText(mList.get(position));
				}
				
				DialogUtils.dismissPopwindow();
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
		setContentView(R.layout.activity_workexperience_layout);
		findViews();
		initViews();
	}
	
	private void findViews(){
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		
		info_companyname = findView(R.id.info_companyname);
		info_industryclassification = findView(R.id.info_industryclassification);
		info_jobtitle = findView(R.id.info_jobtitle);
		info_worktime = findView(R.id.info_worktime);
		info_expectedsalary = findView(R.id.info_expectedsalary);
		info_workdescdetail = findView(R.id.info_workdescdetail);
		
		save = findView(R.id.save);
		saveandgo = findView(R.id.saveandgo);
		
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		save.setOnClickListener(this);
		saveandgo.setOnClickListener(this);
		leftLable.setOnClickListener(this);
		info_industryclassification.setOnClickListener(this);
		info_expectedsalary.setOnClickListener(this);
	}
	
	private void initViews(){
		rightLable.setText(CommUtil.getStrValue(WorkExperienceActivity.this, R.string.review_resume));
		toptext.setText(CommUtil.getStrValue(WorkExperienceActivity.this, R.string.resume_workexperience));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			String info_companynameStr = CommUtil.getEditTextValue(info_companyname);
			String info_jobtitleStr = CommUtil.getEditTextValue(info_jobtitle);
			String info_workdescdetailStr = CommUtil.getEditTextValue(info_workdescdetail);
			
			String info_industryclassificationStr = CommUtil.getTextValue(info_industryclassification);
			String info_worktimeStr = CommUtil.getTextValue(info_worktime);
			String info_expectedsalaryStr = CommUtil.getTextValue(info_expectedsalary);
			
			/*List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			params.add("companyname");
			params.add("industryclassification");
			params.add("jobtitle");
			params.add("worktime");
			params.add("expectedsalary");
			params.add("workdesc");
			params.add("createtime");
			
			values.add(info_companynameStr);
			values.add(info_jobtitleStr);
			values.add(info_workdescdetailStr);
			values.add(info_industryclassificationStr);
			values.add(info_worktimeStr);
			values.add(info_expectedsalaryStr);
			values.add(TimeUtils.getCurrentTimeInString());*/
		
//			String where = "delete from " + CommonText.WORKEXPERIENCE;
//			dbUtil.delectData(WorkExperienceActivity.this, where);
			
			ContentValues cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("companyname", info_companynameStr);
			cValues.put("industryclassification", info_industryclassificationStr);
			cValues.put("jobtitle", info_jobtitleStr);
			cValues.put("worktimeStart", "2014-01-12");
			cValues.put("worktimeEnd", "2015-11-12");
			cValues.put("expectedsalary", info_expectedsalaryStr);
			cValues.put("workdesc", info_workdescdetailStr);
			cValues.put("createtime", TimeUtils.getCurrentTimeInString());
			boolean addWorkExperience = dbUtil.insertData(WorkExperienceActivity.this, 
					CommonText.WORKEXPERIENCE, cValues);
			L.d("==addWorkExperience=="+addWorkExperience);
			
			/*requestData("procInserUser", 1, params, values,CommonText.WORKEXPERIENCE,where, new HandlerData() {
				@Override
				public void error() {
					CommUtil.ToastMsg(getApplicationContext(), "失败");
				}
				
				public void success(Map<String, List<String>> map) {
					try {
					} catch (Exception e) {
					}
				}
			});*/
			
			break;
		case R.id.saveandgo:
			ActivityUtils.startActivity(WorkExperienceActivity.this, MyApplication.PACKAGENAME 
					+ ".ui.EvaluationActivity");
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(WorkExperienceActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		case R.id.info_industryclassification:
			tab = 1;
			item_text = CommUtil.getArrayValue(WorkExperienceActivity.this,R.array.oi_hylb_values); 
			mList = Arrays.asList(item_text);
			DialogUtils.showPopWindow(WorkExperienceActivity.this, info_industryclassification, 
					R.string.we_info_expectedsalary, mList, 
					mHandler);
			break;
		case R.id.info_expectedsalary:
			tab = 2;
			item_text = CommUtil.getArrayValue(WorkExperienceActivity.this,R.array.we_qwyx_values); 
			mList = Arrays.asList(item_text);
			DialogUtils.showPopWindow(WorkExperienceActivity.this, info_expectedsalary, 
					R.string.we_info_expectedsalary, mList, 
					mHandler);
			
			break;
		default:
			break;
		}
		
	}
}
