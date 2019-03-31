package jun.safe.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import jun.safe.R;
import jun.safe.utils.ConstantValue;
import jun.safe.utils.DialogUtil;
import jun.safe.utils.SpUtil;
import jun.safe.utils.StreamUtil;
import jun.safe.utils.ToastUtil;

public class SplashActivity extends AppCompatActivity {

    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"xxx.apk" ;
    private final static String TAG = "SplashActivity";
    private static final int UPDATE_VERSION = 100 ;
    private static final int ENTER_HOME = 101;
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;

    private TextView tv_version_name ;
    private ProgressDialog pd ;

    private String mVersionDes ;
    private String mVersionCode ;
    private String mVersionUrl ;

    private int mLocalVersionCode;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION :
                    showUpdateDialog() ;
                    break ;
                case ENTER_HOME :
                    enterHome() ;
                    break ;
                case URL_ERROR :
                    Log.e(TAG,"Url异常");
                    enterHome() ;
                    break ;
                case IO_ERROR :
                    Log.e(TAG,"读取异常");
                    enterHome();
                    break ;
                case JSON_ERROR :
                    Log.e(TAG,"json解析异常");
                    enterHome() ;
                    break ;
                default :
                    break ;
            }
            return false ;
        }
    }) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        x.Ext.init(getApplication());
        initUI() ;
        initData() ;
    }

    /**
     * 弹出更新提示
     */
    private void showUpdateDialog() {
        DialogUtil.showDialog(this, "更新提示", mVersionDes,
                "现在更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downLoadApk();
                    }
                },
                "稍后更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterHome();
                    }
                } , new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        enterHome();
                        dialog.dismiss();
                    }
                }
        ) ;

    }

    /**
     * 下载apk
     */
    private void downLoadApk() {

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file = new File(path) ;
            if(file.exists()) {
                ToastUtil.show(this,"下载已完成");
                installApk(file) ;
            }
            else {
                pd = new ProgressDialog(SplashActivity.this);
                RequestParams requestParams = new RequestParams(mVersionUrl) ;
                requestParams.setSaveFilePath(path);
                x.http().get(requestParams, new Callback.ProgressCallback<File>() {
                    @Override
                    public void onSuccess(File file) {
                        Log.e(TAG,"下载成功");
                        installApk(file) ;
                    }
                    @Override
                    public void onError(Throwable throwable, boolean b) {

                        ToastUtil.show(SplashActivity.this,"下载失败");
                        Log.e(TAG,"下载失败");
                        enterHome();
                    }
                    @Override
                    public void onCancelled(CancelledException e) {
                        Log.e(TAG,"取消下载");
                    }
                    @Override
                    public void onFinished() {
                        pd.dismiss();
                        Log.e(TAG,"结束下载");
                    }
                    @Override
                    public void onWaiting() {
                        Log.e(TAG,"等待下载");
                    }
                    @Override
                    public void onStarted() {
                        Log.e(TAG,"开始下载");
                        tv_version_name.setText("正在加载");
                        tv_version_name.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onLoading(long total, long current, boolean b) {
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pd.setMessage("正在下载中......");
                        pd.show();
                        pd.setMax((int) total);
                        pd.setProgress((int) current);
                    }
                });
            }

        }
    }

    /**
     * 安装apk
     */
    private void installApk(File file) {
        Log.e(TAG,"开始安装"+file.getAbsolutePath()+"文件") ;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive") ;
        } else {
            Uri uri = FileProvider.getUriForFile(this,"com.jun.fileProvider",file) ;
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        enterHome();
        startActivity(intent);


    }



    /**
     * 进入主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * 获取数据
     */
    private void initData() {
        mLocalVersionCode = getLocalVersionCode();
        if(SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE,true)){
            checkVersion() ;
        }else {
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,500) ;
        }

    }

    /**
     * 检查版本号
     */
    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL("http://jun:8080/safe/updata.json") ;
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(1000);
                    connection.setReadTimeout(1000);

                    if(connection.getResponseCode() == 200){
                        Log.e(TAG,"链接成功") ;
                        InputStream is = connection.getInputStream() ;
                        String  json = StreamUtil.streamToString(is) ;
                        JSONObject jsonObject = new JSONObject(json);

                        mVersionDes = jsonObject.getString("versionDes");
                        mVersionCode = jsonObject.getString("versionCode");
                        mVersionUrl = jsonObject.getString("downloadUrl");
                        Log.e(TAG,"获取的信息：Des"+mVersionDes+".Code:"+mVersionCode+".url:"+mVersionUrl) ;

                        if(mLocalVersionCode<Integer.parseInt(mVersionCode)){
                            msg.what = UPDATE_VERSION ;
                        }
                        else {
                            msg.what = ENTER_HOME ;
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR ;
                }catch (IOException e){
                    e.printStackTrace();
                    msg.what = IO_ERROR ;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR ;
                }finally{
                    long endTime = System.currentTimeMillis();
                    if(endTime-startTime<1500){
                        SystemClock.sleep(1500-endTime+startTime);
                    }
                    mHandler.sendMessage(msg) ;
                }
            }
        }).start();

    }


    /**
     * 初始化UI
     */
    private void initUI() {
        tv_version_name = findViewById(R.id.tv_version_name);
    }

    /**
     * 获取应用版本号
     * @return 应用版本号 null表示异常
     */
    public String getVersionName() {
        try {
            PackageManager packageManager = getPackageManager() ;
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            return packageInfo.versionName ;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }

    /**
     *获取代码版本号
     * @return 代码版本号 0代表异常
     */
    public int getLocalVersionCode() {
        try {
            PackageManager packageManager = getPackageManager() ;
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            return packageInfo.versionCode ;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
