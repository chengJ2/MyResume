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
import com.me.resume.model.UUIDGenerator;
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
	
	private TextView acclogin;
	
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
		acclogin = findView(R.id.acclogin);
		
		btnLogin = findView(R.id.btn_login);
		
		save_checkbox.setOnClickListener(this);
		savePassWord.setOnClickListener(this);
		forgotPassWord.setOnClickListener(this);
		acclogin.setOnClickListener(this);
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
			edtTxt_password.setText(getPreferenceData("password", ""));
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
		case R.id.acclogin:
			if (user_login_layout.getVisibility() == View.GONE) {
				setTopTitle(R.string.action_user_login);
				setRightIconVisible(View.VISIBLE);
				
				setAnimView(user_register_layout,0);
				
				setAnimView(user_login_layout,1);
				
				user_register_layout.setVisibility(View.GONE);
				user_login_layout.setVisibility(View.VISIBLE);
				
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
				postUserInfo();
			}else{
				set3Msg(R.string.check_network);
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
					e.printStackTrace();
					if(map.get("msg").get(0).equals("404")){
						errorLogin();
						set3Msg(R.string.action_login_error);
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
	
	private void getFeildValue(){
		str_username = usernameEt.getText().toString();
		str_password = passwordEt.getText().toString();
	}
	
	private boolean  judgeFeild(){
		if(!RegexUtil.checkNotNull(str_username)){
			setMsg(R.string.action_input_up_isnull);
			return false;
		}
		
		if(!RegexUtil.checkNotNull(str_password)){
			setMsg(R.string.action_input_password_isnull);
			return false;
		}
		
		if(!CommUtil.getEditTextValue(passwordEt).equals(CommUtil.getEditTextValue(password2Et))){
			set3Msg(R.string.action_input_password_equal);
			return false;
		}
		
		if (str_username.length() > 120) {
			set3Msg(R.string.action_input_username_toolong);
			return false;
		}
		
		if (str_password.length() > 30) {
			set3Msg(R.string.action_input_password_toolong);
			return false;
		}
		return true;
	}
	
	/**
	 * 提交用户注册信息
	 */
	private void postUserInfo() {
		getFeildValue();
		
		if(judgeFeild()){
			List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			params.add("p_uid");
			params.add("p_username");
			params.add("p_userpwd");
			params.add("p_deviceId");
			params.add("p_patform");
			
			values.add(UUIDGenerator.getUUID());
			values.add(str_username);
			values.add(CommUtil.getMD5(str_password));
			values.add(deviceID);
			values.add("app");
		
			requestData("pro_user_register", 1, params, values, new HandlerData() {
				@Override
				public void error() {
					set3Msg(R.string.action_regist_fail);
				}
				
				public void success(Map<String, List<String>> map) {
					try {
						sendSuccess(map);
					} catch (Exception e) {
						e.printStackTrace();
						if(map.get("msg").get(0).equals("405")){
							set3Msg(R.string.register_repeatedusername);
						}
					}
				}
			});
		}
		
	}
	
	/**
	 * 登录成功
	 * @param map
	 */
	private void sendSuccess(final Map<String, List<String>> map){
		uTokenId = map.get("uid").get(0);
		String feildStr1 = map.get("username").get(0);
		String feildStr2 = map.get("password").get(0);
		String feildStr3 = map.get("deviceId").get(0);
		String feildStr4 = map.get("patform").get(0);
		String feildStr5 = map.get("createtime").get(0);
		String feildStr6 = map.get("lastlogintime").get(0);
		
		setPreferenceData("username",feildStr1);
		setPreferenceData("password",str_password);
		
		queryWhere = "select * from " + CommonText.USERINFO + " where uid = '" + uTokenId + "'";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("id").length > 0) {
			updResult = dbUtil.updateData(self, CommonText.USERINFO,
					new String[]{"uid=?","username","userpassword","deviceId","patform",
					"createtime","lastlogintime"}, 
					new String[]{uTokenId,feildStr1,feildStr2,feildStr3,feildStr4,feildStr5,feildStr6},1);
			if(updResult == 1){
				setPreferenceData("useId",uTokenId);
				getBaseinfo(map);
			}
		}else{
			ContentValues cValues = new ContentValues();
			cValues.put("uid", uTokenId);
			cValues.put("username", feildStr1);
			cValues.put("userpassword", feildStr2);
			cValues.put("deviceId", feildStr3);
			cValues.put("patform", feildStr4);
			cValues.put("createtime", feildStr5);
			cValues.put("lastlogintime", feildStr6);
			
			queryResult = dbUtil.insertData(self, CommonText.USERINFO, cValues);
			if (queryResult) {
				setPreferenceData("useId",uTokenId);
				getBaseinfo(map);
			}
		}
	}
	
	/**
	 * 获取用户基本信息
	 * @param map
	 */
	private void getBaseinfo(Map<String, List<String>> map){
		ContentValues cValues = new ContentValues();
		
		List<String> feildStr10 = map.get("userId");
		if(feildStr10 != null && feildStr10.size()>0){
			cValues.put("userId", feildStr10.get(0));
		}
		
		List<String> feildStr11 = map.get("realname");
		if(feildStr11 != null && feildStr11.size()>0){
			cValues.put("realname", feildStr11.get(0));
		}
		
		List<String> feildStr12 = map.get("gender");
		if(feildStr12 != null && feildStr12.size()>0){
			cValues.put("gender", feildStr12.get(0));
		}
		
		List<String> feildStr13 = map.get("brithday");
		if(feildStr13 != null && feildStr13.size()>0){
			cValues.put("brithday", feildStr13.get(0));
		}
		
		List<String> feildStr14 = map.get("joinworktime");
		if(feildStr14 != null && feildStr14.size()>0){
			cValues.put("joinworktime", feildStr14.get(0));
		}
		
		List<String> feildStr15 = map.get("phone");
		if(feildStr15 != null && feildStr15.size()>0){
			cValues.put("phone", feildStr15.get(0));
		}
		
		List<String> feildStr16 = map.get("hometown");
		if(feildStr16 != null && feildStr16.size()>0){
			cValues.put("hometown", feildStr16.get(0));
		}
		
		List<String> feildStr17 = map.get("city");
		if(feildStr17 != null && feildStr17.size()>0){
			cValues.put("city", feildStr17.get(0));
		}
		
		List<String> feildStr18 = map.get("email");
		if(feildStr18 != null && feildStr18.size()>0){
			cValues.put("email", feildStr18.get(0));
		}
		
		List<String> feildStr19 = map.get("ismarry");
		if(feildStr19 != null && feildStr19.size()>0){
			cValues.put("ismarry", feildStr19.get(0));
		}
		
		List<String> feildStr20 = map.get("nationality");
		if(feildStr20 != null && feildStr20.size()>0){
			cValues.put("nationality", feildStr20.get(0));
		}
		
		List<String> feildStr21 = map.get("license");
		if(feildStr21 != null && feildStr21.size()>0){
			cValues.put("license", feildStr21.get(0));
		}
		
		List<String> feildStr22 = map.get("workingabroad");
		if(feildStr22 != null && feildStr22.size()>0){
			cValues.put("workingabroad", feildStr22.get(0));
		}
		
		List<String> feildStr23 = map.get("politicalstatus");
		if(feildStr23 != null && feildStr23.size()>0){
			cValues.put("politicalstatus", feildStr23.get(0));
		}
		
		List<String> feildStr24 = map.get("bgcolor");
		if(feildStr24 != null && feildStr24.size()>0){
			cValues.put("bgcolor", feildStr24.get(0));
		}
		
		List<String> feildStr25 = map.get("avator");
		if(feildStr25 != null && feildStr25.size()>0){
			cValues.put("avator", feildStr25.get(0));
			setPreferenceData("avator",feildStr25.get(0));
		}
		
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = '" + uTokenId + "' limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			String baid = commMapArray.get("id")[0];
			updResult = dbUtil.updateData(self, CommonText.BASEINFO, 
					new String[]{baid,"realname","gender","brithday","joinworktime",
					"phone","hometown","city","email","ismarry",
					"nationality","license","workingabroad","politicalstatus","bgcolor","avator"}, 
					new String[]{uTokenId,feildStr11.get(0),feildStr12.get(0),feildStr13.get(0),feildStr14.get(0),
					feildStr15.get(0),feildStr16.get(0),feildStr17.get(0),feildStr18.get(0),feildStr19.get(0),
					feildStr20.get(0),feildStr21.get(0),feildStr22.get(0),feildStr23.get(0),feildStr24.get(0),feildStr25.get(0)},2);
			if (updResult == 1) {
				startActivity(".ui.UserCenterActivity",true);
				
			}
		}else{
			queryResult = dbUtil.insertData(self, CommonText.BASEINFO, cValues);
			if (queryResult) {
				startActivity(".ui.UserCenterActivity",true);
			}
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
			set3Msg(R.string.action_input_up_isnull);
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

	/**
	 * View切换动画
	 * @param v
	 * @param visible
	 */
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
