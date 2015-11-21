package cc.downloadfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cc.http.http.HttpUtil;
import cc.http.http.MNHttpListener;
import cc.http.http.RequestParms;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 21:05
 * Version 1.0
 */

public class HttpTestActivity extends AppCompatActivity {

    private Button btn, btn_calcel;
    private TextView text;

    private HttpUtil httpUtil;

    String url = "http://zhiyue.cutt.com/api/clip/images?clipId=103140789";

    private String tag = HttpTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_layout);

        httpUtil = new HttpUtil();

        btn = (Button)findViewById(R.id.btn);
        btn_calcel = (Button)findViewById(R.id.btn_calcel);
        text = (TextView)findViewById(R.id.text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        btn_calcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpUtil.onCancel(url);
            }
        });
    }

    private void getData(){
        RequestParms parms = new RequestParms();

        httpUtil.get(url, new MNHttpListener() {
            @Override
            public void onStart() {
                Log.i(tag, "----onStart()----");
            }

            @Override
            public void onCancel() {
                Log.i(tag, "----onCancel()----");
            }

            @Override
            public void onSuccess(int code, String result) {
                text.setText("-------"+result);
            }

            @Override
            public void onFilure(int errorCode, String errorMessage) {
                text.setText("-------"+errorMessage);
            }

            @Override
            public void onFinish(int code) {
                Log.i(tag, "----onFinish()----="+code);
            }
        });

    }



}
