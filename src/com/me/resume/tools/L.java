package com.me.resume.tools;

import android.util.Log;

import com.me.resume.BuildConfig;

/**
 * 
* @ClassName: L 
* @Description: Logcat日志输出
* @author Comsys-WH1510032 
* @date 2016/5/6 下午5:43:52 
*
 */
public class L {
    static String className;
    static String methodName;
    static int lineNumber;
    public static boolean DEBUG = true;

    private L() {
        /* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    public static void on() {
        DEBUG = true;
    }

    public static void off() {
        DEBUG = false;
    }

    private static String createLog(String log) {

        return "[" + methodName + ":" + lineNumber + "]" + log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable() && DEBUG)
            return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
//        FileUtils.writeLogToFile("=="+className+"==", message);
    }

    public static void e(Throwable e) {
        if (!isDebuggable() && DEBUG)
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, methodName + ":" + lineNumber, e);
    }

    public static void e(String tag, String message) {
        if (!isDebuggable() && DEBUG)
            return;
        Log.e(tag, message);
    }

    public static void e(String tag, String message, Throwable e) {
        if (!isDebuggable() && DEBUG)
            return;
        Log.e(tag, message, e);
    }

    public static void i(String message) {
        if (!isDebuggable() && DEBUG)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void i(String tag, String message) {
        if (!isDebuggable() && DEBUG)
            return;
        Log.i(tag, message);
    }

    public static void d(String message) {
        if (!isDebuggable() && DEBUG)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void d(String tag, String message) {
        if (!isDebuggable() && DEBUG)
            return;
        Log.d(tag, message);
    }

    public static void v(String message) {
        if (!isDebuggable() && DEBUG)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void v(String tag, String message) {
        if (!isDebuggable() && DEBUG)
            return;
        Log.v(tag, message);
    }

    public static void w(String message) {
        if (!isDebuggable() && DEBUG)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void w(Throwable e) {
        if (!isDebuggable() && DEBUG)
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, methodName + ":" + lineNumber, e);
    }

    public static void w(String tag, String message) {
        if (!isDebuggable() && DEBUG)
            return;
        Log.w(tag, message);
    }

    public static void w(String tag, String message, Throwable e) {
        if (!isDebuggable() && DEBUG)
            return;
        Log.w(tag, message, e);
    }

    public static void wtf(String message) {
        if (!isDebuggable() && DEBUG)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }
    
    public static void print(String message) {
        if (!isDebuggable() && DEBUG)
            return;

        getMethodNames(new Throwable().getStackTrace());
        System.out.println(createLog(message));
    }
}
