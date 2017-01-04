package com.me.resume.ui;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.utils.CommUtil;

/**
 * 管理员界面
* @date 2016/10/18 下午5:04:55 
*
 */
public class AdminFunctionsActivity extends BaseActivity {

	private GridView funViews;
	
	private String[] names = {"用户管理","面试分享","消息通知","文章管理"};
	private String[] icons = {R.drawable.admin_user+"",R.drawable.admin_usershare+"",
			R.drawable.admin_msgpush+"",R.drawable.admin_topic+""};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bodyLayout.removeAllViews();
		View v = View.inflate(self,R.layout.admin_functions_layout, null);
		bodyLayout.addView(v);
		
		funViews = findView(R.id.fungridview);
		
		setTopTitle(R.string.admin_text1);
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		showData();
	}

	private void showData() {
		final Map<String, String[]> maps = new HashMap<String,String[]>();
		maps.put("icon",icons);
		maps.put("name",names);
		
		commMapArrayAdapter = new CommForMapArrayBaseAdapter(AdminFunctionsActivity.this,maps,
				R.layout.admin_fun_item,"icon") {
			
			@Override
			public void convert(ViewHolder holder, String[] item, int position) {
				holder.setText(R.id.item2, maps.get("name")[position]);
				holder.setImageResource(R.id.item1,CommUtil.parseInt(maps.get("icon")[position]));
				
			}
		};
		funViews.setAdapter(commMapArrayAdapter);
		funViews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){
					startChildActivity(Constants.USERMANAGE, false);
				}
			}
		});
	}
}
