package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.tools.ImageLoader;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.CommScrollView;
import com.me.resume.views.CustomFAB;
import com.me.resume.views.MarqueeText;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: TopicActivity 
* @Description: 相关话题预览
* @date 2016/4/20 上午10:49:59 
*
 */
public class TopicViewActivity extends BaseActivity implements OnClickListener{

	private CommScrollView viewlayout;
	private TextView msgText;
	private MarqueeText topic_title;
	private TextView topic_from,topic_datetime;
	private ImageView topic_frompic,topic_frompic2;
	private TextView topic_content,topic_content2;
	private CustomFAB top;
	
	private ImageLoader mImageLoader;
	
	private String topicId = "";
	private String title = "";
	private String type = "";
	private String detail = "";
	private String fromUrl = "";
	private String createtime = "";
	private String sitename = "";
	private String linksite = "";

	private String detail2 = "";
	private String fromUrl2 = "";
//	private String detail3 = "";
//	private String fromUrl3 = "";
//	private String fromUrl4 = "";
//	private String fromUrl5 = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_topic_view_layout, null);
		boayLayout.addView(v);
		
		findView();
		initView();
	}
	
	private void findView() {
		msgText = findView(R.id.msgText);
		viewlayout = findView(R.id.viewlayout);
		
		if (contentView == null) {
			contentView = viewlayout.getChildAt(0);
		}
		
		msgText.setVisibility(View.VISIBLE);
		viewlayout.setVisibility(View.GONE);
		msgText.setText(getStrValue(R.string.item_text43));
		
		topic_title = findView(R.id.topic_title);
		topic_from = findView(R.id.topic_from);
		topic_from.setOnClickListener(this);
		topic_datetime = findView(R.id.topic_datetime);
		topic_frompic = findView(R.id.topic_frompic);
		topic_frompic2 = findView(R.id.topic_frompic2);
		topic_content = findView(R.id.topic_content);
		topic_content2 = findView(R.id.topic_content2);
		
		top = findView(R.id.top);
		top.setOnClickListener(this);
		
		viewlayout.setOnTouchListener(new OnTouchListener() {
			private int lastY = 0;
			private int touchEventId = -9983761;
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					View scroller = (View) msg.obj;
					if (msg.what == touchEventId) {
						if (lastY == scroller.getScrollY()) {
							handleStop(scroller);
						} else {
							handler.sendMessageDelayed(handler.obtainMessage(
									touchEventId, scroller), 5);
							lastY = scroller.getScrollY();
						}
					}
				}
			};

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					handler.sendMessageDelayed(
							handler.obtainMessage(touchEventId, v), 5);
				}
				return false;
			}

			/**
			 * ScrollView 停止
			 * 
			 * @param view
			 */
			private void handleStop(Object view) {
			
				ScrollView scroller = (ScrollView) view;
				scrollY = scroller.getScrollY();

				doOnBorderListener();
			}
		});
	}
	
	private void initView(){
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		if(mImageLoader == null)
			mImageLoader = new ImageLoader(self);
		
		Bundle topicInfo = getIntent().getBundleExtra(Constants.TOPICINFO);
		if (topicInfo != null) {
			setTopicView(topicInfo,null);
		}else{
			String topicidType = getIntent().getStringExtra(Constants.TOPICIDTYPE); 
			String[] titleArr = null;
			if (RegexUtil.checkNotNull(topicidType)) {
				titleArr = topicidType.split(";");
				
				topicId = titleArr[0];
				type = titleArr[1];
				
				setTopTitle(getTitle(CommUtil.parseInt(type)));
				
				if (CommUtil.isNetworkAvailable(self)) {
					getTopicData(topicId);
				}
			}
		}
	}
	
	/**
	 * 获取 title
	 * @return title 
	 */
	private String getTitle(int topicId){
		String[] item_text = CommUtil.getArrayValue(self,R.array.review_link_topics); 
		return item_text[topicId];
	}
	
	/**
	 * 显示话题详情内容
	 * @param bundle
	 */
	private void setTopicView(Bundle bundle, Map<String, List<String>> map) {
		viewlayout.setVisibility(View.VISIBLE);
		msgText.setVisibility(View.GONE);

		if (map == null) {
			topicId = bundle.getString("id");
			title = bundle.getString("title");
			type = bundle.getString("type");
			setTopTitle(getTitle(CommUtil.parseInt(type)));
			detail = bundle.getString("detail");
			fromUrl = bundle.getString("from_url");
			createtime = bundle.getString("createtime");
			sitename = bundle.getString("site_name");
			linksite = bundle.getString("link_site");

			detail2 = bundle.getString("detail2");
			fromUrl2 = bundle.getString("from_url2");
		} else {
			topicId = map.get("id").get(0);
			title = map.get("title").get(0);
			type = map.get("type").get(0);
			detail = map.get("detail").get(0);
			fromUrl = map.get("from_url").get(0);
			createtime = map.get("createtime").get(0);
			sitename = map.get("site_name").get(0);
			linksite = map.get("link_site").get(0);

			detail2 = map.get("detail_part2").get(0);
			fromUrl2 = map.get("from_url2").get(0);
		}

		topic_title.setText(title);
		topic_from.setText(sitename);
		topic_datetime.setText(TimeUtils.showTimeFriendly(createtime));
		if (RegexUtil.checkNotNull(fromUrl)) {
			topic_frompic.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(CommUtil.getHttpLink(fromUrl),
					topic_frompic, false, false);
		} else {
			topic_frompic.setVisibility(View.GONE);
		}

		topic_content.setText(Html.fromHtml(CommUtil.getHtml(detail)));

		if (RegexUtil.checkNotNull(fromUrl2)) {
			topic_frompic2.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(CommUtil.getHttpLink(fromUrl2),
					topic_frompic2, false, false);
		} else {
			topic_frompic2.setVisibility(View.GONE);
		}

		if (RegexUtil.checkNotNull(detail2)) {
			topic_content2.setVisibility(View.VISIBLE);
			topic_content2.setText(Html.fromHtml(CommUtil.getHtml(detail2)));
		} else {
			topic_frompic2.setVisibility(View.GONE);
		}

		queryWhere = "select * from " + CommonText.MYCOLLECTION
				+ " where cid = " + topicId + " and userId = '" + uTokenId
				+ "' and type >= 0";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray == null) {
			setRightIcon(R.drawable.icon_collection_small_nor);
		} else {
			setRightIcon(R.drawable.icon_collection_small_sel);
		}
	}
	
	
	/**
	 * 获取话题
	 */
	private void getTopicData(String topicId){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_id");
		values.add(topicId);
			
		requestData("pro_gettopic_byid", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				set3Msg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					setTopicView(null, map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void noData() {
				msgText.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			if (!MyApplication.USERID.equals("0")) {
				queryWhere = "select * from " + CommonText.MYCOLLECTION 
						+ " where cid = "+ topicId +" and userId = '"+ uTokenId+"' and type >= 0";
				commMapArray = dbUtil.queryData(self, queryWhere);
				if (commMapArray == null) {
					addCollection();
				}else{
					queryWhere = "delete from " + CommonText.MYCOLLECTION 
							+ " where cid = "+ topicId +" and userId = '"+ uTokenId+"' and type >= 0";
					dbUtil.deleteData(self, queryWhere);
					toastMsg(R.string.item_text91);
					setRightIcon(R.drawable.icon_collection_small_nor);
					MyCollectionActivity.loadFlag = true;
					
					syncDelData(topicId);
				}
			}else{
				toastMsg(R.string.action_login_head);
			}
			break;
		case R.id.topic_from:
			if (RegexUtil.checkNotNull(linksite)) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				Uri content_url = Uri.parse(linksite);
				intent.setData(content_url);
				startActivity(intent);
			}
			break;
		case R.id.top:
			top.setVisibility(View.GONE);
			viewlayout.post(new Runnable() {
	              public void run() {
	            	 viewlayout.fullScroll(ScrollView.FOCUS_UP);
	              }
	            });
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 添加到我的收藏
	 * @param map
	 * @param position
	 */
	private void addCollection(){
		if (RegexUtil.checkNotNull(detail) && detail.length() > 56) {
			detail = detail.substring(0, 56);
		}
		ContentValues cValues = new ContentValues();
		cValues.put("cId", topicId); // 收藏的标示
		cValues.put("userId", uTokenId);
		cValues.put("topicId", topicId);
		cValues.put("title", title);
		cValues.put("content", detail);
		cValues.put("from_url", fromUrl);// 第一张图
		cValues.put("site_name", sitename); // 网站名
		cValues.put("link_site", linksite); // 对应的网站地址
		cValues.put("createtime", TimeUtils.getCurrentTimeInString());
		cValues.put("type", type);// 0:面试分享心得; !0:话题

		queryResult = dbUtil.insertData(self, CommonText.MYCOLLECTION, cValues);
		if (queryResult) {
			toastMsg(R.string.item_text9);
			setRightIcon(R.drawable.icon_collection_small_sel);
			
			// TODO 同步到远端
			setSyncData(new String[]{topicId,uTokenId,topicId,title,detail,fromUrl,sitename,linksite,"","","",type});
		}
	}
	
	private View contentView;
	private int scrollY = 0;// 标记上次滑动位置
	
	/**
	 * ScrollView 的顶部，底部判断：
	 * 其中getChildAt表示得到ScrollView的child View， 因为ScrollView只允许一个child
	 * view，所以contentView.getMeasuredHeight()表示得到子View的高度,
	 * getScrollY()表示得到y轴的滚动距离，getHeight()为scrollView的高度。
	 * 当getScrollY()达到最大时加上scrollView的高度就的就等于它内容的高度了啊~
	 * 
	 * @param pos
	 */
	private void doOnBorderListener() {
		// 底部判断
		if (contentView != null
				&& contentView.getMeasuredHeight() <= viewlayout.getScrollY()
						+ viewlayout.getHeight()) {
			top.setVisibility(View.VISIBLE);
		}
		// 顶部判断
		else if (viewlayout.getScrollY() == 0) {
			top.setVisibility(View.GONE);
		}

		else if (viewlayout.getScrollY() > 30) {
			top.setVisibility(View.VISIBLE);
		}

	}
	
	/**
	 * 
	 * @Description: 同步数据(判断库是否存在记录)
	 */
	private void syncData(){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_cId");
		params.add("p_userId");
		values.add(topicId);
		values.add(uTokenId);
		
		requestData("pro_get_collection", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				set3Msg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
			}

			@Override
			public void noData() {
				syncRun(2);
			}
		});
	}
	
	/**
	 * 执行同步数据请求
	 * @param style 2：add 3.update
	 */
	private void syncRun(int style){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_cId");
		params.add("p_userId");
		params.add("p_topicId");
		params.add("p_title");
		params.add("p_content");
		params.add("p_from_url");
		params.add("p_topic_from");
		params.add("p_shareUserId");
		params.add("p_sharename");
		params.add("p_sharenamecity");
		params.add("p_type");
		
		values.add(topicId);
		values.add(uTokenId);
		values.add(topicId);
		values.add(title);
		values.add(detail);
		values.add(fromUrl);
		values.add(sitename);
		values.add("");
		values.add("");
		values.add("");
		values.add(type);
		
		requestData("pro_set_collection", style, params, values, new HandlerData() {
			@Override
			public void error() {
				set3Msg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get(ResponseCode.MSG).get(0).equals(ResponseCode.RESULT_OK)) {
						set3Msg(R.string.action_sync_success);
					}
				} catch (Exception e) {
					set3Msg(R.string.action_sync_fail);
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				set3Msg(R.string.action_sync_fail);
			}
		});
	}
	
}
