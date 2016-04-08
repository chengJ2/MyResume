package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.model.ResumeModel;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;

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
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		if(getPreferenceData("startVerytime", 0) == 1){
			ActivityUtils.startActivity(HomeActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",true);
		}
		topText = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		leftLable.setText(CommUtil.getStrValue(HomeActivity.this, R.string.resume_personcenter));
		rightLable = findView(R.id.right_lable);	
		
		gridView = findView(R.id.grid);
		
		xgln_gridview = findView(R.id.xgln_gridview);
		
		makeResume = findView(R.id.go);
		setData();
        setGridView();
	        
	}
	
	/**设置数据*/
    private void setData() {
    	topText.setText(CommUtil.getStrValue(HomeActivity.this, R.string.resume_center));
    	
    	leftLable.setOnClickListener(this);
    	rightLable.setOnClickListener(this);
    	makeResume.setOnClickListener(this);
    	
    	resumeModelList = new ArrayList<ResumeModel>();
    	
    	for (int i = 0; i < 10; i++) {
			
    		ResumeModel item = new ResumeModel();
    		List<String> url = new ArrayList<String>();
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

        commAdapter = new CommonBaseAdapter<ResumeModel>(HomeActivity.this,resumeModelList,R.layout.home_grilview_item) {
			
			@Override
			public void convert(ViewHolder holder, ResumeModel item, int position) {
				// TODO Auto-generated method stub
				holder.setImageResource(R.id.item_imageview,Integer.parseInt(resumeModelList.get(position).getPicUrl().get(0)));
				holder.setText(R.id.item_textview, resumeModelList.get(position).getTitle());
			}
		};  
		gridView.setAdapter(commAdapter);
		
		
		commAdapter2 = new CommonBaseAdapter<String>(HomeActivity.this,xglnList,R.layout.home_xgln_grilview) {

			@Override
			public void convert(ViewHolder holder, String item, int position) {
				// TODO Auto-generated method stub
				holder.setText(R.id.itemName, xglnList.get(position));
				if (position == 1 || position == 5 || position == 6) {
					holder.setTextColor(R.id.itemName, CommUtil.getIntValue(HomeActivity.this, R.color.red));
				}
			}
		};
		
		xgln_gridview.setAdapter(commAdapter2);
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go:
			ActivityUtils.startActivity(HomeActivity.this,MyApplication.PACKAGENAME + ".ui.BaseInfoActivity");
			break;
		case R.id.left_lable:
			
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(HomeActivity.this,MyApplication.PACKAGENAME + ".ui.SettingActivity");
			break;
		default:
			break;
		}
		
	}
}
