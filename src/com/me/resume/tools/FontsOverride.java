package com.me.resume.tools;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Typeface;

/**
 * 
 * @ClassName: FontsOverride 
 * @Description: 更改字体样式 
 * @date 2016/5/6 下午5:17:15 
 *
 */
public class FontsOverride {

	public static void setDefaultFont(Context context,
            String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }
 
    protected static void replaceFont(String staticTypefaceFieldName,
            final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
