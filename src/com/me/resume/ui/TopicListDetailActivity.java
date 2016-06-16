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
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.XListView;
import com.me.resume.views.XListView.IXListViewListener;

/**
 * 
* @ClassName: TopicListDetailActivity 
* @Description: 话题列表详情
* @date 2016/5/12 上午9:07:55 
*
 */
public class TopicListDetailActivity extends BaseActivity implements OnClickListener,IXListViewListener{

	private XListView topicdetailListView;
	private TextView nodata;
	
	private boolean isAll=false;//是否加载完毕
	private int pos=0;
	private boolean isLoadMore=false;
	private boolean isRequest=false;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				if (msg.obj != null) {
					Map<String, List<String>> newMap = (Map<String, List<String>>)msg.obj;
					if(pos == 0){//刷新
						if(commMapList!=null){
							commMapList.clear();
						}
						commMapList.putAll(newMap);
						setTopicListData(commMapList);
					}else{//加载更多
						commMapList.putAll(getNewMap(commMapList, newMap));
						commapBaseAdapter.notifyDataSetChanged(pos);
					}
					finishLoading();
				}
				break;
			case 12:
				isAll=true;
				finishLoading();
				if(!isLoadMore){
					topicdetailListView.setVisibility(View.GONE); 
					nodata.setVisibility(View.VISIBLE);
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
		
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_topic_list_layout, null);
		boayLayout.addView(v);
		
		topicdetailListView = findView(R.id.topicdetailListView);
		topicdetailListView.setPullLoadEnable(true);
		topicdetailListView.setXListViewListener(this);
		
		nodata = findView(R.id.nodata);
		nodata.setText(getStrValue(R.string.item_text43));
		nodata.setVisibility(View.VISIBLE);
		
		commMapList = new HashMap<String, List<String>>();
		
		initView();
	}
	
	String typeStr = "";
	
	private void initView(){
		
		setTopTitle(R.string.item_text31);
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		
		String title = getIntent().getStringExtra("title");
		String[] titleArr = null;
		if (RegexUtil.checkNotNull(title)) {
			titleArr = title.split(";");
			setTopTitle(titleArr[0]);
			typeStr = "="+titleArr[1];
		}else{
			typeStr = "!=0";
		}
		mHandler.sendEmptyMessageDelayed(100, 200);
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
				
			}
			
			public void success(Map<String, List<String>> map) {
				if (map.get("id").size() < 3) {
					isAll = true;
				}else{
					isAll = false;
				}
				mHandler.sendMessage(mHandler.obtainMessage(11, map));
				
			}

			@Override
			public void nodata() {
				mHandler.sendEmptyMessage(12);
			}
		});
	}
	
	private ViewHolder viewHolder;
	
	private void setTopicListData(final Map<String, List<String>> map){
		commapBaseAdapter = new CommForMapBaseAdapter(self,map,R.layout.topic_list_detail_item,"id") {
			
			@Override
			public void convert(ViewHolder holder, List<String> item, final int position) {
				String fromUrl = map.get("from_url").get(position);
				if (!RegexUtil.checkNotNull(fromUrl)) {
					holder.setImageVisibe(R.id.topic_icon, View.GONE);
				}else{
					holder.setImageVisibe(R.id.topic_icon, View.VISIBLE);
					holder.showImage(R.id.topic_icon,CommUtil.getHttpLink(fromUrl),false);
				}
				holder.setText(R.id.topic_title, map.get("title").get(position));
				holder.setText(R.id.topic_content, map.get("detail").get(position));
				holder.setText(R.id.topic_from, map.get("from").get(position));
				
				
				holder.setText(R.id.topic_datime, TimeUtils.showTimeFriendly(map.get("createtime").get(position)));
				
				viewHolder = holder;
				
				holder.setOnClickEvent(R.id.toplistitemlayout, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						String topicId = map.get("id").get(position);
						String type = map.get("type").get(position);
						ActivityUtils.startActivityPro(self, 
								Constants.PACKAGENAMECHILD + Constants.TOPICVIEW, "tidtype",
								topicId + ";" +type);
					}
				});
				
				holder.setOnClickEvent(R.id.topic_from, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						String fromLink = map.get("from_link").get(position);
						if (RegexUtil.checkNotNull(fromLink)) {
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							Uri content_url = Uri.parse(fromLink);
							intent.setData(content_url);
							startActivity(intent);
						}
					}
				});
			}
		};
		
		topicdetailListView.setAdapter(commapBaseAdapter);
		topicdetailListView.setVisibility(View.VISIBLE); 
		nodata.setVisibility(View.GONE);
		
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
		}else{
			toastMsg(R.string.xlistview_footer_loadfinish);
			finishLoading();
		}
	}
	
	/**
	 * 停止刷新
	 */
	public void finishLoading(){
		topicdetailListView.stopLoadMore();
		topicdetailListView.stopRefresh();
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
	protected void onPause() {
		super.onPause();
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
