package com.anna.sent.soft.childbirthdate.strategy.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.HelpActivity;
import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.SettingsActivity;
import com.anna.sent.soft.childbirthdate.actions.EmailDataActionActivity;
import com.anna.sent.soft.strategy.base.BaseStrategy;

public class MenuStrategy extends BaseStrategy {
    private Activity mActivity;

    public MenuStrategy(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mActivity != null) {
            mActivity.getMenuInflater().inflate(R.menu.main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendData:
                if (mActivity != null) {
                    Intent sendData = new Intent(mActivity, EmailDataActionActivity.class);
                    mActivity.startActivity(sendData);
                }

                return true;
            case R.id.settings:
                if (mActivity != null) {
                    mActivity.startActivity(new Intent(mActivity, SettingsActivity.class));
                }

                return true;
            case R.id.help:
                if (mActivity != null) {
                    Intent help = new Intent(mActivity, HelpActivity.class);
                    mActivity.startActivity(help);
                }

                return true;
            default:
                return false;
        }
    }

    @Override
    public void release() {
        mActivity = null;
    }
}
