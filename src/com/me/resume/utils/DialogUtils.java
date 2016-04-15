package com.me.resume.utils;

import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;

/**
 * 
* @ClassName: DialogUtils 
* @Description: 弹出框类
* @author Comsys-WH1510032 
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
					if (msgwhich == VideoFragment.MSG_RESET) {
						sendMsg(VideoFragment.MSG_RESET);
					}else if(msgwhich == VideoFragment.MSG_WIFI_SETTING){
						sendMsg(VideoFragment.MSG_WIFI_SETTING);
					}
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
		mPopupWindow = new PopupWindow(popView, CommUtil.dip2px(context, 279),
												LayoutParams.WRAP_CONTENT,true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		
		TextView popTitle = (TextView)popView.findViewById(R.id.top_text);
		popTitle.setText(CommUtil.getStrValue(context, resourId));
		
		TextView rightLable = (TextView)popView.findViewById(R.id.right_lable);
		rightLable.setOnClickListener(new OnClickListener() {
			
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
