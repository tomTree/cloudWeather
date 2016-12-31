package com.example.cloud_weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper{

	private static final String CREATE_PROVINCES="create table provinces("
			+ "province_id integer primary key autoincrement,"
			+ "province_name text,"
			+ "province_code text)";
	
	private static final String CREATE_CITIES="create table cities(city_id integer primary key autoincrement,"
			+ "city_name text,"
			+ "city_code text,"
			+ "province_id integer)";
	
	private static final String CREATE_COUNTIES="create table counties("
			+ "county_id integer primary key autoincrement,"
			+ "county_name text,county_code text,city_id integer)";
	
	
	
	
	
	
	
	public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCES);
		db.execSQL(CREATE_CITIES);
		db.execSQL(CREATE_COUNTIES);

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
