package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.anna.sent.soft.childbirthdate.gui.DetailsPagerAdapter;
import com.anna.sent.soft.utils.ThemeUtils;

public class DetailsActivity extends ChildActivity {
	private ViewPager mViewPager;
	private DetailsPagerAdapter mTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ThemeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);

		if (getResources().getBoolean(R.bool.has_two_panes)) {
			// If the screen is now in landscape mode, we can show the
			// dialog in-line with the list so we don't need this activity.
			finish();
			return;
		}

		setContentView(R.layout.activity_details);

		mTabsAdapter = new DetailsPagerAdapter(getSupportFragmentManager(),
				getResources().getStringArray(R.array.MethodNames));
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mTabsAdapter);
		int index = getIntent().getIntExtra("index", 0);
		mViewPager.setCurrentItem(index);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < 3; ++i) {
			Fragment details = mTabsAdapter.getFragment(i);
			if (details != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.remove(details);
				ft.commit();
			}
		}

		super.onSaveInstanceState(outState);
	}
}