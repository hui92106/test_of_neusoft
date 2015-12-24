package com.example.weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class IndexActivity extends Activity {

	String msggmainfo;
	List<CWeather> clist;
	
	String cw_citycode;
	String cw_cityname;
	String cw_temp;
	String cw_weather;
	String cw_wind_direction;
	String cw_wind_power;
	String cw_days;
	String cw_weeks;   //today.
	String cw_tomoweek;
	String cw_tomotemp;
	String cw_tomoweath;
	String cw_daftomoweek;
	String cw_daftomotemp;
	String cw_daftomoweath;
	
	String code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//�õ��������
				Window win=getWindow();
				//�ô����ں��������ȫ��
				win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				requestWindowFeature(Window.FEATURE_NO_TITLE);//����ǰ��activityȥ�����⡣
				setContentView(R.layout.activity_index);
				
				//�ô���ķ�ʽ��ָ��һ��ͼƬ��
				//iv.setImageDrawable(drawable);
				
				welcome();
	}
	
	public void welcome(){
		//�ص㣺�̻߳��ơ�
		//��һ���������̶߳���
		new Thread(
				new Runnable(){//�������߳�ʵ���ࡣ

					@Override
					public void run() {//�ڶ����̵߳����ݴ��롣
						// TODO Auto-generated method stub
						String citycode;
						
						mydbhelper dbh=new mydbhelper(IndexActivity.this);		
						SQLiteDatabase sdb=dbh.getReadableDatabase();
						
						String[] project=new String[]{"city_code"};
						Cursor cur= sdb.query("city_weather", project, null, null, null, null, null);
						if(cur !=null)
						{
							if(cur.getCount()==0)
							{
								Toast.makeText(IndexActivity.this, "����ӳ���", 1).show();
							}
						}
						
						while(cur.moveToNext())
						{
							citycode=cur.getString(0);							
						
						
						//�����������й���������ȥȡ�����ݡ�
	  						//String cityCode="1";//demo.ȡ�������ʱ�š��˴��������ɱ���ȡǰ̨������ֵ��
	  						//�������������������ַ��С�
							System.out.println("���б���"+citycode);
	  						String weatherUrl="http://api.k780.com:88/?app=weather.future&weaid="+citycode+ 
	  								"&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
	  						//����ĳ������ȥ���ӷ�������
	  						String wgsoninfo=connWeb(weatherUrl);
						
	  						try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	  						Message msg=new Message();//�����̷߳�����Ϣ��
	  						msg.obj=wgsoninfo;//�ѽ��������obj���Է��͸����̡߳�
	  						whandler.sendMessage(msg);//�����߳��е���Ϣ���󷢸������̡߳�	
	  						
						}
						dbh.close();
						begin();
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
			  			//System.out.println("main Thread:"+msggmainfo);
			  		
			  			//���Ĳ��������õ������ݡ�����Ƿ�װ����������
			  		clist=getGsonList(msggmainfo);
			  			
			  		//�õ����ķ�ʽ��ȡֵ��
			  		Iterator it=clist.iterator();
			  		if(it.hasNext()){
			  			CWeather cw=(CWeather)it.next();
			  			
			  			cw_citycode=cw.getCityid();
			  			cw_cityname=cw.getCity();
			  			cw_weather=cw.getWeather();
			  			cw_temp=cw.getTemperature();
			  			cw_wind_direction=cw.getWind_dir();
			  			cw_wind_power=cw.getWind_pow();
			  			cw_days=cw.getDays();
			  			cw_weeks=cw.getWeek();  //today.
			  			cw_tomoweek=cw.getTomo_week();
			  			cw_tomotemp=cw.getTomo_temp();
			  		    cw_tomoweath=cw.getTomo_weather();
			  			cw_daftomoweek=cw.getDaftomo_week();
			  			cw_daftomotemp=cw.getDaftomo_temp();
			  			cw_daftomoweath=cw.getDaftomo_weather();
			  			
			  			mydbhelper dbh=new mydbhelper(IndexActivity.this);
						SQLiteDatabase sdb=dbh.getReadableDatabase();
						
						ContentValues cv=new ContentValues();
						cv.put("city_name",cw_cityname);
						cv.put("city_code",cw_citycode);
						cv.put("city_todaytem",cw_temp);
						cv.put("city_weather",cw_weather);
						cv.put("city_wdir",cw_wind_direction);
						cv.put("city_wpow",cw_wind_power);
						cv.put("city_days",cw_days);
						cv.put("city_week",cw_weeks);
						cv.put("city_tomoweek",cw_tomoweek);
						cv.put("city_tomotem",cw_tomotemp);
						cv.put("city_yomoweather",cw_tomoweath);
						cv.put("city_daftomoweek",cw_daftomoweek);
						cv.put("city_daftomotem",cw_daftomotemp);
						cv.put("city_daftomoweather",cw_daftomoweath);
						
							String[] project=new String[]{"city_name"};
							Cursor cur= sdb.query("city_weather", project, "city_name=?", new String[]{cw_cityname}, null, null, null);
							
							if(cur !=null)
							{
								if(cur.getCount()==0)
								{
									System.out.println("��........................................");
									sdb.insert("city_weather", null,cv);
								}
								else
								{
									sdb.update("city_weather", cv, "city_name=?",new String[]{cw_cityname});
									System.out.println("����Ĭ�ϳ���");
								}
							}
							
			  			System.out.println("........."+cw_cityname);
			  			System.out.println("........."+cw_weather);
			  			dbh.close();
			  		}//if.
			  	//	begin();
			  		
			  		}//handlerMessage.
			  	};//whandler.
	
	public void begin(){
		Intent in=new Intent(IndexActivity.this,MainActivity.class);
		startActivity(in);		
		IndexActivity.this.finish();
	}//begin.
	

	//���Ĳ�����װһ�������ķ�����
	public List<CWeather> getGsonList(String msg){
		List<CWeather> mlist=new ArrayList<CWeather>();
		
		try {
			JSONObject job=new JSONObject(msg);//JSONObject�����ܵġ�
		//	JSONObject wjob=job.getJSONObject("result");//�õ�job���json�����еĵ�һ��key.
			 JSONArray wjob= job.getJSONArray("result");
			//���ܵõ����е�ÿһ��key��value.
			//���εİ����е�ÿһ��key��valueȡ��,��Ѫ��ʵ���С�
			CWeather cw=new CWeather();
			
			cw.setCityid(wjob.getJSONObject(0).getString("weaid"));//set��ֵ��ͨ��wjob��get��key���õ���value.
			cw.setCity(wjob.getJSONObject(0).getString("citynm"));
			cw.setTemperature(wjob.getJSONObject(0).getString("temperature"));
			cw.setWeather(wjob.getJSONObject(0).getString("weather"));
			cw.setWind_dir(wjob.getJSONObject(0).getString("wind_direction"));
			cw.setWind_pow(wjob.getJSONObject(0).getString("wind_power"));
			cw.setDays(wjob.getJSONObject(0).getString("days"));
			cw.setWeek(wjob.getJSONObject(0).getString("week"));
			cw.setTomo_week(wjob.getJSONObject(1).getString("week"));
			cw.setTomo_temp(wjob.getJSONObject(1).getString("temperature"));
			cw.setTomo_weather(wjob.getJSONObject(1).getString("weather"));
			cw.setDaftomo_week(wjob.getJSONObject(2).getString("week"));
			cw.setDaftomo_temp(wjob.getJSONObject(2).getString("temperature"));
			cw.setDaftomo_weather(wjob.getJSONObject(2).getString("weather"));
			//cw.setTemp2(wjob.getString("temp_low"));
        
			//cw.setDays(wjob.getString("days"));
			//cw.setWeek(wjob.getString("week"));
			
			//ʵ����뼯�ϡ�
			mlist.add(cw);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//���ַ���������json����
		
		return mlist;
	}//method
	
	//���ӷ������ķ�����
  	private String connWeb(String url){
  		String str="";
  		try {
  		HttpPost request=new HttpPost(url);//get/post.����get�����������������
  		//�������ӿͻ��˶���
  		HttpClient httpClient=new DefaultHttpClient();
  		//�����ӿͻ��˶���ȥ�����������󡣵õ��Ľ����һ�����������ص���Ӧ�������
  		HttpResponse response=httpClient.execute(request);
  		//�ж����ӽ���Ƿ��������������ӳɹ��ˡ���http��״̬�����жϡ�������״̬����200.
  		if(response.getStatusLine().getStatusCode() == 200){
  			//������ӳɹ��ˡ��ѽ�������е�ֵȡ������ת�����ַ�����
  			str=EntityUtils.toString(response.getEntity());
  			System.out.println("response str:"+str);
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

}
