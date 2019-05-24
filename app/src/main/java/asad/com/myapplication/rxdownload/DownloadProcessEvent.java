package asad.com.myapplication.rxdownload;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: DownloadProcessEvent
 * @Autor: Asia
 * @CreateDate: 2019/4/15  17:19
 * @UpdateDate: 2019/4/15  17:19
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class DownloadProcessEvent {

    private long mProcess;

    private long mTotal;

    public DownloadProcessEvent(long process, long total) {
        mProcess = process;
        mTotal = total;
    }

    public long getProcess() {
        return mProcess;
    }


    public long getTotal() {
        return mTotal;
    }


    public  boolean isNotDownLoadFinished() {
        return  mTotal != mProcess;
    }
}
