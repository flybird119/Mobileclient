package com.yqman.mobileclient;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class Startui extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startui);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0)
		{
			finish();
			return;
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(Startui.this, Homepage.class);
				Startui.this.startActivity(intent);
				Startui.this.finish();
			}

		}, 2000);
	}
}
