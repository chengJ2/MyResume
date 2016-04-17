package com.me.resume.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.MainActivity;
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
	
	private CustomFAB save,edit,next;
	
	private TextView info_industryclassification,info_startworktime,info_endworktime,info_expectedsalary;
	
	private EditText info_companyname,info_jobtitle,info_workdescdetail;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				DialogUtils.dismissPopwindow();
				break;
			case 2:
				int position = (int) msg.obj;
				if(whichTab == 1){
					info_industryclassification.setText(mList.get(position));
				}else if(whichTab == 2){
					info_expectedsalary.setText(mList.get(position));
				}
				DialogUtils.dismissPopwindow();
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
		info_startworktime = findView(R.id.info_startworktime);
		info_endworktime = findView(R.id.info_endworktime);
		info_expectedsalary = findView(R.id.info_expectedsalary);
		info_workdescdetail = findView(R.id.info_workdescdetail);
		
		save = findView(R.id.save);
		edit = findView(R.id.edit);
		next = findView(R.id.next);
		
		 queryWhere = "select * from " + CommonText.WORKEXPERIENCE + " where userId = 1";
		 commMapArray = dbUtil.queryData(self, queryWhere);
         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	 edit.setVisibility(View.VISIBLE);
        	 
        	 info_industryclassification.setText(commMapArray.get("industryclassification")[0]);
        	 info_startworktime.setText(commMapArray.get("worktimeStart")[0]);
        	 info_endworktime.setText(commMapArray.get("worktimeEnd")[0]);
        	 info_expectedsalary.setText(commMapArray.get("expectedsalary")[0]);;
        		
        	 info_companyname.setText(commMapArray.get("companyname")[0]);
        	 info_jobtitle.setText(commMapArray.get("jobtitle")[0]);
             info_workdescdetail.setText(commMapArray.get("workdesc")[0]);
        	 
         }else{
        	 edit.setVisibility(View.GONE);
         }
		
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		save.setOnClickListener(this);
		next.setOnClickListener(this);
		leftLable.setOnClickListener(this);
		info_industryclassification.setOnClickListener(this);
		info_expectedsalary.setOnClickListener(this);
		info_startworktime.setOnClickListener(this);
		info_endworktime.setOnClickListener(this);
		
	}
	
	private void initViews(){
		rightLable.setText(CommUtil.getStrValue(self, R.string.review_resume));
		toptext.setText(CommUtil.getStrValue(self, R.string.resume_workexperience));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			String info_companynameStr = CommUtil.getEditTextValue(info_companyname);
			String info_jobtitleStr = CommUtil.getEditTextValue(info_jobtitle);
			String info_workdescdetailStr = CommUtil.getEditTextValue(info_workdescdetail);
			
			String info_industryclassificationStr = CommUtil.getTextValue(info_industryclassification);
			String info_startworktimeStr = CommUtil.getTextValue(info_startworktime);
			String info_endworktimeStr = CommUtil.getTextValue(info_endworktime);
			String info_expectedsalaryStr = CommUtil.getTextValue(info_expectedsalary);
			
			/*List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			params.add("p_userId");
			params.add("p_companyname");
			params.add("p_industryclassification");
			params.add("p_jobtitle");
			params.add("p_worktimeStart");
			params.add("p_worktimeEnd");
			params.add("p_expectedsalary");
			params.add("p_workdesc");
			params.add("createtime");
			
			values.add("2");
			values.add(info_companynameStr);
			values.add(info_jobtitleStr);
			values.add(info_workdescdetailStr);
			values.add(info_industryclassificationStr);
			values.add(info_startworktimeStr);
			values.add(info_endworktimeStr);
			values.add(info_expectedsalaryStr);
			values.add(TimeUtils.getCurrentTimeInString());*/
		
			String where = "delete from " + CommonText.WORKEXPERIENCE + " where  userId = 2";
//			dbUtil.delectData(self, where);
			
			ContentValues cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("companyname", info_companynameStr);
			cValues.put("industryclassification", info_industryclassificationStr);
			cValues.put("jobtitle", info_jobtitleStr);
			cValues.put("worktimeStart", info_startworktimeStr);
			cValues.put("worktimeEnd", info_endworktimeStr);
			cValues.put("expectedsalary", info_expectedsalaryStr);
			cValues.put("workdesc", info_workdescdetailStr);
			cValues.put("createtime", TimeUtils.getCurrentTimeInString());
			queryResult = dbUtil.insertData(self, CommonText.WORKEXPERIENCE, cValues);
			if(queryResult){
				edit.setVisibility(View.VISIBLE);
			}
			
			/*requestData("pro_workexpericnce", 2, params, values,CommonText.WORKEXPERIENCE,where, new HandlerData() {
				@Override
				public void error() {
					CommUtil.ToastMsg(getApplicationContext(), "失败");
				}
				
				public void success(Map<String, List<String>> map) {
					try {
						if (map.get("msg").get(0).equals("200")) {
							CommUtil.ToastMsg(getApplicationContext(), "新增工作经验成功");
						}
					} catch (Exception e) {
					}
				}
			});*/
			
			break;
		case R.id.next:
			startActivity(".ui.EvaluationActivity", false);
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			startActivity(".MainActivity", false);
			break;
		case R.id.info_industryclassification:
			whichTab = 1;
			item_values = CommUtil.getArrayValue(self,R.array.oi_hylb_values); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, info_industryclassification, 
					R.string.we_info_expectedsalary, mList, 
					mHandler);
			break;
		case R.id.info_expectedsalary:
			whichTab = 2;
			item_values = CommUtil.getArrayValue(self,R.array.we_qwyx_values); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, info_expectedsalary, 
					R.string.we_info_expectedsalary, mList, 
					mHandler);
			
			break;
		case R.id.info_startworktime:
			DialogUtils.showTimeChooseDialog(self, info_startworktime,R.string.we_info_choose_start_worktime, 11,mHandler);
			break;
		case R.id.info_endworktime:
			DialogUtils.showTimeChooseDialog(self, info_endworktime,R.string.we_info_choose_end_worktime, 12,mHandler);
			break;
		default:
			break;
		}
		
	}
}
