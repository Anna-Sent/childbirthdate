package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.anna.sent.soft.childbirthdate.gui.DetailsPagerAdapter;
import com.anna.sent.soft.utils.Utils;

public class DetailsActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private DetailsPagerAdapter mTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Utils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);

		if (getResources().getBoolean(R.bool.has_two_panes)) {
			// If the screen is now in landscape mode, we can show the
			// dialog in-line with the list so we don't need this activity.
			finish();
			return;
		}

		setContentView(R.layout.activity_details);

		mTabsAdapter = new DetailsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mTabsAdapter);
		// String title = getIntent().getStringExtra("label");
		int index = getIntent().getIntExtra("index", 0);
		mViewPager.setCurrentItem(index);
	}
}