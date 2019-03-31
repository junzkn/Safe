package jun.safe.utils;

import android.provider.Settings;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

/**
 * Created by zkn on 2018/3/16.
 */

public abstract class DoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long startClickTime = 0;

    public abstract void onDoubleClick(View v);
    @Override

    public void onClick(View v) {
        if(startClickTime!=0){
            long endClickTime =System.currentTimeMillis();
            if(endClickTime-startClickTime<500){
                onDoubleClick(v) ;
            }
        }
        startClickTime = System.currentTimeMillis() ;

    }




}
