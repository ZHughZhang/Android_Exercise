package asad.com.myapplication.rxdownload;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: UpgradeUtil
 * @Autor: Asia
 * @CreateDate: 2019/4/15  17:09
 * @UpdateDate: 2019/4/15  17:09
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (应用升级工具类)
 * @Version: (版本号)
 **/
public class UpgradeUtil {

    private  static final String MIME_TYPE_APK = "application/vnd.andrid.package-archive";

    private  UpgradeUtil() {

    }

    public static Observable<DownloadProcessEvent> getDownLoadProcessEventObservable(Observable<DownloadProcessEvent> observable) {

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public static  boolean isAPkDownloaded(String version) {
        File apkFile = StorageUtil.getApkFile(version);

        if  (apkFile.exists()) {
           // installApk(apkFile);

            return true;
        }

        return false;
    }

    public static  Observable<File> downloodApk(final String url, final String version) {
        return  Engine.getInstance().getDownloafApi().downloadFile(url).map(new Function<ResponseBody, InputStream>() {
            @Override
            public InputStream apply(ResponseBody responseBody) throws Exception {
                return responseBody.byteStream();
            }
        }).concatMap(new Function<InputStream, ObservableSource<? extends File>>() {
            @Override
            public ObservableSource<? extends File> apply(InputStream inputStream) throws Exception {
                final File file = StorageUtil.saveApk(inputStream,version);
                return new ObservableSource<File>() {
                    @Override
                    public void subscribe(Observer<? super File> observer) {

                        if (file == null) observer.onError(new NullPointerException());
                        observer.onNext(file);
                        observer.onComplete();
                    }
                };
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public static void installApk (File apkFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
       //intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            uri = FileProvider.getUriForFile(AppUtil.mApp,AppUtil.mApp.getPackageName()+".update.fileprovider",apkFile);
            intent.setDataAndType(uri,MIME_TYPE_APK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            intent.setDataAndType(Uri.fromFile(apkFile),MIME_TYPE_APK);
        }
        List<ResolveInfo> resolveLists = AppUtil.mApp.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // 然后全部授权
        for (ResolveInfo resolveInfo : resolveLists) {
            String packageName = resolveInfo.activityInfo.packageName;
            AppUtil.mApp.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        AppUtil.mApp.startActivity(intent);
//        if (AppUtil.mApp.getPackageManager().queryIntentActivities(intent,0).size() > 0) {
//            AppUtil.mApp.startActivity(intent);
//        }

    }

    private static  String getFileProviderAuthority () {
        try {
            for (ProviderInfo providerInfo : AppUtil.mApp.getPackageManager().getPackageInfo(AppUtil.mApp.getPackageName(), PackageManager.GET_PROVIDERS).providers){
                if (FileProvider.class.getName().equals(providerInfo.name) && providerInfo.authority.endsWith(".update.fileprovider")){
                        return providerInfo.authority;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return  null;
    }


    public static void delOldApk () {
        File apkDir = StorageUtil.getApkFileDir();

        if (apkDir == null || apkDir.listFiles() == null || apkDir.listFiles().length == 0) {
            return;
        }

        StorageUtil.delFile(apkDir);
    }





}
