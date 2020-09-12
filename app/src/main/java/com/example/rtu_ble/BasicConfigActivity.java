package com.example.rtu_ble;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rtu_ble.util.CRCUtil;
import com.example.rtu_ble.util.IncreaseIdUtil;
import com.example.rtu_ble.util.MsgUtil;
import com.example.rtu_ble.util.TimeUtil;

import androidx.appcompat.app.AppCompatActivity;

public class BasicConfigActivity extends AppCompatActivity {

    private final String FRAM_START = "7E7E";
    private final String FUN_CODE = "40";
    private final String FIXED_80 = "80";
    private final String MSG_START = "02";
    private final String ADDRESS_FLAG = "0228";
    private final String FIXED_MASTER = "0405";
    private final String FIXED_SLAVE = "0550";
    private final String MSG_END = "05";

    private EditText etBasicConfig1;
    private EditText etBasicConfig2;
    private EditText etBasicConfig3;
    private EditText etBasicConfig5;
    private EditText etBasicConfig6;
    private EditText etBasicConfig7;
    private TextView tvBasicConfig;

    private Button basicConfigButton;
    private Spinner spinner1;
    private Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_config);

        init();

       basicConfigButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String id = IncreaseIdUtil.getId();
               String sysTime = TimeUtil.getSystemTime();
               String msgFirst = FRAM_START + etBasicConfig1.getText().toString()
                       + etBasicConfig2.getText().toString() + etBasicConfig3.getText().toString()
                       + FUN_CODE + FIXED_80;

               String msgMiddle = id + sysTime + ADDRESS_FLAG + etBasicConfig5.getText().toString()
                       + FIXED_MASTER + spinner1.getSelectedItem() + etBasicConfig6.getText().toString()
                       + FIXED_SLAVE + spinner2.getSelectedItem() + etBasicConfig7.getText().toString();


               String len = MsgUtil.getMsgLength(msgMiddle);
               String msg = msgFirst + len + MSG_START + msgMiddle + MSG_END;
               if (msg.length() / 2 == 1) {
                   tvBasicConfig.setText("输入有误");
               } else {
                   String crc = CRCUtil.getCRC16(msg);
                   String msgCrc = msg + crc;
                   tvBasicConfig.setText(msgCrc);
               }
           }
       });

    }

    public void init() {
        etBasicConfig1 = (EditText)findViewById(R.id.edit_basic_config_1);
        etBasicConfig2 = (EditText)findViewById(R.id.edit_basic_config_2);
        etBasicConfig3 = (EditText)findViewById(R.id.edit_basic_config_3);
        etBasicConfig5 = (EditText)findViewById(R.id.edit_basic_config_5);
        etBasicConfig6 = (EditText)findViewById(R.id.edit_basic_config_6);
        etBasicConfig7 = (EditText)findViewById(R.id.edit_basic_config_7);
        tvBasicConfig = (TextView)findViewById(R.id.tv_basic_config_msg);
        basicConfigButton = (Button)findViewById(R.id.button_basic_config_send);
        spinner1 = (Spinner)findViewById(R.id.spinner_basic_config1);
        spinner2 = (Spinner)findViewById(R.id.spinner_basic_config2);
    }
}