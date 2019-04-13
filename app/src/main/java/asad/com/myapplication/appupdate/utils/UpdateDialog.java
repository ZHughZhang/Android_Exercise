package asad.com.myapplication.appupdate.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import asad.com.myapplication.appupdate.Service.DownLoadService;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.appupdate.utils
 * @ClassName: UpdateDialog
 * @Autor: Asia
 * @CreateDate: 2019/4/12  16:10
 * @UpdateDate: 2019/4/12  16:10
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
class UpdateDialog {


    public static void show(final Context context, String updateMessage, final String apkUrl) {

        if (isContextValid(context)) {
            new AlertDialog.Builder(context).setTitle("发现新版本").setMessage(updateMessage).setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goToDownload(context, apkUrl);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setCancelable(false).show();
        }

    }


    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }


    private static void goToDownload(Context context, String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownLoadService.class);
        intent.putExtra(Constants.APK_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }
}
