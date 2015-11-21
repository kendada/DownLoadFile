package cc.http.util;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.http.http.MNHttpRuunable;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 16:56
 * Version 1.0
 *
 * 线程池管理器：单例模式，只开启一个线程池
 *
 */

public class ExecutorManager {

    private String tag = ExecutorManager.class.getSimpleName();

    private static ExecutorService threadPools = null;

    /**
     * 私有构造方法
     * */
    private ExecutorManager(){
        threadPools = Executors.newFixedThreadPool(5);
    }

    private static class  Inner{
        static ExecutorManager executorManager = new ExecutorManager();
    }

    /**
     * 获取实例
     * */
    public static ExecutorManager getInstance(){
        return Inner.executorManager;
    }

    /**
     * 添加
     * */
    public void addThread(MNRuunable ruunable){
        Log.i(tag, "---45-----threadPool=="+threadPools);
        if(threadPools==null){
            threadPools = Executors.newFixedThreadPool(5);
        }
        threadPools.submit(ruunable);
    }


}
