package com.example.vept;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.platform.ComposeView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vept.ed.L4.EditorMainActivity;
import com.example.vept.sysops.L1.FileExplorer;
import com.example.vept.sysops.L1.Permission;
import com.example.vept.sysops.L1.SysOpsDB;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnFileExplorer, btnShowDatabasesInfo;
    private FileExplorer fileExplorer;
    private SysOpsDB sysopsDB;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private RecyclerView recyclerView;
    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_main); 비활성화

        initializePermissions();        // 권한 체크
        /*

        initializeUI();                 // UI 초기화는  모두 Compose UI로 대체 → MainDesign.kt
        initializeFileExplorer();       // Compose UI에서 처리 → ViewModel 또는 remember 블록 활용
        setButtonListeners();           // Compose의 onClick으로 대체
        loadDatabaseNames();            // ViewModel에서 처리 → LiveData / StateFlow 사용
        */

        // ComposeView 생성
        ComposeView composeView = new ComposeView(this);
        setContentView(composeView);

        // ViewModel 생성
        MainDesignViewModel viewModel = new ViewModelProvider(this).get(MainDesignViewModel.class);
        viewModel.loadDatabaseNames(this);

        // Kotlin의 정적 유틸리티 메서드 호출
        ComposableWrapper.setComposableContent(composeView, viewModel);
    }



    private void initializeUI() { // UI 초기화는  모두 Compose UI로 대체 → MainDesign.kt
        //btnFileExplorer = findViewById(R.id.btnFileExplorer);
        //btnShowDatabasesInfo = findViewById(R.id.btnShowDatabasesInfo);
        //recyclerView = findViewById(R.id.recyclerViewDatabases);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void initializeFileExplorer() {
        fileExplorer = new FileExplorer(this, null);
        filePickerLauncher = FileExplorer.createFilePickerLauncher(this, fileExplorer);
        fileExplorer.setFilePickerLauncher(filePickerLauncher);
    }

    private void setButtonListeners() { // Compose의 onClick으로 대체
        //btnFileExplorer.setOnClickListener(v -> fileExplorer.openFileExplorer());
        //btnShowDatabasesInfo.setOnClickListener(v -> loadDatabaseNames());
    }


    /*
    private void loadDatabaseNames() {
        sysopsDB = new SysOpsDB(this);
        new Thread(() -> {
            List<String> databaseNames = sysopsDB.getDatabaseNames();

            runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new DatabaseAdapter(databaseNames, new DatabaseAdapter.OnItemClickListener() {
                        @Override
                        public void onEditClick(String databaseName) {
                            // 🔥 편집 버튼 클릭 시 EditorMainActivity 실행
                            Intent intent = new Intent(MainActivity.this, EditorMainActivity.class);
                            intent.putExtra(EditorMainActivity.EXTRA_DATABASE_NAME, databaseName);
                            startActivity(intent);
                        }

                        @Override
                        public void onDeleteClick(String databaseName) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("삭제 확인")
                                    .setMessage("'" + databaseName + "'을 삭제하시겠습니까?")
                                    .setPositiveButton("삭제", (dialog, which) -> {
                                        sysopsDB.deleteDatabaseInfo(databaseName);
                                        loadDatabaseNames(); // UI 갱신
                                    })
                                    .setNegativeButton("취소", null)
                                    .show();
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateData(databaseNames);
                }
            });
        }).start();
    }
    */

    private void initializePermissions() {
        Permission permissionChecker = new Permission(this);
        permissionChecker.checkStoragePermission();
    }


}
