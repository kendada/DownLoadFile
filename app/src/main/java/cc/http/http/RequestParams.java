package cc.http.http;

import android.text.TextUtils;

import java.io.File;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 20:42
 * Version 1.0
 */

public class RequestParams {

    //参数：字符串类型
    private ConcurrentHashMap<String, String> urlParms;

    //参数:文件类型
    private ConcurrentHashMap<String, File> fileParms;

    public RequestParams(){
        //初始化
        urlParms = new ConcurrentHashMap<>();
        fileParms = new ConcurrentHashMap<>();
    }

    public void put(String key, String value){
        if(!TextUtils.isEmpty(key)){ //参数名不能为空
            urlParms.put(key, value);
        }
    }

    public void put(String key, File value){
        if(!TextUtils.isEmpty(key)){ //参数名不能为空
            fileParms.put(key, value);
        }
    }

    public ConcurrentHashMap<String, String> getUrlParms(){
        return this.urlParms;
    }

    public ConcurrentHashMap<String, File> getFileParms(){
        return this.fileParms;
    }

    /**
     * get方法组合参数
     * @param url 访问地址
     * */
    public String getStringParams(String url){
        StringBuffer strParams = new StringBuffer();

        Set<String> keys = urlParms.keySet();
        for(String key:keys){
            String value = urlParms.get(key);
            strParams.append("&");
            strParams.append(key+"="+value);
        }

        if(url.indexOf("?")!=-1){
            return url + strParams.toString();
        } else {
            String params = strParams.toString();
            params = params.replaceFirst("&", "?"); //用?替换第一个&
            return url + params;
        }

    }



}
