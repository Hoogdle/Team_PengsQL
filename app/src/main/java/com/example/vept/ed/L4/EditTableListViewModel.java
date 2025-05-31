package com.example.vept.ed.L4;

import androidx.lifecycle.ViewModel;
import com.example.vept.ed.L2.EditDB;
import java.util.List;


public class EditTableListViewModel  extends ViewModel {

    private EditDB editDB;
    private String itemName;
    private String itemType;


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


    public List<String> getFieldNamesNow(String tableName) {
        return editDB.getFieldNamesForTable(tableName);
    }

    public void updateRowBySnapshot( List<String> row, List<String> fields, String column, String newValue) {
        if (editDB != null && itemName != null) {
            editDB.updateRowBySnapshot(itemName, fields, row, column, newValue);
        }
    }

    // 필터링된 데이터의 특정 페이지를 가져오는 메서드
    public List<List<String>> getFilteredTablePageDataNow(String tableName, int page, int itemsPerPage, List<String> filters) {
        return editDB.getFilteredTablePageData(tableName, page - 1, itemsPerPage, filters); // page - 1로 0부터 시작하게 처리
    }
    



}
