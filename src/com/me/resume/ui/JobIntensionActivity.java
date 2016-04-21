package com.me.resume.ui;

import java.util.Arrays;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.tools.L;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
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

	private TextView toptext;
	
	private ImageView left_icon,right_icon;
	
	private CustomFAB save_edit,next;
	
	// 工作性质，工作点，职业,从事行业，薪资，状态
	private TextView info_exp_workingproperty,info_expworkplace,info_expworkcareer,info_expworkindustry,info_expmonthlysalary,info_workingstate;
	
	private TextView msg;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				DialogUtils.dismissPopwindow();
				break;
			case 2:
				int position = (int) msg.obj;
				if(whichTab == 1){
					info_exp_workingproperty.setText(mList.get(position));
				}else if(whichTab == 5){
					info_workingstate.setText(mList.get(position));
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
		left_icon = findView(R.id.left_lable);
		right_icon = findView(R.id.right_icon);
		left_icon.setOnClickListener(this);
		right_icon.setOnClickListener(this);
		
		msg = findView(R.id.msg);
		msg.setVisibility(View.GONE);
		
		info_exp_workingproperty = findView(R.id.info_exp_workingproperty);
		info_expworkplace = findView(R.id.info_expworkplace);
		info_expworkindustry = findView(R.id.info_expworkindustry);
		info_expmonthlysalary = findView(R.id.info_expmonthlysalary);
		info_workingstate = findView(R.id.info_workingstate);
		
		info_expworkcareer = findView(R.id.info_expworkcareer);
		
		save_edit = findView(R.id.save_edit);
		save_edit.setOnClickListener(this);
		
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
		
		 queryWhere = "select * from " + CommonText.JOBINTENSION + " where userId = 1 limit 1";
		 commMapArray = dbUtil.queryData(self, queryWhere);
         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	 save_edit.setImageResource(R.drawable.ic_btn_edit);
        	 info_exp_workingproperty.setText(commMapArray.get("expworkingproperty")[0]);
        	 info_expworkplace.setText(commMapArray.get("expdworkplace")[0]);
        	 info_expworkindustry.setText(commMapArray.get("expworkindustry")[0]);
        	 info_expmonthlysalary.setText(commMapArray.get("expmonthlysalary")[0]);
        	 info_workingstate.setText(commMapArray.get("workingstate")[0]);
        	 info_expworkcareer.setText(commMapArray.get("expworkcareer")[0]);
         }else{
        	 save_edit.setImageResource(R.drawable.ic_btn_add);
         }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save_edit:
			String info_exp_workingpropertyStr = CommUtil.getTextValue(info_exp_workingproperty);
			String info_expworkplaceStr = CommUtil.getTextValue(info_expworkplace);
			String info_expworkcareerStr = CommUtil.getTextValue(info_expworkcareer);
			String info_expworkindustryStr = CommUtil.getTextValue(info_expworkindustry);
			String info_expmonthlysalaryStr = CommUtil.getTextValue(info_expmonthlysalary);
			String info_workingstateStr = CommUtil.getTextValue(info_workingstate);
			
			if (!RegexUtil.checkNotNull(info_exp_workingpropertyStr)) {
				msg.setText(CommUtil.getStrValue(self, R.string.ji_info_expectedworkingproperty) + fieldNull);
				msg.setVisibility(View.VISIBLE);
				return;
			}
			
//			if (!RegexUtil.checkNotNull(info_expworkplaceStr)) {
//				msg.setText(CommUtil.getStrValue(self, R.string.ji_info_expectedworkplace) + fieldNull);
//				msg.setVisibility(View.VISIBLE);
//				return;
//			}
			
//			if (!RegexUtil.checkNotNull(info_expworkcareerStr)) {
//				msg.setText(CommUtil.getStrValue(self, R.string.ji_info_expectedworkcareer) + fieldNull);
//				msg.setVisibility(View.VISIBLE);
//				return;
//			}
			
			if (!RegexUtil.checkNotNull(info_expworkindustryStr)) {
				msg.setText(CommUtil.getStrValue(self, R.string.ji_info_expectedworkindustry) + fieldNull);
				msg.setVisibility(View.VISIBLE);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_expmonthlysalaryStr)) {
				msg.setText(CommUtil.getStrValue(self, R.string.ji_info_expectedmonthlysalary) + fieldNull);
				msg.setVisibility(View.VISIBLE);
				return;
			}
			
			if (!RegexUtil.checkNotNull(info_workingstateStr)) {
				msg.setText(CommUtil.getStrValue(self, R.string.ji_info_choose_workingstate) + fieldNull);
				msg.setVisibility(View.VISIBLE);
				return;
			}
			
			queryWhere = "select * from " + CommonText.JOBINTENSION + " where userId = 1";
			 commMapArray = dbUtil.queryData(self, queryWhere);
	         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
	        	String edId = commMapArray.get("_id")[0];
				updResult = dbUtil.updateData(self, CommonText.JOBINTENSION, 
						new String[]{edId,"expworkingproperty","expdworkplace","expworkindustry","expworkcareer",
										  "expmonthlysalary","workingstate"}, 
						new String[]{"1",info_exp_workingpropertyStr,info_expworkplaceStr,info_expworkindustryStr,
										info_expworkcareerStr,info_expmonthlysalaryStr,info_workingstateStr});
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
				}else{
					toastMsg(R.string.action_update_fail);
				}
	         }else{
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
	 				save_edit.setImageResource(R.drawable.ic_btn_edit);
	 			}
	         }
			break;
		case R.id.next:
			startActivity(".ui.EducationActivity",false);
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			startActivity(".MainActivity",false);
			break;
		case R.id.info_exp_workingproperty:
			whichTab = 1;
			item_values = CommUtil.getArrayValue(self,R.array.ji_workingproperty_values); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, info_exp_workingproperty, 
					R.string.ji_info_expectedworkingproperty, mList, 
					mHandler);
			break;
		case R.id.info_expworkplace:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAME + ".ui.AddressActivity", false, Constants.JI_REQUEST_CODE2);
			break;
		case R.id.info_expworkindustry:
			ActivityUtils.startActivityForResult(self, 
					Constants.PACKAGENAME + ".ui.IndustryTypeActivity", false, Constants.JI_REQUEST_CODE);
			break;
		case R.id.info_expmonthlysalary:
			whichTab = 4;
			item_values = CommUtil.getArrayValue(self,R.array.we_qwyx_values); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, info_expmonthlysalary, 
					R.string.ji_info_expectedmonthlysalary, mList, 
					mHandler);
			break;
		case R.id.info_workingstate:
			whichTab = 5;
			item_values = CommUtil.getArrayValue(self,R.array.ji_jobstatue_values); 
			mList = Arrays.asList(item_values);
			DialogUtils.showPopWindow(self, info_workingstate, 
					R.string.ji_info_workingstate, mList, 
					mHandler);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d("onActivityResult"+"requestCode="+requestCode+" resultCode="+resultCode + " data:"+data);
        if(requestCode == Constants.JI_REQUEST_CODE) {
            if(resultCode == Constants.RESULT_CODE) {
                String result = data.getStringExtra("name");
                info_expworkindustry.setText(result);
            }
        }else if(requestCode == Constants.JI_REQUEST_CODE2){
        	if(resultCode == Constants.RESULT_CODE) {
                String city = data.getStringExtra("city");
                info_expworkplace.setText(city);
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
		
	}
}
