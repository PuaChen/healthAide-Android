package com.eddy.healthaideandroid.util;


import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toastShort;
    private static Toast toastLong;

    public static Toast ShowShort(Context context, Object object){
        if(toastShort==null){
            toastShort = Toast.makeText(context,object+"", Toast.LENGTH_SHORT);
        }
        toastShort.setText(object+"");
        toastShort.show();
        return toastShort;
    }

    public static Toast ShowLong(Context context, Object object){
        if(toastLong==null){
            toastLong = Toast.makeText(context,object+"", Toast.LENGTH_LONG);
        }
        toastLong.setText(object+"");
        toastLong.show();
        return toastLong;
    }
}
