package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 
* @ClassName: WordsActivity 
* @Description: 留给企业的一句话
* @date 2016/6/12 上午11:50:36 
*
 */
public class WordsActivity extends BaseActivity {

	private EditText input_mywords;
	private ListView words_Listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bodyLayout.removeAllViews();
		View v = View.inflate(self,R.layout.main_mywords_layout, null);
		bodyLayout.addView(v);
		
		setTopTitle(R.string.resume_message);
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		input_mywords = findView(R.id.input_mywords);
		words_Listview = findView(R.id.words_Listview);
		input_mywords.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (RegexUtil.checkNotNull(s.toString())) {
					preferenceUtil.setPreferenceData(Constants.MYWORDS, s.toString());
				}
			}
		});
		input_mywords.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId ==EditorInfo.IME_ACTION_DONE) {
					CommUtil.hideKeyboard(self);
					String s = input_mywords.getText().toString().trim();
					if (RegexUtil.checkNotNull(s)) {
						preferenceUtil.setPreferenceData(Constants.MYWORDS, s);
					}
					return true;
				}else{
					return false;
				}
			}
		});
		
		commStrAdapter = new CommonBaseAdapter<String>(self, getWords(),
				R.layout.mywords_listview_item) {
			
			@Override
			public void convert(ViewHolder holder, String item,
					final int position) {
				holder.setText(R.id.itemName, mList.get(position));
			}
		};
		words_Listview.setAdapter(commStrAdapter);
		words_Listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				preferenceUtil.setPreferenceData(Constants.MYWORDS, commStrAdapter.getItem(position));
				scrollToFinishActivity();
			}
		});
	}
	
	/**
	 * 
	 * @Title:AddressActivity
	 * @Description: 获取留言信息
	 * @return List<String> 
	 */
	private List<String> getWords(){
		List<String> mList = new ArrayList<String>();
		Cursor cursor = null;
		try {
			String sql = "select * from message where type = 1";
			cursor = MyApplication.database.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					mList.add(cursor.getString(cursor.getColumnIndex("message")));
				}
				return mList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
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
