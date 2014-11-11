package com.anna.sent.soft.childbirthdate.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.ActionBarUtils;
import com.anna.sent.soft.utils.NavigationUtils;

public class ChildActivity extends DataKeeperActivity {
	@Override
	public void setViews(Bundle savedInstanceState) {
		ActionBarUtils.setupActionBar(this);
	}

	@Override
	public void onBackPressed() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			boolean isStartedFromWidget = getIntent().getBooleanExtra(
					Shared.Child.EXTRA_IS_STARTED_FROM_WIDGET, false);
			if (isStartedFromWidget) {
				TaskStackBuilder tsb = TaskStackBuilder.create(this)
						.addParentStack(this);
				tsb.startActivities();
			}
		}

		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavigationUtils.navigateUp(this);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}