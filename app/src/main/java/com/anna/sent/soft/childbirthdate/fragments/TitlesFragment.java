package com.anna.sent.soft.childbirthdate.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.anna.sent.soft.childbirthdate.DetailsActivity;
import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.adapters.ListItemArrayAdapter;
import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.childbirthdate.data.DataClient;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.childbirthdate.utils.MyLog;

public class TitlesFragment extends ListFragment implements
        DetailsFragment.OnDetailsChangedListener, ListItemArrayAdapter.OnCheckedListener, DataClient {
    private final static int REQUEST_POSITION = 1;
    private ListItemArrayAdapter mListAdapter;
    private boolean mDualPane;
    private int mSelectedItem = 0;
    private Data mData = null;

    private String wrapMsg(String msg) {
        return getClass().getSimpleName() + ": " + msg;
    }

    private void log(String msg) {
        MyLog.getInstance().logcat(Log.DEBUG, wrapMsg(msg));
    }

    @Override
    public void setData(Data data) {
        mData = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new ListItemArrayAdapter(getActivity(), getStrings1());
        mListAdapter.setOnCheckedListener(this);
        setListAdapter(mListAdapter);

        mDualPane = getResources().getBoolean(R.bool.has_two_panes);

        getListView().setChoiceMode(
                mDualPane ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);

        mSelectedItem = savedInstanceState == null ? 0
                : savedInstanceState.getInt(Shared.Titles.EXTRA_POSITION, 0);
        log("restore index=" + mSelectedItem);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Shared.Titles.EXTRA_POSITION, mSelectedItem);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mDualPane) {
            showDetails();
            log("start with index=" + mSelectedItem);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mSelectedItem = position;
        showDetails();
    }

    private void showDetails() {
        getListView().setItemChecked(mSelectedItem, true);
        log("update index=" + mSelectedItem);
        if (mDualPane) {
            FragmentManager fm = getFragmentManager();
            DetailsFragment details = (DetailsFragment) fm.findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != mSelectedItem) {
                DetailsFragment newDetails = getDetailsFragmentInstance(mSelectedItem);
                if (newDetails != null) {
                    log("update details " + newDetails.getShownIndex());
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(R.id.details, newDetails);
                    ft.commit();
                } else if (details != null) {
                    log("remove details" + details.getShownIndex());
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(details);
                    ft.commit();
                }
            }
        } else {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra(Shared.Titles.EXTRA_POSITION, mSelectedItem);
            startActivityForResult(intent, REQUEST_POSITION);
        }
    }

    /**
     * DO NOT CACHE THESE FRAGMENTS!
     *
     * @param index index of method
     * @return fragment or null
     */
    private DetailsFragment getDetailsFragmentInstance(int index) {
        DetailsFragment result = DetailsFragment.newInstance(index);
        if (result != null) {
            result.setOnDetailsChangedListener(this);
        }

        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_POSITION) {
            if (resultCode == Activity.RESULT_OK) {
                mSelectedItem = data.getIntExtra(Shared.Titles.EXTRA_POSITION,
                        mSelectedItem);
                log("got index=" + mSelectedItem);
                if (mDualPane) {
                    showDetails();
                }
            }
        }
    }

    private String[] getStrings1() {
        return getResources().getStringArray(R.array.MethodNames);
    }

    @Override
    public void detailsChanged() {
        log("details changed, update values");
        if (mData != null) {
            mListAdapter.updateValues(mData.getStrings2());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        log("resume, update ui");
        if (mData != null) {
            mListAdapter.updateValues(mData.byMethod(), mData.getStrings2());
        }
    }

    @Override
    public void checked(int position, boolean isChecked) {
        if (mData != null) {
            mData.byMethod(position, isChecked);
        }
    }
}
