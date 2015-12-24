package com.example.weather;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;



import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReminderActivity extends Activity {
	String msggmainfo;
	List<ReminderInfo> clist;
	TextView clothing;
	TextView goout;
	TextView cold;
	TextView exercise;
	TextView clothing_index;
	TextView goout_index;
	TextView cold_index;
	TextView exercise_index;
	TextView city;
	Button btn_getweather;
	String city_name;

	
	ReminderInfo ri;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        
        System.out.println("create reminder");
        welcome();
        
        clothing = (TextView) this.findViewById(R.id.clothing);
        cold = (TextView) this.findViewById(R.id.cold);
        exercise = (TextView) this.findViewById(R.id.exercise);
        goout = (TextView) this.findViewById(R.id.goout);
        city = (TextView) this.findViewById(R.id.reminder_city);
        clothing_index = (TextView) this.findViewById(R.id.clothing_index);
        cold_index = (TextView) this.findViewById(R.id.cold_index);
        exercise_index = (TextView) this.findViewById(R.id.exercise_index);
        goout_index = (TextView) this.findViewById(R.id.goout_index);
        
    }
    
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		onCreate(null);
	}
  

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		onCreate(null);
	}

	public void welcome(){
		//重点：线程机制。
		//第一步，创建线程对象。
		new Thread(
				new Runnable(){//匿名的线程实现类。

					@Override
					public void run() {//第二个线程的内容代码。
						// TODO Auto-generated method stub
							//第三步，到中国天气网上去取得数据。
	  						//定义访问网络服务器的字符中。
						 mydbhelper dbh=new mydbhelper(ReminderActivity.this);
				 	  		SQLiteDatabase sdb=dbh.getReadableDatabase();
				 	  	city_name="";
				 		Cursor cur= sdb.query("city_info", new String[]{"_city1"}, "_city_code=?", new String[]{mycode._id}, null, null, null);
				 		System.out.println("query");
				 		if(cur.moveToNext()){
							city_name=cur.getString(0);
							System.out.println("city "+city_name);
						}
						dbh.close();
				 		String citycode = "";
							try {
								citycode = java.net.URLEncoder.encode(city_name,"gb2312");
							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							System.out.println(citycode);
	  						String weatherUrl="http://php.weather.sina.com.cn/xml.php?city="+citycode+"&ie=utf-8&password=DJOYnieT8234jlsK&day=0";
	  						//调用某个方法去连接服务器。
	  						try {
								connWeb(weatherUrl);
							} catch (XmlPullParserException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	  						Message msg=new Message();//给主线程发的消息。
	  						msg.obj=msggmainfo;//把结果定义在obj属性发送给主线程。
	  						whandler.sendMessage(msg);//把子线程中的消息对象发给了主线程。
					//run.
					}	
				}//runnable.
				).start();
	}
	
				 //第二步，
			  	Handler whandler=new Handler(){//handler对象是ui线程。

			  		@Override
			  		public void handleMessage(Message msg) {//得到后台线程的消息。
			  			// TODO Auto-generated method stub
			  			super.handleMessage(msg);
			  			msggmainfo=(String)msg.obj;//主线程获取子线程传递过来的内容。
			  			clothing.setText(ri.getClothing());
			  			clothing_index.setText(ri.getClothing_index());
		            	cold.setText(ri.getCold());
		            	cold_index.setText(ri.getCold_index());
		            	exercise.setText(ri.getExercise());
		            	exercise_index.setText(ri.getExercise_index());
		            	goout.setText(ri.getGoout());
		            	goout_index.setText(ri.getGoout_index());
		            	
		            	city.setText(city_name);
			  		}//handlerMessage.
			  	};//whandler.
	

  	
  	//连接服务器的方法。
  	private String connWeb(String url) throws XmlPullParserException{
  		String str="";
  		try {
  		HttpGet request=new HttpGet(url);//get/post.定义get方法的连接请求对象。
  		//定义连接客户端对象。
  		HttpClient httpClient=new DefaultHttpClient();
  		 HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();  
  		//用连接客户端对象去访问连接请求。得到的结果是一个服务器返回的响应结果对象。
  		HttpResponse response=httpClient.execute(request);
  		//判断连接结果是否正常完整的连接成功了。用http的状态码来判断。正常的状态码是200.
  		System.out.println("code: "+response.getStatusLine().getStatusCode());
  		if(response.getStatusLine().getStatusCode() == 200){
  			//如果连接成功了。把结果对象中的值取出，并转化成字符串。
  			//str=EntityUtils.toString(response.getEntity());
  			InputStream in = con.getInputStream();
  			readXml(in);
  			//System.out.println("response str:"+str);
  		}//if.
  		} catch (ClientProtocolException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		return str;//此方法返回连接到的结果。
  	}//connWeb.

  	
  	//第四步，封装一个解析的方法。
  	public ReminderInfo readXml(InputStream in) throws XmlPullParserException, IOException {
  		
  		 XmlPullParser pullParser = Xml.newPullParser();
  	    
  		 pullParser.setInput(in, "UTF-8");  
         int event = pullParser.getEventType();  
         while (event != XmlPullParser.END_DOCUMENT) {  
             switch (event) {  
             case XmlPullParser.START_DOCUMENT:  
                 ri = new ReminderInfo(); 
                 break;  
             case XmlPullParser.START_TAG:  
        
            	 if ("chy_shuoming".equals(pullParser.getName())) {  
                     
                	 ri.setClothing(pullParser.nextText());
                    
                 }  
                 if ("pollution_s".equals(pullParser.getName())) {  
                    
                	 ri.setGoout(pullParser.nextText());
                	
                 }  
                
                 if ("gm_s".equals(pullParser.getName())) {  
                     
                	 ri.setCold(pullParser.nextText());
                   
                 }  

                 if ("yd_s".equals(pullParser.getName())) {  
                     
                	 ri.setExercise(pullParser.nextText());
                    
                 }  
                 if ("chy".equals(pullParser.getName())) {  
                     
                	 ri.setClothing_index(pullParser.nextText());
                    
                 }  
                 if ("pollution_l".equals(pullParser.getName())) {  
                    
                	 ri.setGoout_index(pullParser.nextText());
                	
                 }  
                
                 if ("gm_l".equals(pullParser.getName())) {  
                     
                	 ri.setCold_index(pullParser.nextText());
                   
                 }  

                 if ("yd_l".equals(pullParser.getName())) {  
                     
                	 ri.setExercise_index(pullParser.nextText());
                    
                 }  
                
                 break;  
             case XmlPullParser.END_TAG:  
                 if ("weather".equals(pullParser.getName())) {  
                     System.out.println("end"); 
                 }  
                 break;  
             }  
             event = pullParser.next();  
         }  
         return ri;  
   
		
  	}//method
  	



  
}

