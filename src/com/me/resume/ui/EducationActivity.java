package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.comm.ResponseCode;
import com.me.resume.tools.UUIDGenerator;
import com.me.resume.ui.fragment.AllFragmentFactory;
import com.me.resume.ui.fragment.EducationFragment;
import com.me.resume.ui.fragment.TrainingFragment;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.SegmentButton;
import com.umeng.analytics.MobclickAgent;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: EducationActivity 
* @Description: 教育培训经历
* @date 2016/4/6 下午1:32:23 
*
 */
public class EducationActivity extends BaseActivity implements OnClickListener{

	private SegmentButton segment_button;
	
	private String info_starttimeStr,info_endtimeStr,info_schoolStr,info_majornameStr,info_degressStr,info_examinationStr;
	
	private String info_trainingorganizationStr,info_trainingclassStr,info_certificateStr,info_descriptionStr;
	
	private int cposition = 0;
	private String procname = ""; 
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (String) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.EDUCATION, 
							new String[]{"userId=?","bgcolor"}, 
							new String[]{uTokenId,checkColor},1);
					if (updResult > 0) {
						toastMsg(R.string.action_update_success);
						if (preferenceUtil.getPreferenceData(Constants.AUTOSYNC)) {
							actionAync(0);
						}
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
				actionAync(cposition);
				break;
			case OnTopMenu.MSG_MENU31:
				set3Msg(R.string.action_login_head);
				break;
			case OnTopMenu.MSG_MENU33:
				set3Msg(R.string.check_network);
				break;
			case OnTopMenu.MSG_MENU32:
				Bundle b = new Bundle();
				b.putInt(Constants.TAB, cposition);
				b.putString(Constants.TYPE, CommonText.EDUCATION);
				ActivityUtils.startActivityForResult(self, 
						Constants.PACKAGENAMECHILD + Constants.INFOMANAGER, 
						b,Constants.ED_MANAGER_REQUEST_CODE);
				break;
			default:
				break;
			}
		};
	};
	
	/**
	 * @Description: 执行同步操作
	 * position 0 教育 1 培训
	 */
	private void actionAync(int position){
		if (!MyApplication.USERID.equals("0")) {
			if (CommUtil.isNetworkAvailable(self)) {
				set3Msg(R.string.action_syncing,Constants.DEFAULTIME);
				if (actionFlag == 0) {
					syncData(position,3);
				}else{
					syncData(position,1);
				}
			}else{
				set3Msg(R.string.check_network);
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_education_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.resume_education);
		setMsgHide();
		setRight2IconVisible(View.VISIBLE);
		setfabLayoutVisible(View.VISIBLE);
		setEditBtnVisible(View.GONE);
		
		segment_button = findView(R.id.segment_button);
		
		initData();
		
		registerReceiver();
	}
	
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.EDUCATION_GET);
		filter.addAction(Constants.TRAIN_GET);
		registerReceiver(educationArcReceiver, filter);
		
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
			getData(tablename);
			AllFragmentFactory.putFragment(fragment, tab);
		}
		if(fragment.isAdded()){
			fragment.onResume();
		}else{
			transaction.replace(R.id.layout_content, fragment);
			transaction.commit();
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.save:
			actionFlag = 1;
			if(cposition == 0){ // 教育背景
				if(judgeEduField()){
					ContentValues cValues = new ContentValues();
					tokenId = UUIDGenerator.getKUUID();
					cValues.put("tokenId", tokenId);
					cValues.put("userId", uTokenId);
					cValues.put("educationtimestart", info_starttimeStr);
					cValues.put("educationtimeend", info_endtimeStr);
					cValues.put("school", info_schoolStr);
					cValues.put("majorname", info_majornameStr);
					cValues.put("degree", info_degressStr);
					cValues.put("examination", info_examinationStr);
					cValues.put("bgcolor", checkColor);
					cValues.put("createtime", TimeUtils.getCurrentTimeInString());
					
					queryResult= dbUtil.insertData(self, CommonText.EDUCATION, cValues);
					if (queryResult) {
						toastMsg(R.string.action_add_success);
						setEditBtnVisible(View.VISIBLE);
						if (preferenceUtil.getPreferenceData(Constants.AUTOSYNC)) {
							actionAync(0);
						}
					}
				}
			}else{ // 培训经历
				if(judgeTraField()){
					ContentValues cValues = new ContentValues();
					tokenId = UUIDGenerator.getKUUID();
					cValues.put("tokenId", tokenId);
					cValues.put("userId", uTokenId);
					cValues.put("trainingtimestart", info_starttimeStr);
					cValues.put("trainingtimeend", info_endtimeStr);
					cValues.put("trainingorganization", info_trainingorganizationStr);
					cValues.put("trainingclass", info_trainingclassStr);
					cValues.put("certificate", info_certificateStr);
					cValues.put("description", info_descriptionStr);
					cValues.put("bgcolor", checkColor);
					cValues.put("createtime", TimeUtils.getCurrentTimeInString());
					
					queryResult = dbUtil.insertData(self, 
							CommonText.EDUCATION_TRAIN, cValues);
					if (queryResult) {
						setEditBtnVisible(View.VISIBLE);
						toastMsg(R.string.action_add_success);
						if (preferenceUtil.getPreferenceData(Constants.AUTOSYNC)) {
							actionAync(1);
						}
					}
				}
			}
			break;
		case R.id.edit:
			actionFlag = 2;
			if(cposition == 0){ // 教育背景
				if(judgeEduField()){
					updResult = dbUtil.updateData(self, CommonText.EDUCATION, 
							new String[]{tokenId,"educationtimestart","educationtimeend","school","majorname",
											  "degree","examination"}, 
							new String[]{uTokenId,info_starttimeStr,info_endtimeStr,info_schoolStr,info_majornameStr,
							info_degressStr,info_examinationStr},3);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						if (preferenceUtil.getPreferenceData(Constants.AUTOSYNC)) {
							actionAync(0);
						}
					}else{
						toastMsg(R.string.action_update_fail);
					}
				}
			}else{
				if(judgeTraField()){
					updResult = dbUtil.updateData(self, CommonText.EDUCATION_TRAIN, 
							new String[]{tokenId,"trainingtimestart","trainingtimeend","trainingorganization","trainingclass",
							"certificate","description"}, 
							new String[]{uTokenId,info_starttimeStr,info_endtimeStr,info_trainingorganizationStr,info_trainingclassStr,
							info_certificateStr,info_descriptionStr},3);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						if (preferenceUtil.getPreferenceData(Constants.AUTOSYNC)) {
							actionAync(1);
						}
					}else{
						toastMsg(R.string.action_update_fail);
					}
				}
			}
			break;
		case R.id.next:
			startChildActivity(Constants.OTHERINFO,false);
			break;
		case R.id.right_icon_more:
			DialogUtils.showTopMenuDialog(self, topLayout,1, mHandler);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Title:EducationActivity
	 * @Description: 获取初始字段的值
	 */
	private void getFeild(){
		String tab = segment_button.getSegmentButton(cposition).getText().toString();
		Fragment f = AllFragmentFactory.getFragment(tab);
		if(f != null){
			if(cposition == 0){ // 教育背景
				tokenId = ((EducationFragment)f).getEduId();
				info_starttimeStr = ((EducationFragment)f).getInfoStartTime();
				info_endtimeStr = ((EducationFragment)f).getInfoEndTime();
				info_schoolStr = ((EducationFragment)f).getInfoSchool();
				info_majornameStr = ((EducationFragment)f).getInfomajorname();
				info_degressStr = ((EducationFragment)f).getInfodegree();
				info_examinationStr = ((EducationFragment)f).getInfoexamination();
			}else{
				tokenId = ((TrainingFragment)f).getTrId();
				info_starttimeStr = ((TrainingFragment)f).getInfoStartTime();
				info_endtimeStr = ((TrainingFragment)f).getInfoEndTime();
				info_trainingorganizationStr = ((TrainingFragment)f).getInfotrainingorganization();
				info_trainingclassStr = ((TrainingFragment)f).getInfotrainingclass();
				info_certificateStr = ((TrainingFragment)f).getInfocertificate();
				info_descriptionStr = ((TrainingFragment)f).getInfodescription();
			}
		}
	}
	
	/**
	 * 
	 * @Title:EducationActivity
	 * @Description: 编辑按钮显示隐藏
	 * @param tablename CommonText.EDUCATION  |  CommonText.EDUCATION_TRAIN
	 */
	private boolean getData(String tablename){
		queryWhere = "select * from " + tablename + " where userId = '"+ uTokenId +"' order by id desc limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			tokenId = commMapArray.get("tokenId")[0];
			setEditBtnVisible(View.VISIBLE);
			return true;
		}else{
			setEditBtnVisible(View.GONE);
			return false;
		}
	}
	
	/**
	 * 数据请求是否已同步
	 * @param cposition
	 */
	private void syncData(final int cposition,final int style){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		params.add("p_tokenId");
		params.add("p_userId");
		values.add(tokenId);
		values.add(uTokenId);
		if(cposition == 0){
			procname = "pro_get_education";
		}else{
			procname = "pro_get_training";
		}
		
		requestData(procname, style, params, values, new HandlerData() {
			@Override
			public void error() {
				set3Msg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (style == 1) {
						String tokenId = map.get("tokenId").get(0);
						if (map.get("userId").get(0).equals(uTokenId)) {
							syncRun(cposition,tokenId,3);
						}else{
							syncRun(cposition,tokenId,2);
						}	
					}else{
						if(cposition == 0){
							setEdDataFromServer(map);
						}else{
							setTrDataFromServer(map);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				if (style == 1) {
					syncRun(cposition,tokenId,2);
				}else if(style == 3 && (judgeEduField() || judgeTraField())){
					syncRun(cposition,tokenId,2);
				}
			}
		});
		
	}
	
	
	/**
	 * 更新本地数据
	 * @param map
	 */
	private void setEdDataFromServer(Map<String, List<String>> map){
		queryWhere = "select * from " + CommonText.EDUCATION + " where userId = '" + uTokenId + "' limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			
			tokenId = map.get("tokenId").get(0);
			
			info_starttimeStr = map.get("educationtimestart").get(0);
			info_endtimeStr = map.get("educationtimeend").get(0);
			info_schoolStr = map.get("school").get(0);
			info_majornameStr = map.get("majorname").get(0);
			info_degressStr = map.get("degree").get(0);
			info_examinationStr= map.get("examination").get(0);
			
			updResult = dbUtil.updateData(self, CommonText.EDUCATION, 
					new String[]{tokenId,"educationtimestart","educationtimeend","school","majorname",
									  "degree","examination"}, 
					new String[]{uTokenId,info_starttimeStr,info_endtimeStr,info_schoolStr,info_majornameStr,
					info_degressStr,info_examinationStr},3);
		}else{
			int size = map.get("userId").size();
			for (int i = 0; i < size; i++) {
				ContentValues cValues = new ContentValues();
				cValues.put("tokenId", map.get("tokenId").get(i));
				cValues.put("userId", map.get("userId").get(i));
				cValues.put("educationtimestart", map.get("educationtimestart").get(i));
				cValues.put("educationtimeend", map.get("educationtimeend").get(i));
				cValues.put("school", map.get("school").get(i));
				cValues.put("majorname", map.get("majorname").get(i));
				cValues.put("degree", map.get("degree").get(i));
				cValues.put("bgcolor", map.get("bgcolor").get(i));
				cValues.put("createtime", map.get("createtime").get(i));
				cValues.put("updatetime", map.get("updatetime").get(i));
				queryResult = dbUtil.insertData(self, CommonText.EDUCATION, cValues);
			}
		}
		
		if (updResult == 1 || queryResult) {
			set3Msg(R.string.action_sync_success);
			
			Intent intent=new Intent();
			intent.setAction(Constants.EDUCATION_RECEIVE_ED);
       		sendBroadcast(intent);
		}
	}
	
	/**
	 * 注册从远程同步到本地数据的广播
	 */
	private BroadcastReceiver educationArcReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String tablename = "";
			if (intent.getAction().equals(Constants.EDUCATION_GET)) {
				tablename = CommonText.EDUCATION;
			} else if (intent.getAction().equals(Constants.TRAIN_GET)) {
				tablename = CommonText.EDUCATION_TRAIN;
			}
			getData(tablename);
		}
	};
	
	/**
	 * 更新本地数据
	 * @param map
	 */
	private void setTrDataFromServer(Map<String, List<String>> map){
		queryWhere = "select * from " + CommonText.EDUCATION_TRAIN + " where userId = '" + uTokenId + "' limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			tokenId = map.get("tokenId").get(0);
			info_starttimeStr = map.get("educationtimestart").get(0);
			info_endtimeStr = map.get("educationtimeend").get(0);
			info_trainingorganizationStr = map.get("educationtimestart").get(0);
			info_trainingclassStr= map.get("educationtimestart").get(0);
			info_certificateStr= map.get("educationtimestart").get(0);
			info_descriptionStr= map.get("educationtimestart").get(0);
			
			updResult = dbUtil.updateData(self, CommonText.EDUCATION_TRAIN, 
					new String[]{tokenId,"trainingtimestart","trainingtimeend","trainingorganization","trainingclass",
					"certificate","description"}, 
					new String[]{uTokenId,info_starttimeStr,info_endtimeStr,info_trainingorganizationStr,info_trainingclassStr,
					info_certificateStr,info_descriptionStr},3);
		}else{
			int size = map.get("userId").size();
			for (int i = 0; i < size; i++) {
				ContentValues cValues = new ContentValues();
				cValues.put("tokenId", map.get("tokenId").get(i));
				cValues.put("userId", map.get("userId").get(i));
				cValues.put("trainingtimestart", map.get("trainingtimestart").get(i));
				cValues.put("trainingtimeend", map.get("trainingtimeend").get(i));
				cValues.put("trainingorganization", map.get("trainingorganization").get(i));
				cValues.put("trainingclass", map.get("trainingclass").get(i));
				cValues.put("certificate", map.get("certificate").get(i));
				cValues.put("description", map.get("description").get(i));
				cValues.put("bgcolor", map.get("bgcolor").get(i));
				cValues.put("createtime", map.get("createtime").get(i));
				cValues.put("updatetime", map.get("updatetime").get(i));
				queryResult = dbUtil.insertData(self, CommonText.EDUCATION_TRAIN, cValues);
			}
		}
		
		if (updResult == 1 || queryResult) {
			set3Msg(R.string.action_sync_success);
			Intent intent=new Intent();
			intent.setAction(Constants.EDUCATION_RECEIVE_TR);
       		sendBroadcast(intent);
		}
	}
	
	/**
	 * 同步数据
	 */
	private void syncRun(int cposition,String edId,int style){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_tokenId");
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
				values.add(checkColor);
				
				requestData("pro_set_education", style, params, values, new HandlerData() {
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
		}else{
			if (judgeTraField()) {
				params.add("p_trainingtimestart");
				params.add("p_trainingtimeend");
				params.add("p_trainingorganization");
				params.add("p_trainingclass");
				params.add("p_certificate");				
				params.add("p_description");
				params.add("p_bgcolor");
				
				values.add(info_starttimeStr);
				values.add(info_endtimeStr);
				values.add(info_trainingorganizationStr);
				values.add(info_trainingclassStr);
				values.add(info_certificateStr);
				values.add(info_descriptionStr);
				values.add(checkColor);
				
				requestData("pro_set_training", style, params, values, new HandlerData() {
					@Override
					public void error() {
						
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
	}
	
	/**
	 * 教育背景模块字段校验
	 * @return boolean
	 */
	private boolean judgeEduField(){
		getFeild();
		if (!RegexUtil.checkNotNull(info_starttimeStr)) {
			setMsg(R.string.we_info_start_worktime);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_endtimeStr)) {
			setMsg(R.string.we_info_end_worktime);
			return false;
		}
		
		if (TimeUtils.compareDate(info_starttimeStr, info_endtimeStr) <= 0) {
			set3Msg(R.string.we_info_compare_worktime);
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
		getFeild();
		if (!RegexUtil.checkNotNull(info_starttimeStr)) {
			setMsg(R.string.we_info_start_worktime);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(info_endtimeStr)) {
			setMsg(R.string.we_info_end_worktime);
			return false;
		}
		
		if (TimeUtils.compareDate(info_starttimeStr, info_endtimeStr) <= 0) {
			set3Msg(R.string.we_info_compare_worktime);
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
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
		if (educationArcReceiver != null) {
			unregisterReceiver(educationArcReceiver);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
