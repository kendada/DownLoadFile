package cc.http.http;

import android.os.Handler;
import android.os.Message;

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
    private HttpLoader httpLoader;
    private int mMethod; //网络请求方法

    public MNHttpRuunable(int method, String url, CookieParmas cookieParmas, RequestParams parms, HttpListener listener){
        if(listener!=null){
            mHandler = listener.getHandler();
        }
        mMethod = method;
        mUrl = url;
        mCookieParmas = cookieParmas;
        mParms = parms;
    }

    @Override
    public void run() {
        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        mHandler.sendMessage(msg);
        httpLoader = new HttpLoader(mUrl, mCookieParmas, mParms, mHandler);
        switch (mMethod){
            case 1:
                httpLoader.get();
                break;
            case 2:
                httpLoader.post();
                break;
        }
    }

    public void onCancel(){
        httpLoader.onCancel();
    }
}
