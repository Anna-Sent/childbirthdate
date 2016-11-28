package com.anna.sent.soft.childbirthdate.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.fragments.DetailsFragment;

public class DetailsPagerAdapter extends TitlesPagerAdapter {
    public DetailsPagerAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DetailsFragment.newInstance(position);
    }

    @Override
    protected String[] getTitlesFromContext(Context context) {
        return context.getResources().getStringArray(R.array.MethodNames);
    }
}