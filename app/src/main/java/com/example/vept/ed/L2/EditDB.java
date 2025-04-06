package com.example.vept.ed.L2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vept.R;
import com.example.vept.sysops.L1.SysOpsDB;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class EditDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_FOLDER = "database/";
    private SQLiteDatabase db;
    private List<List<String>> DataList;
    private List<String> HeaderList;
    private int PageSize;
    private int Page;
    private final Context mContext;
    private String dbPath;
    private String databaseName;

    public EditDB(Context mContext, String DBName) {
        super(null, DBName, null, DATABASE_VERSION); // null context
        this.mContext = mContext;
        this.databaseName = DBName;
        this.dbPath = getDBPath(databaseName); // 여기서 mContext 사용 가능
        openDatabase();
    }
    @SuppressLint("Range")
    private String getDBPath(String dbName) { // 경로
        SQLiteDatabase sysopsDB = new SysOpsDB(mContext).getReadableDatabase();
        String query = "SELECT interior_path FROM databases_info WHERE database_name = ?";
        Cursor cursor = null;
        String dbPath = null;
        try {
            // 1차 시도: 확장자 있는 그대로
            cursor = sysopsDB.rawQuery(query, new String[]{dbName});
            if (cursor.moveToFirst()) {
                dbPath = cursor.getString(cursor.getColumnIndex("interior_path"));
                Log.e("EditDB", "DB 경로 성공적으로 조회됨: " + dbPath);
            } else {
                Log.e("EditDB", "getDBPath 실패: 해당 이름의 DB 없음");
            }
        } catch (Exception e) {
            Log.e("EditDB", "쿼리 실패", e);
        } finally {
            if (cursor != null) cursor.close();
            sysopsDB.close();
        }
        return dbPath;
    }

    private void openDatabase() {   // 열기만 하도록 수정함
        try {
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            Log.e("EditDB", "Error opening database", e);
        }
    }

    public List<String> getTableName() {
        List<String> tableNames = new ArrayList<>();

        try {
            Cursor tableCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'android_%';", null);
            if (tableCursor.moveToFirst()) {
                do {
                    String tableName = tableCursor.getString(0);
                    tableNames.add(tableName);
                } while (tableCursor.moveToNext());
            }
            tableCursor.close();
        } catch (Exception e) {
            Log.e("EditDB", "getTableNames 실패", e);
        }

        return tableNames;
    }

    public List<String> getFieldNamesForTable(String tableName) {
        List<String> columnList = new ArrayList<>();

        try {
            Cursor columnCursor = db.rawQuery("PRAGMA table_info('" + tableName + "')", null);
            if (columnCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range")
                    String columnName = columnCursor.getString(columnCursor.getColumnIndex("name"));
                    columnList.add(columnName);
                } while (columnCursor.moveToNext());
            }
            columnCursor.close();
        } catch (Exception e) {
            Log.e("EditDB", "getFieldNamesForTable 실패", e);
        }

        return columnList;
    }

    public Map<String, List<String>> getTableAndFieldName() {
        if (db == null || !db.isOpen()) {
            Log.e("EditDB", "Database is not open.");
            return Collections.emptyMap();
        }
        Map<String, List<String>> result = new LinkedHashMap<>();
        List<String> tableNames = getTableName();
        for (String tableName : tableNames) {
            List<String> fields = getFieldNamesForTable(tableName);
            result.put(tableName, fields);
            Log.d("EditDB", "Table: " + tableName + " => Fields: " + fields);
        }
        return result;
    }

    public Map<String, List<String>> getIndexInfo() {
        Map<String, List<String>> indexMap = new LinkedHashMap<>();

        try {
            Cursor indexCursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type = 'index' AND name NOT LIKE 'sqlite_%';",
                    null
            );

            if (indexCursor.moveToFirst()) {
                do {
                    String indexName = indexCursor.getString(0);
                    List<String> columnNames = new ArrayList<>();

                    // 각 인덱스에 대한 column 정보 가져오기
                    Cursor infoCursor = db.rawQuery("PRAGMA index_info('" + indexName + "');", null);
                    if (infoCursor.moveToFirst()) {
                        do {
                            @SuppressLint("Range")
                            String colName = infoCursor.getString(infoCursor.getColumnIndex("name"));
                            columnNames.add(colName);
                        } while (infoCursor.moveToNext());
                    }
                    infoCursor.close();

                    indexMap.put(indexName, columnNames);
                } while (indexCursor.moveToNext());
            }

            indexCursor.close();
        } catch (Exception e) {
            Log.e("EditDB", "getIndexInfo 실패", e);
        }

        return indexMap;
    }

    public Map<String, List<String>> getViewInfo() {
        Map<String, List<String>> viewMap = new LinkedHashMap<>();

        try {
            // 뷰 이름 조회
            Cursor viewCursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='view' AND name NOT LIKE 'android_%';",
                    null
            );

            if (viewCursor.moveToFirst()) {
                do {
                    String viewName = viewCursor.getString(0);
                    List<String> columnList = new ArrayList<>();

                    // 각 뷰의 컬럼 정보 조회
                    Cursor colCursor = db.rawQuery("PRAGMA table_info('" + viewName + "')", null);
                    if (colCursor.moveToFirst()) {
                        do {
                            @SuppressLint("Range")
                            String colName = colCursor.getString(colCursor.getColumnIndex("name"));
                            columnList.add(colName);
                        } while (colCursor.moveToNext());
                    }
                    colCursor.close();

                    viewMap.put(viewName, columnList);
                } while (viewCursor.moveToNext());
            }

            viewCursor.close();
        } catch (Exception e) {
            Log.e("EditDB", "getViewInfo 실패", e);
        }

        return viewMap;
    }

    public List<String> getTriggerNames() {
        List<String> triggerNames = new ArrayList<>();

        try {
            Cursor triggerCursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='trigger' AND name NOT LIKE 'sqlite_%';",
                    null
            );

            if (triggerCursor.moveToFirst()) {
                do {
                    String triggerName = triggerCursor.getString(0);
                    triggerNames.add(triggerName);
                } while (triggerCursor.moveToNext());
            }

            triggerCursor.close();
        } catch (Exception e) {
            Log.e("EditDB", "getTriggerNames 실패", e);
        }

        return triggerNames;
    }





    public List<String> getTableNames() { // 레거시 코드 종속성 문제 해결될때까지 유지중
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

    public List<List<String>> getTableDataByPage(String TableName, int PageNum) {
        DataList = new ArrayList<>();
        getTableHeader(TableName);
        getTableCellByPage(TableName, PageNum - 1);
        return DataList;
    }

    public int getPageCount(String TableName) {
        String Query = "SELECT count(*) FROM " + TableName + ";";
        Cursor cursor = db.rawQuery(Query, null);
        int elements = 0;
        int count = 0;

        if (cursor.moveToFirst()) {
            do {
                elements = Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        count = (int)((elements - 1) / PageSize);

        return count;
    }

    public void setPageSize(int ps) {
        PageSize = ps;
    }

    public int getPageSize() {
        return PageSize;
    }

    private void getTableCell(String TableName, int start, int length) {
        String Query = "SELECT * FROM " + TableName + " LIMIT " + length +  " OFFSET " + start + ";";
        Cursor cursor = db.rawQuery(Query, null);

        if (cursor.moveToFirst()) {
            do {
                int colnum = cursor.getColumnCount();
                List<String> DataRow = new ArrayList<>();

                for(int i = 0;i < colnum;i++) {
                    DataRow.add(cursor.getString(i));
                }

                DataList.add(DataRow);
            } while (cursor.moveToNext());
        }
    }

    private void getTableCellByPage(String TableName, int PageNum) {
        String Query = "SELECT * FROM " + TableName + " LIMIT " + PageSize +  " OFFSET " + PageNum * PageSize + ";";
        Cursor cursor = db.rawQuery(Query, null);

        if (cursor.moveToFirst()) {
            do {
                int colnum = cursor.getColumnCount();
                List<String> DataRow = new ArrayList<>();

                for(int i = 0;i < colnum;i++) {
                    DataRow.add(cursor.getString(i));
                }

                DataList.add(DataRow);
            } while (cursor.moveToNext());
        }
    }

    private void getTableHeader(String TableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM pragma_table_info('" + TableName + "');", null);

        List<String> tmpList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                tmpList.add(cursor.getString(0));
            } while (cursor.moveToNext());
            DataList.add(tmpList);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades
    }
}
