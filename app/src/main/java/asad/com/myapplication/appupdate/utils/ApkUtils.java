package asad.com.myapplication.appupdate.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.appupdate.utils
 * @ClassName: ApkUtils
 * @Autor: Asia
 * @CreateDate: 2019/4/12  15:00
 * @UpdateDate: 2019/4/12  15:00
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class ApkUtils {

    /**
     * Install apk.安装Apk 操作
     *
     * @param context the context
     * @param apkFile the apk file
     */
    public  static  void installApk(Context context, File apkFile){

        Intent intent = getApkInstallIntent(context,apkFile);

        context.startActivity(intent);

    }

    /**
     * Get apk install intent intent.判断手机系统是否大于8.0,大于8.0安装权限授权
     *
     * @param context the context
     * @param apkFile the apk file
     * @return the intent
     */
    private static  Intent getApkInstallIntent(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N) { //判断版本是否大于5.0
            Log.d("APKUtlis",context.getPackageName()+">>>>>>>>>>>>>>>>>>>");


            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
             uri = FileProvider.getUriForFile(context,context.getPackageName()+".update.fileprovider",apkFile);  // 获取文件权限

        }else {
             uri = getApkUri(apkFile);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");

        List<ResolveInfo> resolveLists = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // 然后全部授权
        for (ResolveInfo resolveInfo : resolveLists) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }


        return  intent;
    }

    /**
     * Gets apk uri.文件读写权限
     *
     * @param apkFile the apk file
     * @return the apk uri
     */
    private static Uri getApkUri(File apkFile) {

        //获取sdcard 的读写权限,apk需要保存才授予安装权限

        String[] command = {"chmod","777",apkFile.toString()};

        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(apkFile);

        return  uri ;
    }






}
