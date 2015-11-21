package cc.downloadfile;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import cc.http.down.DownloadListener;
import cc.http.down.DownloadManager;
import cc.http.down.DownloadRequest;
import cc.http.down.MNDownloadManager;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-18
 * Time: 17:19
 * Version 1.0
 */

public class TestActivity extends Activity implements View.OnClickListener{


    private Button down_btn, pause_btn;

    private ProgressBar bar;

    private TextView text;

    private DownloadManager manager;

    private int mProgress = 0;

    NotificationManager notificationManager = null;
    NotificationCompat.Builder mBuilder = null;


    private String tag = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = (ProgressBar)findViewById(R.id.bar);

        text = (TextView)findViewById(R.id.text);

        down_btn = (Button)findViewById(R.id.down_btn);
        down_btn.setOnClickListener(this);
        pause_btn = (Button)findViewById(R.id.pause_btn);
        pause_btn.setOnClickListener(this);

        manager = new MNDownloadManager();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pause_btn:
                manager.onPause(0);
                down_btn.setEnabled(true);
                pause_btn.setEnabled(false);
                break;
            case R.id.down_btn:
                down_btn.setEnabled(false);
                pause_btn.setEnabled(true);
                createNotification();
                down();
                break;

        }
    }

    private void down(){
        String url = "http://www.apk.anzhi.com/data3/apk/201511/03/3275fe9aef1f452025757d892f844bd6_10199900.apk";
        /* /mnt/sdcard/Android/data/cc.downloadfile/cache/test.apk 此路径，当app卸载时，包名以后的路径将删除 */
        String savePath = getExternalCacheDir().toString()+"/test.apk";
        DownloadRequest request = new DownloadRequest(0, url, savePath, new DownloadListener() {
            @Override
            public void onSuccess(int id) {
                Toast.makeText(TestActivity.this, "下载完成。。。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int id, int errorCode, String errorMessage) {
                Log.i(tag, "--id="+id+"---errorCode="+errorCode+"----errorMessage="+errorMessage);
            }

            @Override
            public void onProgress(int id, int downLoadCount, int length) {
                int tempProgress = downLoadCount * 100 / length;
                bar.setProgress(tempProgress);
                text.setText("" + tempProgress);
                if(tempProgress%20==0){
                    //showNotification(downLoadCount * 100 / length);
                }
            }

            @Override
            public void onPause(int id) {
                Toast.makeText(TestActivity.this, "暂停下载。。。", Toast.LENGTH_SHORT).show();
            }
        });

        manager.addDownloadRequest(request);

    }

    /**
     * 创建通知
     * */
    private void createNotification(){
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("标题") //标题
                .setContentText("正在更新app")  //内容
                .setWhen(System.currentTimeMillis()) //获取系统时间
                .setPriority(Notification.PRIORITY_DEFAULT) //优先级
                .setOngoing(true)
                .setAutoCancel(true) //用户点击取消
        //        .setDefaults(Notification.DEFAULT_VIBRATE) //发送消息时添加震动，闪光等:需要权限
        //        .setProgress(100, 0, false)
                .setSmallIcon(R.mipmap.ic_launcher);
        createNotificationView(); //自定义通知View
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());
    }

    //自定义通知View
    private void createNotificationView(){
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_item);

        mBuilder.setContent(remoteViews);

    }

    //更新进度条
    private void showNotification(int progress){
        mBuilder.setProgress(100, progress, false);
        notificationManager.notify(0, mBuilder.build());
    }


}
