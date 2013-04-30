package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.anna.sent.soft.childbirthdate.adapters.HelpPagerAdapter;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.ChildActivity;

public class HelpActivity extends ChildActivity implements
		ViewPager.OnPageChangeListener {
	private ViewPager mViewPager;
	private HelpPagerAdapter mTabsAdapter;
	private int mIndex = 0;

	@Override
	public void setViews(Bundle savedInstanceState) {
		super.setViews(savedInstanceState);
		setContentView(R.layout.activity_details);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(this);
		mTabsAdapter = new HelpPagerAdapter(getSupportFragmentManager(),
				getResources().getStringArray(R.array.HelpParts));
		/* Log.d("moo", "details: init index=" + mIndex); */
		mViewPager.setAdapter(mTabsAdapter);
		mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount() - 1);
		mViewPager.setCurrentItem(mIndex);
	}

	@Override
	public void restoreState(Bundle state) {
		mIndex = state.getInt(Shared.Titles.EXTRA_GUI_POSITION);
		/* Log.d("moo", "details: restore index=" + mIndex); */
	}

	@Override
	public void saveActivityState(Bundle state) {
		state.putInt(Shared.Titles.EXTRA_GUI_POSITION, mIndex);
		/* Log.d("moo", "details: save index=" + mIndex); */
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