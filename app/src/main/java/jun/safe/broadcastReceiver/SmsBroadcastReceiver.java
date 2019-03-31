package jun.safe.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.telephony.SmsMessage;
import android.util.Log;

import jun.safe.R;
import jun.safe.service.LocationService;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.LogUtil;
import jun.safe.utils.SpUtil;
import jun.safe.utils.ToastUtil;

/**
 * Created by zkn on 2018/3/16.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(SpUtil.getBoolean(context, ConstantValue.MOBILE_SAFE_LOCK_ON,false)){
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for(Object o : pdus){
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
                String messageBody = sms.getMessageBody();
                if(messageBody.contains("1dwdwd0086")) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs) ;
                    mediaPlayer.start();
                }
                if(messageBody.contains("@@10086")) {
                    context.startService(new Intent(context,LocationService.class)) ;
                }
            }
        }
    }
}
