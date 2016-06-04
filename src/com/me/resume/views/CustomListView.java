package com.me.resume.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 控制ListView高度
 * @author Administrator
 *
 */
public class CustomListView extends ListView {

	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomListView(Context context) {
		super(context);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
