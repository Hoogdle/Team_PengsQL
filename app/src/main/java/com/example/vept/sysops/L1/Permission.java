package com.example.vept.sysops.L1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;


public class Permission {
//    private static final String TAG = "Permission";
//    private Activity activity;
//    private static final int STORAGE_PERMISSION_CODE = 101;
//    public Permission(Activity activity) {
//        this.activity = activity;
//    }
//
//    public void checkStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            //모든 파일에 접근할 수 있는 권한이 없다면 요청
//            if (!Environment.isExternalStorageManager()) {
//                try {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                    intent.addCategory("android.intent.category.DEFAULT");
//                    intent.setData(Uri.parse(String.format("package:%s", activity.getApplicationContext().getPackageName())));
//                    activity.startActivityForResult(intent, 100);
//                } catch (Exception e) {
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    activity.startActivityForResult(intent, 100);
//                }
//            } else {
//                Log.d("Permmsion", "이미 모든 파일 접근 권한이 있습니다.");
//            }
//        }
//
//    }
//
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "Storage permission granted");
//                // 권한이 부여된 경우 수행할 작업
//            } else {
//                Log.d(TAG, "Storage permission denied");
//                // 권한이 거부된 경우 수행할 작업
//            }
//        }
//    }
}