package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 添加分享/编辑分享内容
* @date 2016/10/14 下午1:35:51 
*
 */
public class UserShareActivity extends BaseActivity {

	private EditText content;
	private RadioGroup rg_status;
	private RadioButton ispublic,isprivate;
	private Button submitBtn;
	
	private String status = "1";
	
	private String sid = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bodyLayout.removeAllViews();
		View v = View.inflate(self, R.layout.user_share_layout, null);
		bodyLayout.addView(v);

		content = findView(R.id.input_share);
		rg_status = findView(R.id.rg_status);
		ispublic = findView(R.id.ispublic);
		isprivate = findView(R.id.isprivate);
		submitBtn  = findView(R.id.submit_btn);
		
		Bundle shareinfo = getIntent().getBundleExtra(Constants.SHAREINFO);
		if(shareinfo != null){
			setTopTitle(R.string.personal_c_item22);
			sid = (String) shareinfo.get("sid");
			String sContent = (String) shareinfo.get("scontent");
			content.setText(sContent);
		}else{
			setTopTitle(R.string.personal_c_item21);
		}
		
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);

		submitBtn.setOnClickListener(this);
		
		rg_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == ispublic.getId()) {
					status = "1";
				}else if (checkedId == isprivate.getId()){
					status = "2";
				}
			}
		});
		
		content.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					CommUtil.hideKeyboard(self);
					setShare();
					return true;
				}else{
					return false;
				}
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
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.submit_btn:
			setShare();
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 更新状态
	 * @param status
	 */
	private void setShare(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_userId");
		params.add("p_sId");
		params.add("p_content");
		params.add("p_status");
		
		values.add(uTokenId);
		values.add(sid);
		values.add(content.getText().toString().trim());
		values.add(status);
		
		int style = 1;
		if(sid != null && !"".equals(sid)){
			style = 1;
		}else{
			style = 2;
		}
		
		requestData("pro_setmyshare2", style, params, values, new HandlerData() {
			@Override
			public void error() {
				set2Msg(getStrValue(R.string.timeout_network));
			}
			
			public void success(final Map<String, List<String>> map) {
				try {
					if (map.get(ResponseCode.MSG).get(0).equals(ResponseCode.RESULT_OK)) {
						preferenceUtil.setPreferenceData(UserInfoCode.USERSHARE, true);
						scrollToFinishActivity();
					}
				} catch (Exception e) {
					setMsgVisibility(View.GONE);
					L.e(e.getMessage());
				}
			}

			@Override
			public void noData() {
				setMsgVisibility(View.GONE);
			}
		});
	}
	
}
