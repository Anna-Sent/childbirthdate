package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;

import com.anna.sent.soft.childbirthdate.adapters.TabsAdapter;
import com.anna.sent.soft.childbirthdate.fragments.ResultFragment.ResultEcdFragment;
import com.anna.sent.soft.childbirthdate.fragments.ResultFragment.ResultEgaFragment;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.ChildActivity;

public class ResultActivity extends ChildActivity {
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	@Override
	public void setViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_result);
		super.setViews(savedInstanceState);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("ecd").setIndicator(
						getString(R.string.estimatedChildbirthDate)),
				ResultEcdFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("ega").setIndicator(
						getString(R.string.estimatedGestationalAge)),
				ResultEgaFragment.class, null);

		int index = getIntent().getIntExtra(Shared.Result.EXTRA_TAB_INDEX, 0);
		mTabHost.setCurrentTab(index);
	}
}
