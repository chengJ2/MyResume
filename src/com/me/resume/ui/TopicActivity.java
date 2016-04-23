package com.me.resume.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.CommUtil;

/**
 * 
* @ClassName: TopicActivity 
* @Description: 面试简历相关主题
* @author Comsys-WH1510032 
* @date 2016/4/20 上午10:49:59 
*
 */
public class TopicActivity extends BaseActivity implements OnClickListener{

	
	private TextView content;
	
	private String source = "<p>(1)一旦和用人单位约好面试时间后，一定要提前5-10分钟到达面试地点，以表示求职者的诚意，给对方以信任感，同时也可调整自己的心态，作一些简单的仪表准备，以免仓促上阵，手忙脚乱。为了做到这一点，一定要牢记面试的时间地点，有条件的同学最好能提前去一趟，以免因一时找不到地方或途中延误而迟到。"
						  + "如果迟到了，肯定会给招聘者留下不好的印象，甚至会丧失面试的机会。</p>"
						  + "<p>(2) 进入面试场合时不要紧张。</p>" 
						  + "<p>(3) 对用人单位的问题要逐一回答。</p> " 
						  + "<p>(4) 在整个面试过程中，在保持举止文雅大方，谈吐谦虚谨慎，态度积极热情。</p>";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_topic_layout, null);
		boayLayout.addView(v);
		
		setTopTitle(R.string.app_name);
		
		setMsgHide();
		
		setRight2IconVisible(View.GONE);
		
		content = findView(R.id.content);
		content.setText(Html.fromHtml(source));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			
			break;
		default:
			break;
		}
		
	}
}
