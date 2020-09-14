package com.example.rtu_ble;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Button sendDataResetMsg;
    private Button sendBasicConfigMsg;
    private Button sendRunConfigMsg;

    private Button query;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        sendDataResetMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FactoryDataResetActivity.class);
                startActivity(intent);
            }
        });

        sendBasicConfigMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasicConfigActivity.class);
                startActivity(intent);
            }
        });

        sendRunConfigMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RunConfigActivity.class);
                startActivity(intent);
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BleActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);   //屏幕顶端绿框
        setSupportActionBar(toolbar);
        toolbar.setTitle("rtu");
        sendDataResetMsg = (Button)findViewById(R.id.button_data_reset);
        sendBasicConfigMsg = (Button)findViewById(R.id.button_basic_config);
        sendRunConfigMsg = (Button)findViewById(R.id.button_run_config);
        query = (Button)findViewById(R.id.button_query);
    }
}
