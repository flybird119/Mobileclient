package com.yqman.view;

import com.yqman.mobileclient.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SelectPicPopupWindow extends PopupWindow {
	private static final String TAG="SelectPicPopupWindow";
	private View mMenuView;
	private TextView diconnect = null;
	private TextView myset = null;

	public SelectPicPopupWindow(final Activity context,OnClickListener itemsOnClick) {
		super(context);
		Log.d(TAG, "SelectPicPopupWindow()");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d(TAG, "SelectPicPopupWindow()0.1");
		mMenuView = inflater.inflate(R.layout.homepage_head_set, null);

		Log.d(TAG, "SelectPicPopupWindow()0.2");
		myset = (TextView)mMenuView.findViewById(R.id.txt_home_head_changeset);
		Log.d(TAG, "SelectPicPopupWindow()0.3");
		myset.setOnClickListener(itemsOnClick);

		Log.d(TAG, "SelectPicPopupWindow()0.4");
		diconnect = (TextView)mMenuView.findViewById(R.id.txt_home_head_disconnect);
		Log.d(TAG, "SelectPicPopupWindow()0.5");
		diconnect.setOnClickListener(itemsOnClick);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();

		Log.d(TAG, "SelectPicPopupWindow()1");
		//设置按钮监听
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w/2+50);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.mystyle);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		Log.d(TAG, "SelectPicPopupWindow()2");
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "SelectPicPopupWindow()3");
				int height = mMenuView.findViewById(R.id.layout_head_pop).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}
				return true;
			}
		});
		Log.d(TAG, "SelectPicPopupWindow()4");

	}
}
