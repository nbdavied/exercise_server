package com.dw.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class HttpRequest {
    public static String get(String url){
        TrustManager[] tm = {new TrustManager()};
        SSLContext sslContext = null;
        StringBuffer buffer = new StringBuffer();
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL httpUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) httpUrl.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoInput(true);

            InputStream inputStream = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String str = null;
            while((str = bufferedReader.readLine()) != null){
                buffer.append(str);
            }
            bufferedReader.close();
            reader.close();
            inputStream.close();
            conn.disconnect();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return buffer.toString();
    }
}
