package jun.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jun.safe.R;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.NoDoubleClickListener;
import jun.safe.utils.SpUtil;
import jun.safe.view.SettingItemView;

/**
 * Created by zkn on 2018/3/14.
 */

public class LockActivity extends BaseActivity {
    private Button btn_reset;
    private TextView tv_phone;
    private SettingItemView siv_opnSafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        initUI();
    }

    @Override
    public void nextActivity() {
        return ;
    }

    @Override
    public void previousActivity() {
        finish();
    }

    private void initUI() {
        btn_reset = findViewById(R.id.btn_reset);
        tv_phone = findViewById(R.id.tv_phone);
        siv_opnSafe = findViewById(R.id.siv_opnSafe);
        tv_phone.setText(SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PHONE, "未设置"));
        siv_opnSafe.setCheck(SpUtil.getBoolean(getApplicationContext(), ConstantValue.MOBILE_SAFE_LOCK_ON, false));
        siv_opnSafe.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                siv_opnSafe.setCheck(!siv_opnSafe.isCheck());
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.MOBILE_SAFE_LOCK_ON, siv_opnSafe.isCheck());
            }
        });
        btn_reset.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Lock1Activity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        tv_phone.setText(SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PHONE, "未设置"));
        siv_opnSafe.setCheck(SpUtil.getBoolean(getApplicationContext(), ConstantValue.MOBILE_SAFE_LOCK_ON, false));
        super.onResume();
    }
}
