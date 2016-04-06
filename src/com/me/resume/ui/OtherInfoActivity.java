package com.me.resume.ui;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Text;

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
* @ClassName: OtherInfoActivity 
* @Description: 其他信息 
* @author Comsys-WH1510032 
* @date 2016/4/6 下午1:32:58 
*
 */
public class OtherInfoActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private CustomFAB saveGo;
	
	private TextView info_waiyu,info_dx,info_ts;
	
	private String[] item_text = null;
	
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
					info_waiyu.setText(mList.get(position));
					info_dx.setEnabled(false);
					info_ts.setEnabled(false);
				}else if(whichTab == 2){
					info_dx.setText(mList.get(position));
				}else if(whichTab == 2){
					info_ts.setText(mList.get(position));
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
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		toptext.setText(CommUtil.getStrValue(OtherInfoActivity.this, R.string.resume_otherinfo));
		rightLable.setText(CommUtil.getStrValue(OtherInfoActivity.this, R.string.review_resume));
		
		info_waiyu = findView(R.id.oi_info_waiyu);
		info_dx = findView(R.id.oi_info_dxnl);
		info_ts = findView(R.id.oi_info_tsnl);
		info_waiyu.setOnClickListener(this);
		info_dx.setOnClickListener(this);
		info_ts.setOnClickListener(this);
		
		if (CommUtil.textIsNull(info_waiyu)) {
			info_dx.setEnabled(true);
			info_ts.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.saveGo:
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(OtherInfoActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		case R.id.oi_info_waiyu:
			whichTab = 1;
			mList.clear();
			item_text = CommUtil.getArrayValue(OtherInfoActivity.this,R.array.oi_wysp_values); 
			mList = Arrays.asList(item_text);
			DialogUtils.showPopWindow(OtherInfoActivity.this, info_waiyu, 
					R.string.we_info_expectedsalary, mList, 
					mHandler);
			break;
		case R.id.oi_info_dxnl:
			whichTab = 2;
			mList.clear();
			item_text = CommUtil.getArrayValue(OtherInfoActivity.this,R.array.oi_tsdx_values); 
			mList = Arrays.asList(item_text);
			DialogUtils.showPopWindow(OtherInfoActivity.this, info_waiyu, 
					R.string.we_info_expectedsalary, mList, 
					mHandler);
			break;
		case R.id.oi_info_tsnl:
			whichTab = 3;
			mList.clear();
			item_text = CommUtil.getArrayValue(OtherInfoActivity.this,R.array.oi_tsdx_values); 
			mList = Arrays.asList(item_text);
			DialogUtils.showPopWindow(OtherInfoActivity.this, info_waiyu, 
					R.string.we_info_expectedsalary, mList, 
					mHandler);
			break;
		default:
			break;
		}
	}
}
