package com.example.lib;

import android.app.Activity;

public class DownloadFileMain {
    public static void startDownloading(Activity activity, String url, String filename, String extend){
        Downloader.getInstance(activity).downloadByUrl(
                url,
                filename,
                extend
        );
    }

}
