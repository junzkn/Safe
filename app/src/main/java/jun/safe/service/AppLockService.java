package jun.safe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

/**
 * Created by zkn on 2018/3/24.
 */

public class AppLockService extends Service {
    private boolean isWatch;
    private UsageStatsManager sUsageStatsManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isWatch = true ;
        Log.e("service","start");
        watch() ;
        super.onCreate();
    }

    private void watch() {
        new Thread(){
            @Override
            public void run() {

//                    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                    List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(10);

                    UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                    Calendar calendar = Calendar.getInstance();
                    long endTime = calendar.getTimeInMillis();
                calendar.add(Calendar.YEAR, -1);
                    long startTime = calendar.getTimeInMillis();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
                        for( UsageStats u: usageStatsList){
                            Log.e("task",""+u.getPackageName());
                        }

                    }





                super.run();
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isWatch = false ;
    }
}
