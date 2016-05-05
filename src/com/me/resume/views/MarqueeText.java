package com.me.resume.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 滚动的文本
 * @author Administrator
 * 
 */
public class MarqueeText extends TextView {

	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeText(Context context) {
		super(context);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
	}

}
