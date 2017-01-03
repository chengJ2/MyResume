package com.me.resume.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
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
public class ResumeShareMoreActivity extends BaseActivity implements OnClickListener,IXListViewListener{

	private RelativeLayout sharemore_layout;
	private XListView reviewsharemoreListView;
	
	private TextView flag;
	
	private RelativeLayout msgLayout;
	private TextView msgText;
	private Button reloadBtn;
	
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
					}else{//加载更多
						commMapList.putAll(CommUtil.getNewMap(commMapList, newMap));
						commapBaseAdapter.notifyDataSetChanged(pos);
					}
					finishLoading();
				}
				break;
			case 12:
				isAll=true;
				finishLoading();
				if(!isLoadMore){
					reviewsharemoreListView.setVisibility(View.GONE); 
					msgLayout.setVisibility(View.GONE);
					flag.setText(getString(R.string.item_text72));
					flag.setVisibility(View.VISIBLE);
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
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.resume_share_more_layout, null);
		boayLayout.addView(v);
		initView();
	}
	
	private void initView(){
		setTopTitle(R.string.item_text71);
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		flag = findView(R.id.flag);
		
		msgLayout = (RelativeLayout)findView(R.id.msgLayout);
		msgText  = (TextView)findView(R.id.msgText);
		reloadBtn = (Button) findView(R.id.reloadBtn);
		reloadBtn.setOnClickListener(this);
		
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
			flag.setText(getStrValue(R.string.item_text43));
			flag.setVisibility(View.VISIBLE);
			mHandler.sendEmptyMessageDelayed(100, 200);
		}else{
			msgLayout.setVisibility(View.VISIBLE);
			msgText.setText(getString(R.string.check_network));
			reviewsharemoreListView.setVisibility(View.GONE);
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
				flag.setVisibility(View.GONE);
				msgLayout.setVisibility(View.VISIBLE);
				msgText.setText(getString(R.string.timeout_network));
				reviewsharemoreListView.setVisibility(View.GONE);
			}
			
			public void success(Map<String, List<String>> map) {
				reviewsharemoreListView.setVisibility(View.VISIBLE); 
				msgLayout.setVisibility(View.GONE);
				flag.setVisibility(View.GONE);
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
			toastMsg(R.string.xlistview_footer_loadfinish);
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
