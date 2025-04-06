package com.example.vept.ed.L4L;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vept.R;
import com.example.vept.ed.L2.EditDB;

import java.util.List;

public class EditorShowListActivity extends AppCompatActivity {
    public static final String EXTRA_DATABASE_NAME = "database_name";
    public static final String SELECTED_TABLE_NAME = "table_name";
    private static final String DATABASE_FOLDER = "database/";
    private EditDB db;
    private String tableName;
    private int page = 1;
    TableLayout TBL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_field);
        
        //데이터베이스 이름 가져오기
        String databaseName = getIntent().getStringExtra(EXTRA_DATABASE_NAME);
        OpenDB(databaseName);

        // 선택된 테이블 이름 가져오기
        tableName = getIntent().getStringExtra(SELECTED_TABLE_NAME);
        if(tableName != null) {
            TextView tt = (TextView)findViewById(R.id.txtTable);
            tt.setText(tableName);
        }
        TBL = (TableLayout)findViewById(R.id.TableField);
        
        MakeTable();
        SetButton();
    }

    private void OpenDB(String DBName) {
        //db = new EditDB(this,DBName);
    }

    private void MakeTable() {
        final float scale = this.getResources().getDisplayMetrics().density;

        TBL.removeAllViews();

        List<List<String>> Datas = db.getTableDataByPage(tableName, page);


        for(List<String> Row : Datas) {
            TableRow TR = new TableRow(this);
            for(String Cell : Row) {
                TextView txt = new TextView(this);
                txt.setText(Cell);
                txt.setBackground(getDrawable(R.drawable.gridback));
                txt.setWidth((int) (60 * scale + 0.5f));
                txt.setGravity(Gravity.RIGHT);
                txt.setPadding(5,5,5,5);
                txt.setHeight((int) (20 * scale + 0.5f));

                TR.addView(txt);
            }
            TBL.addView(TR);
        }

    }

    private void SetButton() {
        Button btnLeft = (Button)findViewById(R.id.btnFieldLeft);
        Button btnRight = (Button)findViewById(R.id.btnFieldRight);

        btnLeft.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(page > 1) page -= 1;
                        MakeTable();
                    }
                }
        );

        btnRight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(page < db.getPageCount(tableName) + 1) page += 1;
                        MakeTable();
                    }
                }
        );
    }

}