package com.me.resume.comm;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 适配器的抽象基类   
 * 数据形式为Map<String,String[]>
 */
public  abstract class CommForMapArrayBaseAdapter extends BaseAdapter {
	protected LayoutInflater inflater;
	protected Map<String,String[]> mList;
	protected Context context;
	protected int LayoutID;
	protected String valueId;
	protected int flag;
	
	private int position;
	private View convertView;
	
	public CommForMapArrayBaseAdapter(Context context,Map<String,String[]> mList,int LayoutID,String valueId){
		this.context=context;
		this.mList=mList;
		this.LayoutID=LayoutID;
		this.valueId = valueId;
		inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		int count = 0;
		try {
			count = mList.get(valueId).length;
			return count;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public String[] getItem(int position) {
		return mList.get(position);
	}
	
	public void setItemList(Map<String,String[]> list) {
		mList = list;
    }
	
	public void clear() {
		mList.clear();
		//page = 1;
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged(int pos) {
		if(position>pos*5){
			convertView.invalidate();
		}
	}
	
	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		this.position=position;
		this.convertView=convertView;
		ViewHolder holder=ViewHolder.getViewHolder(context, convertView, position, LayoutID, parent);
		convert(holder, getItem(position),position);
		return holder.getConvertView();
	}
	public abstract void convert(ViewHolder holder,String[] item,int position);
	
	
}
