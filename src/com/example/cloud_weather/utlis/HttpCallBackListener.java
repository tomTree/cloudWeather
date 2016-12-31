package com.example.cloud_weather.utlis;

public interface HttpCallBackListener {
	void finish(String response);
	void onError(Exception e);
}
