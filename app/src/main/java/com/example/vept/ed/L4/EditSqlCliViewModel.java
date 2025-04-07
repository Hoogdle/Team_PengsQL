package com.example.vept.ed.L4;

import androidx.lifecycle.ViewModel;

import com.example.vept.ed.L2.EditDB;

import java.util.List;
import java.util.Map;

public class EditSqlCliViewModel  extends ViewModel {

    private EditDB editDB;
    private String databaseName;


    public void setEditDB(EditDB db) {
        this.editDB = db;
    }
}
