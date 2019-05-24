package asad.com.myapplication.rxdownload;

import android.content.pm.PackageManager;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: StorageUtil
 * @Autor: Asia
 * @CreateDate: 2019/4/15  18:16
 * @UpdateDate: 2019/4/15  18:16
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class StorageUtil {

    private static  final String DIR_NAME_APK = "camerademo";

    private StorageUtil() {

    }

    static File getApkFileDir() {
        return AppUtil.mApp.getExternalFilesDir(DIR_NAME_APK);
    }

    static File getApkFile(String version) {
            String appName;

        try {
            appName = AppUtil.mApp.getPackageManager().getPackageInfo(AppUtil.mApp.getPackageName(),0).applicationInfo.loadLabel(AppUtil.mApp.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
           appName = "";
        }
        Log.e("getApkFile",getApkFileDir().getAbsolutePath());

        return  new File(getApkFileDir(),appName+"_v"+version+".apk");
    }

    static  File saveApk (InputStream is,String version) {

        File file = getApkFile(version);

        if  (writeFile(file,is)){
            return file;
        }else  {
            return null;
        }

    }

    static boolean writeFile(File file,InputStream is) {
        OutputStream os = null;

        try {
            os = new FileOutputStream(file);

            byte data[] = new byte[1024];
            int length = -1;

            while ((length = is.read(data) ) != -1) {
                os.write(data,0,length);
            }

            os.flush();
            return true;
        } catch (IOException e) {
            if (file != null && file.exists()) {
                file.deleteOnExit();
            }

            e.printStackTrace();
        } finally {
           closeStream(os);
           closeStream(is);
        }
        return false;

    }

    public static void delFile (File file) {
        try {
            if (file == null || !file.exists()) {
                return;
            }

            if (file.isDirectory()) {
                File[] files = file.listFiles();

                if (files != null && files.length>0) {
                    for (File f: files) {
                        if (f.isDirectory()){
                            delFile(f);
                        }else  {
                            f.delete();
                        }
                    }
                }
            }else {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    static void  closeStream (Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

}
