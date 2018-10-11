package com.anna.sent.soft.childbirthdate.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.anna.sent.soft.utils.ActionBarUtils;
import com.anna.sent.soft.utils.NavigationUtils;

@SuppressWarnings("Registered")
public class ChildActivity extends DataKeeperActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarUtils.setupActionBar(this);
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
