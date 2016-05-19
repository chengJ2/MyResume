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

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.comm.ResponseCode;
import com.me.resume.model.UUIDGenerator;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.TimeUtils;
import com.whjz.android.text.CommonText;

/**
 * 
 * @ClassName: OtherInfoActivity
 * @Description: 其他信息
 * @date 2016/4/6 下午1:32:58
 * 
 */
public class OtherInfoActivity extends BaseActivity implements OnClickListener {

	private TextView info_language, info_literacyskills,info_listeningspeaking;
	private TextView ot_languages_edit, ot_languages_add;
	
	private TextView info_certificate,info_certificatetime;
	private TextView ot_certificate_edit, ot_certificate_add;

	private TextView info_title;
	private EditText info_description;
	private TextView ot_otherinfo_edit, ot_otherinfo_add;
	
	private String info_languageStr,info_literacyskillsStr,info_listeningspeakingStr;
	
	private String info_certificateStr,info_certificatetimesStr;
	
	private String info_titleStr,info_descriptionStr;
	
	private String langTokenId = "",certTokenId = "",otTokenId = "";
	private Map<String, String[]> mapArray,mapArray1,mapArray2;
	
	private String procName = "";
	
	public enum OtherInfoMenu{
		language,certificate,otherinfo
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				break;
			case 2:
				int position = (int) msg.obj;
				if (whichTab == 1) {
					info_language.setText(mList.get(position));
					info_literacyskills.setEnabled(true);
					info_listeningspeaking.setEnabled(true);
				} else if (whichTab == 2) {
					info_literacyskills.setText(mList.get(position));
				} else if (whichTab == 3) {
					info_listeningspeaking.setText(mList.get(position));
				}else if (whichTab == 4) {
					info_certificate.setText(mList.get(position));
				}else if (whichTab == 5) {
					info_title.setText(mList.get(position));
				}
				break;
			case 13:
				if (msg.obj != null) {
					info_certificatetime.setText(((String)msg.obj));
				}
				break;
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.OTHERINFO, 
							new String[]{uTokenId,"background"}, 
							new String[]{"1",getCheckColor(checkColor)},2);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
						actionAync(OtherInfoMenu.language,1);
					}else{
						toastMsg(R.string.action_update_fail);
					}
				}
				break;
			case OnTopMenu.MSG_MENU2:
				if (msg.obj != null) {
					preferenceUtil.setPreferenceData("edit_mode",(boolean) msg.obj);
				}
				break;
			case OnTopMenu.MSG_MENU3:
				if (actionFlag == 0) {
					actionAync(OtherInfoMenu.language,3,R.string.action_syncing_lang);
					actionAync(OtherInfoMenu.certificate,3,R.string.action_syncing_cert);
					actionAync(OtherInfoMenu.otherinfo,3,R.string.action_syncing_other);
				}else{
					actionAync(OtherInfoMenu.language,1,R.string.action_syncing_lang);
					actionAync(OtherInfoMenu.certificate,1,R.string.action_syncing_cert);
					actionAync(OtherInfoMenu.otherinfo,1,R.string.action_syncing_other);
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
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_otherinfo_layout, null);
		boayLayout.addView(v);
		
		findViews();

		initViews();
		
		setLanguages();
		setCertificate();
		setOtherinfoData();

	}

	private void findViews() {
		info_language = findView(R.id.info_language);
		info_literacyskills = findView(R.id.info_literacyskills);
		info_listeningspeaking = findView(R.id.info_listeningspeaking);

		ot_languages_edit = findView(R.id.ot_languages_edit);
		ot_languages_add = findView(R.id.ot_languages_add);

		info_certificate = findView(R.id.info_certificate);;
		info_certificatetime = findView(R.id.info_certificatetime);;
		
		ot_certificate_edit = findView(R.id.ot_certificate_edit);
		ot_certificate_add = findView(R.id.ot_certificate_add);

		info_title = findView(R.id.info_title);
		info_description = findView(R.id.info_description);
		
		ot_otherinfo_edit = findView(R.id.ot_otherinfo_edit);
		ot_otherinfo_add = findView(R.id.ot_otherinfo_add);

		info_language.setOnClickListener(this);
		info_literacyskills.setOnClickListener(this);
		info_listeningspeaking.setOnClickListener(this);

		ot_languages_add.setOnClickListener(this);
		ot_languages_edit.setOnClickListener(this);

		ot_certificate_edit.setOnClickListener(this);
		ot_certificate_add.setOnClickListener(this);
		
		info_certificate.setOnClickListener(this);
		info_certificatetime.setOnClickListener(this);

		ot_otherinfo_edit.setOnClickListener(this);
		ot_otherinfo_add.setOnClickListener(this);
		
		info_title.setOnClickListener(this);
	}

	private void initViews() {
		setTopTitle(R.string.resume_otherinfo);
		setMsgHide();
		setRightIconVisible(View.VISIBLE);
		setRight2IconVisible(View.VISIBLE);
		setfabLayoutVisible(View.GONE);
		
		if (CommUtil.textIsNull(info_language)) {
			info_literacyskills.setEnabled(false);
			info_listeningspeaking.setEnabled(false);
		}else{
			info_literacyskills.setEnabled(true);
			info_listeningspeaking.setEnabled(true);
		}
	}

	
	
	private boolean getLanguagesData(){
		queryWhere = "select * from " + CommonText.OTHERINFO + " where userId = '"+ uTokenId +"' order by id desc limit 1";
		mapArray = dbUtil.queryData(self, queryWhere);
        if (mapArray!= null && mapArray.get("userId").length > 0) {
        	langTokenId = mapArray.get("tokenId")[0];
        	ot_languages_edit.setVisibility(View.VISIBLE);
        	return true;
        }else{
        	ot_languages_edit.setVisibility(View.GONE);
        	return false;
        }
	}
	
	private void setLanguages(){
		if (getLanguagesData()) {
			info_language.setText(mapArray.get("language")[0]);
    		info_literacyskills.setText(mapArray.get("literacyskills")[0]);
    		info_listeningspeaking.setText(mapArray.get("listeningspeaking")[0]);
		}
	}
	
	
	private boolean getcertificateData(){
		queryWhere = "select * from " + CommonText.OTHERINFO1 + " where userId = '"+ uTokenId +"' order by id desc limit 1";
		mapArray1 = dbUtil.queryData(self, queryWhere);
        if (mapArray1!= null && mapArray1.get("userId").length > 0) {
        	certTokenId = mapArray1.get("tokenId")[0];
        	ot_certificate_edit.setVisibility(View.VISIBLE);
    		return true;
        }else{
        	ot_certificate_edit.setVisibility(View.GONE);
        	return false;
        }
	}
	
	private void setCertificate(){
		if (getcertificateData()) {
			
			info_certificate.setText(mapArray1.get("certificate")[0]);
			info_certificatetime.setText(mapArray1.get("certificatetime")[0]);
		}
	}
	
	private boolean getotherinfoData(){
		 queryWhere = "select * from " + CommonText.OTHERINFO2 + " where userId = '"+ uTokenId +"' order by id desc limit 1";
		 mapArray2 = dbUtil.queryData(self, queryWhere);
	        if (mapArray2!= null && mapArray2.get("userId").length > 0) {
	        	otTokenId = mapArray2.get("tokenId")[0];
	        	ot_otherinfo_edit.setVisibility(View.VISIBLE);
	    		return true;
	        }else{
	        	ot_otherinfo_edit.setVisibility(View.GONE);
	        	return false;
	        }
	}
	
	private void setOtherinfoData(){
        if(getotherinfoData()){
        	info_title.setText(mapArray2.get("title")[0]);
    		info_description.setText(mapArray2.get("description")[0]);
        }
	}
	
	private void getlangFeild(){
		info_languageStr = CommUtil.getTextValue(info_language);
		info_literacyskillsStr = CommUtil.getTextValue(info_literacyskills);
		info_listeningspeakingStr = CommUtil.getTextValue(info_listeningspeaking);
	}
	
	private void getCertFeild(){
		info_certificateStr = CommUtil.getTextValue(info_certificate);
		info_certificatetimesStr = CommUtil.getTextValue(info_certificatetime);
	}

	private void getOtherFeild(){
		info_titleStr = CommUtil.getTextValue(info_title);
		info_descriptionStr = CommUtil.getTextValue(info_description);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.info_language:
			whichTab = 1;
			getValues(R.array.oi_wysp_values, info_language,
					R.string.ot_info_language);
			break;
		case R.id.info_literacyskills:
			whichTab = 2;
			getValues(R.array.oi_tsdx_values, info_literacyskills,
					R.string.ot_info_literacyskills);
			break;
		case R.id.info_listeningspeaking:
			whichTab = 3;
			getValues(R.array.oi_tsdx_values, info_listeningspeaking,
					R.string.ot_info_listeningspeaking);
			break;
		case R.id.ot_languages_add:
			actionFlag = 1;
			getlangFeild();
			ContentValues cValues = new ContentValues();
			cValues.put("tokenId", UUIDGenerator.getKUUID());
			cValues.put("userId", uTokenId);
			cValues.put("language", info_languageStr);
			cValues.put("literacyskills", info_literacyskillsStr);
			cValues.put("listeningspeaking", info_listeningspeakingStr);
			cValues.put("bgcolor", getCheckColor(checkColor));
			cValues.put("createtime", TimeUtils.getCurrentTimeInString());

			queryResult = dbUtil.insertData(self, CommonText.OTHERINFO, cValues);
			if (queryResult) {
				toastMsg(R.string.action_add_success);
				ot_languages_edit.setVisibility(View.VISIBLE);
				
				if(getLanguagesData()){
					actionAync(OtherInfoMenu.language,1,R.string.action_syncing_lang);
				}
			}
			break;
		case R.id.ot_languages_edit:
			getlangFeild();
			if (getLanguagesData()) {
				actionFlag = 2;
				updResult = dbUtil.updateData(self, CommonText.OTHERINFO, 
						new String[]{langTokenId,"language","literacyskills","listeningspeaking","updatetime"}, 
						new String[]{uTokenId,info_languageStr,info_literacyskillsStr,info_listeningspeakingStr,TimeUtils.getCurrentTimeInString()},3);
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
					actionAync(OtherInfoMenu.language,1,R.string.action_syncing_lang);
				}else{
					toastMsg(R.string.action_update_fail);
				}
			}
			break;
		case R.id.ot_certificate_add:
			actionFlag = 1;
			getCertFeild();
			cValues = new ContentValues();
			cValues.put("tokenId", UUIDGenerator.getKUUID());
			cValues.put("userId", uTokenId);
			cValues.put("certificate", info_certificateStr);
			cValues.put("certificatetime", info_certificatetimesStr);
			cValues.put("createtime", TimeUtils.getCurrentTimeInString());

			queryResult = dbUtil.insertData(self, CommonText.OTHERINFO1, cValues);
			if (queryResult) {
				toastMsg(R.string.action_add_success);
				actionAync(OtherInfoMenu.certificate,1,R.string.action_syncing_cert);
				if(getcertificateData()){
					ot_certificate_edit.setVisibility(View.VISIBLE);
				}
			}
			break;
		case R.id.ot_certificate_edit:
			getCertFeild();
			if(getcertificateData()){
				actionFlag = 2;
				updResult = dbUtil.updateData(self, CommonText.OTHERINFO1, 
						new String[]{certTokenId,"certificate","certificatetime"}, 
						new String[]{uTokenId,info_certificateStr,info_certificatetimesStr},3);
				if (updResult == 1) {
					actionAync(OtherInfoMenu.certificate,1,R.string.action_syncing_cert);
					toastMsg(R.string.action_update_success);
				}else{
					toastMsg(R.string.action_update_fail);
				}
			}
			break;
		case R.id.ot_otherinfo_add:
			actionFlag = 1;
			getOtherFeild();
			cValues = new ContentValues();
			cValues.put("tokenId", UUIDGenerator.getKUUID());
			cValues.put("userId", uTokenId);
			cValues.put("title", info_titleStr);
			cValues.put("description", info_descriptionStr);

			queryResult = dbUtil.insertData(self, CommonText.OTHERINFO2, cValues);
			if (queryResult) {
				toastMsg(R.string.action_add_success);
				ot_otherinfo_edit.setVisibility(View.VISIBLE);
				if (getotherinfoData()) {
					actionAync(OtherInfoMenu.otherinfo,1,R.string.action_syncing_other);
				}
			}
			break;
		case R.id.ot_otherinfo_edit:
			getOtherFeild();
			if (getotherinfoData()) {
				actionFlag = 2;
				updResult = dbUtil.updateData(self, CommonText.OTHERINFO2, 
						new String[]{otTokenId,"title","description"}, 
						new String[]{uTokenId,info_titleStr,info_descriptionStr},3);
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
					actionAync(OtherInfoMenu.otherinfo,1,R.string.action_syncing_other);
				}else{
					toastMsg(R.string.action_update_fail);
				}
			}
			break;
		case R.id.info_certificate:
			whichTab = 4;
			getValues(R.array.oi_certificatetype_values,info_certificate,R.string.ot_info_certificate);
			break;
		case R.id.info_certificatetime:
			DialogUtils.showTimeChooseDialog(self, info_certificatetime,R.string.ot_info_getcertificatetime,13,mHandler);
			break;
		case R.id.info_title:
			whichTab = 5;
			getValues(R.array.oi_topic_values,info_title,R.string.ot_info_choose_title);
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
	 * @Title:EvaluationActivity
	 * @Description: 执行同步操作
	 */
	private void actionAync(OtherInfoMenu menu,int style){
		if (!MyApplication.USERID.equals("0")) {
			if (CommUtil.isNetworkAvailable(self)) {
				set3Msg(R.string.action_syncing,5*1000);
				syncData(menu,style);
			} else {
				set3Msg(R.string.check_network);
			}
		} else {
			set3Msg(R.string.action_login_head);
		}
	}
	
	/**
	 * 
	 * @Title:EvaluationActivity
	 * @Description: 执行同步操作
	 */
	private void actionAync(OtherInfoMenu menu,int style,int resId){
		if (!MyApplication.USERID.equals("0")) {
			if (CommUtil.isNetworkAvailable(self)) {
				set3Msg(resId,5*1000);
				syncData(menu,style);
			} else {
				set3Msg(R.string.check_network);
			}
		} else {
			set3Msg(R.string.action_login_head);
		}
	}
	
	/**
	 * 
	 * @Title:OtherInfoActivity
	 * @Description: 判断server是否有数据
	 * @param menu 项目
	 * @param style 1:判断server是否有数据 3：同步server数据
	 * @param style2 2:添加 3：更新
	 */
	private void syncData(final OtherInfoMenu menu,final int style){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_tokenId");
		params.add("p_userId");
		
		if(menu == OtherInfoMenu.language){
			procName = "pro_get_other";
			values.add(langTokenId);
		}else if(menu == OtherInfoMenu.certificate){
			procName = "pro_get_other1";
			values.add(certTokenId);
		}else if(menu == OtherInfoMenu.otherinfo){
			procName = "pro_get_other2";
			values.add(otTokenId);
		}
		values.add(uTokenId);
		
		requestData(procName, style, params, values, new HandlerData() {
			@Override
			public void error() {
				if (style == 1) {
					syncRun(menu,tokenId,2);
				}else{
					runOnUiThread(R.string.action_sync_success);
				}
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (style == 1) {
						tokenId = map.get("tokenId").get(0);
						if (map.get("userId").get(0).equals(uTokenId)) {
							syncRun(menu,tokenId,3);
						}else{
							syncRun(menu,tokenId,2);
						}
					}else{
						if(menu == OtherInfoMenu.language){
							setlangDataFromServer(map);
						}else if(menu == OtherInfoMenu.certificate){
							setCretDataFromServer(map);
						}else if(menu == OtherInfoMenu.otherinfo){
							setOtherDataFromServer(map);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 更新本地数据
	 * @param map
	 */
	private void setlangDataFromServer(Map<String, List<String>> map){
		queryWhere = "select * from " + CommonText.OTHERINFO + " where userId = '"+ uTokenId +"' order by id desc limit 1";
		Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
        if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	
        	info_languageStr = map.get("language").get(0);
        	info_literacyskillsStr = map.get("literacyskills").get(0);
        	info_listeningspeakingStr = map.get("listeningspeaking").get(0);
        	
        	updResult = dbUtil.updateData(self, CommonText.OTHERINFO, 
					new String[]{langTokenId,"language","literacyskills","listeningspeaking","updatetime"}, 
					new String[]{uTokenId,info_languageStr,info_literacyskillsStr,info_listeningspeakingStr,
        			TimeUtils.getCurrentTimeInString()},3);
        	
        }else{
        	int size = map.get("userId").size();
			for (int i = 0; i < size; i++) {
				ContentValues cValues = new ContentValues();
				cValues.put("tokenId", map.get("tokenId").get(i));
				cValues.put("userId", map.get("userId").get(i));
				cValues.put("language", map.get("language").get(i));
				cValues.put("literacyskills", map.get("literacyskills").get(i));
				cValues.put("listeningspeaking", map.get("listeningspeaking").get(i));
				cValues.put("createtime", map.get("createtime").get(i));
				cValues.put("updatetime", map.get("updatetime").get(i));
				queryResult = dbUtil.insertData(self, CommonText.OTHERINFO, cValues);
			}
        }
	}
	
	/**
	 * 更新本地数据
	 * @param map
	 */
	private void setCretDataFromServer(Map<String, List<String>> map){
		queryWhere = "select * from " + CommonText.OTHERINFO1 + " where userId = '"+ uTokenId +"' order by id desc limit 1";
        commMapArray = dbUtil.queryData(self, queryWhere);
        if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	
        	info_certificateStr = map.get("certificate").get(0);
        	info_certificatetimesStr = map.get("certificatetime").get(0);
        	
        	updResult = dbUtil.updateData(self, CommonText.OTHERINFO1, 
					new String[]{certTokenId,"certificate","certificatetime","updatetime"}, 
					new String[]{uTokenId,info_certificateStr,info_certificatetimesStr,TimeUtils.getCurrentTimeInString()},3);
        }else{
        	int size = map.get("userId").size();
			for (int i = 0; i < size; i++) {
				ContentValues cValues = new ContentValues();
				cValues.put("tokenId", map.get("tokenId").get(i));
				cValues.put("userId", map.get("userId").get(i));
				cValues.put("certificate", map.get("certificate").get(i));
				cValues.put("certificatetime", map.get("certificatetime").get(i));
				cValues.put("createtime", map.get("createtime").get(i));
				cValues.put("updatetime", map.get("updatetime").get(i));
				queryResult = dbUtil.insertData(self, CommonText.OTHERINFO1, cValues);
			}
        }
	}
	
	/**
	 * 更新本地数据
	 * @param map
	 */
	private void setOtherDataFromServer(Map<String, List<String>> map){
		queryWhere = "select * from " + CommonText.OTHERINFO2 + " where userId = '"+ uTokenId +"' order by id desc limit 1";
        commMapArray = dbUtil.queryData(self, queryWhere);
        if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	
        	info_titleStr = map.get("title").get(0);
        	info_descriptionStr = map.get("description").get(0);
        	
        	updResult = dbUtil.updateData(self, CommonText.OTHERINFO2, 
					new String[]{otTokenId,"title","description","updatetime"}, 
					new String[]{uTokenId,info_titleStr,info_descriptionStr,TimeUtils.getCurrentTimeInString()},3);
        }else{
        	int size = map.get("userId").size();
			for (int i = 0; i < size; i++) {
				ContentValues cValues = new ContentValues();
				cValues.put("tokenId", map.get("tokenId").get(i));
				cValues.put("userId", map.get("userId").get(i));
				cValues.put("title", map.get("title").get(i));
				cValues.put("description", map.get("description").get(i));
				cValues.put("createtime", map.get("createtime").get(i));
				cValues.put("updatetime", map.get("updatetime").get(i));
				queryResult = dbUtil.insertData(self, CommonText.OTHERINFO2, cValues);
			}
        }
	}
	
	/**
	 * 
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void syncRun(OtherInfoMenu menu,String tokenId,int style){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_tokenId");
		params.add("p_userId");
		
		if(menu == OtherInfoMenu.language){
			procName = "pro_set_other";
			
			values.add(langTokenId);
			values.add(uTokenId);
			
			params.add("p_language");
			params.add("p_literacyskills");
			params.add("p_listeningspeaking");
			params.add("p_bgcolor");
			
			values.add(info_languageStr);
			values.add(info_literacyskillsStr);
			values.add(info_listeningspeakingStr);
			values.add(getCheckColor(checkColor));
		}else if(menu == OtherInfoMenu.certificate){
			procName = "pro_set_other1";
			
			values.add(certTokenId);
			values.add(uTokenId);
			
			params.add("p_certificate");
			params.add("p_certificatetime");
			
			values.add(info_certificateStr);
			values.add(info_certificatetimesStr);
		}else if(menu == OtherInfoMenu.otherinfo){
			procName = "pro_set_other2";
			
			values.add(otTokenId);
			values.add(uTokenId);
			
			params.add("p_title");
			params.add("p_description");
			
			values.add(info_titleStr);
			values.add(info_descriptionStr);
		}
		
		requestData(procName, style, params, values, new HandlerData() {
			@Override
			public void error() {
				runOnUiThread(R.string.action_sync_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("msg").get(0).equals(ResponseCode.RESULT_OK)) {
						runOnUiThread(R.string.action_sync_success);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void getValues(int array, View parent, int resId) {
		String[] item_text = CommUtil.getArrayValue(self, array);
		mList = Arrays.asList(item_text);
		DialogUtils.showPopWindow(self, parent, resId, mList, mHandler);
	}
}
