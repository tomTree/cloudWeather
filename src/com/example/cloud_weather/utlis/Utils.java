
package com.example.cloud_weather.utlis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.model.LatLng;
import com.example.cloud_weather.db.CoolWeatherDB;
import com.example.cloud_weather.model.City;
import com.example.cloud_weather.model.County;
import com.example.cloud_weather.model.Province;
import com.example.cloud_weather.model.WeatherInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


public class Utils {
	
	
	public static final int FRESH=0;
	public static final int NO_FRESH=1;
	
	
	
	
	
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
	
	//解析json
	
	public static WeatherInfo parseJson(Context context,String response,int flag){
		
		SharedPreferences pref=context.getSharedPreferences("weatherDate", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		
		if(flag==FRESH){
			JSONObject jsonObject=null;
			JSONArray temp1Array=null;
			JSONObject temp1Object=null;//可以获取当前城市名字，pm2.5;可以获取index数组，weather_data数组
			String cityName=null;//城市名称
			String pm25=null;//pm2.5
			JSONArray indexArr=null;
			JSONObject index1=null;//穿衣
			String clothes_title=null;
			String clothes_zs=null;
			String clothes_tipt=null;
			String clothes_des=null;
			
			JSONObject index2=null;//洗车
			String car_title=null;
			String car_zs=null;
			String car_tipt=null;
			String car_des=null;
			
			JSONObject index3=null;//旅游
			String trip_title=null;
			String trip_zs=null;
			String trip_tipt=null;
			String trip_des=null;
			
			JSONObject index4=null;//感冒
			String cold_title=null;
			String cold_zs=null;
			String cold_tipt=null;
			String cold_des=null;
			JSONObject index5=null;
			String sport_title=null;
			String sport_zs=null;
			String sport_tipt=null;
			String sport_des=null;//运动
			
			JSONObject index6=null;//紫外线
			String ray_title=null;
			String ray_zs=null;
			String ray_tipt=null;
			String ray_des=null;
			
			JSONArray weatherDates=null;
			
			JSONObject date01=null;//当天
			String date01_date=null;
			String date01_dayPictureUrl=null;
			String date01_nightPictureUrl=null;
			String date01_weather=null;
			String date01_wind=null;
			String date01_temperature=null;
			
			JSONObject date02=null;//第二天
			String date02_date=null;
			String date02_dayPictureUrl=null;
			String date02_nightPictureUrl=null;
			String date02_weather=null;
			String date02_wind=null;
			String date02_temperature=null;
			
			JSONObject date03=null;//第三天
			String date03_date=null;
			String date03_dayPictureUrl=null;
			String date03_nightPictureUrl=null;
			String date03_weather=null;
			String date03_wind=null;
			String date03_temperature=null;
			
			JSONObject date04=null;//第三天
			String date04_date=null;
			String date04_dayPictureUrl=null;
			String date04_nightPictureUrl=null;
			String date04_weather=null;
			String date04_wind=null;
			String date04_temperature=null;
			try {
					jsonObject=new JSONObject(response);
					if(jsonObject.getInt("error")==0){
						
						temp1Array=jsonObject.getJSONArray("results");
						temp1Object=temp1Array.getJSONObject(0);
						
						cityName=temp1Object.getString("currentCity");
						
						pm25=temp1Object.getString("pm25");
						
						indexArr=temp1Object.getJSONArray("index");
						
						index1=indexArr.getJSONObject(0);//穿衣
						clothes_title=index1.getString("title");
						clothes_zs=index1.getString("zs");
						clothes_tipt=index1.getString("tipt");
						clothes_des=index1.getString("des");
						
						index2=indexArr.getJSONObject(1);//洗车
						car_title=index2.getString("title");
						car_zs=index2.getString("zs");
						car_tipt=index2.getString("tipt");
						car_des=index2.getString("des");
						
						index3=indexArr.getJSONObject(2);
						trip_title=index3.getString("title");
						trip_zs=index3.getString("zs");
						trip_tipt=index3.getString("tipt");
						trip_des=index3.getString("des");
						
						index4=indexArr.getJSONObject(3);
						cold_title=index4.getString("title");
						cold_zs=index4.getString("zs");
						cold_tipt=index4.getString("tipt");
						cold_des=index4.getString("des");
						
						index5=indexArr.getJSONObject(4);
						sport_title=index5.getString("title");
						sport_zs=index5.getString("zs");
						sport_tipt=index5.getString("tipt");
						sport_des=index5.getString("des");
						
						index6=indexArr.getJSONObject(5);
						ray_title=index5.getString("title");
						ray_zs=index5.getString("zs");
						ray_tipt=index5.getString("tipt");
						ray_des=index5.getString("des");
						
						//几天的天气信息
						weatherDates=temp1Object.getJSONArray("weather_data");
						
						date01=weatherDates.getJSONObject(0);
						date01_date=date01.getString("date");
						Log.e("date01_date", "--------------------"+date01_date);
						date01_dayPictureUrl=date01.getString("dayPictureUrl");
						date01_nightPictureUrl=date01.getString("nightPictureUrl");
						date01_wind=date01.getString("wind");
						date01_weather=date01.getString("weather");
						date01_temperature=date01.getString("temperature");
						
						date02=weatherDates.getJSONObject(1);
						date02_date=date02.getString("date");
						date02_dayPictureUrl=date02.getString("dayPictureUrl");
						date02_nightPictureUrl=date02.getString("nightPictureUrl");
						date02_wind=date02.getString("wind");
						date02_weather=date02.getString("weather");
						date02_temperature=date02.getString("temperature");
						
						date03=weatherDates.getJSONObject(2);
						date03_date=date03.getString("date");
						date03_dayPictureUrl=date03.getString("dayPictureUrl");
						date03_nightPictureUrl=date03.getString("nightPictureUrl");
						date03_wind=date03.getString("wind");
						date03_weather=date03.getString("weather");
						date03_temperature=date03.getString("temperature");
						
						date04=weatherDates.getJSONObject(3);
						date04_date=date04.getString("date");
						date04_dayPictureUrl=date04.getString("dayPictureUrl");
						date04_nightPictureUrl=date04.getString("nightPictureUrl");
						date04_wind=date04.getString("wind");
						date04_weather=date04.getString("weather");
						
						
						
						date04_temperature=date04.getString("temperature");
						
						//将数据存到Preference对象中
						editor.clear();
						editor.putString("currentCity", cityName);
						//
						editor.putString("pm25", pm25);
						//穿衣
						editor.putString("sport_title", clothes_title);
						editor.putString("sport_zs", clothes_zs);
						editor.putString("sport_tipt", clothes_tipt);
						editor.putString("sport_des", clothes_des);
						//洗车
						editor.putString("car_title", car_title);
						editor.putString("car_zs", car_zs);
						editor.putString("car_tipt", car_tipt);
						editor.putString("car_des", car_des);
						//旅游
						editor.putString("trip_title", trip_title);
						editor.putString("trip_zs", trip_zs);
						editor.putString("trip_tipt", trip_tipt);
						editor.putString("trip_des", trip_des);
						//运动
						editor.putString("sport_title", sport_title);
						editor.putString("sport_zs", sport_zs);
						editor.putString("sport_tipt", sport_tipt);
						editor.putString("sport_des", sport_des);
						//感冒
						editor.putString("cold_title", cold_title);
						editor.putString("cold_zs", cold_zs);
						editor.putString("cold_tipt", cold_tipt);
						editor.putString("cold_des", cold_des);
						//紫外线
						editor.putString("ray_title", ray_title);
						editor.putString("ray_zs", ray_zs);
						editor.putString("ray_tipt", ray_tipt);
						editor.putString("ray_des", ray_des);
						
						//当天
						editor.putString("date01_date", date01_date);
						editor.putString("date01_dayPictureUrl", date01_dayPictureUrl);
						editor.putString("date01_nightPicture", date01_nightPictureUrl);
						editor.putString("date01_wind", date01_wind);
						editor.putString("date01_weather", date01_weather);
						editor.putString("date01_temperature",date01_temperature);
						//第二天
						editor.putString("date02_date", date02_date);
						editor.putString("date02_dayPictureUrl", date02_dayPictureUrl);
						editor.putString("date02_nightPicture", date02_nightPictureUrl);
						editor.putString("date02_wind", date02_wind);
						editor.putString("date02_weather", date02_weather);
						editor.putString("date02_temperature",date02_temperature);
						//第三天
						editor.putString("date03_date", date03_date);
						editor.putString("date03_dayPictureUrl", date03_dayPictureUrl);
						editor.putString("date03_nightPicture", date03_nightPictureUrl);
						editor.putString("date03_wind", date03_wind);
						editor.putString("date03_weather", date03_weather);
						editor.putString("date03_temperature",date03_temperature);
						//第四天
						editor.putString("date04_date", date04_date);
						editor.putString("date04_dayPictureUrl", date04_dayPictureUrl);
						editor.putString("date04_nightPicture", date04_nightPictureUrl);
						editor.putString("date04_wind", date04_wind);
						editor.putString("date04_weather", date04_weather);
						editor.putString("date04_temperature",date04_temperature);
						editor.commit();
					}
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
		//从preference对象中获取数据存储到
		WeatherInfo weatherInfo=new WeatherInfo();
		weatherInfo.setCurrentCity(pref.getString("currentCity","casc"));
		weatherInfo.setPm25(pref.getString("pm25",""));
		//穿衣
		weatherInfo.setClothes_title(pref.getString("clothes_title",""));
		weatherInfo.setClothes_zs(pref.getString("clothes_zs",""));
		weatherInfo.setClothes_tipt(pref.getString("clothes_tipt",""));
		weatherInfo.setClothes_des(pref.getString("clothes_des", ""));
		//洗车
		weatherInfo.setCar_title(pref.getString("ar_title",""));
		weatherInfo.setCar_zs(pref.getString("c_zs",""));
		weatherInfo.setCar_tipt(pref.getString("car_tipt",""));
		weatherInfo.setCar_des(pref.getString("car_des", ""));
		//旅游
		weatherInfo.setTrip_title(pref.getString("trip_title",""));
		weatherInfo.setTrip_zs(pref.getString("trip_zs",""));
		weatherInfo.setTrip_tipt(pref.getString("trip_tipt",""));
		weatherInfo.setTrip_des(pref.getString("trip_des", ""));
		//运动
		weatherInfo.setSport_title(pref.getString("sport_title",""));
		weatherInfo.setSport_zs(pref.getString("sport_zs",""));
		weatherInfo.setSport_tipt(pref.getString("sport_tipt",""));
		weatherInfo.setSport_des(pref.getString("sport_des", ""));
		//感冒
		weatherInfo.setCold_title(pref.getString("cold_title",""));
		weatherInfo.setCold_zs(pref.getString("cold_zs",""));
		weatherInfo.setCold_tipt(pref.getString("cold_tipt",""));
		weatherInfo.setCold_des(pref.getString("cold_des", ""));
		//紫外线
		weatherInfo.setRay_title(pref.getString("ray_title",""));
		weatherInfo.setRay_zs(pref.getString("ray_zs",""));
		weatherInfo.setRay_tipt(pref.getString("ray_tipt",""));
		weatherInfo.setRay_des(pref.getString("ray_des", ""));
		
		//当天
		weatherInfo.setDate01_date(pref.getString("date01_date",""));
		weatherInfo.setDate01_dayPictureUrl(pref.getString("date01_dayPictureUrl", ""));
		weatherInfo.setDate01_nightPictureUrl(pref.getString("date01_nightPictureUrl", ""));
		weatherInfo.setDate01_wind(pref.getString("date01_wind",""));
		weatherInfo.setDate01_weather(pref.getString("date01_weather",""));
		weatherInfo.setDate01_temperature(pref.getString("date01_temperature", ""));
		//第二天
		weatherInfo.setDate02_date(pref.getString("date02_date",""));
		weatherInfo.setDate02_dayPictureUrl(pref.getString("date02_dayPictureUrl", ""));
		weatherInfo.setDate02_nightPictureUrl(pref.getString("date02_nightPictureUrl", ""));
		weatherInfo.setDate02_wind(pref.getString("date02_wind",""));
		weatherInfo.setDate02_weather(pref.getString("date02_weather",""));
		weatherInfo.setDate02_temperature(pref.getString("date02_temperature", ""));
		//第三天
		weatherInfo.setDate03_date(pref.getString("date03_date",""));
		weatherInfo.setDate03_dayPictureUrl(pref.getString("date03_dayPictureUrl", ""));
		weatherInfo.setDate03_nightPictureUrl(pref.getString("date03_nightPictureUrl", ""));
		weatherInfo.setDate03_wind(pref.getString("date03_wind",""));
		weatherInfo.setDate03_weather(pref.getString("date03_weather",""));
		weatherInfo.setDate03_temperature(pref.getString("date03_temperature", ""));
		//第四天
		weatherInfo.setDate04_date(pref.getString("date04_date",""));
		weatherInfo.setDate04_dayPictureUrl(pref.getString("date04_dayPictureUrl", ""));
		weatherInfo.setDate04_nightPictureUrl(pref.getString("date04_nightPictureUrl", ""));
		weatherInfo.setDate04_wind(pref.getString("date04_wind",""));
		weatherInfo.setDate04_weather(pref.getString("date04_weather",""));
		weatherInfo.setDate04_temperature(pref.getString("date04_temperature", ""));
		
		
		//将weatherInfo返回
		
		return weatherInfo;
	
		
	}

	//根据传入图片名称获取图片
	public static Bitmap getBitMap(final Context context,final CoolWeatherDB db,String imageUrl){
		
		
		String[] strArr=imageUrl.split("/");
		//白天或夜晚
		String time=strArr[5];
		//图片名称
		String imageName=strArr[6];
		
		String imagefile=db.getImgaeFile(time+"/"+imageName);
		Bitmap bitmap=null;
		File file=null;
		
		if(imagefile!=null&&!imagefile.equals("")){
			
			file=new File(Environment.getExternalStorageDirectory(),imagefile);
			
			Uri uri=Uri.fromFile(file);
			try {
				bitmap=BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
				Log.e("啦啦啦啦", "正在下载图片=="+bitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			
			HttpUtils.getImageUrl(context,imageUrl, new HttpBitmapCallBack(){

				@Override
				public Bitmap finish(CoolWeatherDB coolWeatherDB,String imageUrl) {
					// TODO Auto-generated method stub
					
					String[] strArr=imageUrl.split("/");
					//白天或夜晚
					String time=strArr[5];
					//图片名称
					final String imageName=strArr[6];
					
					coolWeatherDB.addImage(time+"/"+imageName);
					
					return getBitMap(context, db, imageUrl);
				}

				@Override
				public String onError(Exception e) {
					
					return null;
				}});
			
			
			
		}
		
		
	
		return bitmap;
		
		
		
	}
}
