package cc.downloadfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import cc.http.http.CookieParmas;
import cc.http.http.HttpUtil;
import cc.http.http.MNHttpListener;
import cc.http.http.RequestParams;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-20
 * Time: 21:05
 * Version 1.0
 */

public class HttpTestActivity extends AppCompatActivity {

    private Button btn, btn_userinfo, btn_upload, btn_calcel;
    private TextView text;

    private HttpUtil httpUtil;

    String url = "http://snsapp.xzw.com/index.php?app=public&mod=Xzwpassport&act=doLogin";
    String url1 = "http://snsapp.xzw.com/index.php?app=public&mod=xzwindex&act=UserInformation";
    String url2 = "http://snsapp.xzw.com/index.php?app=photo&mod=Xzwindex&act=upload";

    private String tag = HttpTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_layout);

        httpUtil = new HttpUtil();
        CookieParmas cookieParmas = new CookieParmas(this);
        httpUtil.setCookieParmas(cookieParmas);

        btn = (Button)findViewById(R.id.btn);
        btn_userinfo = (Button)findViewById(R.id.btn_userinfo);
        btn_upload = (Button)findViewById(R.id.btn_upload);
        btn_calcel = (Button)findViewById(R.id.btn_calcel);
        text = (TextView)findViewById(R.id.text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData();
            }
        });

        btn_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
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
        httpUtil.get(url1, new MNHttpListener() {
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
                text.setText("-------" + result);
            }

            @Override
            public void onFilure(int errorCode, String errorMessage) {
                text.setText("-------" + errorMessage);
            }

            @Override
            public void onFinish(int code) {
                Log.i(tag, "----onFinish()----=" + code);
            }
        });

    }

    private void postData(){
        RequestParams parms = new RequestParams();
        parms.put("login_email", "nimabi@qq.com");
        parms.put("login_password", "jin123");
        parms.put("login_remember", "1");

        httpUtil.post(url, parms, new MNHttpListener() {
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
                text.setText("-------" + result);
            }

            @Override
            public void onFilure(int errorCode, String errorMessage) {
                text.setText("-------" + errorMessage);
            }

            @Override
            public void onFinish(int code) {
                Log.i(tag, "----onFinish()----=" + code);
            }
        });
    }

    private void upload(){
        RequestParams params = new RequestParams();
        File file = new File("/mnt/sdcard/img500.jpg");
        Log.i(tag, "----148----"+file.exists());
        params.put("Filedata", file);
        httpUtil.post(url2, params, new MNHttpListener() {
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
                text.setText("-------" + result);
                Log.i(tag, "----" + result);
            }

            @Override
            public void onFilure(int errorCode, String errorMessage) {
                text.setText("-------" + errorMessage);
            }

            @Override
            public void onFinish(int code) {
                Log.i(tag, "----onFinish()----=" + code);
            }
        });
    }


}
