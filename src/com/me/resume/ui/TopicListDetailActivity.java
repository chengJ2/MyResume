package com.me.resume.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.me.resume.BaseActivity;
import com.me.resume.R;

/**
 * 
* @ClassName: TopicListDetailActivity 
* @Description: 话题列表详情
* @date 2016/5/12 上午9:07:55 
*
 */
public class TopicListDetailActivity extends BaseActivity implements OnClickListener{

	private ListView topicdetailListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_topic_list_layout, null);
		boayLayout.addView(v);
		
		topicdetailListView = findView(R.id.topicdetailListView);
		
		initView();
	}
	
	private void initView(){
		
//		setTopTitle(titleArr[0]);
		
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		default:
			break;
		}
	}
}
