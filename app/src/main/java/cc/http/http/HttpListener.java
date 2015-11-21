package cc.http.http;

import android.os.Handler;
import android.os.Message;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 17:52
 * Version 1.0
 *
 * 网络访问监听器
 *
 */

public abstract class HttpListener {

    //开始
    public abstract void onStart();

    //成功
    public abstract void onSuccess(int code, String result);

    //失败
    public abstract void onFilure(int errorCode, String errorMessage);

    //进度
    public abstract void onProgress(int code, int progress, int totalCount);

    //取消
    public abstract void onCancel();

    /**
     * 结束
     * @param code 可以用与判断是否成功:小于0表示取消
     * */
    public abstract void onFinish(int code);


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Object[] objects = (Object[]) msg.obj;
            switch (msg.what){
                case 0: //开始
                    onStart();
                    break;
                case 1: //成功
                    onSuccess((int)objects[0], (String)objects[1]);
                    break;
                case 2: //失败
                    onFilure((int)objects[0], (String)objects[1]);
                    break;
                case 3: //进度
                    onProgress((int)objects[0], (int)objects[1], (int)objects[2]);
                    break;
                case 4: //取消
                    onCancel();
                    break;
                case 5: //结束
                    onFinish((int)objects[0]);
                    break;

            }
        }
    };

    public Handler getHandler(){
        return this.handler;
    }

}
