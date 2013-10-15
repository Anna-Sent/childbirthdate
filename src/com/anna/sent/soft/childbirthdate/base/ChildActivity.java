package com.anna.sent.soft.childbirthdate.base;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.shared.Shared;

public class ChildActivity extends StateSaverActivity {
	@Override
	public void setViews(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new ActionBarHelper().setupActionBar();
		}
	}

	private class ActionBarHelper {
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void setupActionBar() {
			ActionBar actionBar = getActionBar();
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		}
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
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this)
						.addNextIntentWithParentStack(upIntent)
						.startActivities();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
