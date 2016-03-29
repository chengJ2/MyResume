package com.me.resume;

import java.util.ArrayList;
import java.util.List;

import com.me.resume.utils.ActivityUtils;
import com.me.resume.views.JazzyViewPager;
import com.me.resume.views.JazzyViewPager.TransitionEffect;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * 
* @ClassName: MainActivity 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Comsys-WH1510032 
* @date 2016/3/29 下午2:13:45 
*
 */
public class MainActivity extends BaseActivity {

	private MainActivity _context;
	private JazzyViewPager jazzyViewPager;
	
	private LayoutInflater mInflater;
    private View view1, view2, view3, view4, view5;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
	
	private static final int MSG_CHANGE_PHOTO = 1;
	/** view自动切换时间 */
	private static final int VIEW_CHANGE_TIME = 5000;
	
	private boolean showEffect = true;
	
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
	private void initViews(){
		mInflater = LayoutInflater.from(this);
        view1 = mInflater.inflate(R.layout.index_resume_1, null);
        view2 = mInflater.inflate(R.layout.index_resume_2, null);
        view3 = mInflater.inflate(R.layout.index_resume_3, null);
        view4 = mInflater.inflate(R.layout.index_resume_4, null);
        view5 = mInflater.inflate(R.layout.index_resume_5, null);
        
        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);
        mViewList.add(view5);
        
        
        
        ((Button)view5.findViewById(R.id.go)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setPreferenceData("firstInstall",0);
				ActivityUtils.startActivity(_context,MyApplication.PACKAGENAME + ".ui.HomeActivity");
			}
		});
	}
	
	private void showViews() {
		if(showEffect){
			jazzyViewPager.setTransitionEffect(TransitionEffect.Tablet);
		}
		
		jazzyViewPager.setCurrentItem(0);
		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, VIEW_CHANGE_TIME);
		
		jazzyViewPager.setAdapter(new MyPagerAdapter(mViewList));
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
