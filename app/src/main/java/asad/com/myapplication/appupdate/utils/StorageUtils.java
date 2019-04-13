package asad.com.myapplication.appupdate.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.appupdate.utils
 * @ClassName: StorageUtils
 * @Autor: Asia
 * @CreateDate: 2019/4/12  17:53
 * @UpdateDate: 2019/4/12  17:53
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class StorageUtils {

    public static File getCacheDireCtory(Context context) {
        File file = context.getCacheDir();

        if  (file == null) {
            Log.w("StorageUtils", "Can't define system cache directory! The app should be re-installed.");
        }

        return  file;
    }

    public static  File getPublicDriCtory(){
       String  storagePath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "camerademo";

       Log.e("StorageUtils",storagePath+">>>>>>>>>>>>>>");

       File file = new File(storagePath);
       if (!file.exists()){
           file.mkdir();
       }


       return  file;
    }

}
