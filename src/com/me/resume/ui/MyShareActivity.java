package com.me.resume.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.tools.L;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;

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
	
	private Map<String, List<String>> sMap = new HashMap<String, List<String>>();
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				String params = (String) msg.obj;
				if(params!=null && !"".equals(params)){
					setStatus(params);
				}
				break;
			case 12:
				params = (String) msg.obj;
				if(params!=null && !"".equals(params)){
					String [] arr = params.split(";");
					Bundle b = new Bundle();
					b.putString("sid", arr[0]);
					b.putString("scontent", arr[1]);
					ActivityUtils.startActivityPro(self, 
							Constants.PACKAGENAMECHILD + Constants.USERSHARE, 
							Constants.SHAREINFO,b);
				}
				break;
			case 13:
				startChildActivity(Constants.USERSHARE, false);
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
		
	}
	
	private void initData() {
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_sId");
		params.add("p_userId");
		values.add("0");
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
					sMap.clear();
					sMap.putAll(map);
					if(commapBaseAdapter != null){
						commapBaseAdapter.notifyDataSetChanged();
					}else{
						commapBaseAdapter = new CommForMapBaseAdapter(self,
								sMap, R.layout.share_list_detail_item, "userId") {
							@Override
							public void convert(ViewHolder holder,
									List<String> item, int position) {
								final String content = sMap.get("content").get(position);
								holder.setText(R.id.content, content);
								final String status = sMap.get("status").get(position);
								if ("1".equals(status)) {
									holder.setText(R.id.status, "已发布");
									holder.setTextBgColor(R.id.status, R.drawable.button_bg_green);
								}else if("2".equals(status)){
									holder.setText(R.id.status, "未公开");
									holder.setTextBgColor(R.id.status, R.drawable.button_bg_red);
								}else if("-1".equals(status)){
									holder.setText(R.id.status, "已删除");
									holder.setTextBgColor(R.id.status, R.drawable.button_bg_grey);
								}
								holder.setText(R.id.datetime, 
										TimeUtils.showTimeFriendly(sMap.get("createtime").get(position)));
								
								final String sid = sMap.get("id").get(position);
								
								holder.setOnClickEvent(R.id.iconmenu, new ClickEvent() {
									
									@Override
									public void onClick(View view) {
										List<String> list = new ArrayList<String>();
										if ("1".equals(status)) {
											list.add("不公开" + ";" + "2");
											list.add("删除" + ";" + "-1");
										}else if("2".equals(status)){
											list.add("公开"+ ";" + "1");
											list.add("删除"+ ";" + "-1");
										}else if("-1".equals(status)){
											list.add("不公开"+ ";" + "2");
											list.add("公开"+ ";" + "1");
										}
										list.add("编辑");
										list.add("新增");
										
										DialogUtils.showShareMenuDialog(self,
												view,R.string.action_do,
												sid,content,
												list,mHandler);
									}
								});
							}
						};
					}
					myshareListView.setVisibility(View.VISIBLE);
					myshareListView.setAdapter(commapBaseAdapter);
					setMsgHide();
					nodata.setVisibility(View.GONE);
				} catch (Exception e) {
					setMsgHide();
					L.e(e.getMessage());
				}
			}

			@Override
			public void noData() {
				setMsgHide();
				nodata.setText(getStrValue(R.string.en_nodata));
				nodata.setVisibility(View.VISIBLE);
			}
		});
	}
	
	/**
	 * 更新状态
	 * @param status
	 */
	private void setStatus(String param){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_userId");
		params.add("p_sId");
		params.add("p_status");
		
		String [] paramArray = param.split(";");
		values.add(uTokenId);
		values.add(paramArray[1]);
		values.add(paramArray[0]);
		
		requestData("pro_setmyshare", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				set2Msg(getStrValue(R.string.timeout_network));
				DialogUtils.dismissPopwindow();
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get(ResponseCode.MSG).get(0).equals(ResponseCode.RESULT_OK)) {
						DialogUtils.dismissPopwindow();
						preferenceUtil.setPreferenceData(UserInfoCode.USERSHARE, true);
						initData();
					}
				} catch (Exception e) {
					setMsgHide();
					L.e(e.getMessage());
				}
			}

			@Override
			public void noData() {
				setMsgHide();
				DialogUtils.dismissPopwindow();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		initData();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
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
