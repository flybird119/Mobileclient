package com.yqman.service;

public class NameUser {
	
	public static class name_store{
		public static final String GlobalSharedName = "LocalDataBase";
		public static final String name_ipaddress="ipaddress";
		public static final String name_ipport="ipport";
		public static final String name_temprature="temprature";
		public static final String name_moist="moist";
		public static final String name_sun="sun";	
		public static final String name_alarm_main="alarmmain";	
		public static final String name_alarm_second="alarmsecond";	
	}
	public static class name_intent{
		public static final String MSG="MSG";
		public static final String writemessage="writemessage";
		public static final String conectstatus="connectstatus";
		public static final String name_temprature="temprature";
		public static final String name_moist="moist";
		public static final String name_sun="sun";	
		public static final String name_ipaddress="ipaddress";
		public static final String name_ipport="ipport";
		public static final String name_alarm_main="alarmmain";	
		public static final String name_alarm_second="alarmsecond";
		public static final String name_alarm="namealarm";
	}

	public static class Connect_status
	{
		public static final int Connect_OK=0;
		public static final int Connect_false=1;
	}
	public static class Service2Home
	{
		public static final String alarm_main="s2h_alarmmain";	
		public static final String alarm_second="s2h_alarmsecond";
	}
	public static class Msg_Class
	{
		
		public static final int READ_MESSAGE=0;
		public static final int WRITE_MESSAGE=1;
		public static final int RESULT_MESSAGE=2;
		public static final int ALARM_MESSAGE=3;
	}
	
	public static class Command_Class
	{
		
		public static final int apply_Connect=1;
		public static final int Disconnect=2;
		public static final int sendmessage=3;
	}

}
