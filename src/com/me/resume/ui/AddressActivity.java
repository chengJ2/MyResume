package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.tools.L;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.views.CustomListView;

/**
 * 
* @ClassName: AddressActivity 
* @Description：所在地 
* @date 2016/4/21 上午10:01:29 
*
 */
public class AddressActivity extends SwipeBackActivity {

	private TextView toptext;
	
	private ImageView left_icon,right_icon;
	
	private GridView hotaddress_gridview;
	
	private ListView alladdrListview;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				mList = (List<String>) msg.obj;
				if (mList != null && mList.size() > 0) {
					fillAllCity(mList);
				}
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_layout);
		
		toptext = findView(R.id.top_text);
		left_icon = findView(R.id.left_lable);
		
		right_icon = findView(R.id.right_icon);
		right_icon.setVisibility(View.GONE);
//		left_icon.setImageResource(R.drawable.icon_person_avtar);
		toptext.setText(CommUtil.getStrValue(self, R.string.resume_address));
		
		left_icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scrollToFinishActivity();
			}
		});
		
		hotaddress_gridview = findView(R.id.hotaddress_gridview);
		alladdrListview = findView(R.id.alladdrListview);
		
		initHotCity();
		
		getAllCity();
	}
	
	/**
	 * 
	 * @Title:AddressActivity
	 * @Description: 显示热门城市
	 */
	private void initHotCity(){
		CommonBaseAdapter<String> commAdapter = new CommonBaseAdapter<String>(self, getHotCity(),
				R.layout.home_xgln_grilview) {

			@Override
			public void convert(ViewHolder holder, String item,
					final int position) {
				holder.setText(R.id.itemName, mList.get(position));
				holder.setOnClickEvent(R.id.itemName, new ClickEvent() {

					@Override
					public void onClick(View view) {
						Intent intent=new Intent();
				        intent.putExtra("city", mList.get(position));
				        setResult(Constants.RESULT_CODE, intent);
						scrollToFinishActivity();
					}
				});
			}
		};

		hotaddress_gridview.setAdapter(commAdapter);
	}
	
	/**
	 * 
	 * @Title:AddressActivity
	 * @Description: 显示全国城市
	 * @param mList
	 */
	private void fillAllCity(List<String> mList){
		CommonBaseAdapter<String> commAdapter = new CommonBaseAdapter<String>(self, mList,
				R.layout.home_xgln_listview) {

			@Override
			public void convert(ViewHolder holder, String item,
					final int position) {
				holder.setText(R.id.itemName, mList.get(position));
				holder.setOnClickEvent(R.id.itemName, new ClickEvent() {

					@Override
					public void onClick(View view) {
						Intent intent=new Intent();
				        intent.putExtra("city", mList.get(position));
				        setResult(Constants.RESULT_CODE, intent);
						scrollToFinishActivity();
					}
				});
			}
		};
		
		alladdrListview.setAdapter(commAdapter);
	}
	
	/**
	 * 
	 * @Title:AddressActivity
	 * @Description: 获取全国城市
	 * @author Comsys-WH1510032
	 * @return 返回类型
	 */
	private void getAllCity() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				List<String> mList = new ArrayList<String>();
				Cursor cursor = null;
				try {
					String sql = "select c.code as code ,c.[cname] as cname from city c " + 
								" union  select p.code,p.name from province p where p.type = 1 and p.code!=110000";
					cursor = MyApplication.database.rawQuery(sql, null);
					if (cursor.getCount() > 0) {
						while (cursor.moveToNext()) {
							mList.add(cursor.getString(cursor.getColumnIndex("cname")));
						}
						mHandler.sendMessage(mHandler.obtainMessage(11, mList));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					cursor.close();
				}
			}
		},500);
		
	}
	
	/**
	 * 
	 * @Title:AddressActivity
	 * @Description: 获取热门城市
	 * @author Comsys-WH1510032
	 * @return List<String> 
	 */
	private List<String> getHotCity(){
		Cursor cursor = null;
		List<String> mList = new ArrayList<String>();
		try {
			String sql = "select c.code as code ,c.[cname] as cname from city c where c.type = 1"
						+ " union  select p.code,p.name from province p where p.type = 1";
			cursor = MyApplication.database.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					mList.add(cursor.getString(cursor.getColumnIndex("cname")));
				}
				return mList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return null;
	}
}
