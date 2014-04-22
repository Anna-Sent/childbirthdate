package com.anna.sent.soft.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class StrategyActivity extends FragmentActivity {
	private Map<String, Strategy> mStrategies = new HashMap<String, Strategy>();
	private List<String> mKeys = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addStrategies();

		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onCreate(savedInstanceState);
		}
	}

	protected void addStrategies() {
	}

	protected final void addStrategy(BaseStrategy strategy) {
		String key = strategy.getName();
		mStrategies.put(key, strategy);
		if (!mKeys.contains(key)) {
			mKeys.add(key);
		}
	}

	protected final Strategy getStrategy(String name) {
		return mStrategies.get(name);
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onRestart();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onStart();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onRestoreInstanceState(savedInstanceState);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onPause();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onSaveInstanceState(outState);
		}

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onStop();
		}

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onStop();
		}

		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onConfigurationChanged(newConfig);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			boolean processed = getStrategy(key).onCreateOptionsMenu(menu);
			if (processed) {
				return true;
			}
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			boolean processed = getStrategy(key).onPrepareOptionsMenu(menu);
			if (processed) {
				return true;
			}
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			boolean processed = getStrategy(key).onOptionsItemSelected(item);
			if (processed) {
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}
}