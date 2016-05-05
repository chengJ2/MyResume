package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.me.resume.R;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;

/**
 * 意见反馈
 * @author Administrator
 *
 */
public class FeedBackActivity extends BaseActivity {

	private EditText feeddesc,feedcontact;
	private Button feedcommit;
	
	private String feeddescStr,feedcontactStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		boayLayout.removeAllViews();
		
		View v = View.inflate(self,R.layout.feedback_layout, null);
		boayLayout.addView(v);
	
		findViews();
		initViews();
	}
	
	private void findViews(){
		feeddesc = findView(R.id.feeddesc);
		feedcontact = findView(R.id.feedcontact);
		feedcommit = findView(R.id.feedcommit);
	}
	
	private void initViews(){
		setTopTitle(R.string.settings_item4);
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		feedcommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (CommUtil.isNetworkAvailable(self)) {
					if(checkInfo()){
						postData();
					}
				}else{
					set3Msg(R.string.check_network);
				}
			}
		});
	}
	
	private void getFeildValue(){
		feeddescStr = CommUtil.getEditTextValue(feeddesc);
		feedcontactStr = CommUtil.getEditTextValue(feedcontact);
	}
	
	private boolean checkInfo(){
		getFeildValue();
		if(!RegexUtil.checkNotNull(feeddescStr)){
			setMsg(R.string.feedback_info_1);
			return false;
		}
		if(!RegexUtil.checkNotNull(feedcontactStr)){
			setMsg(R.string.feedback_info_2);
			return false;
		}
		return true;
	}
	
	private void postData(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_feeddesc");
		params.add("p_feedcontact");
		
		values.add(feeddescStr);
		values.add(feedcontactStr);
		
		requestData("pro_set_feedback", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("msg").get(0).equals("200")) {
						set3Msg(R.string.feedback_info_3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}