package com.me.resume.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.me.resume.utils.L;

public class HackyViewPager extends ViewPager {

	private static final String TAG = "HackyViewPager";

	  public HackyViewPager(Context context) {
	    super(context);
	  }

	  public HackyViewPager(Context context, AttributeSet attrs) {
	    super(context, attrs);
	  }

	  @Override
	  public boolean onInterceptTouchEvent(MotionEvent ev) {
	    try {
	      return super.onInterceptTouchEvent(ev);
	    } catch (IllegalArgumentException e) {
	      // 不理会
	      L.e(TAG, "hacky viewpager error1");
	      return false;
	    } catch (ArrayIndexOutOfBoundsException e) {
	      // 不理会
	      L.e(TAG, "hacky viewpager error2");
	      return false;
	    }
	  }
}