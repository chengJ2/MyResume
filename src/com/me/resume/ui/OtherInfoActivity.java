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

import com.me.resume.R;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.TimeUtils;
import com.whjz.android.text.CommonText;

/**
 * 
 * @ClassName: OtherInfoActivity
 * @Description: 其他信息
 * @author Comsys-WH1510032
 * @date 2016/4/6 下午1:32:58
 * 
 */
public class OtherInfoActivity extends BaseActivity implements
		OnClickListener {


	private TextView info_language, info_literacyskills,
			info_listeningspeaking;

	private TextView ot_languages_edit, ot_languages_add;
	
	private TextView info_certificate,info_certificatetime;

	private TextView ot_certificate_edit, ot_certificate_add;

	private TextView info_title;
	private EditText info_description;
	
	private TextView ot_otherinfo_edit, ot_otherinfo_add;

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
					info_certificatetime.setText(((String)msg.obj)/*.substring(0, 4)*/);
				}
				break;
			case OnTopMenu.MSG_MENU1:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
					updResult = dbUtil.updateData(self, CommonText.OTHERINFO, 
							new String[]{uTokenId,"background"}, 
							new String[]{"1",String.valueOf(checkColor)},2);
					if (updResult == 1) {
						toastMsg(R.string.action_update_success);
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
					syncData();
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
		
		View v = View.inflate(self,R.layout.activity_otherinfo_layout, null);
		boayLayout.addView(v);
		
		findViews();

		initViews();
		
		initData();

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
		
		setRight2IconVisible(View.VISIBLE);
		
		setfabLayoutVisible(View.GONE);
		
		if (CommUtil.textIsNull(info_language)) {
			info_literacyskills.setEnabled(false);
			info_listeningspeaking.setEnabled(false);
		}
	}

	private void initData(){
		queryWhere = "select * from " + CommonText.OTHERINFO + " where userId = '"+ uTokenId +"' order by id desc limit 1";
		Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
        if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	ot_languages_edit.setVisibility(View.VISIBLE);
        	
        	info_language.setText(commMapArray.get("language")[0]);
    		info_literacyskills.setText(commMapArray.get("literacyskills")[0]);
    		info_listeningspeaking.setText(commMapArray.get("listeningspeaking")[0]);
        	
        }else{
        	ot_languages_edit.setVisibility(View.GONE);
        }
        
        queryWhere = "select * from " + CommonText.OTHERINFO1 + " where userId = '"+ uTokenId +"' order by id desc limit 1";
        commMapArray = dbUtil.queryData(self, queryWhere);
        if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	ot_certificate_edit.setVisibility(View.VISIBLE);
        	
        	info_certificate.setText(commMapArray.get("certificate")[0]);
    		info_certificatetime.setText(commMapArray.get("certificatetime")[0]);
        	
        }else{
        	ot_certificate_edit.setVisibility(View.GONE);
        }
        
        queryWhere = "select * from " + CommonText.OTHERINFO2 + " where userId = '"+ uTokenId +"' order by id desc limit 1";
        commMapArray = dbUtil.queryData(self, queryWhere);
        if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	ot_otherinfo_edit.setVisibility(View.VISIBLE);
        	info_title.setText(commMapArray.get("title")[0]);
    		info_description.setText(commMapArray.get("description")[0]);
        }else{
        	ot_otherinfo_edit.setVisibility(View.GONE);
        }
	}
	
	private String info_languageStr,info_literacyskillsStr,info_listeningspeakingStr;
	
	private String info_certificateStr,info_certificatetimesStr;
	
	private String info_titleStr,info_descriptionStr;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			startActivity(".MainActivity", false);
			break;
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
			info_languageStr = CommUtil.getTextValue(info_language);
			info_literacyskillsStr = CommUtil.getTextValue(info_literacyskills);
			info_listeningspeakingStr = CommUtil.getTextValue(info_listeningspeaking);

			ContentValues cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("language", info_languageStr);
			cValues.put("literacyskills", info_literacyskillsStr);
			cValues.put("listeningspeaking", info_listeningspeakingStr);
			cValues.put("createtime", TimeUtils.getCurrentTimeInString());

			queryResult = dbUtil
					.insertData(self, CommonText.OTHERINFO, cValues);
			if (queryResult) {
				toastMsg(R.string.action_add_success);
				ot_languages_edit.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.ot_languages_edit:
			info_languageStr = CommUtil.getTextValue(info_language);
			info_literacyskillsStr = CommUtil
					.getTextValue(info_literacyskills);
			info_listeningspeakingStr = CommUtil
					.getTextValue(info_listeningspeaking);
			queryWhere = "select * from " + CommonText.OTHERINFO + " where userId = '"+ uTokenId +"' order by id desc limit 1";
			 commMapArray = dbUtil.queryData(self, queryWhere);
	         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
	        	String edId = commMapArray.get("id")[0];
				updResult = dbUtil.updateData(self, CommonText.OTHERINFO, 
						new String[]{edId,"language","literacyskills","listeningspeaking"}, 
						new String[]{"1",info_languageStr,info_literacyskillsStr,info_listeningspeakingStr},2);
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
				}else{
					toastMsg(R.string.action_update_fail);
				}
	         }
			break;
		case R.id.ot_certificate_add:
			info_certificateStr = CommUtil.getTextValue(info_certificate);
			info_certificatetimesStr = CommUtil.getTextValue(info_certificatetime);

			cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("certificate", info_certificateStr);
			cValues.put("certificatetime", info_certificatetimesStr);
			cValues.put("createtime", TimeUtils.getCurrentTimeInString());

			queryResult = dbUtil
					.insertData(self, CommonText.OTHERINFO1, cValues);
			if (queryResult) {
				toastMsg(R.string.action_add_success);
				ot_certificate_edit.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.ot_certificate_edit:
			info_certificateStr = CommUtil.getTextValue(info_certificate);
			info_certificatetimesStr = CommUtil.getTextValue(info_certificatetime);
			queryWhere = "select * from " + CommonText.OTHERINFO1 + " where userId = '"+ uTokenId +"' order by id desc limit 1";
			 commMapArray = dbUtil.queryData(self, queryWhere);
	         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
	        	String edId = commMapArray.get("id")[0];
				updResult = dbUtil.updateData(self, CommonText.OTHERINFO1, 
						new String[]{edId,"certificate","certificatetime"}, 
						new String[]{"1",info_certificateStr,info_certificatetimesStr},2);
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
				}else{
					toastMsg(R.string.action_update_fail);
				}
	         }
			break;
		case R.id.ot_otherinfo_add:
			info_titleStr = CommUtil.getTextValue(info_title);
			info_descriptionStr = CommUtil
					.getTextValue(info_description);
			cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("title", info_titleStr);
			cValues.put("description", info_descriptionStr);

			queryResult = dbUtil
					.insertData(self, CommonText.OTHERINFO2, cValues);
			if (queryResult) {
				toastMsg(R.string.action_add_success);
				ot_otherinfo_edit.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.ot_otherinfo_edit:
			info_titleStr = CommUtil.getTextValue(info_title);
			info_descriptionStr = CommUtil
					.getTextValue(info_description);
			queryWhere = "select * from " + CommonText.OTHERINFO2 + " where userId = '"+ uTokenId +"' order by id desc limit 1";
			 commMapArray = dbUtil.queryData(self, queryWhere);
	         if (commMapArray!= null && commMapArray.get("userId").length > 0) {
	        	String edId = commMapArray.get("id")[0];
				updResult = dbUtil.updateData(self, CommonText.OTHERINFO2, 
						new String[]{edId,"title","description"}, 
						new String[]{"1",info_titleStr,info_descriptionStr},2);
				if (updResult == 1) {
					toastMsg(R.string.action_update_success);
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
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void syncData(){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		params.add("p_otId");
		params.add("p_userId");
		params.add("p_language");
		params.add("p_literacyskills");
		params.add("p_listeningspeaking");
		params.add("p_bgcolor");
		
		params.add("p_certificate");
		params.add("p_certificatetime");
		
		params.add("p_title");
		params.add("p_description");
		
		values.add("0");
		values.add("1");
		values.add(info_languageStr);
		values.add(info_literacyskillsStr);
		values.add(info_listeningspeakingStr);
		values.add(String.valueOf(checkColor));
		
		values.add(info_certificateStr);
		values.add(info_certificatetimesStr);
		
		values.add(info_titleStr);
		values.add(info_descriptionStr);
		
		requestData("pro_evaluation", 2, params, values, new HandlerData() {
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
	
	

	private void getValues(int array, View parent, int resId) {
		String[] item_text = CommUtil.getArrayValue(self, array);
		mList = Arrays.asList(item_text);
		DialogUtils.showPopWindow(self, parent, resId, mList, mHandler);
	}
}
