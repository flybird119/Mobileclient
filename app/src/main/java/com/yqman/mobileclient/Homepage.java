package com.yqman.mobileclient;

import com.yqman.service.NameUser;
import com.yqman.service.WiFiSupportService;
import com.yqman.service.WifiService;
import com.yqman.view.Mydialog;
import com.yqman.view.SelectPicPopupWindow;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Homepage extends Activity implements OnClickListener{
	private static final String TAG="Homepage";

	private SelectPicPopupWindow menuWindow = null;
	private ImageView imv_home_set;
	private Button btn_main;
	private TextView mytextview;
	public static Boolean isactive = false;
	private boolean alarmvalue = false;
	private Handler mHandler= new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					color_white();
					break;
				case 2:
					color_red();
					break;
				case 3:
					color_normal();
					break;
				default:
					break;
			}
		}
	};
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

						break;
					case NameUser.Msg_Class.WRITE_MESSAGE:
						Log.d(TAG, "case WiFiSupportService.Msg_Class.WRITE_MESSAGE:");


						break;
					case NameUser.Msg_Class.RESULT_MESSAGE:
						Log.d(TAG, "case WiFiSupportService.Msg_Class.RESULT_MESSAGE:");
						int result = intent.getIntExtra(NameUser.name_intent.conectstatus, -1);
						if(result==NameUser.Connect_status.Connect_OK)
						{
//						Toast.makeText(Homepage.this,""+"连接成功",Toast.LENGTH_SHORT).show();
							color_normal();
						}
						else
						{
//						Toast.makeText(Homepage.this,""+"连接失败",Toast.LENGTH_SHORT).show(); 
							color_unlink();
						}
						break;
					case NameUser.Msg_Class.ALARM_MESSAGE:
						Log.d(TAG, "case WiFiSupportService.Msg_Class.ALARM_MESSAGE:");
					/*
					 * 警报并且更新ui
					 * 数据在prefrence里面
					 */
						String mainpoint_str=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_alarm_main, "");
						String secondpoint_str=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_alarm_second, "");
						String alarm_text="主节点："+mainpoint_str
								+" 从节点："+secondpoint_str+" 出现异常";

						Changecolor(alarm_text);
					default:
						break;
				}
			}

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		Log.d(TAG, TAG+"-->onCreate()");


		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

		setContentView(R.layout.homepage);
//		firsttest();
		initView();
		initevent();
		IntentFilter filter = new IntentFilter(WiFiSupportService.BroadcastFromservice);
		registerReceiver(mReceiver, filter);
		initdata();
		/////////////
		if(WifiService.IsConnect==NameUser.Connect_status.Connect_OK)
		{
			Intent intent=getIntent();
			String getmsg=intent.getStringExtra(NameUser.name_intent.MSG);
			if((getmsg!=null)&&(getmsg.equals(NameUser.name_intent.name_alarm)))
			{

				String mainpoint_str=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_alarm_main, "");
				String secondpoint_str=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_alarm_second, "");
				String alarm_text="主节点："+mainpoint_str
						+" 从节点："+secondpoint_str+" 出现异常";
				Log.d(TAG, "alarm_text="+alarm_text);
				Changecolor(alarm_text);
			}
			else
			{
				color_normal();
			}

		}
		else
		{
			color_unlink();
		}
		/////////////
//		mytextview.setText("异常");
//		mytextview.setTextColor(Color.RED);
//		alarmvalue = true;
//		Changecolor();

	}

	private void firsttest() {
		// TODO Auto-generated method stub
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;   }
	}

	private void initdata() {
		// TODO Auto-generated method stub
		Log.d(TAG, "initdata()");
		WiFiSupportService.userInfoPref = this.getSharedPreferences(NameUser.name_store.GlobalSharedName, MODE_PRIVATE);
		WiFiSupportService.userEditor = WiFiSupportService.userInfoPref.edit();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, TAG+"-->onDestroy()");
		unregisterReceiver(mReceiver);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isactive=false;
		Log.d(TAG, TAG+"-->onPause()");
		//////////////////
//		alarmvalue = false;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isactive=true;
		Log.d(TAG, TAG+"-->onResume()");
//		if(WifiService.IsConnect==NameUser.Connect_status.Connect_OK)
//		{
//			Intent intent=getIntent();
//			String getmsg=intent.getStringExtra(NameUser.name_intent.MSG);
//			if((getmsg!=null)&&(getmsg.equals(NameUser.name_intent.name_alarm)))
//			{
//				
//				String mainpoint_str=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_alarm_main, "");
//				String secondpoint_str=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_alarm_second, "");					
//				String alarm_text="主节点："+mainpoint_str
//						+" 从节点："+secondpoint_str+" 出现异常";
//				Log.d(TAG, "alarm_text="+alarm_text);			
//				Changecolor(alarm_text);
//			}
//			else
//			{
//				color_normal();
//			}
//			
//		}
//		else
//		{
//			color_unlink();
//		}

	}
	private void initView() {
		// TODO Auto-generated method stub
		Log.d(TAG, "initView()");
		imv_home_set=(ImageView)findViewById(R.id.imv_home_head_set);

		Log.d(TAG, "initView()1");
		btn_main=(Button)findViewById(R.id.btn_home_shown);
		Log.d(TAG, "initView()2");

		mytextview=(TextView)findViewById(R.id.txt_homepage);

	}
	private void initevent()
	{
		Log.d(TAG, "initevent()");
		imv_home_set.setOnClickListener(this);
		btn_main.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onClick()");
		switch (v.getId()) {
			case R.id.imv_home_head_set:
				setLoadImage();
				break;
			case R.id.btn_home_shown:
				WiFiSupportService.playMsgmp3_pause();
				if(WifiService.IsConnect==NameUser.Connect_status.Connect_OK)
				{
					go2detail();
				}
				else
				{
//				dialogshow();	
					dialogtest();
				}

				break;
			default:
				break;
		}
	}
	private void dialogtest()
	{
		Mydialog dialog = new  Mydialog(Homepage.this,new Mydialog.ICustomDialogEventListener() {
			@Override
			public void customDialogEvent(int id) {
				switch (id) {
					case 1:
						//连接
						Startconnect();
						break;
					case 2:
						//设置
						go2setting();
						break;

					default:
						break;
				}
			}
		});
		dialog.show();

	}
	private void dialogshow() {
		// TODO Auto-generated method stub
		// 创建退出对话框  
		AlertDialog isExit = new AlertDialog.Builder(this).create();
		// 设置对话框标题
		isExit.setTitle("系统提示:");
		// 设置对话框消息
		isExit.setMessage("确定使用设置里面的ip和端口吗？");

		// 添加选择按钮并注册监听
		isExit.setButton("确定", listener);
		isExit.setButton2("去设置", listener);

		// 显示对话框
		isExit.show();
	}
	/**监听对话框里面的button点击事件*/
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{  	Intent intent=new Intent();
			switch (which)
			{

				case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
					Startconnect();
					break;
				case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
					go2setting();
					break;
				default:
					break;
			}
		}
	};
	private void Startconnect() {
		// TODO Auto-generated method stub
		Log.d(TAG, "Startconnect()");
		if(WiFiSupportService.Wificonnect_status==NameUser.Connect_status.Connect_false)
		{
			String ip_address=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_ipaddress, "");
			String ip_port_str=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_ipport, "");
			if((!ip_address.equals(""))&&(!ip_port_str.equals("")))
			{
				int ip_port=Integer.parseInt(ip_port_str);
				Intent intent = new Intent();
				intent.putExtra(NameUser.name_intent.MSG, NameUser.Command_Class.apply_Connect);
				intent.putExtra(NameUser.name_intent.name_ipaddress, ip_address);
				intent.putExtra(NameUser.name_intent.name_ipport, ip_port);
				intent.setClass(Homepage.this, WiFiSupportService.class);
				startService(intent);
			}
			else
			{
				Toast.makeText( Homepage.this,""+"当前ip地址或串口不可用",Toast.LENGTH_SHORT).show();

			}

		}

	}
	private void go2detail() {
		// TODO Auto-generated method stub
		Log.d(TAG, "go2detail()");
		alarmvalue = false;

		String temprature = WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_temprature, "");
		String moist =WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_moist, "");
		String sun = WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_sun, "");
		String str="1/"+temprature+"/"+moist+"/"+sun;
		Intent intent = new Intent();
		intent.putExtra(NameUser.name_intent.MSG, NameUser.Command_Class.sendmessage);
		intent.putExtra(NameUser.name_intent.writemessage, str);
		intent.setClass(Homepage.this, WiFiSupportService.class);
		startService(intent);
		Log.d(TAG, "go2detail()---finish send setting.");

		Intent mntent=new Intent();
		mntent.setClass(Homepage.this, Detailmessage.class);
		Homepage.this.startActivity(mntent);
	}
	public void setLoadImage(){
		Log.d(TAG, "setLoadImage()");
		menuWindow = new SelectPicPopupWindow(Homepage.this, new itemsOnClick());
		Log.d(TAG, "setLoadImage()1");
		menuWindow.showAtLocation(Homepage.this.findViewById(R.id.imv_home_head_set),
				Gravity.TOP|Gravity.RIGHT, 0, 150); //设置layout在PopupWindow中显示的位置
	}
	//为弹出窗口实现监听类
	class  itemsOnClick implements View.OnClickListener {
		public void onClick(View v) {
			Log.d(TAG, "itemsOnClick->onClick()");
			switch (v.getId()) {
				case R.id.txt_home_head_changeset:
					go2setting();
					break;
				case R.id.txt_home_head_disconnect:
					Cancelconnect();
					break;
				default:
					break;
			}
		}
	}
	private void go2setting() {
		// TODO Auto-generated method stub
		Log.d(TAG, "go2setting()");
		Intent intent=new Intent();
		intent.setClass(Homepage.this, Setting.class);
		Homepage.this.startActivity(intent);
	}
	private void Cancelconnect() {
		Log.d(TAG, "Cancelconnect()");
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(NameUser.name_intent.MSG, NameUser.Command_Class.Disconnect);
		intent.setClass(Homepage.this, WiFiSupportService.class);
		startService(intent);
	}
	private void Changecolor(String alarm_text)
	{
		mytextview.setText(alarm_text);
		mytextview.setTextColor(Color.RED);
		alarmvalue = true;
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int count=1;
				while(alarmvalue)
				{
					//如果当前的主从节点设置有效的话就一直接受数据
					Log.d(TAG, "thread--->run()");
					try {
						Thread.sleep(500);
						if(count==1)
						{
							mHandler.obtainMessage(2,-1,-1).sendToTarget();
							count=2;
						}
						else
						{
							mHandler.obtainMessage(1,-1,-1).sendToTarget();
							count=1;
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mHandler.obtainMessage(3,-1,-1).sendToTarget();


			}
		});
		thread.start();
	}
	private void color_unlink()
	{
		btn_main.setTextColor(Color.WHITE);
		btn_main.setText("未连接");
		btn_main.setBackgroundResource(R.drawable.buttonselectorroll);
		mytextview.setText("");
	}
	private void color_normal()
	{
		btn_main.setTextColor(Color.WHITE);
		btn_main.setText("OK");
		btn_main.setBackgroundResource(R.drawable.buttonselectorroll);
		mytextview.setText("");
	}
	private void color_white()
	{

		btn_main.setTextColor(Color.RED);
		btn_main.setText("error...");
		btn_main.setBackgroundResource(R.drawable.buttonselectorroll_white);

	}
	private void color_red()
	{
		btn_main.setTextColor(Color.WHITE);
		btn_main.setText("error...");
		btn_main.setBackgroundResource(R.drawable.buttonselectorroll_red);
	}

}
