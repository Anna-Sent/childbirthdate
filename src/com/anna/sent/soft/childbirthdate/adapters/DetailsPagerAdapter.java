package com.anna.sent.soft.childbirthdate.adapters;

import com.anna.sent.soft.childbirthdate.fragments.DetailsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class DetailsPagerAdapter extends TitlesPagerAdapter {
	public DetailsPagerAdapter(FragmentManager fm, String[] titles) {
		super(fm, titles);
	}

	@Override
	public Fragment getItem(int position) {
		return DetailsFragment.newInstance(position);
	}
}
