package com.anna.sent.soft.childbirthdate.gui;

import com.anna.sent.soft.childbirthdate.fragments.DetailsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class DetailsPagerAdapter extends MyFragmentPagerAdapter {
	private String[] titles;

	public DetailsPagerAdapter(FragmentManager fm, String[] titles) {
		super(fm);
		this.titles = titles;
	}

	@Override
	public Fragment getItem(int arg0) {
		return DetailsFragment.newInstance(arg0);
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
}
