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
    private HttpListener mListener;
    private HttpLoader httpLoader;

    public MNHttpRuunable(String url, HttpListener listener){
        if(listener!=null){
            mHandler = listener.getHandler();
        }
        mUrl = url;
        mListener = listener;
    }

    @Override
    public void run() {
        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        mHandler.sendMessage(msg);
        httpLoader = new HttpLoader(mUrl, mHandler);
        httpLoader.get();
    }

    public void onCancel(){
        httpLoader.onCancel();
    }

    public MNHttpRuunable getRuunable(){
        return this;
    }


}
