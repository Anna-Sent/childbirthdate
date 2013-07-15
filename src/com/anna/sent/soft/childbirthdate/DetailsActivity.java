package com.anna.sent.soft.childbirthdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.anna.sent.soft.childbirthdate.adapters.DetailsPagerAdapter;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.ChildActivity;

public class DetailsActivity extends ChildActivity implements
		ViewPager.OnPageChangeListener {
	private ViewPager mViewPager;
	private DetailsPagerAdapter mTabsAdapter;
	private static int mIndex = 0;

	@Override
	public void setViews(Bundle savedInstanceState) {
		if (getResources().getBoolean(R.bool.has_two_panes)) {
			// If the screen is now in landscape mode, we can show the
			// dialog in-line with the list so we don't need this activity.
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
		/* Log.d("moo", "details: init index=" + mIndex); */
	}

	@Override
	public void restoreState(Bundle state) {
		mIndex = state.getInt(Shared.Titles.EXTRA_GUI_POSITION);
		/* Log.d("moo", "details: restore index=" + mIndex); */
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
		state.putInt(Shared.Titles.EXTRA_GUI_POSITION, mIndex);
		/* Log.d("moo", "details: save index=" + mIndex); */
	}

	@Override
	protected void onStart() {
		super.onStart();
		mViewPager.setAdapter(mTabsAdapter);
		mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount() - 1);
		mViewPager.setCurrentItem(mIndex);
		/* Log.d("moo", "details: start with index=" + mIndex); */
	}

	@Override
	protected void onStop() {
		mViewPager.setAdapter(null);
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		setResult();
		super.onBackPressed();
	}

	private void setResult() {
		Intent data = new Intent();
		data.putExtra(Shared.Titles.EXTRA_GUI_POSITION, mIndex);
		setResult(RESULT_OK, data);
		/* Log.d("moo", "details: set result index=" + mIndex); */
	}

	@Override
	protected void saveAdditionalData(Bundle state) {
		state.putInt(Shared.Titles.EXTRA_GUI_POSITION, mIndex);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		/* Log.d("moo", "details: update index=" + mIndex); */
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		mIndex = arg0;
		/* Log.d("moo", "details: update index=" + mIndex); */
	}
}