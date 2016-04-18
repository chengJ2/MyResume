package com.me.resume.ui;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
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

import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.Constants;
import com.me.resume.utils.L;

/**
 * 
* @ClassName: IndustryTypeActivity 
* @Description: 行业类别
* @author Comsys-WH1510032 
* @date 2016/4/18 上午10:58:05 
*
 */
public class IndustryTypeActivity extends SwipeBackActivity {

	private TextView toptext,leftLable,rightLable;
	
	private EditText index_search_edit;
	private ImageView clearView;
	
	private ListView industry_listview;
	
	private CommonBaseAdapter<String> commAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_industry_type_layout);
		
		toptext = findView(R.id.top_text);
		toptext.setText(CommUtil.getStrValue(self, R.string.resume_industrytype));
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		rightLable.setVisibility(View.GONE);
		leftLable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scrollToFinishActivity();
			}
		});
		
		index_search_edit = findView(R.id.index_search_edit);
		industry_listview = findView(R.id.industry_listview);
		clearView = findView(R.id.clear);
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
					if (commAdapter != null) {
						
//						commAdapter.getFilter().filter(s.toString());;
//						commAdapter.notifyDataSetChanged();
					}
				}else{
					clearView.setVisibility(View.GONE);
				}
			}
		});
		
		index_search_edit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId ==EditorInfo.IME_ACTION_SEARCH) {
					CommUtil.hideKeyboard(self);
					if (!CommUtil.EditTextIsNull(index_search_edit)) {
//						if (Utils.isNetwork(self)) {
//							handler.sendMessage(handler.obtainMessage(1));
//						}
						industry_listview.setFilterText(index_search_edit.getText().toString().trim());
						
					}
					return true;
				}else{
					return false;
				}
			}
		});
		
		item_values = CommUtil.getArrayValue(self,R.array.info_industrytype_values); 
		mList = Arrays.asList(item_values);
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
}
