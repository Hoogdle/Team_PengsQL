package com.example.vept.pl.L4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.platform.ComposeView;
import androidx.lifecycle.ViewModelProvider;

public class PlannerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ComposeView 생성
        ComposeView composeView = new ComposeView(this);
        setContentView(composeView);

        // ViewModel 생성
        PlannerDiagramViewModel viewModel = new ViewModelProvider(this).get(PlannerDiagramViewModel.class);

        // Kotlin의 정적 유틸리티 메서드 호출
        PlannerComposableWrapper.setComposableContent(composeView, viewModel);
    }
}