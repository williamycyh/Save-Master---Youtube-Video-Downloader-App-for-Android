package com.example.lib.ins;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.lib.DownloadFileMain;
import com.example.lib.R;
import com.example.lib.androidnetworking.AndroidNetworking;
import com.example.lib.androidnetworking.common.Priority;
import com.example.lib.androidnetworking.error.ANError;
import com.example.lib.androidnetworking.interfaces.JSONObjectRequestListener;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class FbDownloader {
    public ProgressDialog myPd;
    public Activity myActivity;

    public static interface FBDownInterface{
        public void download(String url);
    }
    public void startFbDownloader(String url, Activity Mcontext, ProgressDialog pd, FBDownInterface function){
        myPd = pd;
        myActivity = Mcontext;
        if (!((Activity) Mcontext).isFinishing()) {

                   /*
        TODO not working
        https://m.facebook.com/story.php?story_fbid=10160134899489468&id=506699467
        https://www.facebook.com/100004478714645/posts/2230800797079189/
        https://www.facebook.com/100002655463277/posts/4874514195980381/
        https://m.facebook.com/story.php?story_fbid=1881132202078276&id=104400186269172
        https://www.facebook.com/watch/?v=1403988643425724
        https://www.facebook.com/221895084488402/videos/1403988643425724/
        * */

            Dialog dialog = new Dialog(Mcontext);

            dialog.setContentView(R.layout.tiktok_optionselect_dialog);

            String finalUrl1 = url;

            Button methode0 = dialog.findViewById(R.id.dig_btn_met0);
            Button methode1 = dialog.findViewById(R.id.dig_btn_met1);
            Button methode2 = dialog.findViewById(R.id.dig_btn_met2);
            Button methode3 = dialog.findViewById(R.id.dig_btn_met3);
            Button methode4 = dialog.findViewById(R.id.dig_btn_met4);
            Button methode5 = dialog.findViewById(R.id.dig_btn_met5);
            Button methode6 = dialog.findViewById(R.id.dig_btn_met6);

            methode3.setVisibility(View.GONE);
            methode4.setVisibility(View.GONE);
            methode5.setVisibility(View.GONE);
            methode6.setVisibility(View.GONE);

            Button dig_btn_cancel = dialog.findViewById(R.id.dig_btn_cancel);

//            methode0.setOnClickListener(v -> {
//                dialog.dismiss();
//
//                System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);
//
//
//            });
//
//            methode1.setOnClickListener(v -> {
//                dialog.dismiss();
//
//                System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);
//
//            });

            //todo fix
            methode0.setOnClickListener(v -> {
                dialog.dismiss();


                System.out.println("myurliss = " + finalUrl1);


                AndroidNetworking.get("https://s4.downloadfacebook.net/ajax/getLinks.php?video=" + finalUrl1 + "&rand=PNSQNPOPRTTUOLP")
                        .setPriority(Priority.MEDIUM)
                        .addHeaders("Content-type", "application/x-www-form-urlencoded")
                        .addHeaders("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36")
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                System.out.println("ytff wojfdjhfdjhtik yyyy " + response);

                                String matag;
                                try {
                                    dismissMyDialog();
                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    JSONArray jsonaaa = jsonObject.getJSONObject("data").getJSONArray("av");
                                    matag = jsonaaa.getJSONObject(0).getString("url");

                                    DownloadFileMain.startDownloading(Mcontext, matag, "Facebook_" + System.currentTimeMillis(), ".mp4");

                                } catch (Exception e) {

                                    System.out.println("myresponseis111 exp2 " + e.getMessage());
                                    dismissMyDialogErrortoast();

                                }

                            }

                            @Override
                            public void onError(ANError error) {
                                System.out.println("myresponseis111 exp2 " + error.getMessage());
                                dismissMyDialogErrortoast();

                            }
                        });


            });

//            methode4.setOnClickListener(v -> {
////                        dialog.dismiss();
////
////                        FbVideoDownloader fbVideoDownloader = new FbVideoDownloader(Mcontext, finalUrl1, 2);
////                        fbVideoDownloader.DownloadVideo();
//
//
//                dialog.dismiss();
//
//            });
//
//            methode5.setOnClickListener(v -> {
//                dialog.dismiss();
//            });


            methode1.setOnClickListener(v -> {
                dialog.dismiss();

                try {

                    System.out.println("myurliss = " + finalUrl1);

                    Executors.newSingleThreadExecutor().submit(() -> {
                        try {
                            Looper.prepare();

                            ClearableCookieJar cookieJar =
                                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));

                            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                            OkHttpClient client = new OkHttpClient.Builder()
                                    .cookieJar(cookieJar)
                                    .addInterceptor(logging)
                                    .connectTimeout(10, TimeUnit.SECONDS)
                                    .writeTimeout(10, TimeUnit.SECONDS)
                                    .readTimeout(30, TimeUnit.SECONDS)
                                    .build();

                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("q", finalUrl1)
                                    .addFormDataPart("vt", "home")
                                    .addFormDataPart("v", "v2")
                                    .build();
                            Request request = new Request.Builder()
                                    .url("https://x2download.com/api/ajaxSearch")
                                    .header("Cookie", "")
                                    .method("POST", body)
                                    .build();
                            Response response = client.newCall(request).execute();

                            String ressff = Objects.requireNonNull(response.body()).string();
                            System.out.println("myurliss resss = " + ressff);

                            JSONObject document = new JSONObject(ressff);
                            dismissMyDialog();

                            if (document.has("data") && !document.getString("data").equals("")) {
                                String nametitle = "Facebook_" +
                                        System.currentTimeMillis();

                                String inputString = document.getString("data");
//                                                DownloadFileMain.startDownloading(Mcontext, document.getJSONObject("links").getString("hd"), nametitle, ".mp4");

                                String targetString = "decodeURIComponent(escape(r))";
                                String prefix = "console.log('Hello'+";
                                String suffix = ")";

                                String outputString = inputString.replace(targetString, prefix + targetString + suffix);
                                System.out.println(outputString);

                                Mcontext.runOnUiThread(() -> {
                                    WebView web = new WebView(Mcontext);
                                    web.getSettings().setJavaScriptEnabled(true);
                                    web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                                    web.getSettings().getAllowFileAccess();
                                    web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                                    web.getSettings().setDomStorageEnabled(true);
                                    web.getSettings().setAllowUniversalAccessFromFileURLs(true);
                                    web.getSettings().setMediaPlaybackRequiresUserGesture(false);
                                    CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
                                    int randomNumber = iUtils.getRandomNumber(iUtils.UserAgentsList0.length);

                                    try {
                                        web.getSettings().setUserAgentString(iUtils.UserAgentsList0[randomNumber]);
                                    } catch (Exception e) {
                                        web.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36");
                                    }
                                    web.setWebChromeClient(new WebChromeClient() {
                                        @Override
                                        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                                            try {
                                                Log.d("chromium-A-WebView", consoleMessage.message());
                                                String decodedHtml = StringEscapeUtils.unescapeHtml4(consoleMessage.message());

                                                List<String> allurls = iUtils.extractUrlsWithVideo(decodedHtml);


                                                DownloadFileMain.startDownloading(Mcontext, allurls.get(0), nametitle, ".mp4");

                                            } catch (Exception e) {
                                                dismissMyDialogErrortoast();
                                                e.printStackTrace();
                                            }

                                            return true;

                                        }
                                    });
                                    web.evaluateJavascript("javascript:" + outputString, value -> {
                                        System.out.println("myvall=" + value);
                                    });
                                });


                            } else {

                                dismissMyDialogErrortoast();
                            }

                        } catch (Exception e) {
                            System.out.println("myurliss err dialog = " + e.getLocalizedMessage());

                            e.printStackTrace();
                            dismissMyDialogErrortoast();
                        }


                    });

                } catch (Exception e) {
                    System.out.println("myurliss err dialog 4= " + e.getLocalizedMessage());

                    e.printStackTrace();
                    dismissMyDialogErrortoast();
                }

            });

            methode2.setOnClickListener(v -> {
                dialog.dismiss();
                if(function != null){
                    function.download(url);
                }

                System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);

//                        FbVideoDownloader fbVideoDownloader = new FbVideoDownloader(Mcontext, finalUrl1, 3);
//                        fbVideoDownloader.DownloadVideo();
            });

            dig_btn_cancel.setOnClickListener(v -> {
                dialog.dismiss();
                dismissMyDialog();
            });

            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private void dismissMyDialog(){
        if(myPd != null){
            myPd.dismiss();
        }
    }

    private void dismissMyDialogErrortoast(){
        if(myActivity != null && myActivity.isFinishing()){
            Toast.makeText(myActivity, "Error, try another method", Toast.LENGTH_LONG).show();
        }
        if(myPd != null){
            myPd.dismiss();
        }
    }

}
