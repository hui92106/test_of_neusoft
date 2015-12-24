package com.pmas.daylife;

import java.util.ArrayList;

import com.dh.pmas.PageActivityAdapter;
import com.dh.pmas.R;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class daylifeActivity extends Activity {

	private Context context = null;
	private LocalActivityManager manger = null;
	// 标题
	private ViewPager viewPager = null;
	//private ImageView imageView = null;
	private ImageView imageView1 = null;
	private ImageView imageView2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daylife);
		context = daylifeActivity.this;// 获取上下文对象
		manger = new LocalActivityManager(this, true);// 本地的Activity管理者，用于获取View
		manger.dispatchCreate(savedInstanceState);
		// 初始化标题
		imageView1 = (ImageView) this.findViewById(R.id.imageView1);
		imageView2 = (ImageView) this.findViewById(R.id.imageView2);
		imageView1.setBackgroundResource(R.drawable.type2);
		imageView2.setBackgroundResource(R.drawable.message1);
		imageView1.setOnClickListener(new OnClickListenerImp(0));
		imageView2.setOnClickListener(new OnClickListenerImp(1));


		initPager();
	}
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		onCreate(null);
	}
	public void initPager() {
		viewPager = (ViewPager) this.findViewById(R.id.viewPager_01);
		ArrayList<View> ay = new ArrayList<View>();

		Intent intent1 = new Intent(context, daylifeTypeActivity.class);
		ay.add(getView("daylifeTypeActivity", intent1));

		Intent intent2 = new Intent(context, daylifeDaylifeActivity.class);
		ay.add(getView("daylifeDaylifeActivity", intent2));

		viewPager.setAdapter(new PageActivityAdapter(ay));
		viewPager.setCurrentItem(0);// 设置默认的页面
		viewPager.setOnPageChangeListener(new PageChangeListenerImp());

	}

	public View getView(String id, Intent intent) {
		return manger.startActivity(id, intent).getDecorView();
	}

	private class PageChangeListenerImp implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:System.out.println("aaa");
			imageView1.setBackgroundResource(R.drawable.type2);
			imageView2.setBackgroundResource(R.drawable.message1);
				break;
			case 1:System.out.println("bbb");
			
			imageView1.setBackgroundResource(R.drawable.type1);
			imageView2.setBackgroundResource(R.drawable.message2);
				
			}
		}
	}


	// 点击标题事件
	private class OnClickListenerImp implements OnClickListener {
		private int index = 0;

		public OnClickListenerImp(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}

	}
}
