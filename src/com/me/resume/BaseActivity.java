package com.me.resume;

import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.tools.ImageLoader;
import com.me.resume.tools.SystemBarTintManager;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.PreferenceUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.views.CustomFAB;
import com.me.resume.views.MarqueeText;

/**
 * 
 * @ClassName: BaseActivity
 * @Description: 基类
 * @date 2016/4/22 上午10:51:57
 * 
 */
public class BaseActivity extends SwipeBackActivity implements OnClickListener,TextWatcher{

	protected BaseActivity self;
	
	protected RelativeLayout topLayout;
	
	protected TextView toptext;
	protected ImageView left_icon, right_icon,right_icon_more;
	
	protected MarqueeText msg;
	
	protected LinearLayout boayLayout;
	
	private RelativeLayout fabLayout;
	
	private CustomFAB saveButton,editButton,nextButton;
	
	protected CommonBaseAdapter<String> commStrAdapter = null;
	
	protected CommForMapBaseAdapter commapBaseAdapter = null;

	protected Map<String, String[]> commMapArray = null;

	protected Map<String, List<String>> commMapList = null;

	protected String queryWhere = "";

	// 用户没有登录注册时获取UUID
	public static String uTokenId = "0";
	
	// 标示用户本地的id
	protected String tokenId = "";
	
	protected int updResult = -1;

	protected boolean queryResult = false;

	protected int whichTab = 1;

	protected String[] item_values = null;

	protected List<String> mList = null;
	
	protected List<Integer> nList = null;

	protected String fieldNull = null;
	
	protected Integer checkColor = 0;
	
	protected String deviceID = "";// 设备标识码
	
	/**
	 * 操作类型 
	 * 0:从服务器同步数据到本地
	 * 1：add 2：update 3：delete
	 */
	protected int actionFlag = 0; 
	
	private static BaseActivity mInstance;
	
	protected PreferenceUtil preferenceUtil;
	
//	protected ImageLoader mImageLoader;
	
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
		if(preferenceUtil == null)
			preferenceUtil = new PreferenceUtil(self);
		fieldNull = CommUtil.getStrValue(self, R.string.action_input_isnull);
		
		TelephonyManager telephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		deviceID = telephonyMgr.getDeviceId();
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
	 * @Description: 设置顶部标题 及 图标
	 * @author Comsys-WH1510032
	 * @return 返回类型
	 * @param id
	 */
	protected void setTopTitle(String str) {
		toptext.setText(str);
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
	 * @Description: 设置顶部左边按钮图标
	 * @param resId
	 */
	protected void setLeftIcon(Bitmap bitmap) {
		left_icon.setImageBitmap(bitmap);
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
	 * @Description: 设置界面消息delayMillis隐藏
	 * @param id
	 */
	protected void set3Msg(int id,long delayMillis) {
		msg.setText(CommUtil.getStrValue(self, id));
		msg.setVisibility(View.VISIBLE);
		msg.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				setMsgHide();
			}
		}, delayMillis);
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
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置界面消息
	 * @author Comsys-WH1510032
	 * @return 返回类型  
	 * @param id
	 * @param visibility
	 */
	protected void setMsgVisibility(int visibility) {
		msg.setVisibility(visibility);
	}

	protected void startActivity(String src, boolean finish) {
		ActivityUtils.startActivity(self, Constants.PACKAGENAME + src, finish);
	}
	
	protected void startChildActivity(String src, boolean finish) {
		ActivityUtils.startActivity(self, Constants.PACKAGENAMECHILD + src, finish);
	}


	/**
	 * 语言切换
	 * @param newLang
	 */
	protected void switchLang(String newLang) {
		preferenceUtil.setPreferenceData("LANGUAGE", newLang);
		while (0 != mLocalStack.size()) {
			mLocalStack.pop().finish();
		} 
		// finish app内存中的所有activity
		MyApplication.getApplication().exitAll();
		startChildActivity("HomeActivity", false);
	}
	 

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
		if (RegexUtil.checkNotNull(s.toString())) {
			setMsgHide();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			startActivity(Constants.MAINACTIVITY,false);
			break;
		default:
			break;
		}
	}

}
