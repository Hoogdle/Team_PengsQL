package com.example.myapplication;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.compose.ui.platform.ComposeView;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ComposeView 생성
        ComposeView composeView = new ComposeView(this);
        setContentView(composeView);

        // ViewModel 생성
        TmpDesignViewModel viewModel = new ViewModelProvider(this).get(TmpDesignViewModel.class);

        // Kotlin의 정적 유틸리티 메서드 호출
        ComposableWrapper.setComposableContent(composeView, viewModel);
    }
}
