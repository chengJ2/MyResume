package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.utils.CommUtil;
import com.me.resume.views.CommScrollView;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: EvaluationMoreActivity 
* @Description: 自我评价更多详情
* @date 2016/5/23 下午1:22:03 
*
 */
public class EvaluationMoreActivity extends BaseActivity implements OnClickListener{

	private CommScrollView scrollview;
	
	private TextView item1,item2,item3,item4;
	
	private ListView characterListView;
	
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
		
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		getData();
		
		showCharacterData(getCharacterListData(1));
	}

	private int getData(){
		queryWhere = "select count(*) as cun from " + CommonText.CHARACTER + " where userId = '"+ uTokenId+"'";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("cun").length > 0) {
			int cun = CommUtil.parseInt(commMapArray.get("cun")[0]);
			if (cun > 0) {
				setRightIcon(R.drawable.icon_done);
			}else{
				setRightIconVisible(View.GONE);
			}
			return cun;
		}else{
			setRightIconVisible(View.GONE);
		}
		return 0;
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
	
	/**
	 * 
	 * @Description: 显示数据
	 */
	private void showCharacterData(List<String> mList){
		commAdapter = new CommonBaseAdapter<String>(self,mList,R.layout.character_simple_list_item) {
		
		@Override
		public void convert(final ViewHolder holder, String item, final int position) {
				
				final String characterStr = mList.get(position);
				holder.setText(R.id.item_text,mList.get(position));

				queryWhere = "select * from " + CommonText.CHARACTER 
						+ " where character = '"+ characterStr +"' and userId = '"+ uTokenId+"'";
				commMapArray = dbUtil.queryData(self, queryWhere);
				if (commMapArray != null && commMapArray.get("character").length > 0) {
					holder.setViewVisible(R.id.item_check, View.VISIBLE);
				}else{
					holder.setViewVisible(R.id.item_check, View.GONE);
				}
				
				getData();
				
				holder.setOnClickEvent(R.id.itemcheckLayout, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						addCharacterData(characterStr);
					}
				});
				
				holder.setOnClickEvent(R.id.item_check, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						addCharacterData(characterStr);
					}
				});
				
			}
		};
		characterListView.setAdapter(commAdapter);
		
	}
	
	/**
	 * 
	 * @Description: 添加性格标签
	 * @param s 性格
	 */
	private void addCharacterData(String s){
		queryWhere = "select * from " + CommonText.CHARACTER 
				+ " where character = '"+ s +"' and userId = '"+ uTokenId+"'";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray == null) {
			if (getData() > 3) {
				toastMsg(R.string.ev_info_choose_character_cun);
				return;
			}else{
				ContentValues cValues = new ContentValues();
				cValues.put("userId", uTokenId);
				cValues.put("character", s);
				queryResult = dbUtil.insertData(self, CommonText.CHARACTER, cValues);
				if (queryResult) {
					setRightIcon(R.drawable.icon_done);
					commAdapter.notifyDataSetChanged();
				}
			}
		}else{
			queryWhere = "delete from " + CommonText.CHARACTER 
					+ " where character = '"+ s +"' and userId = '"+ uTokenId+"'";
			dbUtil.deleteData(self, queryWhere);
			commAdapter.notifyDataSetChanged();
			
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item1:
			showCharacterData(getCharacterListData(1));
			break;
		case R.id.item2:
			showCharacterData(getCharacterListData(2));
			break;
		case R.id.item3:
			showCharacterData(getCharacterListData(3));
			break;
		case R.id.item4:
			showCharacterData(getCharacterListData(4));
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			queryWhere = "select * from " + CommonText.CHARACTER + " where userId = '"+ uTokenId+"'";
			commMapArray = dbUtil.queryData(self, queryWhere);
			if (commMapArray != null) {
				int count = commMapArray.get("userId").length;
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < count; i++) {
					sb.append(commMapArray.get("character")[i]).append(";");
				}
				
				Intent intent=new Intent();
		        intent.putExtra(Constants.CHARACTER, sb.toString());
		        setResult(Constants.RESULT_CODE, intent);
		        scrollToFinishActivity();
			}
			break;
		default:
			break;
		}
		
	}
}
