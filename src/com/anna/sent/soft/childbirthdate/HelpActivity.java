package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.anna.sent.soft.childbirthdate.adapters.HelpPagerAdapter;
import com.anna.sent.soft.utils.ChildActivity;

public class HelpActivity extends ChildActivity {
	private ViewPager mViewPager;
	private HelpPagerAdapter mTabsAdapter;

	@Override
	public void setViews(Bundle savedInstanceState) {
		super.setViews(savedInstanceState);
		setContentView(R.layout.activity_details);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTabsAdapter = new HelpPagerAdapter(getSupportFragmentManager(),
				getResources().getStringArray(R.array.HelpParts));
		mViewPager.setAdapter(mTabsAdapter);
		mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount() - 1);
		mViewPager.setCurrentItem(0);
	}
}