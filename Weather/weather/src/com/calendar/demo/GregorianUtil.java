package com.calendar.demo;

import java.util.Calendar;

/*
     对公历日期的处理类,判断某天是否是节假日
 */
public class GregorianUtil {
    private final static String[][] GRE_FESTVIAL = {
            //一月
            {"元旦", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    ""},
            //二月
            {"", "", "", "", "", "", "", "", "", "", "", "", "", "情人", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", ""},
            //三月
            {"", "", "", "", "", "", "", "妇女", "", "", "", "植树", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", ""},
            //四月
            {"愚人", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", ""},
            //五月
            {"劳动", "", "", "青年", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", ""},
            //六月
            {"儿童", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", ""},
            //七月
            {"建党", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", ""},
            //八月
            {"建军", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", ""},
            //九月
            {"", "", "", "", "", "", "", "", "", "教师", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", ""},
            //十月
            {"国庆", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", ""},
            //十一月
            {"", "", "", "", "", "", "", "", "", "", "光棍", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", "", "", "", "", ""},
            //十二月
            {"艾滋病", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "圣诞", "", "", "", "", "", ""},
    };
    private int mMonth;
    private int mDay;
    
    //获取月和日
    public GregorianUtil(Calendar calendar) {
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DATE);
    }
    
    //获取节假日
    public String getGremessage() {
        return GRE_FESTVIAL[mMonth][mDay - 1];
    }

}
