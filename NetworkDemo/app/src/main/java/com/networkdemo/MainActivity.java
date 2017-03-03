package com.networkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String BaseUrl = "http://www.baidu.com/";
    private static final String picUrl = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1696792241,2431445178&fm=23&gp=0.jpg";
    private static final MediaType mediaType_markdown = MediaType.parse("text/x-markdown;charset=utf-8");
    private OkHttpClient mOkhttpClient;
    private String str = "";


    @BindView(R.id.tv_info)
    TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_get, R.id.btn_post, R.id.btn_download, R.id.btn_send})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Get请求
            case R.id.btn_get:
                getAsyHttp();
                break;
            //Post请求
            case R.id.btn_post:
                postAsyHttp();
                break;
            //Download File
            case R.id.btn_download:
                downloadAsyFile();
                break;
            //Send File
            case R.id.btn_send:
                postAsyFile();
                break;
        }
    }

    /**
     * 下载文件
     */
    private void downloadAsyFile() {
        mOkhttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(picUrl)
                .build();
        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream("/sdcard/zbc.jpg");
                    byte[] buffer = new byte[2048];
                    int len;
                    len = inputStream.read(buffer);
                    while (len != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    Logger.i("IOException", "MainActivity");
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvInfo.setText(picUrl);
                        Toast.makeText(MainActivity.this, "文件下载成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * post方式上传文件
     */
    private void postAsyFile() {
        mOkhttpClient = new OkHttpClient();
        File file = new File("/Vlog.xml");
        Request request = new Request.Builder().url("https://github.com/Travis1022/Learn_Android_Module/upload/master")
                .post(RequestBody.create(mediaType_markdown, file))
                .build();
        Call call = mOkhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                str = response.body().string();
                Logger.i(str, "MainActivity");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvInfo.setText(str);
                        Toast.makeText(MainActivity.this, "Post传文件成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * POST请求
     */
    private void postAsyHttp() {
        mOkhttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("a", "苏州市")
                .build();
        Request request = new Request.Builder()
                .url("http://gc.ditu.aliyun.com/geocoding?")
                .post(formBody)
                .build();
        Call call = mOkhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                str = response.body().string();
                Logger.i(str, "MainActivity");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvInfo.setText(str);
                        Toast.makeText(MainActivity.this, "Post请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    /**
     * GET请求
     */
    public void getAsyHttp() {
        mOkhttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(BaseUrl);
        requestBuilder.method("GET", null);
        final Request request = requestBuilder.build();
        Call mCall = mOkhttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.cacheResponse() != null) {
                    str = response.cacheResponse().toString();
                    Logger.i("cache---" + str, "MainActivity");
                } else {
                    response.body().string();
                    str = response.networkResponse().toString();
                    Logger.i("network---" + str, "MainActivity");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvInfo.setText(str);
                        Toast.makeText(MainActivity.this, "Get请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
