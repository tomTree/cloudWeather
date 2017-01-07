package com.example.cloud_weather.utlis;

import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.example.cloud_weather.db.CoolWeatherDB;
import com.example.cloud_weather.model.City;
import com.example.cloud_weather.model.County;
import com.example.cloud_weather.model.Province;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	/**
	 * 解析服务器返回的数据
	 * 
	 * */
	
	//1、解析服务器返回的升级数据,放到数据库中
	public synchronized static boolean handleProvinceReaponse(CoolWeatherDB coolWeatherDB,String response){
		
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces=response.trim().split(",");
			for(String s:allProvinces){
				//注意反斜缸
				Log.e("sssssssssssss", s);
				String[] ss=s.trim().split("\\|");
				Province p=new Province();
				p.setCode(ss[0]);
				p.setName(ss[1]);
				coolWeatherDB.saveProvince(p);
			}
			
			return true;
		}else{
	
		return false;
		}
	}
	
	//解析处理市级数据并放到数据
	
	public synchronized static boolean handlerCityResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			for(String c:allCities){
				String[] cc=c.split("\\|");
				City city=new City();
				city.setName(cc[1]);
				city.setCode(cc[0]);
				city.setProvince_id(provinceId);
				coolWeatherDB.saveCity(city);
			}
			
			return true;
			
		}

		return false;
		
	}
	
	public static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties=response.split(",");
			for(String a:allCounties){
				String[] aa=a.split("\\|");
				County county=new County();
				county.setCode(aa[0]);
				county.setName(aa[1]);
				county.setCity_id(cityId);
				coolWeatherDB.saveCounty(county);
			}
			return true;
		}
		return false;
	}
	
	//获取经纬度
	static LatLng latlng;
	public static LatLng getLatLng(Context context,LocationManager locationManager){
		
		
		List<String> providers=locationManager.getProviders(true);
		String provider=null;
		
		if(providers.contains(LocationManager.GPS_PROVIDER)){
			provider=LocationManager.GPS_PROVIDER;
		}else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
			provider=LocationManager.NETWORK_PROVIDER;
		}else if(providers.contains(LocationManager.PASSIVE_PROVIDER)){
			provider=LocationManager.PASSIVE_PROVIDER;
		}else{
			
			Toast.makeText(context,"no provider available", Toast.LENGTH_LONG).show();
			
			
		}
		//创建location对象
		Location location=locationManager.getLastKnownLocation(provider);
		
		
		LocationListener listener=new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				latlng=new LatLng(location.getLatitude(),location.getLongitude());	
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
			}
			
		};
		
		locationManager.requestLocationUpdates(provider,10000, 100, listener);
		
		if(latlng!=null){
			locationManager.removeUpdates(listener);;
		}
		return latlng;
		
	}
	

	
	
}
