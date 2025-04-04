package com.example.vept.ed.L4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vept.MainActivity;
import com.example.vept.R;
import com.example.vept.ed.L2.EditDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditorMainActivity extends AppCompatActivity {
    public static final String EXTRA_DATABASE_NAME = "database_name";

    private TextView tvDatabaseName;
    private EditDB db;

    private String STable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_main);

        tvDatabaseName = findViewById(R.id.tvDatabaseName);

        // 선택된 데이터베이스 이름 가져오기
        String databaseName = getIntent().getStringExtra(EXTRA_DATABASE_NAME);
        if (databaseName != null) {
            tvDatabaseName.setText(databaseName);
        }
        OpenDB(databaseName);
        initializeExpandableListView();
        InitBtns();
    }

    private void OpenDB(String DBName) {
        db = new EditDB(this,DBName);
    }

    private void initializeExpandableListView() {
        List<String> names = db.getTableNames();
        ArrayList<HashMap<String, String>> groupList = new ArrayList<>();

        //그룹 정의
        HashMap<String,String> GroupTable = new HashMap<>();
        GroupTable.put("group","Tables");

        groupList.add(GroupTable);

        //자식 정의
        ArrayList<ArrayList<HashMap<String, String>>> ChildList = new ArrayList<>();

        ArrayList<HashMap<String, String>> TableList = new ArrayList<>();

        for(String name : names) {
            HashMap<String, String> tmp = new HashMap<>();
            tmp.put("group","Tables");
            tmp.put("name",name);
            TableList.add(tmp);
        }

        ChildList.add(TableList);

        EditorChildView adapter = new EditorChildView(this,groupList,
                android.R.layout.simple_expandable_list_item_1,
                new String[] {"group"}, new int[] {android.R.id.text1},
                ChildList, android.R.layout.simple_expandable_list_item_2,
                new String[] {"name","group"}, new int[] {android.R.id.text1,android.R.id.text2});

        ExpandableListView ExDBList = (ExpandableListView) findViewById(R.id.expandableListView);

        ExDBList.setAdapter(adapter);
        ExDBList.setOnChildClickListener(
                new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        adapter.setSelectedItem(groupPosition,childPosition);
                        STable = adapter.getSelectedItemName();
                        return false;
                    }
                }
        );
    }

    private void InitBtns() {
        Button ShowField = (Button) findViewById(R.id.btnShowField);

        ShowField.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(STable != null) {
                            Intent intent = new Intent(EditorMainActivity.this, EditorShowListActivity.class);
                            intent.putExtra(EditorShowListActivity.SELECTED_TABLE_NAME, STable);
                            intent.putExtra(EditorShowListActivity.EXTRA_DATABASE_NAME, getIntent().getStringExtra(EXTRA_DATABASE_NAME));
                            startActivity(intent);
                        } else {
                            Log.e("Error","Table is Not selected");
                        }
                    }
                }
        );
    }
}
