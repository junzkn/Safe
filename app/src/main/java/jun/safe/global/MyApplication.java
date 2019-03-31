package jun.safe.global;

import android.app.Application;
import android.util.Log;

/**
 * Created by zkn on 2018/3/24.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                Log.e("ROOT","eeeeeeee");
                System.exit(0);
            }
        });

    }
}
