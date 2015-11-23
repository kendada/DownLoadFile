package cc.http.http;

import android.os.Handler;
import android.os.Message;

import java.util.Map;

import cc.http.util.MNRuunable;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 21:14
 * Version 1.0
 */

public class MNHttpRuunable extends MNRuunable {

    private Handler mHandler;
    private String mUrl;
    private CookieParmas mCookieParmas;
    private RequestParams mParms;
    private Map<String, String> mHearderMap;
    private HttpLoader httpLoader;
    private int mMethod; //网络请求方法

    /**
     * 构造方法
     * @param method 请求方法
     * @param url 请求地址
     * @param hearderMap 设置头部信息
     * @param cookieParmas Cookie信息
     * @param parms 参数
     * @param listener 回调方法，更新UI
     * */
    public MNHttpRuunable(int method, String url, Map<String, String> hearderMap,
                          CookieParmas cookieParmas, RequestParams parms, HttpListener listener){
        if(listener!=null){
            mHandler = listener.getHandler();
        }
        mMethod = method;
        mUrl = url;
        mHearderMap = hearderMap;
        mCookieParmas = cookieParmas;
        mParms = parms;
    }

    @Override
    public void run() {
        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        mHandler.sendMessage(msg);
        httpLoader = new HttpLoader(mUrl, mHearderMap, mCookieParmas, mParms, mHandler);
        switch (mMethod){ //判断执行什么方法
            case HttpUtil.METHOD_GET:
                httpLoader.get();
                break;
            case HttpUtil.METHOD_POST:
                httpLoader.post();
                break;
        }
    }

    public void onCancel(){
        httpLoader.onCancel();
    }
}
