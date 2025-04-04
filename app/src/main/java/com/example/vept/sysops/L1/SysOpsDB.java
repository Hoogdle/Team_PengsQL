package com.example.vept.sysops.L1;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SysOpsDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sysops.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_FOLDER = "databases/sysops/";
    private static final String PREFS_NAME = "SysOpsDBPrefs";
    private static final String KEY_DB_INITIALIZED = "database_initialized";

    private final Context mContext;
    private final String dbPath;
    private SQLiteDatabase db;

    public SysOpsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        this.dbPath = context.getFilesDir() + "/" + DATABASE_FOLDER + DATABASE_NAME;
        initializeDatabase();
    }

    private void initializeDatabase() {
        if (!isDatabaseInitialized()) {
            copyDatabaseFromAssets();
            setDatabaseInitialized();
        }
        openDatabase();
    }

    private boolean checkDatabaseExists() {
        File dbFile = new File(dbPath);
        return dbFile.exists();
    }

    private void copyDatabaseFromAssets() {
        try {
            InputStream input = mContext.getAssets().open("databases/sysops/" + DATABASE_NAME);
            File outFile = new File(dbPath);
            outFile.getParentFile().mkdirs();
            OutputStream output = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
            Log.d("SysOpsDB", "Database copied successfully.");
        } catch (IOException e) {
            Log.e("SysOpsDB", "Error copying database", e);
        }
    }

    private void openDatabase() {
        try {
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            Log.e("SysOpsDB", "Error opening database", e);
        }
    }

    public List<String> getDatabaseNames() {
        List<String> databaseNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();  // 읽기 모드 DB

        Cursor cursor = db.rawQuery("SELECT database_name FROM databases_info", null);

        if (cursor.moveToFirst()) {
            do {
                databaseNames.add(cursor.getString(0));  // 첫 번째 컬럼 (database_name) 가져오기
            } while (cursor.moveToNext());
        }

        cursor.close();
        return databaseNames;
    }

    public void deleteDatabaseInfo(String databaseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("databases_info", "database_name = ?", new String[]{databaseName});
    }



    public List<String> getDatabasesInfo() {
        List<String> databasesInfo = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM databases_info", null);
        if (cursor.moveToFirst()) {
            do {
                String databaseInfo = "ID: " + cursor.getInt(0) + ", Name: " + cursor.getString(1)
                        + ", Path: " + cursor.getString(2) + ", Interior Path: " + cursor.getString(3);
                databasesInfo.add(databaseInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return databasesInfo;
    }


    public List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null);
        if (cursor.moveToFirst()) {
            do {
                tableNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tableNames;
    }



    public boolean isDatabaseInitialized() {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DB_INITIALIZED, false);
    }

    public void setDatabaseInitialized() {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DB_INITIALIZED, true).apply();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // databases_info 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS databases_info (" +
                "database_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "database_name TEXT NOT NULL UNIQUE, " +
                "original_path TEXT NOT NULL, " +
                "interior_path TEXT NOT NULL, " +
                "path_conn_check TEXT NOT NULL DEFAULT 'true');");

        // diagram 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS diagram (" +
                "diagram_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "diagram_name TEXT NOT NULL UNIQUE, " +
                "original_path TEXT NOT NULL, " +
                "interior_path TEXT NOT NULL, " +
                "path_conn_check TEXT NOT NULL DEFAULT 'true');");

        // order_hierarchy 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS order_hierarchy (" +
                "order_hierarchy_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "entity_type TEXT NOT NULL CHECK(entity_type IN ('database', 'diagram')), " +
                "entity_id INTEGER NOT NULL, " +
                "order_priority INTEGER DEFAULT 0, " +
                "FOREIGN KEY(entity_id) REFERENCES databases_info(database_id) ON DELETE CASCADE, " +
                "FOREIGN KEY(entity_id) REFERENCES diagram(diagram_id) ON DELETE CASCADE);");

        // update_priority_after_update 트리거 생성
        db.execSQL("DROP TRIGGER IF EXISTS update_priority_after_update;");
        db.execSQL("CREATE TRIGGER update_priority_after_update " +
                "AFTER UPDATE ON order_hierarchy " +
                "FOR EACH ROW " +
                "BEGIN " +
                "    UPDATE order_hierarchy " +
                "    SET order_priority = order_priority + 1 " +
                "    WHERE order_hierarchy_id = NEW.order_hierarchy_id; " + // 특정 행만 업데이트하도록 수정
                "END;");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades
    }
}


/*public class SysOpsDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sysops.db";
    private static final int DATABASE_VERSION = 1;

    private final Context mContext;
    private SQLiteDatabase db;
    private final String customDbPath;

    public SysOpsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        this.customDbPath = getDatabasePath();
    }

    private String getDatabasePath() {
        File dbDir = new File(mContext.getFilesDir(), "databases/sysops");
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
        return dbDir.getAbsolutePath() + "/" + DATABASE_NAME;
    }

    private void copyDataBase() throws IOException {
        Log.d("DatabaseHelper", "Copying database from assets");
        InputStream mInput = mContext.getAssets().open("databases/sysops/" + DATABASE_NAME);
        String outFileName = customDbPath;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase().close(); // 빈 데이터베이스 생성 (SQLiteOpenHelper)
            try {
                copyDataBase(); // assets에서 복사
            } catch (IOException e) {
                Log.e("DatabaseHelper", "Error copying database", e);
                throw new Error("Error copying database");
            }
        } else {
            Log.d("DatabaseHelper", "Database already exists");
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(customDbPath);
        return dbFile.exists();
    }

    public void openDataBase() {
        try {
            createDataBase(); // DB가 없으면 생성 (복사)
            db = SQLiteDatabase.openDatabase(customDbPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException e) {
            Log.e("DatabaseHelper", "Unable to open database", e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성 (필요한 경우)
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 업그레이드 (필요한 경우)
    }

    // 테이블 이름 조회하여 List<String>으로 반환
    public List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        if (db == null || !db.isOpen()) {
            openDataBase(); // DB가 닫혀있으면 다시 연다.
        }
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null);
        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                tableNames.add(tableName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tableNames;
    }

    @Override
    public synchronized void close() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.d("DatabaseHelper", "Database connection closed");
        }
        super.close();
    }
}*/