package com.me.resume.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;

/**
 * 更多面试分享心得
 * @author Administrator
 *
 */
public class ResumeShareMoreActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout sharemore_layout;
	private ListView reviewsharemoreListView;
	
	private LinearLayout shareLayout;
	
	private LinearLayout inputshareLayout;
	private EditText input_share;
	private Button submit_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.resume_share_more_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.item_text61);
		
		setMsgHide();
		
		setRightIconVisible(View.INVISIBLE);
		
		setRight2IconVisible(View.GONE);
		
		setfabLayoutVisible(View.GONE);
		
		sharemore_layout = findView(R.id.sharemore_layout);
		
		reviewsharemoreListView = findView(R.id.reviewsharemoreListView);
		
		shareLayout = findView(R.id.shareLayout);
		
		inputshareLayout = findView(R.id.inputshareLayout);
		input_share = findView(R.id.input_share);
		submit_btn = findView(R.id.submit_btn);
		
		sharemore_layout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		submit_btn.setOnClickListener(this);
		
		commMapList = new HashMap<String, List<String>>();
		
		if (CommUtil.isNetworkAvailable(self)) {
			getShareMoreData(true);
		}
		
	}
	
	/**
	 * @Description: 面试分享心得
	 */
	private void getShareMoreData(final boolean frsitReq){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		requestData("pro_getshareinfo", 2, params, values, new HandlerData() {
			@Override
			public void error() {
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map != null) {
						commMapList.clear();
						commMapList.putAll(map);
						
						if (commapBaseAdapter != null) {
							commapBaseAdapter.notifyDataSetChanged();
							reviewsharemoreListView.invalidate();
						}else{
							setShareData(commMapList);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * @Description: 面试分享心得
	 */
	private void setShareData(final Map<String, List<String>> map){
		commapBaseAdapter = new CommForMapBaseAdapter(self,map,R.layout.home_share_item,"id") {
			
			@Override
			public void convert(ViewHolder holder, List<String> item, int position) {
				holder.showImage(R.id.share_usernameavator,
						CommUtil.getHttpLink(map.get("avator").get(position)),true);
				
				String realname = map.get("realname").get(position);
				if (!realname.equals("") && realname != null) {
					holder.setText(R.id.share_username, map.get("realname").get(position));
				}else{
					holder.setText(R.id.share_username, map.get("username").get(position));
				}
				
				/*String jobtitleStr = map.get("jobtitle").get(position);
				if (RegexUtil.checkNotNull(jobtitleStr)) {
					holder.setTextVisibe(R.id.share_jobtitle, View.VISIBLE);
					holder.setText(R.id.share_jobtitle, jobtitleStr);
				}else{
					holder.setTextVisibe(R.id.share_jobtitle, View.GONE);
				}*/
				
				String workyear = map.get("joinworktime").get(position);
				if (RegexUtil.checkNotNull(workyear)) {
					int year = CommUtil.parseInt(workyear.substring(0, 4));
					int theYear = CommUtil.parseInt(TimeUtils.theYear());
					holder.setTextVisibe(R.id.share_workyear, View.VISIBLE);
					holder.setText(R.id.share_workyear,(theYear - year) + "年工作经验");
				}else{
					holder.setTextVisibe(R.id.share_workyear, View.GONE);
				}
				
				
				holder.setText(R.id.share_content, map.get("content").get(position).toString().trim());
				holder.setText(R.id.share_city, map.get("city").get(position));
				holder.setText(R.id.share_datime, map.get("createtime").get(position));
				
				holder.setOnClickEvent(R.id.share_collection, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						if (!MyApplication.USERID.equals("0")) {
							toastMsg(R.string.action_login_head);
						}
					}
				});
			}
		};
		
		reviewsharemoreListView.setAdapter(commapBaseAdapter);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.shareLayout:
			inputshareLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.submit_btn:
			String content = input_share.getText().toString();
			if (RegexUtil.checkNotNull(input_share.getText().toString())) {
				inputshareLayout.setVisibility(View.GONE);
				postShareData(content);
			}
			break;
		case R.id.sharemore_layout:
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
					if("200".equals(map.get("msg").get(0))){
						toastMsg(R.string.item_text7);
						input_share.setText("");
						getShareMoreData(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
