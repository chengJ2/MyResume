package com.me.resume.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.XListView;
import com.me.resume.views.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 
* @ClassName: TopicListDetailActivity 
* @Description: 话题列表详情
* @date 2016/5/12 上午9:07:55 
*
 */
public class TopicListDetailActivity extends CommLoadActivity implements OnClickListener,IXListViewListener{

	private ViewHolder viewHolder;
	private XListView topicdetailListView;
	
	private boolean isAll=false;//是否加载完毕
	private int pos=0;
	private boolean isLoadMore=false;
	private boolean isRequest=false;
	
	private String typeStr = "";
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				Uri content_url = Uri.parse(linksite);
				intent.setData(content_url);
				startActivity(intent);
				break;
			case 13:
				if (msg.obj != null) {
					Map<String, List<String>> newMap = (Map<String, List<String>>)msg.obj;
					if(pos == 0){//刷新
						if(commMapList!=null){
							commMapList.clear();
						}
						commMapList.putAll(newMap);
						setTopicListData(commMapList);
						topicdetailListView.setPullLoadEnable(true);
					}else{//加载更多
						commMapList.putAll(getNewMap(commMapList, newMap));
						commapBaseAdapter.notifyDataSetChanged(pos);
						
						if(isAll){
							topicdetailListView.stopLoadMore();
						}else{
							topicdetailListView.setPullLoadEnable(true);
						}
					}
					topicdetailListView.stopRefresh();
				}
				break;
			case 12:
				isAll=true;
				finishLoading();
				if(!isLoadMore){
					topicdetailListView.setVisibility(View.GONE); 
					setMsgLayoutHide();
					setFlagShow(R.string.en_nodata);
				}
				break;
			case 100:
				getTopciListData(pos);
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		commbodyLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_topic_list_layout, null);
		commbodyLayout.addView(v);
		
		initView();
	}
	
	private void initView(){
		setTopTitle(R.string.item_text31);
		
		topicdetailListView = findView(R.id.topicdetailListView);
		topicdetailListView.setPullLoadEnable(true);
		topicdetailListView.setXListViewListener(this);
		
		commMapList = new HashMap<String, List<String>>();
		
		String topicId = getIntent().getStringExtra(Constants.TOPICID);
		if (RegexUtil.checkNotNull(topicId)) {
			setTopTitle(getTitle(CommUtil.parseInt(topicId)));
			typeStr = "="+topicId;
		}else{
			typeStr = "!=0"; // All Data
		}
		
		loadData();
	}
	
	private void loadData(){
		if (CommUtil.isNetworkAvailable(self)) {
			setFlagShow(R.string.item_text43);
			mHandler.sendEmptyMessageDelayed(100, 200);
		}else{
			setMsgLayoutShow(R.string.check_network);
		}
	}
	
	/**
	 * 
	 * 获取 title
	 * @return title 
	 */
	private String getTitle(int topicId){
		String[] item_text = CommUtil.getArrayValue(self,R.array.review_link_topics); 
		return item_text[topicId-1];
	}
	
	private void getTopciListData(int position){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_index");
		params.add("p_type");
		values.add(String.valueOf(position));
		values.add(typeStr);
		
		requestData("pro_gettopic_bypage", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				setFlagHide();
				setMsgLayoutShow(R.string.timeout_network);
				topicdetailListView.setVisibility(View.GONE);
			}
			
			public void success(Map<String, List<String>> map) {
				topicdetailListView.setVisibility(View.VISIBLE); 
				setFlagHide();
				setMsgLayoutHide();
				if (map.get("id").size() < 10) {
					isAll = true;
				}else{
					isAll = false;
				}
				mHandler.sendMessage(mHandler.obtainMessage(13, map));
			}

			@Override
			public void noData() {
				mHandler.sendEmptyMessage(12);
			}
		});
	}
	
	private String linksite = null;
	
	private void setTopicListData(final Map<String, List<String>> map){
		commapBaseAdapter = new CommForMapBaseAdapter(self,map,R.layout.topic_list_detail_item,"id") {
			
			@Override
			public void convert(ViewHolder holder, List<String> item, final int position) {
				final String title = map.get("title").get(position);
				final String detail = map.get("detail").get(position);
				final String fromUrl = map.get("from_url").get(position);
				//final String detailUrl = map.get("detail_url").get(position);
				final String createtime = map.get("createtime").get(position);
				final String sitename = map.get("site_name").get(position);
				linksite = map.get("link_site").get(position);
				
				if (!RegexUtil.checkNotNull(fromUrl)) {
					holder.setImageVisibe(R.id.topic_icon, View.GONE);
				}else{
					holder.setImageVisibe(R.id.topic_icon, View.VISIBLE);
					holder.showImage(R.id.topic_icon,CommUtil.getHttpLink(fromUrl),false);
				}
				holder.setText(R.id.topic_title, title);
				//holder.setTextForHtml(R.id.topic_content, CommUtil.getHtml(detail));
				holder.setText(R.id.topic_from, sitename);
				holder.setText(R.id.topic_datetime, TimeUtils.showTimeFriendly(createtime));
				
				viewHolder = holder;
				
				holder.setOnClickEvent(R.id.toplistitemlayout, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						String id = map.get("id").get(position);
						String type = map.get("type").get(position);
						String detailUrl = map.get("detail_part2").get(position);
						
						Bundle bundle = new Bundle();
						bundle.putString("topicId", id);
						bundle.putString("title", title);
						bundle.putString("type", type);
						bundle.putString("detail", detail);
						bundle.putString("from_url", fromUrl);
						bundle.putString("detail_url", detailUrl);
						bundle.putString("createtime", createtime);
						bundle.putString("site_name", sitename);
						bundle.putString("link_site", linksite);
						
						ActivityUtils.startActivityPro(self, 
								Constants.PACKAGENAMECHILD + Constants.TOPICVIEW, 
								Constants.TOPICINFO,bundle);
					}
				});
				
				holder.setOnClickEvent(R.id.topic_from, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						linksite  = map.get("link_site").get(position);
						if (RegexUtil.checkNotNull(linksite)) {
							DialogUtils.showAlertDialog(self, 
									String.format(getStrValue(R.string.dialog_action_golink),sitename),
									View.GONE,
									getStrValue(R.string.show_button_continue), mHandler);
						}
					}
				});
			}
		};
		
		topicdetailListView.setAdapter(commapBaseAdapter);
		
		topicdetailListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:
					if (viewHolder != null) {
						viewHolder.setFlagBusy(true);
					}
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (viewHolder != null) {
						viewHolder.setFlagBusy(false);
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					if (viewHolder != null) {
						viewHolder.setFlagBusy(false);
					}
					break;
				default:
					break;
				}
				commapBaseAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.reloadBtn:
			loadData();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		isLoadMore=false;
		pos=0;
		if(!isRequest){
			getTopciListData(pos);
		}
	}

	@Override
	public void onLoadMore() {
		isLoadMore=true;
		if(!isAll){
			pos++;
			getTopciListData(pos);
			//topicdetailListView.setPullLoadEnable(isLoadMore);
		}else{
			//toastMsg(R.string.xlistview_footer_loadfinish);
			finishLoading();
		}
	}
	
	/**
	 * 停止刷新
	 */
	public void finishLoading(){
		topicdetailListView.stopRefresh();
		topicdetailListView.stopLoadMore();
	}
	
	/**
	 * 将泛型一致且key相同的两个Map个并为一个新的map
	 * @param map1
	 * @param map2
	 * @return
	 */
	public Map<String,List<String>> getNewMap(Map<String,List<String>> map1,Map<String,List<String>> map2){
		Map<String,List<String>> map=new HashMap<String, List<String>>();
		Iterator<Entry<String, List<String>>> it=map1.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, List<String>> entry=it.next();
			entry.getValue().addAll(map2.get(entry.getKey()));
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
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
	}
}
