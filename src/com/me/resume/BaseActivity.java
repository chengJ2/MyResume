package com.me.resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.DownloadTask;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.tools.SystemBarTintManager;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.PreferenceUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.CustomFAB;
import com.me.resume.views.MarqueeText;
import com.whjz.android.text.CommonText;

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
	
	protected CommForMapArrayBaseAdapter commMapArrayAdapter = null;

	protected Map<String, String[]> commMapArray = null;

	protected Map<String, List<String>> commMapList = null;

	protected String queryWhere = "";
	
	protected String tableName = "";

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
	
	// resume review bg color
	protected String checkColor = "#CC0000";
	
	protected String deviceID = "";// 设备标识码
	
	protected Boolean localHasData = false;// 本地是否有数据
	
	private ViewHolder viewHolder;
	
	/**
	 * 栏目多项id
	 */
	protected String KID = "0";
	
	/**
	 * 操作类型 
	 * 0:从服务器同步数据到本地
	 * 1：add 2：update 3：delete
	 */
	protected int actionFlag = 0; 
	
	/**
	 * 注册类型 （手机号或者游戏）
	 */
	protected boolean showRegType = false;
	
	/**
	 * 密码框显示密码或者文本
	 */
	private boolean showPwdType = false;
	
	private static BaseActivity mInstance;
	
	protected PreferenceUtil preferenceUtil;
	
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
		toptext.setOnClickListener(this);
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
		
		uTokenId = preferenceUtil.getPreferenceData(UserInfoCode.UTOKENID, "0");
		fieldNull = getStrValue(R.string.action_input_isnull);
		
		TelephonyManager telephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		deviceID = telephonyMgr.getDeviceId();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.top_bar);
		// tintManager.setStatusBarAlpha(1); // 透明度
		// tintManager.setStatusBarTintDrawable(drawable); // 设置Drawable
		// tintManager.setStatusBarTintColor(color); // 设置Color 
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
	 * @param id
	 */
	protected void setTopBarVisibility(int visibility) {
		topLayout.setVisibility(visibility);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置顶部标题 及 图标
	 * @param id
	 */
	protected void setTopTitle(int id) {
		toptext.setText(getStrValue(id));
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置顶部标题 及 图标
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
		right_icon.setVisibility(View.VISIBLE);
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
	
	protected void loadWaitting() {
		Animation operatingAnim = AnimationUtils.loadAnimation(self, R.anim.resetup);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		right_icon.startAnimation(operatingAnim);
		right_icon.setClickable(false);
	}
	
	protected void stopLoad() {
		right_icon.clearAnimation();
		right_icon.setClickable(true);
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
		saveButton.setVisibility(View.VISIBLE);
		saveButton.setImageResource(resId);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置右边按钮图标
	 * @param resId
	 */
	protected void seNextBtnSrc(int resId){
		nextButton.setVisibility(View.VISIBLE);
		nextButton.setImageResource(resId);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置右边按钮隐藏
	 * @param resId
	 */
	protected void setNextBtnHide(){
		nextButton.setVisibility(View.GONE);
	}
	
	/**
	 * 预览界面的背景色
	 * @return checkColor
	 */
//	protected String getCheckColor(String checkColor){
//		return String.valueOf(checkColor);
//	}
	
	/**
	 * 获得String值
	 * @param resId
	 * @return
	 */
	protected String getStrValue(int resId){
		return self.getResources().getString(resId);
	}
	
	/**
	 * 获得Color值
	 * @param resId
	 * @return
	 */
	protected int getColorValue(int resId){
		return self.getResources().getColor(resId);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置界面消息
	 * @param id
	 */
	protected void setMsg(int id) {
		msg.setText(getStrValue(id) + fieldNull);
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
		msg.setText(getStrValue(id));
		msg.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置界面消息
	 * @param id
	 */
	protected void set2Msg(String s) {
		msg.setText(s.toString().trim());
		msg.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 
	 * @Title:BaseActivity
	 * @Description: 设置界面消息delayMillis隐藏
	 * @param id
	 */
	protected void set3Msg(int id,long delayMillis) {
		msg.setText(getStrValue(id));
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
		msg.setText(getStrValue(id));
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
	 * @Description: 设置界面消息
	 */
	protected void setMsgHide() {
		msg.setVisibility(View.GONE);
	}
	
	/**
	 * 
	 * @Description: 设置界面消息
	 * @param visibility
	 */
	protected void setMsgVisibility(int visibility) {
		msg.setVisibility(visibility);
	}

	/**
	 * Activity跳转
	 * @param src
	 * @param finish
	 */
	protected void startActivity(String src, boolean finish) {
		ActivityUtils.startActivity(self, Constants.PACKAGENAME + src, finish);
	}
	
	/**
	 *子Activity跳转
	 * @param src
	 * @param finish
	 */
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

//	/**
//	 * 
//	 * @Title:BaseActivity
//	 * @Description: 更新UI
//	 * @param id
//	 */
//	protected void runOnUiThread(final int id) {
//		runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				set3Msg(id);
//			}
//		});
//	}
	
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

	/**
	 * 显示与隐藏密码框
	 * @param pwdEditText
	 */
	protected void showOrHidePwd(EditText editText,ImageView imageView){
		showPwdType = !showPwdType;
		if (showPwdType) {
			imageView.setImageResource(R.drawable.icon_pwd_show);
			editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}else{
			imageView.setImageResource(R.drawable.icon_pwd_hide);
			editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
	}
	
	/**
	 * 注册类型 （手机号或者游戏）
	 * @param editText
	 */
	protected void userRegisterType(EditText editText){
		showRegType = !showRegType;
		Resources regType = getResources();
		Drawable img_regtype_left = null;
		if (showRegType) {
			editText.setHint(getStrValue(R.string.register_input3_hint));
			img_regtype_left = regType.getDrawable(R.drawable.icon_email);
			editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		}else{
			editText.setHint(getStrValue(R.string.register_input2_hint));
			img_regtype_left = regType.getDrawable(R.drawable.icon_phone);
			editText.setInputType(InputType.TYPE_CLASS_PHONE);
		}
		img_regtype_left.setBounds(0, 0, img_regtype_left.getMinimumWidth(), img_regtype_left.getMinimumHeight());
		editText.setCompoundDrawables(img_regtype_left, null, null, null); //设置左图标
	}
	
	/**
	 * 获取弹出窗数据
	 * @param array
	 * @param parent
	 * @param resId
	 */
	protected void getValues(int array,View parent,int resId,Handler mHandler) {
		String[] item_text = CommUtil.getArrayValue(self,array); 
		mList = Arrays.asList(item_text);
		DialogUtils.showPopWindow(self, parent, resId, mList, mHandler);
	}
	
	/**
	 * 获取输入框值
	 * @param editText
	 * @return
	 */
	protected  String getEditTextValue(EditText editText) {
		String value = editText.getText().toString().trim();
		if (RegexUtil.checkNotNull(value)) {
			return value;
		}
		return "";
	}
	
	/**
	 * 获取文本值
	 * @param editText
	 * @return
	 */
	protected String getTextValue(TextView textView) {
		String value = textView.getText().toString().trim();
		if (RegexUtil.checkNotNull(value)) {
			return value;
		}
		return "";
	}
	
	/**
	 * 
	 * @Title:CommUtil
	 * @Description: 判断编辑框是否为空
	 * @param editText
	 * @return
	 */
	protected boolean editTextIsNull(EditText editText) {
		String value = editText.getText().toString().trim();
		if (RegexUtil.checkNotNull(value)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @Title:CommUtil
	 * @Description: 判断文本是否为空
	 * @param textView
	 * @return boolean
	 */
	protected boolean textIsNull(TextView textView) {
		String value = textView.getText().toString().trim();
		if (RegexUtil.checkNotNull(value)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 通过远程的key获取values插入到本地
	 * @param mapArray
	 * @param map
	 * @param key
	 * @param position
	 * @return
	 */
	protected String getServerKeyValue(Map<String, List<String>> map,String key,int position){
		String value = "";
		List<String> listStr = map.get(key);
		if(listStr != null && listStr.size()>0){
			value = listStr.get(position);
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
	protected String getServerKeyValue(Map<String, List<String>> map,String key){
		String value = "";
		List<String> listStr = map.get(key);
		if(listStr != null && listStr.size()>0){
			value = listStr.get(0);
		}
		return value;
	}
	
	 int maxDescripLine = 3; //TextView默认最大展示行数
	/**
	 * @Description: 面试分享心得
	 */
	protected void setShareData(ListView listview,final Map<String, List<String>> map){
		commapBaseAdapter = new CommForMapBaseAdapter(self,map,R.layout.home_share_item,"id") {
			
			@Override
			public void convert(final ViewHolder holder, List<String> item, final int position) {
				/*String userIsOnline =  map.get("userstatus").get(position);
				if (userIsOnline.equals("1")) {
					holder.setImageVisibe(R.id.user_online, View.VISIBLE);
				}else{
					holder.setImageVisibe(R.id.user_online, View.GONE);
				}*/
				String avatorStr = map.get("avator").get(position);
				if (RegexUtil.checkNotNull(avatorStr)) {
					holder.showImage(R.id.share_usernameavator,
							CommUtil.getHttpLink(map.get("avator").get(position)),true);
				}else{
					holder.setImageResource(R.id.share_usernameavator, R.drawable.user_default_avatar);
				}
				
				String realname = map.get("realname").get(position);
				if (RegexUtil.checkNotNull(realname)) {
					holder.setText(R.id.share_username, realname);
				}else{
					if (preferenceUtil.getPreferenceData(UserInfoCode.LOGINPATFORM, "app").equals("app")) {
						holder.setText(R.id.share_username, map.get("username").get(position));
					}else{
						holder.setText(R.id.share_username, map.get("nickname").get(position));
					}
				}
				
				String jobtitleStr = map.get("expworkindustry").get(position);
				String workyear = map.get("joinworktime").get(position);
				if (!RegexUtil.checkNotNull(jobtitleStr) && !RegexUtil.checkNotNull(workyear)) {
					holder.setViewVisible(R.id.info2Layout, View.GONE);
				}else{
					holder.setViewVisible(R.id.info2Layout, View.VISIBLE);
					
					if (RegexUtil.checkNotNull(jobtitleStr)) {
						holder.setTextVisibe(R.id.share_jobtitle, View.VISIBLE);
						holder.setText(R.id.share_jobtitle, jobtitleStr);
					}else{
						holder.setTextVisibe(R.id.share_jobtitle, View.GONE);
					}
					
					if (RegexUtil.checkNotNull(workyear)) {
						int year = CommUtil.parseInt(workyear.substring(0, 4));
						int theYear = CommUtil.parseInt(TimeUtils.theYear());
						holder.setTextVisibe(R.id.share_workyear, View.VISIBLE);
						
						int work = (theYear - year);
						if (work <= 0) {
							holder.setText(R.id.share_workyear,getStrValue(R.string.personal_c_item17));
						}else{
							holder.setText(R.id.share_workyear, String.format(getStrValue(R.string.personal_c_item18), work));
						}
					}else{
						holder.setTextVisibe(R.id.share_workyear, View.GONE);
					}
				}
				
				holder.setText(R.id.share_content, map.get("content").get(position));
				holder.setText(R.id.share_city, map.get("city").get(position));
				holder.setText(R.id.share_datime, TimeUtils.showTimeFriendly(map.get("createtime").get(position)));
				
				final String cid = map.get("id").get(position);
				
				queryWhere = "select * from " + CommonText.MYCOLLECTION 
						+ " where cid = "+ cid +" and userId = '"+ uTokenId+"' and type < 0";
				commMapArray = dbUtil.queryData(self, queryWhere);
				if (commMapArray == null) {
					holder.setImageResource(R.id.share_collection, R.drawable.icon_collection_nor);
				}else{
					if (!MyApplication.USERID.equals("0")) {
						holder.setImageResource(R.id.share_collection, R.drawable.icon_collection_sel);
					}
				}
				
				holder.setOnClickEvent(R.id.share_content, new ClickEvent() {
					@Override
					public void onClick(View view) {
						String content = map.get("content").get(position);
						DialogUtils.showTextDialog(self, content);
					}
				});
				
				holder.setOnClickEvent(R.id.share_collection, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						if (!MyApplication.USERID.equals("0")) {
							queryWhere = "select * from " + CommonText.MYCOLLECTION 
									+ " where cid = "+ cid +" and userId = '"+ uTokenId+"' and type < 0";
							commMapArray = dbUtil.queryData(self, queryWhere);
							if (commMapArray == null) {
								addCollection(map,position);
							}else{
								queryWhere = "delete from " + CommonText.MYCOLLECTION 
										+ " where cid = "+ cid +" and userId = '"+ uTokenId+"' and type < 0";
								dbUtil.deleteData(self, queryWhere);
								commapBaseAdapter.notifyDataSetChanged();
								toastMsg(R.string.item_text91);
							}
						}else{
							toastMsg(R.string.action_login_head);
						}
					}
				});
			}
		};
		
		listview.setAdapter(commapBaseAdapter);
		
		listview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:
					if (viewHolder != null) {
						viewHolder.setFlagBusy(true);
					}
					break;
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (viewHolder != null) {
						viewHolder.setFlagBusy(false);
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					if (viewHolder != null) {
						viewHolder.setFlagBusy(false);
					}
					break;
				default:
					break;
				}
				commapBaseAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		
	}
	
	private boolean isDownload = false;
	/**
	 * 简历封面数据显示
	 * @param listview
	 * @param map
	 * @param isLocal
	 */
	private String localcover = "";
	private CommForMapBaseAdapter coverAdapter = null;
	protected void setCoverData(GridView gridView,final Map<String, List<String>> map,final boolean isLocal){
		coverAdapter = new CommForMapBaseAdapter(self,map,R.layout.home_cover_gridview_item,"id") {
			
			@Override
			public void convert(final ViewHolder holder, List<String> item, final int position) {
				preferenceUtil.setPreferenceData(Constants.ISLOCAL, isLocal);
				String note = map.get("note").get(position);
				if (RegexUtil.checkNotNull(note)) {
					holder.setText(R.id.item2, note);
				}else{
					holder.setText(R.id.item2, getStrValue(R.string.item_text101));
				}
				
				if (isLocal) {
					holder.setText(R.id.item3, getStrValue(R.string.button_canuse));
					localcover = map.get("url").get(position);
					holder.setImageResource(R.id.item1,CommUtil.parseInt(localcover));
					
					String lacalCoverName = preferenceUtil.getPreferenceData(Constants.COVER,"");
					if (RegexUtil.checkNotNull(lacalCoverName)){
						if (lacalCoverName.equals(localcover)) {
							holder.setText(R.id.item3, getStrValue(R.string.button_useing));
						}else{
							holder.setText(R.id.item3, getStrValue(R.string.button_canuse));
						}
					}
				}else{
					String coverPath = CommUtil.getHttpLink(map.get("url").get(position));
					holder.showImage(R.id.item1,coverPath,false);
					String fileNameStr = FileUtils.getFileName(coverPath);
					queryWhere = "select * from " + CommonText.COVER_FILE 
							+ " where filename = '"+ fileNameStr +"' and isfinish = 1";
					commMapArray = dbUtil.queryData(self, queryWhere);
					if (commMapArray != null && commMapArray.get("filename").length > 0) {
						if (FileUtils.existsFile(FileUtils.COVER_DOWNLOAD_APKPATH + fileNameStr)) {
							String lacalCoverName = preferenceUtil.getPreferenceData(Constants.COVER,"");
							if (RegexUtil.checkNotNull(lacalCoverName)){
								if (fileNameStr.toString().equals(lacalCoverName)) {
									holder.setText(R.id.item3, getStrValue(R.string.button_useing));
								}else{
									holder.setText(R.id.item3, getStrValue(R.string.button_canuse));
								}
							}else{
								holder.setText(R.id.item3, getStrValue(R.string.button_canuse));
							}
						}else{
							holder.setText(R.id.item3, getStrValue(R.string.button_canuse));
						}
					}else{
						holder.setText(R.id.item3,getStrValue(R.string.button_use));
					}
				}
				
				holder.setOnClickEvent(R.id.item3, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						if (!isLocal) {
							String coverPath = CommUtil.getHttpLink(map.get("url").get(position));
							String fileNameStr = FileUtils.getFileName(coverPath);
							if (!isDownload) {
								queryWhere = "select * from " + CommonText.COVER_FILE 
										+ " where filename = '"+ fileNameStr +"' and isfinish = 1";
								commMapArray = dbUtil.queryData(self, queryWhere);
								if (commMapArray == null) {
									holder.setText(R.id.item3, getStrValue(R.string.button_downloading));
									new DownloadTask(mHandler,1).execute(coverPath);
								}else{
									String file = FileUtils.COVER_DOWNLOAD_APKPATH + fileNameStr;
									if (!FileUtils.existsFile(file)) {
										holder.setText(R.id.item3, getStrValue(R.string.button_downloading));
										new DownloadTask(mHandler,1).execute(coverPath);
									}else{
										preferenceUtil.setPreferenceData(Constants.COVER,fileNameStr);
										coverAdapter.notifyDataSetChanged();
									}
								}
							}else{
								toastMsg(R.string.item_text100);
							}
						}else{
							localcover = map.get("url").get(position);
							preferenceUtil.setPreferenceData(Constants.COVER,localcover);
							coverAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		};
		
		gridView.setAdapter(coverAdapter);
	}
	
	/**
	 * 添加到我的收藏
	 * @param holder
	 * @param map
	 * @param position
	 */
	private void addCollection(Map<String, List<String>> map,int position){
		String cid = map.get("id").get(position);
		String content = map.get("content").get(position);
		String sharename = map.get("realname").get(position);
		if (!RegexUtil.checkNotNull(sharename)) {
			sharename = map.get("username").get(position);
		}

		String shareuserId = map.get("userId").get(position);
		String shareuserCity = map.get("city").get(position);
		
		ContentValues cValues = new ContentValues();
		cValues.put("cId", cid);
		cValues.put("userId", uTokenId);
		cValues.put("shareUserId", shareuserId);
		cValues.put("content", content);
		cValues.put("sharename", sharename);
		cValues.put("sharenamecity", shareuserCity);
		cValues.put("createtime", TimeUtils.getCurrentTimeInString());
		cValues.put("type", "-1");// <0:面试分享心得; >0:话题

		queryResult = dbUtil.insertData(self, CommonText.MYCOLLECTION, cValues);
		if (queryResult) {
			toastMsg(R.string.item_text9);
			commapBaseAdapter.notifyDataSetChanged();
			//TODO 同步到远程
			setSyncData(new String[]{cid,uTokenId,"0","",content,"","","","",shareuserId,sharename,shareuserCity,"-1"});
		}
	}
	
	/**
	 * 同步我的收藏
	 */
	protected void setSyncData(String[] data){
		if (!MyApplication.USERID.equals("0")) {
			if (CommUtil.isNetworkAvailable(self)) {
//				set3Msg(R.string.action_syncing,Constants.DEFAULTIME);
				syncData(data);
			}
		}
	}
	
	/**
	 * 
	 * @Description: 同步数据(判断库是否存在记录)
	 */
	private void syncData(final String[] data){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_cId");
		params.add("p_userId");
		values.add(data[0]);
		values.add(data[1]);
		
		requestData("pro_get_collection", 1, params, values, new HandlerData() {
			@Override
			public void error() {
//				set3Msg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
			}

			@Override
			public void noData() {
				syncRun(data,2);
			}
		});
	}
	
	/**
	 * 执行同步数据请求
	 * @param style 2：add 3.update
	 */
	private void syncRun(String[] data,int style){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_cId");
		params.add("p_userId");
		params.add("p_topicId");
		params.add("p_title");
		params.add("p_content");
		params.add("p_from_url");
		params.add("p_detail_url");
		params.add("p_site_name");
		params.add("p_link_site");
		params.add("p_shareUserId");
		params.add("p_sharename");
		params.add("p_sharenamecity");
		params.add("p_type");
		
		values.add(data[0]);
		values.add(data[1]);
		values.add(data[2]);
		values.add(data[3]);
		values.add(data[4]);
		values.add(data[5]);
		values.add(data[6]);
		values.add(data[7]);
		values.add(data[8]);
		values.add(data[9]);
		values.add(data[10]);
		values.add(data[11]);
		values.add(data[12]);
		
		requestData("pro_set_collection", style, params, values, new HandlerData() {
			@Override
			public void error() {
//				set3Msg(R.string.timeout_network);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
//					if (map.get(ResponseCode.MSG).get(0).equals(ResponseCode.RESULT_OK)) {
//						set3Msg(R.string.action_sync_success);
//					}
				} catch (Exception e) {
//					set3Msg(R.string.action_sync_fail);
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
//				set3Msg(R.string.action_sync_fail);
			}
		});
	}
	
	/**
	 * 删除远端数据(我的收藏)
	 */
	protected void syncDelData(String cId){ 
		if (CommUtil.isNetworkAvailable(self)) {
			List<String> params = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			
			params.add("p_cId");
			params.add("p_userId");
			values.add(cId);
			values.add(uTokenId);
			
			requestData("pro_get_collection", 3, params, values, new HandlerData() {
				
				public void success(Map<String, List<String>> map) {
					try {
						if (map.get(ResponseCode.MSG).get(0).equals(ResponseCode.RESULT_OK)) {
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void noData() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void error() {
//					set3Msg(R.string.timeout_network);
				}
			});
		}
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DownloadTask.DOWNLOAD_PROGRESS:
				isDownload = true;
				break;
			case DownloadTask.DOWNLOAD_COMPLETE:
				isDownload = false;
				String file = (String) msg.obj;
				String filename = FileUtils.getFileName(file);
				queryWhere = "select * from " + CommonText.COVER_FILE 
						+ " where filename = '"+ filename +"' and isfinish = 1";
				commMapArray = dbUtil.queryData(self, queryWhere);
				if (commMapArray == null) {
					ContentValues cValues = new ContentValues();
					cValues.put("filename", filename);
					cValues.put("isfinish", 1);
					queryResult = dbUtil.insertData(self, CommonText.COVER_FILE, cValues);
					if (queryResult) {
						preferenceUtil.setPreferenceData(Constants.COVER, filename);
						coverAdapter.notifyDataSetChanged();
					}
				}
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
			if (RegexUtil.checkNotNull(preferenceUtil.getPreferenceData(UserInfoCode.REALNAME,""))) {
				startActivity(Constants.MAINACTIVITY,false);
			}else{
				set3Msg(R.string.action_baseinfo_null);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
		DialogUtils.dismissProgressDialog();
		DialogUtils.dismissPopwindow();
	}
}
