package jun.safe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zkn on 2018/3/13.
 */

public class ToastUtil {
    /**
     *
     * @param context
     * @param msg
     */
    public static void show( Context context ,String msg ){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }
}
