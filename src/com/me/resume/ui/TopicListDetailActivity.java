package com.me.resume.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.utils.CommUtil;
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
						finishLoading();
					}
				}
				break;
			case 12:
				isAll=true;
				toastMsg(R.string.en_nodata);
				finishLoading();
				if(!isLoadMore){
					topicdetailListView.setVisibility(View.GONE); 
					nodata.setVisibility(View.VISIBLE);
				}
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
		View v = View.inflate(self,R.layout.activity_topic_list_layout, null);
		boayLayout.addView(v);
		
		topicdetailListView = findView(R.id.topicdetailListView);
		topicdetailListView.setPullLoadEnable(true);
		topicdetailListView.setXListViewListener(this);
		
		nodata = findView(R.id.nodata);
		
		commMapList = new HashMap<String, List<String>>();
		
		initView();
	}
	
	private void initView(){
		
//		setTopTitle(titleArr[0]);
		
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		getTopciListData(pos);
	}
	
	private void getTopciListData(int position){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_index");
		values.add(String.valueOf(position));
		
		requestData("pro_getAlltopic", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				mHandler.sendEmptyMessage(12);
			}
			
			public void success(Map<String, List<String>> map) {
				if (map.get("id").size() < 10) {
					isAll = true;
				}else{
					isAll = false;
				}
				mHandler.sendEmptyMessage(11);
				
			}
		});
	}
	
	private ViewHolder viewHolder;
	
	private void setTopicListData(final Map<String, List<String>> map){
		topicdetailListView.setVisibility(View.VISIBLE); 
		nodata.setVisibility(View.GONE);
		commapBaseAdapter = new CommForMapBaseAdapter(self,map,R.layout.topic_list_detail_item,"id") {
			
			@Override
			public void convert(ViewHolder holder, List<String> item, int position) {
				holder.showImage(R.id.topic_icon,
						CommUtil.getHttpLink(map.get("topic_icon").get(position)),false);
				holder.setText(R.id.topic_title, map.get("topic_title").get(position));
				holder.setText(R.id.topic_content, map.get("topic_content").get(position));
				holder.setText(R.id.topic_from, map.get("topic_from").get(position));
				holder.setText(R.id.topic_datime, map.get("topic_datime").get(position));
				
				viewHolder = holder;
			}
		};
		
		topicdetailListView.setAdapter(commapBaseAdapter);
		topicdetailListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		topicdetailListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:
					viewHolder.setFlagBusy(true);
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					viewHolder.setFlagBusy(false);
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					viewHolder.setFlagBusy(false);
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
	
	public void finishLoading(){
		topicdetailListView.stopLoadMore();
		topicdetailListView.stopRefresh();
		topicdetailListView.setRefreshTime(TimeUtils.getCurrentTimeInString());
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
}
