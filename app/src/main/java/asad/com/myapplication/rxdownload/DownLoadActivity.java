package asad.com.myapplication.rxdownload;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;

import asad.com.myapplication.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: DownLoadActivity
 * @Autor: Asia
 * @CreateDate: 2019/4/16  17:44
 * @UpdateDate: 2019/4/16  17:44
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class DownLoadActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int  RC_PERMISSION_DOWNLOAD = 1;

    private  static  final  int  RC_PERMISSION_DELETE = 2;

private  DownloadDialog mDownloadDialog;

private String mNameVersion = "1.0.3";

private String mApkUrl = "https://qd.myapp.com/myapp/qqteam/Androidlite/qqlite_3.7.1.704_android_r110206_GuanWang_537057973_release_10000484.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxUtil.getInstance().register(this);

        findViewById(R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dowloadApkFile();
            }
        });

        findViewById(R.id.nitification_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delFile();
            }
        });
        DownloadProcess();

    }

    public void  DownloadProcess () {

        RxBus.getDownloadEventObservable().subscribe(new Observer<DownloadProcessEvent>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DownloadProcessEvent downloadProcessEvent) {
                Log.e("onNext",downloadProcessEvent.getProcess()+">>>>");
                if (mDownloadDialog != null && mDownloadDialog.isShowing() && downloadProcessEvent.isNotDownLoadFinished()){
                    Log.e("onNext",downloadProcessEvent.getProcess()+">>>>");
                    mDownloadDialog.setProgress(downloadProcessEvent.getProcess(),downloadProcessEvent.getTotal());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("onError",e.toString());
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
            finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);

    }


    @AfterPermissionGranted(RC_PERMISSION_DELETE)
    public void delFile() {
        String [] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this,perms)) {
            UpgradeUtil.delOldApk();
        }else  {
            EasyPermissions.requestPermissions(this,"请授予权限",RC_PERMISSION_DELETE,perms);
        }
    }


    @AfterPermissionGranted(RC_PERMISSION_DOWNLOAD)
    public void dowloadApkFile () {
            String [] perms = { Manifest.permission.WRITE_EXTERNAL_STORAGE };

            if (EasyPermissions.hasPermissions(this,perms)) {
                if (UpgradeUtil.isAPkDownloaded(mNameVersion)) {
                    return;
                }


                UpgradeUtil.downloodApk(mApkUrl,mNameVersion).subscribe( new Observer<File>() {
                                                @Override
                                                public void onSubscribe(Disposable d) {
                                                    showDialogDialog();
                                                }

                                                @Override
                                                public void onNext(File file) {
                                                    Log.e("onNext", "onNext:---->getAbsolutePath:"+file.getAbsolutePath() );
                                                    if (file != null) {
                                                        UpgradeUtil.installApk(file);
                                                    }else {

                                                    }
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Log.e("onError",e.toString()+">>>>>>>>>>>>>>>>>>>>");
                                                }

                                                @Override
                                                public void onComplete() {
                                                    dismissDownloadDialog();
                                                }
                                            });
            }else  {
                EasyPermissions.requestPermissions(this,"需要授予权限 ",RC_PERMISSION_DOWNLOAD, perms);
            }
    }

    private  void showDialogDialog() {
        if (mDownloadDialog == null) {
            mDownloadDialog = new DownloadDialog(this);
        }

        mDownloadDialog.show();
    }

    private void dismissDownloadDialog() {
        if (mDownloadDialog != null) {
            mDownloadDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        RxUtil.getInstance().unRegister(this);
        super.onDestroy();

    }
}
