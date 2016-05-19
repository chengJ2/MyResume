package com.me.resume.service;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.me.resume.utils.RegexUtil;

import android.content.Context;
import android.widget.TextView;

/**
 * 定位到当前位置
 * @author Administrator
 *
 */
public class LocationService {

	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private String mData ;
	private TextView textView;
	
	public LocationService(Context context,TextView textView) {
		this.textView = textView;
		mLocationClient = new LocationClient(context);
		mLocationClient.registerLocationListener(myListener);
		setLocationOption();
		mLocationClient.start();
	}
	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy); // 设置定位模式，高精度
		option.setOpenGps(true); // 打开gps
		option.setAddrType("all");// 设置返回具体地址
		option.setScanSpan(900); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		if (true) {
			option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
		} 
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}
	

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// // 网络定位结果
				sb.append(location.getCity());
			}
			if(RegexUtil.checkNotNull(sb.toString())){
				logMsg(sb.toString());
			}else{
				logMsg("武汉市");
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	/**
	 * 显示字符串
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			mData = str;
			if(textView != null){
				textView.setText(mData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
