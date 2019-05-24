package asad.com.myapplication.rxdownload;

import android.app.Application;
import android.util.Log;


/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: AppUtil
 * @Autor: Asia
 * @CreateDate: 2019/4/15  16:09
 * @UpdateDate: 2019/4/15  16:09
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class AppUtil {
    static  final Application mApp;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            Log.e("Application", app.getPackageName()+">>>>>>>>>>>>>>>>" );
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e(AppUtil.class.getSimpleName(), "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(AppUtil.class.getSimpleName(), "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            mApp = app;
        }
    }

    private  AppUtil() {

    }

}
