package asad.com.myapplication.rxdownload;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.widget.ProgressBar;

import asad.com.myapplication.R;
import cn.bingoogolapple.progressbar.BGAProgressBar;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: DownloadDialog
 * @Autor: Asia
 * @CreateDate: 2019/4/16  17:47
 * @UpdateDate: 2019/4/16  17:47
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class DownloadDialog extends AppCompatDialog {
    private ProgressBar mProgressBar;

    public DownloadDialog(Context context) {
        super(context, R.style.AppDialogTheme);
        setContentView(R.layout.download_dialog);
        mProgressBar = findViewById(R.id.dp_download_content);
        setCancelable(false);

    }

    public  void setProgress (long process, long maxprogress) {
        Log.e("setProgress",process+">>>>>"+maxprogress +"---->");
        mProgressBar.setMax((int) maxprogress);
        mProgressBar.setProgress((int) process);
    }


    @Override
    public void show() {
        super.show();
        //mProgressBar.setMax(100);
        //mProgressBar.setProgress(0);
    }
}
