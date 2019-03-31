package jun.safe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import jun.safe.R;
import jun.safe.broadcastReceiver.DeviceAdmin;
import jun.safe.utils.ToastUtil;

/**
 * Created by zkn on 2018/3/19.
 */

public class testActivity extends BaseActivity {
    private TextView tv_test;
    private ComponentName mDeviceAdminSample;
    private DevicePolicyManager mDPM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tv_test = findViewById(R.id.tv_test);
        mDeviceAdminSample = new ComponentName(this, DeviceAdmin.class);
        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    @Override
    public void nextActivity() {

    }

    @Override
    public void previousActivity() {
        finish();
    }


    public void fun1(View view) {
        if (!mDPM.isAdminActive(mDeviceAdminSample)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理器");
            startActivity(intent);
        } else {
            ToastUtil.show(this, "已经激活");
        }


    }

    public void fun2(View view) {
        //是否开启的判断
        if (mDPM.isAdminActive(mDeviceAdminSample)) {
            //激活--->锁屏
            mDPM.lockNow();
            //锁屏同时去设置密码
            mDPM.resetPassword("8023", 0);
        } else {
            ToastUtil.show(this, "请先激活");
        }
    }


}
