package com.gscdz.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    //TODO 可优化

    private  static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat onlyDate=new SimpleDateFormat("yyyy-MM-dd");
    public static String getSimpleDateFormat(Date date){
        if(date==null){
            return "";
        }else{
            return formatter.format(date).toString();
        }
    }

    public static String getOnlyDate(Date date){
        if(date==null){
            return "";
        }else{
            return onlyDate.format(date).toString();
        }
    }
}
