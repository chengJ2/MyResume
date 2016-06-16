package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.tools.ImageLoader;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.MarqueeText;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: TopicActivity 
* @Description: 相关话题预览
* @date 2016/4/20 上午10:49:59 
*
 */
public class TopicViewActivity extends BaseActivity implements OnClickListener{

	private LinearLayout viewlayout;
	private TextView msgText;
	
	private MarqueeText topic_title;
	private TextView topic_from,topic_datetime;
	private ImageView topic_frompic,topic_frompic2;
	
	private TextView topic_content,topic_content2;
	
	private String topicidType,title;
	
	private ImageLoader mImageLoader;
	
	private Map<String, List<String>> cmapList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_topic_layout, null);
		boayLayout.addView(v);
		
		findView();
		
		initView();
	}
	
	private void findView() {
		msgText = findView(R.id.msgText);
		viewlayout = findView(R.id.viewlayout);
		msgText.setVisibility(View.VISIBLE);
		viewlayout.setVisibility(View.GONE);
		msgText.setText(getStrValue(R.string.item_text43));
		
		topic_title = findView(R.id.topic_title);
		topic_from = findView(R.id.topic_from);
		topic_datetime = findView(R.id.topic_datetime);
		topic_frompic = findView(R.id.topic_frompic);
		topic_frompic2 = findView(R.id.topic_frompic2);
		topic_content = findView(R.id.topic_content);
		topic_content2 = findView(R.id.topic_content2);
	}

	private void initView(){
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		title = getIntent().getStringExtra("title");
		topicidType = getIntent().getStringExtra("tidtype");
		String[] titleArr = null;
		if (RegexUtil.checkNotNull(title)) {
			titleArr = title.split(";");
			setTopTitle(titleArr[0]);
			getTopicData(titleArr[1],"");
		}else{
			titleArr = topicidType.split(";");
			String titleStr = setArrayValue(R.array.review_link,CommUtil.parseInt(titleArr[1]));
			if (RegexUtil.checkNotNull(titleStr)) {
				setTopTitle(titleStr.substring(0, titleStr.indexOf(";")));
				getTopicData("",titleArr[0]);
			}
		}
		
		if(mImageLoader == null)
			mImageLoader = new ImageLoader(self);
		
	}
	
	/**
	 * 设置弹出窗数据
	 * @param array
	 * @param parent
	 * @param resId
	 */
	private String setArrayValue(int array,int value){
		String[] item_text = CommUtil.getArrayValue(self,array); 
		return item_text[value-1];
	}
	
	
	/**
	 * 获取话题
	 */
	private void getTopicData(String type,String topicId){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		String procName = "";
		
		if (RegexUtil.checkNotNull(type)) {
			params.add("p_type");
			values.add(type);
			
			procName = "pro_gettopic_info";
		}else if (RegexUtil.checkNotNull(topicId)) {
			params.add("p_id");
			values.add(topicId);
			
			procName = "pro_gettopic_byid";
		}
		
		if (!RegexUtil.checkNotNull(procName)) {
			return;
		}
		
		requestData(procName, 1, params, values, new HandlerData() {
			@Override
			public void error() {
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					cmapList = map;
					viewlayout.setVisibility(View.VISIBLE);
					msgText.setVisibility(View.GONE);
					topic_title.setText(map.get("title").get(0));
					topic_from.setText(map.get("from").get(0));
					topic_datetime.setText(TimeUtils.showTimeFriendly(map.get("createtime").get(0)));
					String frompicUrl = map.get("from_url").get(0);
					if (RegexUtil.checkNotNull(frompicUrl)) {
						topic_frompic.setVisibility(View.VISIBLE);
						mImageLoader.displayImage(CommUtil.getHttpLink(frompicUrl), topic_frompic, false, false);
					}else{
						topic_frompic.setVisibility(View.GONE);
					}
					
					topic_content.setText(Html.fromHtml(CommUtil.getHtml(map.get("detail").get(0))));
					
					String frompicUrl2 = map.get("from_url2").get(0);
					if (RegexUtil.checkNotNull(frompicUrl2)) {
						topic_frompic2.setVisibility(View.VISIBLE);
						mImageLoader.displayImage(CommUtil.getHttpLink(frompicUrl), topic_frompic2, false, false);
					}else{
						topic_frompic2.setVisibility(View.GONE);
					}
					
					/*String topiccontent2 = map.get("detail2").get(0);
					if (RegexUtil.checkNotNull(topiccontent2)) {
						topic_content2.setVisibility(View.VISIBLE);
						topic_content2.setText(Html.fromHtml(CommUtil.getHtml(topiccontent2)));
					}else{
						topic_frompic2.setVisibility(View.GONE);
					}*/
					
					String cid = map.get("id").get(0);
					queryWhere = "select * from " + CommonText.MYCOLLECTION 
							+ " where cid = "+ cid +" and userId = '"+ uTokenId+"' and type != 0";
					commMapArray = dbUtil.queryData(self, queryWhere);
					if (commMapArray == null) {
						setRightIcon(R.drawable.icon_collection_small_nor);
					}else{
						setRightIcon(R.drawable.icon_collection_small_sel);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void nodata() {
				msgText.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			if (cmapList != null) {
				String cid = cmapList.get("id").get(0);
				queryWhere = "select * from " + CommonText.MYCOLLECTION 
						+ " where cid = "+ cid +" and userId = '"+ uTokenId+"' and type != 0";
				commMapArray = dbUtil.queryData(self, queryWhere);
				if (commMapArray == null) {
					addCollection(cmapList);
				}else{
					queryWhere = "delete from " + CommonText.MYCOLLECTION 
							+ " where cid = "+ cid +" and userId = '"+ uTokenId+"' and type != 0";
					dbUtil.deleteData(self, queryWhere);
					toastMsg(R.string.item_text91);
					setRightIcon(R.drawable.icon_collection_small_nor);
				}
			}
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 添加到我的收藏
	 * @param map
	 * @param position
	 */
	private void addCollection(Map<String, List<String>> map){
		String cid = map.get("id").get(0);
		String title = map.get("title").get(0);
		String content = map.get("detail").get(0);
		if (RegexUtil.checkNotNull(content) && content.length() > 56) {
			content = content.substring(0, 56);
		}
		String from_url = map.get("from_url").get(0);
		String from = map.get("from").get(0);

		String type = map.get("type").get(0);	
		
		ContentValues cValues = new ContentValues();
		cValues.put("cId", cid);
		cValues.put("userId", uTokenId);
		cValues.put("topicId", cid);
		cValues.put("title", title);
		cValues.put("content", content);
		cValues.put("from_url", from_url);
		cValues.put("topic_from", from);
		cValues.put("createtime", TimeUtils.getCurrentTimeInString());
		cValues.put("type", type);// 0:面试分享心得; !0:话题

		queryResult = dbUtil.insertData(self, CommonText.MYCOLLECTION, cValues);
		if (queryResult) {
			toastMsg(R.string.item_text9);
			setRightIcon(R.drawable.icon_collection_small_sel);
			
			// TODO 同步到远端
		}
	}
	
	
}
