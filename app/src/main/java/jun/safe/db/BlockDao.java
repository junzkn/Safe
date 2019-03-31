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

public class BlockDao {
    private BlockOpenHelper mOpenHelper;
    private static BlockDao blockDao = null;

    private BlockDao(Context context) {
        mOpenHelper = new BlockOpenHelper(context);
    }


    public void insert(BlockBean bean) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        db.execSQL("insert into block(phone,mode) values(?,?);", new String[]{bean.phone, bean.mode});
        db.close();
    }

    public void delete(String phone) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        db.execSQL("delete from block where phone=?;", new String[]{phone});
        db.close();
    }

    public void update(BlockBean bean) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        db.execSQL("update block set phone=? where mode=?;", new String[]{bean.phone, bean.mode});
        db.close();
    }


    public List<BlockBean> queryAll() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select phone,mode from block ORDER BY id DESC", null);
        List<BlockBean> arrayList = new ArrayList<BlockBean>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                BlockBean blockBean = new BlockBean();
                blockBean.phone = cursor.getString(0);
                blockBean.mode = cursor.getString(1);
                arrayList.add(blockBean);
            }
            cursor.close();
        }
        db.close();
        return arrayList;
    }

    public List<BlockBean> query(int index) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select phone,mode from block ORDER BY id DESC LIMIT ?,40", new String[]{index + ""});
        List<BlockBean> arrayList = new ArrayList<BlockBean>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                BlockBean blockBean = new BlockBean();
                blockBean.phone = cursor.getString(0);
                blockBean.mode = cursor.getString(1);
                arrayList.add(blockBean);
            }
            cursor.close();
        }
        db.close();
        return arrayList;
    }

    public int getCount() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("select count(*) from block ;", null);
        if (cursor != null && cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }


    public String findMode(String phone) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        String mode = "";
        Cursor cursor = db.rawQuery("select mode from block where phone = ?;", new String[]{phone});
        if (cursor != null && cursor.moveToNext()) {
            mode = cursor.getString(0) ;
        }
        cursor.close();
        db.close();
        return mode;
    }



    public static BlockDao getInstance(Context context) {
        if (blockDao == null)
            blockDao = new BlockDao(context);
        return blockDao;
    }
}
