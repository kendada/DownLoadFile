package cc.http.down;

import cc.http.util.MNRuunable;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-18
 * Time: 17:28
 * Version 1.0
 */

public class DownRuunable extends MNRuunable {

    private DownloadRequest request;

    private FileDownLoader fileDown;

    private int id;

    private String tag = DownRuunable.class.getSimpleName();

    public DownRuunable(DownloadRequest request){
        this.request = request;
        setId(request.getId());
    }

    @Override
    public void run() {
        fileDown = new FileDownLoader(request);
        fileDown.onStart();
    }

    public void onStop(){
        if(fileDown!=null){
            fileDown.onStop(true);
        }
    }

    public void onStart(){
       if(fileDown!=null){
           fileDown.onStart(false);
       }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
