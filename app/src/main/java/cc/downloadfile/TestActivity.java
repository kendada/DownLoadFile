package cc.downloadfile;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import cc.cc.down.util.DownloadListener;
import cc.cc.down.util.DownloadManager;
import cc.cc.down.util.DownloadRequest;
import cc.cc.down.util.MNDownloadManager;

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


    private NotificationManager notificationManager;
    private Notification notification;
    private PendingIntent pendingIntent, pendingIntent_stop,
            pendingIntent_restart, pendingIntent_clear;
    private RemoteViews contentView;
    private String app_name = "test.apk";


    private int mProgress = 0;


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
                bar.setProgress(downLoadCount * 100 / length);
                text.setText("" + bar.getProgress());

                if((downLoadCount * 100 / length)%20==0){
                    // 改变通知栏
                    contentView.setTextViewText(R.id.notificationPercent,
                            downLoadCount * 100/ length + "%");
                    contentView.setProgressBar(R.id.notificationProgress, 100,
                            downLoadCount * 100/ length, false);

                    notification.contentView = contentView;

                    notificationManager.notify(R.layout.notification_item,
                            notification);
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
     * 创建通知消息
     * **/
    public void createNotification() {

        notification = new Notification(R.mipmap.ic_launcher, app_name
                + getString(R.string.is_downing), System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        /** 自定义Notification样式 ***/
        contentView = new RemoteViews(getPackageName(),
                R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, app_name
                + getString(R.string.is_downing));
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        notification.contentView = contentView;

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.layout.notification_item, notification);
    }

}
