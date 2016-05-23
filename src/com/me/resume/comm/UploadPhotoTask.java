package com.me.resume.comm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.utils.Base64Util;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.ImageUtils;
import com.me.resume.utils.PreferenceUtil;
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
	private Handler mHandler;
	private Bitmap bitmap = null;
	private PreferenceUtil preferenceUtil;
	
	public UploadPhotoTask(Context context,Handler mHandler){
		this.context = context;
		this.mHandler = mHandler;
		if (preferenceUtil == null) {
			preferenceUtil = new PreferenceUtil(context);
		}
		
	}
	
	@Override
	protected Integer doInBackground(String... arg0) {
		if(!CommUtil.isNetworkAvailable(context)){
			return -1;
		}else if(arg0[0]!=null){
			List<String> paramname = new ArrayList<String>();
			List<String> paramvalue = new ArrayList<String>();
			MyLog.d("photoPath:"+arg0[0].toString()+"---USERID--->"
					+ MyApplication.USERID +"-->"+Base64Util.getPath());
			
			paramname.add("file");
			paramname.add("p_avator");
			paramname.add("p_userId");
			
			paramvalue.add(Base64Util.getbyteString(context, arg0[0].toString()));
			paramvalue.add(Base64Util.getPath());
			paramvalue.add(BaseActivity.uTokenId);
			
			Info info = new Info();
			BaseCommonUtil baseCommon = new CommonUtil();
			DataSetList dataSetList = baseCommon.datasetlistUpdata(info.getUse(),info.getPass(), "pro_user_avator",1, paramname,paramvalue,null);
			if (dataSetList != null) {
				if(dataSetList.nameList.size()>0){
					try {
						Map<String, List<String>> map = dataSetList.getMap();
						String userID = map.get("userId").get(0);
						if(userID != null && !"0".equals(userID)){
							String user_avator = map.get(UserInfoCode.AVATOR).get(0);
							preferenceUtil.setPreferenceData(UserInfoCode.AVATOR, user_avator);
							
							// 删除已存在的头像
							FileUtils.deleteFile(new File(MyApplication.USERAVATORPATH));
							bitmap = ImageUtils.getURLBitmap(CommUtil.getHttpLink(user_avator));
							
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
		DialogUtils.showProgress(context, CommUtil.getStrValue(context, R.string.upload_loading));
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		DialogUtils.dismissDialog();
		if (result == -1) {// 未联网
			CommUtil.ToastMsg(context, R.string.check_network);
		} else if (result == -2) {// 读取服务器失败
			CommUtil.ToastMsg(context, R.string.m_failLoad);
		} else if(result == -3){
			CommUtil.ToastMsg(context, R.string.file_failLoad);
		}else if (result == 1) {
			CommUtil.ToastMsg(context, R.string.file_successLoad);
			if (bitmap != null) {
				mHandler.sendMessage(mHandler.obtainMessage(2, bitmap));
			}
		}
	}
	
}
