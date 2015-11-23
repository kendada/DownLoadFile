package cc.downloadfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cc.http.http.CookieParmas;
import cc.http.http.HttpUtil;
import cc.http.http.MNHttpListener;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-23
 * Time: 16:22
 * Version 1.0
 */

public class WeiBoActivity extends AppCompatActivity implements View.OnClickListener{


    private Button btn, btn_userinfo, btn_upload, btn_calcel;
    private TextView text;

    private HttpUtil httpUtil;

    private String tag = WeiBoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_layout);

        httpUtil = new HttpUtil();

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);

        btn_userinfo = (Button)findViewById(R.id.btn_userinfo);
        btn_userinfo.setOnClickListener(this);

        btn_upload = (Button)findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(this);

        btn_calcel = (Button)findViewById(R.id.btn_calcel);
        btn_calcel.setOnClickListener(this);

        text = (TextView)findViewById(R.id.text);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                login(); //登录
                break;
            case R.id.btn_userinfo:
                userInfo();
                break;
            case R.id.btn_upload:

                break;
            case R.id.btn_calcel:

                break;
        }
    }

    private void login(){
        String url = "https://api.weibo.com/oauth2/authorize" +
                "?client_id=284902573" +
                "&response_type=code" +
                "&redirect_uri=http://www.koudairiji.com";
        Log.i(tag, "---78---"+url);
    }

    private void userInfo(){
        String code = "a4188a109e24ea889ac00a489390ba98";
        String url = "https://api.weibo.com/oauth2/access_token" +
                "?client_id=284902573" +
                "&client_secret=125352d859d7c7c83184d2957fe2a34f" +
                "&grant_type=authorization_code" +
                "&redirect_uri=http://www.koudairiji.com" +
                "&code="+code;

        httpUtil.post(url, new MNHttpListener() {
            @Override
            public void onSuccess(int code, String result) {
                Log.i(tag, "----94----"+result);
            }

            @Override
            public void onFilure(int errorCode, String errorMessage) {
                Log.i(tag, "--99---errorCode="+errorCode);
            }

            @Override
            public void onFinish(int code) {
                Log.i(tag, "----104---code="+code);
            }
        });

    }

}
