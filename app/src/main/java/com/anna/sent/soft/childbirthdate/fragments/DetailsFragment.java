package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.childbirthdate.data.DataClient;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.logging.MyLog;

public abstract class DetailsFragment extends Fragment implements DataClient {
    @SuppressWarnings("WeakerAccess")
    protected Data mData;
    private OnDetailsChangedListener mListener;

    public static DetailsFragment newInstance(int index) {
        DetailsFragment details;
        switch (index + 1) {
            case Shared.Calculation.BY_LMP:
                details = new DetailsLmpMethodFragment();
                break;
            case Shared.Calculation.BY_OVULATION:
                details = new DetailsOvulationMethodFragment();
                break;
            case Shared.Calculation.BY_ULTRASOUND:
                details = new DetailsUltrasoundMethodFragment();
                break;
            case Shared.Calculation.BY_FIRST_APPEARANCE:
                details = new DetailsFirstAppearanceMethodFragment();
                break;
            case Shared.Calculation.BY_FIRST_MOVEMENTS:
                details = new DetailsFirstMovementsMethodFragment();
                break;
            default:
                return null;
        }

        Bundle args = new Bundle();
        args.putInt(Shared.Titles.EXTRA_POSITION, index);
        details.setArguments(args);
        return details;
    }

    private String wrapMsg(String msg) {
        return getClass().getSimpleName() + ": " + msg;
    }

    private void log(String msg) {
        MyLog.getInstance().logcat(Log.DEBUG, wrapMsg(msg));
    }

    public void setOnDetailsChangedListener(OnDetailsChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void setData(Data data) {
        mData = data;
    }

    public int getShownIndex() {
        return getArguments() == null ? -1 : getArguments().getInt(Shared.Titles.EXTRA_POSITION, -1);
    }

    void dataChanged() {
        log("data changed");
        if (mListener != null) {
            mListener.detailsChanged();
        }
    }

    protected abstract void updateData();

    @Override
    public void onResume() {
        super.onResume();
        log("resume, update ui");
        updateData();
    }

    public interface OnDetailsChangedListener {
        void detailsChanged();
    }
}
