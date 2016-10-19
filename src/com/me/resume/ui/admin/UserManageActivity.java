package com.me.resume.ui.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.tools.L;
import com.me.resume.utils.TimeUtils;

/**
 * 用户管理
* @date 2016/10/19 下午4:02:11 
*
 */
public class UserManageActivity extends BaseActivity {

	private ListView userListView;
	private TextView weeknewusers,totalusers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.admin_activity_usermanager_layout, null);
		boayLayout.addView(v);
		
		weeknewusers = findView(R.id.weeknewusers);
		totalusers = findView(R.id.totalusers);
		
		userListView = findView(R.id.userListView);
		
		setTopTitle(R.string.admin_text11);
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		showTotalData();
		
		showData();
	}

	private void showTotalData() {
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		requestData("pro_getallusers", 2, params, values, new HandlerData() {
			@Override
			public void error() {
				set2Msg(getStrValue(R.string.timeout_network));
			}
			
			public void success(final Map<String, List<String>> map) {
				try {
					totalusers.setText("总共用户:"+map.get("totalnum").get(0));
					weeknewusers.setText("本周新增:"+map.get("totalnum").get(1));
				} catch (Exception e) {
					setMsgHide();
					L.e(e.getMessage());
				}
			}

			@Override
			public void noData() {
				setMsgHide();
			}
		});
	}

	private void showData() {
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		requestData("pro_getallusers", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				set2Msg(getStrValue(R.string.timeout_network));
			}
			
			public void success(final Map<String, List<String>> map) {
				try {
					commapBaseAdapter = new CommForMapBaseAdapter(self,
							map, R.layout.admin_usermanager_list_item, "uid") {
						@Override
						public void convert(ViewHolder holder,
								List<String> item, int position) {
							holder.setText(R.id.num, String.valueOf(position+1));
							holder.setText(R.id.username, map.get("username").get(position));
							
							/*String realname = map.get("realname").get(position);
							if(realname!=null && !"".equals(realname)){
								holder.setTextVisibe(R.id.realname, View.VISIBLE);
								holder.setText(R.id.realname, realname);
							}else{*/
								holder.setTextVisibe(R.id.realname, View.GONE);
//							}
							
							String email = map.get("email").get(position);
							String phone = map.get("phone").get(position);
							if(email!=null && !"".equals(email)){
								holder.setText(R.id.phone_email, email);
							}else{
								if(email!=null && !"".equals(email)){
									holder.setText(R.id.phone_email, phone);
								}
							}
							
							holder.setText(R.id.createtime, 
									TimeUtils.showTimeFriendly(map.get("createtime").get(position)));
							
							holder.setOnClickEvent(R.id.iconmenu, new ClickEvent() {
								
								@Override
								public void onClick(View view) {
								}
							});
						}
					};
					userListView.setAdapter(commapBaseAdapter);
					setMsgHide();
				} catch (Exception e) {
					setMsgHide();
					L.e(e.getMessage());
				}
			}

			@Override
			public void noData() {
				setMsgHide();
			}
		});
	}
}
