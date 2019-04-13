package asad.com.myapplication.appupdate.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import asad.com.myapplication.R;
import asad.com.myapplication.appupdate.Service.DownLoadService;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.appupdate.utils
 * @ClassName: NotificationHelper
 * @Autor: Asia
 * @CreateDate: 2019/4/12  16:07
 * @UpdateDate: 2019/4/12  16:07
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (通知帮助类)
 * @Version: (1.0)
 **/
public  class NotificationHelper extends ContextWrapper {

    private NotificationManager mManager;

    private static String CHANNEL_ID = "app_update";

    private final static int   NOTIFI_ID =0x001;



    public NotificationHelper(Context context) {
            super(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel Channel = new NotificationChannel(CHANNEL_ID,"更新应用",NotificationManager.IMPORTANCE_LOW);
            Channel.setDescription("新版本");
            Channel.enableLights(true); //在桌面显示小红点
            getManager().createNotificationChannel(Channel);

        }
    }

    public void showNotification(String updateMessage, String apkUrl) {

        Intent intent = new Intent(this, DownLoadService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.APK_DOWNLOAD_URL,apkUrl);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder  =  getNofity(updateMessage).setContentIntent(pendingIntent);
        getManager().notify(NOTIFI_ID,builder.build());

    }

    public void updateProgress(int progress){
        String text = this.getString(R.string.app_download, progress);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,new Intent(),PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = getNofity(text).setProgress(100,progress,false).setContentIntent(pendingIntent);
        getManager().notify(NOTIFI_ID,builder.build());
    }

    private NotificationCompat.Builder getNofity(String text) {
        return  new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setTicker("发现新版本,点击进行升级")
                .setContentTitle("应用更新")
                .setContentText(text)
                .setSmallIcon(getSmallIcon())
                .setLargeIcon(getLargeIcon())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_LOW);
    }

    public void cancle(){
        getManager().cancel(NOTIFI_ID);
    }

    private int  getSmallIcon(){
        int icon = getResources().getIdentifier("mipush_small_notification","drawable",getPackageName());
        if (icon == 0) icon = getApplicationInfo().icon;

        return icon;
    }

    private Bitmap getLargeIcon(){

        int bigIcon = getResources().getIdentifier("mipush_notification","drawable",getPackageName());

        if  (bigIcon != 0 ) return BitmapFactory.decodeResource(getResources(),bigIcon);

        return null;

    }
    private  NotificationManager getManager() {
        if (mManager == null )  mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return  mManager;
     }
}
