package com.example.tubedown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lib.CommonVideoDownloader;
import com.example.lib.Downloader;
import com.example.lib.FileItem;
import com.example.lib.MainLib;
import com.example.lib.uipage.ASharePreferenceUtils;
import com.example.lib.uipage.MyTFloatTActivity;


import java.util.List;

public class TubeMMActivity extends AppCompatActivity {
    View tubelayout;
//    View support2;

    public static void startMe(Activity activity){
        Intent intent = new Intent();
        intent.setClass(activity, TubeMMActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setTheme(ThemeHelper.getSettingsThemeStyle(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tube_d_main_activity);

        initView();
        tubelayout.post(new Runnable() {
            @Override
            public void run() {
                loadAd();
            }
        });

        setDownloadListener();
        initLib();
    }

    void setDownloadListener(){
        Downloader.getInstance(this).setOnStartDownloadListener(new Downloader.OnStartDownloadListener() {
            @Override
            public void onStart() {
                if(isFinishing()){
                    return;
                }
                boolean rated = ASharePreferenceUtils.getBoolean(TubeMMActivity.this, "rashowed", false);
                if(rated){
                } else {
                    googleRate();
                }
            }
        });
    }

    public String getClipboard(){
        try{
            ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData data = cm.getPrimaryClip();
            ClipData.Item item = data.getItemAt(0);
            return item.getText().toString();
        }catch (Exception e){

        }
        return "";
    }

    private void initLib(){
        MainLib.setOnBaseDownloadCall(new MainLib.OnBaseDownloadCall() {
            @Override
            public void showFullAd() {
                boolean rated = ASharePreferenceUtils.getBoolean(TubeMMActivity.this, "rashowed", false);
                if(rated){
                } else {
                    googleRate();
                }
            }

            @Override
            public void showVideoList(List<FileItem> files, Activity activity) {
                Utils.showListDialog(files, activity);
            }
        });
    }

    private void googleRate(){
        boolean rated = ASharePreferenceUtils.getBoolean(TubeMMActivity.this, "rashowed", false);
        if(rated){
            return;
        }

        ASharePreferenceUtils.putBoolean(TubeMMActivity.this, "rashowed", true);
    }


//    MyCommon myCommon = new MyCommon();
    private void loadAd(){

    }

    EditText eText;
    View galleryLayout;
    private void initView(){

        tubelayout = findViewById(R.id.tube_layout);
//        support2 = findViewById(R.id.vimo_layout);
//        galleryLayout = findViewById(R.id.gallery_layout);
//        View feedbackAction = findViewById(R.id.feedback_action);

        eText = findViewById(R.id.edittext_url);


        tubelayout.setVisibility(View.GONE);
        eText.setHint("Paste Social Video Url or Web Video Url");


//        findViewById(R.id.rate_me_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                googleRate();
//            }
//        });
//        findViewById(R.id.feedback_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String currentUrl = eText != null ? eText.getText().toString() : "";
////                sendFeedbackEmail(currentUrl);
//            }
//        });
        tubelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            MyTFloatTActivity.startMeUrl(TubeMMActivity.this, "https://m.youtube.com/");//


            }
        });

        galleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FileActivity.startMe(TubeMMActivity.this);
            }
        });
//
//        support2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MyTFloatTActivity.startMeUrl(MyWebFragmentActivity.this, "https://vimeo.com/watch");
//            }
//        });

        findViewById(R.id.paste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tt = getClipboard();
                if(!TextUtils.isEmpty(tt)){
                    eText.setText(tt);
                }
            }
        });


        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(eText.getText())){
                    Toast.makeText(TubeMMActivity.this, "Input Social Video Url pls", Toast.LENGTH_LONG).show();
                    return;
                }
                String urlorkey = eText.getText().toString();

                download(urlorkey);
            }
        });
    }

    ProgressDialog progressDialog;
    private void showProgress(){
        if(isFinishing()){
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null && progressDialog.isShowing()){
                    return;
                }
                progressDialog = new ProgressDialog(TubeMMActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });

    }

    private void hideProgress(){
        if(isFinishing() || progressDialog == null){
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    CommonVideoDownloader commonVideoDownloader = new CommonVideoDownloader();
    private void download(String url){
//        if(!DownloadFragment.hasPermissions(this)){
//            ActivityCompat.requestPermissions(this, permissionArray, PERMISSIONS_REQUEST_CODE);
//            return;
//        }

        commonVideoDownloader.setOnDownloadListener(new CommonVideoDownloader.OnDownloadListener() {
            @Override
            public void onSuccess(List<FileItem> files, int videoType) {
                if(files == null || files.isEmpty() || isFinishing()){
                    return;
                }
                if(videoType == Downloader.TU_TYPE){
                    MainLib.showVideoList(files, TubeMMActivity.this);
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
                        final String currentUrl = eText != null ? eText.getText().toString() : "";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                }
            }
        });
        commonVideoDownloader.startDownload(this, url);
    }

    public static boolean isYoutubeUrl(String url){
        url = url.toLowerCase();
        return url.contains("youtube") || url.contains("googlevideo.com") || url.contains("youtu.be");
    }

}
