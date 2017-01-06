package com.me.resume.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.views.XListView;
import com.me.resume.views.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 更多面试分享心得
 * @author Administrator
 *
 */
public class ResumeShareMoreActivity extends CommLoadActivity implements OnClickListener,IXListViewListener{

	private RelativeLayout sharemore_layout;
	private XListView reviewsharemoreListView;
	
	private LinearLayout shareLayout;
	
	private LinearLayout inputshareLayout;
	private EditText input_share;
	private Button submit_btn;
	
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
					if(pos == 0){ //刷新
						if(commMapList != null){
							commMapList.clear();
						}
						commMapList.putAll(newMap);
						
						if (commapBaseAdapter != null) {
							commapBaseAdapter.notifyDataSetChanged();
							reviewsharemoreListView.invalidate();
						}else{
							setShareData(reviewsharemoreListView,commMapList);
						}
						reviewsharemoreListView.setPullLoadEnable(true);
					}else{//加载更多
						commMapList.putAll(CommUtil.getNewMap(commMapList, newMap));
						commapBaseAdapter.notifyDataSetChanged(pos);
						// reviewsharemoreListView.stopLoadMore();
						if(isAll){
							reviewsharemoreListView.stopLoadMore();
						}else{
							reviewsharemoreListView.setPullLoadEnable(true);
						}
					}
					reviewsharemoreListView.stopRefresh();
				}
				break;
			case 12:
				isAll=true;
				finishLoading();
				if(!isLoadMore){
					reviewsharemoreListView.setVisibility(View.GONE); 
					setMsgLayoutHide();
					setFlagShow(R.string.item_text72);
				}
				break;
			case 100:
				getShareMoreData(pos);
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
		View v = View.inflate(self,R.layout.resume_share_more_layout, null);
		commbodyLayout.addView(v);
		initView();
	}
	
	private void initView(){
		setTopTitle(R.string.item_text71);
		
		sharemore_layout = findView(R.id.sharemore_layout);
		reviewsharemoreListView = findView(R.id.reviewsharemoreListView);
		reviewsharemoreListView.setPullLoadEnable(true);
		reviewsharemoreListView.setXListViewListener(this);
		
		shareLayout = findView(R.id.shareLayout);
		inputshareLayout = findView(R.id.inputshareLayout);
		input_share = findView(R.id.input_share);
		submit_btn = findView(R.id.submit_btn);
		
		sharemore_layout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		submit_btn.setOnClickListener(this);
		
		commMapList = new HashMap<String, List<String>>();
		
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
	 * @Description: 面试分享心得
	 */
	private void getShareMoreData(int position){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_index");
		values.add(String.valueOf(position));
		
		requestData("pro_getshareinfo_bypage", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				setFlagHide();
				setMsgLayoutShow(R.string.timeout_network);
				reviewsharemoreListView.setVisibility(View.GONE);
			}
			
			public void success(Map<String, List<String>> map) {
				setFlagHide();
				setMsgLayoutHide();
				reviewsharemoreListView.setVisibility(View.VISIBLE);
				try {
					if (map.get("id").size() < 10) {
						isAll = true;
					}else{
						isAll = false;
					}
					mHandler.sendMessage(mHandler.obtainMessage(11, map));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				mHandler.sendEmptyMessage(12);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(inputshareLayout.getVisibility() == View.VISIBLE){
				shareLayout.setVisibility(View.VISIBLE);
				inputshareLayout.setVisibility(View.GONE);
			}else{
				scrollToFinishActivity();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.shareLayout:
			shareLayout.setVisibility(View.GONE);
			inputshareLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.reloadBtn:
			loadData();
			break;
		case R.id.submit_btn:
			if (!MyApplication.USERID.equals("0")) {
				String content = input_share.getText().toString().trim();
				if (RegexUtil.checkNotNull(content)) {
					if(content.trim().length() <= 512){
						shareLayout.setVisibility(View.VISIBLE);
						inputshareLayout.setVisibility(View.GONE);
						postShareData(content);
					}else{
						toastMsg(R.string.item_text82);
					}
				}
			}else{
				toastMsg(R.string.action_login_head);
			}
			break;
		case R.id.sharemore_layout:
			shareLayout.setVisibility(View.VISIBLE);
			inputshareLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 提交分享内容
	 * @param share
	 */
	private void postShareData(String share){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_userId");
		params.add("p_share");
		
		values.add(uTokenId);
		values.add(share);
		
		requestData("pro_setshareinfo", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				toastMsg(R.string.request_error);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if(map.get(ResponseCode.MSG).get(0).equals(ResponseCode.RESULT_OK)){
						toastMsg(R.string.item_text7);
						input_share.setText("");
						preferenceUtil.setPreferenceData(UserInfoCode.USERSHARE, true);// home data refresh
						getShareMoreData(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void onRefresh() {
		isLoadMore=false;
		pos = 0;
		if(!isRequest){
			getShareMoreData(pos);
		}
	}

	@Override
	public void onLoadMore() {
		isLoadMore=true;
		if(!isAll){
			pos++;
			getShareMoreData(pos);
		}else{
			//toastMsg(R.string.xlistview_footer_loadfinish);
			finishLoading();
		}
	}
	
	public void finishLoading(){
		reviewsharemoreListView.stopLoadMore();
		reviewsharemoreListView.stopRefresh();
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
}
