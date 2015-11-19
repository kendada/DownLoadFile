package cc.cc.down.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-18
 * Time: 16:53
 * Version 1.0
 */

public abstract class DownloadManager {

    //保存路径
    public String savePath = "mn/down";

    public ExecutorService executors;

    //下载集合
    public List<DownloadRequest> requests;

    public DownloadManager(){
        if(executors==null){
            executors = Executors.newFixedThreadPool(3);
        }
        if(requests==null){
            requests = new ArrayList<>();
        }
    }

    /**
     * 添加下载对象
     * */
    public abstract void addDownloadRequest(DownloadRequest request);


    /**
     * 暂停某一个下载对象
     * */
    public abstract void onPause(int id);

    /**
     * 恢复某一个下载对象的下载
     * */
    public abstract void onRestart(int id);

    /**
     * 暂停全部下载
     * */
    public abstract void onPauseAll();

    /**
     * 恢复全部暂停下载的对象
     * */
    public abstract void onRestartAll();

    /**
     * 取消某一个下载对象
     * */
    public abstract void onCancel(int id);

    /**
     * 取消全部下载
     * */
    public abstract void onCancelAll();




}
