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
import android.widget.ImageView;
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
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;

/**
 * 
* @ClassName: IndustryTypeActivity 
* @Description: 行业类别
* @date 2016/4/18 上午10:58:05 
*
 */
public class IndustryTypeActivity extends BaseActivity implements OnClickListener{

	private EditText index_search_edit;
	private ImageView clearView;
	private TextView search_cancle;
	
	private ListView industry_listview;
	
	private CommonBaseAdapter<String> commAdapter;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				getAllIndustry("");
				break;
			case 11:
				mList = (List<String>) msg.obj;
				if (mList != null && mList.size() > 0) {
					fillData(mList);
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
		View v = View.inflate(self,R.layout.activity_industry_type_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.resume_industrytype);
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		findViews();
		
		mHandler.sendEmptyMessage(10);
		
		searchEdit();
	}
	
	
	private void findViews(){
		index_search_edit = findView(R.id.index_search_edit);
		clearView = findView(R.id.clear);
		search_cancle = findView(R.id.search_cancle);
		clearView = findView(R.id.clear);
		
		industry_listview = findView(R.id.industry_listview);
		
		
		left_icon.setOnClickListener(this);
		clearView.setOnClickListener(this);
		search_cancle.setOnClickListener(this);
	}
	
	private void searchEdit(){
		index_search_edit.setHint(CommUtil.getStrValue(self, R.string.hint_industry_text));
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
				if (s.toString() != null && !"".equals(s.toString())) {
					clearView.setVisibility(View.VISIBLE);
					getAllIndustry(s.toString());
				}else{
					getAllIndustry("");
					clearView.setVisibility(View.GONE);
				}
			}
		});
		
		index_search_edit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId ==EditorInfo.IME_ACTION_SEARCH) {
					CommUtil.hideKeyboard(self);
					searchIndustry();
					return true;
				}else{
					return false;
				}
			}
		});
	}
	
	
	private void fillData(List<String> mList){
		
		commAdapter = new CommonBaseAdapter<String>(self,mList,R.layout.listview_item_text) {
			
			@Override
			public void convert(ViewHolder holder, String item, final int position) {
				// TODO Auto-generated method stub
				holder.setText(R.id.item_textview, mList.get(position));
				holder.setOnClickEvent(R.id.item_textview, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						Intent intent=new Intent();
				        intent.putExtra("name", mList.get(position));
				        setResult(Constants.RESULT_CODE, intent);
						scrollToFinishActivity();
					}
				});
			}

		};
		industry_listview.setAdapter(commAdapter);
	}
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.search_cancle:
			whichTab = 0;
			CommUtil.hideKeyboard(self);
			index_search_edit.setText("");
			getAllIndustry("");
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
	
	private void searchIndustry(){
		String keyword = CommUtil.getEditTextValue(index_search_edit);
		if (RegexUtil.checkNotNull(keyword)) {
			whichTab = 1;
			clearView.setVisibility(View.VISIBLE);
			getAllIndustry(keyword);
		}
	}
	
	private void getAllIndustry(final String keyword) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<String> mList = new ArrayList<String>();
				Cursor cursor = null;
				try {
					String sql = "select chinaname,englishname from industry"
								+ " where chinaname like '%"+ keyword +"%' or pinying like '%"+ keyword +"%'";
					cursor = MyApplication.database.rawQuery(sql, null);
					if (cursor.getCount() > 0) {
						while (cursor.moveToNext()) {
							mList.add(cursor.getString(cursor.getColumnIndex("chinaname")));
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
	
	
}
