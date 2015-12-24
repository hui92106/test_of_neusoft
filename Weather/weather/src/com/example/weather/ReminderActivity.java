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
		//�ص㣺�̻߳��ơ�
		//��һ���������̶߳���
		new Thread(
				new Runnable(){//�������߳�ʵ���ࡣ

					@Override
					public void run() {//�ڶ����̵߳����ݴ��롣
						// TODO Auto-generated method stub
							//�����������й���������ȥȡ�����ݡ�
	  						//�������������������ַ��С�
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
	  						//����ĳ������ȥ���ӷ�������
	  						try {
								connWeb(weatherUrl);
							} catch (XmlPullParserException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	  						Message msg=new Message();//�����̷߳�����Ϣ��
	  						msg.obj=msggmainfo;//�ѽ��������obj���Է��͸����̡߳�
	  						whandler.sendMessage(msg);//�����߳��е���Ϣ���󷢸������̡߳�
					//run.
					}	
				}//runnable.
				).start();
	}
	
				 //�ڶ�����
			  	Handler whandler=new Handler(){//handler������ui�̡߳�

			  		@Override
			  		public void handleMessage(Message msg) {//�õ���̨�̵߳���Ϣ��
			  			// TODO Auto-generated method stub
			  			super.handleMessage(msg);
			  			msggmainfo=(String)msg.obj;//���̻߳�ȡ���̴߳��ݹ��������ݡ�
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
	

  	
  	//���ӷ������ķ�����
  	private String connWeb(String url) throws XmlPullParserException{
  		String str="";
  		try {
  		HttpGet request=new HttpGet(url);//get/post.����get�����������������
  		//�������ӿͻ��˶���
  		HttpClient httpClient=new DefaultHttpClient();
  		 HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();  
  		//�����ӿͻ��˶���ȥ�����������󡣵õ��Ľ����һ�����������ص���Ӧ�������
  		HttpResponse response=httpClient.execute(request);
  		//�ж����ӽ���Ƿ��������������ӳɹ��ˡ���http��״̬�����жϡ�������״̬����200.
  		System.out.println("code: "+response.getStatusLine().getStatusCode());
  		if(response.getStatusLine().getStatusCode() == 200){
  			//������ӳɹ��ˡ��ѽ�������е�ֵȡ������ת�����ַ�����
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
  		return str;//�˷����������ӵ��Ľ����
  	}//connWeb.

  	
  	//���Ĳ�����װһ�������ķ�����
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

