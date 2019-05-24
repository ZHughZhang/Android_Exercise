package asad.com.myapplication.rxdownload;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.rxdownload
 * @ClassName: Engine
 * @Autor: Asia
 * @CreateDate: 2019/4/15  17:34
 * @UpdateDate: 2019/4/15  17:34
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
public class Engine {

    private  static Engine Instance;
    private DownloadApi mDownloadAPI;

    static  final Engine  getInstance() {
        if (Instance == null) {
            synchronized ( Engine.class){
                if (Instance == null) {
                    Instance = new Engine();
                }
            }
        }

        return  Instance;
    }

    private Engine () {
        mDownloadAPI = new Retrofit.Builder()
                .baseUrl("http://7xk9dj.com1.z0.glb.clouddn.com/")
                .client(getDwonloadOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DownloadApi.class);

    }

    private  static OkHttpClient getDwonloadOkHttpClient () {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        return response.newBuilder().body(new DowloadResponeBody(response.body())).build();
                    }
                });

        try {
            final TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            } };

            final SSLContext sslContext =  SSLContext.getInstance("TLS");
            sslContext.init(null,trustManagers,new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory).hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.build();

    }
    DownloadApi getDownloafApi() {
        return  mDownloadAPI;
    }

    public  interface  DownloadApi{
        @Streaming
        @GET
        Observable<ResponseBody> downloadFile(@Url String  url);
    }

}
