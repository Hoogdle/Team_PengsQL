package com.example.vept.ed.L4;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vept.ed.L2.EditDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditerMainViewModel extends ViewModel {

    private EditDB editDB;
    private String databaseName;

    private final MutableLiveData<Map<String, List<String>>> indexInfo = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<String>>> tableFieldMap = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<String>>> viewInfo = new MutableLiveData<>();
    private final MutableLiveData<List<String>> triggerList = new MutableLiveData<>();

    public void setEditDB(EditDB db) {
        this.editDB = db;
        this.tableFieldMap.setValue(db.getTableAndFieldName());
        this.indexInfo.setValue(db.getIndexInfo());
        Map<String, List<String>> result = editDB.getViewInfo();
        viewInfo.setValue(result);
        List<String> result1 = editDB.getTriggerNames();
        triggerList.setValue(result1);
    }
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    public String getDatabaseName() {
        return databaseName;
    }

    public LiveData<Map<String, List<String>>> getTableFieldMap() {
        return tableFieldMap;
    }
    public LiveData<Map<String, List<String>>> getViewInfo() {
        return viewInfo;
    }
    public LiveData<Map<String, List<String>>> getIndexInfo() {
        return indexInfo;
    }
    public LiveData<List<String>> getTriggerList() {
        return triggerList;
    }


    public List<String> getFieldNames(String tableName) {
        Map<String, List<String>> map = tableFieldMap.getValue();
        if (map != null) {
            return map.getOrDefault(tableName, Collections.emptyList());
        } else {
            return Collections.emptyList();
        }
    }

    public EditDB getEditDB() {
        return editDB;
    }
}
