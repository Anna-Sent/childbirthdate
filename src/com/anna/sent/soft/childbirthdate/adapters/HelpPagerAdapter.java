package com.anna.sent.soft.childbirthdate.adapters;

import com.anna.sent.soft.childbirthdate.fragments.TabHelpFragmentFactory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class HelpPagerAdapter extends TitlesPagerAdapter {
	public HelpPagerAdapter(FragmentManager fm, String[] titles) {
		super(fm, titles);
	}

	@Override
	public Fragment getItem(int position) {
		return TabHelpFragmentFactory.newInstance(position);
	}
}
