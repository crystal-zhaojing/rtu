package com.example.rtu_ble;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button sendDataResetMsg;
    private Button sendBasicConfigMsg;

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
    }

    public void init() {
        sendDataResetMsg = (Button)findViewById(R.id.button_data_reset);
        sendBasicConfigMsg = (Button)findViewById(R.id.button_basic_config);
    }
}
