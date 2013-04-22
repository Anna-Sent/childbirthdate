package com.anna.sent.soft.childbirthdate.gui;

import com.anna.sent.soft.childbirthdate.fragments.DetailsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DetailsPagerAdapter extends FragmentPagerAdapter {

	public DetailsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		return DetailsFragment.newInstance(arg0);
	}

	@Override
	public int getCount() {
		return 3;
	}

}
