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

        String databaseName = getIntent().getStringExtra(EXTRA_DATABASE_NAME);


        // ComposeView 생성
        ComposeView composeView = new ComposeView(this);
        setContentView(composeView);

        // ViewModel 생성
        EditerMainViewModel viewModel = new ViewModelProvider(this).get(EditerMainViewModel.class);
        // Pass databaseName to ViewModel
        viewModel.setDatabaseName(databaseName);

        // 여기서 EditDB 생성 및 전달
        EditDB editDB = new EditDB(getApplicationContext(), databaseName);
        viewModel.setEditDB(editDB);


        // Kotlin의 정적 유틸리티 메서드 호출
        EditerComposableWrapper.setEditerComposableContent(composeView, viewModel);
    }
}