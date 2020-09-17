package com.example.rtu_ble;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.example.rtu_ble.util.CRCUtil;
import com.example.rtu_ble.util.IncreaseIdUtil;
import com.example.rtu_ble.util.MsgUtil;
import com.example.rtu_ble.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * （自定义扩展）配置要素报文
 */
public class CustomActivity extends AppCompatActivity {

    private Button queryMsgButton;
    private TextView tvQueryMsg;

    private LinearLayout mainLinerLayout;
    private LinearLayout my_layout;

    private EditText editText1;
    private ArrayList<EditText> editTexts;

    private final String FRAM_START = "7E7E";
    private final String FUN_CODE = "42";
    private final String FIXED_80 = "80";
    private final String MSG_START = "02";
    private final String MSG_END = "05";
    private final String END = "0D0A";
    private static final String SERVICE = "0000a002-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_WRITE = "0000c304-0000-1000-8000-00805f9b34fb";
    private final String CHARACTERISTIC_INDICATE = "0000c306-0000-1000-8000-00805f9b34fb";

    private Button btnSend;
    private Button btnAdd;

    private EditText etCustom1;
    private EditText etCustom2;
    private EditText etCustom3;
    private EditText etCustom4;
    private EditText etCustom5;
    private EditText etCustom6;
    private EditText etCustom7;
    private EditText etCustom8;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        init();

        editTexts = new ArrayList<>();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = IncreaseIdUtil.getId();
                String sysTime = TimeUtil.getSystemTime();
                String msgFirst = FRAM_START + etCustom1.getText().toString()
                        + etCustom2.getText().toString() + etCustom3.getText().toString()
                        + FUN_CODE + FIXED_80;

                String msgMiddle = id + sysTime + etCustom4.getText().toString()
                        + etCustom5.getText().toString() + etCustom6.getText().toString()
                        + etCustom7.getText().toString() + etCustom8.getText().toString();

                if (editTexts != null && editTexts.size() > 0) {
                    for (EditText e : editTexts) {
                        msgMiddle += e.getText().toString();
                    }
                }

                String len = MsgUtil.getMsgLength(msgMiddle);
                String msg = msgFirst + len + MSG_START + msgMiddle + MSG_END;
                if (msg.length() % 2 == 1) {
                    Toast.makeText(CustomActivity.this, "输入有误", Toast.LENGTH_SHORT).show();
                } else {
                    String crc = CRCUtil.getCRC16(msg);
                    String msgCrc = msg + crc;
                    // Log.e("customActivity", msgCrc);
                    List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
                    if (bleDeviceList != null && bleDeviceList.size() > 0) {
                        BleManager.getInstance().write(
                                bleDeviceList.get(0),
                                SERVICE,
                                CHARACTERISTIC_WRITE,
                                HexUtil.hexStringToBytes(msgCrc + END),
                                new BleWriteCallback() {
                                    int i = 1;

                                    @Override
                                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                        if (i == 1) {
                                            Toast.makeText(CustomActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                        }
                                        i++;
                                    }

                                    @Override
                                    public void onWriteFailure(final BleException exception) {
                                        Toast.makeText(CustomActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLinearLayout();
            }
        });


    }

    private void init() {
        etCustom1 = (EditText)findViewById(R.id.edit_custom_config_1);
        etCustom2 = (EditText)findViewById(R.id.edit_custom_config_2);
        etCustom3 = (EditText)findViewById(R.id.edit_custom_config_3);
        etCustom4 = (EditText)findViewById(R.id.edit_custom_config_4);
        etCustom5 = (EditText)findViewById(R.id.edit_custom_config_5);
        etCustom6 = (EditText)findViewById(R.id.edit_custom_config_6);
        etCustom7 = (EditText)findViewById(R.id.edit_custom_config_7);
        etCustom8 = (EditText)findViewById(R.id.edit_custom_config_8);
        btnSend = (Button)findViewById(R.id.button_custom_send);
        btnAdd = (Button)findViewById(R.id.btn_add);
    }

    public void addLinearLayout() {
        my_layout = findViewById(R.id.My_layout);
        LinearLayout t = new LinearLayout(this);
        t.setOrientation(LinearLayout.HORIZONTAL);
        TextView tv = new TextView(this);
        tv.setTextSize(20);
        tv.setText("自定义要素：");
        t.addView(tv);
        EditText et = new EditText(this);
        et.setText("FF");
        et.setId(i);
        et.setWidth(1000);
        editTexts.add(et);
        t.addView(et);
        my_layout.addView(t);
    }

}