package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

	private EditText pwdTxt_username,pwdTxt2_username,passwordEt,password2Et;
	
	private View line;
	
	private LinearLayout userpwdLayout;
	
	private Button findPwdBtn;
	
	private String type;
	
	private String usernameStr,username2Str,passwordStr,password2Str;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_user_find_upd_pwd_layout, null);
		boayLayout.addView(v);
		findViews();
		
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
	}

	private void findViews() {
		pwdTxt_username = findView(R.id.pwdTxt_username);
		pwdTxt2_username = findView(R.id.pwdTxt2_username);
		line = findView(R.id.line);
		passwordEt = findView(R.id.new_Txt_password);
		password2Et = findView(R.id.new_Txt2_password);
		
		userpwdLayout = findView(R.id.userpwdLayout);
		
		findPwdBtn = findView(R.id.btn_pwd);
		
		type = getIntent().getStringExtra(Constants.TYPE);
		
		if (type.equals(Constants.RESETPWD)) {
			pwdTxt_username.setVisibility(View.GONE);
			userpwdLayout.setVisibility(View.VISIBLE);
			pwdTxt2_username.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
			setTopTitle(R.string.action_loginresetpwd);
			passwordEt.setHint(CommUtil.getStrValue(self, R.string.login_input_old_pwd_hint));
		}else if(type.equals(Constants.FORGOTPWD)){
			pwdTxt_username.setVisibility(View.VISIBLE);
			userpwdLayout.setVisibility(View.GONE);
			setTopTitle(R.string.action_login_findgotpwd);
			passwordEt.setHint(CommUtil.getStrValue(self, R.string.login_input_new_pwd_hint));
		}
		
		findPwdBtn.setOnClickListener(this);
		
		pwdTxt_username.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				/*final String uname = s.toString().trim();
				if (RegexUtil.checkNotNull(uname)) {
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							getNewPwd(uname);
						}
					}, 1000);
				}*/
			}
		});
		
		pwdTxt_username.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					CommUtil.hideKeyboard(self);
					final String uname = pwdTxt_username.getText().toString().trim();
					if (RegexUtil.checkNotNull(uname)) {
						judgeUserExist(uname);
					}
					return true;
				}else{
					return false;
				}
			}
		});
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.btn_pwd:
			if (CommUtil.isNetworkAvailable(self)) {
				syncNewPwd();
			}else{
				set3Msg(R.string.check_network);
			}
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
	 * 校验字段
	 * @return
	 */
	private boolean judgeFeild() {
		usernameStr = pwdTxt_username.getText().toString().trim();
		username2Str = pwdTxt2_username.getText().toString().trim();
		passwordStr = passwordEt.getText().toString().trim(); 
		password2Str= password2Et.getText().toString().trim(); 
		
		if (type.equals(Constants.RESETPWD)) {
			if(!RegexUtil.checkNotNull(username2Str)){
				setMsg(R.string.action_input_username_isnull);
				return false;
			}
			
			if(!RegexUtil.checkNotNull(passwordStr)){
				setMsg(R.string.action_input_password_isnull);
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
		}else if (type.equals(Constants.FORGOTPWD)){
			if(!RegexUtil.checkNotNull(usernameStr)){
				setMsg(R.string.action_input_username_isnull);
				return false;
			}
			
			if(!RegexUtil.checkNotNull(passwordStr)){
				setMsg(R.string.action_input_password_isnull);
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
		params.add("p_username");
		values.add(str);
	
		requestData("pro_user_exist", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if(map.get("msg").get(0).equals(ResponseCode.USERNAME_NOEXIST)){
						set3Msg(R.string.register_username_noexist);
					}else if(map.get("msg").get(0).equals(ResponseCode.RESULT_OK)){
						setMsgHide();
						pwdTxt2_username.setVisibility(View.GONE);
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
			params.add("p_username");
			params.add("p_password");
			if (type.equals(Constants.RESETPWD)) {
				params.add("p_newpassword");
				
				values.add(username2Str);
				values.add(CommUtil.getMD5(passwordStr)); // 旧密码
				values.add(CommUtil.getMD5(password2Str)); // 新密码
				
				procname = "pro_user_resetpwd";
				
			}else if (type.equals(Constants.FORGOTPWD)){
				values.add(usernameStr);
				values.add(CommUtil.getMD5(passwordStr)); // 新密码
				
				procname = "pro_user_newpwd";
			}
			
			requestData(procname, 1, params, values, new HandlerData() {
				@Override
				public void error() {
					
				}
				
				public void success(Map<String, List<String>> map) {
					try {
						if (type.equals(Constants.RESETPWD)) {
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
