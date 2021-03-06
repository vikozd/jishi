package com.zhangdong.JiShi.Tools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
/**
 * @blog http://blog.csdn.net/xiaanming
 * 
 * @author xiaanming
 *
 */
public class MyScrollView extends ScrollView {
	private OnScrollListener onScrollListener;
	
	public MyScrollView(Context context) {
		this(context, null);
	}
	
	public MyScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	/**
	 * ���ù����ӿ�
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}
	

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(onScrollListener != null){
			onScrollListener.onScroll(t);
		}
	}





	/**
	 * 
	 * �����Ļص��ӿ�
	 * 
	 * @author xiaanming
	 *
	 */
	public interface OnScrollListener{
		/**
		 * �ص������� ����MyScrollView������Y�������
		 * @param scrollY
		 * 				��
		 */
		public void onScroll(int scrollY);
	}
	
	

}
