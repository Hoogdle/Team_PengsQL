package com.example.vept.ed.L4;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.vept.ed.L2.EditDB;
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

    // 특정 아이템을 조회하는 함수
    public void viewItem(String name, String type) {
        // 실제 데이터 조회 로직
        // 예시: 단순히 로그 출력이나 데이터를 조회해서 반환하는 코드
        System.out.println("Viewing item: " + name + " of type " + type);
        // 이 부분에서 필요한 로직 추가 (예: DB 조회)
    }

    public void editItem(String name, String type) {
        // 실제 수정 로직
        // 예시: 단순히 로그 출력
        System.out.println("Editing item: " + name + " of type " + type);
        // 이 부분에서 필요한 수정 로직 추가 (예: DB 수정)
    }

    public void deleteItem(String name, String type) {
        Log.d("삭제기능", "삭제 삭제 삭제");
        Log.d("삭제기능", "Deleting item: " + name + " of type " + type);
        switch (type) {
            case "테이블":
                //deleteTable(name);
                break;
            case "뷰":
                //deleteView(name);
                break;
            case "인덱스":
                //deleteIndex(name);
                break;
            case "트리거":
                //deleteTrigger(name);
                break;
            default:
                Log.w("삭제기능", "Unknown type: " + type);
                break;
        }

    }
}
