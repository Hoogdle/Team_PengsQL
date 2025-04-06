package com.example.vept.ed.L4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.platform.ComposeView;
import androidx.lifecycle.ViewModelProvider;




public class EditTableMod extends AppCompatActivity {

    public static final String EXTRA_DATABASE_NAME = "database_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ComposeView 생성
        ComposeView composeView = new ComposeView(this);
        setContentView(composeView);

        // ViewModel 생성
        EditTableModViewModel viewModel = new ViewModelProvider(this).get(EditTableModViewModel.class);

        // Kotlin의 정적 유틸리티 메서드 호출
        EditerComposableWrapper.setTableModComposableContent(composeView, viewModel);
    }
}