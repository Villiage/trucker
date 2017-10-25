package com.fxlc.trucker.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.util.UriUtil;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by cyd on 2017/10/13.
 */

public class MDownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            long reference = MyApplication.getSp().getLong("update_reference",-1);
            if (downloadId == reference){
                MyApplication.getSp().edit().remove("update_reference").commit();
                DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                Uri uri = manager.getUriForDownloadedFile(downloadId);
                if (uri == null) return;
                Log.d("cyd", uri.toString());

                String path = UriUtil.getRealFilePath(context, uri);
                Log.d("cyd", path);
                File apkFile = new File(path);
                String sharePath = context.getExternalFilesDir("down") + "/" + context.getPackageName() + ".apk";
                Log.d("cyd", sharePath);
                File destFile = new File(sharePath);
                apkFile.renameTo(destFile);
                install(context, destFile);
                manager.remove(downloadId);
            }

        }
    }


    private void install(Context context, File apkFile) {

        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installApkIntent.setDataAndType(FileProvider.getUriForFile(context, "com.fxlc.trucker.fileprovider", apkFile), "application/vnd.android.package-archive");

        } else {
            installApkIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(installApkIntent);
    }
}
