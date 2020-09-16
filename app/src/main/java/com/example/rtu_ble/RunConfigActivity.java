package com.example.rtu_ble;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.example.rtu_ble.util.CRCUtil;
import com.example.rtu_ble.util.IncreaseIdUtil;
import com.example.rtu_ble.util.MsgUtil;
import com.example.rtu_ble.util.TimeUtil;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RunConfigActivity extends AppCompatActivity {

    private final String FRAM_START = "7E7E";
    private final String FUN_CODE = "42";
    private final String FIXED_80 = "80";
    private final String MSG_START = "02";
    private final String MSG_END = "05";
    private final String END = "0D0A";

    private static final String SERVICE = "0000a002-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_WRITE = "0000c304-0000-1000-8000-00805f9b34fb";
    private final String CHARACTERISTIC_INDICATE = "0000c306-0000-1000-8000-00805f9b34fb";

    private Toolbar toolbar;
    private Button runConfigButton;

    private EditText etRunConfig1;
    private EditText etRunConfig2;
    private EditText etRunConfig3;
    private EditText etRunConfig4;
    private EditText etRunConfig5;
    private EditText etRunConfig6;
    private EditText etRunConfig7;
    private EditText etRunConfig8;
    private EditText etRunConfig9;
    private EditText etRunConfig10;
    private EditText etRunConfig11;
    private EditText etRunConfig12;
    private EditText etRunConfig13;
    private EditText etRunConfig14;
    private EditText etRunConfig15;
    private EditText etRunConfig16;
    private EditText etRunConfig122;
    private EditText etRunConfig133;
    private EditText etRunConfig144;
    private EditText etRunConfig155;
    private EditText etRunConfig166;

    private Spinner spinnerRunConfig1;
    private Spinner spinnerRunConfig2;
    private Spinner spinnerRunConfig3;
    private Spinner spinnerRunConfig4;
    private Spinner spinnerRunConfig5;
    private Spinner spinnerRunConfig6;
    private Spinner spinnerRunConfig7;
    private Spinner spinnerRunConfig8;

    private TextView tvRunConfig;
    private TextView tvRunConfigTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_config);

        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        if (bleDeviceList != null && bleDeviceList.size() != 0) {
            BleManager.getInstance().indicate(
                    bleDeviceList.get(0),
                    SERVICE,
                    CHARACTERISTIC_INDICATE,
                    new BleIndicateCallback() {

                        @Override
                        public void onIndicateSuccess() {

                        }

                        @Override
                        public void onIndicateFailure(final BleException exception) {

                        }

                        @Override
                        public void onCharacteristicChanged(final byte[] data) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addText(tvRunConfigTemp, HexUtil.formatHexString(data, false));
                                }
                            });
                        }
                    });
        }

        init();

        runConfigButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View view) {
                String id = IncreaseIdUtil.getId();
                String sysTime = TimeUtil.getSystemTime();
                String msgFirst = FRAM_START + etRunConfig1.getText().toString()
                        + etRunConfig2.getText().toString() + etRunConfig3.getText().toString()
                        + FUN_CODE + FIXED_80;

                String msgMiddle = id + sysTime
                        + etRunConfig4.getText().toString() + spinnerRunConfig1.getSelectedItem()
                        + etRunConfig5.getText().toString() + spinnerRunConfig2.getSelectedItem()
                        + etRunConfig6.getText().toString() + spinnerRunConfig3.getSelectedItem()
                        + etRunConfig7.getText().toString() + spinnerRunConfig4.getSelectedItem()
                        + etRunConfig8.getText().toString() + spinnerRunConfig5.getSelectedItem()
                        + etRunConfig9.getText().toString() + spinnerRunConfig6.getSelectedItem()
                        + etRunConfig10.getText().toString() + spinnerRunConfig7.getSelectedItem()
                        + etRunConfig11.getText().toString() + spinnerRunConfig8.getSelectedItem()
                        + etRunConfig12.getText().toString() + etRunConfig122.getText().toString()
                        + etRunConfig13.getText().toString() + etRunConfig133.getText().toString()
                        + etRunConfig14.getText().toString() + etRunConfig144.getText().toString()
                        + etRunConfig15.getText().toString() + etRunConfig155.getText().toString()
                        + etRunConfig16.getText().toString() + etRunConfig166.getText().toString();


                String len = MsgUtil.getMsgLength(msgMiddle);
                String msg = msgFirst + len + MSG_START + msgMiddle + MSG_END;
                if (msg.length() % 2 == 1) {
                    tvRunConfig.setText("输入有误");
                } else {
                    String crc = CRCUtil.getCRC16(msg);
                    String msgCrc = msg + crc;
                    msgCrc += END;
                    tvRunConfig.setText(msgCrc);

                    // BLEUtil.writeToBLEDevice(msgCrc, SERVICE, CHARACTERISTIC_WRITE);
                    List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
                    if (bleDeviceList != null && bleDeviceList.size() > 0) {
                        BleManager.getInstance().write(
                                bleDeviceList.get(0),
                                SERVICE,
                                CHARACTERISTIC_WRITE,
                                HexUtil.hexStringToBytes(msgCrc),
                                new BleWriteCallback() {
                                    int i = 1;

                                    @Override
                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                        // Toast.makeText(RunConfigActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                        if (i == 1) {
                                            Toast.makeText(RunConfigActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                        }
                                        i++;
                                    }

                                    @Override
                                    public void onWriteFailure(final BleException exception) {
                                        Toast.makeText(RunConfigActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_run_config);
        toolbar.setTitle("中心站修改遥测站基本配置表");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        runConfigButton = (Button) findViewById(R.id.button_run_config_send);

        etRunConfig1 = (EditText) findViewById(R.id.edit_run_config_1);
        etRunConfig2 = (EditText) findViewById(R.id.edit_run_config_2);
        etRunConfig3 = (EditText) findViewById(R.id.edit_run_config_3);
        etRunConfig4 = (EditText) findViewById(R.id.edit_run_config_4);
        etRunConfig5 = (EditText) findViewById(R.id.edit_run_config_5);
        etRunConfig6 = (EditText) findViewById(R.id.edit_run_config_6);
        etRunConfig7 = (EditText) findViewById(R.id.edit_run_config_7);
        etRunConfig8 = (EditText) findViewById(R.id.edit_run_config_8);
        etRunConfig9 = (EditText) findViewById(R.id.edit_run_config_9);
        etRunConfig10 = (EditText) findViewById(R.id.edit_run_config_10);
        etRunConfig11 = (EditText) findViewById(R.id.edit_run_config_11);
        etRunConfig12 = (EditText) findViewById(R.id.edit_run_config_12);
        etRunConfig13 = (EditText) findViewById(R.id.edit_run_config_13);
        etRunConfig14 = (EditText) findViewById(R.id.edit_run_config_14);
        etRunConfig15 = (EditText) findViewById(R.id.edit_run_config_15);
        etRunConfig16 = (EditText) findViewById(R.id.edit_run_config_16);
        etRunConfig122 = (EditText) findViewById(R.id.edit_run_config_122);
        etRunConfig133 = (EditText) findViewById(R.id.edit_run_config_133);
        etRunConfig144 = (EditText) findViewById(R.id.edit_run_config_144);
        etRunConfig155 = (EditText) findViewById(R.id.edit_run_config_155);
        etRunConfig166 = (EditText) findViewById(R.id.edit_run_config_166);

        spinnerRunConfig1 = (Spinner) findViewById(R.id.spinner_run_config1);
        spinnerRunConfig2 = (Spinner) findViewById(R.id.spinner_run_config2);
        spinnerRunConfig3 = (Spinner) findViewById(R.id.spinner_run_config3);
        spinnerRunConfig4 = (Spinner) findViewById(R.id.spinner_run_config4);
        spinnerRunConfig5 = (Spinner) findViewById(R.id.spinner_run_config5);
        spinnerRunConfig6 = (Spinner) findViewById(R.id.spinner_run_config6);
        spinnerRunConfig7 = (Spinner) findViewById(R.id.spinner_run_config7);
        spinnerRunConfig8 = (Spinner) findViewById(R.id.spinner_run_config8);

        tvRunConfig = (TextView) findViewById(R.id.tv_run_config_msg);
        tvRunConfigTemp = (TextView) findViewById(R.id.tv_run_config_temp);
    }

    private void addText(TextView textView, String content) {
        textView.append(content);
        textView.append("\n");
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        }
    }
}