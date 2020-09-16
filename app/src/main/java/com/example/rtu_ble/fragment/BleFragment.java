package com.example.rtu_ble.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rtu_ble.BleActivity;
import com.example.rtu_ble.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BleFragment extends Fragment{

    private View layout;
    private Button buttonBleConn;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_ble, container, false);

        buttonBleConn = (Button)layout.findViewById(R.id.button_ble_connect);
        buttonBleConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BleActivity.class);
                startActivity(intent);
            }
        });
        return layout;
    }

}
