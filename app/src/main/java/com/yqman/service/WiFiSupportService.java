package com.yqman.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.yqman.mobileclient.Detailmessage;
import com.yqman.mobileclient.Homepage;
import com.yqman.mobileclient.R;
import com.yqman.mobileclient.Setting;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Switch;

@SuppressLint("NewApi")
public class WiFiSupportService extends Service {
	private static final String TAG="WiFiSupportService";


	public static final String BroadcastFromservice = "com.yqman.mobileclient.broadcast";

	public static  SharedPreferences userInfoPref = null;
	public static  Editor userEditor = null;
	private static MediaPlayer mPlayer = null;
	public static boolean  detailactive=false;
	public static int counter=0;
	public static final int counter_num=51;
	public static int[] moiValue=new int[counter_num];
	public static int[] temValue=new int[counter_num];
	public static int[] lightValue=new int[counter_num];
	private String main_s2h=null;
	private String second_s2h=null;
	private static boolean alarmsoundisstart=false;
	private Handler ser2supser = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "handleMessage()");
			// TODO Auto-generated method stub
			switch (msg.what)
			{
				case NameUser.Msg_Class.READ_MESSAGE:
					Log.d(TAG, "case Msg_Class.READ_MESSAGE");
					byte[] readBuf = (byte[]) msg.obj;
					try {
						String readMessage = new String(readBuf, 0, msg.arg1,"GB2312");
						Log.d(TAG, "readMessage="+readMessage);
						int place1 = readMessage.indexOf("/");
						String tempMessage = readMessage.substring(place1+1);
						Log.d(TAG, "tempMessage="+tempMessage);
						int place2_tmp = tempMessage.indexOf("/")+place1+1;
						int place2 = readMessage.lastIndexOf("/");
						Log.d(TAG, "place1="+place1);
						Log.d(TAG, "place2_tmp="+place2_tmp);
						Log.d(TAG, "place2="+place2);

						if((place1>0)&&(readMessage.length()>(place2+1))&&(place2>place1)&&(place2_tmp==place2))
						{
							//(place2_tmp==place2)用于判断收到信息是否是正常的数据
							Log.d(TAG, "Message is true");
							if(readMessage.substring(0, place1).equals("9999"))
							{
								//该消息为警报
								//后面两个数字分别为主节点和从节点数
								Log.d(TAG, "Message is 警报");
								//播放音乐
								playMsgmp3_start();
								main_s2h = readMessage.substring(place1+1,place2);
								second_s2h = readMessage.substring(place2+1);
								userInfoPref=getSharedPreferences(NameUser.name_store.GlobalSharedName,MODE_PRIVATE);
								WiFiSupportService.userEditor = WiFiSupportService.userInfoPref.edit();
								WiFiSupportService.userEditor.putString(NameUser.name_store.name_alarm_main, main_s2h);
								WiFiSupportService.userEditor.putString(NameUser.name_store.name_alarm_second, second_s2h);
								WiFiSupportService.userEditor.commit();
								if(Homepage.isactive)
								{
									//当前活动
									Intent intent = new Intent(WiFiSupportService.BroadcastFromservice); // 对应setAction()
									intent.putExtra(NameUser.name_intent.MSG,NameUser.Msg_Class.ALARM_MESSAGE);
									sendBroadcast(intent);
									//通知homepage刷新，ui(那边读数据即可)
								}
								else
								{
									//当前挂掉了
									String tmpstr1=null;
									String tmpstr2=null;
									if(main_s2h.equals("1"))
									{
										tmpstr1="一号农场";
									}
									else
									{
										tmpstr1="二号农场";
									}
									if(second_s2h.equals("1"))
									{
										tmpstr2="橘园";
									}
									else if(second_s2h.equals("2"))
									{
										tmpstr2="桃园";
									}
									else
									{
										tmpstr2="梅园";
									}
									String str=tmpstr1+"的"+tmpstr2+"有异常情况";
									sendstr2notify("警报", str);
									//然后会先销毁homepage再重新创建homepage
									/////
									//如何传数据过去？？
								}

							}
							else
							{
								//正常消息
								Log.d(TAG, "Message is 正常");
								String temperature = readMessage.substring(0, place1);
								String moisture = readMessage.substring(place1+1,place2);
								String sun = readMessage.substring(place2+1);
								Intent intent = new Intent(WiFiSupportService.BroadcastFromservice); // 对应setAction()
								intent.putExtra(NameUser.name_intent.MSG,NameUser.Msg_Class.READ_MESSAGE);
								intent.putExtra(NameUser.name_intent.name_temprature, temperature);
								intent.putExtra(NameUser.name_intent.name_moist, moisture);
								intent.putExtra(NameUser.name_intent.name_sun, sun);
								sendBroadcast(intent);
							}

						}
						else
						{
							Log.d(TAG, "Message is false");
						}

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case NameUser.Msg_Class.WRITE_MESSAGE:
					Log.d(TAG, "Msg_Class.WRITE_MESSAGE");
					byte[] writeBuf = (byte[]) msg.obj;
					try {
						String writeMessage = new String(writeBuf,"GB2312");
						Log.d(TAG, "writeMessage="+writeMessage);
						Intent intent = new Intent(WiFiSupportService.BroadcastFromservice); // 对应setAction()
						intent.putExtra(NameUser.name_intent.MSG,NameUser.Msg_Class.WRITE_MESSAGE);
						intent.putExtra(NameUser.name_intent.writemessage, writeMessage);
						sendBroadcast(intent);

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case NameUser.Msg_Class.RESULT_MESSAGE:
					Log.d(TAG, "Msg_Class.RESULT_MESSAGE");
					if(msg.arg1==NameUser.Connect_status.Connect_OK)
					{
						Log.d(TAG, "Connect_status.Connect_OK");
						Wificonnect_status=NameUser.Connect_status.Connect_OK;
					}
					else
					{
						Log.d(TAG, "Connect_status.Connect_FALSE");
						Wificonnect_status=NameUser.Connect_status.Connect_false;
					}
					Intent intent = new Intent(WiFiSupportService.BroadcastFromservice); // 对应setAction()
					intent.putExtra(NameUser.name_intent.MSG,NameUser.Msg_Class.RESULT_MESSAGE);
					intent.putExtra(NameUser.name_intent.conectstatus, msg.arg1);
					sendBroadcast(intent);
					break;

				default:
					break;
			}

		}
	};


	private WifiService mWifiService=null;
	public static int Wificonnect_status=NameUser.Connect_status.Connect_false;
	public static void playMsgmp3_start() {
		Log.d(TAG, "playMsgmp3_start()");
		if(mPlayer!=null)
		{
			Log.d(TAG, "playMsgmp3_start()===OK");
			alarmsoundisstart=true;
			mPlayer.start();
		}

	}
	public static void playMsgmp3_pause() {
		Log.d(TAG, "playMsgmp3_pause()");
		if(mPlayer!=null)
		{
			if(alarmsoundisstart)
			{
				Log.d(TAG, "playMsgmp3_pause()===OK");
				mPlayer.pause();
			}

		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, TAG+"------>onCreate()");
		mWifiService = new WifiService(ser2supser);
		/*
		 * 对播放器的初始化
	     */
		if (mPlayer == null) { //音乐播放准备
			mPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
			try {
				mPlayer.prepare();
			} catch (IllegalStateException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		super.onCreate();

	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, TAG+"------>onDestroy()");
		if(mPlayer != null)
			mPlayer.release();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG, TAG+"------>onStart()");
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, TAG+"------>onBind()");
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Log.d(TAG, TAG+"------>onStartCommand()");
		int msg = intent.getIntExtra(NameUser.name_intent.MSG, -1);
		switch (msg) {
			case NameUser.Command_Class.apply_Connect:
				Log.d(TAG, "case Command_Class.apply_Connect:");
				String ip_address = intent.getStringExtra(NameUser.name_intent.name_ipaddress);
				int ip_port = intent.getIntExtra(NameUser.name_intent.name_ipport, -1);
				Log.d(TAG, "ip_address="+ip_address);
				Log.d(TAG, "ip_port="+ip_port);
				mWifiService.Connect(ip_address, ip_port);

				break;

			case NameUser.Command_Class.Disconnect:
				Log.d(TAG, "case Command_Class.Disconnect:");
				mWifiService.Restart();
				break;

			case NameUser.Command_Class.sendmessage:
				Log.d(TAG, "case Command_Class.sendmessage:");

				String str =  intent.getStringExtra(NameUser.name_intent.writemessage);
				Log.d(TAG, "sendmessage="+str);
				if(str!=null)
				{
					mWifiService.WriteMessage(str);
				}

				break;
			default:

				break;
		}

		return super.onStartCommand(intent, flags, startId);
	}
	private void sendstr2notify(String title, String str) {
		//先摧毁,应用程序中的其它所有Activity然后再创建要跳转到Activity！！！！！！！！
		long[] vibratetime={500,500,500};
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle(title)
						.setContentText(str)
						.setVibrate(vibratetime);
		Intent resultIntent = new Intent(this, Homepage.class);
		resultIntent.putExtra(NameUser.name_intent.MSG,NameUser.name_intent.name_alarm);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Homepage.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
				);
		mBuilder.setContentIntent(resultPendingIntent);
//		PendingIntent updateIntent = PendingIntent.getActivity(this, 0,
//				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		mBuilder.setContentIntent(updateIntent);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(getApplication().hashCode(), mBuilder.build());
	}

}
