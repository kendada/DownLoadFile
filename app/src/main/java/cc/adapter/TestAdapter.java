package cc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import cc.cc.down.util.DownloadListener;
import cc.cc.down.util.DownloadManager;
import cc.cc.down.util.DownloadRequest;
import cc.cc.down.util.MNDownloadManager;
import cc.downloadfile.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-19
 * Time: 15:20
 * Version 1.0
 */

public class TestAdapter extends BaseAdapter {

    private List<DownloadRequest> mList;
    private Context mContext;

    private DownloadManager manager;

    public TestAdapter(Context context, List<DownloadRequest> list){
        mContext = context;
        mList = list;
        manager = new MNDownloadManager();
    }

    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        MyView mv = null;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_test_layout, null);
            mv = new MyView();
            view.setTag(mv);
        } else {
            mv = (MyView)view.getTag();
        }

        mv.down_btn = (Button)view.findViewById(R.id.down_btn);
        mv.pause_btn = (Button)view.findViewById(R.id.pause_btn);
        mv.cancel_btn = (Button)view.findViewById(R.id.cancel_btn);
        mv.bar = (ProgressBar)view.findViewById(R.id.bar);


        final DownloadRequest request = mList.get(i);
        final MyView finalMv = mv;
        request.setDownloadListener(new DownloadListener() {
            @Override
            public void onSuccess(int id) {
                Toast.makeText(mContext, id + "下载完成。。。", Toast.LENGTH_SHORT).show();
                finalMv.down_btn.setEnabled(false);
                finalMv.pause_btn.setEnabled(false);
            }

            @Override
            public void onFailure(int id, int errorCode, String errorMessage) {

            }

            @Override
            public void onProgress(int id, int downLoadCount, int length) {
                finalMv.bar.setProgress(downLoadCount * 100 / length);
            }

            @Override
            public void onPause(int id) {

            }
        });
        manager.addDownloadRequest(request);
        mv.down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalMv.down_btn.setEnabled(false);
                finalMv.pause_btn.setEnabled(true);
                manager.addDownloadRequest(request);
            }
        });
        mv.pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.onPause(request.getId());
                finalMv.down_btn.setEnabled(true);
                finalMv.pause_btn.setEnabled(false);
            }
        });
        mv.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.onCancel(request.getId());
                mList.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    class MyView{
        Button down_btn, pause_btn, cancel_btn;
        ProgressBar bar;
    }
}
