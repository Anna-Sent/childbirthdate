package com.anna.sent.soft.childbirthdate.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.fragments.TabHelpFragmentFactory;

public class HelpPagerAdapter extends TitlesPagerAdapter {
	public HelpPagerAdapter(Context context, FragmentManager fm) {
		super(context, fm);
	}

	@Override
	public Fragment getItem(int position) {
		return TabHelpFragmentFactory.newInstance(position);
	}

	@Override
	protected String[] getTitlesFromContext(Context context) {
		return context.getResources().getStringArray(R.array.HelpParts);
	}
}
