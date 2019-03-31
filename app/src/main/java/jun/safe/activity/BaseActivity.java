package jun.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;

import jun.safe.R;
import jun.safe.utils.ToastUtil;

/**
 * Created by zkn on 2018/3/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getRawX()-e2.getRawX()>100 ){
                    nextActivity();
                }
                if(e2.getRawX()-e1.getRawX()>100){
                    previousActivity() ;
                }
                return false ;
            }
        });

    }

    public abstract void nextActivity() ;

    public abstract void previousActivity() ;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event) ;
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        ToastUtil.cancel();
        super.onDestroy();
    }
}
