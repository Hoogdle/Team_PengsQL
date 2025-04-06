package com.example.vept.ed.L4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.platform.ComposeView;
import androidx.lifecycle.ViewModelProvider;

public class EditSqlCli extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ComposeView 생성
        ComposeView composeView = new ComposeView(this);
        setContentView(composeView);

        // ViewModel 생성
        EditSqlCliViewModel viewModel = new ViewModelProvider(this).get(EditSqlCliViewModel.class);

        // Kotlin의 정적 유틸리티 메서드 호출
        EditerComposableWrapper.setSqlCliComposableContent(composeView, viewModel);
    }
}