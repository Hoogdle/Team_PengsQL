package com.pangsql.vept.ed.L4;


import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.pangsql.vept.ed.L2.EditDB;
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


        tableFieldMap.setValue(db.getTableAndFieldName());
        indexInfo.setValue(db.getIndexInfo());
        viewInfo.setValue(db.getViewInfo());
        triggerList.setValue(db.getTriggerNames());
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
    public void viewItem(String name, String type, NavController navController) {
        if ("테이블".equals(type) || "뷰".equals(type)) {
            String route = "list?name=" + Uri.encode(name) + "&type=" + Uri.encode(type);
            navController.navigate(route);
        } else {
            Log.w("조회기능", "Unknown type: " + type);
        }
    }

    public void editItem(String name, String type, NavController navController) {
        String encodedName = Uri.encode(name);
        String encodedType = Uri.encode(type);

        switch (type) {
            case "테이블":
                navController.navigate("mod?name=" + encodedName + "&type=" + encodedType);
                break;
            case "인덱스":
            case "트리거":
                navController.navigate("sql?name=" + encodedName + "&type=" + encodedType);
                break;
            default:
                Log.w("수정기능", "Unknown type: " + type);
                break;
        }
    }

    public void deleteItem(String name, String type) {
        Log.d("삭제기능", "Deleting item: " + name + " of type " + type);
        switch (type) {
            case "테이블":
                editDB.deleteTable(name);
                this.tableFieldMap.setValue(editDB.getTableAndFieldName());
                break;
            case "뷰":
                editDB.deleteView(name);
                this.viewInfo.setValue(editDB.getViewInfo());
                break;
            case "인덱스":
                editDB.deleteIndex(name);
                this.indexInfo.setValue(editDB.getIndexInfo());
                break;
            case "트리거":
                editDB.deleteTrigger(name);
                this.triggerList.setValue(editDB.getTriggerNames());
                break;
            default:
                Log.w("삭제기능", "Unknown type: " + type);
                break;
        }
    }

    public void refreshAllData() {
        tableFieldMap.setValue(editDB.getTableAndFieldName());
        indexInfo.setValue(editDB.getIndexInfo());
        viewInfo.setValue(editDB.getViewInfo());
        triggerList.setValue(editDB.getTriggerNames());
    }
}
