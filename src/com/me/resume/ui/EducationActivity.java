package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.ui.fragment.AllFragmentFactory;
import com.me.resume.ui.fragment.EducationFragment;
import com.me.resume.ui.fragment.TrainingFragment;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
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
public class EducationActivity extends BaseActivity implements OnClickListener{

	private SegmentButton segment_button;
	
	private int cposition = 0;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.EDUCATION, 
							new String[]{"userId=?","bgcolor"}, 
							new String[]{uTokenId,String.valueOf(checkColor)},1);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						if(!MyApplication.userId.equals("0")){
							set2Msg(R.string.action_syncing);
							syncData(cposition);
						}
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
				if (msg.obj != null) {
					set2Msg(R.string.action_syncing);
					syncData(cposition);
				}
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
		
		View v = View.inflate(self,R.layout.activity_education_layout, null);
		boayLayout.addView(v);
		
		setTitle(R.string.resume_education);
		setMsgHide();
		setRight2IconVisible(View.VISIBLE);
		
		setfabLayoutVisible(View.VISIBLE);
		setEditBtnVisible(View.GONE);
		
		segment_button = findView(R.id.segment_button);
		
		initData();
	}
	
	private void initData() {
		String tab = segment_button.getSegmentButton(0).getText().toString();
		switchContent(cposition,tab);
		segment_button.setOnCheckedChangeListener(new SegmentButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(int position, Button button) {
				cposition = position;
				switchContent(cposition,button.getText().toString());
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
		queryWhere = "select * from " + tablename + " where userId = '"+ uTokenId +"' limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			setEditBtnVisible(View.VISIBLE);
		}else{
			setEditBtnVisible(View.GONE);
		}
	}
	
	String info_starttimeStr,info_endtimeStr,info_schoolStr,info_majornameStr,info_degressStr,info_examinationStr;
	
	String info_trainingorganizationStr,info_trainingclassStr,info_certificateStr,info_descriptionStr;
	
	String edId = "";
	
	@Override
	public void onClick(View v) {
		String tab = segment_button.getSegmentButton(cposition).getText().toString();
		Fragment f = AllFragmentFactory.getFragment(tab);
		if(f != null){
			if(cposition == 0){ // 教育背景
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
			if(cposition == 0){ // 教育背景
				if( judgeEduField()){
					ContentValues cValues = new ContentValues();
					cValues.put("userId", uTokenId);
					cValues.put("educationtimestart", info_starttimeStr);
					cValues.put("educationtimeend", info_endtimeStr);
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
				if(judgeTraField()){
					ContentValues cValues = new ContentValues();
					cValues.put("userId", uTokenId);
					cValues.put("trainingtimestart", info_starttimeStr);
					cValues.put("trainingtimeend", info_endtimeStr);
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
			if(cposition == 0){ // 教育背景
				if( judgeEduField()){
					queryWhere = "select * from " + CommonText.EDUCATION + " where userId = '"+ uTokenId +"' order by id desc limit 1";
					commMapArray = dbUtil.queryData(self, queryWhere);
					if (commMapArray!= null && commMapArray.get("userId").length > 0) {
						edId = commMapArray.get("id")[0];
						updResult = dbUtil.updateData(self, CommonText.EDUCATION, 
								new String[]{edId,"educationtimestart","educationtimeend","school","majorname",
												  "degree","examination"}, 
								new String[]{uTokenId,info_starttimeStr,info_endtimeStr,info_schoolStr,info_majornameStr,
								info_degressStr,info_examinationStr},2);
						if (updResult == 1) {
							toastMsg(R.string.action_update_success);
						}else{
							toastMsg(R.string.action_update_fail);
						}
					}
				}
			}else{
				if(judgeTraField()){
					queryWhere = "select * from " + CommonText.EDUCATION_TRAIN + " where userId = '"+ uTokenId +"' order by id desc limit 1";
					commMapArray = dbUtil.queryData(self, queryWhere);
					if (commMapArray!= null && commMapArray.get("userId").length > 0) {
						edId = commMapArray.get("id")[0];
						updResult = dbUtil.updateData(self, CommonText.EDUCATION_TRAIN, 
								new String[]{edId,"trainingtimestart","trainingtimeend","trainingorganization","trainingclass",
								"certificate","description"}, 
								new String[]{uTokenId,info_starttimeStr,info_endtimeStr,info_trainingorganizationStr,info_trainingclassStr,
								info_certificateStr,info_descriptionStr},2);
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
		case R.id.right_icon:
			startActivity(".MainActivity",false);
			break;
		default:
			break;
		}
	}
	
	
	private String procname = "";
	
	/**
	 * 数据请求是否已同步
	 * @param cposition
	 */
	private void syncData(final int cposition){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		params.add("p_userId");
		values.add(uTokenId);
		if(cposition == 0){
			procname = "pro_get_education";
		}else{
			procname = "pro_get_trainging";
		}
		
		requestData(procname, 1, params, values, new HandlerData() {
			@Override
			public void error() {
				runOnUiThread(R.string.action_sync_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					String p_edId = map.get("id").get(0);
					if (map.get("userId").get(0).equals(uTokenId)) {
						syncRun(cposition,p_edId,3);
					}else{
						syncRun(cposition,"1",2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	/**
	 * 
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void syncRun(int cposition,String edId,int style){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_edId");
		params.add("p_userId");
		
		values.add(edId);
		values.add(uTokenId);
		
		if(cposition == 0){
			if (judgeEduField()) {
				params.add("p_educationtimestart");
				params.add("p_educationtimeend");
				params.add("p_school");
				params.add("p_majorname");
				params.add("p_degree");
				params.add("p_examination");
				params.add("p_bgcolor");
				
				values.add(info_starttimeStr);
				values.add(info_endtimeStr);
				values.add(info_schoolStr);
				values.add(info_majornameStr);
				values.add(info_degressStr);
				values.add(info_examinationStr);
				values.add(getCheckColor(checkColor));
				
				requestData("pro_education", style, params, values, new HandlerData() {
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
						}
					}
				});
				
			}
		}else{
			if (judgeTraField()) {
				params.add("trainingtimestart");
				params.add("trainingtimeend");
				params.add("trainingorganization");
				params.add("trainingclass");
				params.add("certificate");
				params.add("description");
				
				values.add(info_starttimeStr);
				values.add(info_endtimeStr);
				values.add(info_trainingorganizationStr);
				values.add(info_trainingclassStr);
				values.add(info_certificateStr);
				values.add(info_descriptionStr);
				
				requestData("pro_training", style, params, values, new HandlerData() {
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
						}
					}
				});
			}
		}
	}
	
	/**
	 * 教育背景模块字段校验
	 * @return boolean
	 */
	private boolean judgeEduField(){
		if (!RegexUtil.checkNotNull(info_starttimeStr)) {
			setMsg(R.string.we_info_start_worktime);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_endtimeStr)) {
			setMsg(R.string.we_info_end_worktime);
			return false;
		}
		
		if (TimeUtils.compareDate(info_starttimeStr, info_endtimeStr) <= 0) {
			set2Msg(R.string.we_info_compare_worktime);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_schoolStr)) {
			setMsg(R.string.ed_info_school);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_majornameStr)) {
			setMsg(R.string.ed_info_majorname);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_degressStr)) {
			setMsg(R.string.ed_info_degree);
			return false;
		}
		return true;
	}
	
	/**
	 * 培训模块字段校验
	 * @return boolean
	 */
	private boolean judgeTraField(){
		if (!RegexUtil.checkNotNull(info_starttimeStr)) {
			setMsg(R.string.we_info_start_worktime);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_endtimeStr)) {
			setMsg(R.string.we_info_end_worktime);
			return false;
		}
		
		if (TimeUtils.compareDate(info_starttimeStr, info_endtimeStr) <= 0) {
			set2Msg(R.string.we_info_compare_worktime);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_trainingorganizationStr)) {
			setMsg(R.string.ed_info_trainingorganization);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_trainingclassStr)) {
			setMsg(R.string.ed_info_trainingclass);
			return false;
		}
		return true;
	}
}
