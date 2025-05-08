package com.example.vept.ed.L4;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vept.ed.L2.EditDB;

import java.util.List;
import java.util.concurrent.Executors;


public class EditTableListViewModel  extends ViewModel {

    private EditDB editDB;
    private String databaseName;
    private String itemName;
    private String itemType;
    private MutableLiveData<List<List<String>>> tablePageData = new MutableLiveData<>();
    private MutableLiveData<Integer> rowCount = new MutableLiveData<>();



    public void setEditDB(EditDB db) {
        this.editDB = db;
    }

    public void setItemInfo(String name, String type) {
        this.itemName = name;
        this.itemType = type;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public LiveData<List<List<String>>> getTablePageData(String tablename, int page, int itemsPerPage) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<List<String>> data = editDB.getTablePageDataRaw(tablename, page-1, itemsPerPage);
            tablePageData.postValue(data);
        });
        return tablePageData;
    }

    public List<List<String>> getTablePageDataNow(String tableName, int page, int itemsPerPage) {
        return editDB.getTablePageData(tableName, page-1, itemsPerPage);
    }

    public LiveData<Integer> getRowCount(String itemName) {
        Executors.newSingleThreadExecutor().execute(() -> {
            int count = editDB.getRowCount(itemName);
            rowCount.postValue(count);
        });
        return rowCount;
    }

    public LiveData<List<String>> getFieldNames(String itemName) {
        MutableLiveData<List<String>> result = new MutableLiveData<>();
        result.setValue(editDB.getFieldNamesForTable(itemName));
        return result;
    }

    public void updateRowBySnapshot( List<String> row, List<String> fields, String column, String newValue) {
        if (editDB != null && itemName != null) {
            editDB.updateRowBySnapshot(itemName, fields, row, column, newValue);
        }
    }
}
