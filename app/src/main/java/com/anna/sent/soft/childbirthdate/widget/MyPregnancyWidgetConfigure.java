package com.anna.sent.soft.childbirthdate.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.MainActivity;
import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.base.CbdActivity;
import com.anna.sent.soft.childbirthdate.data.DataImpl;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public abstract class MyPregnancyWidgetConfigure extends CbdActivity implements
        OnClickListener {
    private static final String KEY_CHECKED_RADIO_INDEX = "checkedRadioIndex";
    private final RadioButton[] radio = new RadioButton[Shared.Calculation.METHODS_COUNT];
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private CheckBox checkBoxCountdown, checkBoxShowCalculationMethod;
    private boolean doCalculation = false;

    protected abstract int getTitleStringResourceId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getTitleStringResourceId());
        setContentView(R.layout.my_pregnancy_widget_configure_layout);
        setResult(RESULT_CANCELED);

        // First, get the App Widget ID from the Intent that launched the
        // Activity:
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Toast.makeText(this, R.string.errorInvalidAppWidgetId,
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Perform App Widget configuration.
        init();
        int defaultValue = getDefaultRadioIndex();
        int radioIndex = defaultValue;
        if (savedInstanceState != null) {
            radioIndex = savedInstanceState.getInt(KEY_CHECKED_RADIO_INDEX,
                    defaultValue);
        }

        if (radioIndex != -1) {
            radio[radioIndex].setChecked(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int radioIndex = getCheckedRadioIndex();
        outState.putInt(KEY_CHECKED_RADIO_INDEX, radioIndex);
    }

    protected abstract Class<?> getWidgetProviderClass();

    private void addWidget() {
        int radioIndex = getCheckedRadioIndex();
        if (radioIndex != -1) {
            // When the configuration is complete, get an instance of the
            // AppWidgetManager
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(this);

            // First
            saveWidgetParams();

            // Second. Update the App Widget with a RemoteViews layout
            RemoteViews views = getBuilder().buildViews(this, mAppWidgetId);
            appWidgetManager.updateAppWidget(mAppWidgetId, views);

            MyPregnancyWidget.installAlarms(this, getWidgetProviderClass());

            // Finally, create the return Intent, set it with the Activity
            // result, and finish the Activity:
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    mAppWidgetId);

            setResult(RESULT_OK, resultValue);

            finish();
        } else {
            Toast.makeText(this,
                    getString(R.string.errorNotSelectedCalculationMethod),
                    Toast.LENGTH_LONG).show();
        }
    }

    protected abstract Builder getBuilder();

    private void startTheApplication() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View arg0) {
        if (arg0.getId() == R.id.widgetConfigureButton) {
            if (doCalculation) {
                addWidget();
            } else {
                startTheApplication();
            }
        }
    }

    protected abstract boolean hasCountdown();

    protected abstract boolean hasShowCalculationMethod();

    private void init() {
        DataImpl data = new DataImpl(this);
        data.update();
        doCalculation = data.thereIsAtLeastOneSelectedMethod();
        TextView textView = findViewById(R.id.widgetConfigureTextView);
        textView.setText(doCalculation ? getString(R.string.widgetSpecifyTheMethodOfCalculation)
                : getString(R.string.widgetStartTheApplicationToSpecifyNecessaryData));
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        String[] methodNames = getResources().getStringArray(
                R.array.methodNames);
        boolean byMethod[] = data.byMethod();
        for (int i = 0; i < radio.length; ++i) {
            radio[i] = new RadioButton(this);
            radio[i].setVisibility(byMethod[i] ? View.VISIBLE : View.GONE);
            radio[i].setText(methodNames[i]);
            radioGroup.addView(radio[i]);
        }

        checkBoxCountdown = findViewById(R.id.checkBoxCountdown);
        checkBoxCountdown.setVisibility(hasCountdown() && doCalculation ? View.VISIBLE : View.GONE);

        checkBoxShowCalculationMethod = findViewById(R.id.checkBoxShowCalculationMethod);
        checkBoxShowCalculationMethod.setVisibility(hasShowCalculationMethod()
                && doCalculation ? View.VISIBLE : View.GONE);

        Button button = findViewById(R.id.widgetConfigureButton);
        button.setText(doCalculation ? getString(R.string.widgetAddWidget) : getString(R.string.widgetStartTheApplication));
        button.setOnClickListener(this);
    }

    private void saveWidgetParams() {
        SharedPreferences settings = Settings.getSettings(this);
        Editor editor = settings.edit();

        int methodIndex = getCheckedRadioIndex() + 1;
        editor.putInt(Shared.Saved.Widget.EXTRA_CALCULATION_METHOD
                + mAppWidgetId, methodIndex);

        editor.putBoolean(Shared.Saved.Widget.EXTRA_COUNTDOWN + mAppWidgetId,
                hasCountdown() && checkBoxCountdown.isChecked());

        editor.putBoolean(Shared.Saved.Widget.EXTRA_SHOW_CALCULATION_METHOD
                + mAppWidgetId, hasShowCalculationMethod()
                && checkBoxShowCalculationMethod.isChecked());

        editor.apply();
    }

    private int getCheckedRadioIndex() {
        for (int i = 0; i < radio.length; ++i) {
            if (radio[i].isChecked()) {
                return i;
            }
        }

        return -1;
    }

    private int getDefaultRadioIndex() {
        for (int i = 0; i < radio.length; ++i) {
            if (radio[i].getVisibility() == View.VISIBLE) {
                return i;
            }
        }

        return -1;
    }
}
