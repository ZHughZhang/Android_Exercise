package asad.com.myapplication.rxdownload;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import okio.Timeout;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: DowloadResponeBody
 * @Autor: Asia
 * @CreateDate: 2019/4/15  17:48
 * @UpdateDate: 2019/4/15  17:48
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class DowloadResponeBody  extends ResponseBody {

    private final ResponseBody mResponseBody;

    private BufferedSource mBufferedSource;


    public DowloadResponeBody(ResponseBody responseBody) {
        mResponseBody = responseBody;

    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null ) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }
    private Source source(Source source) {
        return  new ForwardingSource(source) {

            private  long mProgress = 0;
            private long mLastSendTime = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytes = super.read(sink,byteCount);
                mProgress += bytes == -1 ? 0: bytes;
                Log.e("read", "read: "+mProgress );
                if (System.currentTimeMillis() - mLastSendTime > 90 ) {
                    DownloadProcessEvent downloadProcessEvent = new DownloadProcessEvent(contentLength(),mProgress);
                    RxBus.send(downloadProcessEvent);
                    mLastSendTime = System.currentTimeMillis();
                }else  if  (mProgress == contentLength()) {
                    Observable.just(mProgress).delaySubscription(500, TimeUnit.MILLISECONDS, Schedulers.io()).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong)  {
                            DownloadProcessEvent downloadProcessEvent = new DownloadProcessEvent(contentLength(),mProgress);
                           RxBus.send(downloadProcessEvent);
                        }
                    });
                }
                return bytes;
            }

            @Override
            public Timeout timeout() {
                return super.timeout();
            }

            @Override
            public void close() throws IOException {
//                RxUtil.getInstance().unRegister(this);
                super.close();
            }

            @Override
            public String toString() {
                return super.toString();
            }
        };
    }
}
