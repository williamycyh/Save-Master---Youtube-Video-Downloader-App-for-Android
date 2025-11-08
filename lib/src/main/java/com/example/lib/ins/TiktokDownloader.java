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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Rumble   https://rumble.com/v20uy4o-stevewilldoits-top-10-moments-of-2022.html
 * Zing MP3  https://zingmp3.vn/bai-hat/Dung-Lo-Nhe-Co-Anh-Day-Thien-Tu/Z6UUABUW.html
 * Audio boom   https://audioboom.com/posts/7213197-simon-amstell
 * Audiomac   https://audiomack.com/atif-aslam/song/rafta-rafta
 *
 * Vlive  https://www.vlive.tv/video/295143
 * Flickr  https://flic.kr/p/2c8uyGC
 * Imgur  https://imgur.com/6oE77yi
 *
 * Tumblr  https://everythingfox.tumblr.com/post/187585497948/is-this-what-hell-looks-like
 * ESPN  https://www.espn.in/video/clip?id=28032716
 * Reddit  https://www.reddit.com/r/videos/comments/zmttas/how_not_to_sexually_harass_someone/
 * Kwai  https://s.kw.ai/p/yCNZzq8I
 * Trell   https://trell.co/watch/at-home-yoga-flow-22bac1148c5c
 *
 *
 *
 * https://www.facebook.com/al.bahsha.5/videos/943465283687801/?mibextid=NnVzG8
 *
 * https://www.facebook.com/Nataphilelyricspage/videos/2004460953278676/?sfnsn=mo&mibextid=LROouL
 *
 * https://fb.watch/hGmkpE228j/
 *
 * https://fb.watch/hGmmMMuXIP/?mibextid=NnVzG8
 *
 * https://vt.tiktok.com/ZS8jVaevr/
 *
 * https://vt.tiktok.com/ZS8jVAKKa/
 *
 * https://www.linkedin.com/posts/raynatours_raynatours-teamrayna-christmas2022-ugcPost-7012037302264729600-Etuh?utm_source=share&utm_medium=member_android
 *
 * https://twitter.com/usatodayvideo/status/1607043618052661249?s=19
 */
public class TiktokDownloader {
    public ProgressDialog myPd;
    public Activity context;
    static String newddurl = "";
    public void startTiktokDownloader(String url, Activity Mcontext, ProgressDialog pd){
        myPd = pd;
        context = Mcontext;

        if (!((Activity) Mcontext).isFinishing()) {

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
            methode2.setVisibility(View.GONE);
            methode3.setVisibility(View.GONE);
            methode4.setVisibility(View.GONE);
            methode5.setVisibility(View.GONE);
            methode6.setVisibility(View.GONE);

            Button dig_btn_cancel = dialog.findViewById(R.id.dig_btn_cancel);

            String finalUrl2 = url;

            methode0.setOnClickListener(v -> {
                dialog.dismiss();
                Executors.newSingleThreadExecutor().submit(() -> {

                    try {

                        HttpURLConnection con = (HttpURLConnection) (new URL(finalUrl2).openConnection());

                        con.setInstanceFollowRedirects(false);
                        con.connect();
                        int responseCode = con.getResponseCode();
                        System.out.println(responseCode);
                        String location = con.getHeaderField("Location");
                        if (location != null) {
                            System.out.println(location);
                            System.out.println(location.split("/")[5]);
                            System.out.println(location.split("/")[5].split("\\?")[0]);
                        } else {
                            location = finalUrl2;
                            System.out.println(location.split("/")[5]);
                            System.out.println(location.split("/")[5].split("\\?")[0]);
                        }
                        System.out.println("wojfdjhfdjh " + location);

                        String finalLocation = location;

                        try {
                            String username = location.split("/")[5].split("\\?")[0];
                            newddurl = "https://tikcdn.io/ssstik/" + username;

                            DownloadFileMain.startDownloading(context, newddurl, "Tiktok_" + username + "_" + System.currentTimeMillis(), ".mp4");

                            dismissMyDialog();
                        } catch (Exception e2) {
                            Executors.newSingleThreadExecutor().submit(() -> {
                                try {
                                    OkHttpClient client = new OkHttpClient().newBuilder()
                                            .build();
                                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                                    RequestBody body = RequestBody.create(mediaType, "");
                                    Request request = new Request.Builder()
                                            .url("https://youtube4kdownloader.com/ajax/getLinks.php?video=" + java.net.URLEncoder.encode(finalLocation, StandardCharsets.UTF_8) + "&rand=XaZX3gZYcZY8ZdM")
                                            .post(body)
                                            .addHeader("Content-type", "application/x-www-form-urlencoded")
                                            .addHeader("Origin", "https://youtube4kdownloader.com")
                                            .build();


                                    Response response1 = client.newCall(request).execute();
                                    System.out.println("youtube4kdownloader wojfdjhfdjhtik mmmmm " + response1);

                                    String myres = response1.body().string();
                                    System.out.println("youtube4kdownloader wojfdjhfdjhtik yyyy " + myres);

                                    if (response1.code() == 200) {

                                        String matag, myttt;

                                        dismissMyDialog();
                                        JSONObject jsonObject = new JSONObject(myres.toString());

                                        JSONArray jsonaaa = jsonObject.getJSONObject("data").getJSONArray("av");
                                        matag = jsonaaa.getJSONObject(jsonaaa.length() - 1).getString("url");                                                                            //                                                                            try {
                                        myttt = matag;

                                        DownloadFileMain.startDownloading(context, myttt, "Tiktok_" + System.currentTimeMillis(), ".mp4");


                                    } else {
                                        dismissMyDialog();
                                        DownloadFileMain.startDownloading(context, newddurl, "Tiktok_" + System.currentTimeMillis(), ".mp4");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("myresponseis111 exp0 " + e.getMessage());
                                    dismissMyDialog();
                                    DownloadFileMain.startDownloading(context, newddurl, "Tiktok_" + System.currentTimeMillis(), ".mp4");

                                }
                            });
                        }

                    } catch (Exception e1) {


                        System.out.println("myresponseis111 exp " + e1.getMessage());

                        dismissMyDialogErrortoast();
                    }


                });

            });

//            methode1.setVisibility(View.VISIBLE);
//            methode1.setOnClickListener(v -> {
//                dialog.dismiss();
//
//                System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);

//                dismissMyDialog();
//                Intent intent = new Intent(Mcontext, TikTokDownloadCloudBypassWebview_method_2.class);
//                intent.putExtra("myvidurl", finalUrl1);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                Mcontext.startActivity(intent);

//            });

            methode1.setOnClickListener(v -> {
                dialog.dismiss();

                System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);

                Executors.newSingleThreadExecutor().submit(() -> {
                    try {
                        ClearableCookieJar cookieJar =
                                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Mcontext));

                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                        OkHttpClient client = new OkHttpClient.Builder()
                                .cookieJar(cookieJar)
                                .addInterceptor(logging)
                                .build();

                        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("url", finalUrl1)
                                .build();
                        final String[] userAgent = {
                                "Mozilla/5.0 (iPad; CPU OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/87.0.4280.77 Mobile/15E148 Safari/604.1"
                        };
                        ((Activity) Mcontext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userAgent[0] = new WebView(Mcontext).getSettings().getUserAgentString();
                                System.out.println("myresponseis111 " + userAgent[0]);
                            }
                        });

                        Request request = new Request.Builder()
                                .url("https://tikcd.com/en/video/detail")
                                .method("POST", body)
                                .addHeader("origin", "https://tikcd.com")
                                .addHeader("referer", "https://tikcd.com/")
                                .addHeader("user-agent", "" + userAgent[0])
                                .addHeader("x-requested-with", "XMLHttpRequest")
                                .build();
                        Response response = client.newCall(request).execute();

                        if (response.code() == 200) {
                            String urlisddd;
                            String resss = Objects.requireNonNull(response.body()).string();

                            if (!resss.contains("video is currently not available")) {

                                Document document = Jsoup.parse(resss);

                                System.out.println("myresponseis111 exp 888 " + resss);

                                urlisddd = document.select("a").get(0).attr("href");

                                System.out.println("myresponseis111 exp 888 urlisddd " + urlisddd);

                                if (urlisddd != null && !urlisddd.equals("")) {

                                    dismissMyDialog();
                                    DownloadFileMain.startDownloading(context, urlisddd, "Tiktok_" + System.currentTimeMillis(), ".mp4");

                                } else {
                                    dismissMyDialogErrortoast();
                                }
                            } else {
                                dismissMyDialogErrortoast();

                            }


                        } else {
                            dismissMyDialogErrortoast();

                        }

                    } catch (Throwable e) {
                        e.printStackTrace();
                        System.out.println("myresponseis111 exp " + e.getLocalizedMessage());

                        dismissMyDialogErrortoast();
                    }
                });

            });

//            methode3.setOnClickListener(v -> {
//                dialog.dismiss();
//
//                System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);
//
//                dismissMyDialog();
//                Intent intent = new Intent(Mcontext, TikTokDownloadCloudBypassWebview_method_3.class);
//                intent.putExtra("myvidurl", finalUrl1);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                Mcontext.startActivity(intent);
//
//            });
//
//            methode4.setOnClickListener(v -> {
//                dialog.dismiss();
//
//                System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);
//
//                dismissMyDialog();
//                Intent intent = new Intent(Mcontext, TikTokDownloadCloudBypassWebview_method_4.class);
//                intent.putExtra("myvidurl", finalUrl1);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                Mcontext.startActivity(intent);
//
//            });
//
//            methode5.setVisibility(View.VISIBLE);
//            methode5.setOnClickListener(v -> {
//                dialog.dismiss();
//
//                System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);
//                callDlApiNew(finalUrl1);
//
//            });
//
//            methode6.setVisibility(View.VISIBLE);
//            methode6.setOnClickListener(v -> {
//                dialog.dismiss();
//
//                System.out.println("wojfdjhfdjh myfgsdyfsfus=" + finalUrl1);
//
//                dismissMyDialog();
//                Intent intent = new Intent(Mcontext, TikTokDownloadCloudBypassWebview_method_6.class);
//                intent.putExtra("myvidurl", finalUrl1);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                Mcontext.startActivity(intent);
//
//            });

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
        if(context != null && context.isFinishing()){
            Toast.makeText(context, "Error, try another method", Toast.LENGTH_LONG).show();
        }
        if(myPd != null){
            myPd.dismiss();
        }
    }

}
