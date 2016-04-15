package com.me.resume.ui;

import java.util.Map;

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
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.L;
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.save:
			String tab = segment_button.getSegmentButton(MyApplication.cposition).getText().toString();
			Fragment f = AllFragmentFactory.getFragment(tab);
			if(MyApplication.cposition == 0){ // 教育背景
				if(f != null){
					String info_timeStr = ((EducationFragment)f).getInfoTime();
					String info_schoolStr = ((EducationFragment)f).getInfoSchool();
					String info_majornameStr = ((EducationFragment)f).getInfomajorname();
					String info_degressStr = ((EducationFragment)f).getInfodegree();
					String info_examinationStr = ((EducationFragment)f).getInfoexamination();
	
					ContentValues cValues = new ContentValues();
					cValues.put("userId", "1");
					cValues.put("time", info_timeStr);
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
				if(f != null){
					String info_timeStr = ((TrainingFragment)f).getInfoTime();
					String info_trainingorganizationStr = ((TrainingFragment)f).getInfotrainingorganization();
					String info_trainingclassStr = ((TrainingFragment)f).getInfotrainingclass();
					String info_certificateStr = ((TrainingFragment)f).getInfocertificate();
					String info_descriptionStr = ((TrainingFragment)f).getInfodescription();
					
					ContentValues cValues = new ContentValues();
					cValues.put("userId", "1");
					cValues.put("time", info_timeStr);
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
			
			break;
		case R.id.next:
			ActivityUtils.startActivity(self, MyApplication.PACKAGENAME
					+ ".ui.JobIntensionActivity");
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(self, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
	}
}
