package jun.safe.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jun.safe.R;
import jun.safe.engine.AddressDao;
import jun.safe.utils.NoDoubleClickListener;

/**
 * Created by zkn on 2018/3/19.
 */

public class QueryAddressActivity extends BaseActivity {
    private EditText et_phone;
    private Button btn_query;
    private TextView tv_result;
    private String mAddress ;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tv_result.setText(mAddress);
            return false;
        }}
    ) ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_query_address);
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    public void nextActivity() {

    }

    @Override
    public void previousActivity() {
        finish();
    }

    private void initUI() {
        et_phone = findViewById(R.id.et_phone);
        btn_query =findViewById(R.id.btn_query);
        tv_result =findViewById(R.id.tv_result);

        btn_query.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                String phone = et_phone.getText().toString().trim() ;
                if(!TextUtils.isEmpty(phone)){
                    query(phone) ;
                }else {
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    et_phone.startAnimation(shake);
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                }


            }
        });

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query(et_phone.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void query(final String phone) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(phone) ;
                mHandler.sendEmptyMessage(0) ;
            }
        }).start();
    }
}
