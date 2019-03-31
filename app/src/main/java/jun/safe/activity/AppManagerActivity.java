package jun.safe.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


import jun.safe.R;
import jun.safe.bean.AppBean;
import jun.safe.engine.AppInfoProvider;
import jun.safe.utils.ToastUtil;

/**
 * Created by zkn on 2018/3/22.
 */

public class AppManagerActivity extends AppCompatActivity {
    private TextView tv_memory_C;
    private TextView tv_memory_SD;
    private ListView lv_app;
    private ArrayList<AppBean> appInfoList;
    MyAdapter mAdapter;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mAdapter = new MyAdapter();
            lv_app.setAdapter(mAdapter);
            pb_load_app.setVisibility(View.INVISIBLE);

            if (tv_app_title_top != null && mCustomAppList != null) {
                tv_app_title_top.setText("用户应用:" + mCustomAppList.size() + "个");
            }
            return false;
        }
    });
    private ProgressBar pb_load_app;
    private ArrayList<AppBean> mSystemAppList;
    private ArrayList<AppBean> mCustomAppList;
    private TextView tv_app_title_top;
    private AppBean appBean;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.layout_app_manager);
        super.onCreate(savedInstanceState);
        initTitle();
        initLv();
    }

    private void initTitle() {
        String path = Environment.getDataDirectory().getAbsolutePath();
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        String sdSize = android.text.format.Formatter.formatFileSize(this, getAvailSpace(sdPath));
        String size = android.text.format.Formatter.formatFileSize(this, getAvailSpace(path));
        tv_memory_C = findViewById(R.id.tv_memory_C);
        tv_memory_SD = findViewById(R.id.tv_memory_SD);
        tv_memory_C.setText(tv_memory_C.getText().toString() + ":" + size);
        tv_memory_SD.setText(tv_memory_SD.getText().toString() + ":" + sdSize);
    }

    private long getAvailSpace(String path) {
        StatFs statFs = new StatFs(path);
        long blockCount = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        return blockCount * blockSize;
    }

    private void initLv() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                mSystemAppList = new ArrayList<AppBean>();
                mCustomAppList = new ArrayList<AppBean>();
                for (AppBean app : appInfoList) {
                    if (app.isSystem) {
                        mSystemAppList.add(app);
                    } else {
                        mCustomAppList.add(app);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
        lv_app = findViewById(R.id.lv_app);
        pb_load_app = findViewById(R.id.pb_load_app);
        tv_app_title_top = findViewById(R.id.tv_app_title_top);

        lv_app.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mCustomAppList != null && mSystemAppList != null) {
                    if (firstVisibleItem >= mCustomAppList.size() + 1) {
                        tv_app_title_top.setText("系统应用" + mSystemAppList.size() + "个");
                    } else {
                        tv_app_title_top.setText("用户应用:" + mCustomAppList.size() + "个");
                    }
                }

            }
        });


        lv_app.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == mCustomAppList.size() + 1) {
                    return;
                } else {
                    if (position < mCustomAppList.size() + 1) {
                        appBean = mCustomAppList.get(position - 1);
                    } else {
                        appBean = mSystemAppList.get(position - mCustomAppList.size() - 2);
                    }
                    showPopupWindow(view);
                }
            }
        });

    }

    private void showPopupWindow(View view) {
        View popupView = View.inflate(this, R.layout.popup_window, null);
        TextView tv_share = popupView.findViewById(R.id.tv_share);
        TextView tv_uninstall = popupView.findViewById(R.id.tv_uninstall);
        TextView tv_start = popupView.findViewById(R.id.tv_start);

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        tv_share.setOnClickListener(myOnClickListener);
        tv_uninstall.setOnClickListener(myOnClickListener);
        tv_start.setOnClickListener(myOnClickListener);

        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAsDropDown(view, 150, -view.getHeight());

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mCustomAppList.size() + 1) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getCount() {
            return mSystemAppList.size() + mCustomAppList.size() + 2;
        }

        @Override
        public AppBean getItem(int position) {
            if (position == 0 || position == mCustomAppList.size() + 1) {
                return null;
            } else {
                if (position < mCustomAppList.size() + 1) {
                    return mCustomAppList.get(position - 1);
                } else {
                    return mSystemAppList.get(position - mCustomAppList.size() - 2);
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if (type == 0) {
                View view = View.inflate(getApplicationContext(), R.layout.listview_app_title, null);
                TextView tv_app_title = view.findViewById(R.id.tv_app_title);
                if (position == 0) {
                    tv_app_title.setText("用户应用:" + mCustomAppList.size() + "个");
                } else {
                    tv_app_title.setText("系统应用" + mSystemAppList.size() + "个");
                }
                return view;
            } else {
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(), R.layout.listview_app, null);
                    holder = new ViewHolder();
                    holder.img_icon = convertView.findViewById(R.id.img_app_icon);
                    holder.tv_name = convertView.findViewById(R.id.tv_name);
                    holder.tv_path = convertView.findViewById(R.id.tv_path);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.img_icon.setBackgroundDrawable(getItem(position).icon);
                holder.tv_name.setText(getItem(position).name);
                if (getItem(position).isSdCard) {
                    holder.tv_path.setText("SD卡");
                } else {
                    holder.tv_path.setText("手机");
                }
                if (getItem(position).isSystem) {
                    holder.tv_path.setText("系统");
                }

                return convertView;
            }

        }
    }

    private static class ViewHolder {
        ImageView img_icon;
        TextView tv_name;
        TextView tv_path;
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_share:
                    //TODO
                    break;
                case R.id.tv_uninstall:
                    if (appBean.isSystem) {
                        ToastUtil.show(getApplicationContext(), "系统应用不要卸载");
                    } else {
                        Intent intent = new Intent("android.intent.action.DELETE");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setData(Uri.parse("package:" + appBean.packName));
                        startActivity(intent);
                    }
                    break;
                case R.id.tv_start:
                    PackageManager pm = getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage(appBean.packName);
                    if (intent != null) {
                        startActivity(intent);
                    } else {
                        ToastUtil.show(getApplicationContext(), "这应用不能直接开启");
                    }
                    break;
            }
            if (popupWindow != null)
                popupWindow.dismiss();
        }
    }


    @Override
    protected void onResume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                mSystemAppList = new ArrayList<AppBean>();
                mCustomAppList = new ArrayList<AppBean>();
                for (AppBean app : appInfoList) {
                    if (app.isSystem) {
                        mSystemAppList.add(app);
                    } else {
                        mCustomAppList.add(app);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
        super.onResume();
    }
}
