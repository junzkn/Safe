package jun.safe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jun.safe.R;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.SpUtil;
import jun.safe.view.SettingItemView;

/**
 * Created by zkn on 2018/3/14.
 */

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
        intiUpdate();
    }


    /**
     * 版本更新开关
     */
    private void intiUpdate() {
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        final SettingItemView siv_update = findViewById(R.id.siv_update) ;
        siv_update.setCheck(open_update);

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_update.isCheck() ;
                siv_update.setCheck(!isCheck) ;
                SpUtil.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!isCheck);
            }
        });
    }
}
