package com.me.resume.ui;

import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.me.resume.utils.DialogUtils;

/**
 * 
 * @ClassName: BaseActivity
 * @Description: 基类
 * @date 2016/4/22 上午10:51:57
 * 
 */
public class BaseActivity extends SwipeBackActivity implements TextWatcher{

	private RelativeLayout topLayout;
	
	protected TextView toptext;
	protected ImageView left_icon, right_icon,right_icon_more;
	protected TextView msg;
	
	protected LinearLayout boayLayout;
	
	protected BaseActivity self;
	protected SharedPreferences sp;
	
	protected CommonBaseAdapter<String> commStrAdapter = null;

	protected Map<String, String[]> commMapArray = null;

	protected Map<String, List<String>> commMapList = null;

	protected String queryWhere = "";

	protected int updResult = -1;

	protected boolean queryResult = false;

	protected int whichTab = 1;

	protected String[] item_values = null;

	protected List<String> mList = null;
	
	protected List<Integer> nList = null;

	protected String fieldNull = null;
	
	private Integer checkColor = 0;
	
	private Boolean isChecked = false;
	
//	private static BaseActivity mInstance;
//	
//	public static BaseActivity getInstance(){
//		if (mInstance == null) {
//			mInstance = new BaseActivity();
//		}
//		return mInstance;
//	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 11:
				if (msg.obj != null) {
					checkColor = (Integer) msg.obj;
				}
				break;
			case 12:
				if (msg.obj != null) {
					isChecked = (boolean) msg.obj;
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
		setContentView(R.layout.activity_base_layout);
		self = BaseActivity.this;
		sp = getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE);
		fieldNull = CommUtil.getStrValue(self, R.string.action_input_isnull);
		topLayout = findView(R.id.topLayout);
		toptext = findView(R.id.top_text);
		left_icon = findView(R.id.left_lable);
		right_icon = findView(R.id.right_icon);
		right_icon_more = findView(R.id.right_icon_more);
		msg = findView(R.id.msg);
		
		boayLayout = findView(R.id.bodyLayout);
		
		right_icon_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogUtils.showTopMenuDialog(self, topLayout, mHandler);
			}
		});
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

	protected void setLeftIcon(int resId) {
		left_icon.setImageResource(resId);
	}

	protected void setRightIcon(int resId) {
		right_icon.setImageResource(resId);
	}
	
	protected void setLeftIconVisible(int visibility) {
		left_icon.setVisibility(visibility);
	}
	
	protected void setRightIconVisible(int visibility) {
		right_icon.setVisibility(visibility);
	}
	
	protected void setRight2IconVisible(int visibility) {
		right_icon_more.setVisibility(visibility);
	}
	
	/**
	 * 预览界面的背景色
	 * @return checkColor
	 */
	protected Integer getCheckColor(){
		if (checkColor == 0) {
			return R.color.red;
		}
		return checkColor;
	}
	
	/**
	 * 是否显示编辑模式
	 * @return isChecked
	 */
	protected boolean getEditModeCheck(){
		return isChecked;
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
	protected void setMsg(int id) {
		msg.setText(CommUtil.getStrValue(self, id) + fieldNull);
		msg.setVisibility(View.VISIBLE);
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
	protected void set2Msg(int id) {
		msg.setText(CommUtil.getStrValue(self, id));
		msg.setVisibility(View.VISIBLE);
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

	protected void setPreferenceData(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	protected String getPreferenceData(String str, String def) {
		return sp.getString(str, def);
	}

	protected void setPreferenceData(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	protected int getPreferenceData(String str, int def) {
		return sp.getInt(str, def);
	}

	protected void setPreferenceData(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	protected boolean getPreferenceData(String str) {
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
	 * @author Comsys-WH1510032
	 * @return View
	 * @param viewID
	 */
	protected <T extends View> T findView(int viewID) {
		return (T) findViewById(viewID);
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

}
