package jun.safe.broadcastReceiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import jun.safe.utils.ConstantValue;
import jun.safe.utils.SpUtil;

/**
 * Created by zkn on 2018/3/16.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(SpUtil.getBoolean(context,ConstantValue.MOBILE_SAFE_LOCK_ON,false)){
            String oldSimNumber = SpUtil.getString(context, ConstantValue.MOBILE_SAFE_SIM_NUMBER, "");
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return ;
            }
            String newSimNumber = tm.getSimSerialNumber();
            if(!oldSimNumber.equals(newSimNumber)){
                String phone = SpUtil.getString(context,ConstantValue.MOBILE_SAFE_PHONE,"") ;
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(phone,null,"WARMING！SIM CARD HAS CHANGE",null,null);
            }
        }

    }
}
