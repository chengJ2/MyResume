package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.service.LocationService;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 
* @ClassName: AddressActivity 
* @Description：城市列表 
* @date 2016/4/21 上午10:01:29 
*
 */
public class AddressActivity extends BaseActivity implements OnClickListener{

	private GridView hotaddress_gridview;
	
	private ListView alladdrListview;
	
	private LinearLayout topLayout;
	private EditText index_search_edit;
	private ImageView clearView;
	private TextView search_cancle;
	private TextView locationcity;
	
	private TextView msgText;
	
	private LocationService locationService;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 9:
				initHotCity((List<String>) msg.obj);// 热门城市
				break;
			case 10:
				whichTab = 0;
				getAllCity("");
				break;
			case 11:
				if (whichTab == 1) {
					topLayout.setVisibility(View.GONE);
				}else{
					topLayout.setVisibility(View.VISIBLE);
				}
				fillAllCity((List<String>) msg.obj);
				break;
			case 12:
				topLayout.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bodyLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_address_layout, null);
		bodyLayout.addView(v);
		
		findViews();
		initViews();
		
		searchAction();
		getHotCity();
		
		mHandler.sendEmptyMessageDelayed(10, 200);
		
	}
	
	private void findViews(){
		topLayout = findView(R.id.addrtopLayout);
		index_search_edit = findView(R.id.index_search_edit);
		clearView = findView(R.id.clear);
		search_cancle = findView(R.id.search_cancle);
		clearView = findView(R.id.clear);
		hotaddress_gridview = findView(R.id.hotaddress_gridview);
		alladdrListview = findView(R.id.alladdrListview);
		locationcity = findView(R.id.locationcity);
		
		search_cancle.setOnClickListener(this);
		clearView.setOnClickListener(this);
		
		locationcity.setOnClickListener(this);
		
		index_search_edit.setHint(getStrValue(R.string.hint_address_text));
		index_search_edit.setHintTextColor(getColorValue(R.color.grey));
		index_search_edit.requestFocus();
		
		msgText = findView(R.id.msgText);
		msgText.setVisibility(View.VISIBLE);
		msgText.setText(getStrValue(R.string.item_text43));
		
		locationService = new LocationService(self, locationcity);
	}
	
	private void initViews(){
		setTopTitle(R.string.resume_address);
		setMsgHide();
		
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
	}
	
	private void searchAction(){
		index_search_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (RegexUtil.checkNotNull(s.toString())) {
					whichTab = 1;
					clearView.setVisibility(View.VISIBLE);
					search_cancle.setVisibility(View.VISIBLE);
					getAllCity(s.toString());
				}else{
					whichTab = 0;
					getAllCity("");
					clearView.setVisibility(View.GONE);
				}
			}
		});
		
		index_search_edit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					CommUtil.hideKeyboard(self);
					searchCity();
					return true;
				}else{
					return false;
				}
			}
		});
	}
	
	/**
	 * 显示热门城市
	 */
	private void initHotCity(final List<String> list){
		CommonBaseAdapter<String> commAdapter = new CommonBaseAdapter<String>(self, list,
				R.layout.home_xgln_grilview) {

			@Override
			public void convert(ViewHolder holder, String item,
					final int position) {
				holder.setText(R.id.itemName, list.get(position));
				holder.setOnClickEvent(R.id.itemName, new ClickEvent() {

					@Override
					public void onClick(View view) {
						setKeyResult(mList.get(position));
					}
				});
			}
		};

		hotaddress_gridview.setAdapter(commAdapter);
	}
	
	
	private void setKeyResult(String city){
		Intent intent=new Intent();
        intent.putExtra(Constants.CITY, city);
        setResult(Constants.RESULT_CODE, intent);
		scrollToFinishActivity();
	}
	
	/**
	 * 显示全国城市
	 * @param mList
	 */
	private void fillAllCity(List<String> mList){
		msgText.setVisibility(View.GONE);
		
		if (commStrAdapter == null) {
			commStrAdapter = new CommonBaseAdapter<String>(self, mList,
					R.layout.home_xgln_listview) {

				@Override
				public void convert(ViewHolder holder, String item,
						final int position) {
					holder.setText(R.id.itemName, mList.get(position));
					holder.setOnClickEvent(R.id.itemName, new ClickEvent() {

						@Override
						public void onClick(View view) {
							setKeyResult(mList.get(position));
						}
					});
				}
			};
			alladdrListview.setAdapter(commStrAdapter);
		}else{
			commStrAdapter.setItemList(mList);
			commStrAdapter.notifyDataSetChanged();
			alladdrListview.invalidate();
		}
	}
	
	/**
	 * 获取全国城市
	 * @param keyword
	 */
	private void getAllCity(final String keyword) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				mList = new ArrayList<String>();
				Cursor cursor = null;
				try {
					String sql = "";
					if (RegexUtil.checkNotNull(keyword)) {
						sql = "select * from (select c.[code] as code ,c.[cnname] as cnname,c.[egname] as egname,c.[pyname] as pyname from city c " + 
								" union  select p.code,p.cnname,p.[egname],p.[pyname] from province p where p.type = 1 and p.code!=110000)"
								+ " where cnname = '"+ keyword +"' or pyname = '"+ keyword +"' or egname = '"+ keyword +"'";
					}else{
						sql = "select * from (select c.[code] as code ,c.[cnname] as cnname,c.[egname] as egname,c.[pyname] as pyname from city c " + 
								" union  select p.code,p.cnname,p.[egname],p.[pyname] from province p where p.type = 1 and p.code!=110000)";
					}
					cursor = MyApplication.database.rawQuery(sql, null);
					if (cursor.getCount() > 0) {
						while (cursor.moveToNext()) {
							mList.add(cursor.getString(cursor.getColumnIndex("cnname")));
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
		}).start();
		
	}
	
	/**
	 * 获取热门城市
	 * @return List<String> 
	 */
	private void getHotCity(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				mList = new ArrayList<String>();
				Cursor cursor = null;
				try {
					String sql = "select c.code as code ,c.[cnname] as cnname from city c where c.type = 1"
								+ " union  select p.code,p.cnname from province p where p.type = 1";
					cursor = MyApplication.database.rawQuery(sql, null);
					if (cursor.getCount() > 0) {
						while (cursor.moveToNext()) {
							mList.add(cursor.getString(cursor.getColumnIndex("cnname")));
						}
					}
					mHandler.sendMessage(mHandler.obtainMessage(9, mList));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (cursor != null) {
						cursor.close();
					}
					
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.search_cancle:
			whichTab = 0;
			CommUtil.hideKeyboard(self);
			index_search_edit.setText("");
			getAllCity("");
			clearView.setVisibility(View.GONE);
			search_cancle.setVisibility(View.GONE);
		case R.id.clear:
			whichTab = 0;
			index_search_edit.setText("");
			clearView.setVisibility(View.GONE);
			break;
		case R.id.locationcity:
			setKeyResult(locationcity.getText().toString().trim());
			break;
		default:
			break;
		}
	}
	
	private void searchCity(){
		String keyword = getEditTextValue(index_search_edit);
		if (RegexUtil.checkNotNull(keyword)) {
			whichTab = 1;
			clearView.setVisibility(View.VISIBLE);
			getAllCity(keyword);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
		if(locationService != null){
			locationService.mLocationClient.stop();
			locationService.myListener = null;
			locationService = null;
		}
	}
}
