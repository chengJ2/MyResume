package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.utils.CommUtil;

/**
 * 更多简历封面
 * @author Administrator
 *
 */
public class ResumeCoverMoreActivity extends BaseActivity implements OnClickListener{

	private GridView covermoregridview;
	
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
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					setCoverData(map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 简历预览封面
	 * @param map
	 */
	private void setCoverData(final Map<String, List<String>> map){
		commapBaseAdapter = new CommForMapBaseAdapter(self,map,R.layout.home_cover_gridview_item,"id") {
			
			@Override
			public void convert(ViewHolder holder, List<String> item, int position) {
					holder.showImage(R.id.item1,
							CommUtil.getHttpLink(map.get("url").get(position)),false);
				
				holder.setText(R.id.item2, map.get("note").get(position));
				
				holder.setOnClickEvent(R.id.item3, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		};
		
		covermoregridview.setAdapter(commapBaseAdapter);
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
