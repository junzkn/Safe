package jun.safe.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by zkn on 2018/3/17.
 */

public class LogUtil {

    public static void LogV(AppCompatActivity activity, String msg){
        Log.v(activity.getLocalClassName(),msg);
    }
    public static void LogD(AppCompatActivity activity, String msg){
        Log.d(activity.getLocalClassName(),msg);
    }
    public static void LogI(AppCompatActivity activity, String msg){
        Log.i(activity.getLocalClassName(),msg);
    }
    public static void LogW(AppCompatActivity activity, String msg){
        Log.w(activity.getLocalClassName(),msg);
    }
    public static void LogE(AppCompatActivity activity, String msg){
        Log.e(activity.getLocalClassName(),msg);
    }

}
