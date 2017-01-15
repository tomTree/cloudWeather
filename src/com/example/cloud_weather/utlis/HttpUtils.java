package com.example.cloud_weather.utlis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.cloud_weather.db.CoolWeatherDB;

import android.content.Context;
import android.os.Environment;
import android.util.Log;




public class HttpUtils {
	
	private static CoolWeatherDB db;
	
	
	
	public static void sendHttpRequest(final String address,final HttpCallBackListener listener){
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection=null;
				BufferedReader reader=null;
				try {
					URL url=new URL(address);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					//设置自己的apiKey
					connection.connect();
					InputStream in=connection.getInputStream();
					reader=new BufferedReader(new InputStreamReader(in,"utf-8"));
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
						Log.e("这里发生了什么", "++++++++++++++"+e);
						listener.onError(e);
					}
					//e.printStackTrace();
					
					
				} finally{
					if(connection!=null){
						connection.disconnect();
						try {
							if(reader!=null)
							reader.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
					
			}}).start();
	}

	public static String getImageUrl(Context context,final String imageUrl,final HttpBitmapCallBack htbc) {
		

		String[] strArr=imageUrl.split("/");
		//白天或夜晚
		final String time=strArr[5];
		//图片名称
		final String imageName=strArr[6];
		
		db=CoolWeatherDB.getInstance(context);
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				HttpURLConnection connection=null;
				try {
					URL url=new URL(imageUrl+"?ak=FVOnl3OlZCZOFAZa8k5uWs0CabiCd5YR&mcode=B7:05:FF:A2:76:AD:6D:F7:3D:85:F8:AE:65:BF:16:77:EC:B8:63:B5;com.example.cloud_weather");
					connection=(HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(8000);
		
					connection.setReadTimeout(8000);
					connection.setRequestMethod("GET");
					
					InputStream in=connection.getInputStream();
					File file=new File(Environment.getExternalStorageDirectory(),time+"/"+imageName);
					//不存在则创建
					if(!file.exists()){
						file.createNewFile();
					}
		
					FileOutputStream fos=new FileOutputStream(file);
					
					byte[] car=new byte[10];
					int len=0;
					while((len=in.read(car))!=-1){
						fos.write(car, 0,len);
					}
					fos.flush();
					if(fos!=null){
						fos.close();
					}
					if(in!=null){
						in.close();
					}
					
					
					//执行顺利
					htbc.finish(db,imageUrl);
				} catch (Exception e) {
					htbc.onError(e);
					e.printStackTrace();
				}
					
			}}).start();
		
		
		
		
		return time+"/"+imageName;
	}
}
