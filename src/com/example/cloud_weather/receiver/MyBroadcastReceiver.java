package com.example.cloud_weather.receiver;

import com.example.cloud_weather.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent intent01=new Intent(context,AutoUpdateService.class);
		context.startService(intent01);
		
	}

}
