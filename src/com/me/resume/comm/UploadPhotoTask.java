package com.me.resume.comm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.ui.Base64Util;
import com.me.resume.utils.CommUtil;
import com.whjz.android.text.Info;
import com.whjz.android.util.common.CommonUtil;
import com.whjz.android.util.common.DataSetList;
import com.whjz.android.util.common.MyLog;
import com.whjz.android.util.interfa.BaseCommonUtil;

/**
 * 用户上传头像
 * @author ChengJian
 *
 */
public class UploadPhotoTask extends AsyncTask<String, Integer, Integer>{

	private Context context;
	private ProgressDialog pdialog;
	private DataSetList dataSetList = null;
	private List<String> paramname = null;
	private List<String> paramvalue = null;
	
	private Handler mHandler;
	private int style;
	
	protected BaseCommonUtil baseCommon = new CommonUtil();;// 通用工具对象实例
	
	public UploadPhotoTask(Context context,Handler mHandler,int style){
		this.context = context;
		this.mHandler = mHandler;
		this.style = style;
	}
	
	@Override
	protected Integer doInBackground(String... arg0) {
		if(!CommUtil.isNetworkAvailable(context)){
			return -1;
		}else if(arg0[0]!=null){
			paramname = new ArrayList<String>();
			paramvalue = new ArrayList<String>();
			MyLog.d("photoPath:"+arg0[0].toString()+"---USERID--->"+MyApplication.userId +"-->"+Base64Util.getPath()+" style:"+style);
			if (style == 4) { // 修改
				paramname.add("file");
				paramname.add("puser_pic");
				paramname.add("puser_id");
				
				paramvalue.add(Base64Util.getbyteString(context, arg0[0].toString()));
				paramvalue.add(Base64Util.getPath());
				paramvalue.add("3");
			}
			
			Info info = new Info();
			dataSetList = baseCommon.datasetlistUpdata(info.getUse(),info.getPass(), "procdetailed",style, paramname,paramvalue,null);
			if (dataSetList != null) {
				if(dataSetList.nameList.size()>0){
					try {
						Map<String, List<String>> map = dataSetList.getMap();
						int userID =  CommUtil.parseInt(map.get("userId").get(0));
						if(userID>0){
//							GlobalApplication.getInstance().setUserId(userID);
//							GlobalApplication.getInstance().setUserAvatar(map.get("user_pic").get(0));
							return 1;
						}else{
							return -3;
						}
					} catch (Exception e) {
						e.printStackTrace();
						return -3;
					}
				}else{
					return -2;//读取服务器失败
				}
			} else {
				return -2;//读取服务器失败
			}
		}
		return 0;
	}
	
	protected void onPreExecute() {
		super.onPreExecute();
		pdialog = new ProgressDialog(context);
		pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  //圆形进度条
		pdialog.setMessage("正在提交，请稍后...");
		pdialog.setIndeterminate(false);
		pdialog.setCancelable(false);
		pdialog.show();
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		pdialog.dismiss();
		if (result == -1) {// 未联网
//			Utils.ToastMsg(context, context.getResources().getString(R.string.check_network));
		} else if (result == -2) {// 读取服务器失败
//			Utils.ToastMsg(context, context.getResources().getString(R.string.m_failLoad));
		} else if(result == -3){
//			Utils.ToastMsg(context, context.getResources().getString(R.string.file_failLoad));
		}else if (result == 1) {
//			Utils.ToastMsg(context, context.getResources().getString(R.string.file_successLoad));
//			if(GlobalApplication.userhead.exists()){
//				GlobalApplication.userhead.delete();
//        	}
			mHandler.sendEmptyMessage(1);
		}
	}
	
}
