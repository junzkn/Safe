package jun.safe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import jun.safe.R;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.DoubleClickListener;
import jun.safe.utils.SpUtil;

/**
 * Created by zkn on 2018/3/20.
 */

public class ToastLocationActivity extends Activity {
    private ImageView iv_drag;
    private Button btn_top;
    private Button btn_bottom;
    private WindowManager mWm;
    private int mScreenHeight;
    private int mScreenWidth;

    private static final int SETTING_LOCATION_SELF = 103;
    private static final int SETTING_LOCATION_CENTER = 104;
    private int resultLocation = SETTING_LOCATION_SELF ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_toast_location);
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        btn_top = findViewById(R.id.btn_top);
        btn_bottom = findViewById(R.id.btn_bottom);
        iv_drag = findViewById(R.id.iv_drag);

        mWm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenHeight = mWm.getDefaultDisplay().getHeight();
        mScreenWidth = mWm.getDefaultDisplay().getWidth();


        int x = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        int y = SpUtil.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.leftMargin = x;
        params.topMargin = y;
        iv_drag.setLayoutParams(params);

        if (y > mScreenHeight / 2) {
            btn_bottom.setVisibility(View.INVISIBLE);
            btn_top.setVisibility(View.VISIBLE);
        } else {
            btn_bottom.setVisibility(View.VISIBLE);
            btn_top.setVisibility(View.INVISIBLE);
        }

        iv_drag.setOnTouchListener(new mOnTouchListener());
        iv_drag.setOnClickListener(new mDoubleClickListener());
        btn_top.setOnClickListener(new mClickListener());
        btn_bottom.setOnClickListener(new mClickListener());

    }

    private class mClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_X, iv_drag.getLeft());
            SpUtil.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, iv_drag.getTop());
            setResult(resultLocation);
            finish();
        }
    }

    private class mDoubleClickListener extends DoubleClickListener{
        @Override
        public void onDoubleClick(View v) {
            int left = mScreenWidth/2 - iv_drag.getWidth()/2 ;
            int right = mScreenWidth/2 + iv_drag.getWidth()/2 ;
            int top = mScreenHeight/2 - iv_drag.getHeight()/2 ;
            int bottom = mScreenHeight/2 + iv_drag.getHeight()/2 ;
            iv_drag.layout(left,top,right,bottom);
            resultLocation = SETTING_LOCATION_CENTER ;
        }
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

                    int left = iv_drag.getLeft() + disX;
                    int top = iv_drag.getTop() + disY;
                    int right = iv_drag.getRight() + disX;
                    int bottom = iv_drag.getBottom() + disY;

                    if (left < 0) {
                        return false;
                    }
                    if (right > mScreenWidth) {
                        return false;
                    }
                    if (top < 0) {
                        return false;
                    }
                    if (bottom > mScreenHeight - 40) {
                        return false;
                    }
                    if (top > mScreenHeight / 2) {
                        btn_bottom.setVisibility(View.INVISIBLE);
                        btn_top.setVisibility(View.VISIBLE);
                    } else {
                        btn_bottom.setVisibility(View.VISIBLE);
                        btn_top.setVisibility(View.INVISIBLE);
                    }


                    iv_drag.layout(left, top, right, bottom);

                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();


                    break;
                case MotionEvent.ACTION_UP:
                    resultLocation = SETTING_LOCATION_SELF ;
                    break;

            }

            return false;
        }
    }

}
