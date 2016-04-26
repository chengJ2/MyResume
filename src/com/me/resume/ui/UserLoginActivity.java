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
import android.widget.ImageView;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.RegexUtil;
import com.whjz.android.text.CommonText;

/**
 * 用户登录
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_user_login_layout, null);
		boayLayout.addView(v);
		findViews();
		
		initViews();
	}
	
	private void findViews(){
		edtTxt_username = findView(R.id.edtTxt_username);
		edtTxt_password = findView(R.id.edtTxt_password);
		
		save_checkbox = findView(R.id.save_checkbox);
		savePassWord = findView(R.id.savePassWord);
		forgotPassWord = findView(R.id.forgotPassWord);
		
		btnLogin = findView(R.id.btn_login);
		
		save_checkbox.setOnClickListener(this);
		savePassWord.setOnClickListener(this);
		forgotPassWord.setOnClickListener(this);
	}
	
	private void initViews(){
		setTopTitle(R.string.action_user_login);
		setMsgHide();
		setRightIconVisible(View.VISIBLE);
		right_icon.setImageResource(R.drawable.icon_user_register);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		edtTxt_username.setText("");
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
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.right_icon:
			startActivity(".ui.UserRegisterActivity",false);
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
    	
    	paramKey.add("puser_name");
    	paramKey.add("puser_pwd");
    	
    	paramValue.add(str_username);
    	paramValue.add(CommUtil.getMD5(str_password));
    	
		requestData("pro_user_login", 1, paramKey, paramValue,DialogUtils.getProgressDialog(self, R.string.action_loging),new HandlerData() {
			
			@Override
			public void success(Map<String, List<String>> map) {
				try {
					successLogin(map);
				} catch (Exception e) {
					if(map.get("error").get(0).equals("404")){
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
	
	/**
	 * 登录成功
	 * @param map
	 */
	private void successLogin(Map<String, List<String>> map){
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
				// TODO
			}
		}
	}
	
	private void errorLogin(){
		btnLogin.setText(CommUtil.getStrValue(self, R.string.action_login));
		btnLogin.setEnabled(true);
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

}
