package com.example.cloud_weather.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.cloud_weather.R;
import com.example.cloud_weather.db.CoolWeatherDB;
import com.example.cloud_weather.utlis.HttpCallBackListener;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class ShowWeatherInfoActivity extends Activity {

	private LocationManager locationManager;
	private Location location;
	private String provider;
	
	private LocationListener listener;
	
	private LatLng latlng;
	
	private String address;
	
	private HttpCallBackListener callBack;
	
	//查询城市代码数据库
	private CoolWeatherDB coolWeatherDB;
	private Map<String,String> cityCodesMap=new HashMap<String,String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//SDK初始化
		SDKInitializer.initialize(getApplicationContext());
		//初始化城市代码
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.show_weatherinfo);
		coolWeatherDB=CoolWeatherDB.getInstance(this);
		initCityCodesMap(coolWeatherDB,cityCodesMap);
		locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		List<String> providers=locationManager.getProviders(true);
		if(providers.contains(LocationManager.GPS_PROVIDER)){
			provider=LocationManager.GPS_PROVIDER;
		}else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
			provider=LocationManager.NETWORK_PROVIDER;
		}else if(providers.contains(LocationManager.PASSIVE_PROVIDER)){
			provider=LocationManager.PASSIVE_PROVIDER;
		}else{
			Toast.makeText(this, "no provider is avaible", Toast.LENGTH_LONG).show();
			return;
		}
		
		location=locationManager.getLastKnownLocation(provider);
		
		listener=new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				showLocation(location);
				
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
				
			}};
			locationManager.requestLocationUpdates(provider, 5000, 1, listener);
	}
	
	
	
	//初始化城市代码
	private void initCityCodesMap(CoolWeatherDB coolWeatherDB, Map<String, String> map) {
		// TODO Auto-generated method stub
		coolWeatherDB.initalCodes(map);
		
	}


	private void showLocation(Location location) {
		// TODO Auto-generated method stub
		
		
		latlng=new LatLng(location.getLatitude(),location.getLongitude());
		
		GeoCoder geoCoder=GeoCoder.newInstance();
		
		OnGetGeoCoderResultListener bListener=new OnGetGeoCoderResultListener(){
			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				// TODO Auto-generated method stub
				//Toast.makeText(MainActivity.this, result+"====", Toast.LENGTH_LONG).show();
				//Toast.makeText(MainActivity.this, "====="+result, Toast.LENGTH_LONG).show();
				if(result==null
						){
					Toast.makeText(ShowWeatherInfoActivity.this, "wu fajiazai", Toast.LENGTH_LONG).show();
				}else{
					address=result.getAddress();
					String crrentName=result.getAddressDetail().city;
					String cityCode=null;
					//Toast.makeText(ShowWeatherInfoActivity.this,result.getAddressDetail().city, Toast.LENGTH_LONG).show();
					
					
					for(String cityName:cityCodesMap.keySet()){
						
						if(crrentName.contains(cityName)){
							cityCode=cityCodesMap.get(cityName);
							break;
						}
						
					}
					Toast.makeText(ShowWeatherInfoActivity.this,result.getAddressDetail().city+":"+cityCode, Toast.LENGTH_LONG).show();
					return;
				
				}
				
			}};
			
			geoCoder.setOnGetGeoCodeResultListener(bListener);;
			boolean flag=geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
			
	
	}
	
	
	


}
