package jun.safe.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zkn on 2018/3/21.
 */

public class BlockOpenHelper extends SQLiteOpenHelper {
    public BlockOpenHelper(Context context ) {
        super(context, "block.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table block (id integer primary key autoincrement,phone varchar(20),mode varchar(10));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
