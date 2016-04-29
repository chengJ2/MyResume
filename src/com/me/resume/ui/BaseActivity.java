package com.me.resume.ui;

import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.tools.SystemBarTintManager;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.views.CustomFAB;

/**
 * 
 * @ClassName: BaseActivity
 * @Description: 基类
 * @date 2016/4/22 上午10:51:57
 * 
 */
public class BaseActivity extends SwipeBackActivity implements OnClickListener,TextWatcher{

	protected RelativeLayout topLayout;
	
	protected TextView toptext;
	protected ImageView left_icon, right_icon,right_icon_more;
	protected TextView msg;
	
	protected LinearLayout boayLayout;
	
	private RelativeLayout fabLayout;
	
	private CustomFAB saveButton,editButton,nextButton;
	
	protected BaseActivity self;
	protected SharedPreferences sp;
	
	protected CommonBaseAdapter<String> commStrAdapter = null;

	protected Map<String, String[]> commMapArray = null;

	protected Map<String, List<String>> commMapList = null;

	protected String queryWhere = "";

	protected String kId = "";
	
	protected int updResult = -1;

	protected boolean queryResult = false;

	protected int whichTab = 1;

	protected String[] item_values = null;

	protected List<String> mList = null;
	
	protected List<Integer> nList = null;

	protected String fieldNull = null;
	
	protected Integer checkColor = 0;
	
	protected String deviceID = "";// 设备标识码
	
	private static BaseActivity mInstance;
	
	public static BaseActivity getInstance(){
		if (mInstance == null) {
			mInstance = new BaseActivity();
		}
		return mInstance;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_layout);
		findViews();
		initData();
	}
	
	private void findViews(){
		topLayout = findView(R.id.topLayout);
		toptext = findView(R.id.top_text);
		left_icon = findView(R.id.left_lable);
		right_icon = findView(R.id.right_icon);
		right_icon_more = findView(R.id.right_icon_more);
		msg = findView(R.id.msg);
		
		boayLayout = findView(R.id.bodyLayout);
		
		fabLayout = findView(R.id.fabLayout);
		saveButton = findView(R.id.save);
		editButton = findView(R.id.edit);
		nextButton = findView(R.id.next);
		
		left_icon.setOnClickListener(this);
		right_icon.setOnClickListener(this);
		
		right_icon_more.setOnClickListener(this);
		
		saveButton.setOnClickListener(this);
		editButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
	}
	
	private void initData(){
		self = BaseActivity.this;
		sp = getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE);
		fieldNull = CommUtil.getStrValue(self, R.string.action_input_isnull);
		
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		deviceID = TelephonyMgr.getDeviceId();
	}

	@Override
	protected void onResume() {
		super.onResume();
    	// 初始化语言环境
//    	LanguageSettings.getInstance().initLang(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.top_bar);
	}
	
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置顶部标题 及 图标
	 * @author Comsys-WH1510032
	 * @return 返回类型
	 * @param id
	 */
	protected void setTopTitle(int id) {
		toptext.setText(CommUtil.getStrValue(self, id));
	}

	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置顶部左边按钮图标
	 * @param resId
	 */
	protected void setLeftIcon(int resId) {
		left_icon.setImageResource(resId);
	}

	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置顶部右边按钮图标
	 * @param resId
	 */
	protected void setRightIcon(int resId) {
		right_icon.setImageResource(resId);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置顶部左边按钮可视性
	 * @param visibility
	 */
	protected void setLeftIconVisible(int visibility) {
		left_icon.setVisibility(visibility);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置顶部右边按钮可视性
	 * @param visibility
	 */
	protected void setRightIconVisible(int visibility) {
		right_icon.setVisibility(visibility);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置顶部更多按钮可视性
	 * @param visibility
	 */
	protected void setRight2IconVisible(int visibility) {
		right_icon_more.setVisibility(visibility);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置底部按钮区域可视性
	 * @param visibility
	 */
	protected void setfabLayoutVisible(int visibility){
		fabLayout.setVisibility(visibility);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置中间按钮可视性
	 * @param visibility
	 */
	protected void setEditBtnVisible(int visibility){
		editButton.setVisibility(visibility);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置左边按钮图标
	 * @param resId
	 */
	protected void setAddBtnSrc(int resId){
		saveButton.setImageResource(resId);
	}
	
	/**
	 * 预览界面的背景色
	 * @return checkColor
	 */
	protected String getCheckColor(int checkColor){
		if (checkColor == 0) {
			return String.valueOf(R.color.red);
		}
		return String.valueOf(checkColor);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置界面消息
	 * @param id
	 */
	protected void setMsg(int id) {
		msg.setText(CommUtil.getStrValue(self, id) + fieldNull);
		msg.setVisibility(View.VISIBLE);
		msg.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				setMsgHide();
			}
		}, 1500);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置界面消息
	 * @param id
	 */
	protected void set2Msg(int id) {
		msg.setText(CommUtil.getStrValue(self, id));
		msg.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置界面消息1.5S隐藏
	 * @param id
	 */
	protected void set3Msg(int id) {
		msg.setText(CommUtil.getStrValue(self, id));
		msg.setVisibility(View.VISIBLE);
		msg.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				setMsgHide();
			}
		}, 1500);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置界面消息
	 * @author Comsys-WH1510032
	 * @return 返回类型  
	 * @param id
	 * @param visibility
	 */
	protected void setMsgHide() {
		msg.setVisibility(View.GONE);
	}

	protected void startActivity(String src, boolean finish) {
		ActivityUtils.startActivity(self, Constants.PACKAGENAME + src, finish);
	}

	public void setPreferenceData(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	public String getPreferenceData(String str, String def) {
		return sp.getString(str, def);
	}

	public void setPreferenceData(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	public int getPreferenceData(String str, int def) {
		return sp.getInt(str, def);
	}

	public void setPreferenceData(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	public boolean getPreferenceData(String str) {
		return sp.getBoolean(str, false);
	}

	/*
	 * protected void switchLang(String newLang){
	 * setPreferenceData("LANGUAGE",newLang); // finish app内存中的所有activity while
	 * (0 != mLocalStack.size()) { mLocalStack.pop().finish(); } // 跳转到app首页 //
	 * MyApplication.getApplication().exitAll(); //
	 * startActivity("ui.HomeActivity",false); }
	 */

	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: Find View By Id
	 * @return View
	 * @param viewID
	 */
	@SuppressWarnings("unchecked")
	protected <T extends View> T findView(int viewID) {
		return (T) findViewById(viewID);
	}

	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 更新UI
	 * @param id
	 */
	protected void runOnUiThread(final int id) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				set3Msg(id);
			}
		});
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.toString() != null && !"".equals(s.toString())) {
			setMsgHide();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

}
