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
        download("https://www.youtube.com/watch?v=Xg8aWV14vSs");//fill in url
    }


    CommonVideoDownloader commonVideoDownloader = new CommonVideoDownloader();
    private void download(String url){

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
