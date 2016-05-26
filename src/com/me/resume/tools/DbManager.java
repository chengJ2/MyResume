package com.me.resume.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.me.resume.R;
import com.me.resume.comm.Constants;

/**
 * 读取本地DB文件
 * @author Administrator
 *
 */
public class DbManager {

    public static SQLiteDatabase openDatabase(Context context) {  
        try {  
            String databaseFilename = Constants.DATABASE_PATH + "/" + Constants.DATABASE_FILENAME;  
            File dir = new File(Constants.DATABASE_PATH);  
            if (!dir.exists()) {  
                dir.mkdir();  
            }  
            if (!(new File(databaseFilename)).exists()) {  
                InputStream is = context.getResources().openRawResource(R.raw.resume);  
                FileOutputStream fos = new FileOutputStream(databaseFilename);  
                byte[] buffer = new byte[8192];  
                int count = 0;  
                while ((count = is.read(buffer)) > 0) {  
                    fos.write(buffer, 0, count);  
                }  
  
                fos.close();  
                is.close();  
            }  
            return SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
}
