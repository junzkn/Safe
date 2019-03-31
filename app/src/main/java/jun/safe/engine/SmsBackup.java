package jun.safe.engine;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zkn on 2018/3/22.
 */

public class SmsBackup {
    static int index ;

    public static void back(Context context, String path, CallBack callBack) {
        index=0 ;
        Cursor cursor = null;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(path);
            cursor = context.getContentResolver().query(Uri.parse("content://sms/"),
                    new String[]{"address", "date", "type", "body"},
                    null, null, null);

            int count = cursor.getCount() ;
            if(callBack!=null)
                callBack.setMax(count);

            fileOutputStream = new FileOutputStream(file);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(fileOutputStream, "utf-8");
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "smss");
            while (cursor.moveToNext() ) {
                xmlSerializer.startTag(null, "sms");

                xmlSerializer.startTag(null, "address");
                xmlSerializer.text(cursor.getString(0));
                xmlSerializer.endTag(null, "address");
                xmlSerializer.startTag(null, "date");
                xmlSerializer.text(cursor.getString(1));
                xmlSerializer.endTag(null, "date");
                xmlSerializer.startTag(null, "type");
                xmlSerializer.text(cursor.getString(2));
                xmlSerializer.endTag(null, "type");
                xmlSerializer.startTag(null, "body");
                xmlSerializer.text(cursor.getString(3));
                xmlSerializer.endTag(null, "body");

                xmlSerializer.endTag(null, "sms");

               if(callBack!=null){
                   callBack.setProgress(index++);
               }


            }

            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && fileOutputStream != null) {
                try {
                    cursor.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public interface  CallBack {
        void setMax(int max) ;
        void setProgress(int i) ;
    }



}
