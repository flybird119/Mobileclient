package com.yqman.mobileclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.yqman.service.NameUser;
import com.yqman.service.WiFiSupportService;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG="MainActivity";
	private Button btn_ipport_btn=null;
	private Button btn_cancelconnect=null;
	private Button btn_sendmsg=null;
	private EditText eit_send=null;
	private TextView txt_viewdata=null;
	private EditText eit_setip=null;
	private EditText eit_setport=null;
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
			Log.d(TAG, "onReceive()");
	        String action = intent.getAction();
	        if (WiFiSupportService.BroadcastFromservice.equals(action)) 
	        {
	        	//相关操作
	        	int msg = intent.getIntExtra("MSG", -1);
	        	switch (msg) {
				case NameUser.Msg_Class.READ_MESSAGE:
					Log.d(TAG, "case WiFiSupportService.Msg_Class.READ_MESSAGE:");
					String temperature = intent.getStringExtra("temperature");
					String moisture = intent.getStringExtra("moisture");
					txt_viewdata.setText("温度="+temperature+"\n湿度="+moisture);					
					break;
				case NameUser.Msg_Class.WRITE_MESSAGE:
					Log.d(TAG, "case WiFiSupportService.Msg_Class.WRITE_MESSAGE:");
					String writemessage = intent.getStringExtra("message");
					txt_viewdata.setText("写入消息为："+writemessage);
					
					break;
				case NameUser.Msg_Class.RESULT_MESSAGE:
					Log.d(TAG, "case WiFiSupportService.Msg_Class.RESULT_MESSAGE:");
					int result = intent.getIntExtra(NameUser.name_intent.conectstatus, -1);
					if(result==NameUser.Connect_status.Connect_OK)
					{
						txt_viewdata.setText("连接成功");
					}
					else
					{
						txt_viewdata.setText("连接失败");
					}
					break;
					

				default:
					break;
				}
	        }
	        
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, TAG+"--->onCreate()");
		setContentView(R.layout.activity_main);
		initView();
		initEvent();
		IntentFilter filter = new IntentFilter(WiFiSupportService.BroadcastFromservice);
		registerReceiver(mReceiver, filter); 
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(TAG, TAG+"--->onStart()");
		super.onStart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, TAG+"--->onResume()");
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, TAG+"--->onPause()");
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, TAG+"--->onDestroy()");
		super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onClick()");
		switch (v.getId()) {
		case R.id.btn_setip_port:
			Log.d(TAG, "case R.id.setip_port");
			Startconnect();
			break;

		case R.id.btn_cancelconnect:
			Log.d(TAG, "case R.id.cancelconnect");
			Cancelconnect();
			break;
		case R.id.btn_sendmessage:
			Log.d(TAG, "case R.id.btn_sendmessage");
			writemessage();
			break;
		default:
			break;
		}
	}
	
	private void writemessage() {
		// TODO Auto-generated method stub
		String str=eit_send.getText().toString();
		if(!str.equals(""))
		{
			Intent intent = new Intent();
			intent.putExtra(NameUser.name_intent.MSG, NameUser.Command_Class.sendmessage);
			intent.putExtra(NameUser.name_intent.writemessage, str+"\n");
			intent.setClass(MainActivity.this, WiFiSupportService.class);
			startService(intent);
		}
		
	}
	private void Cancelconnect() {
		Log.d(TAG, "Cancelconnect()");
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(NameUser.name_intent.MSG, NameUser.Command_Class.Disconnect);
		intent.setClass(MainActivity.this, WiFiSupportService.class);
		startService(intent);
	}
	private void Startconnect() {
		// TODO Auto-generated method stub
		Log.d(TAG, "Startconnect()");
		if(WiFiSupportService.Wificonnect_status==NameUser.Connect_status.Connect_false)
		{
			String ip_address=eit_setip.getText().toString();
			String ip_port_str=eit_setport.getText().toString();
			int ip_port=Integer.parseInt(ip_port_str);
			if(!ip_address.equals(""))
			{
				Intent intent = new Intent();
				intent.putExtra("MSG", NameUser.Command_Class.apply_Connect);
				intent.putExtra("ip_address", ip_address);
				intent.putExtra("ip_port", ip_port);
				intent.setClass(MainActivity.this, WiFiSupportService.class);
				startService(intent);
			}
			
		}
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		Log.d(TAG, "initView()");
		btn_ipport_btn=(Button)findViewById(R.id.btn_setip_port);
		btn_cancelconnect=(Button)findViewById(R.id.btn_cancelconnect);
		btn_sendmsg=(Button)findViewById(R.id.btn_sendmessage);
		txt_viewdata=(TextView)findViewById(R.id.txt_showdata);
		eit_send=(EditText)findViewById(R.id.eit_send);
		eit_setip=(EditText)findViewById(R.id.eit_changeip);
		eit_setport=(EditText)findViewById(R.id.eit_changeport);
		eit_setip.setText("192.168.1.100");
		eit_setport.setText("9998");
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		Log.d(TAG, "initEvent()");
		btn_ipport_btn.setOnClickListener(this);
		btn_cancelconnect.setOnClickListener(this);
		btn_sendmsg.setOnClickListener(this);
	}

}
