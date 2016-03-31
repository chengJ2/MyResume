package com.me.resume.comm;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * 
 * @Description: TODO 适配器的抽象基类  数据为Map
 * @param <K>
 * @param <V>
 */
public abstract class BaseCommonAdapter<K,V> extends BaseAdapter {
	protected LayoutInflater inflater;
	protected Map<K, V> mMap;
	protected Context context;
	protected int LayoutID;
	protected int size;
	/**
	 * 
	 * @param context 上下文
	 * @param mMap	数据集合		
	 * @param LayoutID item布局ID
	 * @param size 数据长度
	 */
	public BaseCommonAdapter(Context context,Map<K, V> mMap,int LayoutID,int size){
		this.context=context;
		this.mMap=mMap;
		this.LayoutID=LayoutID;
		this.size=size;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		
		return size;
	}
	
	@Override
	public Object getItem(int position) {
		
		return position;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
	/**
	 * 
	 * @param size notifyDataSetChanged数据长度
	 */
	public void notifyDataSetChanged(int size) {
		this.size=size;
		super.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=ViewHolder.getViewHolder(context, convertView, position, LayoutID, parent);
		convert(holder, mMap,position);
		return holder.getConvertView();
	}
	/**
	 * 
	 * @param holder
	 * @param map 
	 * @param position
	 */
	public abstract void convert(ViewHolder holder,Map<K,V> map,int position);
}
