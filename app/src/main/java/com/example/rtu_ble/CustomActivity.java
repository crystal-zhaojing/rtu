package com.example.rtu_ble;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomActivity extends AppCompatActivity {

    private Button queryMsgButton;
    private TextView tvQueryMsg;

    private final String FRAM_START = "7E7E";
    private final String FUN_CODE = "01123437";
    private final String FIXED_80 = "80";
    private final String MSG_START = "02";
    private final String MSG_END = "05";
    private final String END = "0D0A";

    private final String ADDRESS = "6666668888";

    private static final String SERVICE = "0000a002-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_WRITE = "0000c304-0000-1000-8000-00805f9b34fb";
    private final String CHARACTERISTIC_INDICATE = "0000c306-0000-1000-8000-00805f9b34fb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

    }
}