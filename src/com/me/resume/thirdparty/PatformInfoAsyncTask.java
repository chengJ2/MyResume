package com.me.resume.thirdparty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.me.resume.R;
import com.me.resume.model.QQInfo;
import com.me.resume.model.SinaInfo;
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.PreferenceUtil;
import com.tencent.connect.UserInfo;
import com.whjz.android.text.Info;
import com.whjz.android.util.common.CommonUtil;
import com.whjz.android.util.common.DataSetList;
import com.whjz.android.util.interfa.BaseCommonUtil;

/**
 * 第三方平台登录 请求
 */
public class PatformInfoAsyncTask extends AsyncTask<String, Integer, Integer> {
	private PreferenceUtil preferenceUtil;
	
	private SinaInfo sina;
	private QQInfo qq;
	private UserInfo user;
	private Context context;
	private String patform;
	private Object obj;
	
	public PatformInfoAsyncTask(Context context,String patform,Object obj){
		this.context = context;
		this.patform = patform;
		this.obj=obj;
		if (preferenceUtil == null) {
			preferenceUtil = new PreferenceUtil(context);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		DialogUtils.showProgress(context, CommUtil.getStrValue(context, R.string.upload_loading));
	}
	
	@Override
	protected Integer doInBackground(String... params) {
			if (!CommUtil.isNetworkAvailable(context)) {// 断网
				return -1;
			} else {
				List<String> paramname = new ArrayList<String>();
				List<String> paramvalue = new ArrayList<String>();
				
				paramname.add("UserName");
				paramname.add("Sex");
				paramname.add("Avatar");
				paramname.add("Address");
				paramname.add("Patform");
				paramname.add("Openid");
				paramname.add("System");
				
				if(patform.equals("qq")){
					qq=(QQInfo)obj;
					paramvalue.add(qq.getUserName());
					paramvalue.add(qq.getSex());
					paramvalue.add(qq.getPhotoPath());
					paramvalue.add(qq.getAddress());
					paramvalue.add("qq");
					paramvalue.add(qq.getOpenId());
//					
				}else if(patform.equals("sina")){
					sina=(SinaInfo) obj;
					paramvalue.add(sina.getName());
					if("1".equals(sina.getGender())){
						paramvalue.add("男");
					}else{
						paramvalue.add("女");
					}
					paramvalue.add(sina.getAvatar_large());
					paramvalue.add("");
					paramvalue.add("sina");
					paramvalue.add(sina.getId());
				}
				paramvalue.add("Android");
				Info info = new Info();
				BaseCommonUtil baseComm=new CommonUtil();
				DataSetList dataSetList = baseComm.selects(info.getUse(),info.getPass(), "getUserLogin",4, paramname,paramvalue);
				if (dataSetList != null) {
					if(dataSetList.nameList.size()>0){
						try {
							Map<String, List<String>> map = dataSetList.getMap();
//							String msg = map.get("msg").get(0);
//							int userID = Utils.parseInt(map.get("userID").get(0));
//							//System.out.println("-----第三方---userID--->"+userID);
//							GlobalApplication.UserID = userID;
							
/*							user=new UserInfo();
							user.setUserName(map.get("userName").get(0));
							user.setUserSex(map.get("sex").get(0));
							user.setUserAddress(map.get("address").get(0));
							user.setUserEmail(map.get("e_mail").get(0));
							user.setUserSignature(map.get("signature").get(0));
							user.setCurrentCredit(map.get("CurrentCredit").get(0));
							user.setIsMark(map.get("IsMark").get(0));
							user.setTotalCredit(map.get("TotalCredit").get(0));
							user.setUserID(map.get("userID").get(0));
							user.setUserLevel(map.get("level").get(0));
							user.setPhotoPath(map.get("avatar").get(0));
							user.setContinousMark(map.get("ContinousMark").get(0));*/
							/*if(msg.equals("success")){
								return 1;
							}else if(msg.equals("fail")){
								return -4;
							}*/
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
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		DialogUtils.dismissDialog();
		L.d("=====result=====" + result);
		/*if (result == -1) {// 未联网
			CommonUtil.ToastMsg(context, context.getResources().getString(R.string.en_network_error));
		} else if (result == -2) {// 读取服务器失败
			CommonUtil.ToastMsg(context, context.getResources().getString(R.string.m_failLoad));
		} else if(result == -3){
			CommonUtil.ToastMsg(context, context.getResources().getString(R.string.m_loginfail));
		}else if (result == 1||result == -4) {
			CommonUtil.ToastMsg(context, context.getResources().getString(R.string.m_loginsuccess));
			saveData();
			notify.notifys(user);
		}*/
	}
	
	/**
	 * 保存数据至本地
	 */
	private void saveData(){
		/*if(patform.equals("qq")){
			GlobalApplication.patform = "qq";
			storeData(Utils.userInfo,qq.getOpenId());
		}else{
			GlobalApplication.patform = "sina";
			storeData(Utils.userInfo,sina.getUid());
		}
		GlobalApplication.UserName = user.getUserName();
		GlobalApplication.photoPath = user.getPhotoPath();
		Utils.getURLImage(handler,Utils.getHttpLink(user.getPhotoPath()),1);
		editor = Utils.userInfo.edit();
		editor.putInt("userId", GlobalApplication.UserID);
		editor.putString("patform", GlobalApplication.patform);
		editor.commit();*/
	}
	
	/**
	 * 存储变量
	 * @param s
	 * @param clear
	 */
	private void storeData(SharedPreferences s,String uis){
		/*editor = s.edit();
		editor.putString("uid", uis);
		editor.putString("username", user.getUserName());
		editor.putString("gender", user.getUserSex());
		editor.putString("headphoto", user.getPhotoPath());
		editor.putString("address", user.getUserAddress());
		editor.putString("email", user.getUserEmail());
		editor.putString("motto", user.getUserSignature());
		editor.putString("level", user.getUserLevel());
		editor.commit();*/
	}
}
