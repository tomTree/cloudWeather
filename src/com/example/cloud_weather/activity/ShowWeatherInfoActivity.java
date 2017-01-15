package com.example.cloud_weather.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.cloud_weather.R;
import com.example.cloud_weather.db.CoolWeatherDB;
import com.example.cloud_weather.model.WeatherInfo;
import com.example.cloud_weather.service.AutoUpdateService;
import com.example.cloud_weather.utlis.HttpCallBackListener;
import com.example.cloud_weather.utlis.HttpUtils;
import com.example.cloud_weather.utlis.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowWeatherInfoActivity extends Activity {

	//定位用的五个成员变量
	private LocationManager locationManager;
	private String provider;
	private LocationListener listener;
	private LatLng latlng;
	private String address;
	
	//处理Ｊｓｏｎ数据的回调接口
	private HttpCallBackListener callBack;
	
	//查询城市代码数据库
	private CoolWeatherDB coolWeatherDB;
	private Map<String,String> cityCodesMap=new HashMap<String,String>();
	
	
	//显示当天天气信息
	private TextView tv_cityName;
	private TextView tv_date;
	private TextView tv_weather_desp;
	private TextView tv_weather_temp;
	private ProgressDialog progressDialog;
	private RelativeLayout rl_weatherinfo;
	private String currentCityName;
	private static WeatherInfo currentWeatherInfo;
	
	//发送请求handler
	private Handler handlerRequest;
	private Handler handlerAfter;
	private static final int SHOW_WEATHER=0;
	private static final int ERROR=1;
	private static final int SUCCESSFULL=2;
	public static final int FRESH=0;
	public static final int NO_FRESH=1;
	
	
	
	//未来几天的数据
	private TextView tv_today;
	private ImageView iv_today;
	private TextView tv_today_tempRange;
	
	private TextView tv_tomorrow;
	private ImageView iv_tomorrow;
	private TextView tv_tomorrow_tempRange;
	
	private TextView tv_third;
	private ImageView iv_third;
	private TextView tv_third_tempRange;
	
	private TextView tv_forth;
	private ImageView iv_forth;
	private TextView tv_forth_tempRange;
	
	
	//服务lianjie
	private ServiceConnection serviceConnection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//SDK初始化
		SDKInitializer.initialize(getApplicationContext());
		//初始化城市代码
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_weatherinfo);
		coolWeatherDB=CoolWeatherDB.getInstance(this);
		
		//初始化控件
		tv_cityName=(TextView) findViewById(R.id.tv_cityName);
		
		tv_date=(TextView) findViewById(R.id.tv_date);
		tv_weather_desp=(TextView) findViewById(R.id.tv_weather_desp);
		
		tv_weather_temp=(TextView) findViewById(R.id.tv_temp1);
		rl_weatherinfo=(RelativeLayout) findViewById(R.id.weatherinfo);
		
		//未来几天
		tv_today=(TextView) findViewById(R.id.tv_today);
		iv_today=(ImageView) findViewById(R.id.iv_today);
		tv_today_tempRange=(TextView) findViewById(R.id.tv_today_tempRange);
		
		
		tv_tomorrow=(TextView) findViewById(R.id.tv_tommorrow);
		iv_tomorrow=(ImageView) findViewById(R.id.iv_tomorrow);
		tv_tomorrow_tempRange=(TextView) findViewById(R.id.tv_tomorrow_tempRange);
		
		
		tv_third=(TextView) findViewById(R.id.tv_thirdDay);
		iv_third=(ImageView) findViewById(R.id.iv_thirDay);
		tv_third_tempRange=(TextView) findViewById(R.id.tv_third_tempRange);
		
		tv_forth=(TextView) findViewById(R.id.tv_forthDay);
		iv_forth=(ImageView) findViewById(R.id.iv_Forthday);
		tv_forth_tempRange=(TextView) findViewById(R.id.tv_Forthday_tempRange);
		
		
		
		handlerRequest=new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==SHOW_WEATHER){
					String address=(String)msg.obj;
					HttpUtils.sendHttpRequest(address, new HttpCallBackListener(){
						@Override
						public void finish(String response) {
							Message msg=new Message();
							msg.what=SUCCESSFULL;
							msg.obj=response;
							handlerAfter.sendMessage(msg);
						}
						@Override
						public void onError(Exception e) {
							// TODO Auto-generated method stub
							Message msg=new Message();
							msg.what=ERROR;
							msg.obj=e.getMessage();
							handlerAfter.sendMessage(msg);
						}});
				}
			}
		};
		handlerAfter=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==SUCCESSFULL){
					
					 currentWeatherInfo=Utils.parseJson(ShowWeatherInfoActivity.this,(String)msg.obj,FRESH);
					showWeather(currentWeatherInfo);
					//Toast.makeText(ShowWeatherInfoActivity.this,"刷新成功", Toast.LENGTH_LONG).show();
				}else if(msg.what==ERROR){
					
					currentWeatherInfo=Utils.parseJson(ShowWeatherInfoActivity.this,(String)msg.obj,FRESH);
					showWeather(currentWeatherInfo);
					Toast.makeText(ShowWeatherInfoActivity.this,"数据未更新", Toast.LENGTH_LONG).show();
					
				}
			}
		};
		
		//位置监听器实例化
		listener=new LocationListener(){
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				showLocation(location);
			}
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}

			@Override
			public void onProviderDisabled(String provider) {}};
			//服务连接器实例化
			serviceConnection=new ServiceConnection(){
				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					AutoUpdateService.MyBinder myBinder=(AutoUpdateService.MyBinder)service;
					myBinder.refresh(ShowWeatherInfoActivity.this);
					
				}
				@Override
				public void onServiceDisconnected(ComponentName name) {}};
			
			//看intent中是否有数据
			Intent intent=getIntent();
			String cName=intent.getStringExtra("from_chooseActivity");
			if(cName!=null&&!cName.equals("")){
				currentCityName=cName;
				requestToBaidu(currentCityName);
				return;
			}
		//查看网络信息，如果没有网络，就不更新数据
		ConnectivityManager manager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=manager.getActiveNetworkInfo();
		
		if(info==null||!info.isAvailable()){
			WeatherInfo weather=Utils.parseJson(this, null, NO_FRESH);
			showWeather(weather);
			return;
		}
			
		//由于国家气象局接口不可用，此方法暂时用不到
		//initCityCodesMap(coolWeatherDB,cityCodesMap);
		
		//定位当前城市信息
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
		locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 500000, 5000, listener);
	}
	
	
	//显示天气的业务逻辑
	protected void showWeather(WeatherInfo weatherinfo) {
		Bitmap bitmap1=null;
		Bitmap bitmap2=null;
		Bitmap bitmap3=null;
		Bitmap bitmap4=null;
		String day01PictureUrl=weatherinfo.getDate01_dayPictureUrl();
		String day02PictureUrl=weatherinfo.getDate01_dayPictureUrl();
		String day03PictureUrl=weatherinfo.getDate01_dayPictureUrl();
		String day04PictureUrl=weatherinfo.getDate01_dayPictureUrl();
		
		
	
		while(true){
			bitmap1=Utils.getBitMap(this, coolWeatherDB, day01PictureUrl);
		if(bitmap1!=null){
			break;
			}
		}
		while(true){
			bitmap2=Utils.getBitMap(this, coolWeatherDB, day02PictureUrl);
			if(bitmap2!=null){
				break;
				}
			}
		
		while(true){
			bitmap3=Utils.getBitMap(this, coolWeatherDB, day03PictureUrl);
			if(bitmap2!=null){
				break;
				}
			}
		
		while(true){
			bitmap4=Utils.getBitMap(this, coolWeatherDB, day04PictureUrl);
				break;
				}
			
		//iv_test.setImageBitmap(bitmap);
		String currentCity=weatherinfo.getCurrentCity();
		String date=weatherinfo.getDate01_date();
		String currentTemperature=date.substring(date.indexOf("实时")+3, date.length()-1);
		String weather_desp=weatherinfo.getDate01_weather();
		String pm25=weatherinfo.getPm25();
		if(pm25.equals("")){
			pm25="无监测数据";
		}
		tv_date.setText(currentTemperature);
		tv_weather_desp.setText(weather_desp+" | pm2.5: "+pm25);
		tv_cityName.setText(currentCity);
		//tv_weather_temp.setText(date01_temp);
		if(weather_desp.contains("阴")){
			rl_weatherinfo.setBackgroundResource(R.drawable.yin);
		}else if(weather_desp.contains("雷雨")){
			rl_weatherinfo.setBackgroundResource(R.drawable.leiyu);
		}else if(weather_desp.contains("雨")){
			rl_weatherinfo.setBackgroundResource(R.drawable.yu);
		}else if(weather_desp.contains("晴")){
			rl_weatherinfo.setBackgroundResource(R.drawable.qing);
		}else if(weather_desp.contains("雪")){
			rl_weatherinfo.setBackgroundResource(R.drawable.xue);
		}else if(weather_desp.contains("多云")){
			rl_weatherinfo.setBackgroundResource(R.drawable.duoyun);
			tv_weather_desp.setTextColor(Color.BLACK);
		}
		
		
		//未来几天
		String tv_todayStr="今天：";
		String tv_todaytempRangeStr=weatherinfo.getDate01_temperature();
		tv_today.setText(tv_todayStr);
		iv_today.setImageBitmap(bitmap1);
		tv_today_tempRange.setText(tv_todaytempRangeStr);
		
		String tv_tomorrowStr=weatherinfo.getDate02_date();
		String tv_tomorrowtempRangeStr=weatherinfo.getDate02_temperature();
		tv_tomorrow.setText(tv_tomorrowStr);
		iv_tomorrow.setImageBitmap(bitmap2);
		tv_tomorrow_tempRange.setText(tv_tomorrowtempRangeStr);
		
		String tv_thirdStr=weatherinfo.getDate03_date();
		String tv_thirdtempRangeStr=weatherinfo.getDate03_temperature();
		tv_third.setText(tv_thirdStr);
		iv_third.setImageBitmap(bitmap3);
		tv_third_tempRange.setText(tv_thirdtempRangeStr);
		
		String tv_forthStr=weatherinfo.getDate04_date();
		String tv_forthtempRangeStr=weatherinfo.getDate04_temperature();
		tv_forth.setText(tv_forthStr);
		iv_forth.setImageBitmap(bitmap4);
		tv_forth_tempRange.setText(tv_forthtempRangeStr);
		
		
		rl_weatherinfo.setVisibility(View.VISIBLE);
		
		closeProgress();
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
					 currentCityName=result.getAddressDetail().city;
					
					requestToBaidu(currentCityName);
					return;
				
				}
				
			}

		};
			geoCoder.setOnGetGeoCodeResultListener(bListener);;
			//这里开始访问网络
			showProgress();
			boolean flag=geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
			
	
	}
	
	public void showWeatherInfo(final String city,String ptime,final String temp1,final String temp2,final String weather){
		
		runOnUiThread(new Runnable(){

		
			public void run() {
				tv_cityName.setText(city);
				SimpleDateFormat format=new SimpleDateFormat("YYYY-MM-dd");
				String date=format.format(new Date());
				
				tv_date.setText(date);
				tv_weather_desp.setText(weather);
				tv_weather_temp.setText(temp1+" ~ "+temp2);
				rl_weatherinfo.setVisibility(View.VISIBLE);
				closeProgress();
			}});
		
	}
	private void showProgress() {
		// TODO Auto-generated method stub
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("loading...........");
			//progressDialog.setCancelable(false);
		}
		progressDialog.show();
	}
	
	
	//关闭进度条
	private void closeProgress() {
		// TODO Auto-generated method stub
		
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
		
		
	}
	//切换城市的方法
	public void switcher(View v){
		Intent intent=new Intent();
		intent.setClass(this, ChooseCityActivity.class);
		startActivity(intent);
		finish();
	}
	
	//向百度服务器发出请求
	private void requestToBaidu(String crrentName) {
		rl_weatherinfo.setVisibility(View.INVISIBLE);
		String weatherinfo_address="http://api.map.baidu.com/telematics/v3/weather?location="+crrentName+"&output=json&ak=FVOnl3OlZCZOFAZa8k5uWs0CabiCd5YR&mcode=B7:05:FF:A2:76:AD:6D:F7:3D:85:F8:AE:65:BF:16:77:EC:B8:63:B5;com.example.cloud_weather";
		Log.e("address", weatherinfo_address);
		Message msg=new Message();
		msg.what=SHOW_WEATHER;
		msg.obj=weatherinfo_address;
		handlerRequest.sendMessage(msg);
		//startAutoUpdateService();
	}
	
	//刷新
	public void refresh(View v){
		showProgress();
		ConnectivityManager manager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=manager.getActiveNetworkInfo();
		if(info==null||!info.isAvailable()){
			Toast.makeText(this, "刷新失败，请检查网络", Toast.LENGTH_LONG).show();
			return;
		}
		
		requestToBaidu(currentCityName);
		closeProgress();
	}
	
	
	//启动和绑定服务
	public void startAutoUpdateService(){
		//启动服务
		Intent intent=new Intent(this,AutoUpdateService.class);
		
		//绑定服务
		bindService(intent, serviceConnection,BIND_AUTO_CREATE);
		
		
		
		
	}
	
	
	
	
	

}
