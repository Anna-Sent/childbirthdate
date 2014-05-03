package com.anna.sent.soft.childbirthdate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.anna.sent.soft.childbirthdate.adapters.TabsAdapter;
import com.anna.sent.soft.childbirthdate.base.ChildActivity;
import com.anna.sent.soft.childbirthdate.fragments.ResultEcdFragment;
import com.anna.sent.soft.childbirthdate.fragments.ResultSickListFragment;
import com.anna.sent.soft.childbirthdate.strategy.menu.MenuStrategy;

public class ResultsActivity extends ChildActivity implements
		OnTabChangeListener {
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	@SuppressLint("NewApi")
	@Override
	public void setViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_results);
		super.setViews(savedInstanceState);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		mTabsAdapter
				.addTab(mTabHost.newTabSpec(
						ResultEcdFragment.class.getSimpleName()).setIndicator(
						getString(R.string.ecdAndGestationalAge)),
						ResultEcdFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec(
						ResultSickListFragment.class.getSimpleName())
						.setIndicator(getString(R.string.sick_list)),
				ResultSickListFragment.class, null);
		mTabsAdapter.setOnTabChangeListener(this);
	}

	@Override
	protected void addStrategies() {
		super.addStrategies();
		addStrategy(new MenuStrategy(this));
	}

	@Override
	public void onTabChanged(String tabId) {
		if (tabId.equals(ResultSickListFragment.class.getSimpleName())) {
			ResultSickListFragment.showSickListInfoDialog(this);
		}
	}
}