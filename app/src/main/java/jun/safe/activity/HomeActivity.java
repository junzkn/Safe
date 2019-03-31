package jun.safe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import jun.safe.R;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.Md5Util;
import jun.safe.utils.SpUtil;
import jun.safe.utils.ToastUtil;

/**
 * Created by zkn on 2018/3/12.
 */

public class HomeActivity extends AppCompatActivity {
    private TextView tv_ad;
    private GridView gv_home;
    private String[] mTitleStr;
    private int[] mDrawableID;
    final static private String TAG = "HomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        initUI();
        initData();
    }

    private void initData() {
        mTitleStr = new String[]{"手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        ;
        mDrawableID = new int[]{
                R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
                R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings,};
        gv_home.setAdapter(new MyAdapter());
        gv_home.setOnItemClickListener(new MyOnClickItemLiatener());

    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            ImageView img_icon = view.findViewById(R.id.img_icon);
            TextView tv_model = view.findViewById(R.id.tv_model);
            tv_model.setText(mTitleStr[position]);
            img_icon.setBackgroundResource(mDrawableID[position]);
            return view;
        }
    }

    private void initUI() {
        tv_ad = findViewById(R.id.tv_ad);
        gv_home = findViewById(R.id.gv_home);
    }

    private class MyOnClickItemLiatener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    showDiaLog();
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private void showDiaLog() {
        String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psd)) {
            showSetPsdDialog();
        } else {
            showConfirmPsdDialog();
        }

    }

    private void showConfirmPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_confirm_psd, null);
        dialog.setView(view);
        dialog.show();
        Button btn_confirmPsd_submit = view.findViewById(R.id.btn_confirmPsd_submit);
        Button btn_confirmPsd_cancel = view.findViewById(R.id.btn_confirmPsd_cancel);

        btn_confirmPsd_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirmPsd_psd = view.findViewById(R.id.et_confirmPsd_psd);
                String psd = et_confirmPsd_psd.getText().toString();
                String temp = SpUtil.getString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD,"") ;
                if (!TextUtils.isEmpty(psd)) {
                    if(temp.equals(Md5Util.encoder(psd))){
                        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }else {
                        ToastUtil.show(getApplicationContext(), "请输入密码错误");
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "请输入密码");
                }
            }
        });
        btn_confirmPsd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void showSetPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_psd, null);
        dialog.setView(view);
        dialog.show();
        Button btn_setPsd_submit = view.findViewById(R.id.btn_setPsd_submit);
        Button btn_psd_cancel = view.findViewById(R.id.btn_setPsd_cancel);

        btn_setPsd_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_setPsd_setPsd = view.findViewById(R.id.et_setPsd_setPsd);
                EditText et_setPsd_confirmPsd = view.findViewById(R.id.et_setPsd_confirmPsd);

                String psd = et_setPsd_setPsd.getText().toString();
                String confirmPsd = et_setPsd_confirmPsd.getText().toString();

                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)) {
                    if (psd.equals(confirmPsd)) {
                        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(psd));
                    } else {
                        ToastUtil.show(getApplicationContext(), "两次密码不相同");
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "请输入密码");
                }
            }
        });
        btn_psd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}
