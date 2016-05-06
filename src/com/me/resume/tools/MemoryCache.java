package com.me.resume.tools;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * 
 * @ClassName: MemoryCache
 * @Description: 内存缓存类
 * @date 2016/5/6 下午4:55:53
 * 
 */
public class MemoryCache {

	private static final String TAG = "MemoryCache";

	/**
	 * 放入缓存时是个同步操作 LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU
	 * 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换，以提高效率
	 */
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(30, 1.5f, true));

	/**
	 * 缓存中图片所占用的字节，初始为0，将通过此变量严格控制缓存所占用的堆内存
	 */
	private long size = 0;// current allocated size

	/**
	 * 缓存只能占用的最大内存
	 */
	private long limit = 1000000;// max memory in bytes

	public MemoryCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	/**
	 * 设置限制最大使用内存
	 * 
	 * @param newLimit
	 *            最大使用内存
	 */
	public void setLimit(long newLimit) {
		limit = newLimit;
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	/**
	 * 从缓存中获取图片对象
	 * 
	 * @param url
	 *            键名
	 * @return 所缓存的图片
	 */
	public Bitmap get(String url) {
		if (!cache.containsKey(url)) {
			return null;
		}
		return cache.get(url);
	}

	/**
	 * 存放要缓存的图片
	 * 
	 * @param url
	 *            键名
	 * @param bitmap
	 *            待缓存的图片
	 */
	public void put(String url, Bitmap bitmap) {
		if (cache.containsKey(url)) {
			size -= getSizeInBytes(bitmap);
		}
		cache.put(url, bitmap);
		size += getSizeInBytes(bitmap);
		checkSize();
	}

	/**
	 * 图片所占用的内存
	 * 
	 * @param bitmap
	 * @return
	 */
	public long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return 0;
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * 严格控制堆内存，如果超过首先替换最近最少使用的那个图片缓存
	 */
	private void checkSize() {
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, Bitmap>> it = cache.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Bitmap> entry = it.next();
				size -= getSizeInBytes(entry.getValue());
				it.remove();
				if (size <= limit) {
					break;
				}
			}
			Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}

	/**
	 * 清除缓存内容
	 */
	public void clear() {
		cache.clear();
	}
}
