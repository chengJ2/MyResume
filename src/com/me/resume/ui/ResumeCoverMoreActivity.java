package com.me.resume.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.views.CustomGridView;

/**
 * 更多简历封面
 * @author Administrator
 *
 */
public class ResumeCoverMoreActivity extends BaseActivity implements OnClickListener{

	private CustomGridView covermoregridview,covermoregridview_update;
	private TextView msgText;
	
	private TextView localText,updateText;
	private ImageView localupdown,updateupdown;
	
	// 构建cover本地数据
	private String[] id = {"1","2","3","4","5","6"};
	private String[] note = {"只为心中淡淡的梦想","天行健以自强不息","阅历以存储更多的书香气质",
							 "天行健以自强不息","阅历以存储更多的书香气质","只为心中淡淡的梦想"};
	private String[] url = {R.drawable.default_cover1+"",R.drawable.default_cover2+"",R.drawable.default_cover3+"",
							R.drawable.default_cover4+"",R.drawable.default_cover5+"",R.drawable.default_cover6+""};
	
	private boolean isShow = false;
	
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
		
		localText = (TextView) ((RelativeLayout)findView(R.id.localcover)).getChildAt(0);
		localText.setText(getStrValue(R.string.item_cover_local));
		updateText = (TextView) ((RelativeLayout)findView(R.id.updatecover)).getChildAt(0);
		updateText.setText(getStrValue(R.string.item_cover_update));
		
		localupdown = (ImageView) ((RelativeLayout)findView(R.id.localcover)).getChildAt(1);
		updateupdown = (ImageView) ((RelativeLayout)findView(R.id.updatecover)).getChildAt(1);
		
		covermoregridview = findView(R.id.covermoregridview);
		covermoregridview_update = findView(R.id.covermoregridview_update);
		msgText = findView(R.id.msgText);
		
		setCoverView(true);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				getReCoverMoreData();
			}
		}, 800);
		
		localupdown.setImageResource(R.drawable.icon_arrow_up);
		covermoregridview.setVisibility(View.VISIBLE);
		
		updateupdown.setImageResource(R.drawable.icon_arrow_down);
		covermoregridview_update.setVisibility(View.GONE);
		
		localupdown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isShow = !isShow;
				if (!isShow) {
					localupdown.setImageResource(R.drawable.icon_arrow_up);
					covermoregridview.setVisibility(View.VISIBLE);
				}else{
					localupdown.setImageResource(R.drawable.icon_arrow_down);
					covermoregridview.setVisibility(View.GONE);
				}
			}
		});
		
		updateupdown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isShow = !isShow;
				if (!isShow) {
					updateupdown.setImageResource(R.drawable.icon_arrow_up);
					covermoregridview_update.setVisibility(View.VISIBLE);
				}else{
					updateupdown.setImageResource(R.drawable.icon_arrow_down);
					covermoregridview_update.setVisibility(View.GONE);
				}
			}
		});
		
		
//		msgText.setVisibility(View.VISIBLE);
//		msgText.setText(getStrValue(R.string.item_text43));
		
		
	}
	
	/**
	 * 初始化封面视图(default)
	 * @param islocal
	 */
	private void setCoverView(boolean islocal){
		Map<String, List<String>> map = new HashMap<String,List<String>>();
		
		List<String> idList = new ArrayList<String>();
		idList = Arrays.asList(id);
		map.put("id",idList);
		
		List<String> noteList = new ArrayList<String>();
		noteList = Arrays.asList(note);
		map.put("note",noteList);
		
		List<String> urlList = new ArrayList<String>();
		urlList = Arrays.asList(url);
		map.put("url",urlList);
		
		setCoverData(covermoregridview,map,islocal);
	}
	
	
	/**
	 * 面试分享心得
	 */
	private void getReCoverMoreData(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		requestData("pro_getcover_info", 2, params, values, new HandlerData() {
			@Override
			public void error() {
				set3Msg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					/*msgText.setVisibility(View.GONE);*/
					setCoverData(covermoregridview_update,map,false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				/*msgText.setVisibility(View.VISIBLE);
				msgText.setText(getStrValue(R.string.en_nodata));*/	
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
