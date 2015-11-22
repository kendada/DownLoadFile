package cc.http.http;

import android.text.TextUtils;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 20:42
 * Version 1.0
 */

public class RequestParams {

    private ConcurrentHashMap<String, String> urlParms;

    private ConcurrentHashMap<String, File> fileParms;

    public RequestParams(){
        urlParms = new ConcurrentHashMap<>();
        fileParms = new ConcurrentHashMap<>();
    }

    public void put(String key, String value){
        if(!TextUtils.isEmpty(key)){
            urlParms.put(key, value);
        }
    }

    public void put(String key, File value){
        if(!TextUtils.isEmpty(key)){
            fileParms.put(key, value);
        }
    }

    public ConcurrentHashMap<String, String> getUrlParms(){
        return this.urlParms;
    }

    public ConcurrentHashMap<String, File> getFileParms(){
        return this.fileParms;
    }




}
