package com.me.resume.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.me.resume.R;

public class CustomFAB extends ImageButton {

	private Context ctx;
	private int bgColor; 
	private int bgColorPressed; 

	
	public CustomFAB(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.ctx = context;
		init(attrs);
	}

	public CustomFAB(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ctx = context;
		init(attrs);
	}

	public CustomFAB(Context context) {
		super(context);
		this.ctx = context;
	}
	
	private void init(AttributeSet attrSet) {
	    Resources.Theme theme = ctx.getTheme();
	    TypedArray arr = theme.obtainStyledAttributes(attrSet, R.styleable.FAB, 0, 0);
	    try {
	        setBgColor(arr.getColor(R.styleable.FAB_bg_color, Color.RED));
	        setBgColorPressed(arr.getColor(R.styleable.FAB_bg_color_pressed, Color.BLUE));
	        StateListDrawable sld = new StateListDrawable();
	        sld.addState(new int[] {android.R.attr.state_pressed}, createButton(bgColorPressed));
	        sld.addState(new int[] {}, createButton(bgColor));
	        setBackground(sld);
	    }
	    catch(Throwable t) {}
	    finally {
	         arr.recycle();
	    }
	}
	
	
	private Drawable createButton(int color) {
	    OvalShape oShape = new OvalShape();
	    ShapeDrawable sd = new ShapeDrawable(oShape);
	    setWillNotDraw(false);
	    sd.getPaint().setColor(color);
	    OvalShape oShape1 = new OvalShape();
	    ShapeDrawable sd1 = new ShapeDrawable(oShape);
	    sd1.setShaderFactory(new ShapeDrawable.ShaderFactory() {
	        @Override
	        public Shader resize(int width, int height) {
	            LinearGradient lg = new LinearGradient(0,0,0, height,
	            new int[] {
	                Color.TRANSPARENT,
	                Color.TRANSPARENT,
	                Color.TRANSPARENT,
	                Color.TRANSPARENT
	            }, null, Shader.TileMode.REPEAT);
	            return lg;
	        }
	    });
	    LayerDrawable ld = new LayerDrawable(new Drawable[] { sd1, sd });
	    ld.setLayerInset(0, 5, 5, 0, 0);
	    ld.setLayerInset(1, 0, 0, 5, 5);
	    return ld;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public int getBgColorPressed() {
		return bgColorPressed;
	}

	public void setBgColorPressed(int bgColorPressed) {
		this.bgColorPressed = bgColorPressed;
	}

}
