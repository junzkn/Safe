package jun.safe.utils;

import android.view.View;

import java.util.Calendar;

/**
 * Created by zkn on 2018/3/16.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;


    /**
     * 点击事件，1秒内不能双击
     * @param v
     */
    protected abstract void onNoDoubleClick(View v);
    @Override

    public void onClick(View v) {
        long currentTime = System.currentTimeMillis() ;
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }




}
