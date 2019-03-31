package jun.safe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import jun.safe.R;
import jun.safe.db.BlockDao;

/**
 * Created by zkn on 2018/3/21.
 */

public class BlockService extends Service {
    private InnerSmsReceiver smsReceiver;
    private BlockDao mDao;
    private TelephonyManager mTm;
    private WindowManager mWM;
    private myPhoneStateListener mMyPhoneStateListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);
        smsReceiver = new InnerSmsReceiver();
        registerReceiver(smsReceiver, intentFilter);

        mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mMyPhoneStateListener = new myPhoneStateListener();
        mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);


        super.onCreate();
    }


    @Override
    public void onDestroy() {
        if (smsReceiver != null)
            unregisterReceiver(smsReceiver);
        if(mMyPhoneStateListener!=null){
            mTm.listen(mMyPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }

    private class InnerSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for(Object o : pdus){
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
                String address = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();

                mDao = BlockDao.getInstance(getApplicationContext());
                String mode = mDao.findMode(address) ;
                if(mode.equals("短信")|| mode.equals("所有")){
                    Log.e("短信内容",messageBody) ;

                }

            }
        }
    }

    private class myPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            mDao = BlockDao.getInstance(getApplicationContext());
            String mode = mDao.findMode(incomingNumber) ;
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    if(mode.equals("电话")|| mode.equals("所有")){
                        Log.e("来电",""+ System.currentTimeMillis()) ;
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
}
