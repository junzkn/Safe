package jun.safe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import jun.safe.R;
import jun.safe.bean.BlockBean;
import jun.safe.db.BlockDao;
import jun.safe.utils.ToastUtil;

/**
 * Created by zkn on 2018/3/21.
 */

public class BlockActivity extends AppCompatActivity {
    private Button btn_add;
    private ListView lv_number;
    private List<BlockBean> mBlockBeanList;
    private MyAdapter myAdapter;
    private String mode = "短信";
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(myAdapter==null){
                myAdapter = new MyAdapter();
                lv_number.setAdapter(myAdapter);
            }else {
                myAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });
    private BlockDao blockDao;
    private boolean isLoad = false;
    private int mCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_block);
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                blockDao = BlockDao.getInstance(getApplicationContext());
                mCount = blockDao.getCount() ;
                mBlockBeanList = blockDao.query(0);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }


    private void initUI() {
        btn_add = findViewById(R.id.btn_add);
        lv_number = findViewById(R.id.lv_number);

        lv_number.setOnScrollListener(new MyScrollChangeListener());

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getApplicationContext(), R.layout.dialog_add_block, null);
        dialog.setView(view);
        RadioGroup rg_box = view.findViewById(R.id.rg_box);
        final EditText et_phone = view.findViewById(R.id.et_phone);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_submit = view.findViewById(R.id.btn_submit);

        rg_box.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sms:
                        mode = "短信";
                        break;
                    case R.id.rb_phone:
                        mode = "电话";
                        break;
                    case R.id.rb_all:
                        mode = "所有";
                        break;
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    BlockBean bean = new BlockBean(phone, mode);
                    blockDao.insert(bean);
                    mBlockBeanList.add(0, bean);
                    if (myAdapter != null) {
                        myAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                } else {
                    ToastUtil.show(getApplicationContext(), "电话号码不能为空");
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBlockBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBlockBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.listview_block_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_phone = convertView.findViewById(R.id.tv_phone);
                viewHolder.tv_mode = convertView.findViewById(R.id.tv_mode);
                viewHolder.btn_delete = convertView.findViewById(R.id.btn_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }


            viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blockDao.delete(mBlockBeanList.get(position).phone);
                    mBlockBeanList.remove(position);
                    if (myAdapter != null)
                        myAdapter.notifyDataSetChanged();

                }
            });

            viewHolder.tv_phone.setText(mBlockBeanList.get(position).phone);
            viewHolder.tv_mode.setText(mBlockBeanList.get(position).mode);
            return convertView;
        }

    }

    private class MyScrollChangeListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mBlockBeanList != null) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lv_number.getLastVisiblePosition() >= mBlockBeanList.size() - 1
                        && !isLoad) {
                    if(mCount>mBlockBeanList.size()){
                        Log.e("ListView","加载");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<BlockBean> moreList = blockDao.query(mBlockBeanList.size());
                                mBlockBeanList.addAll(moreList);
                                mHandler.sendEmptyMessage(0);
                            }
                        }).start();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


    private static class ViewHolder {
        TextView tv_phone;
        TextView tv_mode;
        Button btn_delete;
    }

}
