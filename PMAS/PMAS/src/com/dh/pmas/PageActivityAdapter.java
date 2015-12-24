package com.dh.pmas;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/*
 *
 * author : deng_hui_long 
 * Date   : 2013-10-27
 *
 */
public class PageActivityAdapter extends PagerAdapter {
	public ArrayList<View> ay = null;

	public PageActivityAdapter(ArrayList<View> ay) {
		this.ay = ay;
	}

	// 是获取当前窗体界面数
	@Override
	public int getCount() {

		return ay.size();
	}

	// 判断是否由对象生成 的view ,
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	// 是从ViewGroup中移出当前View
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ViewPager viewPager = (ViewPager) container;
		viewPager.removeView(ay.get(position));
	}

	// 添加 并返回一个对象
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ViewPager viewPager = (ViewPager) container;
		viewPager.addView(ay.get(position));
		return ay.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
