package com.me.resume.thirdparty;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class QQTokenKeeper {
	private static final String PREFERENCES_NAME  = "tencent_qq_token";
    private static final String KEY_OPENID        = "openid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";
    
    /**
     * 保存 Token 对象到 SharedPreferences。
     * 
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context, TencentQQToken token) {
        if (null == context || null == token) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_OPENID, token.getOpenid());
        editor.putString(KEY_ACCESS_TOKEN, token.getAccess_token());
        String expires_in = token.getExpires_in();
        editor.putLong(KEY_EXPIRES_IN, System.currentTimeMillis() + Long.parseLong(expires_in) * 1000);
        editor.commit();
    }
    
    /**
     * 从 SharedPreferences 读取 TencentQQToken 信息。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 返回 TencentQQToken 对象
     */
    public static TencentQQToken readAccessToken(Context context) {
        if (null == context) {
            return null;
        }
        TencentQQToken token = new TencentQQToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        token.setOpenid(pref.getString(KEY_OPENID, ""));
        token.setAccess_token(pref.getString(KEY_ACCESS_TOKEN, ""));
        long expires_in = (pref.getLong(KEY_EXPIRES_IN, -1) - System.currentTimeMillis())/1000; 
        token.setExpires_in(Long.toString(expires_in));
        return token;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     * 
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context) {
        if (null == context) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
