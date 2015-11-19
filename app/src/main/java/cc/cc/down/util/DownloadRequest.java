package cc.cc.down.util;

import android.os.Handler;
import android.os.Message;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-18
 * Time: 16:48
 * Version 1.0
 */

public class DownloadRequest {

    //下载对象的ID
    private int id;

    //下载地址
    private String downUrl;

    //保存路径
    private String savePath;

    //下载监听
    private DownloadListener downloadListener;

    /**
     * @param id    单个下载可不传
     * @param downUrl 必传参数：下载地址
     * */
    public DownloadRequest(int id, String downUrl, String savePath){
        this(id, downUrl, savePath, null);
    }

    /**
     * @param id 单个下载可不传
     * @param downUrl 必传参数
     * @param  downloadListener 下载监听器，可不传
     * */
    public DownloadRequest(int id, String downUrl, String savePath, DownloadListener downloadListener) {
        setId(id);
        setDownUrl(downUrl);
        setSavePath(savePath);
        setDownloadListener(downloadListener);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public DownloadListener getDownloadListener() {
        return downloadListener;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Object[] objects = (Object[]) msg.obj;
            switch (msg.what){
                case 0: //下载成功
                    if(downloadListener!=null){
                        downloadListener.onSuccess(id);
                    }
                    break;
                case 1: //下载失败
                    if(downloadListener!=null){
                        downloadListener.onFailure(id, (int) objects[0], String.valueOf(objects[1]));
                    }
                    break;
                case 2: //下载进度
                    if(downloadListener!=null){
                        downloadListener.onProgress(id, (int)objects[0], (int)objects[1]);
                    }
                    break;
                case 3: //暂停
                    if(downloadListener!=null){
                        downloadListener.onPause(id);
                    }
                    break;
            }
        }
    };
}
