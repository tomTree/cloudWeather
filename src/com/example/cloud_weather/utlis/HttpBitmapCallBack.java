package com.example.cloud_weather.utlis;

import com.example.cloud_weather.db.CoolWeatherDB;

import android.graphics.Bitmap;

public interface HttpBitmapCallBack {
	
	
	Bitmap finish(CoolWeatherDB coolWeatherDB,String imageUrl);
	String onError(Exception e);
	
	
}
