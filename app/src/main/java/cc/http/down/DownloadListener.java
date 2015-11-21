package cc.http.down;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-18
 * Time: 17:09
 * Version 1.0
 */

public interface DownloadListener {

    //下载成功
    void onSuccess(int id);

    //下载失败
    void onFailure(int id, int errorCode, String errorMessage);

    //下载进度
    void onProgress(int id, int downLoadCount, int length);

    //暂停下载
    void onPause(int id);

}
