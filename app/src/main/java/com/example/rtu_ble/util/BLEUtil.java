package com.example.rtu_ble.util;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class BLEUtil extends Fragment {
    private static boolean indicateFlag = false;
    /**
     * @param data 发往蓝牙设备的字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void writeToBLEDevice(String data, String registerServiceUUID, String registerCharacteristic) {
        String hexData = str2HexStr(data);
        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        if (bleDeviceList.size() != 0) {

            /*
            因FastBle框架里的write设置了强行分包处理，故调用原始BLE给设备发送信息
             */
            List<BluetoothGattService> listBluetoothGattServices = BleManager.getInstance()
                    .getBluetoothGattServices(bleDeviceList.get(0));
            for (int i = 0; i < listBluetoothGattServices.size(); i++) {
                if (listBluetoothGattServices.get(i).getUuid().toString().equals(registerServiceUUID)) {
                    List<BluetoothGattCharacteristic> listBluetoothGattCharacteristics =
                            listBluetoothGattServices.get(i).getCharacteristics();
                    for (int j = 0; j < listBluetoothGattCharacteristics.size(); j++) {
                        if (listBluetoothGattCharacteristics.get(j).getUuid().toString().equals(registerCharacteristic)) {
                            listBluetoothGattCharacteristics.get(j).setValue(HexUtil.hexStringToBytes(hexData));
                            BleManager.getInstance().getBluetoothGatt(bleDeviceList.get(0))
                                    .writeCharacteristic(listBluetoothGattCharacteristics.get(j));
                        }
                    }
                }
            }
        }
    }

    /**
     * @param str 要转换成十六进制的字符串
     * @return 十六进制字符串
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 打开通知
     * @param service
     * @param indicateCharacteristic
     */
    public static boolean openIndicate(String service, String indicateCharacteristic) {
        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        if (bleDeviceList.size() != 0) {
            BleManager.getInstance().indicate(
                    bleDeviceList.get(0),
                    service,
                    indicateCharacteristic,
                    new BleIndicateCallback() {

                        @Override
                        public void onIndicateSuccess() {
                            indicateFlag = true;
                        }

                        @Override
                        public void onIndicateFailure(final BleException exception) {

                        }

                        @Override
                        public void onCharacteristicChanged(byte[] data) {

                        }
                    });
        }
        return indicateFlag;
    }

}
