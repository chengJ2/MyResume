package com.me.resume.comm;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.resume.R;
import com.whjz.android.util.common.MyLog;

public class ProfessionListAdapter extends BaseExpandableListAdapter {

	private Context context;
	
	private ArrayList<Map<String, String>> groupList = null;   
    private ArrayList<ArrayList<String>> childList = null;  
    
	public ProfessionListAdapter(Context context,ArrayList<Map<String, String>> groupList,ArrayList<ArrayList<String>> childList){
		this.context=context;
		this.groupList=groupList;
		this.childList=childList;
	}
	
	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.childList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.expandlist_group_item, null);
		}
		ImageView nav = (ImageView)convertView.findViewById(R.id.group_nav);
		if (!isExpanded) {
			nav.setImageResource(R.drawable.icon_arrow_down);
		}else{
			nav.setImageResource(R.drawable.icon_arrow_up);
		}
		TextView drug_name=(TextView) convertView.findViewById(R.id.group_item);
		drug_name.setText(groupList.get(groupPosition).get("pname"));
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.expandlist_children_item, null);
		}
		MyLog.d("groupPosition:"+groupPosition + "-childPosition-->"+childPosition+"--name-->"+childList.get(groupPosition).get(childPosition).toString());
		TextView txtView=(TextView) convertView.findViewById(R.id.child_item);
		txtView.setText(childList.get(groupPosition).get(childPosition).toString());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
