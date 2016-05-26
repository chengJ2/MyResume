package com.me.resume.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.tools.L;
import com.me.resume.views.CommScrollView;
import com.me.resume.views.CustomListView;

/**
 * 
* @ClassName: EvaluationMoreActivity 
* @Description: 自我评价更多详情
* @author Comsys-WH1510032 
* @date 2016/5/23 下午1:22:03 
*
 */
public class EvaluationMoreActivity extends BaseActivity implements OnClickListener{

	private CommScrollView scrollview;
	
	private TextView item1,item2,item3,item4;
	
	private CustomListView characterListView;
	
	private int style = 1;
	
	private CommonBaseAdapter<String> commAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_evalution_more_layout, null);
		boayLayout.addView(v);
		
		findView();
		
		initView();
	}

	
	private void findView() {
		scrollview = findView(R.id.scrollview);
		
		item1 = findView(R.id.item1);
		item2 = findView(R.id.item2);
		item3 = findView(R.id.item3);
		item4 = findView(R.id.item4);
		
		characterListView = findView(R.id.characterListView);
		
		item1.setOnClickListener(this);
		item2.setOnClickListener(this);
		item3.setOnClickListener(this);
		item4.setOnClickListener(this);
		
		scrollview.scrollTo(0, 0);
	}
	
	private void initView() {
		setTopTitle(R.string.ev_info_character);
		setMsgHide();
		setRightIcon(R.drawable.icon_sync);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		showCharacterData(getCharacterListData(1));
	}

	
	private List<String> getCharacterListData(final int style){
		List<String> mList = new ArrayList<String>();
		Cursor cursor = null;
		try {
			String sql = "select * from character where cid = '" + style +"'";
			cursor = MyApplication.database.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					mList.add(cursor.getString(cursor.getColumnIndex("name")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return mList;
	}
	
	private Map<Integer,Boolean> states = new HashMap<Integer,Boolean>();
	private List<String> charData = new ArrayList<String>();
	/**
	 * 
	 * @Description: 显示数据
	 */
	private void showCharacterData(List<String> mList){
		commAdapter = new CommonBaseAdapter<String>(self,mList,R.layout.character_simple_list_item) {
		
		@Override
		public void convert(final ViewHolder holder, String item, final int position) {
				holder.setText(R.id.item_text,mList.get(position));
				/*if (!charData.isEmpty()) {
					charData.clear();
				}*/
				if (states.isEmpty()) {
					states.put(position, false);
//				}else{
//					L.d("======ddd======" + states.get(position));
//					if (states.get(position)) {
//						holder.setViewVisible(R.id.item_radio_btn, View.VISIBLE);
//						charData.add(mList.get(position));
//					}else{
//						holder.setViewVisible(R.id.item_radio_btn, View.GONE);
//						charData.remove(mList.get(position));
//					}
				}
				
				holder.setOnClickEvent(R.id.itemcheckLayout, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						
						if (!states.get(position)) {
							states.put(position, true);
							holder.setViewVisible(R.id.item_radio_btn, View.VISIBLE);
						}else{
							states.put(position, false);
							holder.setViewVisible(R.id.item_radio_btn, View.GONE);
						}
						
						notifyDataSetChanged();
					}
				});
				
				holder.setOnClickEvent(R.id.item_check, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						if (!states.get(position)) {
							states.put(position, true);
						}else{
							states.put(position, false);
						}
						
						notifyDataSetChanged();
					}
				});
				
			}
		};
		characterListView.setAdapter(commAdapter);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item1:
			style = 1;
			break;
		case R.id.item2:
			style = 2;
			break;
		case R.id.item3:
			style = 3;
			break;
		case R.id.item4:
			style = 4;
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			
			L.d("===charData=="+charData);
			
			
//			Intent intent=new Intent();
//	        intent.putExtra(Constants.CITY, city);
//	        setResult(Constants.RESULT_CODE, intent);
//			scrollToFinishActivity();
			break;
		default:
			break;
		}
		showCharacterData(getCharacterListData(style));
	}
}
