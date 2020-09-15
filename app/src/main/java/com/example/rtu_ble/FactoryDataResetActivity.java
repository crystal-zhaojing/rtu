package com.example.rtu_ble;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FactoryDataResetActivity extends AppCompatActivity {

    private final String FRAM_START = "7E7E";
    private final String FUN_CODE = "00445566776204D248";
    private final String FIXED_80 = "80";
    private final String FIXED_9800 = "9800";
    private final String MSG_START = "02";
    private final String MSG_END = "05";
    private final String END = "0D0A";

    private static final String SERVICE = "0000a002-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_WRITE = "0000c304-0000-1000-8000-00805f9b34fb";
    private final String CHARACTERISTIC_INDICATE = "0000c306-0000-1000-8000-00805f9b34fb";

    private TextView tvReset;
    private TextView tvResetTemp;
    private Button sendButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_data_reset);

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
                                    addText(tvResetTemp, HexUtil.formatHexString(data, false));
                                }
                            });
                        }
                    });
        }

        init();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = IncreaseIdUtil.getId();
                String sysTime = TimeUtil.getSystemTime();
                String msgFirst = FRAM_START + FUN_CODE + FIXED_80;

                String msgMiddle = id + sysTime + FIXED_9800;


                String len = MsgUtil.getMsgLength(msgMiddle);
                String msg = msgFirst + len + MSG_START + msgMiddle + MSG_END;

                String crc = CRCUtil.getCRC16(msg);
                String msgCrc = msg + crc;
                tvReset.setText(msgCrc);

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
                                        Toast.makeText(FactoryDataResetActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                    }
                                    i++;
                                }

                                @Override
                                public void onWriteFailure(final BleException exception) {
                                    Toast.makeText(FactoryDataResetActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                }



            }
        });
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_reset);
        toolbar.setTitle("恢复遥测站出厂设置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvReset = (TextView) findViewById(R.id.tv_reset);
        tvResetTemp = (TextView) findViewById(R.id.tv_reset_temp);
        sendButton = (Button) findViewById(R.id.button_reset_send);
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
