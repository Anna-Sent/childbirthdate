package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.anna.sent.soft.childbirthdate.base.DataKeeperActivity;
import com.anna.sent.soft.childbirthdate.strategy.menu.MenuStrategy;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidget;

public class MainActivity extends DataKeeperActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    protected void onPause() {
        super.onPause();

        // update all widgets
        MyPregnancyWidget.updateAllWidgets(this);
    }
}
