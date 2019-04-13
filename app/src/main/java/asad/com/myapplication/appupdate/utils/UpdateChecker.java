package asad.com.myapplication.appupdate.utils;

import android.content.Context;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.appupdate.utils
 * @ClassName: UpdateChecker
 * @Autor: Asia
 * @CreateDate: 2019/4/12  17:56
 * @UpdateDate: 2019/4/12  17:56
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class UpdateChecker {

    public static void checkForDialog (Context context){

            if  (context != null) {
                new CheckUpdateTask(context ,Constants.TYPE_DIALOG,true).execute();
            }

    }


    public static void  checkForNotification (Context context) {

        if (context != null){
                new CheckUpdateTask(context,Constants.TYPE_NOTIFICATION,false).execute();
        }
    }

}
