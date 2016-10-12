package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 
* @ClassName: MajorActivity 
* @Description: 大学里的专业名称
* @date 2016/4/21 下午2:04:45 
*
 */
public class MajorActivity extends BaseActivity implements OnClickListener{
	
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
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_category_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.ed_info_majorname);
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		catelayout = findView(R.id.catelayout);
		category =  findView(R.id.category);
		backCate = findView(R.id.backCate_btn);
		
		catelayout.setVisibility(View.GONE);
		
		category_Listview = findView(R.id.category_Listview);
		category_Listview.setVisibility(View.VISIBLE);
		
		backCate.setOnClickListener(this);
		
		getMajor();
	}
	
	/**
	 * 
	 * @Title:AddressActivity
	 * @Description: 显示专业类别
	 */
	private void fillCategory(final List<String> mList,final int flag){
//		if (commStrAdapter == null) {
			commStrAdapter = new CommonBaseAdapter<String>(self, mList,
					R.layout.home_xgln_listview) {
				
				@Override
				public void convert(ViewHolder holder, String item,
						final int position) {
					String[] data = mList.get(position).split(";");
					holder.setText(R.id.itemName, data[1]);
				}
			};
			category_Listview.setAdapter(commStrAdapter);
//		}else{
//			commStrAdapter.notifyDataSetChanged();
//			category_Listview.invalidate();
//		}

		category_Listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String[] data = mList.get(position).split(";");
				if (flag == 2) {
					Intent intent=new Intent();
					intent.putExtra(Constants.MAJORNAME, data[1]);
					intent.setAction(Constants.EDUCATION_SEND);
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
	
	/**
	 * 
	 * @Title:AddressActivity
	 * @Description: 获取专业类别
	 * @return List<String> 
	 */
	private void getMajor(){
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
									+ ";" +cursor.getString(cursor.getColumnIndex("cnname")));
						}
						mHandler.sendMessage(mHandler.obtainMessage(11, mList));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}
			}
		},500);
	}
	
	/**
	 * 
	 * @Description: 获取专业类别下的子分类
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
									+ ";" +cursor.getString(cursor.getColumnIndex("cateChinaname")));
						}
						mHandler.sendMessage(mHandler.obtainMessage(13, mList));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}
			}
		},200);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.backCate_btn:
			catelayout.setVisibility(View.GONE);
			getMajor();
			break;
		default:
			break;
		}
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
