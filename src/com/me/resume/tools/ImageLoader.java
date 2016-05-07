package com.me.resume.tools;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.me.resume.utils.ImageUtils;

/**
 * 
* @ClassName: ImageLoader 
* @Description:文件下载类
* @date 2016/5/6 下午4:58:05 
*
 */
public class ImageLoader {

	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	private ExecutorService executorService;

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	/**
	 * 
	 * @Title:ImageLoader
	 * @Description: 三阶缓存下载任务
	 * @author Comsys-WH1510032
	 * @param url
	 * @param imageView
	 * @param isLoadOnlyFromCache
	 */
	public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache,boolean round) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			if (round) {
				imageView.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
			}else{
				imageView.setImageBitmap(bitmap);
			}
			
		else if (!isLoadOnlyFromCache){
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView,round);
		}
	}

	private void queuePhoto(String url, ImageView imageView,boolean round) {
		PhotoToLoad p = new PhotoToLoad(url, imageView,round);
		executorService.submit(new PhotosLoader(p));
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		private boolean round;

		public PhotoToLoad(String u, ImageView i,boolean round) {
			this.url = u;
			this.imageView = i;
			this.round = round;
		}
	}

	/**
	 * 
	 * @Title:ImageLoader
	 * @Description: 下载任务
	 * @author Comsys-WH1510032
	 * @param url
	 * @param imageView
	 */
	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = ImageUtils.getBitmap(fileCache,photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * 防止图片错位
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	/**
	 * 
	* @ClassName: BitmapDisplayer 
	* @Description: 用于在UI线程中更新界面
	* @date 2016/5/6 下午5:02:11 
	*
	 */
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null){
				if (photoToLoad.round) {
					photoToLoad.imageView.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
				}else{
					photoToLoad.imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}
	
}