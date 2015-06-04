package com.zhangdong.JiShi.Tools;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.zhangdong.JiShi.R;
import com.zhangdong.JiShi.Bean.MyFavorites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;

import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MyListInFavoritesAdapter extends BaseAdapter {
	private List<MyFavorites> ll = null;
	private Context mContext = null;
	FinalBitmap fbBitmap = FinalBitmap.create(mContext);
	Bitmap bitmap ;
	private int mRightWidth = 0;

	public MyListInFavoritesAdapter(Context context, List<MyFavorites> ll,
			int RightWidth) {
		this.mContext = context;
		this.ll = ll;
		mRightWidth = RightWidth;
		bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.nopic);
	}

	public MyListInFavoritesAdapter() {
		super();
	}

	@Override
	public int getCount() {

		return ll.size();
	}

	@Override
	public Object getItem(int position) {

		return ll.get(position);

	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.myfavorites_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.item_left = (LinearLayout) convertView
					.findViewById(R.id.item_left);
			viewHolder.item_right = (RelativeLayout) convertView
					.findViewById(R.id.item_right);
			viewHolder.ssImageView = (ImageView) convertView
					.findViewById(R.id.iv_vedioimage);

			viewHolder.ssTextView = (TextView) convertView
					.findViewById(R.id.tv_vediotitle);

			viewHolder.nnTextView = (TextView) convertView
					.findViewById(R.id.tv_dname);
			viewHolder.wwTextView = (TextView) convertView
					.findViewById(R.id.tv_dworkplace);
			viewHolder.hhTextView = (TextView) convertView
					.findViewById(R.id.tv_dhospital);

			viewHolder.item_right_txt = (TextView) convertView
					.findViewById(R.id.item_right_txt);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
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

		MyFavorites myFavorites = (MyFavorites) getItem(position);

		
		fbBitmap.display(viewHolder.ssImageView,
				myFavorites.getvPreviewImageURL(), null, bitmap);

		viewHolder.ssTextView.setText(myFavorites.getvTitle());
		viewHolder.nnTextView.setText(myFavorites.getvDoctorName());
		viewHolder.wwTextView.setText(myFavorites.getvDoctorDepartment());
		viewHolder.hhTextView.setText(myFavorites.getvDoctorHospital());

		return convertView;

	}

	private class ViewHolder {
		LinearLayout item_left;
		RelativeLayout item_right;
		TextView ssTextView = null;
		TextView nnTextView = null;
		TextView hhTextView = null;
		TextView wwTextView = null;
		ImageView ssImageView = null;
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
