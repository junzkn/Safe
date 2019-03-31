package jun.safe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import jun.safe.R;
import jun.safe.bean.AppBean;
import jun.safe.db.AppLockDao;
import jun.safe.engine.AppInfoProvider;
import jun.safe.utils.NoDoubleClickListener;

/**
 * Created by zkn on 2018/3/23.
 */

public class AppLockActivity extends AppCompatActivity {
    private Button btn_locked , btn_unlocked;
    private LinearLayout ll_locked , ll_unlocked;
    private TextView tv_locked , tv_unlocked;
    private ListView lv_locked , lv_unlocked;
    private ArrayList<AppBean> appInfoList;
    private ArrayList<AppBean> lockApps;
    private ArrayList<AppBean> unlockApps;
    private AppLockDao mDao;


    private MyAdapter lockAdapter;
    private MyAdapter unLockAdapter1;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            lockAdapter = new MyAdapter(true);
            unLockAdapter1 = new MyAdapter(false);
            lv_locked.setAdapter(lockAdapter);
            lv_unlocked.setAdapter(unLockAdapter1);
            return false;
        }
    }) ;
    private TranslateAnimation translateAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.applock_activity);
        super.onCreate(savedInstanceState);
        initUI();
        initData();
        initAnim();
    }

    private void initAnim() {
        translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(500);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                lockApps = new ArrayList<>();
                unlockApps = new ArrayList<>();
                mDao = AppLockDao.getInstance(getApplicationContext());
                ArrayList<String> lockList = mDao.query();
                for(AppBean appBean : appInfoList){
                    if(!appBean.isSystem){
                        if(lockList.contains(appBean.packName)){
                            lockApps.add(appBean);
                        }else {
                            unlockApps.add(appBean) ;
                        }
                    }
                }
                handler.sendEmptyMessage(0) ;
            }
        }).start();

    }

    private void initUI() {
        btn_locked = findViewById(R.id.btn_locked);
        btn_unlocked = findViewById(R.id.btn_unlocked);
        ll_locked = findViewById(R.id.ll_locked);
        ll_unlocked = findViewById(R.id.ll_unlocked);
        tv_locked = findViewById(R.id.tv_locked);
        tv_unlocked = findViewById(R.id.tv_unlocked);
        lv_locked = findViewById(R.id.lv_locked);
        lv_unlocked = findViewById(R.id.lv_unlocked);

        btn_locked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_locked.setVisibility(View.VISIBLE);
                ll_unlocked.setVisibility(View.GONE);
            }
        });
        btn_unlocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_unlocked.setVisibility(View.VISIBLE);
                ll_locked.setVisibility(View.GONE);
            }
        });


    }


    private class MyAdapter extends BaseAdapter{
        private Boolean isLock ;
        public MyAdapter(boolean isLock){
            this.isLock = isLock ;
        }

        @Override
        public int getCount() {
            if(isLock){
                tv_locked.setText("有"+lockApps.size()+"个");
                return lockApps.size() ;
            }else {
                tv_unlocked.setText("有"+unlockApps.size()+"个");
                return unlockApps.size();

            }
        }

        @Override
        public AppBean getItem(int position) {
            if(isLock){
                return lockApps.get(position) ;
            }else {
                return unlockApps.get(position) ;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null ;
            if(convertView==null){
                viewHolder = new ViewHolder() ;
                convertView = View.inflate(getApplicationContext(), R.layout.listview_app_lock, null);
                viewHolder.img_app_lock_icon = convertView.findViewById(R.id.img_app_lock_icon) ;
                viewHolder.tv_app_lock_name = convertView.findViewById(R.id.tv_app_lock_name) ;
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final AppBean item = getItem(position);
            final View animView = convertView ;
            viewHolder.img_app_lock_icon.setBackgroundDrawable(item.icon);
            viewHolder.tv_app_lock_name.setText(item.name);


            viewHolder.img_app_lock_icon.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    animView.startAnimation(translateAnimation);
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if(isLock){
                                lockApps.remove(item) ;
                                unlockApps.add(item) ;
                                mDao.delete(item.packName);
                                lockAdapter.notifyDataSetChanged();
                                unLockAdapter1.notifyDataSetChanged();
                            }else {
                                unlockApps.remove(item) ;
                                lockApps.add(item) ;
                                mDao.insert(item.packName);
                                unLockAdapter1.notifyDataSetChanged();
                                lockAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
            });

            return convertView;
        }
    }


    static class ViewHolder{
        ImageView img_app_lock_icon ;
        TextView tv_app_lock_name ;

    }


}
