package asad.com.myapplication.appupdate.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import asad.com.myapplication.R;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.appupdate.utils
 * @ClassName: CheckUpdateTask
 * @Autor: Asia
 * @CreateDate: 2019/4/12  15:49
 * @UpdateDate: 2019/4/12  15:49
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: APP异步下载
 * @Version: (1.0)
 **/
public class CheckUpdateTask extends AsyncTask<Void,Void,String> {

    private ProgressDialog mDialog;
    private Context mContext;
    private  int mtype;
    private boolean mShowDialog;
    private static final String url = Constants.UPDATE_URL;

    public CheckUpdateTask(Context context, int mtype, boolean showDialog) {
        mContext = context;
        this.mtype = mtype;
        mShowDialog = showDialog;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return HttpUtils.get(url);
    }

    @Override
    protected void onPreExecute() {
       if  (mShowDialog) {
           mDialog = new ProgressDialog(mContext);
           mDialog.setMessage(mContext.getString(R.string.app_name));
           mDialog.show();
       }
    }

    @Override
    protected void onPostExecute(String s) {

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (!TextUtils.isEmpty(s)) {
            paramJson(s);
        }

    }

    private void paramJson (String result){

        try {

            JSONObject obj = new JSONObject(result);
            String updateMessage = obj.getString(Constants.APK_UPDATE_CONTENT);
            String apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
            int apkCode = obj.getInt(Constants.APK_VERSION_CODE);
            int vesionCode  = AppUtils.getVersionCode(mContext);

            if (apkCode > vesionCode) {
                if (mtype == Constants.TYPE_NOTIFICATION) {
                    new NotificationHelper(mContext).showNotification(updateMessage,apkUrl);
                }else  if  (mtype == Constants.TYPE_DIALOG){
                    showDialog(mContext,updateMessage,apkUrl);

                }

            }else  {
                Toast.makeText(mContext, "已是最新,不需要更新", Toast.LENGTH_SHORT).show();
            }

        }catch (JSONException e){
             e.printStackTrace();
        }
    }

    private void showDialog(Context context, String updateMessage, String apkUrl) {
        UpdateDialog.show(mContext,updateMessage,apkUrl);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


}
