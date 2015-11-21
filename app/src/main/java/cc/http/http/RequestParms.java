package cc.http.http;

import android.text.TextUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 20:42
 * Version 1.0
 */

public class RequestParms {

    private ConcurrentHashMap<String, String> urlParms;

    public RequestParms(){
        urlParms = new ConcurrentHashMap<>();
    }

    public void put(String key, String value){
        if(!TextUtils.isEmpty(key)){
            urlParms.put(key, value);
        }
    }




}
