package com.anna.sent.soft.childbirthdate.actions;

import com.anna.sent.soft.action.MarketActionActivity;
import com.anna.sent.soft.childbirthdate.R;

public class MarketChildbirthDateActionActivity extends MarketActionActivity {
    @Override
    protected String getAppName() {
        return "com.anna.sent.soft.childbirthdate";
    }

    @Override
    protected int getErrorStringResourceId() {
        return R.string.market_app_not_available;
    }
}
