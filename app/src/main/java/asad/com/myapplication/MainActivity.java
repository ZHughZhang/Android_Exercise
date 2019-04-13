package asad.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import asad.com.myapplication.appupdate.utils.ApkUtils;
import asad.com.myapplication.appupdate.utils.AppUtils;
import asad.com.myapplication.appupdate.utils.StorageUtils;
import asad.com.myapplication.appupdate.utils.UpdateChecker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    UpdateChecker.checkForDialog(MainActivity.this);




            }
        });

        findViewById(R.id.nitification_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(StorageUtils.getPublicDriCtory()+"/android-auto-update-v1.3.apk");


                if (file == null && !file.exists()){
                    ApkUtils.installApk(MainActivity.this,file);
                }else {

                }

                if (file.exists()){
                    UpdateChecker.checkForNotification(MainActivity.this);
                }else {

                }

            }
        });

        ((TextView) findViewById(R.id.version_tv)).setText("当前版本信息: versionName = " + AppUtils.getVersionName(this) + " versionCode = " + AppUtils.getVersionCode(this));

    }
}
