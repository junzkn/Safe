package jun.safe.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by zkn on 2018/3/13.
 */

public class DialogUtil {
    /**
     *只有确定键
     * @param context
     * @param title
     * @param msg
     * @param pMsg
     * @param p
     */
    public static void  showDialog(Context context , String title , String msg ,String pMsg , DialogInterface.OnClickListener p ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg) ;
        builder.setTitle(title) ;
        builder.setPositiveButton(pMsg, p) ;
        builder.show() ;
    }

    /**
     *有确定键和取消键
     * @param context
     * @param title
     * @param msg
     * @param pMsg
     * @param p
     * @param nMsg
     * @param n
     */
    public static void  showDialog(Context context , String title , String msg ,String pMsg , DialogInterface.OnClickListener p ,String nMsg , DialogInterface.OnClickListener n ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg) ;
        builder.setTitle(title) ;
        builder.setPositiveButton(pMsg, p) ;
        builder.setNegativeButton(nMsg,n) ;
        builder.show() ;
    }

    /**
     *有确定键，取消键，返回键
     * @param context
     * @param title
     * @param msg
     * @param pMsg
     * @param p
     * @param nMsg
     * @param n
     * @param c
     */
    public static void  showDialog(Context context  ,String title , String msg ,String pMsg ,
                                   DialogInterface.OnClickListener p ,String nMsg , DialogInterface.OnClickListener n ,
                                    DialogInterface.OnCancelListener c ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg) ;
        builder.setTitle(title) ;
        builder.setPositiveButton(pMsg, p) ;
        builder.setNegativeButton(nMsg,n) ;
        builder.setOnCancelListener(c);
        builder.show() ;
    }

    public static void  showDialog(Context context  ,int icon , String title , String msg ,String pMsg ,
                                   DialogInterface.OnClickListener p ,String nMsg , DialogInterface.OnClickListener n ,
                                   DialogInterface.OnCancelListener c ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg) ;
        builder.setTitle(title) ;
        builder.setPositiveButton(pMsg, p) ;
        builder.setNegativeButton(nMsg,n) ;
        builder.setIcon(icon) ;
        builder.setOnCancelListener(c);
        builder.show() ;
    }


}
