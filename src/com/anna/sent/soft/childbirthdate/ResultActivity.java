package com.anna.sent.soft.childbirthdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TabHost;

import com.anna.sent.soft.childbirthdate.adapters.TabsAdapter;
import com.anna.sent.soft.childbirthdate.fragments.ResultFragment.ResultEcdFragment;
import com.anna.sent.soft.childbirthdate.fragments.ResultFragment.ResultEgaFragment;
import com.anna.sent.soft.utils.ChildActivity;

public class ResultActivity extends ChildActivity {
	private static final String TAG = "moo";
	@SuppressWarnings("unused")
	private static final boolean DEBUG_AD = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg, boolean scenario) {
		if (scenario) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	private static final String EXTRA_GUI_CURRENT_TAB = "com.anna.sent.soft.childbirthdate.currenttab";

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

		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_GUI_CURRENT_TAB)) {
			mTabHost.setCurrentTabByTag(intent
					.getStringExtra(EXTRA_GUI_CURRENT_TAB));
		} else {
			mTabHost.setCurrentTabByTag("ecbd");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
