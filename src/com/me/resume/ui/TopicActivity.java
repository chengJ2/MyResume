package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.tools.ImageLoader;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.views.MarqueeText;

/**
 * 
* @ClassName: TopicActivity 
* @Description: 面试简历相关主题
* @author Comsys-WH1510032 
* @date 2016/4/20 上午10:49:59 
*
 */
public class TopicActivity extends BaseActivity implements OnClickListener{

	private MarqueeText topic_title;
	private TextView topic_from,topic_datetime;
	private ImageView topic_frompic;
	
	private TextView topic_content;
	
	private ImageLoader mImageLoader;
	
	private String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_topic_layout, null);
		boayLayout.addView(v);
		
		initView();
		
		topic_title = findView(R.id.topic_title);
		topic_from = findView(R.id.topic_from);
		topic_datetime = findView(R.id.topic_datetime);
		topic_frompic = findView(R.id.topic_frompic);
		topic_content = findView(R.id.topic_content);
		
		mImageLoader=new ImageLoader(self);
		
	}
	
	private void initView(){
		title = getIntent().getStringExtra("title");
		String[] titleArr = title.split(";");
		
		setTopTitle(titleArr[0]);
		
		setMsgHide();
		
		setRight2IconVisible(View.GONE);
		
		setfabLayoutVisible(View.GONE);
		
		getTopicData(titleArr[1]);
	}
	
	/**
	 * 获取话题
	 */
	private void getTopicData(String type){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_type");
		values.add(type);
		
		requestData("pro_gettopic_info", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					
					topic_title.setText(map.get("title").get(0));
					topic_from.setText(map.get("from").get(0));
					topic_datetime.setText(map.get("createtime").get(0));
					String frompicUrl = map.get("from_url").get(0);
					if (RegexUtil.checkNotNull(frompicUrl)) {
						topic_frompic.setVisibility(View.VISIBLE);
						mImageLoader.DisplayImage(CommUtil.getHttpLink(frompicUrl), topic_frompic, false, false);
					}else{
						topic_frompic.setVisibility(View.GONE);
					}
					topic_content.setText(Html.fromHtml(map.get("detail").get(0)));
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
		case R.id.right_icon:
			// TODO
			break;
		default:
			break;
		}
		
	}
}
