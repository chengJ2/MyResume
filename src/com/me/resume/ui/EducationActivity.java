package com.me.resume.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.ui.fragment.AllFragmentFactory;
import com.me.resume.ui.fragment.EducationFragment;
import com.me.resume.ui.fragment.TrainingFragment;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.CustomFAB;
import com.me.resume.views.SegmentButton;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: EducationActivity 
* @Description: 教育培训经历
* @author Comsys-WH1510032 
* @date 2016/4/6 下午1:32:23 
*
 */
public class EducationActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private SegmentButton segment_button;
	
	private CustomFAB save,edit,next;
	
	private TextView msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_layout);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		toptext.setText(CommUtil.getStrValue(self, R.string.resume_education));
		rightLable.setText(CommUtil.getStrValue(self, R.string.review_resume));
		
		msg = findView(R.id.msg);
		msg.setVisibility(View.GONE);
		
		segment_button = findView(R.id.segment_button);
		
		save = findView(R.id.save);
		save.setOnClickListener(this);
		
		edit = findView(R.id.edit);
		edit.setOnClickListener(this);
		
		next = findView(R.id.next);
		next.setOnClickListener(this);
		
		initData();
	}
	
	private void initData() {
		String tab = segment_button.getSegmentButton(0).getText().toString();
		switchContent(MyApplication.cposition,tab);
		segment_button.setOnCheckedChangeListener(new SegmentButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(int position, Button button) {
				MyApplication.cposition = position;
				switchContent(MyApplication.cposition,button.getText().toString());
			}
		});
		
	}
	
	private void switchContent(int position,String tab){
		FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
		Fragment fragment=AllFragmentFactory.getFragment(tab);
		String tablename = CommonText.EDUCATION;
//		L.d("switchContent--->"+fragment +"--position:"+position+ "--mPosition->"+MyApplication.cposition + " =tab="+tab);
		if(fragment==null){
			switch (position) {
			case 0:
				fragment=new EducationFragment();
				tablename = CommonText.EDUCATION ;				
				break;
			case 1:
				fragment=new TrainingFragment();
				tablename = CommonText.EDUCATION_TRAIN ;
				break;
			}
			setEditVisible(tablename);
			AllFragmentFactory.putFragment(fragment, tab);
		}
		if(fragment.isAdded()){
			fragment.onResume();
		}else{
			transaction.replace(R.id.layout_content, fragment);
			transaction.commit();
		}
	}
	
	/**
	 * 
	 * @Title:EducationActivity
	 * @Description: 编辑按钮显示隐藏
	 * @author Comsys-WH1510032
	 * @return 返回类型  
	 * @param tablename
	 */
	private void setEditVisible(String tablename){
		queryWhere = "select * from " + tablename + " where userId = 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			edit.setVisibility(View.VISIBLE);
		}else{
			edit.setVisibility(View.GONE);
		}
	}
	
	String info_starttimeStr,info_endtimeStr,info_schoolStr,info_majornameStr,info_degressStr,info_examinationStr;
	
	String info_trainingorganizationStr,info_trainingclassStr,info_certificateStr,info_descriptionStr;
	
	@Override
	public void onClick(View v) {
		String tab = segment_button.getSegmentButton(MyApplication.cposition).getText().toString();
		Fragment f = AllFragmentFactory.getFragment(tab);
		if(f != null){
			if(MyApplication.cposition == 0){ // 教育背景
				info_starttimeStr = ((EducationFragment)f).getInfoStartTime();
				info_endtimeStr = ((EducationFragment)f).getInfoEndTime();
				info_schoolStr = ((EducationFragment)f).getInfoSchool();
				info_majornameStr = ((EducationFragment)f).getInfomajorname();
				info_degressStr = ((EducationFragment)f).getInfodegree();
				info_examinationStr = ((EducationFragment)f).getInfoexamination();
			}else{
				info_starttimeStr = ((TrainingFragment)f).getInfoStartTime();
				info_endtimeStr = ((TrainingFragment)f).getInfoEndTime();
				info_trainingorganizationStr = ((TrainingFragment)f).getInfotrainingorganization();
				info_trainingclassStr = ((TrainingFragment)f).getInfotrainingclass();
				info_certificateStr = ((TrainingFragment)f).getInfocertificate();
				info_descriptionStr = ((TrainingFragment)f).getInfodescription();
			}
		}
		switch (v.getId()) {
		case R.id.save:
			if(MyApplication.cposition == 0){ // 教育背景
				if( judgeEduField() == 0){
					ContentValues cValues = new ContentValues();
					cValues.put("userId", "1");
					cValues.put("worktimestart", info_starttimeStr);
					cValues.put("worktimeend", info_endtimeStr);
					cValues.put("school", info_schoolStr);
					cValues.put("majorname", info_majornameStr);
					cValues.put("degree", info_degressStr);
					cValues.put("examination", info_examinationStr);
					cValues.put("createtime", TimeUtils.getCurrentTimeInString());
					
					queryResult= dbUtil.insertData(self, 
							CommonText.EDUCATION, cValues);
					if (queryResult) {
						toastMsg(R.string.action_add_success);
					}
				}
				
			}else{ // 培训经历
				if(judegTraField() == 0){
					ContentValues cValues = new ContentValues();
					cValues.put("userId", "1");
					cValues.put("worktimestart", info_starttimeStr);
					cValues.put("worktimeend", info_endtimeStr);
					cValues.put("trainingorganization", info_trainingorganizationStr);
					cValues.put("trainingclass", info_trainingclassStr);
					cValues.put("certificate", info_certificateStr);
					cValues.put("description", info_descriptionStr);
					cValues.put("createtime", TimeUtils.getCurrentTimeInString());
					
					queryResult = dbUtil.insertData(self, 
							CommonText.EDUCATION_TRAIN, cValues);
					if (queryResult) {
						toastMsg(R.string.action_add_success);
					}
				}
			}
			break;
		case R.id.edit:
			if(MyApplication.cposition == 0){ // 教育背景
				if( judgeEduField() == 0){
					queryWhere = "select * from " + CommonText.EDUCATION + " where userId = 1 order by _id limit 1";
					commMapArray = dbUtil.queryData(self, queryWhere);
					if (commMapArray!= null && commMapArray.get("userId").length > 0) {
						String edId = commMapArray.get("_id")[0];
						updResult = dbUtil.updateData(self, CommonText.EDUCATION, 
								new String[]{edId,"worktimestart","worktimeend","school","majorname",
												  "degree","examination"}, 
								new String[]{"1",info_starttimeStr,info_endtimeStr,info_schoolStr,info_majornameStr,
								info_degressStr,info_examinationStr});
						if (updResult == 1) {
							toastMsg(R.string.action_update_success);
						}else{
							toastMsg(R.string.action_update_fail);
						}
					}
				}
			}else{
				if(judegTraField() == 0){
					queryWhere = "select * from " + CommonText.EDUCATION_TRAIN + " where userId = 1 order by _id limit 1";
					commMapArray = dbUtil.queryData(self, queryWhere);
					if (commMapArray!= null && commMapArray.get("userId").length > 0) {
						String edId = commMapArray.get("_id")[0];
						updResult = dbUtil.updateData(self, CommonText.EDUCATION_TRAIN, 
								new String[]{edId,"worktimestart","worktimeend","trainingorganization","trainingclass",
								"certificate","description"}, 
								new String[]{"1",info_starttimeStr,info_endtimeStr,info_trainingorganizationStr,info_trainingclassStr,
								info_certificateStr,info_descriptionStr});
						if (updResult == 1) {
							toastMsg(R.string.action_update_success);
						}else{
							toastMsg(R.string.action_update_fail);
						}
					}
				}
			}
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
		default:
			break;
		}
	}
	
	// 教育背景
	private int judgeEduField(){
		if (!RegexUtil.checkNotNull(info_starttimeStr)) {
			msg.setText(CommUtil.getStrValue(self, R.string.we_info_start_worktime) + fieldNull);
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		
		if (!RegexUtil.checkNotNull(info_endtimeStr)) {
			msg.setText(CommUtil.getStrValue(self, R.string.we_info_end_worktime) + fieldNull);
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		
		if (TimeUtils.compareDate(info_starttimeStr, info_endtimeStr) <= 0) {
			msg.setText(CommUtil.getStrValue(self, R.string.we_info_compare_worktime));
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		
		if (!RegexUtil.checkNotNull(info_schoolStr)) {
			msg.setText(CommUtil.getStrValue(self, R.string.ed_info_school) + fieldNull);
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		
//		if (!RegexUtil.checkNotNull(info_majornameStr)) {
//			msg.setText(CommUtil.getStrValue(self, R.string.ed_info_majorname) + fieldNull);
//			msg.setVisibility(View.VISIBLE);
//			return -1;
//		}
		
		if (!RegexUtil.checkNotNull(info_degressStr)) {
			msg.setText(CommUtil.getStrValue(self, R.string.ed_info_degree) + fieldNull);
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		return 0;
	}
	
	private int judegTraField(){
		if (!RegexUtil.checkNotNull(info_starttimeStr)) {
			msg.setText(CommUtil.getStrValue(self, R.string.we_info_start_worktime) + fieldNull);
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		
		if (!RegexUtil.checkNotNull(info_endtimeStr)) {
			msg.setText(CommUtil.getStrValue(self, R.string.we_info_end_worktime) + fieldNull);
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		
		if (TimeUtils.compareDate(info_starttimeStr, info_endtimeStr) <= 0) {
			msg.setText(CommUtil.getStrValue(self, R.string.we_info_compare_worktime));
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		
		if (!RegexUtil.checkNotNull(info_trainingorganizationStr)) {
			msg.setText(CommUtil.getStrValue(self, R.string.ed_info_trainingorganization) + fieldNull);
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		
		if (!RegexUtil.checkNotNull(info_trainingclassStr)) {
			msg.setText(CommUtil.getStrValue(self, R.string.ed_info_trainingclass) + fieldNull);
			msg.setVisibility(View.VISIBLE);
			return -1;
		}
		return 0;
	}
	
}
