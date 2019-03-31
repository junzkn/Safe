package jun.safe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import jun.safe.bean.AppBean;

/**
 * Created by zkn on 2018/3/22.
 */

public class AppInfoProvider {


    /**
     * 获取手机所有应用
     * @param context
     * @return
     */
    public static ArrayList<AppBean> getAppInfoList(Context context) {
        PackageManager pm = context.getPackageManager();

        List<PackageInfo> packagesInfoList = pm.getInstalledPackages(0);
        ArrayList<AppBean> appBeans = new ArrayList<>();

        for (PackageInfo packageInfo : packagesInfoList) {
            AppBean appBean = new AppBean();
            appBean.packName = packageInfo.packageName;
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            appBean.name = applicationInfo.loadLabel(pm).toString();
            appBean.icon = applicationInfo.loadIcon(pm);
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                appBean.isSystem = true ;
            }else {
                appBean.isSystem = false ;
            }
            if((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                appBean.isSdCard = true ;
            }else {
                appBean.isSdCard = false ;
            }

            appBeans.add(appBean) ;
        }
        return appBeans ;
    }

}
