package cc.cc.down.util;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-18
 * Time: 17:05
 * Version 1.0
 */

public class MNDownloadManager extends DownloadManager {

    private List<DownRuunable> ruunableList;

    private String tag = MNDownloadManager.class.getSimpleName();

    public MNDownloadManager(){
        super();
        ruunableList = new ArrayList<>();
    }

    @Override
    public void addDownloadRequest(DownloadRequest request) {
        requests.add(request); //添加到集合
        DownRuunable ruunable = new DownRuunable(request);
        executors.submit(ruunable);
        ruunableList.add(ruunable);
    }

    @Override
    public void onPause(int id) {
        Log.i(tag, "---暂停id:" + id);
        for(DownRuunable ruunable : ruunableList){
            Log.i(tag, "----50----" + ruunable);
            if(ruunable.getId()==id){ //此处会有多条相同的ID，全部停掉
                ruunable.onStop();
            }
        }
    }

    @Override
    public void onRestart(int id) {
        DownRuunable ruunable = ruunableList.get(id);
        ruunable.onStart();
    }

    @Override
    public void onPauseAll() {
        for(DownRuunable ruunable : ruunableList){
            Log.i(tag, "----50----"+ruunable);
            ruunable.onStop();
        }
    }

    @Override
    public void onRestartAll() {

    }

    @Override
    public void onCancel(int id) {
        Log.i(tag, "---取消---"+id);
        //取消之前，首先暂停下载
        onPause(id);
        //删除本地文件
        deleteFile(id);
    }

    @Override
    public void onCancelAll() {

    }

    /**
     * 删除本地文件
     * */
    private void deleteFile(int id){
        try {
            DownloadRequest request = requests.get(id);
            File file = new File(request.getSavePath());
            file.delete();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
