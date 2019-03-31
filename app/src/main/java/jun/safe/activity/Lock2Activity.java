package jun.safe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import jun.safe.R;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.NoDoubleClickListener;
import jun.safe.utils.SpUtil;
import jun.safe.utils.ToastUtil;
import jun.safe.view.SettingItemView;

public class Lock2Activity extends BaseActivity {

    public static Lock2Activity instance;
    private Button btn_next;
    private Button btn_previous;
    private SettingItemView siv_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock2);
        initUI();
    }

    @Override
    public void nextActivity() {
        if (siv_sim.isCheck()) {
            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.show(getApplicationContext(),"还没有获取手机信息的权限");
                ActivityCompat.requestPermissions(Lock2Activity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
            }else {
                SpUtil.putString(getApplicationContext(),ConstantValue.MOBILE_SAFE_SIM_NUMBER,manager.getSimSerialNumber());
                Intent intent = new Intent(getApplicationContext(), Lock3Activity.class);
                startActivity(intent);
            }

        } else {
            ToastUtil.show(getApplicationContext(), "需要绑定SIM卡");
        }
    }

    @Override
    public void previousActivity() {
        finish();
    }


    private void initUI() {
        btn_next = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);
        siv_sim = findViewById(R.id.siv_sim);
        boolean isCheck = SpUtil.getBoolean(getApplicationContext(), ConstantValue.MOBILE_SAFE_SIM, siv_sim.isCheck());
        siv_sim.setCheck(isCheck);
        siv_sim.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                siv_sim.setCheck(!siv_sim.isCheck());
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.MOBILE_SAFE_SIM, siv_sim.isCheck());
            }
        });
        btn_next.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                nextActivity(); ;
            }
        });
        btn_previous.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                previousActivity();
            }
        });
    }

}
