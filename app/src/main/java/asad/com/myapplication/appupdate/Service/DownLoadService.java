package asad.com.myapplication.appupdate.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import asad.com.myapplication.appupdate.utils.ApkUtils;
import asad.com.myapplication.appupdate.utils.AppUtils;
import asad.com.myapplication.appupdate.utils.Constants;
import asad.com.myapplication.appupdate.utils.NotificationHelper;
import asad.com.myapplication.appupdate.utils.StorageUtils;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.appupdate.utils
 * @ClassName: DownLoadService
 * @Autor: Asia
 * @CreateDate: 2019/4/12  17:52
 * @UpdateDate: 2019/4/12  17:52
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class DownLoadService extends IntentService {
    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K

    public DownLoadService() {
        super("DownLoadService");
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        String urlstr = intent.getStringExtra(Constants.APK_DOWNLOAD_URL);
        InputStream _inutStream = null;
        FileOutputStream _outStream = null;
        try {
            URL url  = new URL(urlstr);
            HttpURLConnection _urlConnection = (HttpURLConnection) url.openConnection();
            _urlConnection.setRequestMethod("GET");
            _urlConnection.setDoOutput(false);
            _urlConnection.setReadTimeout(10*1000);
            _urlConnection.setConnectTimeout(10*1000);
            _urlConnection.setRequestProperty("Connection","Kepp-Alive");
            _urlConnection.setRequestProperty("Charset","UTF-8");
            _urlConnection.setRequestProperty("Accept-Encoding","gzip,defate");

            _urlConnection.connect();

            long bytotal = _urlConnection.getContentLength();
            long bystesum =0;
            int byteread = 0;
            _inutStream = _urlConnection.getInputStream();
            File dri = StorageUtils.getPublicDriCtory();
            String apkName = urlstr.substring(urlstr.lastIndexOf("/")+1,urlstr.length());
            File file = new File(dri,apkName);
            _outStream = new FileOutputStream(file);

            byte [] buffer = new byte[BUFFER_SIZE];
            int oldProgess = 0;

            while ((byteread = _inutStream.read(buffer)) !=-1){

                bystesum += byteread;

                _outStream.write(buffer,0,byteread);

                int  progress = (int) (bystesum*100l/bytotal);

                if (progress != oldProgess){
                    notificationHelper.updateProgress(progress);
                }

                oldProgess = progress;
                Log.d("DownLoadService", "onHandleIntent: "+file.getPath());
            }

            ApkUtils.installApk(this,file);
            notificationHelper.cancle();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (_outStream != null) _outStream.close();
                if (_inutStream !=null)_inutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
