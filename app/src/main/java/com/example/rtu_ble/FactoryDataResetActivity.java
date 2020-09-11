package com.example.rtu_ble;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtu_ble.util.CRCUtil;
import com.example.rtu_ble.util.TimeUtil;

import androidx.appcompat.app.AppCompatActivity;

public class FactoryDataResetActivity extends AppCompatActivity {

    private TextView tvReset1;
    private TextView tvReset2;
    private TextView tvReset3;
    private TextView tvReset4;
    private TextView tvReset5;
    private TextView tvReset7;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_data_reset);

        init();
        tvReset3.setText(TimeUtil.getSystemTime());
        String content = tvReset1.getText().toString() + tvReset2.getText().toString() +
                tvReset3.getText().toString() + tvReset4.getText().toString();
        String crc = CRCUtil.getCRC16(content);


        tvReset5.setText(crc);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = tvReset1.getText().toString() + tvReset2.getText().toString() +
                        tvReset3.getText().toString() + tvReset4.getText().toString();
                String crc = CRCUtil.getCRC16(content);
                tvReset7.setText(content + crc);
                Toast.makeText(FactoryDataResetActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void init() {
        tvReset1 = (TextView)findViewById(R.id.tv_reset_1);
        tvReset2 = (TextView)findViewById(R.id.tv_reset_2);
        tvReset3 = (TextView)findViewById(R.id.tv_reset_3);
        tvReset4 = (TextView)findViewById(R.id.tv_reset_4);
        tvReset5 = (TextView)findViewById(R.id.tv_reset_5);
        tvReset7 = (TextView)findViewById(R.id.tv_reset_7);
        sendButton = (Button)findViewById(R.id.button_reset_send);
    }
}
