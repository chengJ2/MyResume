package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: UserRegisterActivity 
* @Description: 用户注册
* @date 2016/4/25 下午1:38:18 
*
 */
public class UserRegisterActivity extends BaseActivity implements OnClickListener{

	private EditText usernameEt;
	
	private EditText passwordEt;
	
	private EditText password2Et;
	
	private Button registBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_user_register_layout, null);
		boayLayout.addView(v);
		findViews();
		
		setTopTitle(R.string.action_user_regist);
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
	}

	private void findViews() {
		usernameEt = findView(R.id.edtTxt_username);
		passwordEt = findView(R.id.edtTxt_password);
		password2Et = findView(R.id.edtTxt2_password);
		
		registBtn = findView(R.id.btn_register);
		registBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.btn_register:
			if (CommUtil.isNetworkAvailable(self)) {
				getUserData();
			}else{
				toastMsg(R.string.check_network);
			}
			break;
		default:
			break;
		}
	}
	
	private String str_username,str_password;
	
	private void getUserData() {
		str_username = usernameEt.getText().toString();
		str_password = passwordEt.getText().toString();
		if(!RegexUtil.checkNotNull(str_username)){
			toastMsg(R.string.action_input_up_isnull);
			return;
		}
		
		if(!RegexUtil.checkNotNull(str_password)){
			toastMsg(R.string.action_input_password_isnull);
			return;
		}
		
		if(!CommUtil.getEditTextValue(passwordEt).equals(CommUtil.getEditTextValue(password2Et))){
			toastMsg(R.string.action_input_password_equal);
			return;
		}
		
		if (str_username.length() > 50) {
			toastMsg(R.string.action_input_up_isnull);
			return;
		}
		
		if (str_password.length() > 30) {
			toastMsg(R.string.action_input_up_isnull);
			return ;
		}
		
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		params.add("p_username");
		params.add("p_userpwd");
		params.add("p_deviceId");
		params.add("p_patform");
		
		values.add(str_username);
		values.add(CommUtil.getMD5(str_password));
		values.add(deviceID);
		values.add("app");
	
		requestData("pro_user_register", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				toastMsg(R.string.action_regist_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					registerSuccess(map);
				} catch (Exception e) {
					if(map.get("msg").get(0).equals("405")){
						toastMsg(R.string.register_repeatedusername);
					}
				}
			}
		});
	}
	
	/**
	 * 登录成功
	 * @param map
	 */
	private void registerSuccess(Map<String, List<String>> map){
		int useId = CommUtil.parseInt(map.get("userId").get(0));
		if (useId>0) {
			MyApplication.userId = useId;
			ContentValues cValues = new ContentValues();
			cValues.put("username", map.get("username").get(0));
			cValues.put("userpassword", map.get("password").get(0));
			cValues.put("deviceId", map.get("deviceId").get(0));
			cValues.put("patform", map.get("patform").get(0));
			cValues.put("createtime", map.get("createtime").get(0));
			cValues.put("lastlogintime", map.get("lastlogintime").get(0));
			
			queryResult = dbUtil.insertData(self, CommonText.USERINFO, cValues);
			if (queryResult) {
				cValues = new ContentValues();
				cValues.put("userId", map.get("userId").get(0));
				cValues.put("realname", map.get("realname").get(0));
				cValues.put("gender", map.get("gender").get(0));
				cValues.put("brithday", map.get("joinworktime").get(0));
				cValues.put("phone", map.get("phone").get(0));
				cValues.put("hometown", map.get("hometown").get(0));
				cValues.put("city", map.get("city").get(0));
				cValues.put("email", map.get("email").get(0));
				cValues.put("ismarry", map.get("ismarry").get(0));
				cValues.put("nationality", map.get("nationality").get(0));
				cValues.put("license", map.get("license").get(0));
				cValues.put("workingabroad", map.get("workingabroad").get(0));
				cValues.put("politicalstatus", map.get("politicalstatus").get(0));
				cValues.put("avator", map.get("avator").get(0));
				cValues.put("updatime", map.get("updatime").get(0));
				
				queryResult = dbUtil.insertData(self, CommonText.BASEINFO, cValues);
				if (queryResult) {
					startActivity(".ui.HomeActivity",true);
				}
			}
		}
	}
	
}
