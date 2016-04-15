package com.me.resume.ui;

import java.util.Arrays;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.CustomFAB;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: JobIntensionActivity 
* @Description: 求职意向 
* @author Comsys-WH1510032 
* @date 2016/4/7 下午4:42:34 
*
 */
public class JobIntensionActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private CustomFAB save,edit,next;
	
	// 工作性质，工作点，从事行业，薪资，状态
	private TextView info_exp_workingproperty,info_expworkplace,info_expworkindustry,info_expmonthlysalary,info_workingstate;
	
	// 职业
	private EditText info_expworkcareer;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				DialogUtils.dismissPopwindow();
				break;
			case 2:
				int position = (int) msg.obj;
				if(whichTab == 1){
					
				}else if(whichTab == 2){
					
				}else if(whichTab == 3){
					
				}else if(whichTab == 4){
					info_expmonthlysalary.setText(mList.get(position));
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
		setContentView(R.layout.activity_jobintension_layout);
		
		findViews();
		
		initViews();
	}

	private void findViews(){
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		
		info_exp_workingproperty = findView(R.id.info_exp_workingproperty);
		info_expworkplace = findView(R.id.info_expworkplace);
		info_expworkindustry = findView(R.id.info_expworkindustry);
		info_expmonthlysalary = findView(R.id.info_expmonthlysalary);
		info_workingstate = findView(R.id.info_workingstate);
		
		info_expworkcareer = findView(R.id.info_expworkcareer);
		
		
		save = findView(R.id.save);
		save.setOnClickListener(this);
		
		edit = findView(R.id.edit);
		edit.setOnClickListener(this);
		
		next = findView(R.id.next);
		next.setOnClickListener(this);
		
		info_exp_workingproperty.setOnClickListener(this);
		info_expworkplace.setOnClickListener(this);
		info_expworkindustry.setOnClickListener(this);
		info_expmonthlysalary.setOnClickListener(this);
		info_workingstate.setOnClickListener(this);
	}
	
	private void initViews() {
		toptext.setText(CommUtil.getStrValue(self, R.string.resume_jobintension));
		rightLable.setText(CommUtil.getStrValue(self, R.string.review_resume));
		
		 queryWhere = "select * from " + CommonText.JOBINTENSION + " where userId = 1";
		 commMapArray = dbUtil.queryData(self, queryWhere);
         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	 edit.setVisibility(View.VISIBLE);
        	 info_exp_workingproperty.setText(commMapArray.get("expworkingproperty")[0]);
        	 info_expworkplace.setText(commMapArray.get("expdworkplace")[0]);
        	 info_expworkindustry.setText(commMapArray.get("expworkindustry")[0]);
        	 info_expmonthlysalary.setText(commMapArray.get("expmonthlysalary")[0]);
        	 info_workingstate.setText(commMapArray.get("workingstate")[0]);
        	 info_expworkcareer.setText(commMapArray.get("expworkcareer")[0]);
         }else{
        	 edit.setVisibility(View.GONE);
         }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			String info_exp_workingpropertyStr = CommUtil.getTextValue(info_exp_workingproperty);
			String info_expworkplaceStr = CommUtil.getTextValue(info_expworkplace);
			String info_expworkindustryStr = CommUtil.getTextValue(info_expworkindustry);
			String info_expmonthlysalaryStr = CommUtil.getTextValue(info_expmonthlysalary);
			String info_workingstateStr = CommUtil.getTextValue(info_workingstate);
			
			String info_expworkcareerStr = CommUtil.getEditTextValue(info_expworkcareer);
			
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
				edit.setVisibility(View.VISIBLE);
			}
			
			break;
		case R.id.edit:
			
			
			
			break;
		case R.id.next:
			startActivity(".ui.OtherInfoActivity",false);
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			startActivity(".MainActivity",false);
			break;
		case R.id.info_exp_workingproperty:
			whichTab = 1;
			break;
		case R.id.info_expworkplace:
			whichTab = 2;
			break;
		case R.id.info_expworkindustry:
			whichTab = 3;
			break;
		case R.id.info_expmonthlysalary:
			whichTab = 4;
			item_values = CommUtil.getArrayValue(self,R.array.we_qwyx_values); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, info_expmonthlysalary, 
					R.string.we_info_expectedsalary, mList, 
					mHandler);
			break;
		case R.id.info_workingstate:
			
			break;
		default:
			break;
		}
	}
}
