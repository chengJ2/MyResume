package com.me.resume.ui;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.views.CustomFAB;

/**
 * 
* @ClassName: WorkExperienceActivity 
* @Description: 工作经历 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午3:39:01 
*
 */
public class WorkExperienceActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private CustomFAB saveandgo;
	
	private TextView we_zwyx;
	
	private String[] item_text = null;
	
	private List<String> mList = null;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				DialogUtils.dismissPopwindow();
				break;
			case 2:
				int position = (int) msg.obj;
				we_zwyx.setText(mList.get(position));
				DialogUtils.dismissPopwindow();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workexperience_layout);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		
		rightLable.setText(CommUtil.getStrValue(WorkExperienceActivity.this, R.string.review_resume));
		toptext.setText(CommUtil.getStrValue(WorkExperienceActivity.this, R.string.resume_workexperience));
		
		saveandgo = findView(R.id.saveandgo);
		
		saveandgo.setOnClickListener(this);
		leftLable.setOnClickListener(this);
		
		we_zwyx = findView(R.id.we_zwyx);
		we_zwyx.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saveandgo:
			ActivityUtils.startActivity(WorkExperienceActivity.this, MyApplication.PACKAGENAME 
					+ ".ui.EvaluationActivity");
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(WorkExperienceActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		case R.id.we_zwyx:
			item_text = CommUtil.getArrayValue(WorkExperienceActivity.this,R.array.we_qwyx_values); 
			mList = Arrays.asList(item_text);
			DialogUtils.showPopWindow(WorkExperienceActivity.this, we_zwyx, 
					R.string.we_info_expectedsalary, mList, 
					mHandler);
			
			break;
		default:
			break;
		}
		
	}
}
