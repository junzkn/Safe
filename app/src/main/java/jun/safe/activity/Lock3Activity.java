package jun.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jun.safe.R;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.NoDoubleClickListener;
import jun.safe.utils.SpUtil;
import jun.safe.utils.ToastUtil;

/**
 * Created by zkn on 2018/3/15.
 */

public class Lock3Activity extends BaseActivity {

    private Button btn_next;
    private Button btn_previous;
    private EditText et_phoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock3);
        initUI();
    }

    @Override
    public void nextActivity() {
        String phoneInput = et_phoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneInput)) {
            ToastUtil.show(getApplicationContext(), "需要输入联系手机号");
        } else {
            finish();
            Lock1Activity.instance.finish();
            Lock2Activity.instance.finish();
            ToastUtil.show(getApplicationContext(), "设置完成");
            Intent intent = new Intent(getApplicationContext(), LockActivity.class);
            startActivity(intent);
            SpUtil.putBoolean(getApplicationContext(), ConstantValue.MOBILE_SAFE_LOCK, true);
            SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PHONE, phoneInput);
        }
    }

    @Override
    public void previousActivity() {
        finish();
    }

    private void initUI() {
        btn_next = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);
        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        String phone = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PHONE, et_phoneNumber.getText().toString());
        et_phoneNumber.setText(phone);
        btn_next.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                nextActivity();
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
