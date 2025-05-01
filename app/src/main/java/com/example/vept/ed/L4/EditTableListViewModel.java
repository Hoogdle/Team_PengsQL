package com.example.vept.ed.L4;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vept.ed.L2.EditDB;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executors;


public class EditTableListViewModel  extends ViewModel {

    private EditDB editDB;
    private String databaseName;
    private String itemName;
    private String itemType;
    private MutableLiveData<List<List<String>>> tablePageData = new MutableLiveData<>();
    private MutableLiveData<Integer> rowCount = new MutableLiveData<>();

    private final int itemsPerPage = 50;

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

    public LiveData<List<List<String>>> getTablePageData(String itemName, int page) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Log.w("확인용", "작동중11" + itemName);
            List<List<String>> data = editDB.getTablePageDataRaw(itemName, page, 50);
            tablePageData.postValue(data);
        });
        Log.w("확인용", "작동중12" + itemName);
        return tablePageData;
    }


    public LiveData<Integer> getRowCount(String itemName) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Log.w("확인용", "작동중21" + itemName);
            int count = editDB.getRowCount(itemName);
            rowCount.postValue(count);
        });
        Log.w("확인용", "작동중22" + itemName);
        return rowCount;
    }

    public LiveData<List<String>> getFieldNames(String itemName) {
        MutableLiveData<List<String>> result = new MutableLiveData<>();
        result.setValue(editDB.getFieldNamesForTable(itemName));
        return result;
    }
}
