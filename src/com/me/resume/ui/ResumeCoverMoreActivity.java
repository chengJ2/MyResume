package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.utils.CommUtil;

/**
 * 更多简历封面
 * @author Administrator
 *
 */
public class ResumeCoverMoreActivity extends BaseActivity implements OnClickListener{

	private GridView covermoregridview;
	private TextView msgText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		
		View v = View.inflate(self,R.layout.resume_cover_more_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.item_text61);
		
		setMsgHide();
		
		setRightIconVisible(View.INVISIBLE);
		
		setRight2IconVisible(View.GONE);
		
		setfabLayoutVisible(View.GONE);
		
		covermoregridview = findView(R.id.covermoregridview);
		msgText = findView(R.id.msgText);
		msgText.setVisibility(View.VISIBLE);
		msgText.setText(CommUtil.getStrValue(self, R.string.item_text43));
		
		
		getReCoverMoreData();
	}
	
	/**
	 * @Description: 面试分享心得
	 */
	private void getReCoverMoreData(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		requestData("pro_getcover_info", 2, params, values, new HandlerData() {
			@Override
			public void error() {
				msgText.setVisibility(View.VISIBLE);
				msgText.setText(CommUtil.getStrValue(self, R.string.en_nodata));	
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					msgText.setVisibility(View.GONE);
					setCoverData(covermoregridview,map,false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		default:
			break;
		}
	}
}
