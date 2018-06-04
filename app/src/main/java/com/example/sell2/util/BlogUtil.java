package com.example.sell2.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class BlogUtil {

    public static  String getDate()
    {
        Long time=System.currentTimeMillis();
        Date date=new Date();
        date.setTime(time);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String result=simpleDateFormat.format(date);
        return result;
    }
    public static  String handletime(Date date,String pattern){
        //yyyy-MM-dd HH:mm:ss
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(pattern);
        String result=simpleDateFormat.format(date);

        return result;
    }
}
