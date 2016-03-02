package com.yqman.mobileclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wbing.drawpicture.DrawPicture;
import com.yqman.service.NameUser;
import com.yqman.service.WiFiSupportService;
import com.yqman.service.WifiService;

public class Detailmessage extends Activity {
	private static final String TAG="Detailmessage";

	private Button  btn_read;
	private TextView txt_shownsun;
	private TextView txt_showntemp;
	private TextView txt_shownmoist;
	private RadioGroup mainragroup;
	private RadioGroup subragroup;
	private RadioGroup historyragroup;
	private RadioButton mainrabutton1;
	private RadioButton mainrabutton2;
	private RadioButton historyrabutton1;
	private RadioButton historyrabutton2;
	private RadioButton subrabutton1;
	private RadioButton subrabutton2;
	private RadioButton subrabutton3;
	private int mainpoint=-1;
	private int subpoint=-1;
	private boolean isHistory=false;
	private int isHistory_num=0;
	private DrawPicture mDrawPicture;
	private SurfaceView surfacedraw=null;
	private boolean isactive=true;
	private Handler mdrawHandler= new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1)
			{
				//进入这个activity初始化时候需要读
				Log.d(TAG, "mdrawHandler---handleMessage(Message msg1) ");
				mDrawPicture.drawfigure();
				readmoredata();
				//从此刻起开启读线程
			}
			if(msg.what==2)
			{
				readdata();
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
						String temperature = intent.getStringExtra(NameUser.name_intent.name_temprature);
						String moisture = intent.getStringExtra(NameUser.name_intent.name_moist);
						String sun = intent.getStringExtra(NameUser.name_intent.name_sun);
//					if(WiFiSupportService.counter%10==8)
//					{						
						txt_shownsun.setText("光强："+sun);
						txt_showntemp.setText("温度："+temperature);
						txt_shownmoist.setText("湿度："+moisture);
//					}

						mDrawPicture.drawPhoto(Integer.parseInt(moisture),
								Integer.parseInt(temperature), Integer.parseInt(sun),isHistory);

						break;
					case NameUser.Msg_Class.WRITE_MESSAGE:
						Log.d(TAG, "case WiFiSupportService.Msg_Class.WRITE_MESSAGE:");

						break;
					case NameUser.Msg_Class.RESULT_MESSAGE:
						Log.d(TAG, "case WiFiSupportService.Msg_Class.RESULT_MESSAGE:");
						break;
					case NameUser.Msg_Class.ALARM_MESSAGE:
						Log.d(TAG, "case WiFiSupportService.Msg_Class.ALARM_MESSAGE:");
					/*
					 * 警报并且更新ui
					 * 数据在prefrence里面
					 */
						freshUI();
//					readmoredata();11
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
		setContentView(R.layout.detailmessage);
		initView();
		initevent();
		initdata();
		initDrawpicture();
		freshUI();
		WiFiSupportService.detailactive=true;


	}
	private void initDrawpicture() {
		Log.d(TAG, "initDrawpicture()---surfacedraw"+surfacedraw);
		mDrawPicture = new DrawPicture(surfacedraw);
	}
	private void test() {
		// TODO Auto-generated method stub
		//测试通知栏的intent获取数据
		Log.d(TAG, "test()");
		String str = getIntent().getStringExtra("notify");
		Log.d(TAG, "test()"+str);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, TAG+"--->onResume()");

		IntentFilter filter = new IntentFilter(WiFiSupportService.BroadcastFromservice);
		registerReceiver(mReceiver, filter);
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d(TAG, "thread--->run()");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mdrawHandler.obtainMessage(1,-1,-1).sendToTarget();
			}
		});
		thread.start();

	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, TAG+"--->onPause()");
		unregisterReceiver(mReceiver);
		isactive=false;

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, TAG+"--->onDestroy()");
		isactive=false;

	}
	private void initdata() {
		// TODO Auto-generated method stub
		Log.d(TAG, "initdata()");
		WiFiSupportService.userInfoPref = this.getSharedPreferences(NameUser.name_store.GlobalSharedName, MODE_PRIVATE);
		WiFiSupportService.userEditor = WiFiSupportService.userInfoPref.edit();
	}
	private void initView() {
		// TODO Auto-generated method stub
		Log.d(TAG, "initView()");
		txt_shownsun=(TextView)findViewById(R.id.txt_detail_showsun);
		txt_showntemp=(TextView)findViewById(R.id.txt_detail_showtemp);
		txt_shownmoist=(TextView)findViewById(R.id.txt_detail_showmoist);
		btn_read=(Button)findViewById(R.id.btn_detail_read);
		mainragroup=(RadioGroup)findViewById(R.id.radiogroup_main);
		subragroup=(RadioGroup)findViewById(R.id.radiogroup_sub);
		historyragroup=(RadioGroup)findViewById(R.id.radiogroup_history);
		mainrabutton1=(RadioButton)findViewById(R.id.rib_Mainradiobutton1);
		mainrabutton2=(RadioButton)findViewById(R.id.rib_Mainradiobutton2);
		historyrabutton1=(RadioButton)findViewById(R.id.rib_historyradiobutton1);
		historyrabutton2=(RadioButton)findViewById(R.id.rib_historyradiobutton2);
		subrabutton1=(RadioButton)findViewById(R.id.rib_subradiobutton1);
		subrabutton2=(RadioButton)findViewById(R.id.rib_subradiobutton2);
		subrabutton3=(RadioButton)findViewById(R.id.rib_subradiobutton3);
		surfacedraw = (SurfaceView)findViewById(R.id.wb_surfaceView1);
	}
	private void initevent()
	{
		btn_read.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "btn_read--->onClick()");
				// TODO Auto-generated method stub
				if(!WiFiSupportService.detailactive)
				{

					WiFiSupportService.userEditor.putString(NameUser.name_store.name_alarm_main, ""+mainpoint);
					WiFiSupportService.userEditor.putString(NameUser.name_store.name_alarm_second, ""+subpoint);
					WiFiSupportService.userEditor.commit();
					WiFiSupportService.detailactive=true;
				}


			}
		});
		mainragroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				Log.d(TAG, "mainragroup-->onCheckedChanged()");
				resetreadpoint();
				switch (checkedId)
				{
					case R.id.rib_Mainradiobutton1:
						mainpoint=1;
						break;
					case R.id.rib_Mainradiobutton2:
						mainpoint=2;
						break;
					default:
						break;
				}
			}
		});
		historyragroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				Log.d(TAG, "historyragroup-->onCheckedChanged()");
				resetreadpoint();

				switch (checkedId)
				{
					case R.id.rib_historyradiobutton1:
						//当前状态
						isHistory=false;
//					mainpoint=1;
						break;
					case R.id.rib_historyradiobutton2:
						//历史状态
						isHistory=true;
//					mainpoint=2;
						break;
					default:
						break;
				}
			}
		});
		subragroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				Log.d(TAG, "subragroup-->onCheckedChanged()");
				resetreadpoint();
				// TODO Auto-generated method stub
				switch (checkedId)
				{
					case R.id.rib_subradiobutton1:
						subpoint=1;
						break;
					case R.id.rib_subradiobutton2:
						subpoint=2;
						break;
					case R.id.rib_subradiobutton3:
						subpoint=3;
						break;

					default:
						break;
				}

			}
		});
	}



	private void freshUI() {
		// TODO Auto-generated method stub
		//设置多选选择项
		//发送读取消息
		Log.d(TAG, "freshUI()");

		String mainpoint_str=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_alarm_main, "");
		String secondpoint_str=WiFiSupportService.userInfoPref.getString(NameUser.name_store.name_alarm_second, "");
		Log.d(TAG, "mainpoint="+mainpoint_str+"\nsecondpoint="+secondpoint_str);
		if((!mainpoint_str.equals(""))&&(!secondpoint_str.equals("")))
		{
			mainpoint=Integer.parseInt(mainpoint_str);
			subpoint=Integer.parseInt(secondpoint_str);
			Log.d(TAG, "grounp init().....");
			switch (mainpoint)
			{
				case 1:
					mainragroup.check(R.id.rib_Mainradiobutton1);
					break;
				case 2:
					mainragroup.check(R.id.rib_Mainradiobutton2);
					break;
				default:
					break;
			}
			switch (subpoint)
			{
				case 1:
					subragroup.check(R.id.rib_subradiobutton1);
					break;
				case 2:
					subragroup.check(R.id.rib_subradiobutton2);
					break;
				case 3:
					subragroup.check(R.id.rib_subradiobutton3);
					break;

				default:
					break;
			}
			historyragroup.check(R.id.rib_historyradiobutton1); //默认为当前的状态
		}
		WiFiSupportService.detailactive=true;
	}
	private synchronized void readdata()
	{
		Log.d(TAG, "readdata()");
		if((mainpoint!=-1)&&(subpoint!=-1))
		{
			if(isHistory)
			{
				if(isHistory_num<59)
				{
					//接受历史消息

					Log.i(TAG, "isHistory_num======="+isHistory_num);
					String str="aaaa/"+mainpoint+"/"+subpoint;
					Intent intent = new Intent();
					intent.putExtra(NameUser.name_intent.MSG, NameUser.Command_Class.sendmessage);
					intent.putExtra(NameUser.name_intent.writemessage, str);
					intent.setClass(Detailmessage.this, WiFiSupportService.class);
					startService(intent);
					isHistory_num++;
				}

			}
			else
			{
				String str="0/"+mainpoint+"/"+subpoint;
				Intent intent = new Intent();
				intent.putExtra(NameUser.name_intent.MSG, NameUser.Command_Class.sendmessage);
				intent.putExtra(NameUser.name_intent.writemessage, str);
				intent.setClass(Detailmessage.this, WiFiSupportService.class);
				startService(intent);
			}


		}
	}
	private void readmoredata()
	{
		//一旦点击读取按钮则一直隔一段时间进行读取数据
		Log.d(TAG, "readmoredata()");
		//一旦在运行就不要再开启新的线程了
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while((WifiService.IsConnect==NameUser.Connect_status.Connect_OK)&&isactive)
				{
					//如果当前的主从节点设置有效的话就一直接受数据
					Log.d(TAG, "thread--->run()");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(WiFiSupportService.detailactive)
					{
						//可以读数据
						mdrawHandler.obtainMessage(2,-1,-1).sendToTarget();
					}
				}
			}
		});
		thread.start();
	}
	private void resetreadpoint()
	{
		/*
		 * 一旦监听到radiobutton有变化则停止申请读数据；
		 * 对count值设置0
		 */
		WiFiSupportService.counter=0;//意味着对于以前的数据是不显示的（设了多选项Radiogrounp后会出现这个情况）
		WiFiSupportService.detailactive=false;
		isHistory_num=0;//让其可以显示55个数据
	}



}
