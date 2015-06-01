package com.zhangdong.JiShi.Tools;


import java.util.List;

import com.zhangdong.JiShi.R;
import com.zhangdong.util.Keywords;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyListInSearchAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<Keywords> mTitleList = null;
	
	public MyListInSearchAdapter(Context context,List<Keywords> mTitleList){
		super();
		this.mTitleList=mTitleList;
		mInflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		
		return mTitleList.size();
	}
	
	public void setDate(List<Keywords> mTitleList) {
		this.mTitleList=mTitleList;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mTitleList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.listview_insearch_item, null);
			viewHolder=new ViewHolder();
			viewHolder.ssTextView=(TextView)convertView
					.findViewById(R.id.tv_listviewItem);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.ssTextView.setText(mTitleList.get(position).getKeyword());
		return convertView;
	}
	
	private class ViewHolder{
		TextView ssTextView=null;
		
	}

}
