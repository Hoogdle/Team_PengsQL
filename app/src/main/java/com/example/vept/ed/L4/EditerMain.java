package com.example.vept.ed.L4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.platform.ComposeView;
import androidx.lifecycle.ViewModelProvider;

import com.example.vept.ed.L2.EditDB;


public class EditerMain extends AppCompatActivity {

    public static final String EXTRA_DATABASE_NAME = "database_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 전달된 DB 이름 받기
        String databaseName = getIntent().getStringExtra(EXTRA_DATABASE_NAME);

        // ComposeView 단일 생성
        ComposeView composeView = new ComposeView(this);
        setContentView(composeView);

        // ViewModel들 생성
        EditerMainViewModel mainViewModel = new ViewModelProvider(this).get(EditerMainViewModel.class);
        EditSqlCliViewModel cliViewModel = new ViewModelProvider(this).get(EditSqlCliViewModel.class);
        EditTableListViewModel listViewModel = new ViewModelProvider(this).get(EditTableListViewModel.class);
        EditTableModViewModel modViewModel = new ViewModelProvider(this).get(EditTableModViewModel.class);


        // EditDB 생성 및 모든 ViewModel에 공유 (예시로 Main만 먼저 연결)
        EditDB editDB = new EditDB(getApplicationContext(), databaseName);
        mainViewModel.setDatabaseName(databaseName);
        mainViewModel.setEditDB(editDB);
        cliViewModel.setEditDB(editDB);
        listViewModel.setEditDB(editDB);
        modViewModel.setEditDB(editDB);

        // Navigation 중심으로 ComposeView 설정
        EditerComposableWrapper.setNavComposableContent(
                composeView,
                mainViewModel,
                cliViewModel,
                listViewModel,
                modViewModel
        );
    }
}
