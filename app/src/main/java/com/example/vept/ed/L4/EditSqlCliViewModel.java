package com.example.vept.ed.L4;

import androidx.lifecycle.ViewModel;

import com.example.vept.ed.L2.EditDB;

import java.util.List;
import java.util.Map;

public class EditSqlCliViewModel  extends ViewModel {

    private EditDB editDB;
    private String databaseName;
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
}
