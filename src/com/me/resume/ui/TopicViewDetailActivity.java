package com.me.resume.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.CommScrollView;
import com.me.resume.views.CustomFAB;
import com.umeng.analytics.MobclickAgent;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: TopicActivity 
* @Description: 相关话题预览
* @date 2016/4/20 上午10:49:59 
*
 */
public class TopicViewDetailActivity extends BaseActivity implements OnClickListener{

	private CommScrollView viewlayout;
	
	private TextView msgText;
	private WebView webView;
	private CustomFAB top;
	
	private String topicId = "";
	private String title = "";
	private String type = "";
	private String detail = "";
	private String fromUrl = "";
	private String sitename = "";
	private String linksite = "";
	private String detailUrl = "";
	
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
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bodyLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_topic_view2_layout, null);
		bodyLayout.addView(v);
		
		findView();
		initView();
	}
	
	private void findView() {
		viewlayout = findView(R.id.viewlayout);
		msgText = findView(R.id.msgText);
		webView = findView(R.id.webView);
		top = findView(R.id.top);
		top.setOnClickListener(this);
		
		if (contentView == null) {
			contentView = viewlayout.getChildAt(0);
		}
		
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
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView(){
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		Bundle topicInfo = getIntent().getBundleExtra(Constants.TOPICINFO);
		if (topicInfo != null) {
			setTopicView(topicInfo);
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
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void setTopicView(Bundle bundle) {
		topicId = bundle.getString("topicId");
		title = bundle.getString("title");
		type = bundle.getString("type");
		// setTopTitle(getTitle(CommUtil.parseInt(type)));
		detail = bundle.getString("detail");
		fromUrl = bundle.getString("from_url");
		detailUrl = bundle.getString("detail_url"); // 请求url
		
		sitename = bundle.getString("site_name");
		linksite = bundle.getString("link_site");
		
		setTopTitle(getString(R.string.re_from) + sitename);
		
		webView.loadUrl(detailUrl);
		webView.setWebViewClient(new WebViewClient(){
	           @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	             view.loadUrl(url);
	            return true;
	        }
	           @Override  
	           public void onPageStarted(WebView view, String url, Bitmap favicon) {  
	        	   msgText.setVisibility(View.VISIBLE);  
	        	   webView.setVisibility(View.GONE);  
	           }  
	         
	           @Override  
	           public void onPageFinished(WebView view, String url) {  
	               super.onPageFinished(view, url);  
	               msgText.setVisibility(View.GONE);  
	               webView.setVisibility(View.VISIBLE);  
	           }  
	       });
		
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDisplayZoomControls(false); //隐藏webview缩放按钮
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true); 
		settings.setBuiltInZoomControls(true);
		settings.setSupportZoom(true);//设定支持缩放   
		  
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		if (mDensity == 240) {
			settings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == 160) {
			settings.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 120) {
			settings.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
			settings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == DisplayMetrics.DENSITY_TV) {
			settings.setDefaultZoom(ZoomDensity.FAR);
		} else {
			settings.setDefaultZoom(ZoomDensity.MEDIUM);
		}
		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);

		queryWhere = "select * from " + CommonText.MYCOLLECTION
				+ " where topicId = " + topicId + " and userId = '" + uTokenId
				+ "' and type >= 0";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray == null) {
			setRightIcon(R.drawable.icon_collection_small_nor);
		} else {
			setRightIcon(R.drawable.icon_collection_small_sel);
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.top_text:
			if (RegexUtil.checkNotNull(linksite)) {
				DialogUtils.showAlertDialog(self, 
						String.format(getStrValue(R.string.dialog_action_golink),sitename),
						View.GONE,
						getStrValue(R.string.show_button_continue), mHandler);
			}
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
					
					syncDelData(topicId);
				}
			}else{
				toastMsg(R.string.action_login_head);
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
		cValues.put("detail_url", detailUrl); // 对应的网站地址
		cValues.put("site_name", sitename); // 网站名
		cValues.put("link_site", linksite); // 对应的网站地址
		cValues.put("createtime", TimeUtils.getCurrentTimeInString());
		cValues.put("type", type);// 0:面试分享心得; !0:话题

		queryResult = dbUtil.insertData(self, CommonText.MYCOLLECTION, cValues);
		if (queryResult) {
			toastMsg(R.string.item_text9);
			setRightIcon(R.drawable.icon_collection_small_sel);
			
			// TODO 同步到远端
			setSyncData(new String[]{topicId,uTokenId,topicId,title,detail,fromUrl,detailUrl,sitename,linksite,"","","",type});
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
	/*private void syncData(){ 
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
	}*/
	
	/**
	 * 执行同步数据请求
	 * @param style 2：add 3.update
	 *//*
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
	}*/
	
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
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}
}
