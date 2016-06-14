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
import com.me.resume.R;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.views.XListView;
import com.me.resume.views.XListView.IXListViewListener;

/**
 * 更多面试分享心得
 * @author Administrator
 *
 */
public class ResumeShareMoreActivity extends BaseActivity implements OnClickListener,IXListViewListener{

	private RelativeLayout sharemore_layout;
	private XListView reviewsharemoreListView;
	
	private TextView msgtext;
	
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
					msgtext.setVisibility(View.GONE);
					reviewsharemoreListView.setVisibility(View.VISIBLE);
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
					msgtext.setVisibility(View.VISIBLE);
					msgtext.setText(getStrValue(R.string.item_text72));
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
		
		setTopTitle(R.string.item_text71);
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		sharemore_layout = findView(R.id.sharemore_layout);
		reviewsharemoreListView = findView(R.id.reviewsharemoreListView);
		reviewsharemoreListView.setPullLoadEnable(true);
		reviewsharemoreListView.setXListViewListener(this);
		msgtext = findView(R.id.msgtext);
		shareLayout = findView(R.id.shareLayout);
		inputshareLayout = findView(R.id.inputshareLayout);
		input_share = findView(R.id.input_share);
		submit_btn = findView(R.id.submit_btn);
		
		sharemore_layout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		submit_btn.setOnClickListener(this);
		
		commMapList = new HashMap<String, List<String>>();
		
		if (CommUtil.isNetworkAvailable(self)) {
			mHandler.sendEmptyMessageDelayed(100, 200);
		}else{
			msgtext.setVisibility(View.VISIBLE);
			msgtext.setText(getStrValue(R.string.item_text5));
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
				mHandler.sendEmptyMessage(12);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("id").size() < 3) {
						isAll = true;
					}else{
						isAll = false;
					}
					mHandler.sendMessage(mHandler.obtainMessage(11, map));
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		case R.id.submit_btn:
			String content = input_share.getText().toString().trim();
			if (RegexUtil.checkNotNull(content)) {
				shareLayout.setVisibility(View.VISIBLE);
				inputshareLayout.setVisibility(View.GONE);
				postShareData(content);
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
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if(map.get("msg").get(0).equals(ResponseCode.RESULT_OK)){
						toastMsg(R.string.item_text7);
						input_share.setText("");
						preferenceUtil.setPreferenceData(UserInfoCode.CHANGEAVATOR, true);// home refresh
						getShareMoreData(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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
}
