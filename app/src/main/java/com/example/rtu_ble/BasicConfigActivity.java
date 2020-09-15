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

public class BasicConfigActivity extends AppCompatActivity {

    private final String FRAM_START = "7E7E";
    private final String FUN_CODE = "40";
    private final String FIXED_80 = "80";
    private final String MSG_START = "02";
    private final String ADDRESS_FLAG = "0228";
    private final String FIXED_MASTER = "0405";
    private final String FIXED_SLAVE = "0550";
    private final String MSG_END = "05";
    private final String END = "0D0A";

    private static final String SERVICE = "0000a002-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_WRITE = "0000c304-0000-1000-8000-00805f9b34fb";
    private final String CHARACTERISTIC_INDICATE = "0000c306-0000-1000-8000-00805f9b34fb";

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

    private TextView tvBasicConfigTemp;
    private Toolbar toolbar;

    private boolean indicateFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_config);

        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        if (bleDeviceList != null && bleDeviceList.size() != 0) {
            BleManager.getInstance().indicate(
                    bleDeviceList.get(0),
                    SERVICE,
                    CHARACTERISTIC_INDICATE,
                    new BleIndicateCallback() {

                        @Override
                        public void onIndicateSuccess() {
                            indicateFlag = true;
//                            Toast.makeText(BasicConfigActivity.this, "通知打开成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onIndicateFailure(final BleException exception) {
//                            Toast.makeText(BasicConfigActivity.this, "通知打开失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCharacteristicChanged(final byte[] data) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addText(tvBasicConfigTemp, HexUtil.formatHexString(data, false));
                                }
                            });
                        }
                    });
        }

        init();

       basicConfigButton.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
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

                   // BLEUtil.writeToBLEDevice(msgCrc + END, SERVICE, CHARACTERISTIC_WRITE);
//                   writeToBle(msgCrc + END);
                   List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
                   if (bleDeviceList != null && bleDeviceList.size() > 0) {
                       BleManager.getInstance().write(
                               bleDeviceList.get(0),
                               SERVICE,
                               CHARACTERISTIC_WRITE,
                               HexUtil.hexStringToBytes(msgCrc + END),
                               new BleWriteCallback() {

                                   @Override
                                   public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                       Toast.makeText(BasicConfigActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                   }

                                   @Override
                                   public void onWriteFailure(final BleException exception) {
                                       Toast.makeText(BasicConfigActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                   }
                               });
                   }
//                   Toast.makeText(BasicConfigActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
               }
           }
       });

    }

    public static void writeToBle(String hex) {
        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        if (bleDeviceList != null && bleDeviceList.size() > 0) {
            BleManager.getInstance().write(
                    bleDeviceList.get(0),
                    SERVICE,
                    CHARACTERISTIC_WRITE,
                    HexUtil.hexStringToBytes(hex),
                    new BleWriteCallback() {

                        @Override
                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {

                        }

                        @Override
                        public void onWriteFailure(final BleException exception) {

                        }
                    });
        }
    }

    private void addText(TextView textView, String content) {
        textView.append(content);
        textView.append("\n");
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        }
    }

    public void init() {
        toolbar = (Toolbar)findViewById(R.id.toolbar_basic_config);
        toolbar.setTitle("中心站修改遥测站运行参数配置表");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etBasicConfig1 = (EditText)findViewById(R.id.edit_basic_config_1);
        etBasicConfig1.setText("0068153333");
        etBasicConfig2 = (EditText)findViewById(R.id.edit_basic_config_2);
        etBasicConfig2.setText("00");
        etBasicConfig3 = (EditText)findViewById(R.id.edit_basic_config_3);
        etBasicConfig3.setText("1234");
        etBasicConfig5 = (EditText)findViewById(R.id.edit_basic_config_5);
        etBasicConfig5.setText("6666666602");
        etBasicConfig6 = (EditText)findViewById(R.id.edit_basic_config_6);
        etBasicConfig6.setText("039108190066009999");
        etBasicConfig7 = (EditText)findViewById(R.id.edit_basic_config_7);
        etBasicConfig7.setText("047097218124006666");
        tvBasicConfig = (TextView)findViewById(R.id.tv_basic_config_msg);
        basicConfigButton = (Button)findViewById(R.id.button_basic_config_send);
        spinner1 = (Spinner)findViewById(R.id.spinner_basic_config1);
        spinner2 = (Spinner)findViewById(R.id.spinner_basic_config2);
        tvBasicConfigTemp = (TextView)findViewById(R.id.tv_basic_config_temp);
    }
}