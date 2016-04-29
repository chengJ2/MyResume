package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.whjz.android.text.CommonText;

/**
 * 用户登录/注册
 * @author Administrator
 *
 */
public class UserLoginActivity extends BaseActivity implements
		OnClickListener {
	
	private EditText edtTxt_username;
	private EditText edtTxt_password;
	
	private ImageView save_checkbox;
	private TextView savePassWord;
	private TextView forgotPassWord;
	
	private Button btnLogin;
	
	private boolean fflag = true;
	
	private String str_username,str_password;
	
	private RelativeLayout user_login_layout,user_register_layout;
	
	private EditText usernameEt,passwordEt,password2Et;
	
	private Button registBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_user_layout, null);
		boayLayout.addView(v);
		findViews();
		
		initViews();
	}
	
	private void findViews(){
		user_login_layout = findView(R.id.user_login_layout);
		user_register_layout = findView(R.id.user_register_layout);
		
		user_login_layout.setVisibility(View.VISIBLE);
		user_register_layout.setVisibility(View.GONE);
		
		edtTxt_username = findView(R.id.edtTxt_username);
		edtTxt_password = findView(R.id.edtTxt_password);
		
		save_checkbox = findView(R.id.save_checkbox);
		savePassWord = findView(R.id.savePassWord);
		forgotPassWord = findView(R.id.forgotPassWord);
		
		btnLogin = findView(R.id.btn_login);
		
		save_checkbox.setOnClickListener(this);
		savePassWord.setOnClickListener(this);
		forgotPassWord.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		
		usernameEt = findView(R.id.regTxt_username);
		passwordEt = findView(R.id.regTxt_password);
		password2Et = findView(R.id.regTxt2_password);
		
		registBtn = findView(R.id.btn_register);
		registBtn.setOnClickListener(this);
		
	}
	
	private void initViews(){
		setTopTitle(R.string.action_user_login);
		setMsgHide();
		setRightIconVisible(View.VISIBLE);
		right_icon.setImageResource(R.drawable.icon_user_register);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		edtTxt_username.setText(getPreferenceData("username", ""));
		if (getPreferenceData("fflag")) {
			edtTxt_password.setText("");
			save_checkbox.setBackgroundResource(R.drawable.checkbox_sel);
		}else{
			edtTxt_password.setText("");
			save_checkbox.setBackgroundResource(R.drawable.checkbox_nor);
		}
		
		btnLogin.setText(CommUtil.getStrValue(self, R.string.action_login));
		btnLogin.setEnabled(true);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_lable:
			if (user_login_layout.getVisibility() == View.GONE) {
				setTopTitle(R.string.action_user_login);
				setRightIconVisible(View.VISIBLE);
				
				setAnimView(user_register_layout,0);
				
				setAnimView(user_login_layout,1);
				
				user_register_layout.setVisibility(View.GONE);
				user_login_layout.setVisibility(View.VISIBLE);
				
				edtTxt_username.requestFocus();
				
			}else if (user_login_layout.getVisibility() == View.VISIBLE) {
				self.scrollToFinishActivity();
			}
			break;
		case R.id.right_icon:
			setTopTitle(R.string.action_user_regist);
			setRightIconVisible(View.GONE);
			setAnimView(user_login_layout,0);
			
			setAnimView(user_register_layout,1);
			
			user_login_layout.setVisibility(View.GONE);
			user_register_layout.setVisibility(View.VISIBLE);
			break;
		case R.id.save_checkbox:
		case R.id.savePassWord:
			setState();
			break;
		case R.id.btn_login:
			if (CommUtil.isNetworkAvailable(self)) {
				if(checkInfo()){
					btnLogin.setText(CommUtil.getStrValue(self, R.string.action_wait_loging));
					btnLogin.setEnabled(false);

					loginTask();
				}
			}
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
	
	/**
	 * 登录请求
	 */
	private void loginTask(){
		List<String> paramKey = new ArrayList<String>();
    	List<String> paramValue = new ArrayList<String>();
    	
    	paramKey.add("p_username");
    	paramKey.add("p_userpwd");
    	
    	paramValue.add(str_username);
    	paramValue.add(CommUtil.getMD5(str_password));
    	
		requestData("pro_user_login", 1, paramKey, paramValue,new HandlerData() {
			
			@Override
			public void success(Map<String, List<String>> map) {
				try {
					sendSuccess(map);
				} catch (Exception e) {
					if(map.get("msg").get(0).equals("404")){
						errorLogin();
						toastMsg(R.string.action_login_error);
					}
				}
			}
			
			@Override
			public void error() {
				errorLogin();
			}
		});
	}
	
	
	private void errorLogin(){
		btnLogin.setText(CommUtil.getStrValue(self, R.string.action_login));
		btnLogin.setEnabled(true);
	}
	
	
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
		
//		if (str_username.length() > 50) {
//			toastMsg(R.string.action_input_up_isnull);
//			return;
//		}
//		
//		if (str_password.length() > 30) {
//			toastMsg(R.string.action_input_up_isnull);
//			return ;
//		}
		
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
					sendSuccess(map);
				} catch (Exception e) {
					e.printStackTrace();
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
	private void sendSuccess(Map<String, List<String>> map){
//		int useId = CommUtil.parseInt(map.get("userId").get(0));
//		if (useId > 0) {
			
			setPreferenceData("useId",map.get("userId").get(0));
			
			ContentValues cValues = new ContentValues();
			cValues.put("username", map.get("username").get(0));
			cValues.put("userpassword", map.get("password").get(0));
			cValues.put("deviceId", map.get("deviceId").get(0));
			cValues.put("patform", map.get("patform").get(0));
			cValues.put("createtime", map.get("createtime").get(0));
			cValues.put("lastlogintime", map.get("lastlogintime").get(0));
			
			queryResult = dbUtil.insertData(self, CommonText.USERINFO, cValues);
			if (queryResult) {
				
				setPreferenceData("username",map.get("username").get(0));
//				setPreferenceData("userpwd",map.get("password").get(0));
				
//				cValues = new ContentValues();
//				cValues.put("userId", map.get("userId").get(0));
				/*cValues.put("realname", map.get("realname").get(0));
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
				cValues.put("updatime", map.get("updatime").get(0))*/;
				
//				queryResult = dbUtil.insertData(self, CommonText.BASEINFO, cValues);
//				if (queryResult) {
					startActivity(".ui.HomeActivity",true);
//				}
//			}
		}
	}
	
	/**
	 * 判断用户名和密码
	 * @return
	 */
	private boolean checkInfo(){
		str_username = edtTxt_username.getText().toString();
		str_password = edtTxt_password.getText().toString();
		if(!RegexUtil.checkNotNull(str_username) || !RegexUtil.checkNotNull(str_password)){
			toastMsg(R.string.action_input_up_isnull);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 记住密码
	 */
	private void setState() {
		if (fflag) {
			fflag = false;
			save_checkbox.setBackgroundResource(R.drawable.checkbox_nor);
		} else {
			fflag = true;
			save_checkbox.setBackgroundResource(R.drawable.checkbox_sel);
		}
		setPreferenceData("fflag", fflag);
	}

	private void setAnimView(View v,int visible){
		AlphaAnimation dismiss = new AlphaAnimation(0, 1);
		if (visible == 0) {
			dismiss = new AlphaAnimation(1, 0);
		}
		dismiss.setDuration(1000);
		dismiss.setFillAfter(true);
		v.startAnimation(dismiss);
	}
	
}
