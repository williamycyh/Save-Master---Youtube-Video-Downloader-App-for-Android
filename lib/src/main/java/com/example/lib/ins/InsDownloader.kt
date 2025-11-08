package com.example.lib.ins

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import com.example.lib.DownloadFileMain
import com.example.lib.Downloader
import com.example.lib.R
import com.example.lib.api.RetrofitApiInterface
import com.example.lib.api.RetrofitClient
import com.example.lib.ins.iUtils.ShowToast
import com.example.lib.ins.models.instawithlogin.CarouselMedia
import com.example.lib.ins.models.instawithlogin.ModelInstaWithLogin
import com.example.lib.ins.models.storymodels.ModelEdNode
import com.example.lib.ins.models.storymodels.ModelGetEdgetoNode
import com.example.lib.ins.models.storymodels.ModelInstagramResponse
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.text.StringEscapeUtils
import retrofit2.Call
import retrofit2.Callback
import java.lang.reflect.Type
import java.net.URI
import java.util.Random

class InsDownloader {
    var myVideoUrlIs: String? = null
    var myInstaUsername: String? = ""
    var myPhotoUrlIs: String? = null

    private var myselectedActivity: Activity? = null
    private var myPd: ProgressDialog? = null

    public interface InsDownloadInterface{
        public fun download(url:String);
    }
    @Keep
    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    fun startInstaDownload(Url: String, myselectedActivity: Activity, pd: ProgressDialog, insDownMethod: InsDownloadInterface) {


//         https://www.instagram.com/p/CLBM34Rhxek/?igshid=41v6d50y6u4w
//          https://www.instagram.com/p/CLBM34Rhxek/
//           https://www.instagram.com/p/CLBM34Rhxek/?__a=1&__d=dis
//           https://www.instagram.com/tv/CRyVpDSAE59/

        /*
        * https://www.instagram.com/p/CUs4eKIBscn/?__a=1&__d=dis
        * https://www.instagram.com/p/CUktqS7pieg/?__a=1&__d=dis
        * https://www.instagram.com/p/CSMYRwGna3S/?__a=1&__d=dis
        * https://www.instagram.com/p/CR6AbwDB12R/?__a=1&__d=dis
        * https://www.instagram.com/p/CR6AbwDB12R/?__a=1&__d=dis
        * */

        this.myselectedActivity = myselectedActivity
        this.myPd = pd

        val Urlwi: String?
        try {

            val uri = URI(Url)
            Urlwi = URI(
                uri.scheme,
                uri.authority,
                uri.path,
                null,  // Ignore the query part of the input url
                uri.fragment
            ).toString()


        } catch (ex: java.lang.Exception) {
            dismissMyDialogFrag()
//            ShowToast(myselectedActivity!!, getString(R.string.invalid_url))
            return
        }

        System.err.println("workkkkkkkkk 1122112 $Url")

        var urlwithoutlettersqp: String? = Urlwi
        System.err.println("workkkkkkkkk 1122112 $urlwithoutlettersqp")


        if (urlwithoutlettersqp!!.contains("/reel/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/reel/", "/p/")
        }

        if (urlwithoutlettersqp.contains("/tv/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/tv/", "/p/")
        }

        val urlwithoutlettersqp_noa: String = urlwithoutlettersqp

        urlwithoutlettersqp = "$urlwithoutlettersqp?__a=1&__d=dis"
        System.err.println("workkkkkkkkk 87878788 $urlwithoutlettersqp")


        System.err.println("workkkkkkkkk 777777 $urlwithoutlettersqp")

        try {
            if (urlwithoutlettersqp.split("/")[4].length > 15) {

                val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity)
                if (sharedPrefsFor.preference.preferencE_SESSIONID == "") {
                    sharedPrefsFor.clearSharePrefs()
                }
                val map = sharedPrefsFor.preference
                if (map != null) {
                    if (map.preferencE_ISINSTAGRAMLOGEDIN == "false") {

                        dismissMyDialogFrag()

                        if (!myselectedActivity!!.isFinishing) {
                            val alertDialog =
                                android.app.AlertDialog.Builder(myselectedActivity).create()
                            alertDialog.setTitle("Login")
                            alertDialog.setMessage(myselectedActivity.getString(R.string.urlisprivate))
                            alertDialog.setButton(
                                android.app.AlertDialog.BUTTON_POSITIVE,
                                "Login"
                            ) { dialog, _ ->
                                dialog.dismiss()


                                val intent = Intent(
                                    myselectedActivity,
                                    InstagramLoginActivity::class.java
                                )
//                                startActivityForResult(intent, 200)
                                myselectedActivity.startActivity(intent)
                            }

                            alertDialog.setButton(
                                android.app.AlertDialog.BUTTON_NEGATIVE, "Cancel"
                            ) { dialog, _ ->
                                dialog.dismiss()


                            }
                            alertDialog.show()
                        }
                        return
                    }
                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (!(myselectedActivity)!!.isFinishing) {
            val dialog = Dialog(myselectedActivity!!)
            dialog.setContentView(R.layout.tiktok_optionselect_dialog)

            val methode0 = dialog.findViewById<Button>(R.id.dig_btn_met0)
            val methode1 = dialog.findViewById<Button>(R.id.dig_btn_met1)
            val methode2 = dialog.findViewById<Button>(R.id.dig_btn_met2)
            val methode3 = dialog.findViewById<Button>(R.id.dig_btn_met3)
            val methode4 = dialog.findViewById<Button>(R.id.dig_btn_met4)
            val methode5 = dialog.findViewById<Button>(R.id.dig_btn_met5)
            val methode6 = dialog.findViewById<Button>(R.id.dig_btn_met6)
            val dig_txt_heading = dialog.findViewById<TextView>(R.id.dig_txt_heading)
            methode4.visibility = View.VISIBLE
            methode5.visibility = View.GONE
            methode6.visibility = View.GONE
            dig_txt_heading.text = myselectedActivity!!.getString(R.string.Selectdesiredinsta)

            val dig_btn_cancel = dialog.findViewById<Button>(R.id.dig_btn_cancel)


            methode0.setOnClickListener {
                dialog.dismiss()
                try {
                    System.err.println("workkkkkkkkk 4")
                    val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity!!)
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                    ) {
                        System.err.println("workkkkkkkkk 4.7")
                        downloadInstagramImageOrVideodata_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        login()
//                        System.err.println("workkkkkkkkk 4.8 " + iUtils.myinstawebTempCookies)
//
//
                        dismissMyDialogFrag()
//                        val intent = Intent(
//                            myselectedActivity,
//                            InstagramDownloadCloudBypassWebview_method_0::class.java
//                        )
//                        intent.putExtra("myvidurl", urlwithoutlettersqp)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//                        myselectedActivity!!.startActivity(intent)

                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialogFrag()
                    System.err.println("workkkkkkkkk 5")
                    e.printStackTrace()
                    ShowToast(myselectedActivity!!, "error")
                }

            }
            methode1.setOnClickListener {
                dialog.dismiss()

                try {
                    System.err.println("workkkkkkkkk 4 ")

                    val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity!!)
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                    ) {

                        System.err.println(
                            "workkkkkkkkk 476 " + urlwithoutlettersqp + "____" +
                                    "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )

                        downloadInstagramImageOrVideodata_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        downloadInstagramImageOrVideoData(
                            urlwithoutlettersqp,
                            ""
                        )
                        // downloadInstagramImageOrVideoResOkhttpM2(urlwithoutlettersqp_noa)
                        //downloadInstagramImageOrVideo_tikinfApi(urlwithoutlettersqp)
                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialogFrag()
                    System.err.println("workkkkkkkkk 5")
                    e.printStackTrace()
                    ShowToast(myselectedActivity!!, "error")
                }


            }
            methode2.setOnClickListener {
                dialog.dismiss()

                try {
                    System.err.println("workkkkkkkkk 4 ")

                    val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity!!)
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                    ) {

                        System.err.println(
                            "workkkkkkkkk 476 " + urlwithoutlettersqp + "____" +
                                    "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )

                        downloadInstagramImageOrVideodata_old_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        downloadInstagramImageOrVideoResponseOkhttp(
                            urlwithoutlettersqp_noa
                        )
                        // downloadInstagramImageOrVideoResponseOkhttp(urlwithoutlettersqp_noa)
                        // downloadInstagramImageOrVideoResOkhttpM2(urlwithoutlettersqp_noa)
                        //downloadInstagramImageOrVideo_tikinfApi(urlwithoutlettersqp)
                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialogFrag()
                    System.err.println("workkkkkkkkk 5")
                    e.printStackTrace()
                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                }

            }

            //TODO only working for videos
            methode3.setOnClickListener {
                dialog.dismiss()

                try {
                    System.err.println("workkkkkkkkk 4")
                    val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity!!)
                    val map = sharedPrefsFor.preference
                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                    ) {
                        System.err.println("workkkkkkkkk m2 5.2")
                        downloadInstagramImageOrVideodata_withlogin(
                            urlwithoutlettersqp,
                            "ds_user_id=" + map.preferencE_USERID
                                    + "; sessionid=" + map.preferencE_SESSIONID
                        )
                    } else {
                        System.err.println("workkkkkkkkk 4.5")

                        login()
//                        downloadInstagramImageOrVideodata(
//                            urlwithoutlettersqp,
//                            ""
//                        )
//
                        dismissMyDialogFrag()
//                        val intent = Intent(myselectedActivity, GetLinkThroughWebView::class.java)
//                        intent.putExtra("myurlis", "$urlwithoutlettersqp_noa?_fb_noscript=1")
//                        startActivityForResult(intent, 2)

                    }
                } catch (e: java.lang.Exception) {
                    dismissMyDialogFrag()
                    System.err.println("workkkkkkkkk 5.1")
                    e.printStackTrace()
                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                }

            }

            methode4.setOnClickListener {
                dialog.dismiss()
                insDownMethod.download(Url)

//                try {
//                    loginSnapIntaWeb(urlwithoutlettersqp_noa)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    dismissMyDialogErrorToastFrag()
//                }

            }
//            methode5.setOnClickListener {
//                dialog.dismiss()
//
//                try {
//                    loginDownloadgram(urlwithoutlettersqp_noa)
//
//
////                    val intent = Intent(
////                        myselectedActivity!!,
////                        WebViewDownloadTesting::class.java
////                    )
////                    intent.putExtra("myvidurl", urlwithoutlettersqp_noa)
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////
////                    myselectedActivity!!.startActivity(intent)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    dismissMyDialogErrorToastFrag()
//                }
//
//            }

            //TODO only working for videos yt-dlp
//            methode6.setOnClickListener {
//                dialog.dismiss()
//
//                dismissMyDialogFrag()
//
//                var myurl = urlwithoutlettersqp_noa
//                try {
//                    myurl = iUtils.extractUrls(myurl)[0]
//                } catch (_: Exception) {
//                }
//                DownloadVideosMain.Start(myselectedActivity, myurl.trim(), false)
//                Log.e("downloadFileName12", myurl.trim())
//            }
            dig_btn_cancel.setOnClickListener {
                dialog.dismiss()
                dismissMyDialogFrag()
            }
            dialog.setCancelable(false)
            dialog.show()
        }


    }

    @Keep
    fun downloadInstagramImageOrVideodata_withlogin(URL: String?, Cookie: String?) {
        /*instagram product types
        * product_type
        *
        * igtv "media_type": 2
        * carousel_container  "media_type": 8
        * clips  "media_type": 2
        * feed   "media_type": 1
        * */

        var Cookie = Cookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        val apiService: RetrofitApiInterface =
            RetrofitClient.getClient()

        println("workkkkk777_resyy " + URL)
        val callResult: Call<JsonObject> = apiService.getInstagramData(
            URL,
            Cookie,
            iUtils.generateInstagramUserAgent(),
            "936619743392459"
        )
        callResult.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: retrofit2.Response<JsonObject?>
            ) {
                println("workkkkk777_res " + response.body())

                try {
                    val listType: Type =
                        object : TypeToken<ModelInstaWithLogin?>() {}.type
                    val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                        response.body(),
                        listType
                    )
                    println("workkkkk777 " + modelInstagramResponse.items[0].code)

                    if (modelInstagramResponse.items[0].mediaType == 8) {
                        myInstaUsername = modelInstagramResponse.items[0].user.username + "_"

                        val modelGetEdgetoNode = modelInstagramResponse.items[0]

                        val modelEdNodeArrayList: List<CarouselMedia> =
                            modelGetEdgetoNode.carouselMedia
                        for (i in modelEdNodeArrayList.indices) {

                            System.err.println("workkkkkkkkklogin issue " + modelEdNodeArrayList[i].mediaType)


                            if (modelEdNodeArrayList[i].mediaType == 2) {
                                System.err.println("workkkkkkkkklogin issue vid " + modelEdNodeArrayList[i].videoVersions[0].geturl())


                                myVideoUrlIs =
                                    modelEdNodeArrayList[i].videoVersions[0].geturl()
                                DownloadFileMain.startDownloading(
                                    myselectedActivity!!,
                                    myVideoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                    ".mp4"
                                )
                                // etText.setText("")
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                myVideoUrlIs = ""
                            } else {

                                System.err.println("workkkkkkkkklogin issue img " + modelEdNodeArrayList[i].imageVersions2.candidates[0].geturl())


                                myPhotoUrlIs =
                                    modelEdNodeArrayList[i].imageVersions2.candidates[0]
                                        .geturl()
                                DownloadFileMain.startDownloading(
                                    myselectedActivity!!,
                                    myPhotoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                    ".png"
                                )
                                myPhotoUrlIs = ""
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                // etText.setText("")
                            }
                        }
                    } else {
                        val modelGetEdgetoNode = modelInstagramResponse.items[0]
                        myInstaUsername = modelInstagramResponse.items[0].user.username + "_"

                        if (modelGetEdgetoNode.mediaType == 2) {
                            myVideoUrlIs =
                                modelGetEdgetoNode.videoVersions[0].geturl()
                            DownloadFileMain.startDownloading(
                                myselectedActivity!!,
                                myVideoUrlIs,
                                myInstaUsername + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
                                ".mp4"
                            )
                            try {
                                dismissMyDialogFrag()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            myVideoUrlIs = ""
                        } else {
                            myPhotoUrlIs =
                                modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                            DownloadFileMain.startDownloading(
                                myselectedActivity!!,
                                myPhotoUrlIs,
                                myInstaUsername + iUtils.getVideoFilenameFromURL(myPhotoUrlIs),
                                ".png"
                            )
                            try {
                                dismissMyDialogFrag()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            myPhotoUrlIs = ""
                        }
                    }

                } catch (e: java.lang.Exception) {
                    System.err.println("workkkkkkkkk 5nn errrr " + e.message)



                    try {

                        try {
                            System.err.println("workkkkkkkkk 4")

                            val sharedPrefsFor = SharedPrefsForInstagram(myselectedActivity!!)
                            val map = sharedPrefsFor.preference
                            if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                            ) {
                                System.err.println("workkkkkkkkk 5.2")
                                downloadInstagramImageOrVideodata_old_withlogin(
                                    URL, "ds_user_id=" + map.preferencE_USERID
                                            + "; sessionid=" + map.preferencE_SESSIONID
                                )
                            } else {
                                dismissMyDialogFrag()
                                System.err.println("workkkkkkkkk 5.1")
                                e.printStackTrace()
                                ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                            }
                        } catch (e: java.lang.Exception) {
                            dismissMyDialogFrag()
                            System.err.println("workkkkkkkkk 5.1")
                            e.printStackTrace()
                            ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                        }

                    } catch (e: Exception) {

                        e.printStackTrace()
                        myselectedActivity!!.runOnUiThread {
                            dismissMyDialogFrag()
                            if (!myselectedActivity!!.isFinishing) {
                                val alertDialog =
                                    AlertDialog.Builder(myselectedActivity!!)
                                        .create()
                                alertDialog.setTitle(getString(R.string.logininsta))
                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_POSITIVE,
                                    getString(R.string.logininsta)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(
                                        myselectedActivity!!,
                                        InstagramLoginActivity::class.java
                                    )
                                    myselectedActivity!!.startActivityForResult(intent, 200)
                                }
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_NEGATIVE,
                                    getString(R.string.cancel)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                alertDialog.show()
                            }
                        }
                    }


                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                println("response1122334455:   " + "Failed0")
                try {
                    dismissMyDialogFrag()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                myselectedActivity!!.runOnUiThread {
                    iUtils.ShowToastError(
                        myselectedActivity,
                        myselectedActivity!!.resources.getString(R.string.somthing)
                    )
                }

            }
        })
    }

    @Keep
    fun downloadInstagramImageOrVideodata_old_withlogin(URL: String?, Cookie: String?) {
        val random1 = Random()
        val j = random1.nextInt(iUtils.UserAgentsList.size)
        object : Thread() {
            override fun run() {
                Looper.prepare()
                val client = OkHttpClient().newBuilder()
                    .build()
                val request: Request = Request.Builder()
                    .url(URL!!)
                    .method("GET", null)
                    .addHeader("Cookie", Cookie!!)
                    .addHeader(
                        "User-Agent",
                        iUtils.UserAgentsList[j]
                    )
                    .build()
                try {
                    val response = client.newCall(request).execute()

                    if (response.code == 200) {

                        val ress = response.body!!.string()
                        println("working runed \t Value: $ress")

                        try {
                            val listType: Type =
                                object : TypeToken<ModelInstaWithLogin?>() {}.type
                            val modelInstagramResponse: ModelInstaWithLogin = Gson().fromJson(
                                ress,
                                listType
                            )
                            println("workkkkk777 " + modelInstagramResponse.items[0].code)


                            if (modelInstagramResponse.items[0].mediaType == 8) {


                                println("workkkkk777 mediacount " + modelInstagramResponse.items[0].carouselMediaCount)


                                myInstaUsername =
                                    modelInstagramResponse.items[0].user.username + "_"

                                val modelGetEdgetoNode = modelInstagramResponse.items[0]
                                val modelEdNodeArrayList: List<CarouselMedia> =
                                    modelGetEdgetoNode.carouselMedia
                                for (i in modelEdNodeArrayList.indices) {
                                    if (modelEdNodeArrayList[i].mediaType == 2) {
                                        myVideoUrlIs =
                                            modelEdNodeArrayList[i].videoVersions[0].geturl()
                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myVideoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myVideoUrlIs
                                            ),
                                            ".mp4"
                                        )
                                        // etText.setText("")
                                        try {
                                            dismissMyDialogFrag()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelEdNodeArrayList[i].imageVersions2.candidates[0]
                                                .geturl()
                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myPhotoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myPhotoUrlIs
                                            ),
                                            ".png"
                                        )
                                        myPhotoUrlIs = ""
                                        try {
                                            dismissMyDialogFrag()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        // etText.setText("")
                                    }
                                }
                            } else {
                                val modelGetEdgetoNode = modelInstagramResponse.items[0]
                                myInstaUsername =
                                    modelInstagramResponse.items[0].user.username + "_"

                                if (modelGetEdgetoNode.mediaType == 2) {
                                    myVideoUrlIs =
                                        modelGetEdgetoNode.videoVersions[0].geturl()
                                    DownloadFileMain.startDownloading(
                                        myselectedActivity!!,
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myVideoUrlIs
                                        ),
                                        ".mp4"
                                    )
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                                    DownloadFileMain.startDownloading(
                                        myselectedActivity!!,
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                            myPhotoUrlIs
                                        ),

                                        ".png"
                                    )
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myPhotoUrlIs = ""
                                }
                            }

                        } catch (e: java.lang.Exception) {
                            System.err.println("workkkkkkkkk 5nny errrr " + e.message)
                            try {
                                try {
                                    System.err.println("workkkkkkkkk 4")

                                    val sharedPrefsFor =
                                        SharedPrefsForInstagram(myselectedActivity!!)
                                    val map = sharedPrefsFor.preference
                                    if (map != null && map.preferencE_USERID != null && map.preferencE_USERID != "oopsDintWork" && map.preferencE_USERID != ""
                                    ) {
                                        System.err.println("workkkkkkkkk 5.5")
                                        downloadInstagramImageOrVideoData(
                                            URL, "ds_user_id=" + map.preferencE_USERID
                                                    + "; sessionid=" + map.preferencE_SESSIONID
                                        )
                                    } else {
                                        dismissMyDialogFrag()
                                        System.err.println("workkkkkkkkk 5.1")
                                        e.printStackTrace()
                                        ShowToast(
                                            myselectedActivity!!,
                                            getString(R.string.error_occ)
                                        )
                                    }
                                } catch (e: java.lang.Exception) {
                                    dismissMyDialogFrag()
                                    System.err.println("workkkkkkkkk 5.1")
                                    e.printStackTrace()
                                    ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                try {
                                    myselectedActivity!!.runOnUiThread {
                                        dismissMyDialogFrag()
                                        if (!myselectedActivity!!.isFinishing) {
                                            val alertDialog =
                                                AlertDialog.Builder(myselectedActivity!!).create()
                                            alertDialog.setTitle(getString(R.string.logininsta))
                                            alertDialog.setMessage(getString(R.string.urlisprivate))
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_POSITIVE,
                                                getString(R.string.logininsta)
                                            ) { dialog, _ ->
                                                dialog.dismiss()
                                                val intent = Intent(
                                                    myselectedActivity!!,
                                                    InstagramLoginActivity::class.java
                                                )
//                                                startActivityForResult(intent, 200)
                                                myselectedActivity!!.startActivity(intent)
                                            }
                                            alertDialog.setButton(
                                                AlertDialog.BUTTON_NEGATIVE,
                                                getString(R.string.cancel)
                                            ) { dialog, _ ->
                                                dialog.dismiss()
                                            }
                                            alertDialog.show()
                                        }
                                    }
                                } catch (_: Exception) {

                                }
                            }
                        }
                    } else {
                        object : Thread() {
                            override fun run() {

                                val client = OkHttpClient().newBuilder()
                                    .build()
                                val request: Request = Request.Builder()
                                    .url(URL)
                                    .method("GET", null)
                                    .addHeader("Cookie", iUtils.myInstagramTempCookies)
                                    .addHeader(
                                        "User-Agent",
                                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36"
                                    ).build()
                                try {
                                    val response1: Response = client.newCall(request).execute()

                                    if (response1.code == 200) {

                                        try {
                                            val listType: Type =
                                                object : TypeToken<ModelInstaWithLogin?>() {}.type
                                            val modelInstagramResponse: ModelInstaWithLogin =
                                                Gson().fromJson(
                                                    response.body!!.string(),
                                                    listType
                                                )
                                            if (modelInstagramResponse.items[0].mediaType == 8) {
                                                myInstaUsername =
                                                    modelInstagramResponse.items[0].user.username + "_"

                                                val modelGetEdgetoNode =
                                                    modelInstagramResponse.items[0]
                                                val modelEdNodeArrayList: List<CarouselMedia> =
                                                    modelGetEdgetoNode.carouselMedia
                                                for (i in modelEdNodeArrayList.indices) {
                                                    if (modelEdNodeArrayList[i].mediaType == 2) {
                                                        myVideoUrlIs =
                                                            modelEdNodeArrayList[i].videoVersions[0].geturl()
                                                        DownloadFileMain.startDownloading(
                                                            myselectedActivity!!,
                                                            myVideoUrlIs,
                                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                                myVideoUrlIs
                                                            ),
                                                            ".mp4"
                                                        )
                                                        // etText.setText("")
                                                        try {
                                                            dismissMyDialogFrag()
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                        myVideoUrlIs = ""
                                                    } else {
                                                        myPhotoUrlIs =
                                                            modelEdNodeArrayList[i].imageVersions2.candidates[0].geturl()
                                                        DownloadFileMain.startDownloading(
                                                            myselectedActivity!!,
                                                            myPhotoUrlIs,
                                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                                myPhotoUrlIs
                                                            ),
                                                            ".png"
                                                        )
                                                        myPhotoUrlIs = ""
                                                        try {
                                                            dismissMyDialogFrag()
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                        // etText.setText("")
                                                    }
                                                }
                                            } else {
                                                myInstaUsername =
                                                    modelInstagramResponse.items[0].user.username + "_"

                                                val modelGetEdgetoNode =
                                                    modelInstagramResponse.items[0]
                                                if (modelGetEdgetoNode.mediaType == 2) {
                                                    myVideoUrlIs =
                                                        modelGetEdgetoNode.videoVersions[0]
                                                            .geturl()
                                                    DownloadFileMain.startDownloading(
                                                        myselectedActivity!!,
                                                        myVideoUrlIs,
                                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                            myVideoUrlIs
                                                        ),
                                                        ".mp4"
                                                    )

                                                    try {
                                                        dismissMyDialogFrag()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                    myVideoUrlIs = ""
                                                } else {
                                                    myPhotoUrlIs =
                                                        modelGetEdgetoNode.imageVersions2.candidates[0].geturl()
                                                    DownloadFileMain.startDownloading(
                                                        myselectedActivity!!,
                                                        myPhotoUrlIs,
                                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                            myPhotoUrlIs
                                                        ),
                                                        ".png"
                                                    )
                                                    try {
                                                        dismissMyDialogFrag()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                    myPhotoUrlIs = ""
                                                }
                                            }
                                        } catch (e: java.lang.Exception) {
                                            System.err.println("workkkkkkkkk 5nn errrr " + e.message)
                                            e.printStackTrace()
                                            try {
                                                myselectedActivity!!.runOnUiThread {
                                                    dismissMyDialogFrag()

                                                    if (!myselectedActivity!!.isFinishing) {
                                                        val alertDialog =
                                                            AlertDialog.Builder(myselectedActivity!!)
                                                                .create()
                                                        alertDialog.setTitle(getString(R.string.logininsta))
                                                        alertDialog.setMessage(getString(R.string.urlisprivate))
                                                        alertDialog.setButton(
                                                            AlertDialog.BUTTON_POSITIVE,
                                                            getString(R.string.logininsta)
                                                        ) { dialog, _ ->
                                                            dialog.dismiss()
                                                            val intent = Intent(
                                                                myselectedActivity!!,
                                                                InstagramLoginActivity::class.java
                                                            )
//                                                            startActivityForResult(intent, 200)
                                                            myselectedActivity!!.startActivity(intent)
                                                        }
                                                        alertDialog.setButton(
                                                            AlertDialog.BUTTON_NEGATIVE,
                                                            getString(R.string.cancel)
                                                        ) { dialog, _ ->
                                                            dialog.dismiss()
                                                        }
                                                        alertDialog.show()
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                    } else {
                                        System.err.println("workkkkkkkkk 6bbb errrr ")
                                        myselectedActivity!!.runOnUiThread {
                                            dismissMyDialogFrag()

                                            if (!myselectedActivity!!.isFinishing) {
                                                val alertDialog =
                                                    AlertDialog.Builder(myselectedActivity!!)
                                                        .create()
                                                alertDialog.setTitle(getString(R.string.logininsta))
                                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_POSITIVE,
                                                    getString(R.string.logininsta)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                    val intent = Intent(
                                                        myselectedActivity!!,
                                                        InstagramLoginActivity::class.java
                                                    )
//                                                    startActivityForResult(intent, 200)
                                                    myselectedActivity!!.startActivity(intent)
                                                }
                                                alertDialog.setButton(
                                                    AlertDialog.BUTTON_NEGATIVE,
                                                    getString(R.string.cancel)
                                                ) { dialog, _ ->
                                                    dialog.dismiss()
                                                }
                                                alertDialog.show()
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }.start()
                    }
                } catch (e: Exception) {
                    try {
                        println("response1122334455:   " + "Failed1 " + e.message)
                        dismissMyDialogFrag()
                    } catch (_: Exception) {

                    }
                }
            }
        }.start()
    }

    @Keep
    fun downloadInstagramImageOrVideoData(URL: String?, Coookie: String?) {

        var Cookie = Coookie
        if (TextUtils.isEmpty(Cookie)) {
            Cookie = ""
        }
        val apiService: RetrofitApiInterface =
            RetrofitClient.getClient()

        val callResult: Call<JsonObject> = apiService.getInstagramData(
            URL,
            Cookie,
            iUtils.generateInstagramUserAgent(),
            "936619743392459"
        )
        callResult.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: retrofit2.Response<JsonObject?>
            ) {
                println("response1122334455 ress :   " + response.body())
                try {


//                                val userdata = response.body()!!.getAsJsonObject("graphql")
//                                    .getAsJsonObject("shortcode_media")
//                                binding!!.profileFollowersNumberTextview.setText(
//                                    userdata.getAsJsonObject(
//                                        "edge_followed_by"
//                                    )["count"].asString
//                                )
//                                binding!!.profileFollowingNumberTextview.setText(
//                                    userdata.getAsJsonObject(
//                                        "edge_follow"
//                                    )["count"].asString
//                                )
//                                binding!!.profilePostNumberTextview.setText(userdata.getAsJsonObject("edge_owner_to_timeline_media")["count"].asString)
//                                binding!!.profileLongIdTextview.setText(userdata["username"].asString)
//


                    val listType = object : TypeToken<ModelInstagramResponse?>() {}.type
                    val modelInstagramResponse: ModelInstagramResponse? = GsonBuilder().create()
                        .fromJson<ModelInstagramResponse>(
                            response.body().toString(),
                            listType
                        )
                    if (modelInstagramResponse != null) {
                        if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                            val modelGetEdgetoNode: ModelGetEdgetoNode =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children
                            myInstaUsername =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                            val modelEdNodeArrayList: List<ModelEdNode> =
                                modelGetEdgetoNode.modelEdNodes
                            for (i in modelEdNodeArrayList.indices) {
                                if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                    myVideoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.video_url
//                                    DownloadFileMain.startDownloading(
//                                        myselectedActivity!!,
//                                        myVideoUrlIs,
//                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
//                                            myVideoUrlIs
//                                        ),
//                                        ".mp4"
//                                    )
                                    Downloader.getInstance(myselectedActivity).downloadByUrl(
                                        myVideoUrlIs,
                                        myInstaUsername + iUtils.getVideoFilenameFromURL(
                                        myVideoUrlIs
                                    ),".mp4")
                                    // etText.setText("")
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myVideoUrlIs = ""
                                } else {
                                    myPhotoUrlIs =
                                        modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src
//                                    DownloadFileMain.startDownloading(
//                                        myselectedActivity!!,
//                                        myPhotoUrlIs,
//                                        myInstaUsername + iUtils.getImageFilenameFromURL(
//                                            myPhotoUrlIs
//                                        ),
//                                        ".png"
//                                    )

                                    Downloader.getInstance(myselectedActivity).downloadByUrl(
                                        myPhotoUrlIs,
                                        myInstaUsername + iUtils.getImageFilenameFromURL(
                                            myPhotoUrlIs
                                        ),
                                        ".png")

                                    myPhotoUrlIs = ""
                                    try {
                                        dismissMyDialogFrag()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    // etText.setText("")
                                }
                            }
                        } else {
                            val isVideo =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                            myInstaUsername =
                                modelInstagramResponse.modelGraphshortcode.shortcode_media.owner.username + "_"

                            if (isVideo) {
                                myVideoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url
//                                DownloadFileMain.startDownloading(
//                                    myselectedActivity!!,
//                                    myVideoUrlIs,
//                                    myInstaUsername + iUtils.getVideoFilenameFromURL(myVideoUrlIs),
//                                    ".mp4"
//                                )
                                Downloader.getInstance(myselectedActivity).downloadByUrl(
                                    myVideoUrlIs,
                                    myInstaUsername + iUtils.getVideoFilenameFromURL(
                                        myVideoUrlIs
                                    ),".mp4")
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                myVideoUrlIs = ""
                            } else {
                                myPhotoUrlIs =
                                    modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src
//                                DownloadFileMain.startDownloading(
//                                    myselectedActivity!!,
//                                    myPhotoUrlIs,
//                                    myInstaUsername + iUtils.getImageFilenameFromURL(myPhotoUrlIs),
//                                    ".png"
//                                )
                                Downloader.getInstance(myselectedActivity).downloadByUrl(
                                    myPhotoUrlIs,
                                    myInstaUsername + iUtils.getImageFilenameFromURL(
                                        myPhotoUrlIs
                                    ),
                                    ".png")
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                myPhotoUrlIs = ""
                            }
                        }
                    } else {
                        myselectedActivity!!.runOnUiThread {
                            iUtils.ShowToastError(
                                myselectedActivity,
                                myselectedActivity!!.resources.getString(R.string.somthing)
                            )
                        }
                        try {
                            dismissMyDialogFrag()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: java.lang.Exception) {
                    try {
                        try {
                            System.err.println("workkkkkkkkk 4" + " downloadInstagramImageOrVideodata")

                            downloadInstagramImageOrVideoData(
                                URL, ""
                            )
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            println("response1122334455 exe 1:   " + e.localizedMessage)
                            try {
                                dismissMyDialogFrag()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            System.err.println("workkkkkkkkk 5.1")
                            e.printStackTrace()
                            ShowToast(myselectedActivity!!, getString(R.string.error_occ))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        myselectedActivity!!.runOnUiThread {
                            dismissMyDialogFrag()
                            if (!myselectedActivity!!.isFinishing) {
                                e.printStackTrace()
                                myselectedActivity!!.runOnUiThread {
                                    iUtils.ShowToastError(
                                        myselectedActivity,
                                        myselectedActivity!!.resources.getString(R.string.somthing)
                                    )
                                }
                                println("response1122334455 exe 1:   " + e.localizedMessage)
                                try {
                                    dismissMyDialogFrag()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                val alertDialog = AlertDialog.Builder(myselectedActivity!!).create()
                                alertDialog.setTitle(getString(R.string.logininsta))
                                alertDialog.setMessage(getString(R.string.urlisprivate))
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_POSITIVE, getString(R.string.logininsta)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(
                                        myselectedActivity!!,
                                        InstagramLoginActivity::class.java
                                    )
//                                    startActivityForResult(intent, 200)
                                    myselectedActivity!!.startActivity(intent)
                                }
                                alertDialog.setButton(
                                    AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                alertDialog.show()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                try {
                    println("response1122334455:   " + "Failed0" + t.message)
                    dismissMyDialogFrag()
                    myselectedActivity!!.runOnUiThread {
                        iUtils.ShowToastError(
                            myselectedActivity,
                            myselectedActivity!!.resources.getString(R.string.somthing)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    @Keep
    fun downloadInstagramImageOrVideoResponseOkhttp(URL: String?) {

//TODO check
//        Unirest.config()
//            .socketTimeout(500)
//            .connectTimeout(1000)
//            .concurrency(10, 5)
//            .proxy(Proxy("https://proxy"))
//            .setDefaultHeader("Accept", "application/json")
//            .followRedirects(false)
//            .enableCookieManagement(false)
//            .addInterceptor(MyCustomInterceptor())

        object : Thread() {
            override fun run() {


                try {

                    val cookieJar: ClearableCookieJar = PersistentCookieJar(
                        SetCookieCache(),
                        SharedPrefsCookiePersistor(myselectedActivity)
                    )

                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    // init OkHttpClient
                    val client: OkHttpClient = OkHttpClient.Builder()
                        .cookieJar(cookieJar)
                        .addInterceptor(logging)
                        .build()

                    val request: Request = Request.Builder()
                        .url("$URL?__a=1&__d=dis")
                        .method("GET", null)
                        .header("Cookie", iUtils.myInstagramTempCookies)
                        .header("user-agent", iUtils.generateInstagramUserAgent())
                        .build()
                    val response = client.newCall(request).execute()

                    val ressd = response.body!!.string()

                    println("instaress_test $ressd")

                    var code = response.code
                    if (!ressd.contains("shortcode_media")) {
                        code = 400
                    }
                    if (code == 200) {


                        try {


                            val listType =
                                object : TypeToken<ModelInstagramResponse?>() {}.type
                            val modelInstagramResponse: ModelInstagramResponse? =
                                GsonBuilder().create()
                                    .fromJson<ModelInstagramResponse>(
                                        ressd,
                                        listType
                                    )


                            if (modelInstagramResponse != null) {


                                if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                    val modelGetEdgetoNode: ModelGetEdgetoNode =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children

                                    val modelEdNodeArrayList: List<ModelEdNode> =
                                        modelGetEdgetoNode.modelEdNodes
                                    for (i in 0 until modelEdNodeArrayList.size) {
                                        if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                            myVideoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.video_url


                                            DownloadFileMain.startDownloading(
                                                myselectedActivity!!,
                                                myVideoUrlIs,
                                                myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                    myVideoUrlIs
                                                ),
                                                ".mp4"
                                            )
                                            dismissMyDialogFrag()


                                            myVideoUrlIs = ""
                                        } else {
                                            myPhotoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src

                                            DownloadFileMain.startDownloading(
                                                myselectedActivity!!,
                                                myPhotoUrlIs,
                                                myInstaUsername + iUtils.getImageFilenameFromURL(
                                                    myPhotoUrlIs
                                                ),
                                                ".png"
                                            )
                                            myPhotoUrlIs = ""
                                            dismissMyDialogFrag()
                                            // etText.setText("")
                                        }
                                    }
                                } else {
                                    val isVideo =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                    if (isVideo) {
                                        myVideoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url


                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myVideoUrlIs,
                                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                                myVideoUrlIs
                                            ),
                                            ".mp4"
                                        )
                                        dismissMyDialogFrag()
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src


                                        DownloadFileMain.startDownloading(
                                            myselectedActivity!!,
                                            myPhotoUrlIs,
                                            myInstaUsername + iUtils.getImageFilenameFromURL(
                                                myPhotoUrlIs
                                            ),
                                            ".png"
                                        )
                                        dismissMyDialogFrag()
                                        myPhotoUrlIs = ""
                                    }
                                }


                            } else {
                                myselectedActivity!!.runOnUiThread {
                                    iUtils.ShowToastError(
                                        myselectedActivity,
                                        myselectedActivity!!.resources.getString(R.string.somthing)
                                    )
                                }

                                dismissMyDialogFrag()

                            }


                        } catch (e: Exception) {
//                            myselectedActivity!!.runOnUiThread {
//                                progressDralogGenaratinglink.setMessage("Method 1 failed trying method 2")
//                            }
                            downloadInstagramImageOrVideoResOkhttpM2(URL!!)

                        }

                    } else {
//                        myselectedActivity!!.runOnUiThread {
//                            progressDralogGenaratinglink.setMessage("Method 1 failed trying method 2")
//                        }
                        downloadInstagramImageOrVideoResOkhttpM2(URL!!)
                    }


                } catch (e: Throwable) {
                    e.printStackTrace()
                    println("The request has failed " + e.message)
//                    myselectedActivity!!.runOnUiThread {
//                        progressDralogGenaratinglink.setMessage("Method 1 failed trying method 2")
//                    }
                    downloadInstagramImageOrVideoResOkhttpM2(URL!!)
                }
            }
        }.start()
    }

    @Keep
    fun downloadInstagramImageOrVideoResOkhttpM2(URL: String?) {


        try {


            val cookieJar: ClearableCookieJar = PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(myselectedActivity)
            )

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            // init OkHttpClient
            val client: OkHttpClient = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(logging)
                .build()
            println("instaress_test ${URL!!.split("?").get(0)}" + "embed/captioned/?_fb_noscript=1")
            val request: Request = Request.Builder()
                .url(URL.split("?").get(0) + "embed/captioned/?_fb_noscript=1")
                .method("GET", null)
                .build()
            val response = client.newCall(request).execute()
            //https://www.instagram.com/reel/C8M0LtuIHpb/embed/captioned/
            val ss = response.body!!.string()

            println("instaress_test $ss")

            if (response.code == 200) {

                try {
                    var doc: String =
                        ss.substring(ss.indexOf("contextJSON"), ss.indexOf("[]}}}\"")) + "[]}}}"
                    doc = doc.replace("contextJSON\":\"", "")

                    doc = doc.substring(doc.indexOf("video_url"), doc.indexOf("video_view_count"))
                    doc = doc.substring(14, doc.length - 5)

//                    println("instaress_test ${decodedHtml.getJSONObject("context")}")
                    val decodedHtml = StringEscapeUtils.unescapeHtml4(doc)
                    myVideoUrlIs = decodedHtml.replace("""\\\/""", "/")
                    println("instaress_test ${myVideoUrlIs}")
                    myVideoUrlIs = StringEscapeUtils.unescapeJava(myVideoUrlIs);

                    println("instaress_test after decode= ${myVideoUrlIs}")

                    if (myVideoUrlIs != null && !myVideoUrlIs.equals("")) {


                        DownloadFileMain.startDownloading(
                            myselectedActivity!!,
                            myVideoUrlIs,
                            myInstaUsername + iUtils.getVideoFilenameFromURL(
                                myVideoUrlIs
                            ),
                            ".mp4"
                        )
                        dismissMyDialogFrag()


                        myVideoUrlIs = ""

                    } else {
                        myselectedActivity!!.runOnUiThread {
                            iUtils.ShowToastError(
                                myselectedActivity,
                                myselectedActivity!!.resources.getString(R.string.somthing)
                            )
                        }

                        dismissMyDialogFrag()


                    }


                } catch (e: Exception) {
                    dismissMyDialogFrag()
//                    myselectedActivity!!.runOnUiThread {
//                        progressDralogGenaratinglink.setMessage("Method 2 failed trying method 3")
//                    }
//                    downloadInstagramImageOrVideo_tikinfApi(URL)


                }

            } else {
                dismissMyDialogFrag()
//                myselectedActivity!!.runOnUiThread {
//                    progressDralogGenaratinglink.setMessage("Method 2 failed trying method 3")
//                }
//                downloadInstagramImageOrVideo_tikinfApi(URL)
            }


        } catch (e: Throwable) {
            e.printStackTrace()
            println("The request has failed " + e.message)
//            myselectedActivity!!.runOnUiThread {
//                progressDralogGenaratinglink.setMessage("Method 2 failed trying method 3")
//            }
//            downloadInstagramImageOrVideo_tikinfApi(URL)
            dismissMyDialogFrag()
        }
    }

    fun login(){
        val alertDialog =
            AlertDialog.Builder(myselectedActivity!!)
                .create()
        alertDialog.setTitle(getString(R.string.logininsta))
        alertDialog.setMessage(getString(R.string.urlisprivate))
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            getString(R.string.logininsta)
        ) { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(
                myselectedActivity!!,
                InstagramLoginActivity::class.java
            )
            myselectedActivity!!.startActivityForResult(intent, 200)
        }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            getString(R.string.cancel)
        ) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }
    fun getString(resid: Int): String{
        return myselectedActivity!!.getString(resid)
    }
    fun dismissMyDialogFrag(){
        myPd?.dismiss()
    }
}