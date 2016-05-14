package com.me.resume.comm;

import java.util.List;
import java.util.Map;

import com.me.resume.tools.L;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 适配器的抽象基类 数据形式为List
 * 
 * @author Administrator
 * 
 * @param <T>
 */
public abstract class CommForMapBaseAdapter extends BaseAdapter {
	protected LayoutInflater inflater;
	protected Map<String, List<String>> mList;
	protected Context context;
	protected int LayoutID;
	protected String keyId;
	protected int flag;

	private int position;
	private View convertView;

	public CommForMapBaseAdapter(Context context,
			Map<String, List<String>> mList, int LayoutID, String keyId) {
		this.context = context;
		this.mList = mList;
		this.LayoutID = LayoutID;
		this.keyId = keyId;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		int count = 0;
		try {
			count = mList.get(keyId).size();
			return count;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public List<String> getItem(int position) {
		return mList.get(keyId);
	}

	// public void setItemList(Map<String,List<String>> list) {
	// mList = list;
	// }

	// public void clear() {
	// mList.clear();
	// //page = 1;
	// notifyDataSetChanged();
	// }
	//
	public void notifyDataSetChanged(int pos) {
		if (position > pos * 3) {
			convertView.invalidate();
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		this.position = position;
		this.convertView = convertView;
		ViewHolder holder = ViewHolder.getViewHolder(context, convertView,
				position, LayoutID, parent);
		convert(holder, getItem(position), position);
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, List<String> item,
			int position);

}
