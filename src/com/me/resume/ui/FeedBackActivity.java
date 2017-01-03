package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 意见反馈
 * @author Administrator
 *
 */
public class FeedBackActivity extends BaseActivity {

	private EditText feeddesc,feedcontact;
	private Button feedcommit;
	
	private String feeddescStr,feedcontactStr;
	
	private LinearLayout feedback_layout;
	private CheckBox feedback_cb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		feedback_layout = findView(R.id.feedback_layout);
		feedback_cb = findView(R.id.feedback_cb);
		
	}
	
	private void initViews(){
		setTopTitle(R.string.settings_item4);
		setMsgHide();
		setRightIconVisible(View.INVISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		feedback_cb.setChecked(true);
		if(preferenceUtil.getPreferenceData(Constants.SET_FEEDBACK)){
			feedback_cb.setChecked(true);
		}else{
			feedback_cb.setChecked(false);
		}
		
		feedcommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (CommUtil.isNetworkAvailable(self)) {
					if(checkInfo()){
						postData();
					}
				}else{
					toastMsg(R.string.check_network);
				}
			}
		});
		
		feedback_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(preferenceUtil.getPreferenceData(Constants.SET_FEEDBACK)){
					feedback_cb.setChecked(true);
					preferenceUtil.setPreferenceData(Constants.SET_FEEDBACK,true);
				}else{
					feedback_cb.setChecked(false);
					preferenceUtil.setPreferenceData(Constants.SET_FEEDBACK,false);

				}
			}
		});
	}
	
	private void getFeildValue(){
		feeddescStr = getEditTextValue(feeddesc);
		feedcontactStr = getEditTextValue(feedcontact);
	}
	
	private boolean checkInfo(){
		getFeildValue();
		if(!RegexUtil.checkNotNull(feeddescStr)){
			setMsg(R.string.feedback_info_1);
			return false;
		}
		/*if(!RegexUtil.checkNotNull(feedcontactStr)){
			setMsg(R.string.feedback_info_2);
			return false;
		}*/
		return true;
	}
	
	private void postData(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_content");
		params.add("p_contact");
		params.add("p_userId");
		params.add("p_version");
		
		values.add(feeddescStr);
		values.add(feedcontactStr);
		values.add(uTokenId);
		values.add(CommUtil.getVersionName(self));
		
		requestData("pro_set_feedback", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				toastMsg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get(ResponseCode.MSG).get(0).equals(ResponseCode.RESULT_OK)) {
						toastMsg(R.string.feedback_info_3);
						feeddesc.setText("");
						feedcontact.setText("");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
