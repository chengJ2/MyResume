package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.tools.L;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
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
	private TextView resetPassWord,forgotPassWord;
	
	private TextView acclogin;
	
	private Button btnLogin;
	
	private boolean fflag = false;
	
	private String str_username,str_phone,str_password;
	
	private RelativeLayout user_login_layout,user_register_layout;
	
	private EditText usernameEt,regTxt_phone,passwordEt,password2Et;
	
	private Button registBtn;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				if (updResult == 1 || queryResult) {
					startChildActivity(Constants.USERCENTER,true);
				}
				break;
			default:
				break;
			}
		};
	};
	
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
		resetPassWord = findView(R.id.resetPassWord);
		forgotPassWord = findView(R.id.forgotPassWord);
		acclogin = findView(R.id.acclogin);
		
		btnLogin = findView(R.id.btn_login);
		
		save_checkbox.setOnClickListener(this);
		savePassWord.setOnClickListener(this);
		resetPassWord.setOnClickListener(this);
		forgotPassWord.setOnClickListener(this);
		acclogin.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		
		usernameEt = findView(R.id.regTxt_username);
		regTxt_phone = findView(R.id.regTxt_phone);
		passwordEt = findView(R.id.regTxt_password);
		password2Et = findView(R.id.regTxt2_password);
		
		registBtn = findView(R.id.btn_register);
		registBtn.setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		edtTxt_username.setText(preferenceUtil.getPreferenceData(UserInfoCode.USERNAME, ""));
		if (preferenceUtil.getPreferenceData(UserInfoCode.SAVEPWD)) {
			edtTxt_password.setText(preferenceUtil.getPreferenceData(UserInfoCode.PASSWORD, ""));
			save_checkbox.setBackgroundResource(R.drawable.checkbox_sel);
		}else{
			edtTxt_password.setText("");
			save_checkbox.setBackgroundResource(R.drawable.checkbox_nor);
		}
	}
	
	private void initViews(){
		setTopTitle(R.string.action_user_login);
		setMsgHide();
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		if (preferenceUtil.getPreferenceData(UserInfoCode.ISREGISTER)) {
			setRightIconVisible(View.GONE);
		}else{
			right_icon.setImageResource(R.drawable.icon_user_register);
			setRightIconVisible(View.VISIBLE);
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
		case R.id.resetPassWord:
			ActivityUtils.startActivityPro(self,
			Constants.PACKAGENAMECHILD +Constants.USERNEWPWD,Constants.TYPE,Constants.RESETPWD);
			break;
		case R.id.forgotPassWord:
			ActivityUtils.startActivityPro(self,
					Constants.PACKAGENAMECHILD +Constants.USERNEWPWD,Constants.TYPE,Constants.FORGOTPWD);
			break;
		case R.id.btn_login:
			if (CommUtil.isNetworkAvailable(self)) {
				if(checkInfo()){
					btnLogin.setText(CommUtil.getStrValue(self, R.string.action_wait_loging));
					btnLogin.setEnabled(false);

					actionLogin();
				}
			}
			break;
		case R.id.btn_register:
			if (CommUtil.isNetworkAvailable(self)) {
				registBtn.setText(CommUtil.getStrValue(self, R.string.action_wait_reging));
				registBtn.setEnabled(false);
				actionRegister();
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
	private void actionLogin(){
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
					CommUtil.hideKeyboard(self);
					sendSuccess(map);
				} catch (Exception e) {
					e.printStackTrace();
					if(map.get("msg").get(0).equals(ResponseCode.INVALID_INFO)){
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
		str_phone = regTxt_phone.getText().toString();
	}
	
	private boolean  judgeFeild(){
		if(!RegexUtil.checkNotNull(str_username)){
			setMsg(R.string.action_input_up_isnull);
			return false;
		}
		
		if (RegexUtil.checkChs(str_username)) {
			set3Msg(R.string.action_input_usename_cn);
			return false;
		}
		
		if (!RegexUtil.checkNotNull(str_phone)) {
			set3Msg(R.string.action_input_up_phone_email);
			return false;
		}
		
		if(!RegexUtil.isPhone(str_phone)){
			set3Msg(R.string.reg_info_phone);
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
	private void actionRegister() {
		getFeildValue();
		
		if(judgeFeild()){
			List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			params.add("p_uid");
			params.add("p_username");
			params.add("p_userpwd");
			params.add("p_phone");
			params.add("p_deviceId");
			params.add("p_patform");
			
			values.add(uTokenId);
			values.add(str_username);
			values.add(str_phone);
			values.add(CommUtil.getMD5(str_password));
			values.add(deviceID);
			values.add("app");
		
			requestData("pro_user_register", 1, params, values, new HandlerData() {
				@Override
				public void error() {
					set3Msg(R.string.action_regist_fail);
					registBtn.setText(CommUtil.getStrValue(self, R.string.action_register));
					registBtn.setEnabled(true);
				}
				
				public void success(Map<String, List<String>> map) {
					try {
						CommUtil.hideKeyboard(self);
						registBtn.setText(CommUtil.getStrValue(self, R.string.action_wait_regsuccess));
						registBtn.setEnabled(false);
						preferenceUtil.setPreferenceData("isregister", true);
						sendSuccess(map);
					} catch (Exception e) {
						e.printStackTrace();
						if(map.get("msg").get(0).equals(ResponseCode.USERNAME_REPEAT)){
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
		preferenceUtil.setPreferenceData(UserInfoCode.UTOKENID, uTokenId);
		String feildStr1 = map.get("username").get(0);
		String feildStr2 = map.get("password").get(0);
		String feildStr3 = map.get("deviceId").get(0);
		String feildStr4 = map.get("patform").get(0);
		String feildStr5 = map.get("createtime").get(0);
		String feildStr6 = map.get("lastlogintime").get(0);
		String feildStr7 = map.get("userstatus").get(0);
		
		preferenceUtil.setPreferenceData(UserInfoCode.USERNAME,feildStr1);
		preferenceUtil.setPreferenceData(UserInfoCode.PASSWORD,str_password);
		
		queryWhere = "select * from " + CommonText.USERINFO + " where uid = '" + uTokenId + "'";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("id").length > 0) {
			updResult = dbUtil.updateData(self, CommonText.USERINFO,
					new String[]{"uid=?","username","userpassword","patform",
										 "createtime","updatetime","lastlogintime","userstatus"}, 
					new String[]{uTokenId,feildStr1,feildStr2,feildStr4,
										feildStr5,TimeUtils.getCurrentTimeInString(),feildStr6,feildStr7},1);
		}else{
			ContentValues cValues = new ContentValues();
			cValues.put("uid", uTokenId);
			cValues.put("username", feildStr1);
			cValues.put("userpassword", feildStr2);
			cValues.put("deviceId", feildStr3);
			cValues.put("patform", feildStr4);
			cValues.put("createtime", feildStr5);
			cValues.put("updatetime", TimeUtils.getCurrentTimeInString());
			cValues.put("lastlogintime", feildStr6);
			cValues.put("userstatus", feildStr7);
			
			queryResult = dbUtil.insertData(self, CommonText.USERINFO, cValues);
		}
		
		if(updResult == 1 || queryResult){
			preferenceUtil.setPreferenceData(UserInfoCode.USEID,uTokenId);
			getBaseinfo(map);
		}
		
	}
	
	/**
	 * 通过本地的key获取values更新远程的values
	 * @param mapArray
	 * @param map
	 * @param key
	 * @return
	 */
	private String getLocalKeyValue(Map<String, String[]> mapArray,Map<String, List<String>> map,String key){
		String value = mapArray.get(key)[0];
		if (!RegexUtil.checkNotNull(value)) {
			List<String> listStr = map.get(key);
			if(listStr != null && listStr.size()>0){
				value = listStr.get(0);
			}
		}
		return value;
	}
	
	/**
	 * 通过远程的key获取values插入到本地
	 * @param mapArray
	 * @param map
	 * @param key
	 * @return
	 */
	private String getServerKeyValue(Map<String, List<String>> map,String key){
		String value = "";
		List<String> listStr = map.get(key);
		if(listStr != null && listStr.size()>0){
			value = listStr.get(0);
		}
		return value;
	}
	
	/**
	 * 获取用户基本信息
	 * @param map
	 */
	private void getBaseinfo(Map<String, List<String>> map){
		L.d("========getBaseinfo==========="+uTokenId);
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = '" + uTokenId + "' limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		
		if (commMapArray != null) {
			String userId = commMapArray.get("userId")[0];
			if (RegexUtil.checkNotNull(userId)) {
//				String feildStr10 = getLocalKeyValue(commMapArray,map,"userId");
				String feildStr11 = getLocalKeyValue(commMapArray,map,"realname");
				String feildStr12 = getLocalKeyValue(commMapArray,map,"gender");
				String feildStr13 = getLocalKeyValue(commMapArray,map,"brithday");
				String feildStr14 = getLocalKeyValue(commMapArray,map,"joinworktime");
				String feildStr15 = getLocalKeyValue(commMapArray,map,"phone");
				String feildStr16 = getLocalKeyValue(commMapArray,map,"hometown");
				String feildStr17 = getLocalKeyValue(commMapArray,map,"city");
				String feildStr18 = getLocalKeyValue(commMapArray,map,"email");
				String feildStr19 = getLocalKeyValue(commMapArray,map,"ismarry");
				String feildStr20 = getLocalKeyValue(commMapArray,map,"nationality");
				String feildStr21 = getLocalKeyValue(commMapArray,map,"license");
				String feildStr22 = getLocalKeyValue(commMapArray,map,"workingabroad");
				String feildStr23 = getLocalKeyValue(commMapArray,map,"politicalstatus");
				String feildStr24 = getLocalKeyValue(commMapArray,map,"bgcolor");
				String feildStr25 = getLocalKeyValue(commMapArray,map,"avator");
				String feildStr26 = getLocalKeyValue(commMapArray,map,"updatetime");
				preferenceUtil.setPreferenceData("avator",feildStr25);
				
				String baid = commMapArray.get("id")[0];
				updResult = dbUtil.updateData(self, CommonText.BASEINFO, 
						new String[]{baid,"realname","gender","brithday","joinworktime",
						"phone","hometown","city","email","ismarry",
						"nationality","license","workingabroad","politicalstatus","bgcolor","avator","updatetime"}, 
						new String[]{uTokenId,feildStr11,feildStr12,feildStr13,feildStr14,
						feildStr15,feildStr16,feildStr17,feildStr18,feildStr19,
						feildStr20,feildStr21,feildStr22,feildStr23,feildStr24,feildStr25,feildStr26},2);
			}else{
				insertBaseInfo(map);
			}
		}else{
			insertBaseInfo(map);
		}
		
		mHandler.sendEmptyMessage(11);
		
	}
	
	/**
	 * 如本地没有当前用户的数据，将远程数据同步到本地
	 * @param map
	 */
	private void insertBaseInfo(Map<String, List<String>> map){
		L.d("========insertBaseInfo==========="+uTokenId);
		ContentValues cValues = new ContentValues();
		cValues.put("userId",uTokenId);
		cValues.put("realname",getServerKeyValue(map,"realname"));
		cValues.put("gender",getServerKeyValue(map,"gender"));
		cValues.put("brithday",getServerKeyValue(map,"brithday"));
		cValues.put("joinworktime",getServerKeyValue(map,"joinworktime"));
		cValues.put("phone",getServerKeyValue(map,"phone"));
		cValues.put("hometown",getServerKeyValue(map,"hometown"));
		cValues.put("city",getServerKeyValue(map,"city"));
		cValues.put("email",getServerKeyValue(map,"email"));
		cValues.put("ismarry",getServerKeyValue(map,"ismarry"));
		cValues.put("nationality",getServerKeyValue(map,"nationality"));
		cValues.put("license",getServerKeyValue(map,"license"));
		cValues.put("workingabroad",getServerKeyValue(map,"workingabroad"));
		cValues.put("politicalstatus",getServerKeyValue(map,"politicalstatus"));
		cValues.put("bgcolor",getServerKeyValue(map,"bgcolor"));
		String avatorStr = getServerKeyValue(map,"avator");
		cValues.put("avator",avatorStr);
		preferenceUtil.setPreferenceData(UserInfoCode.AVATOR,avatorStr);
		
		queryResult = dbUtil.insertData(self, CommonText.BASEINFO, cValues);
		if (queryResult) {
			startChildActivity(Constants.USERCENTER,true);
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
		fflag = !fflag;
		if (fflag) {
			save_checkbox.setBackgroundResource(R.drawable.checkbox_sel);
		} else {
			save_checkbox.setBackgroundResource(R.drawable.checkbox_nor);
		}
		preferenceUtil.setPreferenceData(UserInfoCode.SAVEPWD, fflag);
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
