package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.anna.sent.soft.childbirthdate.base.ChildActivity;
import com.anna.sent.soft.childbirthdate.fragments.ResultSickListFragment;

public class SickListActivity extends ChildActivity {
	@Override
	public void setViews(Bundle savedInstanceState) {
		super.setViews(savedInstanceState);
		initFragment(new ResultSickListFragment());
	}

	protected void initFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(android.R.id.content, fragment);
		fragmentTransaction.commit();
	}
}