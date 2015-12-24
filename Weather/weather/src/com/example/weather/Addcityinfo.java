package com.example.weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class Addcityinfo extends Activity implements 
    SearchView.OnQueryTextListener{

	String msggmainfo;
	String cityCode="";
	ListView lv1;
	SearchView sw1;
	GridView gv_hotcity;
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
	List<CWeather> clist;
	
    ArrayList<HashMap<String,Object>> mylist=new ArrayList<HashMap<String,Object>>();
    ArrayList<String> mylist2=new ArrayList<String>();
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addcityinfo);
		lv1=(ListView) this.findViewById(R.id.listView1);
		sw1=(SearchView) this.findViewById(R.id.searchView1);
		gv_hotcity=(GridView) this.findViewById(R.id.gv_hotcity);
		
		
		lv1.setTextFilterEnabled(true);
		sw1.setOnQueryTextListener((OnQueryTextListener) this);
        sw1.setSubmitButtonEnabled(false);
         
        mylist2.add("上海");
        mylist2.add("广州");
        mylist2.add("深圳");
        mylist2.add("成都");
        mylist2.add("乐山");
        mylist2.add("武汉");
        mylist2.add("南京");
        mylist2.add("沈阳");
        mylist2.add("北京");
        mylist2.add("长沙");
        mylist2.add("西安");
        mylist2.add("杭州");
        mylist2.add("青岛");
        mylist2.add("天津");
        mylist2.add("昆明");
        mylist2.add("福州");
        mylist2.add("厦门");
        mylist2.add("桂林");
        mylist2.add("大连");
        mylist2.add("三亚");
        ArrayAdapter<String> myada=new ArrayAdapter<String>(Addcityinfo.this,android.R.layout.simple_list_item_1
        		,mylist2);
		
        gv_hotcity.setAdapter(myada);
		
        gv_hotcity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				String str=mylist2.get(pos);
				
				mydbhelper dbh=new mydbhelper(Addcityinfo.this);
				SQLiteDatabase sdb=dbh.getReadableDatabase();
				
				String[] project=new String[]{"_city_code"};
				Cursor cur= sdb.query("city_info", project, "_city1=?", new String[]{str}, null, null, null);
				while(cur.moveToNext())
				{
					cityCode=cur.getString(0);
				}
				
				welcome();			
	
	        	dbh.close();
			}
		});  //热门城市选择.
        
        Button btn_back=(Button) this.findViewById(R.id.addcity_back);
        btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			Intent in=new Intent(Addcityinfo.this,Addcity.class);
			startActivity(in);
			finish();
			}
		});
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.addcityinfo, menu);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String cityname) {
		// TODO Auto-generated method stub
		
		if(cityname.length()!=0)
		{
		
		cityname="%"+cityname+"%";
		mydbhelper dbh=new mydbhelper(Addcityinfo.this);		
		SQLiteDatabase sdb=dbh.getReadableDatabase();
		
		String[] project=new String[]{"_city1","_province"};
		
		Cursor cur= sdb.query("city_info", project, "_province like ?", new String[]{cityname}, null, null, null);
		Cursor cur1= sdb.query("city_info", project, "_city1 like ?", new String[]{cityname}, null, null, null);
		if(cur !=null)
		{
			if(cur.getCount()==0)
			{
				
			}
		}
		
		String str1;
		String str2;
		mylist.clear();
		while(cur.moveToNext())
		{
			HashMap<String,Object> map1=new HashMap<String,Object>();
			str1=cur.getString(0);
			str2=cur.getString(1);
			map1.put("name", str2+" - ");
	        map1.put("name1", str1);
	        mylist.add(map1);
		}
		if(cur1 !=null)
		{
			if(cur1.getCount()==0)
			{
				;
			}
		}
		
		while(cur1.moveToNext())
		{
			HashMap<String,Object> map1=new HashMap<String,Object>();
			str1=cur1.getString(0);
			str2=cur1.getString(1);
			map1.put("name", str2+" - ");
	        map1.put("name1", str1);
	        mylist.add(map1);
		}
		SimpleAdapter myadapter=new SimpleAdapter(this,mylist,R.layout.searchlist,
				new String[]{"name","name1"},new int[]{R.id.textView1,R.id.textView2});
		
		lv1.setAdapter(myadapter);	
		
		gv_hotcity.setVisibility(View.INVISIBLE);
		lv1.setVisibility(View.VISIBLE);
		
		}
		else
		{
			gv_hotcity.setVisibility(View.VISIBLE);
			lv1.setVisibility(View.INVISIBLE);
		}
		
		
		lv1.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				// TODO Auto-generated method stub
				HashMap map=mylist.get(pos);

				String str=(String)map.get("name1");
				mydbhelper dbh=new mydbhelper(Addcityinfo.this);
				SQLiteDatabase sdb=dbh.getReadableDatabase();
				
				String[] project=new String[]{"_city_code"};
				Cursor cur= sdb.query("city_info", project, "_city1=?", new String[]{str}, null, null, null);
				while(cur.moveToNext())
				{
					cityCode=cur.getString(0);
				}			
				//获取天气.				
				welcome();			
	        	dbh.close();
		  		}
			});
		return false;
	}   //搜索城市.

	@Override
	public boolean onQueryTextSubmit(String cityname) {
		// TODO Auto-generated method stub
		
		return false;
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
	  						//String cityCode="1";//demo.取大连的邮编号。此处可以做成变量取前台的输入值。
	  						//定义访问网络服务器的字符中。
	  						String weatherUrl="http://api.k780.com:88/?app=weather.future&weaid="+cityCode+ 
	  								"&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
	  						//调用某个方法去连接服务器。
	  						String wgsoninfo=connWeb(weatherUrl);
	  						try {
								Thread.sleep(0);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	  						Message msg=new Message();//给主线程发的消息。
	  						msg.obj=wgsoninfo;//把结果定义在obj属性发送给主线程。
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
			  			System.out.println("main Thread:"+msggmainfo);
			  		
			  			//第四步，解析得到的数据。最好是封装解析方法。
			  		clist=getGsonList(msggmainfo);
			  			
			  		//用迭代的方式来取值。
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
			  			
			  			mydbhelper dbh=new mydbhelper(Addcityinfo.this);
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
									System.out.println("无........................................");
									sdb.insert("city_weather", null,cv);
								}
								else
									sdb.update("city_weather", cv, "city_name=?",new String[]{cw_cityname});
							}
							
						
			  			System.out.println("........."+cw_cityname);
			  			System.out.println("........."+cw_weather);
			  		}//if.
			  		begin();
			  		}//handlerMessage.
			  	};//whandler.
	
	public void begin(){
		Intent in1 = new Intent(Addcityinfo.this,Addcity.class);
		in1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    	startActivity(in1);	   
    	Addcityinfo.this.finish();

	}//begin.
	
	public List<CWeather> getGsonList(String msg){
		List<CWeather> mlist=new ArrayList<CWeather>();
		
		try {
			JSONObject job=new JSONObject(msg);//JSONObject是万能的。
		//	JSONObject wjob=job.getJSONObject("result");//得到job这个json对象中的第一个key.
			 JSONArray wjob= job.getJSONArray("result");
			//就能得到其中的每一个key和value.
			//依次的把其中的每一个key和value取出,充血到实体中。
			CWeather cw=new CWeather();
			
			cw.setCityid(wjob.getJSONObject(0).getString("weaid"));//set的值是通过wjob的get（key）得到的value.
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
			
			//实体加入集合。
			mlist.add(cw);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//把字符串创建成json对象。
		
		return mlist;
	}//method
	
	
	//连接服务器的方法。
  	private String connWeb(String url){
  		String str="";
  		try {
  		HttpPost request=new HttpPost(url);//get/post.定义get方法的连接请求对象。
  		//定义连接客户端对象。
  		HttpClient httpClient=new DefaultHttpClient();
  		//用连接客户端对象去访问连接请求。得到的结果是一个服务器返回的响应结果对象。
  		HttpResponse response=httpClient.execute(request);
  		//判断连接结果是否正常完整的连接成功了。用http的状态码来判断。正常的状态码是200.
  		if(response.getStatusLine().getStatusCode() == 200){
  			//如果连接成功了。把结果对象中的值取出，并转化成字符串。
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
  		return str;//此方法返回连接到的结果。
  	}//connWeb.


}
