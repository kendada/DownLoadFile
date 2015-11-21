package cc.http.http;

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    private String tag = HttpLoader.class.getSimpleName();

    public HttpLoader(String url, Handler handler){
        mUrl = url;
        mHandler = handler;
    }

    public void get(){
        try {
            doGet();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doGet() throws Exception{
        URL url = new URL(mUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //HttpURLConnection的各种设置
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.connect();
        int responseCode = conn.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK){
            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            while((len=is.read(buffer))!=-1){
                if(isCancle){
                    Message msg = mHandler.obtainMessage();
                    msg.what = 4;
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
        //发送完成的消息
        Message msg = mHandler.obtainMessage();
        msg.what = 5;
        Object[] objects = {responseCode};
        msg.obj = objects;
        mHandler.sendMessage(msg);
    }

    /**
     * 取消
     * */
    public void onCancel(){
        isCancle = true;
        Message msg = mHandler.obtainMessage();
        msg.what = 5;
        Object[] objects = {-100};
        msg.obj = objects;
        mHandler.sendMessage(msg);
    }

}
