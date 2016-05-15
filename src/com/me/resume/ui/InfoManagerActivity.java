package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.whjz.android.text.CommonText;

/**
 * 栏目管理界面
 * 
 * @author Administrator
 * 
 */
public class InfoManagerActivity extends BaseActivity {

	private ListView infoMoreListView;
	private TextView nodata;

	private CommForMapArrayBaseAdapter commMapAdapter = null;
	
	private Map<String, String[]> commMapArray = null;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String weId = (String)msg.obj;
				queryWhere = "delete from " + CommonText.WORKEXPERIENCE
						+ " where userId = '" + uTokenId +"' and id = " + weId;
				dbUtil.deleteData(self, queryWhere);
				
				set3Msg(R.string.action_delete_success);
				
				initData();
				if (!MyApplication.userId.equals("0")) {
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
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);

		infoMoreListView = findView(R.id.infoMoreListView);
		nodata = findView(R.id.nodata);
		nodata.setText(CommUtil.getStrValue(self, R.string.item_text43));
		nodata.setVisibility(View.VISIBLE);

		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	private void initData() {
		queryWhere = "select * from " + CommonText.WORKEXPERIENCE
				+ " where userId = '" + uTokenId + "' order by id desc";
		commMapArray = dbUtil.queryData(self,queryWhere);
		
		if (commMapArray != null) {
			String userId = commMapArray.get("userId")[0];
			if (RegexUtil.checkNotNull(userId)) {
				commMapAdapter = new CommForMapArrayBaseAdapter(self, commMapArray,
						R.layout.manage_info_list_item, "userId") {

					@Override
					public void convert(ViewHolder holder, String[] item,
							int position) {
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
							sbStr.append(info_industryclassificationStr + " | ");
						}
						if (RegexUtil.checkNotNull(info_jobtitleStr)) {
							sbStr.append(info_jobtitleStr + " | ");
						}
						if (RegexUtil.checkNotNull(info_expectedsalaryStr)) {
							sbStr.append(info_expectedsalaryStr + " | ");
						}

						if (RegexUtil.checkNotNull(info_companynatureStr)) {
							sbStr.append(info_companynatureStr + " | ");
						}

						if (RegexUtil.checkNotNull(info_companyscaleStr)) {
							sbStr.append(info_companyscaleStr + " | ");
						}

						if (RegexUtil.checkNotNull(info_startworktimeStr)
								&& RegexUtil.checkNotNull(info_endworktimeStr)) {
							sbStr.append(info_startworktimeStr + " 至 "
									+ info_endworktimeStr);
						}

						holder.setText(R.id.item11, sbStr.toString());

						final String weId = commMapArray.get("id")[position];
						holder.setOnClickEvent(R.id.item21, new ClickEvent() {

							@Override
							public void onClick(View view) {
//								MyApplication.KID = CommUtil.parseInt(weId);
								DialogUtils.showDeleteDialog(self, weId, mHandler);
							}
						});

						holder.setOnClickEvent(R.id.item22, new ClickEvent() {

							@Override
							public void onClick(View view) {
								Intent intent=new Intent();
						        intent.putExtra("weId", weId);
						        setResult(Constants.RESULT_CODE, intent);
								scrollToFinishActivity();
							}
						});
					}
				};

				infoMoreListView.setAdapter(commMapAdapter);
				nodata.setVisibility(View.GONE);
			}else{
				nodata.setText(CommUtil.getStrValue(self, R.string.en_nodata));
				nodata.setVisibility(View.VISIBLE);
			}
		} else {
			nodata.setText(CommUtil.getStrValue(self, R.string.en_nodata));
			nodata.setVisibility(View.VISIBLE);
		}
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

}
