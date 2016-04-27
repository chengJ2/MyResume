package com.me.resume.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ImageUtils {

	public static final int CEMERA_WITH_DATA = 3023;
	public static final int CROP_WITH_DATA = 3021;

	
	private ImageUtils() {}

	private static ImageUtils imageUtils;

	public static ImageUtils getInstance() {
		if (imageUtils == null)
			imageUtils = new ImageUtils();
		return imageUtils;
	}
	
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
		photo.compress(CompressFormat.JPEG, 70, baos);
		File file = null;
		BufferedOutputStream bos = null;
		try {
			File dir = new File(Environment.getExternalStorageDirectory(),
					FileUtils.TEMPDIR);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file = File.createTempFile("test", null, dir);
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
	 * 保存图片的bitmap 保存到sdcard
	 * 
	 * @throws Exception
	 * 
	 */
	public static void saveImage(Context context, Bitmap bitmap,String filename)
			throws Exception {
		String filePath = FileUtils.isExistsFilePath(context);
		File file = new File(filePath, filename);
		FileOutputStream fos = null;
		// file.createNewFile();
		try {
			fos = new FileOutputStream(file);
			if (null != fos) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		try {
			fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
        //draw  text  to the bottom  
//        int x = (bitmap.getWidth() - bounds.width())/10*9 ;  
//        int y = (bitmap.getHeight() + bounds.height())/10*9;  
        canvas.drawText(gText, x , y, paint);  
  
        return bitmap;  
    }  
}
