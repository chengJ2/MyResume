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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;

/**
 * 
* @ClassName: AddressActivity 
* @Description：所在地 
* @date 2016/4/21 上午10:01:29 
*
 */
public class AddressActivity extends BaseActivity implements OnClickListener{

	private GridView hotaddress_gridview;
	
	private ListView alladdrListview;
	
	private LinearLayout topLayout;
	private EditText index_search_edit;
	private ImageView clearView;
	private Button search_btn;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
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
				
				mList = (List<String>) msg.obj;
				if (mList != null && mList.size() > 0) {
					fillAllCity(mList);
				}
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
		boayLayout.removeAllViews();
		
		View v = View.inflate(self,R.layout.activity_address_layout, null);
		boayLayout.addView(v);
		
		setTitle(R.string.resume_address);
		
		setMsgHide();
		
		setRight2IconVisible(View.GONE);
		
		findViews();
		
		mHandler.sendEmptyMessage(10);
		
		initHotCity();
		
		index_search_edit.setHint(CommUtil.getStrValue(self, R.string.hint_address_text));
		index_search_edit.requestFocus();
		
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
				if (s.toString() != null && !"".equals(s.toString())) {
					whichTab = 1;
					clearView.setVisibility(View.VISIBLE);
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
				if (actionId ==EditorInfo.IME_ACTION_SEARCH) {
					CommUtil.hideKeyboard(self);
					searchCity();
					return true;
				}else{
					return false;
				}
			}
		});
	}
	
	private void findViews(){
		topLayout = findView(R.id.addrtopLayout);
		index_search_edit = findView(R.id.index_search_edit);
		clearView = findView(R.id.clear);
		search_btn = findView(R.id.search_btn);
		clearView = findView(R.id.clear);
		hotaddress_gridview = findView(R.id.hotaddress_gridview);
		alladdrListview = findView(R.id.alladdrListview);
		
		left_icon.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		clearView.setOnClickListener(this);
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
	private void getAllCity(final String keyword) {
		/*new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				List<String> mList = new ArrayList<String>();
				Cursor cursor = null;
				try {
					String sql = "select * from (select c.code as code ,c.[cname] as cname from city c " + 
								" union  select p.code,p.name from province p where p.type = 1 and p.code!=110000)"
								+ " where cname like '%"+ keyword +"%'";
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
		},100);*/
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<String> mList = new ArrayList<String>();
				Cursor cursor = null;
				try {
					String sql = "select * from (select c.code as code ,c.[cname] as cname from city c " + 
								" union  select p.code,p.name from province p where p.type = 1 and p.code!=110000)"
								+ " where cname like '%"+ keyword +"%'";
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
		}).start();
		
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.search_btn:
			searchCity();
			break;
		case R.id.clear:
			whichTab = 0;
			CommUtil.hideKeyboard(self);
			index_search_edit.setText("");
			getAllCity("");
			clearView.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	private void searchCity(){
		String keyword = CommUtil.getEditTextValue(index_search_edit);
		if (RegexUtil.checkNotNull(keyword)) {
			whichTab = 1;
			clearView.setVisibility(View.VISIBLE);
			getAllCity(keyword);
		}
	}
}
