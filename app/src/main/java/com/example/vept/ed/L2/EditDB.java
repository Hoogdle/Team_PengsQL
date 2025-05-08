package com.example.vept.ed.L2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.vept.sysops.L1.SysOpsDB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EditDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
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


    public void deleteTable(String tableName) {
        try {
            db.execSQL("DROP TABLE IF EXISTS `" + tableName + "`;");
            Log.d("EditDB", "테이블 삭제됨: " + tableName);
        } catch (Exception e) {
            Log.e("EditDB", "테이블 삭제 실패: " + tableName, e);
        }
    }

    public void deleteView(String viewName) {
        try {
            db.execSQL("DROP VIEW IF EXISTS `" + viewName + "`;");
            Log.d("EditDB", "뷰 삭제됨: " + viewName);
        } catch (Exception e) {
            Log.e("EditDB", "뷰 삭제 실패: " + viewName, e);
        }
    }

    public void deleteIndex(String indexName) {
        try {
            db.execSQL("DROP INDEX IF EXISTS `" + indexName + "`;");
            Log.d("EditDB", "인덱스 삭제됨: " + indexName);
        } catch (Exception e) {
            Log.e("EditDB", "인덱스 삭제 실패: " + indexName, e);
        }
    }

    public void deleteTrigger(String triggerName) {
        try {
            db.execSQL("DROP TRIGGER IF EXISTS `" + triggerName + "`;");
            Log.d("EditDB", "트리거 삭제됨: " + triggerName);
        } catch (Exception e) {
            Log.e("EditDB", "트리거 삭제 실패: " + triggerName, e);
        }
    }

    public List<List<String>> getTablePageDataRaw(String tableName, int page, int pageSize) {
        List<List<String>> result = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        try {
            String query = "SELECT * FROM `" + tableName + "` LIMIT " + pageSize + " OFFSET " + offset;
            Cursor cursor = db.rawQuery(query, null);

            int columnCount = cursor.getColumnCount();

            while (cursor.moveToNext()) {
                List<String> row = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    row.add(cursor.getString(i));
                }
                result.add(row);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("EditDB", "getTablePageDataRaw 실패", e);
        }

        return result;
    }

    // EditDB.java
//    public List<List<String>> getTablePageData(String tableName, int page, int itemsPerPage) {
//        List<List<String>> result = new ArrayList<>();
//        try (Cursor cursor = db.rawQuery("SELECT * FROM `" + tableName + "` LIMIT ? OFFSET ?", new String[]{
//                String.valueOf(itemsPerPage),
//                String.valueOf(page * itemsPerPage)
//        })) {
//            int columnCount = cursor.getColumnCount();
//            while (cursor.moveToNext()) {
//                List<String> row = new ArrayList<>();
//                for (int i = 0; i < columnCount; i++) {
//                    row.add(cursor.getString(i));
//                }
//                result.add(row);
//            }
//        } catch (Exception e) {
//            Log.e("EditDB", "페이지 데이터 가져오기 실패", e);
//        }
//        return result;
//    }

    public List<List<String>> getTablePageData(String tableName, int page, int itemsPerPage) {
        List<List<String>> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(
                "SELECT * FROM `" + tableName + "` LIMIT ? OFFSET ?",
                new String[]{String.valueOf(itemsPerPage), String.valueOf(page * itemsPerPage)}
        )) {
            int columnCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                List<String> row = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    switch (cursor.getType(i)) {
                        case Cursor.FIELD_TYPE_NULL:
                            row.add("NULL");
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            row.add(String.valueOf(cursor.getLong(i)));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            row.add(String.valueOf(cursor.getDouble(i)));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            row.add(cursor.getString(i));
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            byte[] blob = cursor.getBlob(i);
                            row.add("[BLOB: " + (blob != null ? blob.length : 0) + " bytes]");
                            break;
                        default:
                            row.add("[UNKNOWN TYPE]");
                            break;
                    }
                }
                result.add(row);
            }
        } catch (Exception e) {
            Log.e("EditDB", "페이지 데이터 가져오기 실패", e);
        }
        return result;
    }


    public int getRowCount(String tableName) {
        int count = 0;
        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM `" + tableName + "`", null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("EditDB", "getRowCountRaw 실패", e);
        }
        return count;
    }

    public void updateRowBySnapshot(String tableName, List<String> fields, List<String> oldRow, String columnToUpdate, String newValue) {
        if (fields.size() != oldRow.size()) {
            Log.e("EditDB", "필드 수와 데이터 수 불일치");
            return;
        }

        try {
            // SET 구문
            StringBuilder setClause = new StringBuilder();
            setClause.append(columnToUpdate).append(" = ?");

            // WHERE 구문 (스냅샷과 동일한 행을 찾기 위해 전체 필드를 조건으로 사용)
            StringBuilder whereClause = new StringBuilder();
            List<String> whereArgs = new ArrayList<>();
            for (int i = 0; i < fields.size(); i++) {
                if (i > 0) whereClause.append(" AND ");
                whereClause.append(fields.get(i)).append(" = ?");
                whereArgs.add(oldRow.get(i));
            }

            // 최종 쿼리 실행
            String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;
            List<String> allArgs = new ArrayList<>();
            allArgs.add(newValue); // SET 절
            allArgs.addAll(whereArgs); // WHERE 절
            db.execSQL(sql, allArgs.toArray());

            Log.d("EditDB", "수정 시도: 테이블=" + tableName + " 필드=" + columnToUpdate + " 새값=" + newValue);

            Log.d("EditDB", "WHERE 조건: ");
            for (int i = 0; i < fields.size(); i++) {
                Log.d("EditDB", fields.get(i) + " = " + oldRow.get(i));
            }

            Log.d("EditDB", "최종 SQL = " + sql);
            Log.d("EditDB", "ARGS = " + allArgs.toString());

        } catch (Exception e) {
            Log.e("EditDB", "updateRowBySnapshot 실패", e);
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
