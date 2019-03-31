package jun.safe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by zkn on 2018/3/19.
 */

public class AddressDao {
    private static String path = "data/data/jun.safe/files/address.db" ;
    private static String address = "未知号码" ;
    public static String getAddress(String phone){
        address = "未知号码" ;
        if(phone.matches("^[0-9]{0,11}$")){
            if(phone.length()<11)phone += String.format("%1$0"+(11-phone.length())+"d",0) ;
            phone = phone.substring(0,7) ;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);
            if(cursor.moveToNext()){
                String outkey = cursor.getString(0) ;
                Cursor indexCursor = db.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);
                if(indexCursor.moveToNext()) {
                    address = indexCursor.getString(0);
                }else {
                    address = "未知号码" ;
                }
            }
        }else {
            address = "未知号码" ;
        }
        return address ;
    }


}
