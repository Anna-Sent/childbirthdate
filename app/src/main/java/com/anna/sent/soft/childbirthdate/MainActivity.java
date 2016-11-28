package com.anna.sent.soft.childbirthdate;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.anna.sent.soft.childbirthdate.base.DataKeeperActivity;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.childbirthdate.strategy.menu.MenuStrategy;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidget;

public class MainActivity extends DataKeeperActivity {
    private static final boolean TEST_CLEAR_SETTINGS = false;

    @Override
    public void setViews(Bundle savedInstanceState) {
        if (TEST_CLEAR_SETTINGS) {
            Settings.clear(this);
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void addStrategies() {
        super.addStrategies();
        addStrategy(new MenuStrategy(this));
    }

    @Override
    public void beforeOnSaveInstanceState() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment details = fm.findFragmentById(R.id.details);
        if (details != null) {
            fm.beginTransaction().remove(details).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // update all widgets
        MyPregnancyWidget.updateAllWidgets(this);
    }

    public final static String EXTRA_CONFIGURATION_CHANGED = "extra_configuration_changed";

    @Override
    protected void onNewIntent(Intent intent) {
        log("onNewIntent");
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey(EXTRA_CONFIGURATION_CHANGED)) {
            Bundle state = new Bundle();
            saveState(state);
            finish();
            intent.putExtras(state);
            startActivity(intent);
        }
    }
}