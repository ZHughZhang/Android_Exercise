package asad.com.myapplication.appupdate.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ProjectName: android_exercise
 * @Package: asad.com.myapplication.appupdate.utils
 * @ClassName: HttpUtils
 * @Autor: Asia
 * @CreateDate: 2019/4/12  16:12
 * @UpdateDate: 2019/4/12  16:12
 * @UpdateUser: (更新人)
 * @UpdateRemark: (更新说明)
 * @Description: (java类作用描述)
 * @Version: (版本号)
 **/
class HttpUtils {
    public static String get(String urlstr) {

        HttpURLConnection urlConnection = null;
        InputStream _inuptStream = null;
        BufferedReader buffer = null;
        String  result = null;

        try {
            URL  url  = new URL(urlstr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            _inuptStream = urlConnection.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(_inuptStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line ;

            while ((line = buffer.readLine()) !=null) {
                stringBuilder.append(line);
            }

            result = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (buffer !=null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (_inuptStream != null) {
                try {
                    _inuptStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (urlConnection != null ) urlConnection.disconnect();
        }


        return result;
    }
}
