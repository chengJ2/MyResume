package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.whjz.android.text.CommonText;

/**
 * 
* 栏目管理界面
* 2016/5/30 下午5:27:36 
 */
public class InfoManagerActivity extends BaseActivity implements OnClickListener{

	private ListView infoMoreListView;
	private TextView msgText;

	private CommForMapArrayBaseAdapter commMapAdapter = null;
	
	private Map<String, String[]> commMapArray = null;
	
	private String type = "";
	private int tab = 0;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String kId = (String)msg.obj;
				String procName = "";
				if (type.equals(CommonText.EDUCATION)) {
					tab = getIntent().getIntExtra(Constants.TAB, 0);
					L.d("==tab=="+tab);
					if (tab == 0) {
						procName = "pro_get_education";
						queryWhere = "delete from " + CommonText.EDUCATION
								+ " where userId = '" + uTokenId +"' and tokenId = '" + kId +"'";
					}else{
						procName = "pro_get_training";
						queryWhere = "delete from " + CommonText.EDUCATION_TRAIN
								+ " where userId = '" + uTokenId +"' and tokenId = '" + kId +"'";
					}
					dbUtil.deleteData(self, queryWhere);
					set3Msg(R.string.action_delete_success);
					initData(CommonText.EDUCATION);
					
				}else if(type.equals(CommonText.WORKEXPERIENCE)){
					procName = "pro_get_workexpericnce";
					queryWhere = "delete from " + CommonText.WORKEXPERIENCE
							+ " where userId = '" + uTokenId +"' and tokenId = '" + kId +"'";
					dbUtil.deleteData(self, queryWhere);
					
					set3Msg(R.string.action_delete_success);
					
					initData(CommonText.WORKEXPERIENCE);
					
				}else if (type.equals(CommonText.PROJECT_EXPERIENCE)) {
					procName = "pro_get_projectexpericnce";
					queryWhere = "delete from " + CommonText.PROJECT_EXPERIENCE
							+ " where userId = '" + uTokenId +"' and tokenId = '" + kId +"'";
					dbUtil.deleteData(self, queryWhere);
					
					set3Msg(R.string.action_delete_success);
					
					initData(CommonText.PROJECT_EXPERIENCE);
				}
				
				if (!MyApplication.USERID.equals("0")) {
					if (CommUtil.isNetworkAvailable(self)) {
						set3Msg(R.string.action_syncing, Constants.DEFAULTIME);
						syncData(kId,procName);
					}
				}
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
		View v = View.inflate(self, R.layout.info_manager_more_layout, null);
		boayLayout.addView(v);

		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		infoMoreListView = findView(R.id.infoMoreListView);
		msgText = findView(R.id.msgText);
		msgText.setVisibility(View.VISIBLE);
		msgText.setText(CommUtil.getStrValue(self, R.string.item_text43));

		type = getIntent().getStringExtra(Constants.TYPE);
		L.d("==type==" + type);
		initData(type);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	private void initData(final String type) {
		int layoutID = 0;
		if (type.equals(CommonText.EDUCATION)) {
			
			tab = getIntent().getIntExtra(Constants.TAB, 0);
			L.d("==tab=="+tab);
			if (tab == 0) {
				setTopTitle(R.string.resume_education);
				tableName = CommonText.EDUCATION;
			}else{
				setTopTitle(R.string.resume_training);
				tableName = CommonText.EDUCATION_TRAIN;
			}
		}else if(type.equals(CommonText.WORKEXPERIENCE)){
			setTopTitle(R.string.resume_workexperience);
			tableName = CommonText.WORKEXPERIENCE;
		}else if (type.equals(CommonText.PROJECT_EXPERIENCE)) {
			setTopTitle(R.string.resume_project_experience);
			tableName = CommonText.PROJECT_EXPERIENCE;
		}
		layoutID = R.layout.manage_info_list_item;
		
		queryWhere = "select * from " + tableName + " where userId = '" + uTokenId + "' order by createtime desc";
		commMapArray = dbUtil.queryData(self,queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			infoMoreListView.setVisibility(View.VISIBLE);
			setRightIconVisible(View.INVISIBLE);
			commMapAdapter = new CommForMapArrayBaseAdapter(self, commMapArray,
					layoutID, "userId") {

				@Override
				public void convert(ViewHolder holder, String[] item,
						int position) {
					if (type.equals(CommonText.EDUCATION)) {
						setEDData(holder, commMapArray, position, tab);
					} else if (type.equals(CommonText.WORKEXPERIENCE)) {
						setWEData(holder, commMapArray, position);
					} else if (type.equals(CommonText.PROJECT_EXPERIENCE)) {
						setPEData(holder, commMapArray, position);
					}
				}
			};
			infoMoreListView.setAdapter(commMapAdapter);
			msgText.setVisibility(View.GONE);
		} else {
			setRightIcon(R.drawable.icon_sync);
			msgText.setText(CommUtil.getStrValue(self, R.string.en_nodata));
			msgText.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 
	 * @Title:InfoManagerActivity
	 * @Description: 管理我的教育培训经历
	 * @param holder
	 * @param commMapArray
	 * @param position
	 */
	private void setEDData(ViewHolder holder,Map<String, String[]> commMapArray,int position,final int tab){
		if (tab == 0) {
			holder.setText(R.id.item1,
					commMapArray.get("educationtimestart")[position] + " — "
							+ commMapArray.get("educationtimeend")[position]);
			holder.setText(R.id.item11, commMapArray.get("school")[position]);
			
			StringBuffer sbStr = new StringBuffer();
			String info = commMapArray.get("majorname")[position];
			sbStr.append(info);
			sbStr.append(" | ");
			
			info = commMapArray.get("degree")[position];
			sbStr.append(info);
			
			holder.setText(R.id.item12, sbStr.toString());
			
		}else{
			holder.setText(R.id.item1,
					commMapArray.get("trainingtimestart")[position] + " — "
							+ commMapArray.get("trainingtimeend")[position]);
			
			String info = commMapArray.get("trainingorganization")[position];
			holder.setText(R.id.item11, info.toString());
			
			StringBuffer sbStr = new StringBuffer();
			String info2 = commMapArray.get("trainingclass")[position];
			sbStr.append("<strong>培训课程：</strong>"+ info2 + "<br/>");
			info2 = commMapArray.get("certificate")[position];
			if(RegexUtil.checkNotNull(info2)){
				sbStr.append("<strong>所获证书：</strong>"+ info2 + "<br/>");
			}
			info2 = commMapArray.get("description")[position];
			if(RegexUtil.checkNotNull(info2)){
				sbStr.append("<strong>培训描述：</strong>"+ info2 );
			}
			holder.setTextForHtml(R.id.item12, sbStr.toString());
			
		}
		final String tokenId = commMapArray.get("tokenId")[position];
		holder.setOnClickEvent(R.id.item21, new ClickEvent() {

			@Override
			public void onClick(View view) {
				DialogUtils.showDeleteDialog(self, tokenId, mHandler);
			}
		});

		holder.setOnClickEvent(R.id.item22, new ClickEvent() {

			@Override
			public void onClick(View view) {
		        Intent intent=new Intent();
				intent.putExtra(Constants.TOKENID, tokenId);
				if (tab == 0) {
					intent.setAction(Constants.MANAGER_EDUCATION_RECEIVE_ED);
				}else{
					intent.setAction(Constants.MANAGER_EDUCATION_RECEIVE_TR);
				}
				sendBroadcast(intent);
		        
				scrollToFinishActivity();
			}
		});
	}

	
	/**
	 * 
	 * @Title:InfoManagerActivity
	 * @Description: 管理我的工作经验
	 * @param holder
	 * @param commMapArray
	 * @param position
	 */
	private void setWEData(ViewHolder holder,Map<String, String[]> commMapArray,int position){
		holder.setText(R.id.item1,commMapArray.get("companyname")[position]);

		String info_jobtitleStr = commMapArray.get("jobtitle")[position];

		String info_companynatureStr = commMapArray.get("companynature")[position];
		String info_companyscaleStr = commMapArray.get("companyscale")[position];

		String info_industryclassificationStr = commMapArray.get("industryclassification")[position];

		String info_startworktimeStr = commMapArray.get("worktimestart")[position];
		String info_endworktimeStr = commMapArray.get("worktimeend")[position];
		String info_expectedsalaryStr = commMapArray.get("expectedsalary")[position];

		StringBuffer sbStr = new StringBuffer();
		if (RegexUtil.checkNotNull(info_industryclassificationStr)) {
			sbStr.append(info_industryclassificationStr );
		}

		if (RegexUtil.checkNotNull(info_companynatureStr)) {
			sbStr.append(" | " + info_companynatureStr);
		}

		if (RegexUtil.checkNotNull(info_companyscaleStr)) {
			sbStr.append(" | " + info_companyscaleStr);
		}

		StringBuffer sbStr2 = new StringBuffer();
		if (RegexUtil.checkNotNull(info_jobtitleStr)) {
			sbStr2.append(info_jobtitleStr);
		}
		if (RegexUtil.checkNotNull(info_expectedsalaryStr)) {
			sbStr2.append(" | " +info_expectedsalaryStr);
		}
		
		if (RegexUtil.checkNotNull(info_startworktimeStr)
				&& RegexUtil.checkNotNull(info_endworktimeStr)) {
			sbStr2.append(" | " + info_startworktimeStr + " — "
					+ info_endworktimeStr);
		}
		
		holder.setText(R.id.item11, sbStr.toString());
		
		holder.setText(R.id.item12, sbStr2.toString());
		
		final String tokenId = commMapArray.get("tokenId")[position];
		holder.setOnClickEvent(R.id.item21, new ClickEvent() {

			@Override
			public void onClick(View view) {
				DialogUtils.showDeleteDialog(self, tokenId, mHandler);
			}
		});

		holder.setOnClickEvent(R.id.item22, new ClickEvent() {

			@Override
			public void onClick(View view) {
				Intent intent=new Intent();
		        intent.putExtra(UserInfoCode.TOKENID, tokenId);
		        setResult(Constants.RESULT_CODE, intent);
				scrollToFinishActivity();
			}
		});
	}
	
	/**
	 * 
	 * @Title:InfoManagerActivity
	 * @Description: 管理我的项目经验
	 * @param holder
	 * @param commMapArray
	 * @param position
	 */
	private void setPEData(ViewHolder holder,Map<String, String[]> commMapArray,int position){
		holder.setText(R.id.item1,commMapArray.get("worktimestart")[position] + "--" + commMapArray.get("worktimeend")[position]);

		String info_dutiesStr = commMapArray.get("duties")[position];
		StringBuffer sbStr = new StringBuffer();
		sbStr.append("<font color=\"black\">");
		sbStr.append("责任描述：");
		sbStr.append(info_dutiesStr);
		sbStr.append("</font>");
		holder.setTextForHtml(R.id.item11, sbStr.toString());
		
		String info_prokectdescStr = commMapArray.get("prokectdesc")[position];
		sbStr = new StringBuffer();
		sbStr.append("<font color=\"black\">");
		sbStr.append("项目简介：");
		sbStr.append(info_prokectdescStr);
		sbStr.append("</font>");
		holder.setTextForHtml(R.id.item12, sbStr.toString());
		
		final String tokenId = commMapArray.get("tokenId")[position];
		holder.setOnClickEvent(R.id.item21, new ClickEvent() {

			@Override
			public void onClick(View view) {
				DialogUtils.showDeleteDialog(self, tokenId, mHandler);
			}
		});

		holder.setOnClickEvent(R.id.item22, new ClickEvent() {

			@Override
			public void onClick(View view) {
				Intent intent=new Intent();
		        intent.putExtra("tokenId", tokenId);
		        setResult(Constants.RESULT_CODE, intent);
				scrollToFinishActivity();
			}
		});
	}
	
	
	/**
	 * 
	 * @Description: 删除远端数据
	 */
	private void syncData(String weId,String procName){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_tokenId");
		params.add("p_userId");
		values.add(weId);
		values.add(uTokenId);
		requestData(procName, 2, params, values, new HandlerData() {
			@Override
			public void error() {
//				runOnUiThread(R.string.action_sync_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("msg").get(0).equals(ResponseCode.RESULT_OK)) {
						set3Msg(R.string.action_sync_success);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 
	 * @Title:InfoManagerActivity
	 * @Description: 从远端下载数据插入本地
	 * @return 返回类型
	 */
	private void getServerData(){
		String procName = "";
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_tokenId");
		params.add("p_userId");
		values.add("0");
		values.add(uTokenId);
		
		if (type.equals(CommonText.EDUCATION)) {
			tab = getIntent().getIntExtra(Constants.TAB, 0);
			L.d("==tab=="+tab);
			if (tab == 0) {
				procName = "pro_get_education";
			}else{
				procName = "pro_get_training";
			}
		}else if(type.equals(CommonText.WORKEXPERIENCE)){
			procName = "pro_get_workexpericnce";
		}else if (type.equals(CommonText.PROJECT_EXPERIENCE)) {
			procName = "pro_get_projectexpericnce";
		}
		
		requestData(procName, 3, params, values, new HandlerData() {
			@Override
			public void error() {
				setRightIcon(R.drawable.icon_sync);
				msgText.setText(CommUtil.getStrValue(self, R.string.en_nodata));
				msgText.setVisibility(View.VISIBLE);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (type.equals(CommonText.EDUCATION)) {
						tab = getIntent().getIntExtra(Constants.TAB, 0);
						L.d("==tab=="+tab);
						if (tab == 0) {
							setEDDataFromServer(map);
						}else{
							setTRDataFromServer(map);
						}
					}else if(type.equals(CommonText.WORKEXPERIENCE)){
						setWEDataFromServer(map);
					}else if (type.equals(CommonText.PROJECT_EXPERIENCE)) {
						setPEDataFromServer(map);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	/**
	 * 更新工作经验
	 * @param map
	 */
	private void setWEDataFromServer(Map<String, List<String>> map){
		int size = map.get("userId").size();
		for (int i = 0; i < size; i++) {
			ContentValues cValues = new ContentValues();
			cValues.put("tokenId", map.get("tokenId").get(i));
			cValues.put("userId", map.get("userId").get(i));
			cValues.put("companyname", map.get("companyname").get(i));
			cValues.put("companynature", map.get("companynature").get(i));
			cValues.put("companyscale", map.get("companyscale").get(i));
			cValues.put("industryclassification", map.get("industryclassification").get(i));
			cValues.put("jobtitle", map.get("jobtitle").get(i));
			cValues.put("worktimeStart", map.get("worktimeStart").get(i));
			cValues.put("worktimeEnd", map.get("worktimeEnd").get(i));
			cValues.put("expectedsalary", map.get("expectedsalary").get(i));
			cValues.put("workdesc", map.get("workdesc").get(i));
			cValues.put("bgcolor", map.get("bgcolor").get(i));
			cValues.put("createtime", map.get("createtime").get(i));
			cValues.put("updatetime", map.get("updatetime").get(i));
			queryResult = dbUtil.insertData(self, CommonText.WORKEXPERIENCE, cValues);
		}
		
		if (queryResult) {
			set3Msg(R.string.action_sync_success);
			initData(CommonText.WORKEXPERIENCE);
		}
	}
	
	/**
	 * 更新项目经验
	 * @param map
	 */
	private void setPEDataFromServer(Map<String, List<String>> map){
		int size = map.get("userId").size();
		for (int i = 0; i < size; i++) {
			ContentValues cValues = new ContentValues();
			cValues.put("tokenId", map.get("tokenId").get(i));
			cValues.put("userId", map.get("userId").get(i));
			cValues.put("projectname", map.get("projectname").get(i));
			cValues.put("worktimestart", map.get("worktimestart").get(i));
			cValues.put("worktimeend", map.get("worktimeend").get(i));
			cValues.put("duties", map.get("duties").get(i));
			cValues.put("prokectdesc", map.get("prokectdesc").get(i));
			cValues.put("createtime", map.get("createtime").get(i));
			queryResult = dbUtil.insertData(self, CommonText.PROJECT_EXPERIENCE, cValues);
		}
		
		if (queryResult) {
			set3Msg(R.string.action_sync_success);
			initData(CommonText.PROJECT_EXPERIENCE);
		}
	}
	
	/**
	 * 更新项目经验
	 * @param map
	 */
	private void setEDDataFromServer(Map<String, List<String>> map){
		int size = map.get("userId").size();
		for (int i = 0; i < size; i++) {
			ContentValues cValues = new ContentValues();
			cValues.put("tokenId", map.get("tokenId").get(i));
			cValues.put("userId", map.get("userId").get(i));
			cValues.put("projectname", map.get("projectname").get(i));
			cValues.put("worktimestart", map.get("worktimestart").get(i));
			cValues.put("worktimeend", map.get("worktimeend").get(i));
			cValues.put("duties", map.get("duties").get(i));
			cValues.put("prokectdesc", map.get("prokectdesc").get(i));
			cValues.put("createtime", map.get("createtime").get(i));
			queryResult = dbUtil.insertData(self, CommonText.EDUCATION, cValues);
		}
		
		if (queryResult) {
			set3Msg(R.string.action_sync_success);
			tab = 0;
			initData(CommonText.EDUCATION);
		}
	}
	
	/**
	 * 更新项目经验
	 * @param map
	 */
	private void setTRDataFromServer(Map<String, List<String>> map){
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
			queryResult = dbUtil.insertData(self, CommonText.EDUCATION_TRAIN, cValues);
		}
		
		if (queryResult) {
			set3Msg(R.string.action_sync_success);
			tab = 1;
			initData(CommonText.EDUCATION);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			if (!MyApplication.USERID.equals("0")) {
				if (CommUtil.isNetworkAvailable(self)) {
					set2Msg(R.string.action_syncing);
					getServerData();
				}else{
					set3Msg(R.string.check_network);
				}
			}else{
				set3Msg(R.string.action_login_head);
			}
			break;
		default:
			break;
		}
	}

}
