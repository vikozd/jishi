package com.zhangdong.JiShi.Tools;



import com.zhangdong.JiShi.R;

import android.content.Context;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.zhangdong.JiShi.R;
import com.zhangdong.JiShi.R.color;
import com.zhangdong.JiShi.Bean.BodyPart;

import android.R.layout;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridViewAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	
	private int clickTemp=-1;
	List<BodyPart> ll;
	Context context;
	public MyGridViewAdapter(Context context,List<BodyPart> ll){
		super();
		this.ll=ll;
		this.context=context;

		mInflater=LayoutInflater.from(context);
		
	}
	
	public void setSeclection(int position) {
		clickTemp = position;
	}
	
	@Override
	public int getCount() {
		return ll.size();

	}

	@Override
	public Object getItem(int arg0) {

		return ll.get(arg0);

	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		

		if(convertView==null){
			convertView=mInflater.inflate(R.layout.gridview_item, null);

			viewHolder=new ViewHolder();
			viewHolder.zzTextView=(TextView) convertView
					.findViewById(R.id.tv_zhengzhuang);
			viewHolder.zzImageView=(ImageView) convertView
					.findViewById(R.id.imageView1);
			
			

			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		BodyPart bpBodyPart= (BodyPart) getItem(position);
		viewHolder.zzTextView.setText(bpBodyPart.getvcName());
		if(bpBodyPart.getvcImageURL().equals("")){
			viewHolder.zzImageView.setBackgroundResource(R.drawable.jiqiren);
			
		}else{
			FinalBitmap fbBitmap=FinalBitmap.create(context);
			fbBitmap.display(viewHolder.zzImageView, bpBodyPart.getvcImageURL());
		}
		
		if (clickTemp == position) {
			convertView.setBackgroundResource(R.color.light_gray);
			} else {
			convertView.setBackgroundColor(Color.WHITE);
			}
		

			
		return convertView;
	}
	
	private class ViewHolder{
		TextView zzTextView=null;
		ImageView zzImageView=null;
	}

}
