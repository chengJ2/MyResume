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
	private TextView nodata;

	private CommForMapArrayBaseAdapter commMapAdapter = null;
	
	private Map<String, String[]> commMapArray = null;
	
	private String type = "";
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String weId = (String)msg.obj;
				queryWhere = "delete from " + CommonText.WORKEXPERIENCE
						+ " where userId = '" + uTokenId +"' and tokenId = " + weId;
				dbUtil.deleteData(self, queryWhere);
				
				set3Msg(R.string.action_delete_success);
				
				initData(CommonText.WORKEXPERIENCE);
				
				// TODO
				if (!MyApplication.USERID.equals("0")) {
					if (CommUtil.isNetworkAvailable(self)) {
						syncData(weId);
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

		setTopTitle(R.string.resume_workexperience);

		setMsgHide();
		setRightIconVisible(View.VISIBLE);
		setRightIcon(R.drawable.icon_sync);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);

		infoMoreListView = findView(R.id.infoMoreListView);
		nodata = findView(R.id.nodata);
		nodata.setText(CommUtil.getStrValue(self, R.string.item_text43));
		nodata.setVisibility(View.VISIBLE);

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
			
		}else if(type.equals(CommonText.WORKEXPERIENCE)){
			layoutID = R.layout.manage_info_list_item;
			tableName = CommonText.WORKEXPERIENCE;
		}else if (type.equals(CommonText.PROJECT_EXPERIENCE)) {
			tableName = CommonText.PROJECT_EXPERIENCE;
		}
		queryWhere = "select * from " + tableName + " where userId = '" + uTokenId + "' order by createtime desc";
		commMapArray = dbUtil.queryData(self,queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length>0) {
				commMapAdapter = new CommForMapArrayBaseAdapter(self, commMapArray,
						layoutID, "userId") {

					@Override
					public void convert(ViewHolder holder, String[] item,
							int position) {
						if (type.equals(CommonText.EDUCATION)) {
							
						}else if(type.equals(CommonText.WORKEXPERIENCE)){
							setWEData(holder,commMapArray,position);
						}else if (type.equals(CommonText.PROJECT_EXPERIENCE)) {
							tableName = CommonText.PROJECT_EXPERIENCE;
						}
					}
				};

				infoMoreListView.setAdapter(commMapAdapter);
				nodata.setVisibility(View.GONE);
			}else{
				nodata.setText(CommUtil.getStrValue(self, R.string.en_nodata));
				nodata.setVisibility(View.VISIBLE);
			}
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
		        intent.putExtra("tokenId", tokenId);
		        setResult(Constants.RESULT_CODE, intent);
				scrollToFinishActivity();
			}
		});
	}
	
	/**
	 * 
	 * @Description: 删除远端数据
	 * @author Comsys-WH1510032
	 */
	private void syncData(String weId){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_weId");
		params.add("p_userId");
		values.add(weId);
		values.add(uTokenId);
		requestData("pro_get_workexpericnce", 2, params, values, new HandlerData() {
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
	
	private void getServerData(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_tokenId");
		params.add("p_userId");
		values.add("0");
		values.add(uTokenId);
		requestData("pro_get_workexpericnce", 3, params, values, new HandlerData() {
			@Override
			public void error() {
				set3Msg(R.string.action_sync_success);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					// 删除本地数据
//					queryWhere = "delete from " + CommonText.WORKEXPERIENCE;
//					dbUtil.deleteData(self, queryWhere);
					
					// 更新本地数据
					setDataFromServer(map);
					
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
	private void setDataFromServer(Map<String, List<String>> map){
		queryWhere = "select * from " + CommonText.WORKEXPERIENCE
				+ " where userId = '" + uTokenId + "' order by id desc";
		commMapArray = dbUtil.queryData(self,queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			// TODO
		}else{
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
	}
	
	@Override
	public void onClick(View v) {
//		super.onClick(v);
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
