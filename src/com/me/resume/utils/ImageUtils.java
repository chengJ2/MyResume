package com.me.resume.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.me.resume.tools.FileCache;

/**
 * 图片处理类
 * @author Administrator
 *
 */
public class ImageUtils {

	public static final int IMAGE_SELECT = 1; // 调用系统图库
	public static final int CEMERA_WITH_DATA = 3023; // 调用系统相机
	public static final int CROP_WITH_DATA = 3021; // 裁剪

	private ImageUtils() {}

	/**
	 * 
	 * @Title:ImageUtils
	 * @Description: 裁剪图片到指定大小
	 * @author Comsys-WH1510032
	 * @param act
	 * @param data
	 * @param width
	 * @param height
	 */
	public static void doCropPhoto(Activity act, Intent data, int width,
			int height) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		Bitmap photo = data.getParcelableExtra("data");
		if (photo != null) {
			intent.setType("image/*");
			intent.putExtra("data", photo);
		} else {
			intent.setDataAndType(data.getData(), "image/*");
		}
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		act.startActivityForResult(intent, CROP_WITH_DATA);
	}

	public static File getPhotoFile(Intent data) {
		Bitmap photo = data.getParcelableExtra("data");
		if (photo == null) {
			Uri uri = data.getData();
			photo = BitmapFactory.decodeFile(uri.getPath());
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		photo.compress(CompressFormat.JPEG, 100, baos);
		File file = null;
		BufferedOutputStream bos = null;
		try {
			File dir = new File(FileUtils.TEMPDIR);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file = File.createTempFile("temp", null, dir);
			bos = new BufferedOutputStream(new FileOutputStream(file));
			baos.writeTo(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 
	 * @Title:ImageUtils
	 * @Description:保存图片的bitmap 保存到sdcard
	 * @param context
	 * @param bitmap
	 * @param filename
	 */
	public static void saveImage(Context context, Bitmap bitmap,String filename){
		String filePath = FileUtils.isExistsFilePath(context);
		File file = new File(filePath, filename);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			if (null != fos) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static void getURLImage(final Handler handler,
			final String imageUrl, final int what) {
		new Thread() {
			public void run() {
				Bitmap bitmap = ImageUtils.getURLBitmap(imageUrl);
				Message msg = new Message();
				msg.what = what;
				msg.obj = bitmap;
				handler.sendMessage(msg);
			}
		}.start();
	}

	public static Bitmap getURLBitmap(String imageUrl) {
		HttpGet httpRequest = new HttpGet(imageUrl);
		HttpClient httpclient = new DefaultHttpClient();
		Bitmap bitmap = null;
		try {
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// System.out.println("SC_OK:"+httpResponse.getStatusLine().getStatusCode()
			// );
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得相关信息 取得HttpEntiy
				HttpEntity httpEntity = httpResponse.getEntity();
				// 获得一个输入流
				InputStream is = httpEntity.getContent();
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
			}

		} catch (FileNotFoundException err) {
			err.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 请求网络图片
	 * 
	 * @param url
	 *            图片url
	 * @return Bitmap
	 */
	public static Bitmap loadImageFromUrl(String url) {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		HttpResponse response;
		try {
			response = client.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("PicShow", "Request URL failed, error code ="
						+ statusCode);
			}

			HttpEntity entity = response.getEntity();
			if (entity == null) {
				Log.e("PicShow", "HttpEntity is null");
			}
			InputStream is = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				is = entity.getContent();
				byte[] buf = new byte[1024];
				int readBytes = -1;
				while ((readBytes = is.read(buf)) != -1) {
					baos.write(buf, 0, readBytes);
				}
			} finally {
				if (baos != null) {
					baos.close();
				}
				if (is != null) {
					is.close();
				}
			}
			byte[] imageArray = baos.toByteArray();
			return BitmapFactory.decodeByteArray(imageArray, 0,
					imageArray.length);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			if (new File(url).exists()) {
				fis = new FileInputStream(url);
				bitmap = BitmapFactory.decodeStream(fis);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
	
	/** 
     * 添加文字到图片，类似水印文字。 
     * @param gContext 
     * @param gResId 
     * @param gText 
     * @return 
     */  
    public static Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {  
        Resources resources = gContext.getResources();  
        float scale = resources.getDisplayMetrics().density;  
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);  
  
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();  
        // set default bitmap config if none  
        if (bitmapConfig == null) {  
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;  
        }  
        // resource bitmaps are imutable,  
        // so we need to convert it to mutable one  
        bitmap = bitmap.copy(bitmapConfig, true);  
  
        Canvas canvas = new Canvas(bitmap);  
        // new antialised Paint  
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        // text color - #3D3D3D  
        paint.setColor(Color.rgb(61,61,61));  
        // text size in pixels  
        paint.setTextSize((int) (10 * scale*5));  
        // text shadow  
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);  
  
        // draw text to the Canvas center  
        Rect bounds = new Rect();  
        paint.getTextBounds(gText, 0, gText.length(), bounds);  
        int x = (bitmap.getWidth() - bounds.width()) / 2;  
        int y = (bitmap.getHeight() + bounds.height()) / 2;  
        canvas.drawText(gText, x , y, paint);  
        return bitmap;  
    }  
    
 // decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
 	public static Bitmap decodeFile(File f) {
 		FileInputStream fis = null;
 		try {
 			// decode image size
 			BitmapFactory.Options o = new BitmapFactory.Options();
 			o.inJustDecodeBounds = true;
 			fis = new FileInputStream(f);
 			BitmapFactory.decodeStream(fis, null, o);
 			fis.close();
 			// Find the correct scale value. It should be the power of 2.
 			final int REQUIRED_SIZE = 100;
 			int width_tmp = o.outWidth, height_tmp = o.outHeight;
 			int scale = 1;
 			while (true) {
 				if (width_tmp / 2 < REQUIRED_SIZE
 						|| height_tmp / 2 < REQUIRED_SIZE)
 					break;
 				width_tmp /= 2;
 				height_tmp /= 2;
 				scale *= 2;
 			}

 			// decode with inSampleSize
 			BitmapFactory.Options o2 = new BitmapFactory.Options();
 			o2.inSampleSize = scale;
 			fis = new FileInputStream(f);
 			return BitmapFactory.decodeStream(fis, null, o2);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}finally{
 			try {
 				fis.close();
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
 		}
 		return null;
 	}
 	
 	/**
 	 * 
 	 * @Title:ImageUtils
 	 * @Description: 下载文件转bitmap
 	 * @author Comsys-WH1510032
 	 * @param fileCache
 	 * @param url
 	 * @return Bitmap
 	 */
 	public static Bitmap getBitmap(FileCache fileCache,String url) {
		File f = fileCache.getFile(url);
		// 先从文件缓存中查找是否有
		Bitmap b = null;
		if (f != null && f.exists()){
			b = ImageUtils.decodeFile(f);
		}
		if (b != null){
			return b;
		}
		// 最后从指定的url中下载图片
		try {
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			b = ImageUtils.decodeFile(f);
			return b;
		} catch (Exception ex) {
			Log.e("", "getBitmap catch Exception...\nmessage = " + ex.getMessage());
			return null;
		}
	}
 	
 	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
