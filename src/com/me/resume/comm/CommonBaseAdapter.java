package com.me.resume.comm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * 适配器的抽象基类   数据形式为List
 * @author Administrator
 * @param <T>
 */
public  abstract class CommonBaseAdapter<T> extends BaseAdapter implements Filterable{
	protected LayoutInflater inflater;
	protected List<T> mList;
	protected Context context;
	protected int LayoutID;
	
	private CommFilter mNameFilter;
//	private List<String> mArrayList;
	protected List<T> mFilteredArrayList;
	
	public CommonBaseAdapter(Context context,List<T> mList,int LayoutID){
		this.context=context;
		this.mList=mList;
		this.LayoutID=LayoutID;
		inflater=LayoutInflater.from(context);
		mFilteredArrayList = new ArrayList<T>();
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
	
	@Override
	public Filter getFilter() {
		if (mNameFilter == null) {
			mNameFilter = new CommFilter();
		}
		return mNameFilter;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=ViewHolder.getViewHolder(context, convertView, position, LayoutID, parent);
		convert(holder, getItem(position),position);
		return holder.getConvertView();
	}
	
	public abstract void convert(ViewHolder holder,T item,int position);
	
	/**
	 * 
	* @ClassName: CommFilter 
	* @Description: 过滤数据 
	* @author Comsys-WH1510032 
	* @date 2016/4/18 下午1:23:16 
	*
	 */
	private class CommFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			for (Iterator<T> iterator = mList.iterator(); iterator.hasNext();) {
		        T name = iterator.next();
		        if (((List<T>) name).contains(constraint)) {
		        	mList.add(name);
		        }
		      }
		      filterResults.values = mList;
		      return filterResults;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
				mList = (List<T>) results.values;
		      if (results.count > 0) {
		        notifyDataSetChanged();
		      } else {
		        notifyDataSetInvalidated();
		      }
		}
		
	};
	
	public static List<String> getarray(Activity activity, int r_array_id){
		String[] arraytexts = activity.getResources().getStringArray(r_array_id);
		final List<String> list = new ArrayList<String>();
		for (int i = 0; i < arraytexts.length; i++) {
			list.add(arraytexts[i]);
		}
		return list;
		
	}
	
}
