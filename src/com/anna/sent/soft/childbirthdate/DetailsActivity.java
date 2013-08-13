package com.anna.sent.soft.childbirthdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.anna.sent.soft.childbirthdate.adapters.DetailsPagerAdapter;
import com.anna.sent.soft.childbirthdate.base.ChildActivity;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public class DetailsActivity extends ChildActivity implements
		ViewPager.OnPageChangeListener {
	private ViewPager mViewPager;
	private DetailsPagerAdapter mTabsAdapter;
	private int mIndex = 0;

	@Override
	public void setViews(Bundle savedInstanceState) {
		if (getResources().getBoolean(R.bool.has_two_panes)) {
			restoreState(savedInstanceState);
			setResult();
			finish();
			return;
		}

		setContentView(R.layout.activity_details);
		super.setViews(savedInstanceState);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(this);
		mTabsAdapter = new DetailsPagerAdapter(this,
				getSupportFragmentManager());
	}

	@Override
	public void restoreState(Bundle state) {
		mIndex = state.getInt(Shared.Titles.EXTRA_POSITION, 0);
	}

	@Override
	public void beforeOnSaveInstanceState() {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < mTabsAdapter.getCount(); ++i) {
			Fragment details = mTabsAdapter.getFragment(i);
			if (details != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.detach(details);
				ft.commit();
			}
		}
	}

	@Override
	protected void saveActivityState(Bundle state) {
		state.putInt(Shared.Titles.EXTRA_POSITION, mIndex);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mViewPager.setAdapter(mTabsAdapter);
		mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount() - 1);
		mViewPager.setCurrentItem(mIndex);
	}

	@Override
	protected void onStop() {
		mViewPager.setAdapter(null);
		super.onStop();
	}

	private void setResult() {
		Intent data = new Intent();
		data.putExtra(Shared.Titles.EXTRA_POSITION, mIndex);
		setResult(RESULT_OK, data);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		mIndex = arg0;
		setResult();
	}
}