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
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.btn_register:
			if (CommUtil.isNetworkAvailable(self)) {
//				getUserData();
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
		
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		params.add("puser_name");
		params.add("puser_pwd");
		
		values.add(str_username);
		values.add(CommUtil.getMD5(str_password));
	
		requestData("procSetUser", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				toastMsg(R.string.action_regist_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					int useId = CommUtil.parseInt(map.get("userId").get(0));
					if (useId>0) {
						startActivity("MainActivity",true);
					}
				} catch (Exception e) {
					if(map.get("error").get(0).equals("405")){
						toastMsg(R.string.register_repeatedusername);
					}
				}
			}
		});
	}
	
}
