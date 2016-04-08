package com.me.resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.views.JazzyViewPager;
import com.me.resume.views.JazzyViewPager.TransitionEffect;
import com.me.resume.views.TagFlowLayout;
import com.whjz.android.listview.CustomListView;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: MainActivity 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午2:13:45 
*
 */
public class MainActivity extends SwipeBackActivity {

	private MainActivity _context;
	private JazzyViewPager jazzyViewPager;
	
	private LayoutInflater mInflater;
    private View view1, view2, view3, view4, view5;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
	
	private static final int MSG_CHANGE_PHOTO = 1;
	/** view自动切换时间 */
	private static final int VIEW_CHANGE_TIME = 10000;
	
	private boolean showEffect = true;
	
	private ListView weListview;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_CHANGE_PHOTO:
				int index = jazzyViewPager.getCurrentItem();
				if (index == mViewList.size() - 1) {
					index = -1;
				}
				jazzyViewPager.setCurrentItem(index + 1);
				mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
						VIEW_CHANGE_TIME);
				break;
			case 2:
				break;
			}
		}

	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_context = MainActivity.this;
		findViews();
		initViews();
		showViews();
	}
	

	private void findViews(){
		jazzyViewPager = findView(R.id.index_product_container);
	}
	
	private TagFlowLayout tagFlowLayout;
	private String mTags[] = {  
	            "welcome","android","TextView",  
	            "apple","jamy","kobe bryant",  
	            "jordan","layout","viewgroup",  
	            "margin","padding","text",  
	            "name","type","search","logcat"  
	    };
	
	
	private TextView nameTextView;
	private Button goHomeBtn;
	
	private void initViews(){
		mInflater = LayoutInflater.from(this);
        view1 = mInflater.inflate(R.layout.index_resume_1, null);
        
        
        
        view2 = mInflater.inflate(R.layout.index_resume_2, null);
        weListview = (ListView)view2.findViewById(R.id.weListview);
        
        view3 = mInflater.inflate(R.layout.index_resume_3, null);
        view4 = mInflater.inflate(R.layout.index_resume_4, null);
        view5 = mInflater.inflate(R.layout.index_resume_5, null);
        
        //添加页卡视图
        mViewList.add(view1);
        
        String where = "select * from " + CommonText.WORKEXPERIENCE + " where userId = 1";
        final Map<String, String[]> map = dbUtil.queryData(MainActivity.this, where);
        if (map!= null && map.get("userId").length > 0) {
        	mViewList.add(view2);
        	int LayoutID = R.layout.index_2_list_item;
        	int we_show_nav = getPreferenceData("we_show_nav",1);
        	if(we_show_nav == 1){
        		LayoutID = R.layout.index_2_list_item;
			}else if(we_show_nav == 2){
				LayoutID = R.layout.index_2_list2_item;
			}else if(we_show_nav == 3){
				
			}
        	CommForMapArrayBaseAdapter commMapAdapter = new CommForMapArrayBaseAdapter(MainActivity.this,
        			map,LayoutID,"userId") {
				
				@Override
				public void convert(ViewHolder holder, String[] item, int position) {
					// TODO Auto-generated method stub
					holder.setText(R.id.item1, map.get("companyname")[position]);
					holder.setText(R.id.item2, map.get("worktimeStart")[position] + " 至 " + map.get("worktimeEnd")[position]);
				}
			};
        	
        	weListview.setAdapter(commMapAdapter);
		}
        
        mViewList.add(view3);
        mViewList.add(view4);
        mViewList.add(view5);
        
        nameTextView = ((TextView)view1.findViewById(R.id.name));
        tagFlowLayout = (TagFlowLayout)view3.findViewById(R.id.flowlayout);
        
        goHomeBtn = (Button)view5.findViewById(R.id.go);
	}
	
	private void showViews() {
		if(showEffect){
			jazzyViewPager.setTransitionEffect(TransitionEffect.Tablet);
		}
		
		jazzyViewPager.setCurrentItem(0);
		
		if(getPreferenceData("autoShow", 0) == 1){
			mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, VIEW_CHANGE_TIME);
		}
		
		jazzyViewPager.setAdapter(new MyPagerAdapter(mViewList));
		
		nameTextView.setText(CommUtil.getStrValue(_context, R.string.info_name) 
        		+ " :"+ getPreferenceData("info_realname",""));
        
        MarginLayoutParams lp = new MarginLayoutParams(  
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
        lp.leftMargin = 5;  
        lp.rightMargin = 5;  
        lp.topMargin = 5;  
        lp.bottomMargin = 5;  
        for(int i = 0; i < mTags.length; i ++){  
            TextView view = new TextView(this);  
            view.setText(mTags[i]);  
            view.setTextColor(CommUtil.getIntValue(_context, R.color.white));  
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
            tagFlowLayout.addView(view,lp);  
        }  
        
        goHomeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setPreferenceData("firstInstall",0);
				ActivityUtils.startActivity(_context,MyApplication.PACKAGENAME + ".ui.HomeActivity");
			}
		});
		
	}
	
	//ViewPager适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return mTitleList.get(position);//页卡标题
        	return null;
        }

    }

}
