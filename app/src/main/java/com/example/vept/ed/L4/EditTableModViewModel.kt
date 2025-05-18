package com.example.vept.ed.L4;

import androidx.lifecycle.ViewModel;

import com.example.vept.ed.L2.EditDB;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlinx.coroutines.flow.MutableStateFlow;


public class EditTableModViewModel extends ViewModel {
    private EditDB editDB;
    private String databaseName;
    private String itemName;
    private String itemType;

    private final MutableStateFlow<String> tableNameFlow = new MutableStateFlow<>("");

    public void setEditDB(EditDB db) {
        this.editDB = db;
    }

    public void setItemInfo(String name, String type) {
        this.itemName = name;
        this.itemType = type;
        this.tableNameFlow.setValue(name); // 초기값 설정
    }

    public String getItemName() {
        return itemName;
    }
    public String getItemType() {
        return itemType;
    }

    public void updateTableName(String name) {
        this.tableNameFlow.setValue(name);
    }

    public void applyNewTable() {
        String newName = tableNameFlow.getValue();
        // CREATE TABLE 로직 작성
        if (editDB != null && newName != null && !newName.isEmpty()) {
            String query = "CREATE TABLE IF NOT EXISTS `" + newName + "` (id INTEGER PRIMARY KEY AUTOINCREMENT);";
            try {
                editDB.getDb().execSQL(query);
                Log.d("EditTableMod", "새 테이블 생성됨: " + newName);
            } catch (Exception e) {
                Log.e("EditTableMod", "테이블 생성 실패", e);
            }
        }
    }

    public void applyModifyTable() {
        String newName = tableNameFlow.getValue();
        if (editDB != null && itemName != null && !itemName.equals(newName)) {
            try {
                String query = "ALTER TABLE `" + itemName + "` RENAME TO `" + newName + "`;";
                editDB.getDb().execSQL(query);
                Log.d("EditTableMod", "테이블 이름 변경됨: " + itemName + " -> " + newName);
                this.itemName = newName;
            } catch (Exception e) {
                Log.e("EditTableMod", "테이블 이름 변경 실패", e);
            }
        }
    }



}





//    private List<String> fieldList = new ArrayList<>();
//    private Map<String, Boolean> fieldReferenceMap = new HashMap<>();
//    public List<String> getFieldList() {
//        return fieldList;
//    }
//
//    public void setFieldList(List<String> newFieldList) {
//        this.fieldList = new ArrayList<>(newFieldList);
//    }
//
//    public boolean hasPrimaryKey() {
//        if (editDB != null && itemName != null && !itemName.startsWith("이름 없음")) {
//            return editDB.tableHasPrimaryKey(itemName);
//        }
//        return false;
//    }
//
//
//    public void loadFieldsFromDB() {
//        if (editDB != null && itemName != null && !itemName.startsWith("이름 없음")) {
//            List<String> fields = editDB.getFieldNames(itemName);
//            if (fields != null) {
//                fieldList = new ArrayList<>(fields);
//                fieldReferenceMap.clear();
//                for (String field : fields) {
//                    fieldReferenceMap.put(field, editDB.isFieldReferenced(itemName, field));
//                }
//            }
//        }
//    }
//
//    public boolean isFieldReferenced(String fieldName) {
//        return fieldReferenceMap.getOrDefault(fieldName, false);
//    }
//
//
//    public void updateTableName(@NotNull Object it) {
//
//    }
