package com.anna.sent.soft.childbirthdate.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.anna.sent.soft.childbirthdate.data.DataClient;
import com.anna.sent.soft.childbirthdate.data.DataImpl;

@SuppressLint("Registered")
public class DataKeeperActivity extends StateSaverActivity {
    private DataImpl mConcreteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mConcreteData = new DataImpl(this);
        mConcreteData.update();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("resume, update data");
        mConcreteData.update();
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("pause, save data");
        mConcreteData.save();
    }

    @Override
    public final void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        log("attach " + fragment.toString());
        if (fragment instanceof DataClient) {
            DataClient dataClient = (DataClient) fragment;
            dataClient.setData(mConcreteData);
            log("set data");
        }
    }
}