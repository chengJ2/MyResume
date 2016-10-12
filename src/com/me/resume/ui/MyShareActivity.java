package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.tools.L;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.TimeUtils;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: MyShareActivity 
* @Description: 我的分享 
* @date 2016/10/12 下午3:08:13 
*
 */
public class MyShareActivity extends BaseActivity {

	private ListView myshareListView;
	private TextView nodata;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				
				//setStatus();
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

		setTopTitle(R.string.personal_c_item2);
		setMsgHide();
		setRightIcon(R.drawable.icon_sync);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);

		myshareListView = findView(R.id.collectionListView);
		nodata = findView(R.id.nodata);
		nodata.setText(getStrValue(R.string.item_text43));
		nodata.setVisibility(View.VISIBLE);
		
		initData();
	}
	
	private void initData() {
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_userId");
		values.add(uTokenId);
		
		requestData("pro_getmyshare", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				set2Msg(getStrValue(R.string.timeout_network));
				nodata.setText(getStrValue(R.string.en_nodata));
				nodata.setVisibility(View.VISIBLE);
			}
			
			public void success(final Map<String, List<String>> map) {
				try {
					commapBaseAdapter = new CommForMapBaseAdapter(self,
							map, R.layout.share_list_detail_item, "userId") {
							@Override
							public void convert(ViewHolder holder,
									List<String> item, int position) {
								holder.setText(R.id.content, map.get("content").get(position));
								final String status = map.get("status").get(position);
								if ("1".equals(status)) {
									holder.setText(R.id.status, "已发布");
									holder.setTextColor(R.id.status, getColorValue(R.color.top_bar));
								}else if("2".equals(status)){
									holder.setText(R.id.status, "未公开");
									holder.setTextColor(R.id.status, getColorValue(R.color.red));
								}else if("-1".equals(status)){
									holder.setText(R.id.status, "已删除");
									holder.setTextColor(R.id.status, getColorValue(R.color.grey_10));
								}
								holder.setText(R.id.datetime, 
										TimeUtils.showTimeFriendly(map.get("createtime").get(position)));
								
								holder.setOnClickEvent(R.id.iconmenu, new ClickEvent() {
									
									@Override
									public void onClick(View view) {
										// TODO Auto-generated method stub
										
										if ("1".equals(status)) {
											
										}else if("2".equals(status)){
											
										}else if("-1".equals(status)){
											
										}
										
										DialogUtils.showAlertDialog(self, 
												getStrValue(R.string.dialog_action_topic_delete_sure_alert),View.GONE,
												getStrValue(R.string.show_button_sure),mHandler);
										
									}
								});
							}
					};
					myshareListView.setVisibility(View.VISIBLE);
					myshareListView.setAdapter(commapBaseAdapter);
					nodata.setVisibility(View.GONE);
					
				} catch (Exception e) {
					setMsgVisibility(View.GONE);
					L.e(e.getMessage());
				}
			}

			@Override
			public void noData() {
				setMsgVisibility(View.GONE);
				nodata.setText(getStrValue(R.string.en_nodata));
				nodata.setVisibility(View.VISIBLE);
			}
		});
		
		myshareListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
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
			initData();
			break;
		default:
			break;
		}
	}
}
