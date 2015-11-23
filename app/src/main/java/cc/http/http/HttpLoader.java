package cc.http.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 21:24
 * Version 1.0
 */

public class HttpLoader {

    private String mUrl;

    private Handler mHandler;

    private boolean isCancle = false;

    //定义数据分割线
    private String BOUNDARY = "----------MNHttpUtil---------";

    //定义最后的分割线
    private byte[] END_DATA = ("\r\n--"+BOUNDARY+"--\r\n").getBytes();

    //参数
    private RequestParams mRequestParams;

    //Cookie信息
    private CookieParmas mCookieParmas;

    private Map<String, String> mHearderMap;
    private ConcurrentHashMap<String, String> urlParms;
    private ConcurrentHashMap<String, File> fileParms;

    private String tag = HttpLoader.class.getSimpleName();

    public HttpLoader(String url, Map<String, String> hearderMap, CookieParmas cookieParmas, RequestParams requestParams, Handler handler){
        mUrl = url;
        mHearderMap = hearderMap;
        mCookieParmas = cookieParmas;
        mHandler = handler;
        mRequestParams = requestParams;
        if(requestParams !=null){
            urlParms = mRequestParams.getUrlParms();
            fileParms = mRequestParams.getFileParms();
        }
    }

    public void get(){
        try {
            //判断是Http协议还是Https协议
            if(mUrl.indexOf("https")!=-1){
                //https协议
                doGet(getHttpsURLConnection("GET"));
            } else {
                //http协议
                doGet(getHttpURLConnection("GET"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void post(){
        try {
            //判断是Http协议还是Https协议
            if(mUrl.indexOf("https")!=-1){
                //https协议
                doPost(getHttpsURLConnection("POST"));
            } else {
                //http协议
                doPost(getHttpURLConnection("POST"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 具体执行get操作
     * */
    private void doGet(HttpURLConnection conn){
        int responseCode = -1000;
        try{
            conn.connect();
            responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream is = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                while((len=is.read(buffer))!=-1){
                    if(isCancle){
                        Message msg = mHandler.obtainMessage();
                        msg.what = 5;
                        Object[] objects = {-100};
                        msg.obj = objects;
                        mHandler.sendMessage(msg);
                        return;
                    }
                    os.write(buffer);
                }
                String res = os.toString();
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                Object[] objects = {responseCode, res};
                msg.obj = objects;
                mHandler.sendMessage(msg);
            } else { //请求失败
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                Object[] objects = {responseCode, "网络请求失败。。。"};
                msg.obj = objects;
                mHandler.sendMessage(msg);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //发送完成的消息
            Message msg = mHandler.obtainMessage();
            msg.what = 5;
            Object[] objects = {responseCode};
            msg.obj = objects;
            mHandler.sendMessage(msg);
        }

    }

    /**
     * 具体执行post请求
     * */
    private void doPost(HttpURLConnection conn) {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        int responseCode = -1000;
        try {
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            conn.connect();

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            StringBuffer params = new StringBuffer();
            if(urlParms!=null){
                Set<String> keys = urlParms.keySet();
                for(String key : keys){
                    String value = urlParms.get(key);
                    params.append("--" + BOUNDARY + "\r\n");
                    params.append("Content-Disposition: form-data; name=\""+key+"\"\r\n\r\n");
                    params.append(value);
                    params.append("\r\n");
                }
            }
            out.write(params.toString().getBytes()); //写入数据
            if(fileParms!=null){
                Set<String> keys = fileParms.keySet();
                for(String key : keys){
                    File file = fileParms.get(key);
                    StringBuilder sb = new StringBuilder();
                    sb.append("-----");
                    sb.append(BOUNDARY);
                    sb.append("\r\n");
                    sb.append("Content-Disposition: form-data; name=\""+key+"\"; filename=\"" + file.getName() + "\"\r\n");
                    sb.append("Content-Type: application/octet-stream\r\n\r\n");
                    Log.i(tag, "---160---" + sb.toString());
                    out.write(sb.toString().getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len=in.read(buffer))!=-1){
                        out.write(buffer, 0, len);
                    }
                    out.write("\r\n".getBytes());
                }
            }
            out.write(END_DATA);
            out.flush();
            out.close();

            Log.i(tag, "----173----\r\n" + params);

            responseCode = conn.getResponseCode();
            if(mCookieParmas!=null && !mCookieParmas.isSavedCookie()){ //是否已经保存了Cookie信息
                CookieStore cookieStore = cookieManager.getCookieStore();
                List<HttpCookie> cookies = cookieStore.getCookies();
                for(HttpCookie cookie:cookies){
                    Log.i(tag, "---179----"+cookie);
                    mCookieParmas.saveCookieItem(cookie.getName(), cookie.getValue());
                }
            }

            InputStream is = null;
            ByteArrayOutputStream os = null;
            if(responseCode == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                os = new ByteArrayOutputStream();
                while ((len=is.read(buffer))!=-1) {
                    os.write(buffer);
                }
                String res = os.toString();
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                Object[] objects = {responseCode, res};
                msg.obj = objects;
                mHandler.sendMessage(msg);
            } else {
                Message msg = mHandler.obtainMessage();
                msg.what = 2;
                Object[] objects = {responseCode, "网络请求失败。。。"};
                msg.obj = objects;
                mHandler.sendMessage(msg);
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //发送完成的消息
            Message msg = mHandler.obtainMessage();
            msg.what = 5;
            Object[] objects = {responseCode};
            msg.obj = objects;
            mHandler.sendMessage(msg);
        }

    }

    /**
     * Http请求
     * */
    private HttpURLConnection getHttpURLConnection(String method) throws Exception{
        URL url = new URL(mUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        //HttpURLConnection设置
        connection.setRequestMethod(method);
        connection.setRequestProperty("Charsert", "UTF-8");
        connection.setConnectTimeout(10 * 1000);
        connection.setRequestProperty("Connection", "keep-live");
        if(mHearderMap!=null){
            Set<String> keys = mHearderMap.keySet();
            for(String key:keys){ //设置头部信息
                String value = mHearderMap.get(key);
                Log.i(tag, key+"=="+value);
                connection.setRequestProperty(key, value);
            }
        }
        connection.setDoInput(true); // 允许输入
        connection.setDoOutput(true); // 允许输出
        connection.setUseCaches(false); ///不允许使用缓存
        if(mCookieParmas!=null && mCookieParmas.isSavedCookie()) { //判断Cookie是否为空
            connection.setRequestProperty("Cookie", mCookieParmas.getCookie());
        }

        return connection;
    }

    /**
     * Https请求
     * */
    private HttpsURLConnection getHttpsURLConnection(String method) throws Exception{
        URL url = new URL(mUrl);
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        //HttpsURLConnection设置
        connection.setRequestMethod(method);
        connection.setRequestProperty("Charsert", "UTF-8");
        connection.setConnectTimeout(10 * 1000);
        if(mHearderMap!=null){
            Set<String> keys = mHearderMap.keySet();
            for(String key:keys){ //设置头部信息
                String value = mHearderMap.get(key);
                connection.setRequestProperty(key, value);
            }
        }
        //创建SSL
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new MnTrustManager()}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new MnHostnameVerifier());

        connection.setSSLSocketFactory(sslContext.getSocketFactory());

        return connection;
    }

    /**
     * https 忽略所有证书
     * */
    private class MnHostnameVerifier implements HostnameVerifier{

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

    /**
     * https 忽略所有证书
     * */
    private class MnTrustManager implements X509TrustManager{

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


    /**
     * 取消
     * */
    public void onCancel(){
        isCancle = true;
        Message msg = mHandler.obtainMessage();
        msg.what = 4;
        mHandler.sendMessage(msg);
    }

}
