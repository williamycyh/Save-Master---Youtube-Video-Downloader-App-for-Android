package com.example.tubedown;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.lib.Downloader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileFragment extends Fragment {
    View rootView;
    public static final String TAG = "FileFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.d_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                listener();
                loadNative();
            }
        });
    }

//    NativeAd minNativeAd = new NativeAd();

    void loadNative(){
        if(getActivity() == null || getActivity().isFinishing()){
            return;
        }
//        minNativeAd.setNativeLoadResult(new NativeAd.NativeLoadResult() {
//            @Override
//            public void onResult(boolean success) {
//                if(!success){
//                    loadBanner();
//                }
//            }
//        });
//        minNativeAd.loadNativeAds(true, getActivity(), banner_container_file, AdUnit.ADUNIT_MIN_NATIVE);
//        loadBanner();
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
//            bannerAdCenter.stopAutoRefresh();
        }catch (Exception e){
        }
    }

//    BannerAd bannerAdCenter = new BannerAd();
//    private void loadBanner(){
//        if(getActivity() == null || getActivity().isFinishing()){
//            return;
//        }
//
//        bannerAdCenter.loadBannerAds(getActivity(), banner_container_file,
//                AdUnit.ADUNIT_banner);
//    }

    RecyclerView recyclerView;
    FileAdapter adapter;
    List<File> fileList = new ArrayList();

    FrameLayout banner_container_file;
    public void initView(){
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new FileAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        banner_container_file = rootView.findViewById(R.id.banner_container_file);
        adapter.addAll(fileList);
        loadData();
    }

    public void loadDataIfNO(){
        loadData();
    }

    public void loadData(){
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                if(getActivity() == null || getActivity().isFinishing()){
                    return;
                }
                File dir = Downloader.getInstance(getActivity()).getDownloadDir();
                if(dir == null || dir.listFiles() == null){
                    return;
                }
                List<File> tempList = new ArrayList();
                tempList.clear();

                for(File file : dir.listFiles()){
                    if(file.getName().endsWith(".voicetemp")){
//                        tempList.add(file);
                    } else {
                        tempList.add(file);
                    }
                }
//                fileList.addAll(Arrays.asList());
                Collections.sort(tempList, new FileComparator());

//                UIConfigManager.setFileCount(tempList.size());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fileList.clear();
                        fileList.addAll(tempList);
                        adapter.addAll(fileList);
                        adapter.notifyDataSetChanged();

                        mergeAudio();
                    }
                });
            }
        });
    }

    class FileAdapter extends RecyclerView.Adapter{
        List<File> allFiles = new ArrayList<>();
        public void addAll(List<File> lists){
            if(lists == null || lists.isEmpty()){
                return;
            }
            allFiles.clear();
            allFiles.addAll(lists);
        }

        public List<File> getAllFiles() {
            return allFiles;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.d_file_item_v,parent,false);
            FileItemViewHolder viewHolder = new FileItemViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            final File file = allFiles.get(position);
            ((FileItemViewHolder)holder).fileNameTv.setText(file.getName());
            ((FileItemViewHolder)holder).fileDateTv.setText(Commons.format(file.lastModified()));
            ((FileItemViewHolder)holder).fileSizeTv.setText(Commons.readableFileSize(file.length()));

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    openFile(file);
                }
            });

            ((FileItemViewHolder)holder).audio_more_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPop(view, file, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return allFiles.size();
        }
    }

    private void openFile(File file){
        try{

//            int lastindex = file.getName().lastIndexOf('.');
//            if(lastindex > 0 && lastindex < file.getName().length()){
//                String na = file.getName().substring(0, file.getName().lastIndexOf('.'));
//                String tempName = file.getParent() + "/" + na + Commons.VOICE_TEMP;
//                if(new File(tempName).exists()){
//                    Toast.makeText(getActivity(), getString(R.string.no_voice_alert),
//                            Toast.LENGTH_LONG).show();
//                }
//            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri= FileProvider.getUriForFile(rootView.getContext(),
                    BuildConfig.APPLICATION_ID + ".provider",file);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            String extension = Commons.getExtension(file.getName());
            if("gif".equalsIgnoreCase(extension)
                    || "jpg".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension)){
                intent.setDataAndType(uri, "image/*");
            } else {
                intent.setDataAndType(uri, "video/*");
            }

            rootView.getContext().startActivity(intent);
        }catch (IllegalArgumentException e){
            Toast.makeText(rootView.getContext(), "error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    void mergeAudioAlert(final File item){
//        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.file_merge_dialog, null);
//        Button merge = view.findViewById(R.id.merge);
//        Button open_anyway =view.findViewById(R.id.open_anyway);
//
//        final Dialog dialog= builder.create();
//        dialog.show();
//        dialog.getWindow().setContentView(view);
//        //使editext可以唤起软键盘
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        open_anyway.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openFile(item);
//                dialog.dismiss();
//            }
//        });
//        merge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                dialog.dismiss();
//            }
//        });
//    }


    PopupWindow actionPopup = null;

    public void showPop(View view, final File item, final int postioin) {

        int width = Commons.dip2px(getActivity(), 140f);
        View poprootView = LayoutInflater.from(getActivity()).inflate(R.layout.d_action_item, null);
        actionPopup = new PopupWindow(poprootView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        actionPopup.setTouchable(true);
        actionPopup.setFocusable(true);
        actionPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        actionPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        actionPopup.showAsDropDown(view, width, 0);

        View ranameView = poprootView.findViewById(R.id.rename_layout);
        ranameView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showNameInputDialog(item, postioin);
                actionPopup.dismiss();
            }
        });

        View deleteView = poprootView.findViewById(R.id.delete_layout);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete(item);
                actionPopup.dismiss();
            }
        });

        View share_layout = poprootView.findViewById(R.id.share_layout);
        share_layout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Commons.share(getActivity(), item);
                actionPopup.dismiss();
            }
        });

        View detail_Layout = poprootView.findViewById(R.id.detail_layout);
        detail_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), item.getPath(), Toast.LENGTH_LONG).show();
                actionPopup.dismiss();
            }
        });
    }

    void confirmDelete(final File item){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.d_delete_layout, null);
        Button delete = view.findViewById(R.id.delete_confirm);
        Button cancel =view.findViewById(R.id.cancel_delete_confirm);

        final Dialog dialog= builder.create();
        dialog.show();
        dialog.getWindow().setContentView(view);
        //使editext可以唤起软键盘
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileList.remove(item);
                item.delete();
                adapter.addAll(fileList);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    void showNameInputDialog(final File item, final int postioin){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.d_input_layout, null);
        TextView cancel = view.findViewById(R.id.path_cancel);
                TextView sure =view.findViewById(R.id.path_confirm);
        final EditText edittext = view.findViewById(R.id.path_edittext);
         edittext.setText(item.getName());

        final Dialog dialog= builder.create();
        dialog.show();
        dialog.getWindow().setContentView(view);
        //使editext可以唤起软键盘
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edittext.getText())){
                    return;
                }
                String name = edittext.getText().toString().trim();
                if(item.getName().equals(name)){
                    return;
                }
                File newFIle = new File(item.getParent() + "/" +name);
                boolean success = item.renameTo(newFIle);

                fileList.set(postioin, newFIle);
                adapter.addAll(fileList);
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
    }


    class FileItemViewHolder extends RecyclerView.ViewHolder{
        TextView fileNameTv;
        TextView fileSizeTv;
        TextView fileDateTv;
        ImageView audio_more_action;
        public FileItemViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTv = itemView.findViewById(R.id.file_name);
            fileSizeTv = itemView.findViewById(R.id.file_size);
            fileDateTv = itemView.findViewById(R.id.file_date);
            audio_more_action = itemView.findViewById(R.id.audio_more_action);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(getActivity() != null){
                getActivity().unregisterReceiver(broadcastReceiver);
            }
        }catch (Exception e){
        }
    }

    class FileComparator implements Comparator<File> {
        @Override
        public int compare(File file1, File file2) {
            if(file1.lastModified() > file2.lastModified())
            {
                return -1;
            }else
            {
                return 1;
            }
        }
    }

    void downloadCheck(long id, File audioFile){
        if(getActivity() == null || getActivity().isFinishing()){
            return;
        }
        DownloadManager.Query query=new DownloadManager.Query();
        DownloadManager dm= (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        query.setFilterById(id);

        Cursor c = dm.query(query);
        if (c!=null){
            Log.d("muxvideo","downloadCheck start");
            try {
                if (c.moveToFirst()){
                    //获取文件下载路径
                    int fileUriIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    String fileUri = c.getString(fileUriIdx);
                    String filename = null;
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        if (fileUri != null) {
                            filename = Uri.parse(fileUri).getPath();
                        }
                    } else {
                        //Android 7.0以上的方式：请求获取写入权限，这一步报错
                        //过时的方式：DownloadManager.COLUMN_LOCAL_FILENAME
                        int fileNameIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                        filename = c.getString(fileNameIdx);
                    }

                    int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));

//                    int reaonIndex = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON));
//
//                    String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
//
//                    int toatalsize = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//
//                    Log.d("muxvideo","downloadCheck:"+status  +"  reaonIndex:"+reaonIndex +"  " +toatalsize);

//                    if (status==DownloadManager.STATUS_SUCCESSFUL){
//                        if(filename != null && filename.toLowerCase().endsWith(".mp4")){
//                            if(audioFile.exists()){//need merge
//                                String outputFile = filename.replace(".mp4","") +"1" + ".mp4";
//                                Log.d("muxvideo","onReceive: start merge outputfile:"+outputFile);
//                                boolean result = Commons.muxVideoAndAudio(getActivity(), filename, audioFile.getAbsolutePath(), outputFile);
//                            }
//                        }
//                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d("muxvideo","Exception:"+e.getMessage());
                return;
            }finally {
                c.close();
            }
        }
    }

    void mergeAudio(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if(getActivity() == null || getActivity().isFinishing()){
//                    return;
//                }
//                File dir = DownloadFile.getDownloadDir();
//                if(dir == null || dir.listFiles() == null){
//                    return;
//                }
//                List<File> tempList = new ArrayList<>();
//                for(File file : dir.listFiles()){
//                    if(file.getName().endsWith(Commons.VOICE_TEMP)){
//                        tempList.add(file);
//                    }
//                }
//                if(tempList.size() <= 0){
//                    return;
//                }
//                showMergeProgress();
//                for(File file : tempList){
//                    int lastindex = file.getName().lastIndexOf('.');
//                    if(lastindex < 0){
//                        return;
//                    }
//                    String na = file.getName().substring(0, file.getName().lastIndexOf('.'));
//                    long id = UIConfigManager.getDownloadId(na);
//                    if(id != 0){
//                        downloadCheck(id, file);
//                    }
//
//                }
//                hideProgress();
//            }
//        }).start();
    }

    ProgressDialog progressDialog;
    private void showMergeProgress(){
        if(getActivity() == null || getActivity().isFinishing()){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null && progressDialog.isShowing()){
                    return;
                }
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Processing...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });

    }

    private void hideProgress(){
        if(getActivity() == null || getActivity().isFinishing()){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }

    BroadcastReceiver broadcastReceiver;
    private void listener() {
        try {
            // 注册广播监听系统的下载完成事件。
            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("muxvideo","onReceive ACTION_DOWNLOAD_COMPLETE");
                    mergeAudio();
                }
            };
            if(getActivity() != null){
                getActivity().registerReceiver(broadcastReceiver, intentFilter);
            }
        }catch (Exception e){
        }

    }

}
