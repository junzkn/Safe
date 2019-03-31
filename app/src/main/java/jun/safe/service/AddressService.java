package jun.safe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import jun.safe.R;
import jun.safe.engine.AddressDao;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.SpUtil;

/**
 * Created by zkn on 2018/3/19.
 */

public class AddressService extends Service {
    private TelephonyManager mTm;
    private myPhoneStateListener mMyPhoneStateListener;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private View mViewToast;
    private String mQueryAddress;
    private TextView tv_toast_address;
    private Handler  mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tv_toast_address.setText(mQueryAddress);
            return false;
        }
    });
    int[] drawableIds = new int[]{
            R.drawable.call_locate_white,
            R.drawable.call_locate_orange,
            R.drawable.call_locate_blue,
            R.drawable.call_locate_gray,
            R.drawable.call_locate_green,
    };
    private InnerOutCallReceiver mInnerOutCallReceiver;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mMyPhoneStateListener = new myPhoneStateListener();
        mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        mInnerOutCallReceiver = new InnerOutCallReceiver();
        registerReceiver(mInnerOutCallReceiver,intentFilter) ;
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if(mTm!=null && mMyPhoneStateListener!=null){
            mTm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        if(mInnerOutCallReceiver!=null){
            unregisterReceiver(mInnerOutCallReceiver);
        }
        super.onDestroy();
    }

    private class myPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if(mWM!=null && mViewToast!=null)
                        mWM.removeView(mViewToast);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    showToast(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    public void showToast(final String incomingNumber) {

        // 自定义吐司样式,时间
        //给吐司定义出来的参数(宽高,类型,触摸方式)
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        //将吐司放置在左上角显示
        mParams.gravity=Gravity.TOP+ Gravity.LEFT;
        mParams.setTitle("Toast");

        new Thread(){
            public void run() {
                mQueryAddress = AddressDao.getAddress(incomingNumber);
                mHandler.sendEmptyMessage(0);
            };
        }.start();


        mViewToast = View.inflate(this,R.layout.toast_view, null);
        mViewToast.setOnTouchListener(new mOnTouchListener());
        tv_toast_address = (TextView) mViewToast.findViewById(R.id.tv_toast_address);

        mParams.x = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        mParams.y = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);

        int toast_style_index = SpUtil.getInt(this, ConstantValue.TOAST_STYLE, 0);
        tv_toast_address.setBackgroundResource(drawableIds[toast_style_index]);

        mWM.addView(mViewToast, mParams);
    }

    private class mOnTouchListener implements View.OnTouchListener {
            int startX;
            int startY;
            int moveX;
            int moveY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveX = (int) event.getRawX();
                        moveY = (int) event.getRawY();

                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        mParams.x+=disX ;
                        mParams.y+=disY ;

                        mWM.updateViewLayout(mViewToast,mParams);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }

    }

    private class InnerOutCallReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData();
            showToast(phone);
        }
    }
}
