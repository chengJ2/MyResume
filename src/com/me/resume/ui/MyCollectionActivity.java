package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;
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
	
	private String cid;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				if(flag == 1){
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					Uri content_url = Uri.parse(linksite);
					intent.setData(content_url);
					startActivity(intent);
				}else{
					queryWhere = "delete from " + CommonText.MYCOLLECTION
							+ " where userId = '" + uTokenId +"' and cId = " + cid;
					dbUtil.deleteData(self, queryWhere);
					set3Msg(R.string.action_delete_success);
					initData();
					syncDelData(cid);
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
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);

		collectionListView = findView(R.id.collectionListView);
		nodata = findView(R.id.nodata);
		nodata.setText(getStrValue(R.string.item_text43));
		nodata.setVisibility(View.VISIBLE);
		
	}
	
	private void initData() {
		queryWhere = "select * from " + CommonText.MYCOLLECTION
				+ " where userId = '" + uTokenId + "' order by createtime desc";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			localHasData = true;
			setRightIconVisible(View.GONE);
			commMapArrayAdapter = new CommForMapArrayBaseAdapter(self,
					commMapArray, R.layout.topic_list_detail_item, "userId") {

				@Override
				public void convert(ViewHolder holder, String[] item,
						int position) {
					setCollectionData(holder, commMapArray, position);
				}
			};
			collectionListView.setVisibility(View.VISIBLE);
			collectionListView.setAdapter(commMapArrayAdapter);
			nodata.setVisibility(View.GONE);
		} else {
			localHasData = false;
			setRightIcon(R.drawable.icon_sync);
			nodata.setText(getStrValue(R.string.en_nodata));
			nodata.setVisibility(View.VISIBLE);
		}
	}
	
	private String sitename,linksite = null;
	private int flag = 0;
	
	/**
	 * @Description: 我的收藏
	 * @param holder
	 * @param commMapArray
	 * @param position
	 */
	private void setCollectionData(ViewHolder holder,final Map<String, String[]> commMapArray,final int position){
		final int type = CommUtil.parseInt(commMapArray.get("type")[position]);//  <0:面试分享心得;  >0:话题
		if (type < 0) {
			holder.setImageVisibe(R.id.topic_icon, View.GONE);
			holder.setText(R.id.topic_content, commMapArray.get("content")[position]);
			
			StringBuffer sbStr = new StringBuffer();
			sbStr.append("来自  ");
			sbStr.append("<font color=\"red\">");
			sbStr.append(commMapArray.get("sharename")[position]);
			sbStr.append("</font>");
			sbStr.append(" 的分享");
			
			holder.setTextForHtml(R.id.topic_title, sbStr.toString());
			
			holder.setText(R.id.topic_from, "面试心得"); // TODO 
			holder.setTextColor(R.id.topic_from, getColorValue(R.color.grey_10));
			holder.setText(R.id.topic_datetime, TimeUtils.showTimeFriendly(commMapArray.get("createtime")[position]));
		}else{
			String fromUrl = commMapArray.get("from_url")[position];
			if (!RegexUtil.checkNotNull(fromUrl)) {
				holder.setImageVisibe(R.id.topic_icon, View.GONE);
			}else{
				holder.setImageVisibe(R.id.topic_icon, View.VISIBLE);
				holder.showImage(R.id.topic_icon,CommUtil.getHttpLink(fromUrl),false);
			}
			sitename = commMapArray.get("site_name")[position];
			holder.setText(R.id.topic_title, commMapArray.get("title")[position]);
			holder.setText(R.id.topic_content, commMapArray.get("content")[position]);
			holder.setText(R.id.topic_from, sitename);
			holder.setText(R.id.topic_datetime, TimeUtils.showTimeFriendly(commMapArray.get("createtime")[position]));
			
		}
		holder.setOnClickEvent(R.id.topic_from, new ClickEvent() {
			
			@Override
			public void onClick(View view) {
				if (type >= 0) {
					flag = 1;
					linksite = commMapArray.get("link_site")[position];
					sitename = commMapArray.get("site_name")[position];
					if (RegexUtil.checkNotNull(linksite)) {
						DialogUtils.showAlertDialog(self, 
								String.format(getStrValue(R.string.dialog_action_golink),sitename),
								View.GONE,
								getStrValue(R.string.show_button_continue), mHandler);
					}
				}
			}
		});
		
		collectionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int type = CommUtil.parseInt(commMapArray.get("type")[position]);
				if (type < 0) {
					/*cid = commMapArray.get("cId")[position];
					DialogUtils.showAlertDialog(self, 
							getStrValue(R.string.dialog_action_topic_delete_sure_alert),View.GONE,
							getStrValue(R.string.show_button_sure),mHandler);*/
				}else{
					// 跳转到详情
					/*String topicId = commMapArray.get("topicId")[position];
					ActivityUtils.startActivityPro(self, 
							Constants.PACKAGENAMECHILD + Constants.TOPICVIEW,
							Constants.TOPICIDTYPE, topicId + ";" + type,false);*/
					String tid = commMapArray.get("id")[position];
					String title = commMapArray.get("title")[position];
					String detail = commMapArray.get("content")[position];
					String fromUrl = commMapArray.get("from_url")[position];
					String detailUrl = commMapArray.get("detail_url")[position];
					
					String createtime = commMapArray.get("createtime")[position];
					String sitename = commMapArray.get("site_name")[position];
					String linksite = commMapArray.get("link_site")[position];
					
					Bundle bundle = new Bundle();
					bundle.putString("id", tid);
					bundle.putString("title", title); // 标题
					bundle.putString("type", String.valueOf(type));
					bundle.putString("detail", detail); // 简介
					bundle.putString("from_url", fromUrl); // 文章头像
					bundle.putString("detail_url", detailUrl);// 文章请求网址
					bundle.putString("createtime", createtime);
					bundle.putString("site_name", sitename);
					bundle.putString("link_site", linksite);
					
					ActivityUtils.startActivityPro(self, 
							Constants.PACKAGENAMECHILD + Constants.TOPICVIEW, 
							Constants.TOPICINFO,bundle);
				}
			}
		});
		
		collectionListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int type = CommUtil.parseInt(commMapArray.get("type")[position]);
				if (type < 0) {
					flag = 0;
					cid = commMapArray.get("cId")[position];
					DialogUtils.showAlertDialog(self, 
							getStrValue(R.string.dialog_action_topic_delete_sure_alert),View.GONE,
							getStrValue(R.string.show_button_sure),mHandler);
					return true;
				}
				return false;
			}
		});
	}
	
	/**
	 * 获取server数据
	 */
	private void getServerData(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_cId");
		params.add("p_userId");
		values.add("0");
		values.add(uTokenId);
		requestData("pro_get_collection", 2, params, values, new HandlerData() {
			@Override
			public void error() {
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					setDataFromServer(map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				setMsgHide();
				nodata.setText(getStrValue(R.string.en_nodata));
				nodata.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	/**
	 * 更新本地数据
	 * @param map
	 */
	private void setDataFromServer(Map<String, List<String>> map){
		int size = map.get("userId").size();
		for (int i = 0; i < size; i++) {
			ContentValues cValues = new ContentValues();
			cValues.put("cId", getServerKeyValue(map, "cId",i));
			cValues.put("userId", getServerKeyValue(map,"userId",i));
			cValues.put("topicId", getServerKeyValue(map,"topicId",i));
			cValues.put("title", getServerKeyValue(map,"title",i));
			cValues.put("content", getServerKeyValue(map,"content",i));
			cValues.put("site_name", getServerKeyValue(map,"site_name",i));
			cValues.put("from_url", getServerKeyValue(map,"from_url",i));
			cValues.put("link_site", getServerKeyValue(map,"link_site",i));
			cValues.put("shareUserId", getServerKeyValue(map,"shareUserId",i));
			cValues.put("sharename", getServerKeyValue(map,"sharename",i));
			cValues.put("sharenamecity", getServerKeyValue(map,"sharenamecity",i));
			cValues.put("createtime", getServerKeyValue(map,"createtime",i));
			cValues.put("type", getServerKeyValue(map,"type",i));
			
			queryResult = dbUtil.insertData(self, CommonText.MYCOLLECTION, cValues);
		}
		
		if (queryResult) {
			set3Msg(R.string.action_sync_success);
			initData();
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
				if (!localHasData) {
					if (CommUtil.isNetworkAvailable(self)) {
						set2Msg(R.string.action_syncing);
						getServerData();
					}else{
						set3Msg(R.string.check_network);
					}
				}
			}else{
				set3Msg(R.string.action_login_head);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initData();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
