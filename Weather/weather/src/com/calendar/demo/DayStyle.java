package com.calendar.demo;

import java.util.Calendar;

/*�����ؼ���ʽ������
 */
public class DayStyle {
	private final static String[] vecStrWeekDayNames = getWeekDayNames();

	private static String[] getWeekDayNames() {
		String[] vec = new String[10];

		vec[Calendar.SUNDAY] = "����";
		vec[Calendar.MONDAY] = "��һ";
		vec[Calendar.TUESDAY] = "�ܶ�";
		vec[Calendar.WEDNESDAY] = "����";
		vec[Calendar.THURSDAY] = "����";
		vec[Calendar.FRIDAY] = "����";
		vec[Calendar.SATURDAY] = "����";
		
		return vec;
	}
	//����һ�ܵ�ÿ��
	public static String getWeekDayName(int iDay) {
		return vecStrWeekDayNames[iDay];
	}
	
	//���ù�����
	public static int getWeekDay(int index, int iFirstDayOfWeek) {
		int iWeekDay = -1;

		if (iFirstDayOfWeek == Calendar.MONDAY) {
			iWeekDay = index + Calendar.MONDAY;
			
			if (iWeekDay > Calendar.SATURDAY)
				iWeekDay = Calendar.SUNDAY;
		}

		if (iFirstDayOfWeek == Calendar.SUNDAY) {
			iWeekDay = index + Calendar.SUNDAY;
		}

		return iWeekDay;
	}
}
