package com.example.lib.uipage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.lib.CommonVideoDownloader;
import com.example.lib.Downloader;
import com.example.lib.FileItem;
import com.example.lib.MainLib;
import com.example.lib.R;

import java.lang.reflect.Method;
import java.util.List;

public class MyTFloatTActivity extends BaseWebActivity {

    public String target_url = "";
    ProgressBar progress_bar;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.d_webview);

        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);
        webView = (WebView) findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true); // 支持HTML5中的一些控件标签
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.addJavascriptInterface(this, "FBDownloader");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        } else {
            webView.getSettings().setMixedContentMode(WebSettings.LOAD_NORMAL);
        }
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progress_bar.setProgress(newProgress);
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress_bar.setVisibility(View.VISIBLE);
                Log.d("tag","onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                Log.d("tag","onPageFinished");
                progress_bar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) { // 4.0以上必须要加
                    view.loadUrl(url);
                    return true;
                } else if (url.startsWith("fb://")) {
                    String fallbackUrl = url.replace("fb://", "https://www.facebook.com/");
                    view.loadUrl(fallbackUrl);
                    return true; // 拦截 WebView 加载
                }
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("http://") || url.startsWith("https://")) { // 4.0以上必须要加
                    view.loadUrl(url);
                    return true;
                } else if (url.startsWith("fb://")) {
                    String fallbackUrl = url.replace("fb://", "https://www.facebook.com/");
                    view.loadUrl(fallbackUrl);
                    return true; // 拦截 WebView 加载
                }
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 允许javascript出错
            try {
                Method method = Class.forName("android.webkit.WebView").
                        getMethod("setWebContentsDebuggingEnabled", Boolean.TYPE);
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(null, true);
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        CookieSyncManager.getInstance().startSync();

        String url = getIntent().getExtras().getString("url");
        int showBtn = getIntent().getExtras().getInt("no");
        if(TextUtils.isEmpty(url)){
            finish();
            return;
        }
        target_url = url;
        webView.loadUrl(url);

        findViewById(R.id.download_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = webView.getUrl();
                download(url);
            }
        });
        if(showBtn == 1){
            findViewById(R.id.download_file).setVisibility(View.GONE);
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Downloader.getInstance(this).setOnStartDownloadListener(new Downloader.OnStartDownloadListener() {
            @Override
            public void onStart() {
                MainLib.showFullScreen();
            }
        });

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q){
            permissionArray = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        } else {
            permissionArray =  new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        ActivityCompat.requestPermissions(this, permissionArray, 992);
    }
    public static String[] permissionArray = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private void download(String url){
//        if(!DownloadFragment.hasPermissions(this)){
//            ActivityCompat.requestPermissions(this, permissionArray, PERMISSIONS_REQUEST_CODE);
//            return;
//        }

        CommonVideoDownloader commonVideoDownloader = new CommonVideoDownloader();
        commonVideoDownloader.setOnDownloadListener(new CommonVideoDownloader.OnDownloadListener() {
            @Override
            public void onSuccess(List<FileItem> files, int videoType) {
                if(files == null || files.isEmpty() || isFinishing()){
                    return;
                }

                if(videoType == Downloader.TU_TYPE){
                    MainLib.showVideoList(files, MyTFloatTActivity.this);
                }
//                else if(videoType == Downloader.F_TYPE){
//                    showfDialog(files);
//                } else if(videoType == CommonVideoDownloader.TYPE_INS){
//                    for(FileItem item: files){
//                        Downloader.getInstance(getActivity()).download(item, true, false, System.currentTimeMillis());
//                    }
//                }
            }

            @Override
            public void onFail(int videoType, int failType, String failDetail) {
//                UIConfigManager.setLastException(failDetail);
                if(isFinishing()){
                    return;
                }
                if(videoType == Downloader.INS_TYPE){
//                    if(!Downloader.cookieOK()) {
//                        showLoginDialog();
//                    }
                } else {
                    if(!isFinishing()){
                        Toast.makeText(MyTFloatTActivity.this, "Download error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        commonVideoDownloader.startDownload(this, url);
    }

    public static void startMe(Activity activity){
        Intent intent = new Intent();
        intent.putExtra("url", BaseCommon.decodeToString("aHR0cHM6Ly9tLnlvdXR1YmUuY29tLw=="));
        intent.setClass(activity, MyTFloatTActivity.class);
        activity.startActivity(intent);
    }

    public static void startMeUrl(Activity activity, String url){
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.setClass(activity, MyTFloatTActivity.class);
        activity.startActivity(intent);
    }

    public static void startMeUrlNoDown(Activity activity, String url, int notshow){ //1: not show download button
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.putExtra("no",notshow);
        intent.setClass(activity, MyTFloatTActivity.class);
        activity.startActivity(intent);
    }
}
