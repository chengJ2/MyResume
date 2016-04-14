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

/**
 * 
* @ClassName: OtherInfoActivity 
* @Description: 其他信息 
* @author Comsys-WH1510032 
* @date 2016/4/6 下午1:32:58 
*
 */
public class OtherInfoActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private TextView info_language,info_literacyskills,info_listeningspeaking;
	
	
	private List<String> mList = null;
	
	private int whichTab = 1;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				DialogUtils.dismissPopwindow();
				break;
			case 2:
				int position = (int) msg.obj;
				if (whichTab == 1) {
					info_language.setText(mList.get(position));
					info_literacyskills.setEnabled(true);
					info_listeningspeaking.setEnabled(true);
				}else if(whichTab == 2){
					info_literacyskills.setText(mList.get(position));
				}else if(whichTab == 3){
					info_listeningspeaking.setText(mList.get(position));
				}
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
		setContentView(R.layout.activity_otherinfo_layout);
		
		findViews();
		
		initViews();
		
	}
	
	private void findViews(){
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		
		info_language = findView(R.id.info_language);
		info_literacyskills = findView(R.id.info_literacyskills);
		info_listeningspeaking = findView(R.id.info_listeningspeaking);
		
		info_language.setOnClickListener(this);
		info_literacyskills.setOnClickListener(this);
		info_listeningspeaking.setOnClickListener(this);
	}
	
	private void initViews(){
		toptext.setText(CommUtil.getStrValue(self, R.string.resume_otherinfo));
		rightLable.setText(CommUtil.getStrValue(self, R.string.review_resume));
		
		if (CommUtil.textIsNull(info_language)) {
			info_literacyskills.setEnabled(false);
			info_listeningspeaking.setEnabled(false);
		}
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(self, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		case R.id.info_language:
			whichTab = 1;
			getValues(R.array.oi_wysp_values,info_language,R.string.ot_info_language);
			break;
		case R.id.info_literacyskills:
			whichTab = 2;
			getValues(R.array.oi_tsdx_values,info_literacyskills,R.string.ot_info_literacyskills);
			break;
		case R.id.info_listeningspeaking:
			whichTab = 3;
			getValues(R.array.oi_tsdx_values,info_listeningspeaking,R.string.ot_info_listeningspeaking);
			break;
		default:
			break;
		}
	}

	private void getValues(int array,View parent,int resId) {
		String[] item_text = CommUtil.getArrayValue(self,array); 
		mList = Arrays.asList(item_text);
		DialogUtils.showPopWindow(self, parent, resId, mList, mHandler);
	}
}
