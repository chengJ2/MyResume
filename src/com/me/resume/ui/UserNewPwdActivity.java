package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;

/**
 * 
* @ClassName: UserNewPwdActivity 
* @Description: 找回/重置密码
* @date 2016/4/25 下午1:38:18 
*
 */
public class UserNewPwdActivity extends BaseActivity implements OnClickListener{

	private EditText pwdTxt_phone_email,passwordEt,password2Et;
	private View line;
	private LinearLayout userpwdLayout;
	private Button btn_check,btn_newPwd;
	private String type;
	private String phoneemailStr,username2Str,passwordStr,password2Str;
	
	private boolean showRegType = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_user_find_upd_pwd_layout, null);
		boayLayout.addView(v);
		boayLayout.setBackgroundResource(R.drawable.user_bg_shape);
		findViews();
		
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
	}

	private void findViews() {
		pwdTxt_phone_email = findView(R.id.pwdTxt_phone_email);
		line = findView(R.id.line);
		passwordEt = findView(R.id.new_Txt_password);
		password2Et = findView(R.id.new_Txt2_password);
		userpwdLayout = findView(R.id.userpwdLayout);
		btn_check = findView(R.id.btn_check);
		btn_newPwd = findView(R.id.btn_newPwd);
		
		pwdTxt_phone_email.setOnClickListener(this);
		btn_check.setOnClickListener(this);
		btn_newPwd.setOnClickListener(this);
		
		type = getIntent().getStringExtra(Constants.TYPE);
		if (type.equals(UserInfoCode.RESETPWD)) {
			line.setVisibility(View.VISIBLE);
			setTopTitle(R.string.action_loginresetpwd);
			passwordEt.setHint(getStrValue(R.string.login_input_old_pwd_hint));
		}else if(type.equals(UserInfoCode.FORGOTPWD)){
			setTopTitle(R.string.action_login_findgotpwd);
			passwordEt.setHint(getStrValue(R.string.login_input_new_pwd_hint));
		}
		
		pwdTxt_phone_email.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					check();
					return true;
				}else{
					return false;
				}
			}
		});
	}
	
	private void check(){
		CommUtil.hideKeyboard(self);
		final String uname = pwdTxt_phone_email.getText().toString().trim();
		if (RegexUtil.checkNotNull(uname)) {
			judgeUserExist(uname);
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.btn_check:
			if (judge()) {
				if (CommUtil.isNetworkAvailable(self)) {
					check();
				}else{
					set3Msg(R.string.check_network);
				}
			}
			break;
		case R.id.btn_newPwd:
			if (CommUtil.isNetworkAvailable(self)) {
				syncNewPwd();
			}else{
				set3Msg(R.string.check_network);
			}
			break;
		case R.id.pwdTxt_phone_email:
			showRegType = !showRegType;
			Resources regType = getResources();
			Drawable img_regtype_left = null;
			Drawable img_regtype_right = regType.getDrawable(R.drawable.icon_reg_switch);
			if (showRegType) {
				pwdTxt_phone_email.setHint(getStrValue(R.string.register_input3_hint));
				img_regtype_left = regType.getDrawable(R.drawable.icon_email);
				pwdTxt_phone_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			}else{
				pwdTxt_phone_email.setHint(getStrValue(R.string.register_input2_hint));
				img_regtype_left = regType.getDrawable(R.drawable.icon_phone);
				pwdTxt_phone_email.setInputType(InputType.TYPE_CLASS_PHONE);
			}
			img_regtype_left.setBounds(0, 0, img_regtype_left.getMinimumWidth(), img_regtype_left.getMinimumHeight());
			img_regtype_right.setBounds(0, 0, img_regtype_left.getMinimumWidth(), img_regtype_left.getMinimumHeight());
			pwdTxt_phone_email.setCompoundDrawables(img_regtype_left, null, img_regtype_right, null); //设置左图标
			break;
		default:
			break;
		}
	}
	
	/**
	 * View切换动画
	 * @param v
	 * @param visible
	 */
	private void setAnimView(View v){
		AlphaAnimation dismiss = new AlphaAnimation(0, 1);
		dismiss.setDuration(1000);
		dismiss.setFillAfter(true);
		v.startAnimation(dismiss);
		v.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 
	 * 校验 手机号 或 邮箱
	 * @return
	 */
	private boolean judge(){
		phoneemailStr = pwdTxt_phone_email.getText().toString().trim();
		if(!RegexUtil.checkNotNull(phoneemailStr)){
			setMsg(R.string.action_input_username_isnull);
			return false;
		}
		
		if (showRegType) {
			if(!RegexUtil.checkEmail(phoneemailStr)){
				set3Msg(R.string.reg_info_email);
				return false;
			}
		}else{
			if(!RegexUtil.isPhone(phoneemailStr)){
				set3Msg(R.string.reg_info_phone);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 校验字段
	 * @return
	 */
	private boolean judgeFeild() {
		passwordStr = passwordEt.getText().toString().trim(); 
		password2Str= password2Et.getText().toString().trim(); 
		
		if (type.equals(UserInfoCode.RESETPWD)) {
			if(!RegexUtil.checkNotNull(passwordStr)){
				setMsg(R.string.action_input_newpassword_isnull);
				return false;
			}
			
			if(!RegexUtil.checkNotNull(password2Str)){
				setMsg(R.string.action_input_newpassword_isnull);
				return false;
			}
			
			if (!RegexUtil.checkStringLength(password2Str, 30)) {
				set3Msg(R.string.action_input_password_toolong);
				return false;
			}
		}else if (type.equals(UserInfoCode.FORGOTPWD)){
			if(!RegexUtil.checkNotNull(passwordStr)){
				setMsg(R.string.action_input_newpassword_isnull);
				return false;
			}
			
			if (!RegexUtil.checkStringLength(passwordStr, 30)) {
				set3Msg(R.string.action_input_password_toolong);
				return false;
			}
			
			if(!passwordStr.equals(password2Str)){
				set3Msg(R.string.action_input_password_equal);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 获得新密码
	 * @param str
	 */
	private void judgeUserExist(String str){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		params.add("p_userinfo");
		values.add(str);
	
		requestData("pro_user_exist", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if(map.get("msg").get(0).equals(ResponseCode.USERNAME_NOEXIST)){
						set3Msg(R.string.register_username_noexist);
						userpwdLayout.setVisibility(View.GONE);
						btn_check.setVisibility(View.VISIBLE);
						btn_newPwd.setVisibility(View.GONE);
					}else if(map.get("msg").get(0).equals(ResponseCode.RESULT_OK)){
						setMsgHide();
						userpwdLayout.setVisibility(View.VISIBLE);
						btn_check.setVisibility(View.GONE);
						btn_newPwd.setVisibility(View.VISIBLE);
						line.setVisibility(View.GONE);
						setAnimView(userpwdLayout);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/**
	 * 同步新密码
	 * @param str
	 */
	private void syncNewPwd(){
		if(judgeFeild()){
			String procname = "";
			List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			params.add("p_userinfo");
			params.add("p_password");
			if (type.equals(UserInfoCode.RESETPWD)) {
				params.add("p_newpassword");
				
				values.add(username2Str);
				values.add(CommUtil.getMD5(passwordStr)); // 旧密码
				values.add(CommUtil.getMD5(password2Str)); // 新密码
				
				procname = "pro_user_resetpwd";
				
			}else if (type.equals(UserInfoCode.FORGOTPWD)){
				values.add(phoneemailStr);
				values.add(CommUtil.getMD5(passwordStr)); // 新密码
				
				procname = "pro_user_newpwd";
			}
			
			requestData(procname, 1, params, values, new HandlerData() {
				@Override
				public void error() {
					
				}
				
				public void success(Map<String, List<String>> map) {
					try {
						if (type.equals(UserInfoCode.RESETPWD)) {
							if(map.get("msg").get(0).equals(ResponseCode.INVALID_INFO)){
								set3Msg(R.string.action_login_error);
							}else{
								set3Msg(R.string.login_input_updatepwd_success);
								preferenceUtil.setPreferenceData("password",passwordStr);
							}
						}else{
							if(map.get("msg").get(0).equals(ResponseCode.RESULT_OK)){
								preferenceUtil.setPreferenceData("password",passwordStr);
								scrollToFinishActivity();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
}
