package cc.downloadfile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cc.adapter.TestAdapter;
import cc.cc.down.util.DownloadRequest;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-19
 * Time: 15:30
 * Version 1.0
 */

public class TestActivity1 extends Activity {


    private TestAdapter adapter;

    private List<DownloadRequest> list;


    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        getLists();

        list_view = (ListView)findViewById(R.id.list_view);


        adapter = new TestAdapter(this, list);
        list_view.setAdapter(adapter);

    }

    private void getLists(){
        list = new ArrayList<>();
        for(int i=1; i<5; i++){
            String name = null;
            if (i<10){
                name = "00"+i+".mp3";
            } else {
                name = "0"+i+".mp3";
            }
            String url = "http://t.kanshulou.com/%E8%AF%84%E4%B9%A6/%E8%AF%84%E4%B9%A6%E9%9A%8B%E5%94%90%E6%BC%94%E4%B9%89/"+name;
            /* /mnt/sdcard/Android/data/cc.downloadfile/cache/test.apk 此路径，当app卸载时，包名以后的路径将删除 */
            String savePath = getExternalCacheDir().toString()+"/"+name;
            DownloadRequest request = new DownloadRequest(i, url, savePath, null);

            list.add(request);
        }
    }





}
