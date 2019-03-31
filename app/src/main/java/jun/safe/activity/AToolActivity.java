package jun.safe.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import jun.safe.R;
import jun.safe.engine.SmsBackup;
import jun.safe.utils.NoDoubleClickListener;

/**
 * Created by zkn on 2018/3/19.
 */

public class AToolActivity extends BaseActivity {
    private TextView tv_query_phone_address;
    private TextView tv_sms_backup;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"smsBackup.xml";
    private TextView tv_lock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_atool) ;
        super.onCreate(savedInstanceState);
        initUI() ;

    }

    @Override
    public void nextActivity() {
    }

    @Override
    public void previousActivity() {
        finish();
    }

    private void initUI() {
        tv_query_phone_address = findViewById(R.id.tv_query_phone_address);
        tv_query_phone_address.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                startActivity(new Intent(getApplicationContext(),QueryAddressActivity.class));
            }
        });

        tv_sms_backup = findViewById(R.id.tv_sms_backup);
        tv_sms_backup.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showSmsBackupDialog() ;
            }
        });

        tv_lock = findViewById(R.id.tv_lock);
        tv_lock.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                startActivity(new Intent(getApplicationContext(),AppLockActivity.class));
            }
        });


    }

    private void showSmsBackupDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("短信备份");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmsBackup.back(getApplicationContext(), path, new SmsBackup.CallBack() {
                    @Override
                    public void setMax(int max) {
                        progressDialog.setMax(max);
                    }

                    @Override
                    public void setProgress(int i) {
                        progressDialog.setProgress(i);
                    }
                });
                SystemClock.sleep(1000);
                progressDialog.dismiss();
            }
        }).start();
    }
}
