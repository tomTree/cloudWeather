package com.example.cloud_weather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.cloud_weather.model.City;
import com.example.cloud_weather.model.County;

import com.example.cloud_weather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	//���ݿ�����
	private static final String DB_NAME="coolweather";
	//���ݿ�汾
	private static final int VERSION=1;
	//����
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context,
				DB_NAME,null,VERSION);
		db=dbHelper.getWritableDatabase();
	}
	//��ȡcoolWeather ʵ��
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	//����ʡ��
	
	public void saveProvince(Province province){
		ContentValues values=new ContentValues();
		values.put("province_id", province.getId());
		values.put("province_name", province.getName());
		values.put("province_code", province.getCode());
		db.insert("provinces",null, values);
	}
	//�������
	public void saveCity(City city){
		ContentValues values=new ContentValues();
		values.put("city_id",city.getId());
		values.put("city_name",city.getName());
		values.put("city_code",city.getCode());
		values.put("pronvince_id",city.getProvince_id());
		db.insert("cities", null, values);
	}
	//�����ؼ�
	
	public void saveCounty(County county){
		ContentValues values=new ContentValues();
		values.put("county_id", county.getId());
		values.put("county_name", county.getName());
		values.put("county_code", county.getCode());
		values.put("city_name", county.getCity_id());
		db.insert("counties", null, values);
	}
	
	//��ȡʡ���б�
	
	public List<Province> getProvinces(){
		Cursor cursor=db.query("province",null, null,null, null, null, null);
		List<Province> list=new ArrayList<Province>();
		if(cursor.moveToFirst()){
			
			do{
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("pronvice_id")));
				province.setCode(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setName(cursor.getString(cursor.getColumnIndex("province_name")));
			list.add(province);
			}while(cursor.moveToNext());
			
			
			
			
		}
		
		return list;
	}
	
	//��ȡ����
	public List<City> getCities(int provinceId){
		Cursor cursor=db.query("cities", null,"province_id=?", new String[]{String.valueOf(provinceId)},null,null, null);
		List<City> list=new ArrayList<City>();
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("city_id")));
				city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvince_id(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		return list;
	}
	//��ȡ�ؼ�
	public List<County> getCounties(int cityId){
		Cursor cursor=db.query("cities", null,"city_id=?", new String[]{String.valueOf(cityId)},null,null, null);
		List<County> list=new ArrayList<County>();
		if(cursor.moveToFirst()){
			do{
				County county=new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("county_id")));
				county.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
				county.setName(cursor.getString(cursor.getColumnIndex("city_name")));
				county.setCity_id(cityId);
				list.add(county);
			}while(cursor.moveToNext());
		}
	return list;
	}
}
