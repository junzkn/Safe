package jun.safe.utils;

import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

/**
 * Created by zkn on 2018/3/16.
 */

public abstract class NoDoubleHomeItemClickListener implements AdapterView.OnItemClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(parent,view,position,id);
        }
    }

    protected abstract void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id);

}
