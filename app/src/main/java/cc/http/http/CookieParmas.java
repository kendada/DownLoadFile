package cc.http.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;
import java.util.Set;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-22
 * Time: 12:26
 * Version 1.0
 */

public class CookieParmas {

    private Context mContext;
    private SharedPreferences mPreferences;

    private final static String COOKIE_NAME = "cookie";

    private boolean savedCookie = false; //是否已经保存了cookie信息

    private StringBuffer cookieStringBuffer; //字符串Cookie

    private String tag = CookieParmas.class.getSimpleName();

    public CookieParmas(Context context){
        mContext = context;
        cookieStringBuffer = new StringBuffer();
        init();
    }

    /**
     * 初始化
     * */
    private void init(){
        mPreferences = mContext.getSharedPreferences(COOKIE_NAME, Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
        Map<String, ?> keyMap = mPreferences.getAll();
        Set<String> keys = keyMap.keySet();
        if(keys!=null && keys.size()>0){ //判断cookie信息是否为空
            savedCookie = true;
        } else {
            savedCookie = false;
        }
        for(String key:keys){ //自动获取SharePreferences保存的键值对
            String value = mPreferences.getString(key, null);
            cookieStringBuffer.append(key+"="+value+";");
        }
    }

    /**
     * 保存Cookie信息
     * */
    public void saveCookieItem(String key, String value){
        SharedPreferences.Editor ed = mPreferences.edit();
        ed.putString(key, value);
        ed.commit();
    }

    /**
     * 清除Cookiey已经保存的Cookie信息
     * */
    public void clearCookie(){
        SharedPreferences.Editor ed = mPreferences.edit();
        ed.clear();
        ed.commit();
    }

    /**
     * Cookie信息是否已经保存
     * */
    public boolean isSavedCookie() {
        return savedCookie;
    }

    /**
     * 获取Cookie
     * */
    public String getCookie(){
        return this.cookieStringBuffer.toString();
    }
}
