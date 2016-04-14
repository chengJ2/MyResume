package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.MainActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.model.ResumeModel;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: HomeActivity 
* @Description: 首页
* @author Comsys-WH1510032 
* @date 2016/3/29 下午4:56:41 
*
 */
public class HomeActivity extends SwipeBackActivity implements OnClickListener{

	private List<ResumeModel> resumeModelList;
	private  RelativeLayout itmel;
	 private CommonBaseAdapter<ResumeModel> commAdapter = null;
	private GridView gridView;
    
	private TextView topText;
    private TextView leftLable,rightLable;
    
    private Button makeResume;
    
    private CommonBaseAdapter<String> commAdapter2 = null;
    private List<String> xglnList = null;
    private GridView xgln_gridview;
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case 1:
//				queryWhere = "select * from " + CommonText.BASEINFO + " where userId = 1";
//				commMapArray = dbUtil.queryData(self, queryWhere);
				String gotoStr = ".ui.BaseInfoActivity";
//				if (map!= null && map.get("userId").length > 0) {
//					gotoStr = ".ui.WorkExperienceActivity";
//				}else{
					gotoStr = ".ui.BaseInfoActivity";
//				}
				ActivityUtils.startActivity(self,MyApplication.PACKAGENAME + gotoStr);
				break;
			default:
				break;
			}
    	};
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		if(getPreferenceData("startVerytime", 0) == 1){
			ActivityUtils.startActivity(self, MyApplication.PACKAGENAME 
					+ ".MainActivity",true);
		}
		topText = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		leftLable.setText(CommUtil.getStrValue(self, R.string.resume_personcenter));
		rightLable = findView(R.id.right_lable);	
		
		gridView = findView(R.id.grid);
		
		xgln_gridview = findView(R.id.xgln_gridview);
		
		makeResume = findView(R.id.go);
		setData();
        setGridView();
        
//        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//		keyID = TelephonyMgr.getDeviceId();
	        
	}
	
	/**设置数据*/
    private void setData() {
    	topText.setText(CommUtil.getStrValue(self, R.string.resume_center));
    	
    	leftLable.setOnClickListener(this);
    	rightLable.setOnClickListener(this);
    	makeResume.setOnClickListener(this);
    	
    	resumeModelList = new ArrayList<ResumeModel>();
    	
    	for (int i = 0; i < 10; i++) {
			
    		ResumeModel item = new ResumeModel();
    		ArrayList<String> url = new ArrayList<String>();
    		url.add(R.drawable.a001+"");
    		url.add(R.drawable.a002+"");
    		url.add(R.drawable.a003+"");
    		url.add(R.drawable.a004+"");
    		
    		item.setTitle("简历模板"+i);
    		item.setDesc("普通求职个人基本简历模板 ");
    		item.setPicUrl(url);
    		item.setDatetime("2015-06-" + i);
    		resumeModelList.add(item);
		}
        
        
        xglnList = new ArrayList<String>();
        xglnList.add("求职简历");
        xglnList.add("面试问题");
        xglnList.add("自我鉴定");
        xglnList.add("面试技巧");
        xglnList.add("求职简历");
        xglnList.add("面试问题");
        xglnList.add("自我鉴定");
        xglnList.add("面试技巧");
        xglnList.add("简历注意事项");
        xglnList.add("笔试经验");
        xglnList.add("自我鉴定");
        xglnList.add("面试技巧");
    }
    
    /**设置GirdView参数，绑定数据*/
    private void setGridView() {
        int size = resumeModelList.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        commAdapter = new CommonBaseAdapter<ResumeModel>(self,resumeModelList,R.layout.home_grilview_item) {
			
			@Override
			public void convert(ViewHolder holder, ResumeModel item, int position) {
				// TODO Auto-generated method stub
				holder.setImageResource(R.id.item_imageview,Integer.parseInt(resumeModelList.get(position).getPicUrl().get(0)));
				holder.setText(R.id.item_textview, resumeModelList.get(position).getTitle());
			}
		};  
		gridView.setAdapter(commAdapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(self, ImagePagerActivity.class);
			    // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
			    intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, resumeModelList.get(position).getPicUrl());
			    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
			    startActivity(intent);
			}
		});
		
		
		commAdapter2 = new CommonBaseAdapter<String>(self,xglnList,R.layout.home_xgln_grilview) {

			@Override
			public void convert(ViewHolder holder, String item, int position) {
				// TODO Auto-generated method stub
				holder.setText(R.id.itemName, xglnList.get(position));
				if (position == 1 || position == 5 || position == 6) {
					holder.setTextColor(R.id.itemName, CommUtil.getIntValue(self, R.color.red));
				}
			}
		};
		
		xgln_gridview.setAdapter(commAdapter2);
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go:
			if(MyApplication.userId == 0){
				DialogUtils.showAlertDialog(self, 
						CommUtil.getStrValue(self, R.string.dialog_action_alert), mHandler);
			}else{
				mHandler.sendEmptyMessage(1);
			}
			break;
		case R.id.left_lable:
			// TODO
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(self,MyApplication.PACKAGENAME + ".ui.SettingActivity");
			break;
		default:
			break;
		}
		
	}
}
