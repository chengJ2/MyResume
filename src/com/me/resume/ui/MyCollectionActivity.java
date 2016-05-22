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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: MyCollectionActivity 
* @Description: 个人收藏 
* @date 2016/5/20 上午10:28:49 
*
 */
public class MyCollectionActivity extends BaseActivity implements OnClickListener{

	private ListView collectionListView;
	private TextView nodata;

	private CommForMapArrayBaseAdapter commMapAdapter = null;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String weId = (String)msg.obj;
				queryWhere = "delete from " + CommonText.MYCOLLECTION
						+ " where userId = '" + uTokenId +"' and tokenId = " + weId;
				dbUtil.deleteData(self, queryWhere);
				
				set3Msg(R.string.action_delete_success);
				
				initData();
				
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
		View v = View.inflate(self, R.layout.my_collection_layout, null);
		boayLayout.addView(v);

		setTopTitle(R.string.personal_c_item1);

		setMsgHide();
		setRightIconVisible(View.VISIBLE);
		setRightIcon(R.drawable.icon_sync);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);

		collectionListView = findView(R.id.collectionListView);
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
		queryWhere = "select * from " + CommonText.MYCOLLECTION + " where userId = '" + uTokenId + "' order by createtime desc";
		commMapArray = dbUtil.queryData(self,queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length>0) {
				commMapAdapter = new CommForMapArrayBaseAdapter(self, commMapArray,
						R.layout.topic_list_detail_item, "userId") {

					@Override
					public void convert(ViewHolder holder, String[] item,
							int position) {
						setCollectionData(holder,commMapArray,position);
					}
				};

				collectionListView.setAdapter(commMapAdapter);
				nodata.setVisibility(View.GONE);
			}else{
				nodata.setText(CommUtil.getStrValue(self, R.string.en_nodata));
				nodata.setVisibility(View.VISIBLE);
			}
	}
	
	/**
	 * 
	 * @Title:InfoManagerActivity
	 * @Description: 我的收藏
	 * @param holder
	 * @param commMapArray
	 * @param position
	 */
	private void setCollectionData(ViewHolder holder,final Map<String, String[]> commMapArray,int position){
		String type = commMapArray.get("type")[position];//  0:面试分享心得;  !0:话题
		if ("0".equals(type)) {
			holder.setImageVisibe(R.id.topic_icon, View.GONE);
			holder.setText(R.id.topic_content, commMapArray.get("content")[position]);
			holder.setText(R.id.topic_title, "来自 " + commMapArray.get("sharename")[position] + " 的分享");
			holder.setText(R.id.topic_from, "面试心得"); // TODO 
			holder.setText(R.id.topic_datime, commMapArray.get("createtime")[position]);
		}else{
			String fromUrl = commMapArray.get("from_url")[position];
			if (RegexUtil.checkNotNull(fromUrl)) {
				holder.setImageVisibe(R.id.topic_icon, View.GONE);
			}else{
				holder.setImageVisibe(R.id.topic_icon, View.VISIBLE);
				holder.showImage(R.id.topic_icon,CommUtil.getHttpLink(fromUrl),false);
			}
			
			holder.setText(R.id.topic_title, commMapArray.get("title")[position]);
			holder.setText(R.id.topic_content, commMapArray.get("content")[position]);
			holder.setText(R.id.topic_from, commMapArray.get("topic_from")[position]);
			holder.setText(R.id.topic_datime, commMapArray.get("sharedatime")[position]);
		}
		
		collectionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String type = commMapArray.get("type")[position];
				if ("0".equals(type)) {
					// 取消关注
				}else{
					// 跳转到详情
					String topicId = commMapArray.get("topicId")[position];
					
				}
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
				initData();
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			/*if (!MyApplication.USERID.equals("0")) {
				if (CommUtil.isNetworkAvailable(self)) {
					set2Msg(R.string.action_syncing);
					getServerData();
				}else{
					set3Msg(R.string.check_network);
				}
			}else{
				set3Msg(R.string.action_login_head);
			}*/
			break;
		default:
			break;
		}
	}
	
}
