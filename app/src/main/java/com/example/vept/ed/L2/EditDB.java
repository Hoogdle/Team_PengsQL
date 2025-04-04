package com.example.vept.ed.L2;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class EditDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_FOLDER = "database/";
    private final Context mContext;
    private final String dbPath;
    private SQLiteDatabase db;
    private List<List<String>> DataList;
    private List<String> HeaderList;
    private int PageSize;
    private int Page;

    public EditDB(Context context, String DBName) {
        super(context, DBName, null, DATABASE_VERSION);
        this.PageSize = 30;
        this.Page = 0;
        this.mContext = context;
        this.dbPath = context.getFilesDir() + "/" + DATABASE_FOLDER + DBName;
        openDatabase();
    }

    private void openDatabase() {
        try {
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            DataList = new ArrayList<>();
            HeaderList = new ArrayList<>();
        } catch (Exception e) {
            Log.e("SysOpsDB", "Error opening database", e);
        }
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

    public List<List<String>> getTableData(String TableName) {
        getTableHeader(TableName);
        getTableCell(TableName, 0, -1);
        return DataList;
    }

    public List<List<String>> getTableData(String TableName, int start, int length) {
        DataList = new ArrayList<>();
        getTableHeader(TableName);
        getTableCell(TableName, start, length);
        return DataList;
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
