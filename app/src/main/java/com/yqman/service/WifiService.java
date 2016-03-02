/*
 * @function 为应用程序提供服务
 * 
 */
package com.yqman.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.conn.ClientConnectionManager;

import android.os.Handler;
import android.util.Log;

public class WifiService {

	private static final String TAG="WifiService ";

	public static int IsConnect =NameUser.Connect_status.Connect_false;
	private ConnectThread mConnectThread=null;
	private ConnectedThread mConnectedThread=null;
	private Handler mHandler=null;
	public WifiService(Handler tmpHandler) {
		Log.d(TAG, "WifiService()");
		// TODO Auto-generated constructor stub
		mHandler=tmpHandler;
	}
	/*
	 * 申请连接
	 */
	public void Connect(String ip_address, int ip_port)
	{
		Log.d(TAG, "Connect()");
		if(mConnectThread!=null)
		{
			mConnectThread.Cancel();
			mConnectThread=null;
			mConnectThread = new ConnectThread(ip_address, ip_port);
			mConnectThread.start();
		}
		else
		{
			mConnectThread = new ConnectThread(ip_address, ip_port);
			mConnectThread.start();
		}

	}

	public void WriteMessage(String str)
	{
		Log.d(TAG, "WriteMessage()");
		byte[] buffer;
		try {
			buffer = str.getBytes("GB2312");
			if(buffer!=null)
			{
				mConnectedThread.write(buffer);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/*
	 * 断开连接
	 */
	public void Restart()
	{
		Log.d(TAG, "Restart()");
		if(mConnectThread!=null)
		{
			mConnectThread.Cancel();
			mConnectThread=null;

		}
		if(mConnectedThread!=null)
		{
			mConnectedThread=null;
		}
		IsConnect = NameUser.Connect_status.Connect_false;
		mHandler.obtainMessage(NameUser.Msg_Class.RESULT_MESSAGE,
				NameUser.Connect_status.Connect_false, -1).sendToTarget();
	}
	private class ConnectThread extends Thread
	{
		private Socket mSocket;
		private String ip_address;
		private int ip_port;
		public ConnectThread(String ip_addresstmp,int ip_porttmp)
		{
			// TODO Auto-generated constructor stub
			Log.d(TAG, "ConnectThread()");
			ip_address=ip_addresstmp;
			ip_port=ip_porttmp;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d(TAG, "ConnectThread—— run()");
			Socket tmp = null;
			try {
				Log.d(TAG, "ip_address="+ip_address);
				Log.d(TAG, "ip_port="+ip_port);
				tmp = new Socket(ip_address, ip_port);
				mSocket = tmp;
				mConnectedThread = new ConnectedThread(mSocket);
				mConnectedThread.start();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		public void Cancel()
		{
			Log.d(TAG, "ConnectThread——Cancel()");
			try {
				if(mSocket!=null)
				{
					mSocket.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class ConnectedThread extends Thread
	{
		private  Socket mSocket;
		private  InputStream mInputStream;
		private  OutputStream mOutputStream;
		public ConnectedThread(Socket mSocket_tmp)
		{
			// TODO Auto-generated constructor stub
			Log.d(TAG, "ConnectedThread()");
			try {
				mSocket = mSocket_tmp;
				mInputStream=mSocket.getInputStream();
				mOutputStream=mSocket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IsConnect = NameUser.Connect_status.Connect_OK;
			mHandler.obtainMessage(NameUser.Msg_Class.RESULT_MESSAGE,
					NameUser.Connect_status.Connect_OK, -1).sendToTarget();

		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d(TAG, "ConnectedThread——run()");
			byte[] readbuffer = new byte[1024];
			int readnum=-1;
			while (true)
			{
				try
				{
					if((readnum=mInputStream.read(readbuffer))==-1)
					{
						Restart();
						break;
					}

					Log.d(TAG,"receivemessage"+readbuffer);
					Log.d(TAG, "接受到的数据长度="+readnum);
					mHandler.obtainMessage(NameUser.Msg_Class.READ_MESSAGE,
							readnum, -1, readbuffer).sendToTarget();

				}
				catch (IOException e)
				{
					Restart();
					break;
				}

			}
		}
		/**
		 * Write to the connected OutStream.
		 * @param buffer  The bytes to write
		 */
		public void write(byte[] writebuffer) {
			Log.d(TAG, "ConnectedThread——write()==》"+writebuffer);
			try {
				mOutputStream.write(writebuffer);
				mHandler.obtainMessage(NameUser.Msg_Class.WRITE_MESSAGE,
						-1, -1, writebuffer).sendToTarget();
			}
			catch (IOException e)
			{

			}
		}

		public void Cancel()
		{
			Log.d(TAG, "ConnectedThread——Cancel()");
			try {
				mSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
