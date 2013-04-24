package com.anna.sent.soft.childbirthdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.anna.sent.soft.childbirthdate.adapters.DetailsPagerAdapter;
import com.anna.sent.soft.utils.ChildActivity;

public class DetailsActivity extends ChildActivity implements
		ViewPager.OnPageChangeListener {
	private ViewPager mViewPager;
	private DetailsPagerAdapter mTabsAdapter;
	private int mIndex;

	@Override
	protected void internalOnCreate() {
		super.internalOnCreate();

		if (getResources().getBoolean(R.bool.has_two_panes)) {
			// If the screen is now in landscape mode, we can show the
			// dialog in-line with the list so we don't need this activity.
			finish();
			return;
		}

		setContentView(R.layout.activity_details);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(this);
		mTabsAdapter = new DetailsPagerAdapter(getSupportFragmentManager(),
				getResources().getStringArray(R.array.MethodNames));
		mIndex = 0;
		/* Log.d("moo", "init index=" + mIndex); */
	}

	@Override
	protected void saveState(Bundle state) {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < 3; ++i) {
			Fragment details = mTabsAdapter.getFragment(i);
			if (details != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.detach(details);
				ft.commit();
			}
		}

		state.putInt("index", mIndex);
		/* Log.d("moo", "save index=" + mIndex); */
	}

	@Override
	protected void restoreState(Bundle state) {
		mIndex = state.getInt("index");
		/* Log.d("moo", "restore index=" + mIndex); */
	}

	@Override
	protected void onStart() {
		mViewPager.setAdapter(mTabsAdapter);
		mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount() - 1);
		mViewPager.setCurrentItem(mIndex);
		/* Log.d("moo", "start with index=" + mIndex); */
		super.onStart();
	}

	@Override
	protected void onStop() {
		mViewPager.setAdapter(null);
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Intent data = new Intent();
		data.putExtra("index", mIndex);
		setResult(RESULT_OK, data);
		/* Log.d("moo", "pause with index=" + mIndex); */
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		/* Log.d("moo", "update index=" + mIndex); */
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		mIndex = arg0;
		/* Log.d("moo", "update index=" + mIndex); */
	}
}