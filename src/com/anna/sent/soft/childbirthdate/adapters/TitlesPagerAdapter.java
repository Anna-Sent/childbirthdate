package com.anna.sent.soft.childbirthdate.adapters;

import android.support.v4.app.FragmentManager;

public abstract class TitlesPagerAdapter extends MyFragmentPagerAdapter {
	private String[] mTitles;

	public TitlesPagerAdapter(FragmentManager fm, String[] titles) {
		super(fm);
		mTitles = titles;
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}
}
