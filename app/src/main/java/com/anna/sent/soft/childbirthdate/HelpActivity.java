package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.anna.sent.soft.childbirthdate.adapters.HelpPagerAdapter;
import com.anna.sent.soft.childbirthdate.base.ChildActivity;

public class HelpActivity extends ChildActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.help);
        setContentView(R.layout.activity_details);

        ViewPager viewPager = findViewById(R.id.pager);
        HelpPagerAdapter tabsAdapter = new HelpPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
        viewPager.setOffscreenPageLimit(tabsAdapter.getCount() - 1);
        viewPager.setCurrentItem(0);
    }
}
