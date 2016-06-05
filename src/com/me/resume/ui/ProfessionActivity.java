package com.me.resume.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ProfessionListAdapter;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;

/**
 * 
* @ClassName: ProfessionActivity 
* @Description: 职业类别
* @date 2016/5/31 上午10:58:05 
*
 */
public class ProfessionActivity extends BaseActivity implements OnClickListener{

	private EditText index_search_edit;
	private ImageView clearView;
	private TextView search_cancle;
	
	private ExpandableListView exprefession_listview;
	private ProfessionListAdapter listAdapter;
	
	private TextView msgText;
	
	private ArrayList<Map<String, String>> groupList = new ArrayList<Map<String,String>>();
	private ArrayList<List<String>> childList; 
	
	private List<String> llistChild; // 职业类别子项数据
	private List<Map<String, List<String>>> childrens;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				getAllProfession("");
				break;
			case 11:
				if (msg.obj != null) {
					fillData((List<String>) msg.obj);
				}
				break;
			case 12:
				msgText.setVisibility(View.GONE);
				exprefession_listview.setVisibility(View.VISIBLE);
				if (listAdapter == null) {
					listAdapter = new ProfessionListAdapter(self, groupList, childList);
					exprefession_listview.setAdapter(listAdapter);
				}else{
					listAdapter.notifyDataSetChanged();
					exprefession_listview.invalidate();
				}
				
				exprefession_listview.setOnChildClickListener(new OnChildClickListener() {
					
					@Override
					public boolean onChildClick(ExpandableListView parent, View v,
							int groupPosition, int childPosition, long id) {
						Intent intent=new Intent();
				        intent.putExtra(Constants.PROFESSIONNAME, childList.get(groupPosition).get(childPosition).toString());
				        setResult(Constants.RESULT_CODE, intent);
						scrollToFinishActivity();
						return true;
					}
				});
				
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
		View v = View.inflate(self,R.layout.activity_profession_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.resume_profession);
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		findViews();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(10);
			}
		}, 200);
		
		searchEdit();
	}
	
	
	private void findViews(){
		index_search_edit = findView(R.id.index_search_edit);
		clearView = findView(R.id.clear);
		search_cancle = findView(R.id.search_cancle);
		clearView = findView(R.id.clear);
		
		exprefession_listview = findView(R.id.exprefession_listview);
		
		msgText = findView(R.id.msgText);
		msgText.setVisibility(View.VISIBLE);
		msgText.setText(getStrValue(R.string.item_text43));
		
		clearView.setOnClickListener(this);
		search_cancle.setOnClickListener(this);
	}
	
	private void searchEdit(){
		index_search_edit.setHint(getStrValue(R.string.hint_profession_text));
		index_search_edit.setHintTextColor(getColorValue(R.color.grey));
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
				L.d("ss:"+s.toString());
				if (RegexUtil.checkNotNull(s.toString())) {
					whichTab = 1;
					clearView.setVisibility(View.VISIBLE);
					search_cancle.setVisibility(View.VISIBLE);
					getAllProfession(s.toString());
				}else{
					whichTab = 0;
					getAllProfession("");
					clearView.setVisibility(View.GONE);
				}
			}
		});
		
		index_search_edit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					CommUtil.hideKeyboard(self);
					searchProfession();
					return true;
				}else{
					return false;
				}
			}
		});
	}
	
	/**
	 * 显示数据
	 * @param list
	 */
	private void fillData(final List<String> list){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				groupList.clear();
				childrens = new ArrayList<Map<String, List<String>>>();
				int count = list.size();
				for (int i = 0; i < count; i++) {
					Map<String, String> groupMap = new HashMap<String, String>();
					
					String[] values = list.get(i).toString().split(";");
					
					groupMap.put("pname", values[1]);
					groupList.add(groupMap);
					
					getProfessionTypeName(values[0]);
					
					Map<String, List<String>> childMap = new HashMap<String, List<String>>();
					childMap.put("childitem", llistChild);
					childrens.add(childMap);
				}
				
				childList = new ArrayList<List<String>>(); 
				for (Map<String, List<String>> mapChildItem : childrens) {
					List<String> lisItem = mapChildItem.get("childitem");
//					List<String> tmp = new ArrayList<String>(); 
//					for (String name : lisItem) {
//						tmp.add(name);
//					}
					childList.add(lisItem);
				}
				
				mHandler.sendEmptyMessage(12);
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
			getAllProfession("");
			clearView.setVisibility(View.GONE);
			break;
		case R.id.clear:
			whichTab = 0;
			index_search_edit.setText("");
			clearView.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 查询按钮
	 */
	private void searchProfession(){
		String keyword = getEditTextValue(index_search_edit);
		if (RegexUtil.checkNotNull(keyword)) {
			whichTab = 1;
			clearView.setVisibility(View.VISIBLE);
			getAllProfession(keyword);
		}
	}
	
	/**
	 * 获取所有职业分类
	 * @param keyword
	 */
	private void getAllProfession(final String keyword) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<String> llist = new ArrayList<String>();
				Cursor cursor = null;
				try {
					String sql = "select id,cname from profession"
								+ " where cname like '%"+ keyword +"%' or pinying like '%"+ keyword +"%'";
					cursor = MyApplication.database.rawQuery(sql, null);
					if (cursor.getCount() > 0) {
						while (cursor.moveToNext()) {
							llist.add(cursor.getString(cursor.getColumnIndex("id")) + ";"+ cursor.getString(cursor.getColumnIndex("cname")));
						}
						mHandler.sendMessage(mHandler.obtainMessage(11, llist));
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
	 * 获得职业分类下的子类
	 * @param type
	 */
	private void getProfessionTypeName(final String type) {
		llistChild = new ArrayList<String>();
		Cursor cursor = null;
		try {
			String sql = "select cname from professioname"+ " where pid = '"+ type +"'";
			cursor = MyApplication.database.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					llistChild.add(cursor.getString(cursor.getColumnIndex("cname")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
}
