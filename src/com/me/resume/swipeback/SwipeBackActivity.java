package com.me.resume.swipeback;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.tools.SystemBarTintManager;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.Constants;
import com.whjz.android.text.Info;
import com.whjz.android.util.common.CommonUtil;
import com.whjz.android.util.common.DataSetList;
import com.whjz.android.util.common.DbUtilImplement;
import com.whjz.android.util.interfa.BaseCommonUtil;
import com.whjz.android.util.interfa.DbLocalUtil;

/**
 * 
* @ClassName: SwipeBackActivity 
* @Description: CommActivity 
* @author Comsys-WH1510032 
* @date 2016/4/7 下午1:42:36 
*
 */
public class SwipeBackActivity extends FragmentActivity implements
		SwipeBackActivityBase {
	
	protected SwipeBackActivity self;
	
	private SwipeBackActivityHelper mHelper;
	
	protected Map<String, String[]> commMapArray = null;
	
	protected Map<String, List<String>> commMapList = null;
	
	protected String queryWhere = "";
	
	protected int updResult = -1;
	
	protected boolean queryResult = false;
	
	protected int whichTab = 1;
	
	protected String[] item_values = null;
	
	protected List<String> mList = null;
	
	// activity访问栈
    private static Stack<FragmentActivity> mLocalStack = new Stack<FragmentActivity>();

	protected DbLocalUtil dbUtil = new DbUtilImplement();;// 本地数据库对象
	protected BaseCommonUtil baseCommon = new CommonUtil();;// 通用工具对象实例
	protected Info info = new Info();

	protected SharedPreferences sp;
	private AsyncTask task;
	private ProgressDialog progressDialog = null;
	
	// 请求超时
	public static final int EXECUTE_TIMEOUT = -0X2000;
	
	// 请求网络异常
	public static final int EXECUTE_NETERROR = -0X1000;
	
	// 加载数据成功
	public static final int LOAD_DATA_SUCCESS = 0X1000;
	
	// 加载数据失败
	public static final int LOAD_DATA_ERROR = 0X8000;
	
	// 加载暂无数据
	public static final int LOAD_NO_DATA = -0X8000;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Constants.DEVELOPER_MODE) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork() 
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}
		super.onCreate(savedInstanceState);
		self = SwipeBackActivity.this;
		MyApplication.getApplication().addActivity(this);

		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivtyCreate();
		
		sp = getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE);
	        
	    mLocalStack.add(this);
	}
	
    protected void switchLang(String newLang){
    	setPreferenceData("LANGUAGE",newLang);
        // finish app内存中的所有activity
    	while (0 != mLocalStack.size()) {
            mLocalStack.pop().finish();
        }
        // 跳转到app首页
//    	WiApplication.getApplication().exitAll();
//      ActivityUtils.startActivity(BaseActivity.this, WiApplication.PACKAGENAME+".ui.WiVehicleMainActivity");
    }

	/**
     * 
     * @Title:BaseActivity
     * @Description: Find View By Id
     * @author Comsys-WH1510032
     * @return View  
     * @param viewID
     */
    public <T extends View> T findView(int viewID){
    	return (T)findViewById(viewID);
    }
	
    protected void toastMsg(int resId){
    	CommUtil.ToastMsg(self, resId);
    }
    
    protected void startActivity(String src,boolean finish){
    	   ActivityUtils.startActivity(self, MyApplication.PACKAGENAME + src,finish);
    }
    
    public void setPreferenceData(String key, String value){
    	sp.edit().putString(key, value).commit();
    }
    
    public String getPreferenceData(String str,String def){
    	return sp.getString(str, def);
    }
    
    public void setPreferenceData(String key, int value){
    	sp.edit().putInt(key, value).commit();
    }
    
    public int getPreferenceData(String str,int def){
    	return sp.getInt(str, def);
    }
    
    public void setPreferenceData(String key, boolean value){
    	sp.edit().putBoolean(key, value).commit();
    }
    
    public boolean getPreferenceData(String str){
    	return sp.getBoolean(str, false);
    }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate();
	}

	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v == null && mHelper != null)
			return mHelper.findViewById(id);
		return v;
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

	@Override
	public SwipeBackLayout getSwipeBackLayout() {
		return mHelper.getSwipeBackLayout();
	}

	@Override
	public void setSwipeBackEnable(boolean enable) {
		getSwipeBackLayout().setEnableGesture(enable);
	}

	@Override
	public void scrollToFinishActivity() {
		getSwipeBackLayout().scrollToFinishActivity();
	}

	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
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

	/**
	 * 处理点击返回按钮的情况
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if(progressDialog != null && task != null && task.getStatus() ==
			// AsyncTask.Status.RUNNING ){
			//
			// }else{
			getSwipeBackLayout().scrollToFinishActivity();

			// }
		}
		return false;
	}

	private HandlerData handlerData;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			Map<String, List<String>> map = (Map<String, List<String>>) msg.obj;
			if (map == null || map.size() == 0) {
				handlerData.error();
			} else {
				handlerData.success(map);
			}
		};
	};

	/**
	 * 请求数据
	 * 
	 * @param str 接口名
	 * @param style 请求类型
	 * @param params 请求参数
	 * @param values 请求值
	 * @param progressDialog 加载框
	 * @return
	 */
	protected void requestData(final String str, final int style,
			final List<String> params, final List<String> values,
			ProgressDialog progressDialog, final HandlerData handlerData) {

		task = new MyAsyncTask(str, style, params, values, progressDialog,
				handlerData).execute();
	}

	protected void requestData(final String str, final int style,
			final List<String> params, final List<String> values,
			final HandlerData handlerData) {

		task = new MyAsyncTask(str, style, params, values, handlerData)
				.execute();
	}
	
	/**
	 * 请求数据（本地持久）
	 * @param str 接口名
	 * @param style 请求类型
	 * @param params 请求参数
	 * @param values 请求值
	 * @param tablename 表名
	 * @param where 条件
	 * @param progressDialog 加载框
	 * @param handlerData
	 */
	protected void requestData(final String str, final int style, final List<String> params, final List<String> values,
			final String tablename,final String where,
			ProgressDialog progressDialog, final HandlerData handlerData) {
		task = (MyAsyncTask) new MyAsyncTask(str, style, params, values,true,tablename,where,progressDialog, handlerData).execute();
	}
	
	
	protected void requestData(final String str, final int style, final List<String> params, final List<String> values,
			final String tablename,final String where, final HandlerData handlerData) {
		task = (MyAsyncTask) new MyAsyncTask(str, style, params, values,true,tablename,where,handlerData).execute();
	}
	

	class MyAsyncTask extends AsyncTask<String, Integer, Integer> {

		String procname;// 接口名
		int style;
		List<String> params;
		List<String> values;
		HandlerData handlerData;
		DataSetList dataSetlist;

		Map<String, List<String>> map = null;
		
		String tablename; // 表名
		String where;// 条件
		boolean isLocalCache; // 开启本地缓存
		
		public MyAsyncTask(String str, int style, List<String> params, List<String> values,boolean isLocalCache,
				String tablename,String where, ProgressDialog progressDialog, HandlerData handlerData) {
			this.procname = str;
			this.style = style;
			this.params = params;
			this.values = values;
			this.handlerData = handlerData;
			this.isLocalCache = isLocalCache;
			SwipeBackActivity.this.progressDialog = progressDialog;
			this.tablename = tablename;
			this.where = where;
			progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						cancel(true);
						SwipeBackActivity.this.progressDialog = null;
						task = null;
					}
					return false;
				}
			});	
		}
		
		public MyAsyncTask(String str, int style, List<String> params, List<String> values,boolean isLocalCache,
				String tablename,String where, HandlerData handlerData) {
			this.procname = str;
			this.style = style;
			this.params = params;
			this.values = values;
			this.handlerData = handlerData;
			this.isLocalCache = isLocalCache;
			this.tablename = tablename;
			this.where = where;
		}
		

		public MyAsyncTask(String str, int style, List<String> params,
				List<String> values, ProgressDialog progressDialog,
				HandlerData handlerData) {

			this.procname = str;
			this.style = style;
			this.params = params;
			this.values = values;
			this.handlerData = handlerData;
			SwipeBackActivity.this.progressDialog = progressDialog;
			progressDialog
					.setOnKeyListener(new DialogInterface.OnKeyListener() {
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								cancel(true);
								SwipeBackActivity.this.progressDialog = null;
								task = null;
							}
							return false;
						}
					});

		}

		
		public MyAsyncTask(String str, int style, List<String> params, List<String> values, 
				boolean isLocalCache,HandlerData handlerData) {
			this.procname = str;
			this.style = style;
			this.params = params;
			this.values = values;
			this.isLocalCache = isLocalCache;
			this.handlerData = handlerData;
		}
		
		
		public MyAsyncTask(String str, int style, List<String> params,
				List<String> values, HandlerData handlerData) {
			this.procname = str;
			this.style = style;
			this.params = params;
			this.values = values;
			this.handlerData = handlerData;
		}
		
		@Override
		protected Integer doInBackground(String... arg0) {
			dataSetlist = baseCommon.selects(info.getUse(), info.getPass(),
					procname, style, params, values);
			if (isCancelled()) {
				return LOAD_DATA_ERROR;
			}
			if (!CommUtil.isNetworkAvailable(SwipeBackActivity.this)) {
				return EXECUTE_NETERROR;
			}

			if (dataSetlist != null && dataSetlist.valueList.size() > 0) {
				if (dataSetlist.valueList.get(0).equals("timeout")) {
					return EXECUTE_TIMEOUT;
				} else {
					if (isLocalCache) {
						dbUtil.deleteData(SwipeBackActivity.this, where);
						dbUtil.insertDataSetList(SwipeBackActivity.this, tablename, dataSetlist);
					}
					map = dataSetlist.getMap();
					return LOAD_DATA_SUCCESS;
				}
			} else {
				return LOAD_NO_DATA;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if (result == LOAD_NO_DATA || result == LOAD_DATA_ERROR) {
				handlerData.error();
			} else if (result == LOAD_DATA_SUCCESS) {
				handlerData.success(map);
			} else if (result == EXECUTE_TIMEOUT) {
				CommUtil.ToastMsg(SwipeBackActivity.this, R.string.timeout_network);
			} else if (result == EXECUTE_NETERROR) {
				CommUtil.ToastMsg(SwipeBackActivity.this, R.string.check_network);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!CommUtil.isNetworkAvailable(SwipeBackActivity.this)) {
				return;
			}
			if (progressDialog != null) {
				progressDialog.show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

		}
	}
	
	/**
	 * 请求数据的回调
	 */
	public  abstract interface HandlerData {
		public abstract void success(Map<String, List<String>> map);

		public abstract void error();
	}
	
	 @Override
    protected void onDestroy() {
        // 出栈
    	mLocalStack.remove(this);
        super.onDestroy();
    }
	 
}
