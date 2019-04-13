package asad.com.myapplication.appupdate.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.appupdate.utils
 * @ClassName: AppUtils
 * @Autor: Asia
 * @CreateDate: 2019/4/12  15:32
 * @UpdateDate: 2019/4/12  15:32
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class AppUtils {

    /**
     * Gets version code.获取App的版本号
     *
     * @param context the context
     * @return the version code
     */
    public static  int getVersionCode (Context context) {
        int versionCode= 0;
        if (context ==null) return versionCode;

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) { //9.0以后versioncode 宣告过时,建议使用getLongVersionCode
                versionCode = (int) context.getPackageManager().getPackageInfo(context.getPackageName(),0).getLongVersionCode();
            }else {
                versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  versionCode;
    }

    /**
     * Get version name string. 获取App的名字
     *
     * @param context the context
     * @return the string
     */
    public static  String getVersionName(Context context){
        String versionName = "";
        if (context == null) return  versionName;

        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return  versionName;
    }

}
