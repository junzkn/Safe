package jun.safe.engine;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.util.List;

/**
 * Created by zkn on 2018/3/22.
 */

public class ProcessInfoProvider {

    public static String getProcessCount(Context context){
        UsageStatsManager m=(UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time =System.currentTimeMillis()-24*60*60*1000;
        List<UsageStats> usageStats = null;
        String packageName = null;
        long totalTimeInForeground = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, time, System.currentTimeMillis());
            packageName = usageStats.get(2).getPackageName();
            totalTimeInForeground = usageStats.get(2).getTotalTimeInForeground();
        }

//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(10);

        return packageName+":"+totalTimeInForeground ;
    }


    public static long getAvailSpace(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
       return  memoryInfo.availMem ;
    }

    public static long getTotalSpace(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return  memoryInfo.totalMem ;
    }

}
