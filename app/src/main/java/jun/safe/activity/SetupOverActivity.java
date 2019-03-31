package jun.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import jun.safe.R;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.SpUtil;

/**
 * Created by zkn on 2018/3/14.
 */

public class SetupOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean mobile_safe_lock = SpUtil.getBoolean(this, ConstantValue.MOBILE_SAFE_LOCK, false);
        if(mobile_safe_lock){
            Intent intent = new Intent(this, Lock1Activity.class);
            startActivity(intent);
            finish();
        }
        else{
            setContentView(R.layout.activity_safe);
        }


    }
}
