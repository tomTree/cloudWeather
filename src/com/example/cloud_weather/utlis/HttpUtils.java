package com.example.cloud_weather.utlis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;




public class HttpUtils {
	
	public static void sendHttpRequest(final String address,final HttpCallBackListener listener){
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestProperty("apikey","FVOnl3OlZCZOFAZa8k5uWs0CabiCd5YR");
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					//设置自己的apiKey
					
					
					InputStream in=connection.getInputStream();
					
					BufferedReader reader=new BufferedReader(new InputStreamReader(in,"utf-8"));
					
					String line="";
					
					StringBuilder response=new StringBuilder();
					
					while((line=reader.readLine())!=null&&(!line.equals(""))){
						response.append(line);
						response.append("\n");
					}
					
					if(listener!=null){
						listener.finish(response.toString());
					}
				
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if(listener!=null){
						listener.onError(e);
					}
					e.printStackTrace();
					
				} finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
					
			}}).start();
	}
}
