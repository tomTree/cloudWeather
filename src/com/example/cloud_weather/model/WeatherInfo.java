package com.example.cloud_weather.model;

public class WeatherInfo {
	
	//当前城市名称
	
	private String currentCity;
	private String pm25;
	
	//穿衣
	private String clothes_title;
	private String clothes_zs;
	private String clothes_tipt;
	private String clothes_des;
	//洗车
	private String car_title;
	private String car_zs;
	private String car_tipt;
	private String car_des;
	//旅游
	private String trip_title;
	private String trip_zs;
	private String trip_tipt;
	private String trip_des;
	//运动
	private String sport_title;
	private String sport_zs;
	private String sport_tipt;
	private String sport_des;
	//感冒
	private String cold_title;
	private String cold_zs;
	private String cold_tipt;
	private String cold_des;
	//紫外线
	private String ray_title;
	private String ray_zs;
	private String ray_tipt;
	private String ray_des;
	
	//当天
	private String date01_date;
	private String date01_dayPictureUrl;
	private String date01_nightPictureUrl;
	private String date01_wind;
	private String date01_weather;
	
	public String getDate01_weather() {
		return date01_weather;
	}
	public void setDate01_weather(String date01_weather) {
		this.date01_weather = date01_weather;
	}
	public String getDate02_weather() {
		return date02_weather;
	}
	public void setDate02_weather(String date02_weather) {
		this.date02_weather = date02_weather;
	}
	public String getDate03_weather() {
		return date03_weather;
	}
	public void setDate03_weather(String date03_weather) {
		this.date03_weather = date03_weather;
	}
	public String getDate04_weather() {
		return date04_weather;
	}
	public void setDate04_weather(String date04_weather) {
		this.date04_weather = date04_weather;
	}
	private String date01_temperature;
	//第二天
	private String date02_date;
	private String date02_dayPictureUrl;
	private String date02_nightPictureUrl;
	private String date02_wind;
	private String date02_weather;
	private String date02_temperature;
	//第三天
	private String date03_date;
	private String date03_dayPictureUrl;
	private String date03_nightPictureUrl;
	private String date03_wind;
	private String date03_weather;
	private String date03_temperature;
	//第四天
	private String date04_date;
	private String date04_dayPictureUrl;
	private String date04_nightPictureUrl;
	private String date04_wind;
	private String date04_weather;
	private String date04_temperature;
	
	
	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String getClothes_title() {
		return clothes_title;
	}
	public void setClothes_title(String clothes_title) {
		this.clothes_title = clothes_title;
	}
	public String getClothes_zs() {
		return clothes_zs;
	}
	public void setClothes_zs(String clothes_zs) {
		this.clothes_zs = clothes_zs;
	}
	public String getClothes_tipt() {
		return clothes_tipt;
	}
	public void setClothes_tipt(String clothes_tipt) {
		this.clothes_tipt = clothes_tipt;
	}
	public String getClothes_des() {
		return clothes_des;
	}
	public void setClothes_des(String clothes_des) {
		this.clothes_des = clothes_des;
	}
	public String getCar_title() {
		return car_title;
	}
	public void setCar_title(String car_title) {
		this.car_title = car_title;
	}
	public String getCar_zs() {
		return car_zs;
	}
	public void setCar_zs(String car_zs) {
		this.car_zs = car_zs;
	}
	public String getCar_tipt() {
		return car_tipt;
	}
	public void setCar_tipt(String car_tipt) {
		this.car_tipt = car_tipt;
	}
	public String getCar_des() {
		return car_des;
	}
	public void setCar_des(String car_des) {
		this.car_des = car_des;
	}
	public String getTrip_title() {
		return trip_title;
	}
	public void setTrip_title(String trip_title) {
		this.trip_title = trip_title;
	}
	public String getTrip_zs() {
		return trip_zs;
	}
	public void setTrip_zs(String trip_zs) {
		this.trip_zs = trip_zs;
	}
	public String getTrip_tipt() {
		return trip_tipt;
	}
	public void setTrip_tipt(String trip_tipt) {
		this.trip_tipt = trip_tipt;
	}
	public String getTrip_des() {
		return trip_des;
	}
	public void setTrip_des(String trip_des) {
		this.trip_des = trip_des;
	}
	public String getSport_title() {
		return sport_title;
	}
	public void setSport_title(String sport_title) {
		this.sport_title = sport_title;
	}
	public String getSport_zs() {
		return sport_zs;
	}
	public void setSport_zs(String sport_zs) {
		this.sport_zs = sport_zs;
	}
	public String getSport_tipt() {
		return sport_tipt;
	}
	public void setSport_tipt(String sport_tipt) {
		this.sport_tipt = sport_tipt;
	}
	public String getSport_des() {
		return sport_des;
	}
	public void setSport_des(String sport_des) {
		this.sport_des = sport_des;
	}
	public String getCold_title() {
		return cold_title;
	}
	public void setCold_title(String cold_title) {
		this.cold_title = cold_title;
	}
	public String getCold_zs() {
		return cold_zs;
	}
	public void setCold_zs(String cold_zs) {
		this.cold_zs = cold_zs;
	}
	public String getCold_tipt() {
		return cold_tipt;
	}
	public void setCold_tipt(String cold_tipt) {
		this.cold_tipt = cold_tipt;
	}
	public String getCold_des() {
		return cold_des;
	}
	public void setCold_des(String cold_des) {
		this.cold_des = cold_des;
	}
	public String getRay_title() {
		return ray_title;
	}
	public void setRay_title(String ray_title) {
		this.ray_title = ray_title;
	}
	public String getRay_zs() {
		return ray_zs;
	}
	public void setRay_zs(String ray_zs) {
		this.ray_zs = ray_zs;
	}
	public String getRay_tipt() {
		return ray_tipt;
	}
	public void setRay_tipt(String ray_tipt) {
		this.ray_tipt = ray_tipt;
	}
	public String getRay_des() {
		return ray_des;
	}
	public void setRay_des(String ray_des) {
		this.ray_des = ray_des;
	}
	public String getDate01_date() {
		return date01_date;
	}
	public void setDate01_date(String date01_date) {
		this.date01_date = date01_date;
	}
	public String getDate01_dayPictureUrl() {
		return date01_dayPictureUrl;
	}
	public void setDate01_dayPictureUrl(String date01_dayPictureUrl) {
		this.date01_dayPictureUrl = date01_dayPictureUrl;
	}
	public String getDate01_nightPictureUrl() {
		return date01_nightPictureUrl;
	}
	public void setDate01_nightPictureUrl(String date01_nightPictureUrl) {
		this.date01_nightPictureUrl = date01_nightPictureUrl;
	}
	public String getDate01_wind() {
		return date01_wind;
	}
	public void setDate01_wind(String date01_wind) {
		this.date01_wind = date01_wind;
	}
	public String getDate01_temperature() {
		return date01_temperature;
	}
	public void setDate01_temperature(String date01_temperature) {
		this.date01_temperature = date01_temperature;
	}
	public String getDate02_date() {
		return date02_date;
	}
	public void setDate02_date(String date02_date) {
		this.date02_date = date02_date;
	}
	public String getDate02_dayPictureUrl() {
		return date02_dayPictureUrl;
	}
	public void setDate02_dayPictureUrl(String date02_dayPictureUrl) {
		this.date02_dayPictureUrl = date02_dayPictureUrl;
	}
	public String getDate02_nightPictureUrl() {
		return date02_nightPictureUrl;
	}
	public void setDate02_nightPictureUrl(String date02_nightPictureUrl) {
		this.date02_nightPictureUrl = date02_nightPictureUrl;
	}
	public String getDate02_wind() {
		return date02_wind;
	}
	public void setDate02_wind(String date02_wind) {
		this.date02_wind = date02_wind;
	}
	public String getDate02_temperature() {
		return date02_temperature;
	}
	public void setDate02_temperature(String date02_temperature) {
		this.date02_temperature = date02_temperature;
	}
	public String getDate03_date() {
		return date03_date;
	}
	public void setDate03_date(String date03_date) {
		this.date03_date = date03_date;
	}
	public String getDate03_dayPictureUrl() {
		return date03_dayPictureUrl;
	}
	public void setDate03_dayPictureUrl(String date03_dayPictureUrl) {
		this.date03_dayPictureUrl = date03_dayPictureUrl;
	}
	public String getDate03_nightPictureUrl() {
		return date03_nightPictureUrl;
	}
	public void setDate03_nightPictureUrl(String date03_nightPictureUrl) {
		this.date03_nightPictureUrl = date03_nightPictureUrl;
	}
	public String getDate03_wind() {
		return date03_wind;
	}
	public void setDate03_wind(String date03_wind) {
		this.date03_wind = date03_wind;
	}
	public String getDate03_temperature() {
		return date03_temperature;
	}
	public void setDate03_temperature(String date03_temperature) {
		this.date03_temperature = date03_temperature;
	}
	public String getDate04_date() {
		return date04_date;
	}
	public void setDate04_date(String date04_date) {
		this.date04_date = date04_date;
	}
	public String getDate04_dayPictureUrl() {
		return date04_dayPictureUrl;
	}
	public void setDate04_dayPictureUrl(String date04_dayPictureUrl) {
		this.date04_dayPictureUrl = date04_dayPictureUrl;
	}
	public String getDate04_nightPictureUrl() {
		return date04_nightPictureUrl;
	}
	public void setDate04_nightPictureUrl(String date04_nightPictureUrl) {
		this.date04_nightPictureUrl = date04_nightPictureUrl;
	}
	public String getDate04_wind() {
		return date04_wind;
	}
	public void setDate04_wind(String date04_wind) {
		this.date04_wind = date04_wind;
	}
	public String getDate04_temperature() {
		return date04_temperature;
	}
	public void setDate04_temperature(String date04_temperature) {
		this.date04_temperature = date04_temperature;
	}
	
	
}
