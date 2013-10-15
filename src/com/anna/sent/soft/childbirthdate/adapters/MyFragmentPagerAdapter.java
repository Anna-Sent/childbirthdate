package com.anna.sent.soft.childbirthdate.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public abstract class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	private final SparseArray<Fragment> mFragments = new SparseArray<Fragment>();

	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container,
				position);
		mFragments.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		mFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	public Fragment getFragment(int position) {
		return mFragments.get(position);
	}
}
