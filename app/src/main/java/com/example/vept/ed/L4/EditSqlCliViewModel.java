package com.example.vept.ed.L4;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vept.ed.L2.EditDB;

public class EditSqlCliViewModel extends ViewModel {

    private EditDB editDB;
    private String databaseName;
    private String itemName;
    private String itemType;

    private final MutableLiveData<String> cliResult = new MutableLiveData<>();

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

    public void executeSQL(String sqlText) {
        if (editDB != null && sqlText != null && !sqlText.trim().isEmpty()) {
            String result = editDB.executeSQL(sqlText);
            cliResult.postValue(result);
        } else {
            cliResult.postValue("SQL 실행 실패: 유효하지 않은 입력 또는 DB 연결되지 않음.");
        }
    }

    public void cancelExecution() {
        cliResult.postValue("실행 중단");
    }

    public LiveData<String> getCliResult() {
        return cliResult;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (editDB != null) {
            editDB.close();
        }
    }
}