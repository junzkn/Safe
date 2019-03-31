package jun.safe.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by zkn on 2018/3/19.
 */

public class ServiceUtil {
    public static boolean isRunning(Context context , String serviceName){

        ActivityManager mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = mAm.getRunningServices(10);
        for(ActivityManager.RunningServiceInfo info : runningServices){
            String className = info.service.getClassName();
            if(className.equals(serviceName)) {
                return true ;
            }
        }
        return false ;
    }

}
