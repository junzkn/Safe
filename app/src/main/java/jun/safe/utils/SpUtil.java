package jun.safe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zkn on 2018/3/14.
 */

public class SpUtil {

    private static SharedPreferences sp;


    /**
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key , boolean value){
        if(sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit() ;
    }

    /**
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key , boolean defValue){
        if(sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue) ;
    }

    public static void putString(Context context, String key , String value){
        if(sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit() ;
    }


    public static String getString(Context context, String key , String defValue){
        if(sp==null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key,defValue) ;
    }

}
