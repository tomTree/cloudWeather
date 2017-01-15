package com.example.cloud_weather.service;

import com.example.cloud_weather.activity.ShowWeatherInfoActivity;
import com.example.cloud_weather.receiver.MyBroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class AutoUpdateService extends Service{
	
	/**
	 *后台更新数据
	 *
	 *1创建一个Binder的子类，以便调用Context里面的方法
	 *
	 * 
	 * 
	 * 
	 * */
	
	
	private AlarmManager manager;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MyBinder();
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		manager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}
	
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
	
		
		long triggerTime=SystemClock.elapsedRealtime()+5000;
		Intent intent01=new Intent(this,MyBroadcastReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, intent01, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	//Binder是Ibinder的实现类
	public class MyBinder extends Binder{
		
		public void refresh(ShowWeatherInfoActivity show){
			Log.e("AUtodjsfhsdisdji", "refresh.......................");
			show.refresh(null);
		}
		
		
		
		
	}
	
	

}
