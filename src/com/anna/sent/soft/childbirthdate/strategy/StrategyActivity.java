package com.anna.sent.soft.childbirthdate.strategy;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class StrategyActivity extends FragmentActivity {
	private List<Strategy> mStrategies = new ArrayList<Strategy>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addStrategies();

		for (int i = 0; i < mStrategies.size(); ++i) {
			mStrategies.get(i).onCreate(savedInstanceState);
		}
	}

	protected void addStrategies() {
	}

	protected final void addStrategy(Strategy strategy) {
		mStrategies.add(strategy);
	}

	protected final void addStrategy(int location, Strategy strategy) {
		mStrategies.add(location, strategy);
	}

	protected final void removeStrategy(int location) {
		mStrategies.remove(location);
	}

	@Override
	protected void onStart() {
		super.onStart();

		for (int i = 0; i < mStrategies.size(); ++i) {
			mStrategies.get(i).onStart();
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		for (int i = 0; i < mStrategies.size(); ++i) {
			mStrategies.get(i).onRestoreInstanceState(savedInstanceState);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		for (int i = 0; i < mStrategies.size(); ++i) {
			mStrategies.get(i).onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		for (int i = 0; i < mStrategies.size(); ++i) {
			mStrategies.get(i).onPause();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		for (int i = 0; i < mStrategies.size(); ++i) {
			mStrategies.get(i).onSaveInstanceState(outState);
		}

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		for (int i = 0; i < mStrategies.size(); ++i) {
			mStrategies.get(i).onStop();
		}

		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		for (int i = 0; i < mStrategies.size(); ++i) {
			mStrategies.get(i).onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		for (int i = 0; i < mStrategies.size(); ++i) {
			boolean processed = mStrategies.get(i).onCreateOptionsMenu(menu);
			if (processed) {
				return true;
			}
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int i = 0; i < mStrategies.size(); ++i) {
			boolean processed = mStrategies.get(i).onPrepareOptionsMenu(menu);
			if (processed) {
				return true;
			}
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		for (int i = 0; i < mStrategies.size(); ++i) {
			boolean processed = mStrategies.get(i).onOptionsItemSelected(item);
			if (processed) {
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}
}