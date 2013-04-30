package com.anna.sent.soft.childbirthdate.adapters;

import com.anna.sent.soft.childbirthdate.fragments.DetailsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class DetailsPagerAdapter extends MyFragmentPagerAdapter {
	private String[] mTitles;

	public DetailsPagerAdapter(FragmentManager fm, String[] titles) {
		super(fm);
		mTitles = titles;
	}

	@Override
	public Fragment getItem(int position) {
		return DetailsFragment.newInstance(position);
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
