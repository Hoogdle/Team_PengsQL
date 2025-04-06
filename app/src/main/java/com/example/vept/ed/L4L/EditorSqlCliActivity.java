package com.example.vept.ed.L4L;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vept.R;
import com.example.vept.ed.L2.EditDB;

public class EditorSqlCliActivity extends AppCompatActivity {
    private EditDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_cli);

        // 선택된 데이터베이스 이름 가져오기
        String databaseName = getIntent().getStringExtra(EditorMainActivity.EXTRA_DATABASE_NAME);
        OpenDB(databaseName);
    }

    private void OpenDB(String DBName) {
        //db = new EditDB(this,DBName);
    }
}
