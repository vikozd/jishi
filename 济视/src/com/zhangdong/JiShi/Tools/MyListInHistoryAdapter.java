package com.zhangdong.JiShi.Tools;


import java.util.List;



import com.zhangdong.JiShi.R;
import com.zhangdong.JiShi.VideoMainActivity;
import com.zhangdong.JiShi.Bean.MyHistory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class MyListInHistoryAdapter extends BaseAdapter{
	private List<MyHistory> ll=null;
	private Context mContext = null;
	
	private int mRightWidth = 0;
	
	public MyListInHistoryAdapter(Context context,List<MyHistory> ll,int RightWidth){
		super();
		this.mContext=context;
		this.ll=ll;
		mRightWidth=RightWidth;
		
	}
	
	public MyListInHistoryAdapter(){
		super();
		
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.history_item, parent,false);

			viewHolder=new ViewHolder();
			viewHolder.item_left = (LinearLayout) convertView
					.findViewById(R.id.item_left);
			viewHolder.item_right = (RelativeLayout) convertView
					.findViewById(R.id.item_right);
			viewHolder.zzTextView=(TextView) convertView
					.findViewById(R.id.tv_vediotitle);
			viewHolder.zzButton=(Button) convertView
					.findViewById(R.id.bnt_play);
			viewHolder.item_right_txt = (TextView) convertView
					.findViewById(R.id.item_right_txt);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		LinearLayout.LayoutParams lp1 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		viewHolder.item_left.setLayoutParams(lp1);
		LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
				LayoutParams.MATCH_PARENT);
		viewHolder.item_right.setLayoutParams(lp2);
		viewHolder.item_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightItemClick(v, position);
				}
			}
		});
//		viewHolder.zzButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent it=new Intent();
//				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//				it.setClass(mContext, VideoMainActivity.class);
//				it.putExtra("videotitle", viewHolder.zzTextView.getText().toString().trim());
//				v.getContext().startActivity(it); 
//			}
//		});

		
		MyHistory MyHistory= (MyHistory) getItem(position);
		viewHolder.zzTextView.setText(MyHistory.getvTitle());
		
		return convertView;
	}
	
	private class ViewHolder{
		LinearLayout item_left;
		RelativeLayout item_right;
		TextView zzTextView=null;
		Button zzButton=null;
		TextView item_right_txt;
	}
	
	/**
	 * 单击事件监听器
	 */
	private onRightItemClickListener mListener = null;

	public void setOnRightItemClickListener(onRightItemClickListener listener) {
		mListener = listener;
	}

	public interface onRightItemClickListener {
		void onRightItemClick(View v, int position);
	}

}
