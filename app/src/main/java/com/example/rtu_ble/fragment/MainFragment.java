package com.example.rtu_ble.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rtu_ble.BasicConfigActivity;
import com.example.rtu_ble.FactoryDataResetActivity;
import com.example.rtu_ble.QueryMsgActivity;
import com.example.rtu_ble.R;
import com.example.rtu_ble.RunConfigActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    private Button sendDataResetMsg;
    private Button sendBasicConfigMsg;
    private Button sendRunConfigMsg;
    private Button query;
    private Toolbar toolbar;

    private View layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_main, container, false);
        init();

        sendDataResetMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FactoryDataResetActivity.class);
                startActivity(intent);
            }
        });

        sendBasicConfigMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BasicConfigActivity.class);
                startActivity(intent);
            }
        });

        sendRunConfigMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RunConfigActivity.class);
                startActivity(intent);
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QueryMsgActivity.class);
                startActivity(intent);
            }
        });


        return layout;
    }

    public void init() {
        toolbar = (Toolbar) layout.findViewById(R.id.toolbar_main);   //屏幕顶端绿框
//        setSupportActionBar(toolbar);
        toolbar.setTitle("rtu");
        sendDataResetMsg = (Button)layout.findViewById(R.id.button_data_reset);
        sendBasicConfigMsg = (Button)layout.findViewById(R.id.button_basic_config);
        sendRunConfigMsg = (Button)layout.findViewById(R.id.button_run_config);
        query = (Button)layout.findViewById(R.id.button_query);
    }
}
