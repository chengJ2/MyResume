package com.me.resume.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.os.Looper;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.TimeUtils;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
   
	MyApplication application;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * @param application 上下文
     * @brief 构造函数
     * @details 获取系统默认的UncaughtException处理器，设置该CrashHandler为程序的默认处理器 。
     */
    public CrashHandler(MyApplication application) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.application = application;
    }

    /**
     * The thread is being terminated by an uncaught exception. Further
     * exceptions thrown in this method are prevent the remainder of the
     * method from executing, but are otherwise ignored.
     *
     * @param thread the thread that has an uncaught exception
     * @param ex     the exception that was thrown
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 如果用户没有处理则让系统默认的异常处理器来处理
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // 等待会后结束程序
            try {
                Thread.sleep(3000);
                android.os.Process.killProcess(android.os.Process.myPid());
            } catch (InterruptedException e) {
                L.e(e);
            }
        }
    }

    /**
     * @param ex 异常
     * @return true：如果处理了该异常信息；否则返回false。
     * @brief 自定义错误处理，收集错误信息
     * @details 发送错误报告等操作均在此完成
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        L.e(ex);
        // 提示错误消息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                CommUtil.ToastMsg(application, R.string.crash);
                Looper.loop();
            }
        }.start();
        // 保存错误报告文件
        saveCrashInfoToFile(ex);
        return true;
    }

    /**
     * @param ex 异常
     * @brief 保存错误信息到文件中
     */
    private void saveCrashInfoToFile(Throwable ex) {
        final StackTraceElement[] stack = ex.getStackTrace();
        final String message = ex.getMessage();
        /* 准备错误日志文件 */
        String filePath = Environment.getExternalStorageDirectory()  
                .getAbsolutePath() + File.separator + Constants.DIR_PATH + File.separator 
                + FileUtils.LOG_PATH + FileUtils.LOG_NAME;
        File logFile = new File(filePath);
        if(!logFile.getParentFile().exists()) {
        	logFile.getParentFile().mkdirs();
    	 }
        try {
			logFile.createNewFile();
		} catch (IOException e) {
			L.e(e);
		}
        /* 写入错误日志 */
        FileWriter fw = null;
        final String lineFeed = "\r\n";
        try {
            fw = new FileWriter(logFile, true);
            fw.write(TimeUtils.getCurrentTimeInString() + lineFeed
                    + lineFeed);
            fw.write(message + lineFeed);
            for (StackTraceElement aStack : stack) {
                fw.write(aStack.toString() + lineFeed);
            }
            final Throwable cause = ex.getCause();
            if (cause != null) {
                final StackTraceElement[] cStack = cause.getStackTrace();
                final String cMessage = cause.getMessage();
                fw.write(cMessage + lineFeed);
                for (StackTraceElement aStack : cStack) {
                    fw.write(aStack.toString() + lineFeed);
                }
            }
            fw.write(lineFeed);
            fw.flush();
        } catch (IOException e) {
            L.e(e);
        } finally {
            try {
                if (null != fw)
                    fw.close();
            } catch (IOException e) {
                L.e(e);
            }
        }
    }
}
