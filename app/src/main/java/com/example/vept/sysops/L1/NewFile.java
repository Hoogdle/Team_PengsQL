package com.example.vept.sysops.L1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.example.vept.sysops.L1.FileExplorer;
import androidx.activity.result.ActivityResultLauncher;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class NewFile {
    private Context context;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private Uri selectedFileUri; // Uri 타입으로 변경
    private SysOpsDB sysOpsDB;

    public NewFile(Context context, ActivityResultLauncher<Intent> filePickerLauncher) {
        this.sysOpsDB = new SysOpsDB(context); // SysOpsDB 인스턴스 초기화
    }


    public static void createNewDb(Context context, String dbNameWithoutExt) {
        String fileName = dbNameWithoutExt + ".db";
        File dbDir = new File(context.getFilesDir(), "database");

        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }

        File newDbFile = new File(dbDir, fileName);

        if (newDbFile.exists()) {
            Log.e("NewFile", "같은 이름의 DB가 이미 존재합니다.");
            Toast.makeText(context, "이미 존재하는 DB 이름입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 1. DB 파일 생성
            SQLiteDatabase newDb = SQLiteDatabase.openOrCreateDatabase(newDbFile, null);
            newDb.close();

            // 2. DB 정보 등록
            SysOpsDB sysOpsDB = new SysOpsDB(context);

            String documentsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
            String originalPath = documentsPath + "/" + fileName;

            sysOpsDB.createDatabaseInfoToDB(fileName, originalPath, newDbFile.getAbsolutePath());



            Toast.makeText(context, "DB 생성 완료: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("NewFile", "DB 생성 실패", e);
            Toast.makeText(context, "DB 생성 중 오류 발생", Toast.LENGTH_SHORT).show();
        }
    }


}
