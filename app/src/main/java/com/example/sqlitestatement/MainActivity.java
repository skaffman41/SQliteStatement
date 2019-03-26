package com.example.sqlitestatement;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DB_NAME = "MyDb";
    private static final String TABLE_NAME = "MyTable";
    private SQLiteDatabase database;
    private TextView tvTime;
    private Button btnInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();
        tvTime = findViewById(R.id.tvTime);
        btnInsert = findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(this   );
    }

    private void initDB() {
        database = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE,  null);
        database.execSQL("create table if not exists " + TABLE_NAME  + " (" +
                "FirstNumber int, " +
                "SecondNumber int, " +
                "Result int)");
        database.delete(TABLE_NAME, null, null);
    }

    @Override
    public void onClick(View v) {
        database.delete(TABLE_NAME, null, null);
        long startTime = System.currentTimeMillis();
        insertRecords();
        long diff = System.currentTimeMillis() - startTime;
        tvTime.setText("Time: " + Long.toString(diff) + " ms");
    }

    private void insertRecords() {
        String sql =  "insert into " + TABLE_NAME + " values(?, ?, ?);";
        SQLiteStatement sqLiteStatement = database.compileStatement(sql);
        database.beginTransaction();
        try {
            for (int i = 0; i < 1000; i++) {
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, i);
                sqLiteStatement.bindLong(2, i);
                sqLiteStatement.bindLong(3, i + i);
                sqLiteStatement.execute();
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
