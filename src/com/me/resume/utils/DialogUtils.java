package com.me.resume.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.tools.L;
import com.me.resume.views.SwitchButton;
import com.me.resume.views.SwitchButton.OnChangedListener;

/**
 * 
* @ClassName: DialogUtils 
* @Description: 弹出框类
* @date 2015/11/27 下午1:39:10 
*
 */
public class DialogUtils {

	public static Dialog dialog = null;
	
	public static ProgressDialog pDialog = null;
	
	private static Handler mHandler = new Handler();
	
	private static PopupWindow mPopupWindow = null;
	
	/**
	 * 
	 * @param context 上下文
	 */
	public static void showDialog(Context context,String content,String btnText,Handler handler,final int msgwhich){
		try {
			/*mHandler = handler;
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_simple_layout);
//			WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
//		    layoutParams.width = width;  
//		    layoutParams.height = heigth;
//			dialog.getWindow().setAttributes(layoutParams);
			dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
			dialog.setCanceledOnTouchOutside(true);
			TextView msg = (TextView)dialog.findViewById(R.id.content);
			msg.setText(content);
			Button btn = (Button)dialog.findViewById(R.id.connabtn);
			btn.setText(btnText);
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismissDialog();
				}
			});
			dialog.show();*/
		} catch (Exception e) {
			L.e(e);
		}
	}
	
	
	/**
	 * 显示STK初始化时的加载框
	 * */
	public static void showDialog(Context context,int resId,final boolean cancele) {
		// TODO Auto-generated method stub
		pDialog=new ProgressDialog(context);  
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
		pDialog.setMessage(CommUtil.getStrValue(context, resId));
		pDialog.setCancelable(cancele);
//		pDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		pDialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if(cancele){
						dismissProgressDialog();
					}
				}
				return true;
			}
		});
		pDialog.show();
	}
	
	/**
	 * 初始化ProgressDialog
	 */
	public static ProgressDialog getProgressDialog(Context context,int message) {
		ProgressDialog 	mpDialog = new ProgressDialog(context);
		mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
		mpDialog.setMessage(CommUtil.getStrValue(context, message));
		mpDialog.setIndeterminate(false);// 设置进度条是否为不明确
		mpDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		mpDialog.setCanceledOnTouchOutside(false);
//		mpDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_BACK) {
//					if(thread != null){
//						thread.interrupt();
//						thread = null;
//						loadDataFlag = false;
//					}
//				}
//				return false;
//			}
//		});
		return mpDialog;
	}
	
	/**
	 * 批量删除时的加载框
	 * */
	public static void showDialog(Context context,int resId,boolean cancele, final Handler handler,final int msgwhich) {
		pDialog=new ProgressDialog(context);  
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
		pDialog.setMessage(CommUtil.getStrValue(context, resId));
		pDialog.setCancelable(cancele);
//		pDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		pDialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					handler.sendEmptyMessage(msgwhich);
					dismissProgressDialog();
				}
				return true;
			}
		});
		pDialog.show();
	}
	
	/**
	 * 
	 * @Title:DialogUtils
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @author Comsys-WH1510032
	 * @return 返回类型  
	 * @param context
	 * @param resource
	 * @param parent
	 * @param position
	 */
	private static int selecPosition = -1;
	private static HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个 
	public static void showPopWindow(Context context,View parent,int resourId,List<String> mList,Handler handler){
		mHandler = handler;
		View popView = View.inflate(context,R.layout.pop_simple_list_layout, null);
		mPopupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
												LayoutParams.MATCH_PARENT,true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		
		TextView popTitle = (TextView)popView.findViewById(R.id.top_text);
		popTitle.setText(CommUtil.getStrValue(context, resourId));
		
		ImageView rightIcon = (ImageView)popView.findViewById(R.id.icon_cancle);
		rightIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismissPopwindow();
			}
		});
		
		ListView dataListView = (ListView)popView.findViewById(R.id.data_list);
		CommonBaseAdapter<String> commAdapter = new CommonBaseAdapter<String>(context,
					mList,
					R.layout.pop_simple_list_item) {
			
			@Override
			public void convert(ViewHolder holder, String item, final int position) {
				// TODO Auto-generated method stub
				holder.setText(R.id.item_text,mList.get(position));
				final RadioButton radio = (RadioButton) holder.getRadioButton(R.id.item_radio_btn);

				if (!states.isEmpty()) {
					states.clear();
				}

				if (selecPosition == position) {
					states.put(String.valueOf(position), true);
					radio.setChecked(true);
				} else {
					states.put(String.valueOf(position), false);
					radio.setChecked(false);
				}

				holder.setOnClickEvent(R.id.checkLayout, new ClickEvent() {

					@Override
					public void onClick(View view) {
						selecPosition = position;
						
						for (String key : states.keySet()) {
							states.put(key, false);
						}

						states.put(String.valueOf(position), false);

						notifyDataSetChanged();
						
						sendMsg(2,position);
						
						dismissPopwindow();
						
					}
				});
				
				holder.setOnClickEvent(R.id.item_radio_btn, new ClickEvent() {

					@Override
					public void onClick(View view) {
						selecPosition = position;
						
						for (String key : states.keySet()) {
							states.put(key, false);
						}
						states.put(String.valueOf(position), false);

						notifyDataSetChanged();
						
						sendMsg(2,position);
						
						dismissPopwindow();
					}
				});
				
				states.put(String.valueOf(selecPosition), true);
			}
		};
		dataListView.setAdapter(commAdapter);
	}
	
	public static int years,months,days;
	
	public static void showTimeChooseDialog(Context context,View parent,int resId,final int msgWhat,Handler handler){
		mHandler = handler;
		View layout = View.inflate(context,R.layout.date_layout, null);
		mPopupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		
		final DatePicker datePicker = (DatePicker)layout.findViewById(R.id.datePicker);
		//获取当前的年、月、日、小时、分钟  
        Calendar c = Calendar.getInstance();  
        years = c.get(Calendar.YEAR);  
        months = c.get(Calendar.MONTH);  
        days = c.get(Calendar.DAY_OF_MONTH);  
        
        datePicker.init(years, months + 1, days, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int month,
					int day) {
				years = year;  
				months = month + 1;  
				days = day;  
			}
		});
		
        TextView msg = (TextView)layout.findViewById(R.id.title);
		msg.setText(CommUtil.getStrValue(context, resId));
        
		Button btn1 = (Button)layout.findViewById(R.id.btn_sure);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 /*Calendar mycalendar=Calendar.getInstance();//获取现在时间
				 int iyear = mycalendar.get(Calendar.YEAR);//获取年份
				 int imonth = mycalendar.get(Calendar.MONTH);
				 int myYear = UserInfoEditActivity.this.year;
				 if (myYear > iyear) {
					 Utils.ToastMsg(UserInfoEditActivity.this, "出生年月不能大于等于今年");
					 return;
				 }
				 int myMonth = UserInfoEditActivity.this.month;
				 int age = iyear - myYear;
				 if (imonth > myMonth) {
					 age = age + 1;
				 }*/
				 String date = years +"-"+ (months+1) + "-"+days;
				 sendMsg(msgWhat,date);
				 
				 dismissPopwindow();
				 
			}
		});
		
		Button btn2 = (Button)layout.findViewById(R.id.btn_cancle);
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismissPopwindow();
			}
		});
		mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
	
	/**
	 * 
	 * @param context
	 * @param parent
	 * @param resId
	 * @param handler
	 */
	public static void showTopMenuDialog(Activity context,View parent,Handler handler){
		mHandler = handler;
		View layout = View.inflate(context,R.layout.topbar_menu_layout, null);
		mPopupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.setTouchable(true);
		mPopupWindow.setFocusable(true);
		
		GridView bgrid = (GridView)layout.findViewById(R.id.bgrid);
		SwitchButton setting_editmode_cb = (SwitchButton)layout.findViewById(R.id.setting_editmode_cb);
		
		final TypedArray typedArray = context.getResources().obtainTypedArray(R.array.review_bgcolor);
		List<Integer> nList = new ArrayList<Integer>();
		for (int i = 0; i < typedArray.length(); i++) {
			nList.add(typedArray.getResourceId(i, 0));
		}
		
		int size = nList.size();
        int length = 50;
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        bgrid.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        bgrid.setColumnWidth(itemWidth); // 设置列表项宽
        bgrid.setHorizontalSpacing(5); // 设置列表项水平间距
        bgrid.setStretchMode(GridView.NO_STRETCH);
        bgrid.setNumColumns(size); // 设置列数量=列表集合数
        CommonBaseAdapter<Integer> commIntAdapter = new CommonBaseAdapter<Integer>(context, nList,
				R.layout.base_grilview_item) {

			@Override
			public void convert(final ViewHolder holder, final Integer item,
					final int position) {
				holder.setViewBgColor(R.id.itemName, context.getResources().getColor(item));
				holder.setViewVisible(R.id.check, View.GONE);
				
				if (!states.isEmpty()) {
					states.clear();
				}

				if (selecPosition == position) {
					states.put(String.valueOf(position), true);
					holder.setViewVisible(R.id.check, View.VISIBLE);
				} else {
					states.put(String.valueOf(position), false);
					holder.setViewVisible(R.id.check, View.GONE);
				}
				
				holder.setOnClickEvent(R.id.itemName, new ClickEvent() {

					@Override
					public void onClick(View view) {
						
						sendMsg(11, item);
						
						selecPosition = position;
						holder.setViewVisible(R.id.check, View.VISIBLE);
						for (String key : states.keySet()) {
							states.put(key, false);
						}

						states.put(String.valueOf(position), false);
						
//						notifyDataSetChanged();
						
						dismissPopwindow();
					}
				});
			}
		};
		
		bgrid.setAdapter(commIntAdapter);
		
		setting_editmode_cb.setOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(SwitchButton switchButton, final boolean checkState) {
				sendMsg(12, checkState);
			}
		});
		
		mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
	
	/**
	 * 
	 * @param context 上下文
	 */
	public static void showNoticeDialog(Context context,String msgstr){
		try {
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.base_notice_dialog);
			WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
			dialog.getWindow().setAttributes(layoutParams);
			dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
			dialog.setCanceledOnTouchOutside(true);
			TextView msg = (TextView)dialog.findViewById(R.id.msg);
			msg.setText(msgstr);
			Button btn = (Button)dialog.findViewById(R.id.btn);
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (dialog!=null || dialog.isShowing()) {
						dialog.dismiss();
						dialog = null;
					}
				}
			});
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param context 上下文
	 */
	public static void showAlertDialog(Context context,String msgstr,Handler handler){
		try {
			mHandler = handler;
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.base_alert_dialog);
			WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
			dialog.getWindow().setAttributes(layoutParams);
			dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
			dialog.setCanceledOnTouchOutside(true);
			
			TextView noshow = (TextView)dialog.findViewById(R.id.noshow);
			noshow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sendMsg(110);
				}
			});
			
			TextView msg = (TextView)dialog.findViewById(R.id.msg);
			msg.setText(msgstr);
			Button btnCancle = (Button)dialog.findViewById(R.id.btn_cancle);
			btnCancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismissDialog();
				}
			});
			Button btnSure = (Button)dialog.findViewById(R.id.btn_sure);
			btnSure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismissDialog();
					sendMsg(1);
				}
			});
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 隐藏对话框
	 */
	public static void dismissDialog(){
		try {
			if(dialog != null && dialog.isShowing()){
				dialog.dismiss();
			}
		} catch (Exception e) {
			L.e(e);
		}
	}
	
	/**
	 * 隐藏加载对话框
	 */
	public static void dismissProgressDialog(){
		try {
			if(pDialog != null && pDialog.isShowing()){
				pDialog.dismiss();
				pDialog = null;
			}
		} catch (Exception e) {
			L.e(e);
		}
	}
	
	/**
	 * 隐藏加载对话框
	 */
	public static void dismissPopwindow(){
		try {
			if(mPopupWindow != null && mPopupWindow.isShowing()){
				mPopupWindow.dismiss();
				mPopupWindow = null;
			}
		} catch (Exception e) {
			L.e(e);
		}
	} 
	
	/**
	 * 发消息更新UI
	 * @param what
	 * @param obj
	 */
	public static void sendMsg(int what,Object obj){
		mHandler.sendMessage(mHandler.obtainMessage(what, obj));
	}
	
	public static void sendMsg(int what){
		mHandler.sendEmptyMessage(what);
	}
}
