package com.anna.sent.soft.childbirthdate;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.anna.sent.soft.childbirthdate.adapters.DetailsPagerAdapter;
import com.anna.sent.soft.childbirthdate.base.ChildActivity;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public class DetailsActivity extends ChildActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private DetailsPagerAdapter mTabsAdapter;
    private int mIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            setResult();
            finish();
            return;
        }

        setTitle(R.string.app_name);
        setContentView(R.layout.activity_details);

        mViewPager = findViewById(R.id.pager);
        mViewPager.addOnPageChangeListener(this);
        mTabsAdapter = new DetailsPagerAdapter(this, getSupportFragmentManager());

        mIndex = savedInstanceState == null
                ? 0
                : savedInstanceState.getInt(Shared.Titles.EXTRA_POSITION, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(Shared.Titles.EXTRA_POSITION, mIndex);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewPager.setAdapter(mTabsAdapter);
        mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount() - 1);
        mViewPager.setCurrentItem(mIndex);
    }

    @Override
    protected void onStop() {
        mViewPager.setAdapter(null);
        super.onStop();
    }

    private void setResult() {
        Intent data = new Intent();
        data.putExtra(Shared.Titles.EXTRA_POSITION, mIndex);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        mIndex = arg0;
        setResult();
    }
}
