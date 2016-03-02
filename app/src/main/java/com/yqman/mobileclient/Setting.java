package com.yqman.mobileclient;


import com.yqman.service.NameUser;
import com.yqman.service.WiFiSupportService;
import com.yqman.service.WifiService;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Setting extends Activity {
	private static  final String TAG="Setting";
	private Button btn_set;
	private EditText eit_temperature;
	private EditText eit_moist;
	private EditText eit_sun;
	private EditText eit_ipaddress;
	private EditText eit_ipport;
	private ImageView imv_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, TAG+"-->onCreate");
		setContentView(R.layout.setting);
		initView();
		initEvent();
		initdata();

	}


	private void initdata() {
		// TODO Auto-generated method stub
		Log.d(TAG, "initdata()");
		WiFiSupportService.userInfoPref = this.getSharedPreferences(NameUser.name_store.GlobalSharedName, MODE_PRIVATE);
		WiFiSupportService.userEditor = WiFiSupportService.userInfoPref.edit();
		String temprature = WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_temprature, "");
		String moist =WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_moist, "");
		String sun = WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_sun, "");
		String ipaddress = WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_ipaddress, "");
		String ipport = WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_ipport, "");
		if((!temprature.equals(""))&&(!moist.equals(""))&&(!sun.equals(""))&&(!ipaddress.equals(""))&&(!ipport.equals("")))
		{

			eit_temperature.setText(temprature);
			eit_moist.setText(moist);
			eit_sun.setText(sun);
			eit_ipaddress.setText(ipaddress);
			eit_ipport.setText(ipport);
		}

	}


	private void initView() {
		// TODO Auto-generated method stub
		Log.d(TAG, "initView()");
		btn_set=(Button)findViewById(R.id.btn_setting_set);
		eit_temperature=(EditText)findViewById(R.id.eit_setting_temperature);
		eit_moist=(EditText)findViewById(R.id.eit_setting_moist);
		eit_sun=(EditText)findViewById(R.id.eit_setting_sun);
		eit_ipaddress=(EditText)findViewById(R.id.eit_setting_ipaddress);
		eit_ipport=(EditText)findViewById(R.id.eit_setting_ipport);
		imv_return = (ImageView )findViewById(R.id.imv_setting_return);


	}
	private void initEvent()
	{
		Log.d(TAG, "initEvent()");
		imv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				savedata();
			}
		});
	}
	private void savedata() {
		Log.d(TAG, "savedata()");
		// TODO Auto-generated method stub
		String temprature = eit_temperature.getText().toString();
		String moist = eit_moist.getText().toString();
		String sun = eit_sun.getText().toString();
		String ipaddress = eit_ipaddress.getText().toString();
		String ipport = eit_ipport.getText().toString();
		if((!temprature.equals(""))&&(!moist.equals(""))&&(!sun.equals(""))&&(!ipaddress.equals(""))&&(!ipport.equals("")))
		{
			WiFiSupportService.userEditor.putString(NameUser.name_store.name_ipaddress, ipaddress);
			WiFiSupportService.userEditor.putString(NameUser.name_store.name_ipport, ipport);
			WiFiSupportService.userEditor.putString(NameUser.name_store.name_temprature, temprature);
			WiFiSupportService.userEditor.putString(NameUser.name_store.name_moist, moist);
			WiFiSupportService.userEditor.putString(NameUser.name_store.name_sun, sun);
			WiFiSupportService.userEditor.commit();
			Toast.makeText(Setting.this,""+"保存数据成功",Toast.LENGTH_SHORT).show();

		}
		else
		{
			Toast.makeText(Setting.this,""+"请输入正确的值",Toast.LENGTH_SHORT).show();

		}
		if(WifiService.IsConnect==NameUser.Connect_status.Connect_OK)
		{
			String str="1/"+temprature+"/"+moist+"/"+sun;
			Intent intent = new Intent();
			intent.putExtra(NameUser.name_intent.MSG, NameUser.Command_Class.sendmessage);
			intent.putExtra(NameUser.name_intent.writemessage, str);
			intent.setClass(Setting.this, WiFiSupportService.class);
			startService(intent);
			Toast.makeText(Setting.this,""+"更新数据库成功",Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onKeyDown()");
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return true;
	}
}
