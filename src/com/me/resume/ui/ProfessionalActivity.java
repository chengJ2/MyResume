package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.CommUtil;

/**
 * 
* @ClassName: ProfessionalActivity 
* @Description: 专业名称
* @author Comsys-WH1510032 
* @date 2016/4/21 下午2:04:45 
*
 */
public class ProfessionalActivity extends BaseActivity {
	
	private RelativeLayout catelayout;
	private TextView category;
	private Button backCate = null;
	
	private ListView category_Listview;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				mList = (List<String>) msg.obj;
				if (mList != null && mList.size() > 0) {
					fillCategory(mList,1);
				}
				break;
			case 12:
				String cateId = (String) msg.obj;
				getCategoryName(cateId);
				break;
			case 13:
				mList = (List<String>) msg.obj;
				if (mList != null && mList.size() > 0) {
					fillCategory(mList,2);
				}
				break;
			default:
				break;
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_category_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.resume_address);
		
		setMsgHide();
		
		setRightIconVisible(View.GONE);
		
		left_icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scrollToFinishActivity();
			}
		});
		
		catelayout = findView(R.id.catelayout);
		category =  findView(R.id.category);
		backCate = findView(R.id.backCate_btn);
		
		backCate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				catelayout.setVisibility(View.GONE);
				getCategory();
			}
		});
		
		category_Listview = findView(R.id.category_Listview);
		
		getCategory();
	}
	
	/**
	 * 
	 * @Title:AddressActivity
	 * @Description: 显示热门城市
	 */
	private void fillCategory(List<String> mList,final int flag){
		CommonBaseAdapter<String> commAdapter = new CommonBaseAdapter<String>(self, mList,
				R.layout.home_xgln_listview) {

			@Override
			public void convert(ViewHolder holder, String item,
					final int position) {
				final String[] data = mList.get(position).split("#");
				holder.setText(R.id.itemName, data[1]);

				holder.setOnClickEvent(R.id.itemName, new ClickEvent() {

					@Override
					public void onClick(View view) {
						if (flag == 2) {
							Intent intent=new Intent();
							intent.putExtra("category", data[1]);
							intent.setAction(Constants.EDCATION);
			           		sendBroadcast(intent);
							scrollToFinishActivity();
						}else{
							catelayout.setVisibility(View.VISIBLE);
							category.setText(data[1]);
							mHandler.sendMessage(mHandler.obtainMessage(12, data[0]));
						}
					}
				});
			}
		};

		category_Listview.setAdapter(commAdapter);
	}
	
	/**
	 * 
	 * @Title:AddressActivity
	 * @Description: 获取热门城市
	 * @author Comsys-WH1510032
	 * @return List<String> 
	 */
	private void getCategory(){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				List<String> mList = new ArrayList<String>();
				Cursor cursor = null;
				try {
					String sql = "select * from category";
					cursor = MyApplication.database.rawQuery(sql, null);
					if (cursor.getCount() > 0) {
						while (cursor.moveToNext()) {
							mList.add(cursor.getString(cursor.getColumnIndex("id")) 
									+ "#" +cursor.getString(cursor.getColumnIndex("chinaname")));
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
	private void getCategoryName(final String cateId){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				List<String> mList = new ArrayList<String>();
				Cursor cursor = null;
				try {
					String sql = "select * from categoryname where cateId = ?";
					cursor = MyApplication.database.rawQuery(sql, new String[]{cateId});
					if (cursor.getCount() > 0) {
						while (cursor.moveToNext()) {
							mList.add(cursor.getString(cursor.getColumnIndex("cateId")) 
									+ "#" +cursor.getString(cursor.getColumnIndex("cateChinaname")));
						}
						mHandler.sendMessage(mHandler.obtainMessage(13, mList));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					cursor.close();
				}
			}
		},500);
	}
}
