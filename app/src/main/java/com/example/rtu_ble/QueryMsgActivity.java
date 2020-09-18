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

/**
 * 查询数据
 */
public class QueryMsgActivity extends AppCompatActivity {

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

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_msg);
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
                                    addText(tvQueryMsg, HexUtil.formatHexString(data, false));
                                }
                            });
                        }
                    });
        }

        init();

        queryMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = IncreaseIdUtil.getId();
                String sysTime = TimeUtil.getSystemTime();
                String msgFirst = FRAM_START + ADDRESS + FUN_CODE + FIXED_80;
                String msgMiddle = id + sysTime;
                String len = MsgUtil.getMsgLength(msgMiddle);
                String msg = msgFirst + len + MSG_START + msgMiddle + MSG_END;

                if (msg.length() / 2 == 1) {
                    tvQueryMsg.setText("输入有误");
                } else {
                    String crc = CRCUtil.getCRC16(msg);
                    String msgCrc = msg + crc;
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
                                            Toast.makeText(QueryMsgActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                                        }
                                        i++;
                                    }

                                    @Override
                                    public void onWriteFailure(final BleException exception) {
                                        Toast.makeText(QueryMsgActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_query_msg);
        toolbar.setTitle("查询");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        queryMsgButton = (Button)findViewById(R.id.button_query_msg);
        tvQueryMsg = (TextView)findViewById(R.id.tv_custom_temp);
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