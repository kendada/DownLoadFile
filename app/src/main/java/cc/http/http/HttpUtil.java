package cc.http.http;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import cc.http.util.ExecutorManager;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 17:38
 * Version 1.0
 *
 *
 * 基于HttpURLConnection的http请求：get和post
 */

public class HttpUtil {

    private ExecutorManager manager;

    private Map<String, MNHttpRuunable> mnHttpRuunableMap;

    private CookieParmas mCookieParmas; //设置cookie信息

    private Map<String, String> mHeaderMap; //保存头部信息


    public final static int METHOD_GET = 1;
    public final static int METHOD_POST = 2;

    private String tag = HttpUtil.class.getSimpleName();

    public HttpUtil(){
        manager = ExecutorManager.getInstance();
        mnHttpRuunableMap = new HashMap<>();
        mHeaderMap = new HashMap<>();
    }

    public void get(String url, HttpListener listener){
        get(url, null, listener);
    }

    public void get(String url, RequestParams parms, HttpListener listener){
        if(parms!=null){
            url = parms.getStringParams(url);
            Log.i(tag, "----46---"+url);
        }
        doGet(url, parms, listener);
    }

    /**
     * 执行http get方法
     * */
    private void doGet(String url, RequestParams parms, HttpListener listener){
        MNHttpRuunable ruunable = new MNHttpRuunable(METHOD_GET, url, mHeaderMap, mCookieParmas, null, listener);
        manager.addThread(ruunable);
        mnHttpRuunableMap.put(url, ruunable);
    }

    /**
     * 无参post方法
     * */
    public void post(String url, HttpListener listener){
        doPost(url, null, listener);
    }

    public void post(String url, RequestParams parms, HttpListener listener){
        doPost(url, parms, listener);
    }

    /**
     * 执行http post方法
     * */
    private void doPost(String url, RequestParams parms, HttpListener listener){
        MNHttpRuunable ruunable = new MNHttpRuunable(METHOD_POST, url, mHeaderMap, mCookieParmas, parms, listener);
        manager.addThread(ruunable);
        mnHttpRuunableMap.put(url, ruunable);
    }

    /**
     * 取消请求
     * */
    public void onCancel(String url){
        MNHttpRuunable ruunable = mnHttpRuunableMap.get(url);
        if(ruunable!=null){
            ruunable.onCancel();
        }
    }

    /**
     * 设置头部信息
     * */
    public void setHeader(String key, String value){
        mHeaderMap.put(key, value);
    }

    /**
     * 清除头部信息
     * */
    public void clearHeader(){
        mHeaderMap.clear();
    }

    public CookieParmas getCookieParmas(){
        return this.mCookieParmas;
    }

    public void setCookieParmas(CookieParmas cookieParmas){
        this.mCookieParmas = cookieParmas;
    }



}
