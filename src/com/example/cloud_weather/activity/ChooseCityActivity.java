package com.example.cloud_weather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.cloud_weather.R;
import com.example.cloud_weather.db.CoolWeatherDB;
import com.example.cloud_weather.model.City;
import com.example.cloud_weather.model.County;
import com.example.cloud_weather.model.Province;
import com.example.cloud_weather.utlis.HttpCallBackListener;
import com.example.cloud_weather.utlis.HttpUtils;
import com.example.cloud_weather.utlis.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseCityActivity extends Activity{
	private TextView titletext;
	private ListView lv_choose;
	private ArrayAdapter<String> adapter;
	private List<String> listData;
	private CoolWeatherDB coolWeatherDB;
	
	public static final int PROVINCE_LEVEL=0;
	public static final int CITY_LEVEL=1;
	public static final int COUNTY_LEVEL=2;
	
	private List<Province> provinces;
	private List<City> cities;
	private List<County> counties;
	
	private int currentLevel;
	
	private Province selectedProvince;
	
	private City selectedCity;
	
	//进度条
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		titletext=(TextView) findViewById(R.id.tv_titleText);
		lv_choose=(ListView) findViewById(R.id.lv_choose);
		listData=new ArrayList<String>();
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listData);
		lv_choose.setAdapter(adapter);
		
		coolWeatherDB=CoolWeatherDB.getInstance(this);
		queryProvices();
		lv_choose.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(currentLevel==PROVINCE_LEVEL){
					
					selectedProvince=provinces.get(position);
					queryCitities();
					titletext.setText(selectedProvince.getName());
					
				}else if(currentLevel==CITY_LEVEL){
					selectedCity=cities.get(position);
					queryCounties();
					titletext.setText(selectedCity.getName());
				}
				
			}

		
	});
		
		
	
	}
	private void queryProvices() {
		// TODO Auto-generated method stub
		provinces=coolWeatherDB.getProvinces();
		if(provinces.size()>0){
			listData.clear();
			for(Province p:provinces){
				listData.add(p.getName());
			}
			adapter.notifyDataSetChanged();
			lv_choose.setSelection(0);
			currentLevel=PROVINCE_LEVEL;
		}else{
			queryFromServer(null,"province");
		}
		
		
	}
	
	
	private void queryCitities() {
		// TODO Auto-generated method stub
		cities=coolWeatherDB.getCities(selectedProvince.getId());
		if(cities.size()>0){
			listData.clear();
			for(City c:cities){
				listData.add(c.getName());
			}
			adapter.notifyDataSetChanged();
			lv_choose.setSelection(0);
			currentLevel=CITY_LEVEL;
			
		}else{
			queryFromServer(selectedProvince.getCode(),"city");
		}
	}
	
	private void queryCounties() {
		// TODO Auto-generated method stub
		
		counties=coolWeatherDB.getCounties(selectedCity.getId());
		if(counties.size()>0){
			listData.clear();
			for(County c:counties){
				listData.add(c.getName());
			}
			adapter.notifyDataSetChanged();
			lv_choose.setSelection(0);
			currentLevel=COUNTY_LEVEL;
			
		}else{
			queryFromServer(selectedCity.getCode(),"county");
		}
	
	}
	
	

	private void queryFromServer(final String code,final String type) {
		// TODO Auto-generated method stub
		
		String address="";
		
		
		if(!TextUtils.isEmpty(code)){
			address="http://www.weather.com.cn/data/list3/city"+code+".xml";
			Log.e("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",address);
		}else{
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		
			showProgress();
		
			HttpUtils.sendHttpRequest(address, new HttpCallBackListener(){

				String str="";
				
				@Override
				public void finish(String response) {
					// TODO Auto-generated method stub
					boolean result=false;
					
					str=response;
					
					if("province".equals(type)){
						result=Utils.handleProvinceReaponse(coolWeatherDB, response);
					}else if("city".equals(type)){
						result=Utils.handlerCityResponse(coolWeatherDB, response, selectedProvince.getId());
					}else if("county".equals(type)){
						result=Utils.handleCountyResponse(coolWeatherDB, response, selectedCity.getId());
					}
					
					if(result){
						
						//通过runOnUiThread()方法回到主线线程更新UI
						runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if("province".equals(type)){
									queryProvices();
								}else if("city".equals(type)){
									queryCitities();
								}else if("county".equals(type)){
									queryCounties();
								}
							}});
					
					
					}
					Log.e("result", "++++++"+str.trim().split(",")[9]);
					
					closeProgress();
				}

				@Override
				public void onError(final Exception e) {
					
					// TODO Auto-generated method stub
					Looper.prepare();
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgress();
							
							Toast.makeText(ChooseCityActivity.this, "加载失败le "+e.getMessage(), Toast.LENGTH_LONG).show();
							
						}
							});
					
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
	
	//重写onBack方法，通过currentLevel判断是否退出
	@Override
	public void onBackPressed() {
		if(currentLevel==COUNTY_LEVEL){
			queryCitities();
			
			
		}else if(currentLevel==CITY_LEVEL){
			queryProvices();
		}else if(currentLevel==PROVINCE_LEVEL){
			finish();
		}
	}
	
}
