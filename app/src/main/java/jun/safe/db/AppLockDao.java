package jun.safe.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jun.safe.bean.BlockBean;

/**
 * Created by zkn on 2018/3/21.
 */

public class AppLockDao {
    private AppLockOpenHelper mOpenHelper;
    private static AppLockDao appLockDao = null;

    private AppLockDao(Context context) {
        mOpenHelper = new AppLockOpenHelper(context);
    }


    public void insert(String packageName) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        db.execSQL("insert into applock(packageName) values(?);", new String[]{packageName});
        db.close();
    }

    public void delete(String packageName) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        db.execSQL("delete from applock where packageName=?;", new String[]{packageName});
        db.close();
    }



    public ArrayList<String> query() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select packageName from applock ", null);
        ArrayList<String> list = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0)) ;
            }
            cursor.close();
        }
        db.close();
        return list;
    }



    public static AppLockDao getInstance(Context context) {
        if (appLockDao == null)
            appLockDao = new AppLockDao(context);
        return appLockDao;
    }
}
