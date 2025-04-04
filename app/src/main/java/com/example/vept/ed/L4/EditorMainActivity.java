package com.example.vept.ed.L4;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vept.R;

public class EditorMainActivity extends AppCompatActivity {
    public static final String EXTRA_DATABASE_NAME = "database_name";

    private TextView tvDatabaseName;

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
    }
}
