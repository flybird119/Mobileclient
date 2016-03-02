package com.yqman.view;

import com.yqman.mobileclient.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class Mydialog extends Dialog  implements View.OnClickListener{

	Context mContext;

	public interface ICustomDialogEventListener {
		public void customDialogEvent(int id);
	}
	private ICustomDialogEventListener mCustomDialogEventListener;
	public Mydialog(Context context,ICustomDialogEventListener listener) {
		super(context,R.style.dialog);
		mContext = context;
		mCustomDialogEventListener = listener;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_dialog, null);
		bindImageClickEvent(layout);
		this.setContentView(layout);
	}
	private void bindImageClickEvent(View layout){
		Button btn_ok = (Button)layout.findViewById(R.id.btn_dialog_ok);
		Button btn_setting = (Button)layout.findViewById(R.id.btn_dialog_set);
		btn_ok.setOnClickListener(this);
		btn_setting.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		int drawableID = -1;
		switch (id){
			case R.id.btn_dialog_ok:
				drawableID = 1;
				break;
			case R.id.btn_dialog_set:
				drawableID = 2;
				break;
		}
		if (drawableID != -1) {
			mCustomDialogEventListener.customDialogEvent(drawableID);
		}
		dismiss();
	}

}
