package cc.cc.down.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-18
 * Time: 17:49
 * Version 1.0
 *
 *  具体执行下载文件
 */

public class FileDownLoader {

    //下载对象
    private DownloadRequest request;

    //是否暂停
    private boolean isStop = false;

    private Handler handler;

    private long downLoadFileLength;

    private InputStream is;
    int len = -1;
    int count = 0;
    int length;

    int sendMagCount = 0; //测试发送消息的次数

    private String tag = File.class.getSimpleName();

    public FileDownLoader(DownloadRequest request){
        this.request = request;
        handler = request.handler;

    }

    public void onStart(){
        getFileLenght();
    }

    private void downLoad() throws IOException {

        File downLoadFile = new File(request.getSavePath());
        downLoadFileLength = downLoadFile.length(); //获取本地已下载的长度

        URL url = new URL(request.getDownUrl());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
        // 设置范围，格式为Range：bytes x-y;
        conn.setRequestProperty("Range", "bytes=" + downLoadFileLength + "-");

        conn.connect();

        if(downLoadFileLength > 0){
            Message msg = handler.obtainMessage();
            msg.what = 2;
            Object[] objects = {(int)downLoadFileLength, length};
            msg.obj = objects;
            handler.sendMessage(msg);
        }

        if(downLoadFileLength == length){
            //下载完成
            Message msg = handler.obtainMessage();
            msg.what = 0;
            handler.sendMessage(msg);
            return;
        }

        RandomAccessFile ras = new RandomAccessFile(request.getSavePath(), "rwd");

        ras.seek(downLoadFileLength);

        if(conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL){
            is = conn.getInputStream();
            byte[] buffer = new byte[1024];
            doDownLoad(ras, buffer, length);
        } else {
            Message msg = handler.obtainMessage();
            msg.what = 1;
            Object[] objects = {conn.getResponseCode(), "下载失败。。。。"};
            msg.obj = objects;
            handler.sendMessage(msg);
        }
    }

    private void doDownLoad(RandomAccessFile ras, byte[] buffer, int length) {
        try {
            while ((len=is.read(buffer))!=-1){
                if(isStop) {
                    Message msg = handler.obtainMessage();
                    msg.what = 3;
                    handler.sendMessage(msg);
                    return; //暂停下载
                }
                sendMagCount++;
                ras.write(buffer, 0, len);
                count += len;
                Message msg = handler.obtainMessage();
                msg.what = 2;
                Object[] objects = {count+(int)downLoadFileLength, length};
                msg.obj = objects;
                handler.sendMessage(msg);
            }
            Log.i(tag, "发送消息的次数="+sendMagCount);
            //下载完成
            Message msg = handler.obtainMessage();
            msg.what = 0;
            handler.sendMessage(msg);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(is!=null) {
                    is.close();
                }
                if(ras!=null){
                    ras.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件大小
     * */
    private void getFileLenght(){

        try{

            URL url = new URL(request.getDownUrl());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            length =  conn.getContentLength();

            conn.disconnect();


            downLoad(); //开始下载

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onStop(boolean isStop){
        this.isStop = isStop;
    }

    public void onStart(boolean isStop){
        this.isStop = isStop;
        onStart();
    }

}
