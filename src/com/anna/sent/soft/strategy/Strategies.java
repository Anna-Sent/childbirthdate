package com.anna.sent.soft.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Strategies implements Strategy {
	private Map<String, Strategy> mStrategies = new HashMap<String, Strategy>();
	private List<String> mKeys = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onCreate(savedInstanceState);
		}
	}

	public final void addStrategy(BaseStrategy strategy) {
		String key = strategy.getName();
		mStrategies.put(key, strategy);
		if (!mKeys.contains(key)) {
			mKeys.add(key);
		}
	}

	public final Strategy getStrategy(String name) {
		return mStrategies.get(name);
	}

	@Override
	public void onRestart() {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onRestart();
		}
	}

	@Override
	public void onStart() {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onStart();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onRestoreInstanceState(savedInstanceState);
		}
	}

	@Override
	public void onResume() {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onResume();
		}
	}

	@Override
	public void onPause() {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onPause();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onSaveInstanceState(outState);
		}
	}

	@Override
	public void onStop() {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onStop();
		}
	}

	@Override
	public void onDestroy() {
		for (int i = 0; i < mKeys.size(); ++i) {
			String key = mKeys.get(i);
			getStrategy(key).onStop();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
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

		return false;
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

		return false;
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

		return false;
	}
}