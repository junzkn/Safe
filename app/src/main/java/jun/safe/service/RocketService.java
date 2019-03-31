package jun.safe.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import jun.safe.R;

/**
 * Created by zkn on 2018/3/20.
 */

public class RocketService extends Service {

    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private View viewToast;
    private int mScreenWidth;
    private int mScreenHeight;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mParams.y = (int) msg.obj;
            mWM.updateViewLayout(viewToast,mParams);
            return false;
        }
    }) ;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

        mScreenWidth = mWM.getDefaultDisplay().getWidth() ;
        mScreenHeight = mWM.getDefaultDisplay().getHeight() ;

        showRocket() ;
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if(mWM!=null && viewToast!=null)
            mWM.removeView(viewToast);
        super.onDestroy();
    }

    private void showRocket() {
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT ;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT ;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON ;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        mParams.gravity = Gravity.CENTER ;

        viewToast = View.inflate(this, R.layout.rocket_view,null) ;

        ImageView iv_rocket = (ImageView) viewToast.findViewById(R.id.iv_rocket);
        AnimationDrawable drawable = (AnimationDrawable) iv_rocket.getBackground();
        drawable.start();

        iv_rocket.setOnTouchListener(new MyListener()) ;

        mWM.addView(viewToast, mParams);
    }

    private class MyListener implements View.OnTouchListener{
        private int startX;
        private int startY;
        private int moveX ;
        private int moveY ;
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

                    mWM.updateViewLayout(viewToast,mParams);

                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    if(mParams.y>700){
                        sendRocket();
                    }
                    break;
            }
            return true;
        }
    }

    private void sendRocket() {
        new Thread(){
            public void run() {
                for(int i=0;i<20;i++){
                    int y = 700 - i*70;
                    SystemClock.sleep(20);
                    Message msg = Message.obtain();
                    msg.obj = y;
                    mHandler.sendMessage(msg);
                }
            };
        }.start();
    }

}
